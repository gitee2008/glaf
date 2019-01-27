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
import com.alibaba.fastjson.JSONArray;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.DatabaseAccess;
import com.glaf.core.domain.SysKey;
import com.glaf.core.domain.util.DatabaseAccessJsonFactory;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.mapper.DatabaseAccessMapper;
import com.glaf.core.mapper.DatabaseMapper;
import com.glaf.core.query.DatabaseQuery;
import com.glaf.core.security.SecurityUtils;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.service.SysKeyService;
import com.glaf.core.util.UUID32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("databaseService")
@Transactional(readOnly = true)
public class DatabaseServiceImpl implements IDatabaseService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private DatabaseMapper databaseMapper;

	private DatabaseAccessMapper databaseAccessMapper;

	private SysKeyService sysKeyService;

	public DatabaseServiceImpl() {

	}

	public int count(DatabaseQuery query) {
		return databaseMapper.getDatabaseCount(query);
	}

	@Transactional
	public void createAccessor(long databaseId, String actorId) {
		String cacheKey = "sys_db_" + databaseId;
		CacheFactory.remove("database", cacheKey);
		cacheKey = "sys_dbaccess_" + databaseId;
		CacheFactory.remove("database", cacheKey);
		cacheKey = "sys_db_actor_" + actorId;
		CacheFactory.remove("database", cacheKey);
		DatabaseAccess model = new DatabaseAccess();
		model.setId(idGenerator.nextId("SYS_DATABASE_ACCESS"));
		model.setActorId(actorId);
		model.setDatabaseId(databaseId);
		databaseAccessMapper.insertDatabaseAccess(model);
	}

	@Transactional
	public void deleteAccessor(long databaseId, String actorId) {
		String cacheKey = "sys_db_" + databaseId;
		CacheFactory.remove("database", cacheKey);
		cacheKey = "sys_dbaccess_" + databaseId;
		CacheFactory.remove("database", cacheKey);
		cacheKey = "sys_db_actor_" + actorId;
		CacheFactory.remove("database", cacheKey);
		DatabaseAccess model = new DatabaseAccess();
		model.setActorId(actorId);
		model.setDatabaseId(databaseId);
		databaseAccessMapper.deleteAccessor(model);
	}

	@Transactional
	public void deleteById(long databaseId) {
		if (databaseId != 0) {
			Database database = this.getDatabase(databaseId);
			if (database.getRemoveFlag() == null || StringUtils.equalsIgnoreCase(database.getRemoveFlag(), "Y")) {
				String cacheKey = "sys_db_" + databaseId;
				CacheFactory.remove("database", cacheKey);
				cacheKey = "sys_dbaccess_" + databaseId;
				CacheFactory.remove("database", cacheKey);
				databaseAccessMapper.deleteDatabaseAccessByDatabaseId(databaseId);
				sysKeyService.deleteById("sys_database_" + database.getId());
				databaseMapper.deleteDatabaseById(databaseId);
			}
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long databaseId : ids) {
				String cacheKey = "sys_db_" + databaseId;
				CacheFactory.remove("database", cacheKey);
				cacheKey = "sys_dbaccess_" + databaseId;
				CacheFactory.remove("database", cacheKey);
				databaseAccessMapper.deleteDatabaseAccessByDatabaseId(databaseId);
				sysKeyService.deleteById("sys_database_" + databaseId);
				databaseMapper.deleteDatabaseById(databaseId);
			}
		}
	}

	public List<DatabaseAccess> getAllDatabaseAccesses() {
		return databaseAccessMapper.getAllDatabaseAccesses();
	}

	private Database getDatabase(long databaseId) {
		if (databaseId == 0) {
			return null;
		}
		return databaseMapper.getDatabaseById(databaseId);
	}

	/**
	 * 获取某个数据库访问权限
	 *
	 * @return
	 */
	public List<DatabaseAccess> getDatabaseAccesses(long databaseId) {
		String cacheKey = "sys_dbaccess_" + databaseId;
		String text = CacheFactory.getString("database", cacheKey);
		if (StringUtils.isNotEmpty(text)) {
			try {
				JSONArray array = JSON.parseArray(text);
				return DatabaseAccessJsonFactory.arrayToList(array);
			} catch (Exception ignored) {
			}
		}
		List<DatabaseAccess> accesses = databaseAccessMapper.getDatabaseAccessesByDatabaseId(databaseId);
		if (accesses != null && !accesses.isEmpty()) {
			JSONArray array = DatabaseAccessJsonFactory.listToArray(accesses);
			CacheFactory.put("database", cacheKey, array.toJSONString());
		}
		return accesses;
	}

	/**
	 * 获取某个数据库访问用户
	 *
	 * @return
	 */
	public List<String> getDatabaseAccessors(long databaseId) {
		List<String> actorIds = new ArrayList<String>();
		List<DatabaseAccess> accesses = this.getDatabaseAccesses(databaseId);
		if (accesses != null && !accesses.isEmpty()) {
			for (DatabaseAccess access : accesses) {
				if (!actorIds.contains(access.getActorId())) {
					actorIds.add(access.getActorId());
				}
			}
		}
		return actorIds;
	}

	/**
	 * 根据编码获取一条记录
	 *
	 * @return
	 */
	public Database getDatabaseByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		List<Database> list = databaseMapper.getDatabases(query);
		if (list != null && !list.isEmpty()) {
			for (Database database : list) {
				if (StringUtils.equals(database.getActive(), "1") && StringUtils.equals(code, database.getCode())) {
					List<DatabaseAccess> accesses = this.getDatabaseAccesses(database.getId());
					database.setAccesses(accesses);
					return database;
				}
			}
		}
		return null;
	}

	public Database getDatabaseById(long databaseId) {
		if (databaseId == 0) {
			return null;
		}
		Database database = databaseMapper.getDatabaseById(databaseId);
		if (database != null) {
			List<DatabaseAccess> accesses = databaseAccessMapper.getDatabaseAccessesByDatabaseId(databaseId);
			if (accesses != null && !accesses.isEmpty()) {
				for (DatabaseAccess access : accesses) {
					database.addAccessor(access.getActorId());
				}
			}
		}
		return database;
	}

	/**
	 * 根据mapping获取一条记录
	 *
	 * @return
	 */
	public Database getDatabaseByMapping(String mapping) {
		if (StringUtils.isEmpty(mapping)) {
			return null;
		}
		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		List<Database> list = databaseMapper.getDatabases(query);
		if (list != null && !list.isEmpty()) {
			for (Database database : list) {
				if (StringUtils.equals(database.getActive(), "1")
						&& StringUtils.equals(mapping, database.getMapping())) {
					List<DatabaseAccess> accesses = this.getDatabaseAccesses(database.getId());
					database.setAccesses(accesses);
					return database;
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
	public Database getDatabaseByName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		DatabaseQuery query = new DatabaseQuery();
		query.active("1");
		List<Database> list = databaseMapper.getDatabases(query);
		if (list != null && !list.isEmpty()) {
			for (Database database : list) {
				if (StringUtils.equals(database.getActive(), "1") && StringUtils.equals(name, database.getName())) {
					List<DatabaseAccess> accesses = this.getDatabaseAccesses(database.getId());
					database.setAccesses(accesses);
					return database;
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
	public int getDatabaseCountByQueryCriteria(DatabaseQuery query) {
		return databaseMapper.getDatabaseCount(query);
	}

	public List<Database> getDatabases() {
		List<Database> databases = new ArrayList<Database>();
		DatabaseQuery query = new DatabaseQuery();
		List<Database> list = databaseMapper.getDatabases(query);
		if (list != null && !list.isEmpty()) {
			for (Database database : list) {
				if (StringUtils.equals(database.getActive(), "1")) {
					List<DatabaseAccess> accesses = this.getDatabaseAccesses(database.getId());
					database.setAccesses(accesses);
					if (accesses != null && !accesses.isEmpty()) {
						for (DatabaseAccess access : accesses) {
							database.addAccessor(access.getActorId());
						}
					}
					databases.add(database);
				}
			}
		}
		logger.debug("databases size:" + databases.size());
		if (!databases.isEmpty()) {
			Collections.sort(databases);
		}
		return databases;
	}

	/**
	 * 根据查询参数获取记录列表
	 *
	 * @return
	 */
	public List<Database> getDatabases(String actorId) {
		List<Database> databases = new ArrayList<Database>();
		DatabaseQuery query = new DatabaseQuery();
		query.setCreateBy(actorId);
		List<Database> list = databaseMapper.getDatabases(query);
		if (list != null && !list.isEmpty()) {
			for (Database database : list) {
				if (StringUtils.equals(database.getActive(), "1")) {
					List<DatabaseAccess> accesses = this.getDatabaseAccesses(database.getId());
					database.setAccesses(accesses);
					if (accesses != null && !accesses.isEmpty()) {
						for (DatabaseAccess access : accesses) {
							database.addAccessor(access.getActorId());
						}
					}
					databases.add(database);
				}
			}
		}

		List<Database> list2 = databaseMapper.getDatabasesByActorId(actorId);
		if (list2 != null && !list2.isEmpty()) {
			databases.addAll(list2);
		}

		return databases;
	}

	/**
	 * 根据查询参数获取一页的数据
	 *
	 * @return
	 */
	public List<Database> getDatabasesByQueryCriteria(int start, int pageSize, DatabaseQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getDatabases", query, rowBounds);
	}

	/**
	 * 保存一条数据库信息
	 *
	 * @return
	 */
	@Transactional
	public void insert(Database database) {
		database.setId(idGenerator.nextId("SYS_DATABASE"));
		database.setName("db_" + database.getId());
		database.setCode("db_" + database.getId());
		database.setToken(UUID32.getUUID() + UUID32.getUUID());
		database.setCreateTime(new Date());
		database.setActive("1");

		if (database.getIntToken() < 1000 || database.getIntToken() > 9999) {
			database.setIntToken(Math.abs(1000 + new Random().nextInt(8999)));
		}

		databaseMapper.insertDatabase(database);
	}

	public List<Database> list(DatabaseQuery query) {
		List<Database> list = databaseMapper.getDatabases(query);
		for (Database database : list) {
			if (StringUtils.equals(database.getActive(), "1")) {
				List<DatabaseAccess> accesses = this.getDatabaseAccesses(database.getId());
				database.setAccesses(accesses);
				if (accesses != null && !accesses.isEmpty()) {
					for (DatabaseAccess access : accesses) {
						database.addAccessor(access.getActorId());
					}
				}
			}
		}
		return list;
	}

	@Transactional
	public void save(Database database) {
		String password = database.getPassword();
		if (database.getId() == 0) {
			SysKey sysKey = new SysKey();
			sysKey.setCreateBy(database.getCreateBy());
			sysKey.setCreateDate(new Date());
			sysKey.setType("DATABASE");

			if (!"88888888".equals(password) && password.length() != 32) {
				String key = SecurityUtils.genKey();
				String pass = SecurityUtils.encode(key, password);
				database.setKey(key);
				database.setPassword(pass);
				sysKey.setData(Base64.encodeBase64(key.getBytes()));
			}

			database.setId(idGenerator.nextId("SYS_DATABASE"));
			database.setName("db_" + database.getId());
			database.setCode("db_" + database.getId());
			database.setToken(UUID32.getUUID() + UUID32.getUUID());
			database.setCreateTime(new Date());
			database.setActive("1");

			if (database.getIntToken() < 1000 || database.getIntToken() > 9999) {
				database.setIntToken(Math.abs(1000 + new Random().nextInt(8999)));
			}

			sysKey.setId("sys_database_" + database.getId());

			if (StringUtils.isEmpty(database.getQueueName())) {
				String queueName = database.getSysId();
				if (StringUtils.isEmpty(queueName)) {
					// queueName = database.getHost() + "_" +
					// database.getDbname();
					// queueName = StringTools.replace(queueName, ".", "_");
					queueName = database.getSysId();
					// queueName=queueName.toLowerCase();
				}
				database.setQueueName(queueName);
			}

			databaseMapper.insertDatabase(database);

			sysKey.setName(database.getName());
			sysKey.setTitle(database.getTitle());
			sysKeyService.save(sysKey);

		} else {
			Database model = this.getDatabase(database.getId());

			if (model.getIntToken() != database.getIntToken()) {
				model.setToken(UUID32.getUUID() + UUID32.getUUID());
			}
			model.setId(database.getId());
			model.setTitle(database.getTitle());
			model.setProviderClass(database.getProviderClass());
			model.setRemoteUrl(database.getRemoteUrl());
			model.setLevel(database.getLevel());
			model.setPriority(database.getPriority());
			model.setOperation(database.getOperation());
			model.setHost(database.getHost());
			model.setPort(database.getPort());
			model.setUser(database.getUser());
			model.setActive(database.getActive());
			model.setParentId(database.getParentId());
			model.setMapping(database.getMapping());
			model.setRunType(database.getRunType());
			model.setUseType(database.getUseType());
			model.setSection(database.getSection());
			model.setCatalog(database.getCatalog());
			model.setInfoServer(database.getInfoServer());
			model.setLoginAs(database.getLoginAs());
			model.setLoginUrl(database.getLoginUrl());
			model.setTicket(database.getTicket());
			model.setProgramId(database.getProgramId());
			model.setProgramName(database.getProgramName());
			model.setUserNameKey(database.getUserNameKey());
			model.setServerId(database.getServerId());
			model.setSysId(database.getSysId());
			model.setQueueName(database.getQueueName());
			model.setDiscriminator(database.getDiscriminator());
			model.setIntToken(database.getIntToken());
			model.setToken(database.getToken());
			model.setSort(database.getSort());
			model.setUpdateTime(new Date());

			if (model.getIntToken() < 1000 || model.getIntToken() > 9999) {
				model.setIntToken(Math.abs(1000 + new Random().nextInt(8999)));
				model.setToken(UUID32.getUUID() + UUID32.getUUID());
			}

			SysKey sysKey = new SysKey();
			sysKey.setId("sys_database_" + database.getId());
			sysKey.setCreateBy(database.getCreateBy());
			sysKey.setCreateDate(new Date());
			sysKey.setName(database.getName());
			sysKey.setTitle(database.getTitle());
			sysKey.setType("DATABASE");

			if (!"88888888".equals(password) && password.length() != 32) {
				String key = SecurityUtils.genKey();
				String pass = SecurityUtils.encode(key, password);
				model.setKey(key);
				model.setPassword(pass);
				model.setVerify("N");
				sysKey.setData(Base64.encodeBase64(key.getBytes()));
			}

			if (!StringUtils.equals(model.getDbname(), database.getDbname())) {
				model.setVerify("N");
			}
			if (!StringUtils.equals(model.getHost(), database.getHost())) {
				model.setVerify("N");
			}
			if (!StringUtils.equals(model.getUser(), database.getUser())) {
				model.setVerify("N");
			}
			/**
			 * 只有没有初始化时可以更新库名及表名
			 */
			if (!StringUtils.equals(model.getInitFlag(), "Y")) {
				model.setDbname(database.getDbname());
			}

			if (StringUtils.isEmpty(model.getToken())) {
				model.setToken(UUID32.getUUID() + UUID32.getUUID());
			}

			if (StringUtils.isEmpty(model.getQueueName())) {
				// String queueName = model.getHost() + "_" + model.getDbname();
				// queueName = StringTools.replace(queueName, ".", "_");
				// model.setQueueName(queueName.toLowerCase());
				String queueName = model.getSysId();
				model.setQueueName(queueName);
			}

			databaseMapper.updateDatabase(model);

			sysKeyService.save(sysKey);

			String cacheKey = "sys_db_" + model.getId();
			CacheFactory.remove("database", cacheKey);

			if (model.getCode() != null) {
				cacheKey = "sys_db_" + model.getCode();
				CacheFactory.remove("database", cacheKey);
			}

			if (model.getName() != null) {
				cacheKey = "sys_db_" + model.getName();
				CacheFactory.remove("database", cacheKey);
			}

			if (model.getMapping() != null) {
				cacheKey = "sys_db_" + model.getMapping();
				CacheFactory.remove("database", cacheKey);
			}

			cacheKey = "sys_dbaccess_" + model.getId();
			CacheFactory.remove("database", cacheKey);
		}

		databaseAccessMapper.deleteDatabaseAccessByDatabaseId(database.getId());
		if (database.getActorIds() != null && database.getActorIds().isEmpty()) {
			for (String actorId : database.getActorIds()) {
				DatabaseAccess access = new DatabaseAccess();
				access.setId(idGenerator.nextId("SYS_DATABASE_ACCESS"));
				access.setActorId(actorId);
				access.setDatabaseId(database.getId());
				databaseAccessMapper.insertDatabaseAccess(access);
			}
		}
	}

	/**
	 * 保存数据库访问者
	 *
	 * @return
	 */
	@Transactional
	public void saveAccessors(long databaseId, Collection<String> accessors) {
		databaseAccessMapper.deleteDatabaseAccessByDatabaseId(databaseId);
		for (String actorId : accessors) {
			String cacheKey = "sys_db_" + databaseId;
			CacheFactory.remove("database", cacheKey);
			cacheKey = "sys_dbaccess_" + databaseId;
			CacheFactory.remove("database", cacheKey);
			cacheKey = "sys_db_actor_" + actorId;
			CacheFactory.remove("database", cacheKey);
			DatabaseAccess access = new DatabaseAccess();
			access.setId(idGenerator.nextId("SYS_DATABASE_ACCESS"));
			access.setActorId(actorId);
			access.setDatabaseId(databaseId);
			databaseAccessMapper.insertDatabaseAccess(access);
		}
	}

	/**
	 * 保存数据库访问者
	 *
	 * @return
	 */
	@Transactional
	public void saveAccessors(String accessor, Collection<Long> databaseIds) {
		databaseAccessMapper.deleteDatabaseAccessByActorId(accessor);
		for (Long databaseId : databaseIds) {
			String cacheKey = "sys_db_" + databaseId;
			CacheFactory.remove("database", cacheKey);
			cacheKey = "sys_dbaccess_" + databaseId;
			CacheFactory.remove("database", cacheKey);
			cacheKey = "sys_db_actor_" + accessor;
			CacheFactory.remove("database", cacheKey);
			DatabaseAccess access = new DatabaseAccess();
			access.setId(idGenerator.nextId("SYS_DATABASE_ACCESS"));
			access.setActorId(accessor);
			access.setDatabaseId(databaseId);
			databaseAccessMapper.insertDatabaseAccess(access);
		}
	}

	@javax.annotation.Resource
	public void setDatabaseAccessMapper(DatabaseAccessMapper databaseAccessMapper) {
		this.databaseAccessMapper = databaseAccessMapper;
	}

	@javax.annotation.Resource
	public void setDatabaseMapper(DatabaseMapper databaseMapper) {
		this.databaseMapper = databaseMapper;
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
	public void setSysKeyService(SysKeyService sysKeyService) {
		this.sysKeyService = sysKeyService;
	}

	@Transactional
	public void update(Database database) {
		String cacheKey = "sys_db_" + database.getId();
		CacheFactory.remove("database", cacheKey);
		if (database.getCode() != null) {
			cacheKey = "sys_db_" + database.getCode();
			CacheFactory.remove("database", cacheKey);
		}
		if (database.getName() != null) {
			cacheKey = "sys_db_" + database.getName();
			CacheFactory.remove("database", cacheKey);
		}
		if (database.getMapping() != null) {
			cacheKey = "sys_db_" + database.getMapping();
			CacheFactory.remove("database", cacheKey);
		}

		cacheKey = "sys_dbaccess_" + database.getId();
		CacheFactory.remove("database", cacheKey);
		Database model = this.getDatabase(database.getId());

		if (model.getIntToken() != database.getIntToken()) {
			model.setToken(UUID32.getUUID() + UUID32.getUUID());
		}

		model.setActive(database.getActive());
		model.setVerify(database.getVerify());
		model.setInitFlag(database.getInitFlag());

		if (model.getIntToken() < 1000 || model.getIntToken() > 9999) {
			model.setIntToken(Math.abs(1000 + new Random().nextInt(8999)));
			model.setToken(UUID32.getUUID() + UUID32.getUUID());
		}

		if (StringUtils.isEmpty(model.getToken())) {
			model.setToken(UUID32.getUUID() + UUID32.getUUID());
		}

		databaseMapper.updateDatabase(model);
	}

	@Transactional
	public void verify(Database database) {
		Database model = this.getDatabase(database.getId());
		model.setActive(database.getActive());
		model.setVerify(database.getVerify());
		model.setInitFlag(database.getInitFlag());
		model.setUpdateBy(database.getUpdateBy());
		model.setUpdateTime(new Date());
		databaseMapper.verifyDatabase(model);
	}

	public Database getDatabaseBySysId(String sysId) {
		if (StringUtils.isEmpty(sysId)) {
			return null;
		}
		return databaseMapper.getDatabaseBySysId(sysId);
	}

}
