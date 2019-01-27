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

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.mapper.SysRoleMapper;
import com.glaf.base.modules.sys.model.SysRole;
import com.glaf.base.modules.sys.query.SysRoleQuery;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.util.SysRoleJsonFactory;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.Constants;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.UUID32;

@Service("sysRoleService")
@Transactional(readOnly = true)
public class SysRoleServiceImpl implements SysRoleService {
	protected final static Log logger = LogFactory.getLog(SysRoleServiceImpl.class);

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private SysRoleMapper sysRoleMapper;

	public SysRoleServiceImpl() {

	}

	private int count(SysRoleQuery query) {
		return sysRoleMapper.getSysRoleCount(query);
	}

	@Transactional
	public boolean create(SysRole bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		if (bean.getId() == null) {
			bean.setId(UUID32.getUUID());
		}
		bean.setCreateDate(new Date());
		bean.setSort(0);
		if (StringUtils.isEmpty(bean.getCode())) {
			bean.setCode("role_" + bean.getId());
		}
		sysRoleMapper.insertSysRole(bean);

		return true;
	}

	@Transactional
	public boolean delete(String id) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		this.deleteById(id);

		return true;
	}

	@Transactional
	public boolean delete(SysRole bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		this.deleteById(bean.getId());

		return true;
	}

	@Transactional
	public boolean deleteAll(String[] ids) {
		if (ids != null && ids.length > 0) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_ROLE_REGION);
				CacheFactory.clear(Constants.CACHE_USER_REGION);
			}
			for (String id : ids) {
				this.deleteById(id);
			}
		}

		return true;
	}

	@Transactional
	private void deleteById(String id) {
		if (id != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_ROLE_REGION);
				CacheFactory.clear(Constants.CACHE_USER_REGION);
			}
			SysRole role = sysRoleMapper.getSysRoleById(id);
			if (role != null && StringUtils.equals(role.getType(), "SYS")) {
				throw new RuntimeException("Can't delete sys role");
			} else {
				sysRoleMapper.deleteSysRoleById(id);
			}
		}

	}

	@Transactional
	public void deleteByIds(List<String> roleIds) {
		if (roleIds != null && !roleIds.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_ROLE_REGION);
				CacheFactory.clear(Constants.CACHE_USER_REGION);
			}
			for (String roleId : roleIds) {
				this.deleteById(roleId);
			}
		}

	}

	public SysRole findByCode(String code) {
		String cacheKey = Constants.CACHE_ROLE_KEY + code;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ROLE_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysRoleJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		SysRoleQuery query = new SysRoleQuery();
		query.code(code);
		query.setOrderBy(" E.CODE asc ");

		List<SysRole> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			SysRole sysRole = list.get(0);
			JSONObject json = sysRole.toJsonObject();
			CacheFactory.put(Constants.CACHE_ROLE_REGION, cacheKey, json.toJSONString());
			return sysRole;
		}

		return null;
	}

	public SysRole findById(String id) {
		String cacheKey = Constants.CACHE_ROLE_KEY + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ROLE_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysRoleJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysRole sysRole = sysRoleMapper.getSysRoleById(id);

		if (sysRole != null && SystemConfig.getBoolean("use_query_cache")) {
			JSONObject json = sysRole.toJsonObject();
			CacheFactory.put(Constants.CACHE_ROLE_REGION, cacheKey, json.toJSONString());
		}
		return sysRole;
	}

	public SysRole findByName(String name) {
		String cacheKey = Constants.CACHE_ROLE_KEY + name;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ROLE_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysRoleJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		SysRoleQuery query = new SysRoleQuery();
		query.name(name);
		query.setOrderBy(" E.ROLENAME asc ");

		List<SysRole> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			SysRole sysRole = list.get(0);
			JSONObject json = sysRole.toJsonObject();
			CacheFactory.put(Constants.CACHE_ROLE_REGION, cacheKey, json.toJSONString());
			return sysRole;
		}

		return null;
	}

	public SysRole getSysRole(String id) {
		if (id == null) {
			return null;
		}
		return this.findById(id);
	}

	public int getSysRoleCountByQueryCriteria(SysRoleQuery query) {
		return sysRoleMapper.getSysRoleCount(query);
	}

	public List<SysRole> getSysRoleList() {
		SysRoleQuery query = new SysRoleQuery();
		query.setDeleteFlag(0);
		query.setOrderBy(" E.SORTNO asc ");

		return this.list(query);
	}

	public PageResult getSysRoleList(int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		SysRoleQuery query = new SysRoleQuery();
		query.setDeleteFlag(0);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}

		query.setOrderBy(" E.SORTNO asc");

		int start = pageSize * (pageNo - 1);
		List<SysRole> list = this.getSysRolesByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public PageResult getSysRoleList(int pageNo, int pageSize, SysRoleQuery query) {
		// 计算总数
		PageResult pager = new PageResult();

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}

		query.setOrderBy(" E.SORTNO asc ");

		int start = pageSize * (pageNo - 1);
		List<SysRole> list = this.getSysRolesByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public List<SysRole> getSysRolesByQueryCriteria(int start, int pageSize, SysRoleQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getSysRoles", query, rowBounds);
	}

	private List<SysRole> list(SysRoleQuery query) {
		return sysRoleMapper.getSysRoles(query);
	}

	@Transactional
	public void save(SysRole sysRole) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		if (sysRole.getId() == null) {
			sysRole.setId(idGenerator.getNextId());
			sysRole.setCreateDate(new Date());
			sysRoleMapper.insertSysRole(sysRole);
		} else {
			sysRole.setUpdateDate(new Date());
			sysRoleMapper.updateSysRole(sysRole);
		}
	}

	@Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Resource
	public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
		this.sysRoleMapper = sysRoleMapper;
	}

	/**
	 * 排序
	 * 
	 * @param bean    SysRole
	 * @param operate int 操作
	 */
	@Transactional
	public void sort(SysRole bean, int operate) {
		if (bean == null)
			return;
		if (operate == SysConstants.SORT_PREVIOUS) {// 前移
			sortByPrevious(bean);
		} else if (operate == SysConstants.SORT_FORWARD) {// 后移
			sortByForward(bean);
		}
	}

	/**
	 * 向后移动排序
	 * 
	 * @param bean
	 */
	private void sortByForward(SysRole bean) {
		SysRoleQuery query = new SysRoleQuery();
		query.setSortLessThan(bean.getSort());
		query.setOrderBy(" E.SORTNO desc ");
		List<SysRole> list = this.list(query);
		if (list != null && list.size() > 0) {// 有记录
			SysRole temp = list.get(0);
			int i = bean.getSort();
			bean.setSort(temp.getSort());
			this.update(bean);// 更新bean

			temp.setSort(i);
			this.update(temp);// 更新temp
		}
	}

	/**
	 * 向前移动排序
	 * 
	 * @param bean
	 */
	private void sortByPrevious(SysRole bean) {
		SysRoleQuery query = new SysRoleQuery();
		query.setSortGreaterThan(bean.getSort());

		List<SysRole> list = this.list(query);
		if (list != null && list.size() > 0) {// 有记录
			SysRole temp = list.get(0);
			int i = bean.getSort();
			bean.setSort(temp.getSort());
			this.update(bean);// 更新bean

			temp.setSort(i);
			this.update(temp);// 更新temp
		}
	}

	@Transactional
	public boolean update(SysRole bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = Constants.CACHE_ROLE_KEY + bean.getId();
			CacheFactory.remove(Constants.CACHE_ROLE_REGION, cacheKey);
			CacheFactory.clear(Constants.CACHE_ROLE_REGION);
			CacheFactory.clear(Constants.CACHE_USER_REGION);
		}

		bean.setUpdateDate(new Date());
		sysRoleMapper.updateSysRole(bean);

		return true;
	}
}
