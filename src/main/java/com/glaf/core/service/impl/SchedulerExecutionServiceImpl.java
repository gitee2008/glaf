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

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.mapper.*;
import com.glaf.core.domain.*;
import com.glaf.core.query.*;
import com.glaf.core.service.ISchedulerExecutionService;
import com.glaf.core.util.DateUtils;

@Service("schedulerExecutionService")
@Transactional(readOnly = true)
public class SchedulerExecutionServiceImpl implements
		ISchedulerExecutionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SchedulerExecutionMapper schedulerExecutionMapper;

	public SchedulerExecutionServiceImpl() {

	}

	public int count(SchedulerExecutionQuery query) {
		return schedulerExecutionMapper.getSchedulerExecutionCount(query);
	}

	public SchedulerExecution getSchedulerExecution(Long id) {
		if (id == null) {
			return null;
		}
		SchedulerExecution schedulerExecution = schedulerExecutionMapper
				.getSchedulerExecutionById(id);
		return schedulerExecution;
	}

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
	public int getSchedulerExecutionCount(String schedulerId, int runDay,
			int status) {
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
	public int getSchedulerExecutionCountByQueryCriteria(
			SchedulerExecutionQuery query) {
		return schedulerExecutionMapper.getSchedulerExecutionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SchedulerExecution> getSchedulerExecutionsByQueryCriteria(
			int start, int pageSize, SchedulerExecutionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SchedulerExecution> rows = sqlSessionTemplate.selectList(
				"getSchedulerExecutions", query, rowBounds);
		return rows;
	}

	public List<SchedulerExecution> list(SchedulerExecutionQuery query) {
		List<SchedulerExecution> list = schedulerExecutionMapper
				.getSchedulerExecutions(query);
		return list;
	}

	@Transactional
	public void save(SchedulerExecution schedulerExecution) {
		if (schedulerExecution.getId() == null) {
			schedulerExecution.setId(idGenerator
					.nextId("SYS_SCHEDULER_EXECUTION"));
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
			} else if (month > 3 && month <= 6) {
				schedulerExecution.setRunQuarter(2);
			}
			if (month > 6 && month <= 9) {
				schedulerExecution.setRunQuarter(3);
			}
			if (month > 9) {
				schedulerExecution.setRunQuarter(4);
			}

			schedulerExecutionMapper
					.insertSchedulerExecution(schedulerExecution);
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
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSchedulerExecutionMapper(
			SchedulerExecutionMapper schedulerExecutionMapper) {
		this.schedulerExecutionMapper = schedulerExecutionMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
