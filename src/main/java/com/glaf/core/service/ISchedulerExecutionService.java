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

@Transactional(readOnly = true)
public interface ISchedulerExecutionService {

	/**
	 * 根据主键获取一条记录
	 * 
	 * @return
	 */
	SchedulerExecution getSchedulerExecution(Long id);

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	int getSchedulerExecutionCountByQueryCriteria(SchedulerExecutionQuery query);

	/**
	 * 获取某个调度某天某个状态的记录总数
	 * 
	 * @param schedulerId
	 *            调度编号
	 * @param runDay
	 *            运行年月日
	 * @param status
	 *            状态
	 * @return
	 */
	int getSchedulerExecutionCount(String schedulerId, int runDay, int status);

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	List<SchedulerExecution> getSchedulerExecutionsByQueryCriteria(int start,
			int pageSize, SchedulerExecutionQuery query);

	/**
	 * 根据查询参数获取记录列表
	 * 
	 * @return
	 */
	List<SchedulerExecution> list(SchedulerExecutionQuery query);

	/**
	 * 保存一条记录
	 * 
	 * @return
	 */
	@Transactional
	void save(SchedulerExecution schedulerExecution);

}
