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
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.modules.sys.mapper.GroupLeaderMapper;
import com.glaf.base.modules.sys.mapper.GroupMapper;
import com.glaf.base.modules.sys.mapper.GroupUserMapper;
import com.glaf.base.modules.sys.model.Group;
import com.glaf.base.modules.sys.model.GroupLeader;
import com.glaf.base.modules.sys.model.GroupUser;
import com.glaf.base.modules.sys.query.GroupQuery;
import com.glaf.base.modules.sys.service.GroupService;
import com.glaf.base.modules.sys.util.GroupJsonFactory;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.Constants;
import com.glaf.core.util.JsonUtils;
import com.glaf.core.util.PageResult;
import com.glaf.core.util.UUID32;

@Service("groupService")
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected GroupMapper groupMapper;

	protected GroupUserMapper groupUserMapper;

	protected GroupLeaderMapper groupLeaderMapper;

	protected SqlSessionTemplate sqlSessionTemplate;

	public GroupServiceImpl() {

	}

	public int count(GroupQuery query) {
		return groupMapper.getGroupCount(query);
	}

	@Transactional
	public void deleteById(String groupId) {
		if (groupId != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_GROUP_REGION);
			}

			groupMapper.deleteGroupById(groupId);
		}
	}

	@Transactional
	public void deleteByIds(List<String> groupIds) {
		if (groupIds != null && !groupIds.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.clear(Constants.CACHE_GROUP_REGION);
			}
			GroupQuery query = new GroupQuery();
			query.rowIds(groupIds);
			groupMapper.deleteGroups(query);
		}
	}

	public Group getGroup(String groupId) {
		if (groupId == null) {
			return null;
		}

		String cacheKey = Constants.CACHE_GROUP_KEY + groupId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_GROUP_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return GroupJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		Group group = groupMapper.getGroupById(groupId);
		if (group != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONObject json = group.toJsonObject();
				CacheFactory.put(Constants.CACHE_GROUP_REGION, cacheKey, json.toJSONString());
			}
		}
		return group;
	}

	public int getGroupCountByQueryCriteria(GroupQuery query) {
		return groupMapper.getGroupCount(query);
	}

	public List<Group> getGroupLeadersByGroupId(GroupQuery query) {
		return groupMapper.getGroupLeadersByGroupId(query);
	}

	public PageResult getGroupList(String tenantId, String type, String createBy, int pageNo, int pageSize) {
		PageResult pager = new PageResult();
		GroupQuery query = new GroupQuery();
		query.tenantId(tenantId);
		query.setType(type);
		query.setCreateBy(createBy);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.SORTNO desc");

		int start = pageSize * (pageNo - 1);
		List<Group> list = this.getGroupsByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public PageResult getGroupList(String tenantId, String type, String createBy, int pageNo, int pageSize,
			GroupQuery query) {
		PageResult pager = new PageResult();
		query.tenantId(tenantId);
		query.setType(type);
		query.setCreateBy(createBy);

		int count = this.count(query);
		if (count == 0) {// 结果集为空
			pager.setPageSize(pageSize);
			return pager;
		}
		query.setOrderBy(" E.SORTNO desc");

		int start = pageSize * (pageNo - 1);
		List<Group> list = this.getGroupsByQueryCriteria(start, pageSize, query);
		pager.setResults(list);
		pager.setPageSize(pageSize);
		pager.setCurrentPageNo(pageNo);
		pager.setTotalRecordCount(count);

		return pager;
	}

	public List<Group> getGroupsByQueryCriteria(int start, int pageSize, GroupQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Group> rows = sqlSessionTemplate.selectList("getGroups", query, rowBounds);
		return rows;
	}

	/**
	 * 通过用户账号获取群组
	 * 
	 * @param userId
	 * @return
	 */
	public List<Group> getGroupsByUserId(String tenantId, String userId) {
		String cacheKey = Constants.CACHE_GROUP_KEY + "_" + tenantId + "_" + userId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_GROUP_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return GroupJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		List<Group> groups = groupMapper.getGroupsByUserId(userId);
		if (groups != null) {
			JSONArray jsonArray = GroupJsonFactory.listToArray(groups);
			CacheFactory.put(Constants.CACHE_GROUP_REGION, cacheKey, jsonArray.toJSONString());
		}
		return groups;
	}

	/**
	 * 通过用户账号及组类型获取群组
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<Group> getGroupsByUserIdAndType(String tenantId, String userId, String type) {
		String cacheKey = Constants.CACHE_GROUP_KEY + "_" + tenantId + "_" + userId + "_" + type;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_GROUP_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return GroupJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		GroupQuery query = new GroupQuery();
		query.tenantId(tenantId);
		query.setUserId(userId);
		query.setType(type);
		List<Group> groups = groupMapper.getGroupsByUserIdAndType(query);
		if (groups != null) {
			JSONArray jsonArray = GroupJsonFactory.listToArray(groups);
			CacheFactory.put(Constants.CACHE_GROUP_REGION, cacheKey, jsonArray.toJSONString());
		}
		return groups;
	}

	/**
	 * 通过群组ID取用户
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Group> getGroupUsersByGroupId(GroupQuery query) {
		return groupMapper.getGroupUsersByGroupId(query);
	}

	public List<String> getLeaderUserIdsByGroupId(String tenantId, String groupId) {
		String cacheKey = Constants.CACHE_GROUP_KEY + "_x_" + tenantId + "_" + groupId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_GROUP_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return JsonUtils.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		List<String> list = groupLeaderMapper.getUserIdsByGroupId(groupId);
		if (list != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = JsonUtils.listToArray(list);
				CacheFactory.put(Constants.CACHE_GROUP_REGION, cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	public List<String> getUserIdsByGroupId(String tenantId, String groupId) {
		String cacheKey = Constants.CACHE_GROUP_KEY + "_y_" + tenantId + "_" + groupId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_GROUP_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return JsonUtils.arrayToList(jsonArray);
				} catch (Exception ex) {
				}
			}
		}
		List<String> list = groupUserMapper.getUserIdsByGroupId(groupId);
		if (list != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = JsonUtils.listToArray(list);
				CacheFactory.put(Constants.CACHE_GROUP_REGION, cacheKey, array.toJSONString());
			}
		}
		return list;
	}

	/**
	 * 根据群组名称及类型获取群组用户
	 * 
	 * @param groupName
	 * @param groupType
	 * @return
	 */
	public List<String> getUserIdsByGroupNameAndType(String tenantId, String groupName, String groupType) {
		GroupQuery query = new GroupQuery();
		query.tenantId(tenantId);
		query.name(groupName);
		query.type(groupType);
		List<Group> list = groupMapper.getGroups(query);
		if (list != null && list.isEmpty()) {
			Group group = list.get(0);
			return this.getUserIdsByGroupId(tenantId, group.getGroupId());
		}
		return null;
	}

	public List<Group> list(GroupQuery query) {
		List<Group> list = groupMapper.getGroups(query);
		return list;
	}

	@Transactional
	public void save(Group group) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_GROUP_REGION);
		}
		if (StringUtils.isEmpty(group.getGroupId())) {
			group.setGroupId(UUID32.getUUID());
			group.setSort(1);
			group.setCreateDate(new Date());
			groupMapper.insertGroup(group);
		} else {
			if (groupMapper.getGroupById(group.getGroupId()) == null) {
				group.setCreateDate(new Date());
				groupMapper.insertGroup(group);
			} else {
				group.setUpdateDate(new Date());
				groupMapper.updateGroup(group);
			}
		}
	}

	/**
	 * 保存群组用户
	 * 
	 * @param groupId
	 * @param userIds
	 */
	@Transactional
	public void saveGroupLeaders(String tenantId, String groupId, Set<String> userIds) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_GROUP_REGION);
		}
		groupLeaderMapper.deleteGroupLeadersByGroupId(groupId);
		if (userIds != null && !userIds.isEmpty()) {
			for (String userId : userIds) {
				GroupLeader g = new GroupLeader();
				g.setId(idGenerator.nextId());
				g.setGroupId(groupId);
				g.setUserId(userId);
				g.setTenantId(tenantId);
				groupLeaderMapper.insertGroupLeader(g);
			}
		}
	}

	/**
	 * 保存群组用户
	 * 
	 * @param groupId
	 * @param userIds
	 */
	@Transactional
	public void saveGroupUsers(String tenantId, String groupId, Set<String> userIds) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_GROUP_REGION);
		}
		groupUserMapper.deleteGroupUsersByGroupId(groupId);
		if (userIds != null && !userIds.isEmpty()) {
			for (String userId : userIds) {
				GroupUser gu = new GroupUser();
				gu.setId(idGenerator.nextId());
				gu.setGroupId(groupId);
				gu.setUserId(userId);
				gu.setTenantId(tenantId);
				groupUserMapper.insertGroupUser(gu);
			}
		}
	}

	/**
	 * 保存群组用户或更新
	 * 
	 * @param groupId
	 * @param userIds
	 */
	@Transactional
	public void saveOrUpdateGroupLeaders(String tenantId, String groupId, Set<String> userIds) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_GROUP_REGION);
		}
		if (userIds != null && !userIds.isEmpty()) {
			GroupQuery query = new GroupQuery();
			query.setGroupId(groupId);
			for (String userId : userIds) {
				query.setUserId(userId);
				int count = groupLeaderMapper.getGroupLeaderCount(query);
				if (count == 0) {
					GroupLeader g = new GroupLeader();
					g.setId(idGenerator.nextId());
					g.setGroupId(groupId);
					g.setUserId(userId);
					g.setTenantId(tenantId);
					groupLeaderMapper.insertGroupLeader(g);
				}
			}
		}
	}

	@Transactional
	public void saveOrUpdateGroupUsers(String tenantId, String groupId, Set<String> userIds) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_GROUP_REGION);
		}
		if (userIds != null && !userIds.isEmpty()) {
			GroupQuery query = new GroupQuery();
			query.setGroupId(groupId);
			for (String userId : userIds) {
				query.setUserId(userId);
				int count = groupUserMapper.getGroupUserCount(query);
				if (count == 0) {
					GroupUser gu = new GroupUser();
					gu.setId(idGenerator.nextId());
					gu.setGroupId(groupId);
					gu.setUserId(userId);
					gu.setTenantId(tenantId);
					groupUserMapper.insertGroupUser(gu);
				}
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setGroupLeaderMapper(GroupLeaderMapper groupLeaderMapper) {
		this.groupLeaderMapper = groupLeaderMapper;
	}

	@javax.annotation.Resource
	public void setGroupMapper(GroupMapper groupMapper) {
		this.groupMapper = groupMapper;
	}

	@javax.annotation.Resource
	public void setGroupUserMapper(GroupUserMapper groupUserMapper) {
		this.groupUserMapper = groupUserMapper;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Transactional
	public void update(Group group) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.clear(Constants.CACHE_GROUP_REGION);
		}
		group.setUpdateDate(new Date());
		groupMapper.updateGroup(group);
	}

}
