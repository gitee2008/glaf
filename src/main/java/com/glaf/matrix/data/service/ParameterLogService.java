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

package com.glaf.matrix.data.service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;

@Transactional(readOnly = true)
public interface ParameterLogService {

	@Transactional
	void bulkInsert(List<ParameterLog> list);

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

	@Transactional
	void deleteOverdueParameterLogs(Date dateBefore);

	@Transactional
	void deleteTodayParameterLogs(String type, String businessKey);

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	ParameterLog getParameterLog(Long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getParameterLogCountByQueryCriteria(ParameterLogQuery query);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<ParameterLog> getParameterLogsByQueryCriteria(int start, int pageSize, ParameterLogQuery query);

	/**
	 * 获取某个类型最新的指定条数的日志列表
	 * 
	 * @param type
	 *            类型
	 * @param dataAfter
	 *            日期
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<ParameterLog> getLatestLogs(String type, Date dataAfter, int offset, int limit);

	/**
	 * 获取某个类型某个实例当天的执行日志
	 * 
	 * @param type
	 * @param businessKey
	 * @return
	 */
	List<ParameterLog> getTodayParameterLogs(String type, String businessKey);

	/**
	 * 获取某个类型某个日期之后日志总数
	 * 
	 * @param type
	 *            类型
	 * @param dataAfter
	 *            日期
	 * @return
	 */
	int getTotal(String type, Date dataAfter);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<ParameterLog> list(ParameterLogQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(ParameterLog parameterLog);

	/**
	 * 存储日志
	 * 
	 * @param logs
	 */
	@Transactional
	void saveLogs(List<ParameterLog> logs);

}
