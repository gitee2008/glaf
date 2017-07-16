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

package com.glaf.core.access.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.access.domain.*;
import com.glaf.core.access.query.*;

@Transactional(readOnly = true)
public interface AccessLogService {

	@Transactional
	void bulkInsert(List<AccessLog> list);

	/**
	 * 删除某个日期以前的日志
	 * 
	 * @param dateBefore
	 */
	@Transactional
	boolean deleteAccessLogs(Date dateBefore);

	/**
	 * 根据主键删除记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteById(Long id);

	/**
	 * 根据主键删除多条记录
	 * 
	 * @return
	 */
	@Transactional
	void deleteByIds(List<Long> ids);
	
	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	AccessLog getAccessLog(Long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getAccessLogCountByQueryCriteria(AccessLogQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<AccessLog> getAccessLogsByQueryCriteria(int start, int pageSize, AccessLogQuery query);

	/**
	 * 根据条件统计某天的访问数量
	 * @param query
	 * @return
	 */
	List<AccessTotal> getAccessTotal(AccessLogQuery query);

	/**
	 * 按小时统计某天的访问数量
	 * 
	 * @param day
	 * @return
	 */
	List<AccessTotal> getHourAccessTotal(int day);

	/**
	 * 获取某个uri最新的指定条数的日志列表
	 * 
	 * @param uri
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<AccessLog> getLatestLogs(String uri, int offset, int limit);

	/**
	 * 按分钟统计某天的访问数量
	 * 
	 * @param day
	 * @return
	 */
	List<AccessTotal> getMinuteAccessTotal(int day);

	/**
	 * 获取某个模块日志总数
	 * 
	 * @param moduleId
	 * @return
	 */
	int getTotal(String uri);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<AccessLog> list(AccessLogQuery query);

}
