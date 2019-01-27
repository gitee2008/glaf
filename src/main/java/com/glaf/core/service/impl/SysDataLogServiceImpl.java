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

import com.glaf.core.domain.SysDataLog;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.mapper.SysDataLogMapper;
import com.glaf.core.query.SysDataLogQuery;
import com.glaf.core.service.SysDataLogService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sysDataLogService")
@Transactional(readOnly = true)
public class SysDataLogServiceImpl implements SysDataLogService {
	private final static Log logger = LogFactory.getLog(SysDataLogServiceImpl.class);

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private SysDataLogMapper sysLogMapper;

	public int count(SysDataLogQuery query) {
		return sysLogMapper.getSysDataLogCount(query);
	}

	/**
	 * 获取某个模块最新的指定条数的日志列表
	 *
	 * @param moduleId
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<SysDataLog> getLatestLogs(String moduleId, int offset, int limit) {
		logger.debug("moduleId:" + moduleId);
		SysDataLogQuery query = new SysDataLogQuery();
		query.setSuffix("_" + moduleId);
		// query.setModuleId(moduleId);
		query.setOrderBy(" E.CREATETIME_ desc ");
		RowBounds rowBounds = new RowBounds(offset, limit);
		return sqlSessionTemplate.selectList("getSysDataLogs", query, rowBounds);
	}

	public int getSysDataLogCountByQueryCriteria(SysDataLogQuery query) {
		return sysLogMapper.getSysDataLogCount(query);
	}

	public List<SysDataLog> getSysDataLogsByQueryCriteria(int start, int pageSize, SysDataLogQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getSysDataLogs", query, rowBounds);
	}

	/**
	 * 获取某个模块日志总数
	 *
	 * @param moduleId
	 * @return
	 */
	public int getTotal(String moduleId) {
		logger.debug("moduleId:" + moduleId);
		SysDataLogQuery query = new SysDataLogQuery();
		query.setSuffix("_" + moduleId);
		// query.setModuleId(moduleId);
		return this.getSysDataLogCountByQueryCriteria(query);
	}

	public List<SysDataLog> list(SysDataLogQuery query) {
		return sysLogMapper.getSysDataLogs(query);
	}

	@Transactional
	public void save(SysDataLog sysLog) {
		sysLog.setId(idGenerator.nextId("LOG" + sysLog.getSuffix(), "ID_"));
		sysLogMapper.insertSysDataLog(sysLog);
	}

	@Transactional
	public void saveLogs(List<SysDataLog> logs) {
		if (logs != null && !logs.isEmpty()) {
			for (SysDataLog sysLog : logs) {
				sysLog.setId(idGenerator.nextId("LOG" + sysLog.getSuffix(), "ID_"));
				sysLogMapper.insertSysDataLog(sysLog);
			}
		}
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
	public void setSysDataLogMapper(SysDataLogMapper sysLogMapper) {
		this.sysLogMapper = sysLogMapper;
	}

}
