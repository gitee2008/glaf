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

import com.glaf.core.domain.SchedulerExecution;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.mapper.SchedulerExecutionMapper;
import com.glaf.core.query.SchedulerExecutionQuery;
import com.glaf.core.service.ISchedulerExecutionService;
import com.glaf.core.util.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("schedulerExecutionService")
@Transactional(readOnly = true)
public class SchedulerExecutionServiceImpl implements ISchedulerExecutionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private SchedulerExecutionMapper schedulerExecutionMapper;

	public SchedulerExecutionServiceImpl() {

	}

	public int count(SchedulerExecutionQuery query) {
		return schedulerExecutionMapper.getSchedulerExecutionCount(query);
	}

	public SchedulerExecution getSchedulerExecution(Long id) {
		if (id == null) {
			return null;
		}
		return schedulerExecutionMapper.getSchedulerExecutionById(id);
	}

	/**
	 * 获取某个调度某天某个状态的记录总数
	 *
	 * @param schedulerId 调度编号
	 * @param runDay      运行年月日
	 * @param status      状态
	 * @return
	 */
	public int getSchedulerExecutionCount(String schedulerId, int runDay, int status) {
		SchedulerExecutionQuery query = new SchedulerExecutionQuery();
		query.schedulerId(schedulerId);
		query.runDay(runDay);
		query.status(status);
		return schedulerExecutionMapper.getSchedulerExecutionCount(query);
	}

	/**
	 * 根据查询参数获取记录总数
	 *
	 * @return
	 */
	public int getSchedulerExecutionCountByQueryCriteria(SchedulerExecutionQuery query) {
		return schedulerExecutionMapper.getSchedulerExecutionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 *
	 * @return
	 */
	public List<SchedulerExecution> getSchedulerExecutionsByQueryCriteria(int start, int pageSize,
			SchedulerExecutionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getSchedulerExecutions", query, rowBounds);
	}

	public List<SchedulerExecution> list(SchedulerExecutionQuery query) {
		return schedulerExecutionMapper.getSchedulerExecutions(query);
	}

	@Transactional
	public void save(SchedulerExecution schedulerExecution) {
		if (schedulerExecution.getId() == null) {
			schedulerExecution.setId(idGenerator.nextId("SYS_SCHEDULER_EXECUTION"));
			schedulerExecution.setCreateTime(new Date());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int week = calendar.get(Calendar.WEEK_OF_YEAR);

			schedulerExecution.setRunYear(year);
			schedulerExecution.setRunMonth(month);
			schedulerExecution.setRunWeek(week);
			schedulerExecution.setRunDay(DateUtils.getNowYearMonthDay());

			if (month <= 3) {
				schedulerExecution.setRunQuarter(1);
			} else if (month <= 6) {
				schedulerExecution.setRunQuarter(2);
			} else if (month <= 9) {
				schedulerExecution.setRunQuarter(3);
			} else {
				schedulerExecution.setRunQuarter(4);
			}

			schedulerExecutionMapper.insertSchedulerExecution(schedulerExecution);
		}
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSchedulerExecutionMapper(SchedulerExecutionMapper schedulerExecutionMapper) {
		this.schedulerExecutionMapper = schedulerExecutionMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
