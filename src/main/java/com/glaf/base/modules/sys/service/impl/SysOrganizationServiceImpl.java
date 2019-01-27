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
import com.glaf.base.modules.sys.mapper.SysOrganizationMapper;
import com.glaf.base.modules.sys.model.SysOrganization;
import com.glaf.base.modules.sys.query.SysOrganizationQuery;
import com.glaf.base.modules.sys.service.SysOrganizationService;
import com.glaf.base.modules.sys.util.PinyinUtils;
import com.glaf.base.modules.sys.util.SysOrganizationJsonFactory;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.Constants;
import com.glaf.core.util.PageResult;

@Service("sysOrganizationService")
@Transactional(readOnly = true)
public class SysOrganizationServiceImpl implements SysOrganizationService {
	protected final static Log logger = LogFactory.getLog(SysOrganizationServiceImpl.class);

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private SysOrganizationMapper sysOrganizationMapper;

	public SysOrganizationServiceImpl() {

	}

	private int count(SysOrganizationQuery query) {
		return sysOrganizationMapper.getSysOrganizationCount(query);
	}

	@Transactional
	public boolean create(SysOrganization bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ORGANIZATION_REGION);
		}
		if (bean.getId() == 0) {
			bean.setId(idGenerator.nextId("SYS_ORGANIZATION"));
		}
		if (bean.getParentId() > 0) {
			SysOrganization parent = this.findById(bean.getParentId());
			if (parent != null && parent.getTreeId() != null) {
				bean.setTreeId(parent.getTreeId() + bean.getId() + "|");
			}
		} else {
			bean.setTreeId(bean.getId() + "|");
		}
		bean.setCreateTime(new Date());
		bean.setNamePinyin(PinyinUtils.converterToFirstSpell(bean.getName(), true));
		sysOrganizationMapper.insertSysOrganization(bean);
		return true;
	}

	@Transactional
	public boolean delete(long organizationId) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ORGANIZATION_REGION);
		}
		this.deleteById(organizationId);
		return true;
	}

	@Transactional
	public boolean delete(SysOrganization bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ORGANIZATION_REGION);
		}
		this.deleteById(bean.getId());
		return true;
	}

	@Transactional
	public boolean deleteAll(long[] organizationIds) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ORGANIZATION_REGION);
		}
		if (organizationIds != null && organizationIds.length > 0) {
			for (long organizationId : organizationIds) {
				this.deleteById(organizationId);
			}
		}
		return true;
	}

	@Transactional
	private void deleteById(long organizationId) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ORGANIZATION_REGION);
		}
	}

	public SysOrganization findByCode(String code) {
		String cacheKey = Constants.CACHE_ORGANIZATION_CODE_KEY + code;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ORGANIZATION_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysOrganizationJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysOrganizationQuery query = new SysOrganizationQuery();
		query.code(code);
		query.setDeleteFlag(0);
		query.setOrderBy(" E.ID asc ");

		SysOrganization organization = null;
		List<SysOrganization> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			organization = list.get(0);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put(Constants.CACHE_ORGANIZATION_REGION, cacheKey,
						organization.toJsonObject().toJSONString());
			}
		}

		return organization;
	}

	public SysOrganization findById(long organizationId) {
		return this.getSysOrganization(organizationId);
	}

	public SysOrganization findByName(String name) {
		SysOrganizationQuery query = new SysOrganizationQuery();
		query.name(name);
		query.setDeleteFlag(0);
		query.setOrderBy(" E.ID asc ");

		List<SysOrganization> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	public SysOrganization findByNo(String organizationNo) {
		SysOrganizationQuery query = new SysOrganizationQuery();
		query.no(organizationNo);
		query.setDeleteFlag(0);
		query.setOrderBy(" E.ID asc ");

		List<SysOrganization> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 获取某个部门及上级部门
	 * 
	 * @param organizationId
	 * @return
	 */
	public SysOrganization getSysOrganization(long organizationId) {
		if (organizationId <= 0) {
			return null;
		}

		String cacheKey = Constants.CACHE_ORGANIZATION_KEY + organizationId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ORGANIZATION_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysOrganizationJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysOrganization organization = sysOrganizationMapper.getSysOrganizationById(organizationId);
		if (organization != null && organization.getParentId() > 0) {
			SysOrganization parent = this.getSysOrganization(organization.getParentId());
			organization.setParent(parent);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put(Constants.CACHE_ORGANIZATION_REGION, cacheKey,
						organization.toJsonObject().toJSONString());
			}
		}
		return organization;
	}

	public int getSysOrganizationCountByQueryCriteria(SysOrganizationQuery query) {
		return sysOrganizationMapper.getSysOrganizationCount(query);
	}

	public List<SysOrganization> getSysOrganizationList() {
		SysOrganizationQuery query = new SysOrganizationQuery();
		return this.list(query);
	}

	@Override
	public PageResult getSysOrganizationList(int pageNo, int pageSize, SysOrganizationQuery query) {
		// 计算总数
		PageResult pager = new PageResult();

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.TREEID asc, E.SORTNO asc");

		int start = pageSize * (pageNo - 1);
		List<SysOrganization> list = this.getSysOrganizationsByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public List<SysOrganization> getSysOrganizationList(long parentId) {
		String cacheKey = Constants.CACHE_ORGANIZATION_KEY + parentId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ORGANIZATION_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return SysOrganizationJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysOrganizationQuery query = new SysOrganizationQuery();
		query.setDeleteFlag(0);
		query.parentId(parentId);
		List<SysOrganization> list = this.list(query);
		if (list != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = SysOrganizationJsonFactory.listToArray(list);
				CacheFactory.put(Constants.CACHE_ORGANIZATION_REGION, cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	@Override
	public PageResult getSysOrganizationList(long parentId, int pageNo, int pageSize) {
		// 计算总数
		PageResult pager = new PageResult();
		SysOrganizationQuery query = new SysOrganizationQuery();
		query.setDeleteFlag(0);
		query.parentId(parentId);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.SORTNO asc");

		int start = pageSize * (pageNo - 1);
		List<SysOrganization> list = this.getSysOrganizationsByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	/**
	 * 获取列表
	 * 
	 * @param tenantId
	 * @return List
	 */
	public List<SysOrganization> getSysOrganizationList(String tenantId) {
		String cacheKey = Constants.CACHE_ORGANIZATION_KEY + tenantId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ORGANIZATION_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return SysOrganizationJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SysOrganizationQuery query = new SysOrganizationQuery();
		query.tenantId(tenantId);
		List<SysOrganization> list = this.list(query);
		if (list != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = SysOrganizationJsonFactory.listToArray(list);
				CacheFactory.put(Constants.CACHE_ORGANIZATION_REGION, cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	public List<SysOrganization> getSysOrganizationsByQueryCriteria(int start, int pageSize,
			SysOrganizationQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getSysOrganizations", query, rowBounds);
	}

	/**
	 * 获取某个部门及全部上级部门
	 * 
	 * @param organizationId
	 * @return
	 */
	public SysOrganization getSysOrganizationWithAncestor(long organizationId) {
		if (organizationId <= 0) {
			return null;
		}
		SysOrganization organization = this.getSysOrganization(organizationId);
		if (organization != null) {
			if (organization.getParentId() > 0) {
				SysOrganization parent = this.getSysOrganizationWithAncestor(organization.getParentId());
				organization.setParent(parent);
			}
		}
		return organization;
	}

	/**
	 * 获取列表
	 * 
	 * @param parentId long
	 * @return List
	 */
	public List<SysOrganization> getSysOrganizationWithChildren(long parentId) {
		SysOrganization parent = this.findById(parentId);
		if (parent != null && parent.getTreeId() != null) {
			SysOrganizationQuery query = new SysOrganizationQuery();
			query.treeIdLike(parent.getTreeId() + "%");
			return this.list(query);
		}
		return null;
	}

	public SysOrganization getTopOrganizationByTenantId(String tenantId) {
		String cacheKey = Constants.CACHE_ORGANIZATION_KEY + tenantId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_ORGANIZATION_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SysOrganizationJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		SysOrganizationQuery query = new SysOrganizationQuery();
		query.setTenantId(tenantId);
		query.setOrderBy(" E.ID asc ");

		List<SysOrganization> list = this.list(query);
		SysOrganization organization = null;
		if (list != null && !list.isEmpty()) {
			organization = list.get(0);
			if (organization != null && SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put(Constants.CACHE_ORGANIZATION_REGION, cacheKey,
						organization.toJsonObject().toJSONString());
			}
		}
		return organization;
	}

	public List<SysOrganization> list(SysOrganizationQuery query) {
		return sysOrganizationMapper.getSysOrganizations(query);
	}

	@Transactional
	public void markDeleteFlag(long organizationId, int deleteFlag) {

	}

	/**
	 * 设置删除标记
	 * 
	 * @param organizationIds
	 * @param deleteFlag
	 */
	@Transactional
	public void markDeleteFlag(long[] organizationIds, int deleteFlag) {
		if (organizationIds != null && organizationIds.length > 0) {
			for (long organizationId : organizationIds) {
				this.markDeleteFlag(organizationId, deleteFlag);
			}
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
	public void setSysOrganizationMapper(SysOrganizationMapper sysOrganizationMapper) {
		this.sysOrganizationMapper = sysOrganizationMapper;
	}

	/**
	 * 排序
	 * 
	 * @param bean    SysOrganization
	 * @param operate int 操作
	 */
	@Transactional
	public void sort(long parentId, SysOrganization bean, int operate) {
		if (bean == null)
			return;
		if (operate == SysConstants.SORT_PREVIOUS) {// 前移
			sortByPrevious(parentId, bean);
		} else if (operate == SysConstants.SORT_FORWARD) {// 后移
			sortByForward(parentId, bean);
		}
	}

	/**
	 * 向后移动排序
	 * 
	 * @param bean
	 */
	private void sortByForward(long parentId, SysOrganization bean) {
		SysOrganizationQuery query = new SysOrganizationQuery();
		query.setDeleteFlag(0);
		query.parentId(parentId);
		query.setSortLessThan(bean.getSort());
		query.setOrderBy(" E.SORTNO desc ");

		// 查找后一个对象

		List<SysOrganization> list = this.list(query);
		if (list != null && list.size() > 0) {// 有记录
			SysOrganization temp = list.get(0);
			bean.setSort(temp.getSort());
			this.update(bean);// 更新bean
		}
	}

	/**
	 * 向前移动排序
	 * 
	 * @param bean
	 */
	private void sortByPrevious(long parentId, SysOrganization bean) {
		SysOrganizationQuery query = new SysOrganizationQuery();
		query.setDeleteFlag(0);
		query.parentId(parentId);
		query.setSortGreaterThan(bean.getSort());
		query.setOrderBy(" E.SORTNO asc ");

		// 查找后一个对象

		List<SysOrganization> list = this.list(query);
		if (list != null && list.size() > 0) {// 有记录
			SysOrganization temp = list.get(0);
			int i = bean.getSort();
			bean.setSort(temp.getSort());
			this.update(bean);// 更新bean

			temp.setSort(i);
			this.update(temp);// 更新temp
		}
	}

	@Transactional
	public boolean update(SysOrganization bean) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_ORGANIZATION_REGION);
		}
		bean.setUpdateDate(new Date());
		bean.setNamePinyin(PinyinUtils.converterToFirstSpell(bean.getName(), true));
		sysOrganizationMapper.updateSysOrganization(bean);
		return true;
	}

}
