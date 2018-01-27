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

package com.glaf.matrix.data.service.impl;

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.matrix.data.mapper.*;
import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.ExecutionLogService;

@Service("executionLogService")
@Transactional(readOnly = true)
public class ExecutionLogServiceImpl implements ExecutionLogService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected ExecutionLogMapper executionLogMapper;

	public ExecutionLogServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<ExecutionLog> list) {
		Date createTime = new Date();
		List<ExecutionLog> logs = new ArrayList<ExecutionLog>();
		for (ExecutionLog executionLog : list) {
			if (executionLog.getId() == 0) {
				executionLog.setId(idGenerator.nextId("SYS_EXECUTION_LOG"));
			}
			executionLog.setCreateTime(createTime);
			logs.add(executionLog);
			if (logs.size() > 0 && logs.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					executionLogMapper.bulkInsertExecutionLog_oracle(logs);
				} else {
					executionLogMapper.bulkInsertExecutionLog(logs);
				}
				logs.clear();
			}
		}

		if (logs.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				executionLogMapper.bulkInsertExecutionLog_oracle(logs);
			} else {
				executionLogMapper.bulkInsertExecutionLog(logs);
			}
			logs.clear();
		}
	}

	public int count(ExecutionLogQuery query) {
		return executionLogMapper.getExecutionLogCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			executionLogMapper.deleteExecutionLogById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				executionLogMapper.deleteExecutionLogById(id);
			}
		}
	}

	@Transactional
	public void deleteOverdueExecutionLogs(Date dateBefore) {
		executionLogMapper.deleteOverdueExecutionLogs(dateBefore);
	}

	@Transactional
	public void deleteTodayExecutionLogs(String type, String businessKey) {
		ExecutionLogQuery query = new ExecutionLogQuery();
		query.type(type);
		query.businessKey(businessKey);
		query.runDay(DateUtils.getNowYearMonthDay());
		executionLogMapper.deleteExecutionLogs(query);
	}

	public ExecutionLog getExecutionLog(Long id) {
		if (id == null) {
			return null;
		}
		ExecutionLog executionLog = executionLogMapper.getExecutionLogById(id);
		return executionLog;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getExecutionLogCountByQueryCriteria(ExecutionLogQuery query) {
		return executionLogMapper.getExecutionLogCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<ExecutionLog> getExecutionLogsByQueryCriteria(int start, int pageSize, ExecutionLogQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<ExecutionLog> rows = sqlSessionTemplate.selectList("getExecutionLogs", query, rowBounds);
		return rows;
	}

	public List<ExecutionLog> getLatestLogs(String type, Date dataAfter, int offset, int limit) {
		ExecutionLogQuery query = new ExecutionLogQuery();
		query.type(type);
		query.createTimeGreaterThanOrEqual(dataAfter);
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<ExecutionLog> rows = sqlSessionTemplate.selectList("getExecutionLogs", query, rowBounds);
		return rows;
	}

	/**
	 * 获取某个类型某个实例当天的执行日志
	 * 
	 * @param type
	 * @param businessKey
	 * @return
	 */
	public List<ExecutionLog> getTodayExecutionLogs(String type, String businessKey) {
		ExecutionLogQuery query = new ExecutionLogQuery();
		query.type(type);
		query.businessKey(businessKey);
		query.runDay(DateUtils.getNowYearMonthDay());
		return executionLogMapper.getExecutionLogs(query);
	}

	public int getTotal(String type, Date dataAfter) {
		ExecutionLogQuery query = new ExecutionLogQuery();
		query.type(type);
		query.createTimeGreaterThanOrEqual(dataAfter);
		return executionLogMapper.getExecutionLogCount(query);
	}

	public List<ExecutionLog> list(ExecutionLogQuery query) {
		List<ExecutionLog> list = executionLogMapper.getExecutionLogs(query);
		return list;
	}

	@Transactional
	public void save(ExecutionLog executionLog) {
		if (executionLog.getId() == 0) {
			executionLog.setId(idGenerator.nextId("SYS_EXECUTION_LOG"));
			executionLog.setCreateTime(new Date());
			executionLogMapper.insertExecutionLog(executionLog);
		} else {
			executionLogMapper.updateExecutionLog(executionLog);
		}
	}

	@Transactional
	public void saveLogs(List<ExecutionLog> logs) {
		this.bulkInsert(logs);
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setExecutionLogMapper(ExecutionLogMapper executionLogMapper) {
		this.executionLogMapper = executionLogMapper;
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
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
