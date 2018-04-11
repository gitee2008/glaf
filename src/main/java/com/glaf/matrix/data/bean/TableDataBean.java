/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.matrix.data.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.base.BaseItem;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.identity.User;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.jdbc.QueryHelper;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.service.EntityService;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.UUID32;

import com.glaf.matrix.data.domain.Comment;
import com.glaf.matrix.data.domain.DataModel;
import com.glaf.matrix.data.domain.SqlCriteria;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.domain.TableCorrelation;
import com.glaf.matrix.data.domain.TableSysPermission;
import com.glaf.matrix.data.factory.DataItemFactory;
import com.glaf.matrix.data.helper.SqlCriteriaHelper;
import com.glaf.matrix.data.query.TableSysPermissionQuery;
import com.glaf.matrix.data.service.ITableService;
import com.glaf.matrix.data.service.SqlCriteriaService;
import com.glaf.matrix.data.service.TableCorrelationService;
import com.glaf.matrix.data.service.TableSysPermissionService;
import com.glaf.matrix.data.util.TableAuditDomainFactory;

public class TableDataBean {
	protected final static Log logger = LogFactory.getLog(TableDataBean.class);

	protected EntityService entityService;

	protected IDatabaseService databaseService;

	protected ITableService tableService;

	protected ITablePageService tablePageService;

	protected SqlCriteriaService sqlCriteriaService;

	protected TableCorrelationService tableCorrelationService;

	protected TableSysPermissionService tableSysPermissionService;

	public void audit(LoginContext loginContext, SysTable sysTable, Map<String, Object> dataMap, boolean approval,
			String comment) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		java.sql.Connection conn = null;
		java.sql.PreparedStatement psmt = null;
		try {
			if (dataMap.get("id_") != null && dataMap.get("uuid_") != null) {
				if (approval) {
					dataMap.put("business_status_", 9);
					dataMap.put("approval_", 1);
				} else {
					dataMap.put("business_status_", -1);
					dataMap.put("approval_", -1);
				}

				dataMap.put("approvaldate_", new Date());
				dataMap.put("approver_", loginContext.getActorId());
				// dataMap.put("tenantid_", loginContext.getTenantId());
				dataMap.put("content_", comment);
				dataList.add(dataMap);

				if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
						&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
					int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
					sysTable.setTableName(sysTable.getTableName() + hash);
				}

				conn = DBConnectionFactory.getConnection();
				conn.setAutoCommit(false);
				BatchUpdateBean updateBean = new BatchUpdateBean();
				updateBean.dynamicUpdate(loginContext, conn, sysTable, dataList);
				dataList.clear();

				TableDefinition table = TableAuditDomainFactory.getTableDefinition(sysTable.getTableName() + "_AUDIT");
				dataMap.put("topid_", dataMap.get("id_"));
				dataMap.put("tenantid_", loginContext.getTenantId());
				dataMap.put("organizationid_", loginContext.getOrganizationId());
				dataMap.put("id_", getEntityService().nextId(table.getTableName()));
				dataList.add(dataMap);

				logger.debug("audit table:" + table.getTableName());
				com.glaf.core.jdbc.BulkInsertBean insertBean = new com.glaf.core.jdbc.BulkInsertBean();
				insertBean.bulkInsert(loginContext, conn, table, dataList);

				conn.commit();
				JdbcUtils.close(conn);
			}
		} catch (Exception ex) {
			logger.error(ex);
			// ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	public boolean canUpdate(LoginContext loginContext, SysTable sysTable, String uuid) {
		boolean canUpdate = false;

		String tableName = sysTable.getTableName();
		if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
				&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
			int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
			tableName = sysTable.getTableName() + hash;
		}

		if (loginContext.isSystemAdministrator()) {
			canUpdate = true;
		} else {
			Map<String, Object> rowMap = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uuid", uuid);
			params.put("tenantId", loginContext.getTenantId());
			if (StringUtils.isNotEmpty(uuid)) {
				StringBuilder sqlBuffer = new StringBuilder();
				sqlBuffer.append(
						" select E.ID_ as \"id\", E.CREATEBY_ as \"createby\", E.ORGANIZATIONID_ as \"organizationid\" from ")
						.append(tableName).append(" E ");
				sqlBuffer.append(" where 1=1 ");
				sqlBuffer.append(" and E.UUID_ = #{uuid} ");

				if (loginContext.getOrganizationId() > 0) {
					sqlBuffer.append(" and E.ORGANIZATIONID_ = ").append(loginContext.getOrganizationId());
				}
				if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
					sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
				}
				logger.debug(sqlBuffer.toString());
				logger.debug(params);
				// rowMap = getTablePageService().getOne(sqlBuffer.toString(),
				// params);
				QueryHelper helper = new QueryHelper();
				rowMap = helper.selectOne(sqlBuffer.toString(), params);
			}

			LowerLinkedMap dataMap = new LowerLinkedMap();
			if (rowMap != null && !rowMap.isEmpty()) {
				dataMap.putAll(rowMap);
			}
			if (StringUtils.equals(loginContext.getActorId(), ParamUtils.getString(dataMap, "createby"))) {
				canUpdate = true;
			}
			logger.debug(loginContext.getActorId() + " roles : " + loginContext.getRoles());
			if (loginContext.getRoles() != null && loginContext.getRoles().contains("TenantAdmin")) {
				if (loginContext.getOrganizationId() == ParamUtils.getLong(dataMap, "organizationid")) {
					canUpdate = true;
				}
			}
		}
		return canUpdate;
	}

	public List<Comment> getComments(LoginContext loginContext, SysTable sysTable, long topId) {
		List<Comment> comments = new ArrayList<Comment>();
		if (topId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tenantId", loginContext.getTenantId());
			params.put("topId", topId);

			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.*  from ").append(sysTable.getTableName()).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.TOPID_ = #{topId} ");
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			sqlBuffer.append(" order by E.APPROVALDATE_ asc ");
			List<Map<String, Object>> datalist = getTablePageService().getListData(sqlBuffer.toString(), params);
			if (datalist != null && !datalist.isEmpty()) {
				Map<String, User> userMap = IdentityFactory.getUserMap(loginContext.getTenantId());
				LowerLinkedMap dataMap = new LowerLinkedMap();
				for (Map<String, Object> rowMap : datalist) {
					dataMap.clear();
					dataMap.putAll(rowMap);
					Comment c = new Comment();
					c.setId(ParamUtils.getLong(dataMap, "id_"));
					c.setTopId(ParamUtils.getLong(dataMap, "topid_"));
					c.setApproval(ParamUtils.getInt(dataMap, "approval_"));
					c.setApprovalDate(ParamUtils.getDate(dataMap, "approvaldate_"));
					c.setContent(ParamUtils.getString(dataMap, "content_"));
					c.setOrganizationId(ParamUtils.getLong(dataMap, "organizationid_"));
					c.setTenantId(ParamUtils.getString(dataMap, "tenantid_"));
					c.setUserId(ParamUtils.getString(dataMap, "approver_"));
					if (c.getUserId() != null && userMap.get(c.getUserId()) != null) {
						c.setUsername(userMap.get(c.getUserId()).getName());
					}
					comments.add(c);
				}
			}
		}

		return comments;
	}

	public IDatabaseService getDatabaseService() {
		if (databaseService == null) {
			databaseService = ContextFactory.getBean("databaseService");
		}
		return databaseService;
	}

	public DataModel getDataModel(LoginContext loginContext, SysTable sysTable, String uuid) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("uuid", uuid);
		if (StringUtils.isNotEmpty(uuid)) {
			String tableName = sysTable.getTableName();
			if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
					&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
				int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
				tableName = sysTable.getTableName() + hash;
			}
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.*  from ").append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.UUID_ = #{uuid} ");
			if (loginContext.getOrganizationId() > 0) {
				sqlBuffer.append(" and E.ORGANIZATIONID_ = ").append(loginContext.getOrganizationId());
			}
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			DataModel dataModel = new DataModel();
			dataModel.setDataMap(rowMap);
			this.populate(rowMap, dataModel);
			return dataModel;
		}
		return null;
	}

	public DataModel getDataModelById(LoginContext loginContext, SysTable sysTable, long id) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("id", id);
		if (id > 0) {
			String tableName = sysTable.getTableName();
			if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
					&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
				int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
				tableName = sysTable.getTableName() + hash;
			}
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.*  from ").append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.ID_ = #{id} ");
			if (loginContext.getOrganizationId() > 0) {
				sqlBuffer.append(" and E.ORGANIZATIONID_ = ").append(loginContext.getOrganizationId());
			}
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			DataModel dataModel = new DataModel();
			dataModel.setDataMap(rowMap);
			this.populate(rowMap, dataModel);
			return dataModel;
		}
		return null;
	}

	public EntityService getEntityService() {
		if (entityService == null) {
			entityService = ContextFactory.getBean("entityService");
		}
		return entityService;
	}

	private Map<String, Object> getRowMapById(LoginContext loginContext, SysTable sysTable, long id) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("id", id);
		if (id > 0) {
			String tableName = sysTable.getTableName();
			if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
					&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
				int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
				tableName = sysTable.getTableName() + hash;
			}
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(" select E.*  from ").append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.ID_ = #{id} ");
			if (loginContext.getOrganizationId() > 0) {
				sqlBuffer.append(" and E.ORGANIZATIONID_ = ").append(loginContext.getOrganizationId());
			}
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			return dataMap;
		}
		return null;
	}

	public Map<String, Object> getSimpleRowMap(LoginContext loginContext, SysTable sysTable, String uuid) {
		Map<String, Object> rowMap = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", loginContext.getTenantId());
		params.put("uuid", uuid);
		if (StringUtils.isNotEmpty(uuid)) {
			String tableName = sysTable.getTableName();
			if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
					&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
				int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
				tableName = sysTable.getTableName() + hash;
			}
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append(
					" select E.ID_ as \"id\", E.CREATEBY_ as \"createby\", E.ORGANIZATIONID_ as \"organizationid\" from ")
					.append(tableName).append(" E ");
			sqlBuffer.append(" where 1=1 ");
			sqlBuffer.append(" and E.UUID_ = #{uuid} ");
			if (loginContext.getOrganizationId() > 0) {
				sqlBuffer.append(" and E.ORGANIZATIONID_ = ").append(loginContext.getOrganizationId());
			}
			if (StringUtils.isNotEmpty(loginContext.getTenantId())) {
				sqlBuffer.append(" and E.TENANTID_ = #{tenantId} ");
			}
			rowMap = getTablePageService().getOne(sqlBuffer.toString(), params);
		}
		if (rowMap != null && !rowMap.isEmpty()) {
			LowerLinkedMap dataMap = new LowerLinkedMap();
			dataMap.putAll(rowMap);
			return dataMap;
		}
		return null;
	}

	public SqlCriteriaService getSqlCriteriaService() {
		if (sqlCriteriaService == null) {
			sqlCriteriaService = ContextFactory.getBean("sqlCriteriaService");
		}
		return sqlCriteriaService;
	}

	public TableCorrelationService getTableCorrelationService() {
		if (tableCorrelationService == null) {
			tableCorrelationService = ContextFactory.getBean("tableCorrelationService");
		}
		return tableCorrelationService;
	}

	public ITablePageService getTablePageService() {
		if (tablePageService == null) {
			tablePageService = ContextFactory.getBean("tablePageService");
		}
		return tablePageService;
	}

	public ITableService getTableService() {
		if (tableService == null) {
			tableService = ContextFactory.getBean("tableService");
		}
		return tableService;
	}

	public TableSysPermissionService getTableSysPermissionService() {
		if (tableSysPermissionService == null) {
			tableSysPermissionService = ContextFactory.getBean("tableSysPermissionService");
		}
		return tableSysPermissionService;
	}

	public boolean hasPermission(LoginContext loginContext, SysTable sysTable, String privilege) {
		if (StringUtils.equals(sysTable.getPrivilegeFlag(), "N")) {
			return true;
		}
		if (loginContext.isSystemAdministrator()) {
			return true;
		}
		if (loginContext.getRoles().contains("TenantAdmin")) {
			return true;
		}
		TableSysPermissionQuery query = new TableSysPermissionQuery();
		query.tableId(sysTable.getTableId());
		query.privilegeLike(privilege + "%");
		List<TableSysPermission> list = getTableSysPermissionService().list(query);
		if (list != null && !list.isEmpty()) {
			for (TableSysPermission p : list) {
				switch (p.getGranteeType()) {
				case "role":
					if (loginContext.getRoles().contains(p.getGrantee())) {
						return true;
					}
					break;
				case "group":
					break;
				default:
					break;
				}
			}
		}

		return false;
	}

	public void populate(Map<String, Object> dataMap, DataModel dataModel) {
		dataModel.setId(ParamUtils.getLong(dataMap, "id_"));
		dataModel.setTopId(ParamUtils.getLong(dataMap, "topid_"));
		dataModel.setParentId(ParamUtils.getLong(dataMap, "parentid_"));
		dataModel.setApprover(ParamUtils.getString(dataMap, "approver_"));
		dataModel.setApprovalDate(ParamUtils.getDate(dataMap, "approvaldate_"));
		dataModel.setName(ParamUtils.getString(dataMap, "name_"));
		dataModel.setCode(ParamUtils.getString(dataMap, "code_"));
		dataModel.setDesc(ParamUtils.getString(dataMap, "desc_"));
		dataModel.setDiscriminator(ParamUtils.getString(dataMap, "discriminator_"));
		dataModel.setTitle(ParamUtils.getString(dataMap, "title_"));
		dataModel.setType(ParamUtils.getString(dataMap, "type_"));
		dataModel.setTreeId(ParamUtils.getString(dataMap, "treeid_"));
		dataModel.setGradeId(ParamUtils.getString(dataMap, "gradeid_"));
		dataModel.setOrganizationId(ParamUtils.getLong(dataMap, "organizationid_"));
		dataModel.setTenantId(ParamUtils.getString(dataMap, "tenantid_"));
		dataModel.setIcon(ParamUtils.getString(dataMap, "icon_"));
		dataModel.setIconCls(ParamUtils.getString(dataMap, "iconcls_"));
		dataModel.setLevel(ParamUtils.getInt(dataMap, "level_"));
		dataModel.setSortNo(ParamUtils.getInt(dataMap, "sortno_"));
		dataModel.setBusinessStatus(ParamUtils.getInt(dataMap, "business_status_"));
		dataModel.setCreateBy(ParamUtils.getString(dataMap, "createby_"));
		dataModel.setCreateTime(ParamUtils.getDate(dataMap, "createtime_"));
		dataModel.setUpdateBy(ParamUtils.getString(dataMap, "updateby_"));
		dataModel.setUpdateTime(ParamUtils.getDate(dataMap, "updatetime_"));
		dataModel.setDeleteFlag(ParamUtils.getInt(dataMap, "deleteflag_"));
		dataModel.setDeleteTime(ParamUtils.getDate(dataMap, "deletetime_"));
	}

	public void saveOrUpdate(LoginContext loginContext, SysTable sysTable, Map<String, Object> dataMap) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		java.sql.Connection conn = null;
		java.sql.PreparedStatement psmt = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			if (dataMap.get("id_") != null && dataMap.get("uuid_") != null) {
				int topId = ParamUtils.getInt(dataMap, "topid_");
				if (topId > 0) {
					// TableCorrelationQuery query = new TableCorrelationQuery();
					// query.slaveTableId(sysTable.getTableId());
					List<TableCorrelation> list = getTableCorrelationService()
							.getTableCorrelationsBySlaveTableId(sysTable.getTableId());
					// logger.debug("list:" + list);
					if (list != null && !list.isEmpty()) {
						TableCorrelation tc = list.get(0);
						SysTable masterTable = getTableService().getSysTableById(tc.getMasterTableId());
						Map<String, Object> rowMap2 = this.getRowMapById(loginContext, masterTable, topId);
						if (rowMap2 != null && !rowMap2.isEmpty()) {
							// logger.debug("master data:" + rowMap2);
							int business_status = ParamUtils.getInt(rowMap2, "business_status_");
							if (business_status == 9) {
								return;
							}
						}
					}
				}

				if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
						&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
					int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
					sysTable.setTableName(sysTable.getTableName() + hash);
				}

				dataMap.put("updateby_", loginContext.getActorId());
				dataMap.put("updatetime_", new Date());
				dataMap.remove("uuid_");
				dataMap.remove("createby_");
				dataMap.remove("createtime_");
				dataMap.remove("gradeid_");
				dataMap.remove("organizationid_");
				dataList.add(dataMap);
				BatchUpdateBean updateBean = new BatchUpdateBean();
				updateBean.dynamicUpdate(loginContext, conn, sysTable, dataList);
				conn.commit();
				JdbcUtils.close(conn);
				/**
				 * 判断是否允许上传附件，如果有附件，修改附件状态为正式
				 */
				if ("1".equals(sysTable.getAttachmentFlag()) || "2".equals(sysTable.getAttachmentFlag())) {
					int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
					String sql = " update DATA_FILE" + (hash > 0 ? hash : "")
							+ " set STATUS_ = 1 where SERVICEKEY_ = ? and CREATEBY_ = ? and STATUS_ = 0 ";
					Database database = getDatabaseService().getDatabaseByMapping("file");
					conn = DBConnectionFactory.getConnection(database.getName());
					conn.setAutoCommit(false);
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, sysTable.getTableId());
					psmt.setString(2, loginContext.getActorId());
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
					conn.commit();
					JdbcUtils.close(conn);

					conn = DBConnectionFactory.getConnection();
					conn.setAutoCommit(false);
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, sysTable.getTableId());
					psmt.setString(2, loginContext.getActorId());
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
					conn.commit();
					JdbcUtils.close(conn);
				}
			} else {
				dataMap.put("id_", getEntityService().nextId(sysTable.getTableName()));
				dataMap.put("uuid_", UUID32.getUUID());
				dataMap.put("locked_", 0);
				dataMap.put("sortno_", 0);
				dataMap.put("tenantid_", loginContext.getTenantId());
				dataMap.put("organizationid_", loginContext.getOrganizationId());
				dataMap.put("createby_", loginContext.getActorId());
				dataMap.put("createtime_", new Date());
				dataMap.put("deleteflag_", 0);
				dataList.add(dataMap);

				if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
						&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
					int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
					sysTable.setTableName(sysTable.getTableName() + hash);
				}

				BatchInsertBean insertBean = new BatchInsertBean();
				insertBean.bulkInsert(loginContext, conn, sysTable, dataList);
				conn.commit();
				JdbcUtils.close(conn);

				/**
				 * 判断是否允许上传附件，如果有附件，修改附件状态为正式
				 */
				if ("1".equals(sysTable.getAttachmentFlag()) || "2".equals(sysTable.getAttachmentFlag())) {
					logger.debug("修改附件状态为正式...");
					int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
					String sql = " update DATA_FILE" + (hash > 0 ? hash : "")
							+ " set STATUS_ = 1, BUSINESSKEY_ = ? where SERVICEKEY_ = ? and CREATEBY_ = ? and STATUS_ = 0 ";
					Database database = getDatabaseService().getDatabaseByMapping("file");
					conn = DBConnectionFactory.getConnection(database.getName());
					conn.setAutoCommit(false);
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, ParamUtils.getString(dataMap, "uuid_"));
					psmt.setString(2, sysTable.getTableId());
					psmt.setString(3, loginContext.getActorId());
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
					conn.commit();
					JdbcUtils.close(conn);

					conn = DBConnectionFactory.getConnection();
					conn.setAutoCommit(false);
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, ParamUtils.getString(dataMap, "uuid_"));
					psmt.setString(2, sysTable.getTableId());
					psmt.setString(3, loginContext.getActorId());
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
					conn.commit();
					JdbcUtils.close(conn);
				}
			}

		} catch (Exception ex) {
			logger.error(ex);
			// ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson(HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();
		String tableId = request.getParameter("tableId");
		if (StringUtils.isNotEmpty(tableId)) {
			SysTable sysTable = getTableService().getSysTableById(tableId);
			if (sysTable != null) {
				TableDataBean tableDataBean = new TableDataBean();
				if (!tableDataBean.hasPermission(loginContext, sysTable, Constants.PRIVILEGE_READ)) {
					return result;
				}
				String tableName = sysTable.getTableName();
				if (StringUtils.equals(sysTable.getPartitionFlag(), "Y")
						&& StringUtils.isNotEmpty(loginContext.getTenantId())) {
					int hash = IdentityFactory.getTenantHash(loginContext.getTenantId());
					tableName = sysTable.getTableName() + hash;
				}
				List<TableColumn> columns = getTableService().getTableColumnsByTableId(tableId);
				if (columns != null && !columns.isEmpty()) {
					List<String> list = new ArrayList<String>();
					list.add("id_");
					list.add("parentid_");
					list.add("topid_");
					list.add("treeid_");
					list.add("uuid_");
					list.add("name_");
					list.add("code_");
					list.add("title_");
					list.add("type_");
					list.add("level_");
					list.add("sortno_");
					list.add("locked_");
					list.add("organizationid_");
					list.add("gradeid_");
					list.add("createby_");
					list.add("createtime_");
					list.add("business_status_");
					list.add("approvaldate_");
					list.add("approver_");

					Map<String, TableColumn> columnMap = new HashMap<String, TableColumn>();
					for (TableColumn column : columns) {
						columnMap.put(column.getId(), column);
						columnMap.put(column.getColumnName().toLowerCase(), column);
						list.add(column.getColumnName().toLowerCase());

						if (StringUtils.isNotEmpty(column.getDataCode())) {
							if (StringUtils.startsWith(column.getDataCode(), "@sys_dict:")) {
								long nodeId = Long
										.parseLong(column.getDataCode().substring(10, column.getDataCode().length()));
								List<BaseItem> items = DataItemFactory.getInstance().getDictoryItems(nodeId);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							} else if (StringUtils.startsWith(column.getDataCode(), "@sql:")) {
								String rowId = column.getDataCode().substring(5, column.getDataCode().length());
								List<BaseItem> items = DataItemFactory.getInstance().getSqlDataItems(loginContext,
										Long.parseLong(rowId), params);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							} else if (StringUtils.startsWith(column.getDataCode(), "@table:")) {
								String rowId = column.getDataCode().substring(7, column.getDataCode().length());
								List<BaseItem> items = DataItemFactory.getInstance().getTableItems(loginContext, rowId,
										params);
								column.setItems(items);
								if (items != null && !items.isEmpty()) {
									for (BaseItem item : items) {
										column.addProperty(item.getValue(), item.getName());
									}
								}
							}
						}
					}

					int start = 0;
					int limit = 20;
					String orderName = null;
					String order = null;

					int pageNo = ParamUtils.getInt(params, "page");
					limit = ParamUtils.getInt(params, "rows");
					start = (pageNo - 1) * limit;
					orderName = ParamUtils.getString(params, "sortName");
					order = ParamUtils.getString(params, "sortOrder");

					if (start < 0) {
						start = 0;
					}

					if (limit <= 0) {
						limit = 20;
					}

					if (request.getAttribute("exportXls") != null) {
						limit = 50000;
					}

					StringBuilder sqlBuffer = new StringBuilder();
					sqlBuffer.append(" from ").append(tableName).append(" E ");
					sqlBuffer.append(" where 1=1 ");

					if (sysTable.getAddType() == 9) {
						sqlBuffer.append(" and E.TENANTID_ = '").append(loginContext.getTenantId()).append("' ");
					} else {
						if (!loginContext.isSystemAdministrator()) {
							sqlBuffer.append(" and E.TENANTID_ = '").append(loginContext.getTenantId()).append("' ");

							if (loginContext.getRoles().contains("SeniorManager")
									|| loginContext.getRoles().contains("TenantAdmin")) {
								Collection<Long> subOrganizationIds = loginContext.getSubOrganizationIds();
								if (subOrganizationIds == null) {
									subOrganizationIds = new HashSet<Long>();
								}
								subOrganizationIds.add(loginContext.getOrganizationId());
								// sqlBuffer.append(QueryUtils.getLongParameterSQLCondition(subOrganizationIds,
								// "E","ORGANIZATIONID_"));
							} else {
								if (loginContext.getOrganizationId() > 0) {
									// sqlBuffer.append(" and E.ORGANIZATIONID_
									// =
									// ").append(loginContext.getOrganizationId());
								}

								if (loginContext.getGradeIds() != null && !loginContext.getGradeIds().isEmpty()) {
									sqlBuffer.append(
											QueryUtils.getSQLCondition(loginContext.getGradeIds(), "E", "GRADEID_"));
								} else {
									sqlBuffer.append(" and E.CREATEBY_ = '").append(loginContext.getActorId())
											.append("' ");
								}
							}
						}
					}
					/**
					 * 从表必须有主表的编号
					 */
					if (StringUtils.equals(sysTable.getIsSubTable(), "Y")) {
						long topId = RequestUtils.getLong(request, "topId");
						sqlBuffer.append(" and E.TOPID_ = ").append(topId);
					}

					// SqlCriteriaQuery query = new SqlCriteriaQuery();
					// query.businessKey(sysTable.getTableName());
					// query.moduleId(tableId);
					List<SqlCriteria> sqlCriterias = getSqlCriteriaService().getSqlCriterias(sysTable.getTableName(),
							tableId);
					if (sqlCriterias != null && !sqlCriterias.isEmpty()) {
						for (SqlCriteria col : sqlCriterias) {
							if (StringUtils.equals(col.getColumnType(), "String")) {
								String value = request.getParameter(col.getParamName() + "_enc");
								if (StringUtils.isNotEmpty(value)) {
									params.put(col.getParamName(), RequestUtils.decodeString(value));
								}
							}
						}
					}

					SqlCriteriaHelper helper = new SqlCriteriaHelper();
					SqlExecutor sqlExecutor = helper.buildMyBatisSql(sysTable.getTableId(), sysTable.getTableName(),
							"E", params);
					if (sqlExecutor.getSql() != null && sqlExecutor.getParameter() != null) {
						sqlBuffer.append(sqlExecutor.getSql());
						params.putAll((Map<String, Object>) sqlExecutor.getParameter());
					}

					// logger.debug("sql:" + " select count(*) " +
					// sqlBuffer.toString());
					// logger.debug("params:" + params);
					try {
						int total = getTablePageService().getQueryCount(" select count(*) " + sqlBuffer.toString(),
								params);
						if (total > 0) {
							if (orderName != null && list.contains(orderName.trim().toLowerCase())) {
								sqlBuffer.append(" order by E.").append(orderName);
								if (StringUtils.equalsIgnoreCase(order, "desc")) {
									sqlBuffer.append(" desc ");
								}
							} else {
								if (StringUtils.isNotEmpty(sysTable.getSortColumn())) {
									sqlBuffer.append(" order by E.").append(sysTable.getSortColumn());
									if (StringUtils.isNotEmpty(sysTable.getSortOrder())) {
										sqlBuffer.append(" ").append(sysTable.getSortOrder());
									}
								} else {
									if (sysTable.getAddType() == 9) {
										sqlBuffer.append(" order by E.CREATETIME_ desc ");
									} else {
										sqlBuffer.append(" order by E.SORTNO_ asc, E.CREATETIME_ desc ");
									}
								}
							}
							List<Map<String, Object>> dataList = getTablePageService()
									.getListData(" select E.* " + sqlBuffer.toString(), params, start, limit);
							if (dataList != null && !dataList.isEmpty()) {
								JSONArray rowsJSON = new JSONArray();
								Map<String, Object> dataMap = null;
								int index = start;
								Date date = null;
								for (Map<String, Object> rowMap : dataList) {
									dataMap = new LowerLinkedMap();
									dataMap.putAll(rowMap);
									index++;
									JSONObject json = new JSONObject();
									for (String columnName : list) {
										if (StringUtils.equalsIgnoreCase("id_", columnName)) {
											json.put("id", dataMap.get(columnName));
										}
										if (StringUtils.equalsIgnoreCase("uuid_", columnName)) {
											json.put("uuid", dataMap.get(columnName));
										}
										TableColumn col = columnMap.get(columnName);
										if (col != null && col.getJavaType() != null) {
											switch (col.getJavaType()) {
											case "Date":
												date = ParamUtils.getDate(dataMap, columnName);
												if (date != null) {
													if (StringUtils.equals(col.getFormatter(), "yyyy-MM-dd")) {
														json.put(columnName, DateUtils.getDate(date));
													} else {
														json.put(columnName, DateUtils.getDateTime(date));
													}
												}
												break;
											default:
												if (col.getProperties() != null && !col.getProperties().isEmpty()) {
													json.put(columnName,
															col.getProperties().get(dataMap.get(columnName)));
												} else {
													json.put(columnName, dataMap.get(columnName));
												}
												break;
											}
										} else {
											json.put(columnName, dataMap.get(columnName));
										}
									}
									json.put("startIndex", index);
									rowsJSON.add(json);
								}
								result.put("rows", rowsJSON);
							}
						}

						result.put("total", total);
						result.put("totalCount", total);
						result.put("totalRecords", total);
						result.put("start", start);
						result.put("startIndex", start);
						result.put("limit", limit);
						result.put("pageSize", limit);

					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		}
		// logger.debug(result.toJSONString());
		return result;
	}

}
