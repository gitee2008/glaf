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

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.glaf.base.modules.sys.mapper.SysTenantMapper;
import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.base.modules.sys.model.SysUser;
import com.glaf.base.modules.sys.query.SysTenantQuery;
import com.glaf.base.modules.sys.service.SysOrganizationService;
import com.glaf.base.modules.sys.service.SysTenantService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.modules.sys.util.PinyinUtils;
import com.glaf.base.modules.sys.util.SysTenantJsonFactory;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.security.Authentication;
import com.glaf.core.util.Constants;
import com.glaf.core.util.UUID32;

@Service("sysTenantService")
@Transactional(readOnly = true)
public class SysTenantServiceImpl implements SysTenantService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private SysTenantMapper sysTenantMapper;

	private SysOrganizationService sysOrganizationService;

	private SysUserService sysUserService;

	public SysTenantServiceImpl() {

	}

	public int count(SysTenantQuery query) {
		return sysTenantMapper.getSysTenantCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = Constants.CACHE_TENANT_KEY + id;
				CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
			}
			sysTenantMapper.deleteSysTenantById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				if (SystemConfig.getBoolean("use_query_cache")) {
					String cacheKey = Constants.CACHE_TENANT_KEY + id;
					CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
				}
				sysTenantMapper.deleteSysTenantById(id);
			}
		}
	}

	public SysTenant getSysTenant(long id) {
		if (id == 0) {
			return null;
		}
		String cacheKey = Constants.CACHE_TENANT_KEY + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_TENANT_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					SysTenant model = SysTenantJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ignored) {
				}
			}
		}

		SysTenant sysTenant = sysTenantMapper.getSysTenantById(id);
		if (sysTenant != null) {
			CacheFactory.put(Constants.CACHE_TENANT_REGION, cacheKey, sysTenant.toJsonObject().toJSONString());
		}
		return sysTenant;
	}

	/**
	 * 根据name获取一条记录
	 * 
	 * @return
	 */
	public SysTenant getSysTenantByName(String name) {
		String cacheKey = Constants.CACHE_TENANT_KEY + name;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_TENANT_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					SysTenant model = SysTenantJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ignored) {
				}
			}
		}

		SysTenantQuery query = new SysTenantQuery();
		query.name(name);
		List<SysTenant> list = sysTenantMapper.getSysTenants(query);
		if (list != null && !list.isEmpty()) {
			SysTenant sysTenant = list.get(0);
			CacheFactory.put(Constants.CACHE_TENANT_REGION, cacheKey, sysTenant.toJsonObject().toJSONString());
		}
		return null;
	}

	public SysTenant getSysTenantByTenantId(String tenantId) {
		String cacheKey = Constants.CACHE_TENANT_KEY + tenantId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_TENANT_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					com.alibaba.fastjson.JSONObject json = JSON.parseObject(text);
					SysTenant model = SysTenantJsonFactory.jsonToObject(json);
					if (model != null) {
						return model;
					}
				} catch (Exception ignored) {
				}
			}
		}
		SysTenant sysTenant = sysTenantMapper.getSysTenantByTenantId(tenantId);
		if (sysTenant != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put(Constants.CACHE_TENANT_REGION, cacheKey, sysTenant.toJsonObject().toJSONString());
			}
		}
		return sysTenant;
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
		return sqlSessionTemplate.selectList("getSysTenants", query, rowBounds);
	}

	public List<SysTenant> list(SysTenantQuery query) {
		return sysTenantMapper.getSysTenants(query);
	}

	@Transactional
	public void register(SysTenant sysTenant, SysUser user) {
		if (sysTenant.getId() == 0) {
			sysTenant.setId(idGenerator.nextId("SYS_TENANT"));
			sysTenant.setTenantId(UUID32.getUUID());
			sysTenant.setCreateTime(new Date());
			sysTenant.setPrincipal(user.getName());
			sysTenant.setTelephone(user.getMobile());
			sysTenant.setNamePinyin(PinyinUtils.converterToFirstSpell(sysTenant.getName(), true));
			sysTenantMapper.insertSysTenant(sysTenant);
		} else {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = Constants.CACHE_TENANT_KEY + sysTenant.getId();
				CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
				cacheKey = Constants.CACHE_TENANT_KEY + sysTenant.getName();
				CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
				cacheKey = Constants.CACHE_TENANT_KEY + sysTenant.getTenantId();
				CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
			}

			sysTenant.setNamePinyin(PinyinUtils.converterToFirstSpell(sysTenant.getName(), true));
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
		user.setAccountType(10000);// 注册用户即为管理员
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
			sysTenant.setNamePinyin(PinyinUtils.converterToFirstSpell(sysTenant.getName(), true));
			sysTenantMapper.insertSysTenant(sysTenant);
		} else {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = Constants.CACHE_TENANT_KEY + sysTenant.getId();
				CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
				cacheKey = Constants.CACHE_TENANT_KEY + sysTenant.getName();
				CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
				cacheKey = Constants.CACHE_TENANT_KEY + sysTenant.getTenantId();
				CacheFactory.remove(Constants.CACHE_TENANT_REGION, cacheKey);
			}

			sysTenant.setNamePinyin(PinyinUtils.converterToFirstSpell(sysTenant.getName(), true));
			sysTenantMapper.updateSysTenant(sysTenant);
		}
		SysOrganization organization = sysOrganizationService.getTopOrganizationByTenantId(sysTenant.getTenantId());
		if (organization == null) {
			try {
				organization = new SysOrganization();
				organization.setId(idGenerator.nextId("SYS_ORGANIZATION"));
				organization.setTenantId(sysTenant.getTenantId());
				organization.setCode(sysTenant.getCode());
				organization.setName(sysTenant.getName());
				organization.setDescription(sysTenant.getName());
				organization.setPrincipal(sysTenant.getPrincipal());
				organization.setTelphone(sysTenant.getTelephone());
				organization.setCreateBy("system");
				sysOrganizationService.create(organization);
			} catch (java.lang.Throwable ignored) {

			}
		}
		String userId = sysTenant.getId() + "000";
		if (sysUserService.findByAccount(userId) == null) {
			SysUser bean = new SysUser();
			bean.setActorId(userId);
			bean.setName("管理员");
			bean.setAccountType(10000);
			bean.setUserType(8);
			bean.setEvection(0);
			bean.setCreateBy("system");
			bean.setCreateDate(new Date());
			assert organization != null;
			bean.setOrganizationId(organization.getId());
			bean.setTenantId(sysTenant.getTenantId());
			sysUserService.create(bean);
			List<String> userIds = new ArrayList<String>();
			userIds.add(userId);
			sysUserService.saveAddRoleUsers(sysTenant.getTenantId(), "TenantAdmin", 9, userIds);
		}
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
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
