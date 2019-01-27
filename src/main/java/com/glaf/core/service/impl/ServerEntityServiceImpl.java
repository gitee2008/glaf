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

package com.glaf.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.domain.ServerEntity;
import com.glaf.core.domain.util.ServerEntityJsonFactory;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.mapper.ServerEntityMapper;
import com.glaf.core.query.ServerEntityQuery;
import com.glaf.core.security.SecurityUtils;
import com.glaf.core.service.IServerEntityService;
import com.glaf.core.util.UUID32;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("serverEntityService")
@Transactional(readOnly = true)
public class ServerEntityServiceImpl implements IServerEntityService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private ServerEntityMapper serverEntityMapper;

	public ServerEntityServiceImpl() {

	}

	public int count(ServerEntityQuery query) {
		return serverEntityMapper.getServerEntityCount(query);
	}

	@Transactional
	public void deleteById(Long serverEntityId) {
		if (serverEntityId != null) {
			String cacheKey = "SYS_SERVER_" + serverEntityId;
			CacheFactory.remove("server", cacheKey);
			serverEntityMapper.deleteServerEntityById(serverEntityId);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long serverEntityId : ids) {
				String cacheKey = "SYS_SERVER_" + serverEntityId;
				CacheFactory.remove("server", cacheKey);
				serverEntityMapper.deleteServerEntityById(serverEntityId);
			}
		}
	}

	public List<ServerEntity> getServerEntities() {
		List<ServerEntity> serverEntitys = new ArrayList<ServerEntity>();
		ServerEntityQuery query = new ServerEntityQuery();
		List<ServerEntity> list = serverEntityMapper.getServerEntities(query);
		if (list != null && !list.isEmpty()) {
			for (ServerEntity serverEntity : list) {
				if (StringUtils.equals(serverEntity.getActive(), "1")) {
					serverEntitys.add(serverEntity);
				}
			}
		}
		logger.debug("servers size:" + serverEntitys.size());
		if (!serverEntitys.isEmpty()) {
			Collections.sort(serverEntitys);
		}
		return serverEntitys;
	}

	/**
	 * 根据查询参数获取记录列表
	 *
	 * @return
	 */
	public List<ServerEntity> getServerEntities(String actorId) {
		List<ServerEntity> serverEntitys = new ArrayList<ServerEntity>();
		ServerEntityQuery query = new ServerEntityQuery();
		query.setCreateBy(actorId);
		List<ServerEntity> list = serverEntityMapper.getServerEntities(query);
		if (list != null && !list.isEmpty()) {
			for (ServerEntity serverEntity : list) {
				if (StringUtils.equals(serverEntity.getActive(), "1")) {
					serverEntitys.add(serverEntity);
				}
			}
		}

		return serverEntitys;
	}

	/**
	 * 根据查询参数获取一页的数据
	 *
	 * @return
	 */
	public List<ServerEntity> getServerEntitiesByQueryCriteria(int start, int pageSize, ServerEntityQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getServerEntities", query, rowBounds);
	}

	private ServerEntity getServerEntity(Long serverEntityId) {
		if (serverEntityId == null || serverEntityId == 0) {
			return null;
		}
		return serverEntityMapper.getServerEntityById(serverEntityId);
	}

	/**
	 * 根据编码获取一条记录
	 *
	 * @return
	 */
	public ServerEntity getServerEntityByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		String cacheKey = "SYS_SERVER_" + code;
		String text = CacheFactory.getString("server", cacheKey);
		if (StringUtils.isNotEmpty(text)) {
			try {
				JSONObject json = JSON.parseObject(text);
				return ServerEntityJsonFactory.jsonToObject(json);
			} catch (Exception ignored) {
			}
		}
		ServerEntityQuery query = new ServerEntityQuery();
		query.active("1");
		List<ServerEntity> list = serverEntityMapper.getServerEntities(query);
		if (list != null && !list.isEmpty()) {
			for (ServerEntity serverEntity : list) {
				if (StringUtils.equals(serverEntity.getActive(), "1")
						&& StringUtils.equals(code, serverEntity.getCode())) {
					CacheFactory.put("server", cacheKey, serverEntity.toJsonObject().toJSONString());
					return serverEntity;
				}
			}
		}
		return null;
	}

	public ServerEntity getServerEntityById(Long serverEntityId) {
		if (serverEntityId == null || serverEntityId == 0) {
			return null;
		}
		String cacheKey = "SYS_SERVER_" + serverEntityId;
		String text = CacheFactory.getString("server", cacheKey);
		if (StringUtils.isNotEmpty(text)) {
			try {
				JSONObject json = JSON.parseObject(text);
				return ServerEntityJsonFactory.jsonToObject(json);
			} catch (Exception ignored) {
			}
		}
		ServerEntity serverEntity = serverEntityMapper.getServerEntityById(serverEntityId);
		if (serverEntity != null) {
			CacheFactory.put("server", cacheKey, serverEntity.toJsonObject().toJSONString());
		}
		return serverEntity;
	}

	/**
	 * 根据mapping获取一条记录
	 *
	 * @return
	 */
	public ServerEntity getServerEntityByMapping(String mapping) {
		if (StringUtils.isEmpty(mapping)) {
			return null;
		}
		String cacheKey = "SYS_SERVER_" + mapping;
		String text = CacheFactory.getString("server", cacheKey);
		if (StringUtils.isNotEmpty(text)) {
			try {
				JSONObject json = JSON.parseObject(text);
				return ServerEntityJsonFactory.jsonToObject(json);
			} catch (Exception ignored) {
			}
		}
		ServerEntityQuery query = new ServerEntityQuery();
		query.active("1");
		List<ServerEntity> list = serverEntityMapper.getServerEntities(query);
		if (list != null && !list.isEmpty()) {
			for (ServerEntity serverEntity : list) {
				if (StringUtils.equals(serverEntity.getActive(), "1")
						&& StringUtils.equals(mapping, serverEntity.getMapping())) {
					CacheFactory.put("server", cacheKey, serverEntity.toJsonObject().toJSONString());
					return serverEntity;
				}
			}
		}
		return null;
	}

	/**
	 * 根据name获取一条记录
	 *
	 * @return
	 */
	public ServerEntity getServerEntityByName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		String cacheKey = "SYS_SERVER_" + name;
		String text = CacheFactory.getString("server", cacheKey);
		if (StringUtils.isNotEmpty(text)) {
			try {
				JSONObject json = JSON.parseObject(text);
				return ServerEntityJsonFactory.jsonToObject(json);
			} catch (Exception ignored) {
			}
		}
		ServerEntityQuery query = new ServerEntityQuery();
		query.active("1");
		List<ServerEntity> list = serverEntityMapper.getServerEntities(query);
		if (list != null && !list.isEmpty()) {
			for (ServerEntity serverEntity : list) {
				if (StringUtils.equals(serverEntity.getActive(), "1")
						&& StringUtils.equals(name, serverEntity.getName())) {
					CacheFactory.put("server", cacheKey, serverEntity.toJsonObject().toJSONString());
					return serverEntity;
				}
			}
		}
		return null;
	}

	/**
	 * 根据查询参数获取记录总数
	 *
	 * @return
	 */
	public int getServerEntityCountByQueryCriteria(ServerEntityQuery query) {
		return serverEntityMapper.getServerEntityCount(query);
	}

	public List<ServerEntity> list(ServerEntityQuery query) {
		return serverEntityMapper.getServerEntities(query);
	}

	@Transactional
	public void save(ServerEntity serverEntity) {
		String password = serverEntity.getPassword();
		if (serverEntity.getId() == 0) {
			if (!"88888888".equals(password)) {
				String key = SecurityUtils.genKey();
				String pass = SecurityUtils.encode(key, password);
				serverEntity.setKey(key);
				serverEntity.setPassword(pass);
			}
			serverEntity.setId(idGenerator.nextId("SYS_SERVER"));
			serverEntity.setName("server_" + serverEntity.getId());
			serverEntity.setCode("server_" + serverEntity.getId());
			serverEntity.setCreateTime(new Date());
			serverEntity.setActive("1");
			if (StringUtils.isEmpty(serverEntity.getSecretKey())) {
				if (StringUtils.equals("DES", serverEntity.getSecretAlgorithm())) {
					serverEntity.setSecretKey(SecurityUtils.genKey2048());
					serverEntity.setSecretIv(UUID32.getUUID());
				}
			}
			serverEntityMapper.insertServerEntity(serverEntity);
		} else {
			ServerEntity model = this.getServerEntity(serverEntity.getId());
			model.setId(serverEntity.getId());
			model.setTitle(serverEntity.getTitle());
			model.setDiscriminator(serverEntity.getDiscriminator());
			model.setProviderClass(serverEntity.getProviderClass());
			model.setAttribute(serverEntity.getAttribute());
			model.setAddressPerms(serverEntity.getAddressPerms());
			model.setPerms(serverEntity.getPerms());
			model.setLevel(serverEntity.getLevel());
			model.setPriority(serverEntity.getPriority());
			model.setOperation(serverEntity.getOperation());
			model.setHost(serverEntity.getHost());
			model.setPort(serverEntity.getPort());
			model.setActive(serverEntity.getActive());
			model.setDetectionFlag(serverEntity.getDetectionFlag());
			model.setNodeId(serverEntity.getNodeId());
			model.setMapping(serverEntity.getMapping());
			model.setPath(serverEntity.getPath());
			model.setProgram(serverEntity.getProgram());
			model.setCatalog(serverEntity.getCatalog());
			model.setUpdateTime(new Date());
			model.setUser(serverEntity.getUser());
			model.setSecretAlgorithm(serverEntity.getSecretAlgorithm());
			model.setSecretKey(serverEntity.getSecretKey());
			model.setSecretIv(serverEntity.getSecretIv());

			if (!"88888888".equals(password)) {
				String key = SecurityUtils.genKey();
				String pass = SecurityUtils.encode(key, password);
				model.setKey(key);
				model.setPassword(pass);
			}

			if (StringUtils.isEmpty(model.getSecretKey())) {
				if (StringUtils.equals("DES", model.getSecretAlgorithm())) {
					model.setSecretKey(SecurityUtils.genKey2048());
					model.setSecretIv(UUID32.getUUID());
				}
			}

			/**
			 * 只有没有初始化时可以更新库名及表名
			 */
			if (!StringUtils.equals(model.getInitFlag(), "Y")) {
				model.setDbname(serverEntity.getDbname());
			}
			serverEntityMapper.updateServerEntity(model);

			String cacheKey = "SYS_SERVER_" + model.getId();
			CacheFactory.remove("server", cacheKey);

			if (model.getCode() != null) {
				cacheKey = "SYS_SERVER_" + model.getCode();
				CacheFactory.remove("server", cacheKey);
			}

			if (model.getName() != null) {
				cacheKey = "SYS_SERVER_" + model.getName();
				CacheFactory.remove("server", cacheKey);
			}

			if (model.getMapping() != null) {
				cacheKey = "SYS_SERVER_" + model.getMapping();
				CacheFactory.remove("server", cacheKey);
			}

		}

	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setServerEntityMapper(ServerEntityMapper serverEntityMapper) {
		this.serverEntityMapper = serverEntityMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Transactional
	public void update(ServerEntity serverEntity) {
		String cacheKey = "SYS_SERVER_" + serverEntity.getId();
		CacheFactory.remove("server", cacheKey);
		if (serverEntity.getCode() != null) {
			cacheKey = "SYS_SERVER_" + serverEntity.getCode();
			CacheFactory.remove("server", cacheKey);
		}
		if (serverEntity.getName() != null) {
			cacheKey = "SYS_SERVER_" + serverEntity.getName();
			CacheFactory.remove("server", cacheKey);
		}
		if (serverEntity.getMapping() != null) {
			cacheKey = "SYS_SERVER_" + serverEntity.getMapping();
			CacheFactory.remove("server", cacheKey);
		}

		ServerEntity model = this.getServerEntity(serverEntity.getId());
		model.setActive(serverEntity.getActive());
		model.setVerify(serverEntity.getVerify());
		model.setInitFlag(serverEntity.getInitFlag());
		serverEntityMapper.updateServerEntity(model);
	}

}
