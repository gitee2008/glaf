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

import com.glaf.core.domain.SchedulerLog;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.mapper.SchedulerLogMapper;
import com.glaf.core.query.SchedulerLogQuery;
import com.glaf.core.service.ISchedulerLogService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.UUID32;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("schedulerLogService")
@Transactional(readOnly = true)
public class SchedulerLogServiceImpl implements ISchedulerLogService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private SqlSessionTemplate sqlSessionTemplate;

	private SchedulerLogMapper schedulerLogMapper;

	public SchedulerLogServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SchedulerLog> list) {
		for (SchedulerLog schedulerLog : list) {
			schedulerLog.setId(UUID32.getUUID());
			schedulerLog.setCreateDate(new Date());
		}
		if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
			schedulerLogMapper.bulkInsertSchedulerLog_oracle(list);
		} else {
			schedulerLogMapper.bulkInsertSchedulerLog(list);
		}
	}

	public int count(SchedulerLogQuery query) {
		return schedulerLogMapper.getSchedulerLogCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			schedulerLogMapper.deleteSchedulerLogById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				schedulerLogMapper.deleteSchedulerLogById(id);
			}
		}
	}

	@Transactional
	public void deleteSchedulerLogByTaskId(String taskId) {
		schedulerLogMapper.deleteSchedulerLogByTaskId(taskId);
	}

	public SchedulerLog getSchedulerLog(String id) {
		if (id == null) {
			return null;
		}
		return schedulerLogMapper.getSchedulerLogById(id);
	}

	/**
	 * 根据查询参数获取记录总数
	 *
	 * @return
	 */
	public int getSchedulerLogCountByQueryCriteria(SchedulerLogQuery query) {
		return schedulerLogMapper.getSchedulerLogCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 *
	 * @return
	 */
	public List<SchedulerLog> getSchedulerLogsByQueryCriteria(int start, int pageSize, SchedulerLogQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getSchedulerLogs", query, rowBounds);
	}

	public List<SchedulerLog> list(SchedulerLogQuery query) {
		return schedulerLogMapper.getSchedulerLogs(query);
	}

	@Transactional
	public void save(SchedulerLog schedulerLog) {
		if (StringUtils.isEmpty(schedulerLog.getId())) {
			schedulerLog.setId(UUID32.getUUID());
			schedulerLog.setCreateDate(new Date());
			schedulerLogMapper.insertSchedulerLog(schedulerLog);
		} else {
			if (this.getSchedulerLog(schedulerLog.getId()) == null) {
				schedulerLog.setCreateDate(new Date());
				schedulerLogMapper.insertSchedulerLog(schedulerLog);
			} else {
				schedulerLogMapper.updateSchedulerLog(schedulerLog);
			}
		}
	}

	@javax.annotation.Resource
	public void setSchedulerLogMapper(SchedulerLogMapper schedulerLogMapper) {
		this.schedulerLogMapper = schedulerLogMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
