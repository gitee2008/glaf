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

package com.glaf.base.online.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.util.DateUtils;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.*;
import com.glaf.base.online.mapper.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.base.online.domain.*;
import com.glaf.base.online.factory.OnlineLogCollectFactory;
import com.glaf.base.online.query.*;
import com.glaf.base.online.util.UserOnlineJsonFactory;

@Service("userOnlineService")
@Transactional(readOnly = true)
public class UserOnlineServiceImpl implements UserOnlineService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected UserOnlineMapper userOnlineMapper;

	protected UserOnlineLogService userOnlineLogService;

	public UserOnlineServiceImpl() {

	}

	public int count(UserOnlineQuery query) {
		return userOnlineMapper.getUserOnlineCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			UserOnline userOnline = userOnlineMapper.getUserOnlineById(id);
			if (userOnline != null) {
				if (SystemConfig.getBoolean("use_query_cache")) {
					String cacheKey = "useronline_" + userOnline.getActorId();
					CacheFactory.remove("useronline", cacheKey);
				}
				userOnlineMapper.deleteUserOnlineById(id);
			}
		}
	}

	/**
	 * 删除超时的在线用户
	 * 
	 * @param timeoutSeconds
	 */
	@Transactional
	public void deleteTimeoutUsers(int timeoutSeconds) {
		UserOnlineQuery query = new UserOnlineQuery();
		List<UserOnline> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			for (UserOnline bean : list) {
				if (bean.getCheckDateMs() != 0) {
					long ts = System.currentTimeMillis() - bean.getCheckDateMs();
					if (ts / 1000 > timeoutSeconds) {// 如果超时，从在线用户列表中删除
						userOnlineMapper.deleteUserOnlineById(bean.getId());
						userOnlineLogService.logout(bean.getActorId());
						if (SystemConfig.getBoolean("use_query_cache")) {
							String cacheKey = "useronline_" + bean.getActorId();
							CacheFactory.remove("useronline", cacheKey);
						}
					}
				}
			}
		}
	}

	/**
	 * 获取当天的用户在线列表
	 * 
	 * @return
	 */
	public List<UserOnline> getTodayUserOnlines() {
		UserOnlineQuery query = new UserOnlineQuery();
		String datetime = DateUtils.getDate(new Date());
		datetime = datetime + " 00:00:00";
		query.setLoginDateGreaterThanOrEqual(DateUtils.toDate(datetime));
		List<UserOnline> list = userOnlineMapper.getUserOnlines(query);
		return list;
	}

	public UserOnline getUserOnline(long id) {
		if (id == 0) {
			return null;
		}
		UserOnline userOnline = userOnlineMapper.getUserOnlineById(id);
		return userOnline;
	}

	public UserOnline getUserOnline(String actorId) {
		String cacheKey = "useronline_" + actorId;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("useronline", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return UserOnlineJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		UserOnlineQuery query = new UserOnlineQuery();
		query.actorId(actorId);
		List<UserOnline> list = this.list(query);
		UserOnline userOnline = null;
		if (list != null && !list.isEmpty()) {
			userOnline = list.get(0);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put("useronline", cacheKey, userOnline.toJsonObject().toJSONString());
			}
		}
		return userOnline;
	}

	public int getUserOnlineCountByQueryCriteria(UserOnlineQuery query) {
		return userOnlineMapper.getUserOnlineCount(query);
	}

	public List<UserOnline> getUserOnlinesByQueryCriteria(int start, int pageSize, UserOnlineQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<UserOnline> rows = sqlSessionTemplate.selectList("getUserOnlines", query, rowBounds);
		return rows;
	}

	public List<UserOnline> list(UserOnlineQuery query) {
		List<UserOnline> list = userOnlineMapper.getUserOnlines(query);
		return list;
	}

	@Transactional
	public void login(UserOnline model) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "useronline_" + model.getActorId();
			CacheFactory.remove("useronline", cacheKey);
		}
		UserOnline userOnline = this.getUserOnline(model.getActorId());
		if (userOnline != null) {
			userOnline.setLoginDate(model.getLoginDate());
			userOnline.setLoginIP(model.getLoginIP());
			if (model.getLoginDate() == null) {
				userOnline.setLoginDate(new Date());
			}
			userOnlineMapper.updateUserOnline(userOnline);

			UserOnlineLog log = new UserOnlineLog();
			log.setActorId(model.getActorId());
			log.setName(model.getName());
			log.setLoginDate(new Date());
			log.setLoginIP(model.getLoginIP());
			OnlineLogCollectFactory.getInstance().collectData(model.getActorId(), log);
		} else {
			if (model.getLoginDate() == null) {
				model.setLoginDate(new Date());
			}
			model.setCheckDateMs(System.currentTimeMillis());
			model.setCheckDate(new Date(model.getCheckDateMs()));
			model.setId(idGenerator.nextId());
			userOnlineMapper.insertUserOnline(model);

			UserOnlineLog log = new UserOnlineLog();
			log.setActorId(model.getActorId());
			log.setName(model.getName());
			log.setLoginIP(model.getLoginIP());
			log.setLoginDate(new Date());
			OnlineLogCollectFactory.getInstance().collectData(model.getActorId(), log);
		}
	}

	/**
	 * 退出系统
	 * 
	 * @param actorId
	 */
	@Transactional
	public void logout(String actorId) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "useronline_" + actorId;
			CacheFactory.remove("useronline", cacheKey);
		}
		UserOnline userOnline = this.getUserOnline(actorId);
		if (userOnline != null) {
			this.deleteById(userOnline.getId());
			userOnlineLogService.logout(actorId);
		}
	}

	/**
	 * 持续在线用户
	 * 
	 * @param actorId
	 */
	@Transactional
	public void remain(String actorId) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "useronline_" + actorId;
			CacheFactory.remove("useronline", cacheKey);
		}
		UserOnline userOnline = this.getUserOnline(actorId);
		if (userOnline != null) {
			userOnline.setCheckDateMs(System.currentTimeMillis());
			userOnline.setCheckDate(new Date(userOnline.getCheckDateMs()));
			userOnlineMapper.updateUserOnlineCheckDate(userOnline);
		}
	}

	@Transactional
	public void save(UserOnline userOnline) {
		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "useronline_" + userOnline.getActorId();
			CacheFactory.remove("useronline", cacheKey);
		}
		if (userOnline.getId() == 0) {
			userOnline.setId(idGenerator.nextId());
			userOnline.setCheckDateMs(System.currentTimeMillis());
			userOnline.setCheckDate(new Date(userOnline.getCheckDateMs()));
			userOnlineMapper.insertUserOnline(userOnline);

			UserOnlineLog log = new UserOnlineLog();
			log.setActorId(userOnline.getActorId());
			log.setName(userOnline.getName());
			log.setLoginIP(userOnline.getLoginIP());
			log.setLoginDate(new Date());
			OnlineLogCollectFactory.getInstance().collectData(userOnline.getActorId(), log);
		} else {
			userOnlineMapper.updateUserOnline(userOnline);
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
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setUserOnlineLogService(UserOnlineLogService userOnlineLogService) {
		this.userOnlineLogService = userOnlineLogService;
	}

	@javax.annotation.Resource
	public void setUserOnlineMapper(UserOnlineMapper userOnlineMapper) {
		this.userOnlineMapper = userOnlineMapper;
	}

}
