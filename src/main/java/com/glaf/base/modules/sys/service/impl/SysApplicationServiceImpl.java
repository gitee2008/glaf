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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.SysConstants;
import com.glaf.base.modules.sys.mapper.SysAccessMapper;
import com.glaf.base.modules.sys.mapper.SysApplicationMapper;
import com.glaf.base.modules.sys.model.RealmInfo;
import com.glaf.base.modules.sys.model.SysAccess;
import com.glaf.base.modules.sys.model.SysApplication;
import com.glaf.base.modules.sys.query.SysApplicationQuery;
import com.glaf.base.modules.sys.service.SysApplicationService;
import com.glaf.base.modules.sys.service.SysRoleService;
import com.glaf.base.modules.sys.service.SysUserService;
import com.glaf.base.modules.sys.util.PinyinUtils;
import com.glaf.base.modules.sys.util.SysApplicationJsonFactory;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.service.EntityService;
import com.glaf.core.util.Constants;
import com.glaf.core.util.PageResult;

@Service("sysApplicationService")
@Transactional(readOnly = true)
public class SysApplicationServiceImpl implements SysApplicationService {
	protected final static Log logger = LogFactory.getLog(SysApplicationServiceImpl.class);

	protected IdGenerator idGenerator;

	protected EntityService entityService;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SysApplicationMapper sysApplicationMapper;

	protected SysAccessMapper sysAccessMapper;

	protected SysRoleService sysRoleService;

	protected SysUserService sysUserService;

	public SysApplicationServiceImpl() {

	}

	@Transactional
	public void batchCreate(List<SysApplication> rows) {
		if (rows != null && !rows.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_MENU_REGION);
			}
			for (SysApplication bean : rows) {
				this.create(bean);
			}
		}
	}

	public int count(SysApplicationQuery query) {
		return sysApplicationMapper.getSysApplicationCount(query);
	}

	@Transactional
	public boolean create(SysApplication app) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_MENU_REGION);
		}
		boolean ret = false;

		if (app.getId() == 0) {
			app.setId(idGenerator.nextId("SYS_APPLICATION"));
		}

		if (app.getParentId() > 0) {
			SysApplication parent = this.findById(app.getParentId());
			if (parent != null && parent.getTreeId() != null) {
				app.setTreeId(parent.getTreeId() + app.getId() + "|");
			}
		} else {
			app.setTreeId(app.getId() + "|");
		}

		if (StringUtils.isEmpty(app.getCode())) {
			app.setCode("app_" + app.getId());
		}
		app.setSort(0);// 设置排序号为刚插入的id值
		app.setCreateDate(new Date());
		app.setNamePinyin(PinyinUtils.converterToFirstSpell(app.getName(), true));
		sysApplicationMapper.insertSysApplication(app);
		ret = true;
		return ret;
	}

	@Transactional
	public boolean delete(long id) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "sys_app_" + id;
			CacheFactory.remove(Constants.CACHE_MENU_REGION, cacheKey);
			CacheFactory.clear(Constants.CACHE_MENU_REGION);
		}
		this.deleteById(id);
		return true;
	}

	@Transactional
	public boolean delete(SysApplication bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "sys_app_" + bean.getId();
			CacheFactory.remove(Constants.CACHE_MENU_REGION, cacheKey);
			CacheFactory.clear(Constants.CACHE_MENU_REGION);
		}
		this.deleteById(bean.getId());
		return true;
	}

	@Transactional
	public boolean deleteAll(long[] ids) {
		if (ids != null && ids.length > 0) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_MENU_REGION);
			}
			for (long id : ids) {
				this.deleteById(id);
			}
		}
		return true;
	}

	@Transactional
	public void deleteById(long appId) {
		if (appId > 0) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				String cacheKey = "sys_app_" + appId;
				CacheFactory.remove(Constants.CACHE_MENU_REGION, cacheKey);
				CacheFactory.clear(Constants.CACHE_MENU_REGION);
			}
			List<SysApplication> list = this.getApplicationListWithChildren(appId);
			if (list != null && !list.isEmpty()) {
				if (list.size() > 1) {
					throw new RuntimeException("children is exists");
				}
			}
			SysApplication app = this.findById(appId);
			if (app != null) {
				sysAccessMapper.deleteSysAccessByAppId(appId);
				sysApplicationMapper.deleteSysApplicationById(appId);
			}
		}
	}

	/**
	 * 按编码查找对象
	 * 
	 * @param code
	 *            String
	 * @return SysApplication
	 */
	public SysApplication findByCode(String code) {
		SysApplicationQuery query = new SysApplicationQuery();
		query.code(code);
		List<SysApplication> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			SysApplication sysApplication = list.get(0);
			return sysApplication;
		}

		return null;
	}

	public SysApplication findById(long id) {
		if (id == 0) {
			return null;
		}
		String cacheKey = "sys_app_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_MENU_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysApplicationJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysApplication sysApplication = sysApplicationMapper.getSysApplicationById(id);
		if (sysApplication != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put(Constants.CACHE_MENU_REGION, cacheKey, sysApplication.toJsonObject().toJSONString());
			}
		}
		return sysApplication;
	}

	public SysApplication findByName(String name) {
		SysApplicationQuery query = new SysApplicationQuery();
		query.name(name);

		List<SysApplication> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			SysApplication sysApplication = list.get(0);
			return sysApplication;
		}

		return null;
	}

	public List<SysApplication> getAllSysApplications() {
		String cacheKey = "sys_app_all";
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_MENU_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return SysApplicationJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysApplicationQuery query = new SysApplicationQuery();
		List<SysApplication> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray jsonArray = SysApplicationJsonFactory.listToArray(list);
				CacheFactory.put(Constants.CACHE_MENU_REGION, cacheKey, jsonArray.toJSONString());
			}
		}
		return list;
	}

	public PageResult getApplicationList(int pageNo, int pageSize, SysApplicationQuery query) {
		// 计算总数
		PageResult pager = new PageResult();

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.LOCKED asc, E.SORTNO asc ");

		int start = pageSize * (pageNo - 1);
		List<SysApplication> list = this.getSysApplicationsByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public PageResult getApplicationList(long parentId, int pageNo, int pageSize) {
		PageResult pager = new PageResult();
		SysApplicationQuery query = new SysApplicationQuery();
		query.setDeleteFlag(0);
		query.parentId(parentId);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.LOCKED asc, E.SORTNO asc ");

		int start = pageSize * (pageNo - 1);
		List<SysApplication> list = this.getSysApplicationsByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public List<SysApplication> getApplicationListWithChildren(long parentId) {
		String treeIdLike = null;
		SysApplication parent = findById(parentId);
		logger.debug("parentId:" + parentId);
		// logger.debug("parent:" + parent);
		if (parent != null && parent.getTreeId() != null) {
			treeIdLike = parent.getTreeId() + "%";
		}
		SysApplicationQuery query = new SysApplicationQuery();
		query.treeIdLike(treeIdLike);
		query.setOrderBy(" E.TREEID asc, E.LOCKED asc, E.SORTNO asc");
		List<SysApplication> apps = this.list(query);
		List<SysApplication> list = new ArrayList<SysApplication>();
		if (apps != null && !apps.isEmpty()) {
			Map<Long, SysApplication> disableMap = new HashMap<Long, SysApplication>();
			for (SysApplication app : apps) {
				if (app.getLocked() == 1 || app.getDeleteFlag() == 1) {
					app.setLocked(1);
					disableMap.put(app.getId(), app);
				}
				if (disableMap.get(app.getParentId()) != null) {
					disableMap.put(app.getParentId(), app);
				}
				for (SysApplication t : apps) {
					if (t.getLocked() == 1 || t.getDeleteFlag() == 1) {
						t.setLocked(1);
						disableMap.put(t.getId(), t);
						continue;
					}
					if (disableMap.get(t.getParentId()) != null) {
						t.setLocked(1);
						disableMap.put(t.getId(), t);
						continue;
					}
				}
			}

			for (SysApplication app : apps) {
				if (disableMap.get(app.getId()) != null) {
					continue;
				}
				if (disableMap.get(app.getParentId()) != null) {
					continue;
				}
				if (app.getLocked() == 0 && app.getDeleteFlag() == 0) {
					if (app.getParent() != null) {
						if (app.getParent().getLocked() == 0) {
							list.add(app);
						}
					} else {
						list.add(app);
					}
				}
			}
		}

		return list;
	}

	public List<RealmInfo> getRealmInfos() {
		Map<String, Object> params = new HashMap<String, Object>();
		return sysApplicationMapper.getRealmInfos(params);
	}

	/**
	 * 获取某个用户的全部模块列表
	 * 
	 * @return List
	 */
	public List<SysApplication> getSysApplicationByUserId(String actorId) {
		String cacheKey = "x_sys_app_user_" + actorId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_MENU_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return SysApplicationJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		List<SysApplication> list = sysApplicationMapper.getSysApplicationsByUserId(actorId);

		if (SystemConfig.getBoolean("use_query_cache")) {
			JSONArray jsonArray = SysApplicationJsonFactory.listToArray(list);
			CacheFactory.put(Constants.CACHE_MENU_REGION, cacheKey, jsonArray.toJSONString());
		}

		return list;
	}

	public int getSysApplicationCountByQueryCriteria(SysApplicationQuery query) {
		return sysApplicationMapper.getSysApplicationCount(query);
	}

	public List<SysApplication> getSysApplicationsByQueryCriteria(int start, int pageSize, SysApplicationQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SysApplication> list = sqlSessionTemplate.selectList("getSysApplications", query, rowBounds);
		if (list != null && !list.isEmpty()) {

		}
		return list;
	}

	public List<SysApplication> getSysApplicationsByRoleCode(String roleCode) {
		return sysApplicationMapper.getSysApplicationsByRoleCode(roleCode);
	}

	public List<SysApplication> getSysApplicationsByRoleId(String roleId) {
		return sysApplicationMapper.getSysApplicationsByRoleId(roleId);
	}

	public List<SysApplication> list(SysApplicationQuery query) {
		List<SysApplication> list = sysApplicationMapper.getSysApplications(query);
		return list;
	}

	@Transactional
	public void markDeleteFlag(long id, int deleteFlag) {
		if (id > 0) {
			SysApplication application = this.findById(id);
			if (application != null) {
				if (SystemConfig.getBoolean("use_query_cache")) {
					String cacheKey = "sys_app_" + id;
					CacheFactory.remove(Constants.CACHE_MENU_REGION, cacheKey);
					CacheFactory.clear(Constants.CACHE_MENU_REGION);
				}
				application.setDeleteFlag(1);
				application.setDeleteTime(new Date());
				sysApplicationMapper.updateSysApplication(application);
			}
		}
	}

	/**
	 * 设置删除标记
	 * 
	 * @param ids
	 * @param deleteFlag
	 */
	@Transactional
	public void markDeleteFlag(long[] ids, int deleteFlag) {
		if (ids != null && ids.length > 0) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_MENU_REGION);
			}
			for (long id : ids) {
				this.markDeleteFlag(id, deleteFlag);
			}
		}
	}

	@Transactional
	public void saveRoleApplications(String roleId, List<Long> appIds) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_MENU_REGION);
		}
		sysAccessMapper.deleteSysAccessByRoleId(roleId);
		for (Long appId : appIds) {
			SysAccess model = new SysAccess();
			model.setId(idGenerator.nextId());
			model.setRoleId(roleId);
			model.setAppId(appId);
			sysAccessMapper.insertSysAccess(model);
		}
	}

	@Resource
	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
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
	public void setSysAccessMapper(SysAccessMapper sysAccessMapper) {
		this.sysAccessMapper = sysAccessMapper;
	}

	@Resource
	public void setSysApplicationMapper(SysApplicationMapper sysApplicationMapper) {
		this.sysApplicationMapper = sysApplicationMapper;
	}

	@javax.annotation.Resource
	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@Resource
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
	}

	@Transactional
	public void sort(long parent, SysApplication bean, int operate) {
		if (bean == null) {
			return;
		}
		if (operate == SysConstants.SORT_PREVIOUS) {// 前移
			sortByPrevious(parent, bean);
		} else if (operate == SysConstants.SORT_FORWARD) {// 后移
			sortByForward(parent, bean);
		}
	}

	/**
	 * 向后移动排序
	 * 
	 * @param bean
	 */
	private void sortByForward(long parentId, SysApplication bean) {
		SysApplicationQuery query = new SysApplicationQuery();
		query.setDeleteFlag(0);
		query.parentId(parentId);
		query.setSortLessThan(bean.getSort());
		query.setOrderBy(" E.SORTNO desc ");
		List<SysApplication> list = this.list(query);
		if (list != null && list.size() > 0) {// 有记录
			SysApplication temp = (SysApplication) list.get(0);
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
	private void sortByPrevious(long parentId, SysApplication bean) {
		// 查找前一个对象
		SysApplicationQuery query = new SysApplicationQuery();
		query.parentId(parentId);
		query.setSortGreaterThan(bean.getSort());
		query.setOrderBy(" E.SORTNO asc ");
		query.setDeleteFlag(0);

		List<SysApplication> list = this.list(query);
		if (list != null && list.size() > 0) {// 有记录
			SysApplication temp = (SysApplication) list.get(0);
			int i = bean.getSort();
			bean.setSort(temp.getSort());
			this.update(bean);// 更新bean

			temp.setSort(i);
			this.update(temp);// 更新temp
		}
	}

	@Transactional
	public boolean update(SysApplication bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "sys_app_" + bean.getId();
			CacheFactory.remove(Constants.CACHE_MENU_REGION, cacheKey);
			CacheFactory.clear(Constants.CACHE_MENU_REGION);
		}
		bean.setUpdateDate(new Date());
		if (StringUtils.isEmpty(bean.getCode())) {
			bean.setCode("app_" + bean.getId());
		}
		bean.setNamePinyin(PinyinUtils.converterToFirstSpell(bean.getName(), true));
		this.sysApplicationMapper.updateSysApplication(bean);
		return true;
	}

}
