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

package com.glaf.heathcare.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.base.TableModel;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GradePrivilege;
import com.glaf.heathcare.mapper.GradeInfoMapper;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.util.GradeInfoJsonFactory;

@Service("com.glaf.heathcare.service.gradeInfoService")
@Transactional(readOnly = true)
public class GradeInfoServiceImpl implements GradeInfoService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GradeInfoMapper gradeInfoMapper;

	protected ITableDataService tableDataService;

	protected ITablePageService tablePageService;

	protected SysUserService sysUserService;

	protected SysTenantService sysTenantService;

	public GradeInfoServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<GradeInfo> list) {
		for (GradeInfo gradeInfo : list) {
			if (StringUtils.isEmpty(gradeInfo.getId())) {
				gradeInfo.setId(UUID32.getUUID());
				gradeInfo.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<GradeInfo> rows = new ArrayList<GradeInfo>(batch_size);

		for (GradeInfo bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					gradeInfoMapper.bulkInsertGradeInfo_oracle(rows);
				} else {
					gradeInfoMapper.bulkInsertGradeInfo(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				gradeInfoMapper.bulkInsertGradeInfo_oracle(rows);
			} else {
				gradeInfoMapper.bulkInsertGradeInfo(rows);
			}
			rows.clear();
		}
	}

	public int count(GradeInfoQuery query) {
		return gradeInfoMapper.getGradeInfoCount(query);
	}

	@Transactional
	public String createTeacher(String createBy, GradeInfo gradeInfo) {
		SysTenant sysTenant = sysTenantService.getSysTenantByTenantId(gradeInfo.getTenantId());
		int seqno = idGenerator.nextId("tenant_" + sysTenant.getId()).intValue();
		String userId = String.valueOf(sysTenant.getId() + StringTools.getDigit4Id(seqno));
		SysUser bean = new SysUser();
		bean.setActorId(userId);
		bean.setPasswordHash(userId);
		bean.setName("教师" + seqno);
		bean.setAccountType(8);
		bean.setUserType(8);
		bean.setEvection(0);
		bean.setCreateBy(createBy);
		bean.setCreateDate(new Date());
		bean.setOrganizationId(sysTenant.getId());
		bean.setTenantId(sysTenant.getTenantId());
		sysUserService.create(bean);

		List<String> userIds = new ArrayList<String>();
		userIds.add(userId);
		sysUserService.saveRoleUsers(sysTenant.getTenantId(), "Teacher", 0, userIds);

		this.saveGradeUsers(gradeInfo.getId(), gradeInfo.getTenantId(), "rw", userIds);

		return userId;
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			gradeInfoMapper.deleteGradeInfoById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				gradeInfoMapper.deleteGradeInfoById(id);
			}
		}
	}

	public GradeInfo getGradeInfo(String id) {
		if (id == null) {
			return null;
		}
		GradeInfo gradeInfo = gradeInfoMapper.getGradeInfoById(id);
		return gradeInfo;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGradeInfoCountByQueryCriteria(GradeInfoQuery query) {
		return gradeInfoMapper.getGradeInfoCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GradeInfo> getGradeInfosByQueryCriteria(int start, int pageSize, GradeInfoQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GradeInfo> rows = sqlSessionTemplate.selectList("getGradeInfos", query, rowBounds);
		return rows;
	}

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	public List<GradeInfo> getGradeInfosByTenantId(String tenantId) {
		String cacheKey = "grade_info_" + tenantId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("grade_info", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return GradeInfoJsonFactory.arrayToList(array);
				} catch (Exception ex) {
				}
			}
		}
		GradeInfoQuery query = new GradeInfoQuery();
		query.tenantId(tenantId);
		List<GradeInfo> list = gradeInfoMapper.getGradeInfos(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("grade_info", cacheKey, GradeInfoJsonFactory.listToArray(list).toJSONString());
			}
		}
		return list;
	}

	public List<GradePrivilege> getGradePrivileges(String gradeId, String tenantId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "  select E.USERID_ as \"userId\", E.PRIVILEGE_ as \"privilege\" from HEALTH_GRADE_PRIVILEGE E where ( E.TENANTID_ = #{tenantId} ) ";
		if (StringUtils.isNotEmpty(gradeId)) {
			params.put("gradeId", gradeId);
			sql = sql + " and ( E.GRADEID_ = #{gradeId} ) ";
		}

		params.put("tenantId", tenantId);
		List<GradePrivilege> users = new ArrayList<GradePrivilege>();
		List<Map<String, Object>> datalist = tablePageService.getListData(sql, params);
		if (datalist != null && !datalist.isEmpty()) {
			for (Map<String, Object> dataMap : datalist) {
				GradePrivilege user = new GradePrivilege();
				user.setGradeId(gradeId);
				user.setTenantId(tenantId);
				user.setPrivilege(ParamUtils.getString(dataMap, "privilege"));
				user.setUserId(ParamUtils.getString(dataMap, "userId"));
			}
		}
		return users;
	}

	public List<String> getGradeUserIds(String gradeId, String tenantId, String privilege) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "  select E.USERID_ as \"userId\" from HEALTH_GRADE_PRIVILEGE E where ( E.TENANTID_ = #{tenantId} ) and ( E.PRIVILEGE_ = #{privilege} ) ";
		if (StringUtils.isNotEmpty(gradeId)) {
			params.put("gradeId", gradeId);
			sql = sql + " and ( E.GRADEID_ = #{gradeId} )";
		}
		params.put("privilege", privilege);
		params.put("tenantId", tenantId);
		List<String> userIds = new ArrayList<String>();
		List<Map<String, Object>> datalist = tablePageService.getListData(sql, params);
		if (datalist != null && !datalist.isEmpty()) {
			for (Map<String, Object> dataMap : datalist) {
				userIds.add(ParamUtils.getString(dataMap, "userId"));
			}
		}
		return userIds;
	}

	public List<String> getPrivileges(String gradeId, String tenantId, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select E.PRIVILEGE_ as \"privilege\" from HEALTH_GRADE_PRIVILEGE E where  ( E.TENANTID_ = #{tenantId} ) and ( E.USERID_ = #{userId} ) ";
		if (StringUtils.isNotEmpty(gradeId)) {
			params.put("gradeId", gradeId);
			sql = sql + " and ( E.GRADEID_ = #{gradeId} ) ";
		}
		params.put("userId", userId);
		params.put("tenantId", tenantId);
		List<String> privileges = new ArrayList<String>();
		List<Map<String, Object>> datalist = tablePageService.getListData(sql, params);
		if (datalist != null && !datalist.isEmpty()) {
			for (Map<String, Object> dataMap : datalist) {
				privileges.add(ParamUtils.getString(dataMap, "privilege"));
			}
		}
		return privileges;
	}

	public List<GradeInfo> list(GradeInfoQuery query) {
		List<GradeInfo> list = gradeInfoMapper.getGradeInfos(query);
		return list;
	}

	@Transactional
	public void save(GradeInfo gradeInfo) {
		String cacheKey = "grade_info_" + gradeInfo.getTenantId();
		CacheFactory.remove("grade_info", cacheKey);

		if (StringUtils.isEmpty(gradeInfo.getId())) {
			gradeInfo.setId(UUID32.getUUID());
			gradeInfo.setCreateTime(new Date());
			gradeInfoMapper.insertGradeInfo(gradeInfo);
		} else {
			gradeInfoMapper.updateGradeInfo(gradeInfo);
		}
	}

	@Transactional
	public void saveGradeUsers(String gradeId, String tenantId, String privilege, List<String> actorIds) {
		String cacheKey = "grade_info_" + tenantId;
		CacheFactory.remove("grade_info", cacheKey);

		TableModel table = new TableModel();
		table.setTableName("HEALTH_GRADE_PRIVILEGE");
		if (StringUtils.isNotEmpty(gradeId)) {
			table.addStringColumn("GRADEID_", gradeId);
		}
		table.addStringColumn("TENANTID_", tenantId);
		table.addStringColumn("PRIVILEGE_", privilege);
		tableDataService.deleteTableData(table);

		if (actorIds != null && !actorIds.isEmpty()) {
			for (String actorId : actorIds) {
				TableModel t = new TableModel();
				t.setTableName("HEALTH_GRADE_PRIVILEGE");
				t.addStringColumn("ID_", UUID32.getUUID());
				if (StringUtils.isNotEmpty(gradeId)) {
					t.addStringColumn("GRADEID_", gradeId);
				}
				t.addStringColumn("TENANTID_", tenantId);
				t.addStringColumn("USERID_", actorId);
				t.addStringColumn("PRIVILEGE_", privilege);
				tableDataService.insertTableData(t);
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GradeInfoMapper")
	public void setGradeInfoMapper(GradeInfoMapper gradeInfoMapper) {
		this.gradeInfoMapper = gradeInfoMapper;
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

	@javax.annotation.Resource
	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

}
