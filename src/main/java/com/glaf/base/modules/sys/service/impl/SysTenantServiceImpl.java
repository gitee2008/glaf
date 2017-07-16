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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.mapper.SysTenantMapper;
import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.service.SysOrganizationService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.security.Authentication;
import com.glaf.core.util.UUID32;

@Service("sysTenantService")
@Transactional(readOnly = true)
public class SysTenantServiceImpl implements SysTenantService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SysTenantMapper sysTenantMapper;

	protected SysOrganizationService sysOrganizationService;

	protected SysUserService sysUserService;

	public SysTenantServiceImpl() {

	}

	public int count(SysTenantQuery query) {
		return sysTenantMapper.getSysTenantCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			sysTenantMapper.deleteSysTenantById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				sysTenantMapper.deleteSysTenantById(id);
			}
		}
	}

	public SysTenant getSysTenant(long id) {
		if (id == 0) {
			return null;
		}
		SysTenant sysTenant = sysTenantMapper.getSysTenantById(id);
		return sysTenant;
	}

	/**
	 * 根据name获取一条记录
	 * 
	 * @return
	 */
	public SysTenant getSysTenantByName(String name) {
		SysTenantQuery query = new SysTenantQuery();
		query.name(name);
		List<SysTenant> list = sysTenantMapper.getSysTenants(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public SysTenant getSysTenantByTenantId(String tenantId) {
		return sysTenantMapper.getSysTenantByTenantId(tenantId);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSysTenantCountByQueryCriteria(SysTenantQuery query) {
		return sysTenantMapper.getSysTenantCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SysTenant> getSysTenantsByQueryCriteria(int start, int pageSize, SysTenantQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SysTenant> rows = sqlSessionTemplate.selectList("getSysTenants", query, rowBounds);
		return rows;
	}

	public List<SysTenant> list(SysTenantQuery query) {
		List<SysTenant> list = sysTenantMapper.getSysTenants(query);
		return list;
	}

	@Transactional
	public void register(SysTenant sysTenant, SysUser user) {
		if (sysTenant.getId() == 0) {
			sysTenant.setId(idGenerator.nextId("SYS_TENANT"));
			sysTenant.setTenantId(UUID32.getUUID());
			sysTenant.setCreateTime(new Date());
			sysTenant.setPrincipal(user.getName());
			sysTenant.setTelephone(user.getMobile());
			sysTenantMapper.insertSysTenant(sysTenant);
		} else {
			sysTenantMapper.updateSysTenant(sysTenant);
		}

		SysOrganization organization = sysOrganizationService.getTopOrganizationByTenantId(sysTenant.getTenantId());
		if (organization == null) {
			organization = new SysOrganization();
			organization.setId(sysTenant.getId());
			organization.setTenantId(sysTenant.getTenantId());
			organization.setCode(sysTenant.getCode());
			organization.setName(sysTenant.getName());
			organization.setDescription(sysTenant.getName());
			organization.setPrincipal(sysTenant.getPrincipal());
			organization.setTelphone(sysTenant.getTelephone());
			organization.setCreateBy("system");
			sysOrganizationService.create(organization);
		}

		user.setUserType(8);// 租户用户
		user.setAccountType(10000);// 注册用户即为租户管理员
		user.setEvection(0);
		user.setCreateBy("web");
		user.setCreateDate(new Date());
		user.setOrganizationId(organization.getId());
		user.setTenantId(sysTenant.getTenantId());
		sysUserService.create(user);

		List<String> userIds = new ArrayList<String>();
		userIds.add(user.getActorId());
		Authentication.setAuthenticatedActorId(user.getActorId());
		sysUserService.saveRoleUsers(sysTenant.getTenantId(), "TenantAdmin", 9, userIds);

	}

	@Transactional
	public void save(SysTenant sysTenant) {
		if (sysTenant.getId() == 0) {
			sysTenant.setId(idGenerator.nextId("SYS_TENANT"));
			sysTenant.setTenantId(UUID32.getUUID());
			sysTenant.setCreateTime(new Date());
			sysTenantMapper.insertSysTenant(sysTenant);
		} else {
			sysTenantMapper.updateSysTenant(sysTenant);
		}
		SysOrganization organization = sysOrganizationService.getTopOrganizationByTenantId(sysTenant.getTenantId());
		if (organization == null) {
			organization = new SysOrganization();
			organization.setId(sysTenant.getId());
			organization.setTenantId(sysTenant.getTenantId());
			organization.setCode(sysTenant.getCode());
			organization.setName(sysTenant.getName());
			organization.setDescription(sysTenant.getName());
			organization.setPrincipal(sysTenant.getPrincipal());
			organization.setTelphone(sysTenant.getTelephone());
			organization.setCreateBy("system");
			sysOrganizationService.create(organization);
		}
		String userId = String.valueOf(sysTenant.getId() + "0000");
		if (sysUserService.findByAccount(userId) == null) {
			SysUser bean = new SysUser();
			bean.setActorId(userId);
			bean.setPasswordHash(userId);
			bean.setName("机构管理员");
			bean.setAccountType(10000);
			bean.setUserType(8);
			bean.setEvection(0);
			bean.setCreateBy("system");
			bean.setCreateDate(new Date());
			bean.setOrganizationId(organization.getId());
			bean.setTenantId(sysTenant.getTenantId());
			sysUserService.create(bean);
			List<String> userIds = new ArrayList<String>();
			userIds.add(userId);
			sysUserService.saveRoleUsers(sysTenant.getTenantId(), "TenantAdmin", 9, userIds);
		}
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

	@javax.annotation.Resource
	public void setSysOrganizationService(SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	@javax.annotation.Resource
	public void setSysTenantMapper(SysTenantMapper sysTenantMapper) {
		this.sysTenantMapper = sysTenantMapper;
	}

	@javax.annotation.Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

}
