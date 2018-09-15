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

package com.glaf.core.service;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.domain.*;
import com.glaf.core.query.*;

/**
 * 多数据库相关操作
 *
 */
@Transactional(readOnly = true)
public interface IDatabaseService {

	/**
	 * 新建一条用户的数据库访问权限
	 * 
	 * @param databaseId
	 * @param actorId
	 */
	@Transactional
	void createAccessor(long databaseId, String actorId);

	/**
	 * 删除一条用户的数据库访问权限
	 * 
	 * @param databaseId
	 * @param actorId
	 */
	@Transactional
	void deleteAccessor(long databaseId, String actorId);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(long databaseId);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<Long> ids);

	/**
	 * 获取全部数据库访问权限
	 * 
	 * @return
	 */
	List<DatabaseAccess> getAllDatabaseAccesses();

	/**
	 * 获取某个数据库访问权限
	 * 
	 * @return
	 */
	List<DatabaseAccess> getDatabaseAccesses(long databaseId);

	/**
	 * 获取某个数据库访问用户
	 * 
	 * @return
	 */
	List<String> getDatabaseAccessors(long databaseId);

	/**
	 * 根据编码获取一条数据库信息
	 * 
	 * @return
	 */
	Database getDatabaseByCode(String code);

	/**
	 * 根据主键获取一条数据库信息
	 * 
	 * @return
	 */
	Database getDatabaseById(long databaseId);

	/**
	 * 根据mapping获取一条数据库信息
	 * 
	 * @return
	 */
	Database getDatabaseByMapping(String mapping);

	/**
	 * 根据名称获取一条数据库信息
	 * 
	 * @return
	 */
	Database getDatabaseByName(String name);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getDatabaseCountByQueryCriteria(DatabaseQuery query);

	/**
	 * 获取全部数据库信息
	 * 
	 * @return
	 */
	List<Database> getDatabases();

	/**
	 * 获取某个用户能访问的数据库
	 * 
	 * @return
	 */
	List<Database> getDatabases(String actorId);

	/**
	 * 根据查询参数获取一页的数据库
	 * 
	 * @return
	 */
	List<Database> getDatabasesByQueryCriteria(int start, int pageSize, DatabaseQuery query);

	/**
	 * 保存一条数据库信息
	 * 
	 * @return
	 */
	@Transactional
	void insert(Database database);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<Database> list(DatabaseQuery query);

	/**
	 * 保存一条数据库信息
	 * 
	 * @return
	 */
	@Transactional
	void save(Database database);

	/**
	 * 保存某个数据库的访问者
	 * 
	 * @return
	 */
	@Transactional
	void saveAccessors(long databaseId, Collection<String> accessors);

	/**
	 * 保存某个用户能访问的数据库列表
	 * 
	 * @return
	 */
	@Transactional
	void saveAccessors(String accessor, Collection<Long> databaseIds);

	/**
	 * 
	 * @param 修改一条数据库信息
	 */
	@Transactional
	void update(Database database);

	/**
	 * 
	 * @param 验证一条数据库信息
	 */
	@Transactional
	void verify(Database database);

	/**
	 * 根据系统ID获取一条数据库信息
	 * 
	 * @return
	 */
	Database getDatabaseBySysId(String sysId);
}
