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

package com.glaf.base.modules.sys.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.base.TableModel;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.*;

import com.glaf.base.modules.sys.mapper.*;
import com.glaf.base.modules.sys.model.*;
import com.glaf.base.modules.sys.query.*;
import com.glaf.base.modules.sys.service.TreePermissionService;

@Service("treePermissionService")
@Transactional(readOnly = true)
public class TreePermissionServiceImpl implements TreePermissionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TreePermissionMapper treePermissionMapper;

	protected ITableDataService tableDataService;

	public TreePermissionServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TreePermission> list) {
		for (TreePermission treePermission : list) {
			if (treePermission.getId() == 0) {
				treePermission.setId(idGenerator.nextId("SYS_TREE_PERMISSION"));
				treePermission.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<TreePermission> rows = new ArrayList<TreePermission>(batch_size);

		for (TreePermission bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					treePermissionMapper.bulkInsertTreePermission_oracle(rows);
				} else {
					treePermissionMapper.bulkInsertTreePermission(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				treePermissionMapper.bulkInsertTreePermission_oracle(rows);
			} else {
				treePermissionMapper.bulkInsertTreePermission(rows);
			}
			rows.clear();
		}
	}

	public int count(TreePermissionQuery query) {
		return treePermissionMapper.getTreePermissionCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			treePermissionMapper.deleteTreePermissionById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				treePermissionMapper.deleteTreePermissionById(id);
			}
		}
	}

	public List<Long> getNodeIds(String userId, String privilege) {
		TreePermissionQuery query = new TreePermissionQuery();
		query.userId(userId);
		query.privilege(privilege);
		List<TreePermission> list = treePermissionMapper.getTreePermissions(query);
		List<Long> nodeIds = new ArrayList<Long>();
		for (TreePermission p : list) {
			nodeIds.add(p.getNodeId());
		}
		return nodeIds;
	}

	/**
	 * 获取某个租户某种类型权限的节点集合
	 * 
	 * @param tenantId
	 * @param type
	 * @return
	 */
	public List<Long> getTenantNodeIds(String tenantId, String type) {
		TreePermissionQuery query = new TreePermissionQuery();
		query.tenantId(tenantId);
		query.type(type);
		List<TreePermission> list = treePermissionMapper.getTreePermissions(query);
		List<Long> nodeIds = new ArrayList<Long>();
		for (TreePermission p : list) {
			nodeIds.add(p.getNodeId());
		}
		return nodeIds;
	}

	public TreePermission getTreePermission(Long id) {
		if (id == null) {
			return null;
		}
		TreePermission treePermission = treePermissionMapper.getTreePermissionById(id);
		return treePermission;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTreePermissionCountByQueryCriteria(TreePermissionQuery query) {
		return treePermissionMapper.getTreePermissionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TreePermission> getTreePermissionsByQueryCriteria(int start, int pageSize, TreePermissionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TreePermission> rows = sqlSessionTemplate.selectList("getTreePermissions", query, rowBounds);
		return rows;
	}

	public List<TreePermission> list(TreePermissionQuery query) {
		List<TreePermission> list = treePermissionMapper.getTreePermissions(query);
		return list;
	}

	@Transactional
	public void save(TreePermission treePermission) {
		if (treePermission.getId() == 0) {
			treePermission.setId(idGenerator.nextId("SYS_TREE_PERMISSION"));
			treePermission.setCreateTime(new Date());
			treePermissionMapper.insertTreePermission(treePermission);
		}
	}

	/**
	 * 保存记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveAll(String userId, String type, String privilege, List<TreePermission> treePermissions) {
		TableModel table = new TableModel();
		table.setTableName("SYS_TREE_PERMISSION");
		table.addStringColumn("USERID_", userId);
		table.addStringColumn("TYPE_", type);
		table.addStringColumn("PRIVILEGE_", privilege);
		tableDataService.deleteTableData(table);

		this.bulkInsert(treePermissions);
	}

	/**
	 * 保存记录
	 * 
	 * @return
	 */
	@Transactional
	public void saveTenantAll(String tenantId, String type, String privilege, List<TreePermission> treePermissions) {
		TableModel table = new TableModel();
		table.setTableName("SYS_TREE_PERMISSION");
		table.addStringColumn("TENANTID_", tenantId);
		table.addStringColumn("TYPE_", type);
		table.addStringColumn("PRIVILEGE_", privilege);
		tableDataService.deleteTableData(table);

		this.bulkInsert(treePermissions);
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTreePermissionMapper(TreePermissionMapper treePermissionMapper) {
		this.treePermissionMapper = treePermissionMapper;
	}

}
