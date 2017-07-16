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

package com.glaf.core.access.service.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.access.mapper.*;
import com.glaf.core.access.domain.*;
import com.glaf.core.access.query.*;
import com.glaf.core.access.service.AccessLogService;

@Service("accessLogService")
@Transactional(readOnly = true)
public class AccessLogServiceImpl implements AccessLogService {
	protected static ConcurrentMap<String, Long> uriMap = new ConcurrentHashMap<String, Long>();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected AccessLogMapper accessLogMapper;

	protected AccessUriMapper accessUriMapper;

	public AccessLogServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<AccessLog> list) {
		Calendar cal = Calendar.getInstance();
		for (AccessLog accessLog : list) {
			if (accessLog.getId() == 0) {
				accessLog.setId(idGenerator.nextId("ACCESS_LOG"));
			}
			cal.setTime(accessLog.getAccessTime());
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			accessLog.setDay(DateUtils.getYearMonthDay(accessLog.getAccessTime()));
			accessLog.setHour(hour);
			accessLog.setMinute(minute);

			if (StringUtils.isNotEmpty(accessLog.getUri())) {
				if (uriMap.get(accessLog.getUri()) == null) {
					AccessUri bean = accessUriMapper.getAccessUriByUri(accessLog.getUri());
					if (bean != null) {
						uriMap.put(accessLog.getUri(), bean.getId());
					} else {
						bean = new AccessUri();
						bean.setUri(accessLog.getUri());
						bean.setId(idGenerator.nextId("SYS_ACCESS_URI"));
						accessUriMapper.insertAccessUri(bean);
						uriMap.put(accessLog.getUri(), bean.getId());
					}
				}
			}
		}

		for (AccessLog accessLog : list) {
			if (StringUtils.isNotEmpty(accessLog.getUri())) {
				if (uriMap.get(accessLog.getUri()) != null) {
					accessLog.setUriRefId(uriMap.get(accessLog.getUri()));
				} else {
					AccessUri bean = accessUriMapper.getAccessUriByUri(accessLog.getUri());
					if (bean != null) {
						accessLog.setUriRefId(bean.getId());
						uriMap.put(accessLog.getUri(), bean.getId());
					}
				}
			}
		}

		List<AccessLog> logs = new ArrayList<AccessLog>();

		for (AccessLog log : list) {
			logs.add(log);
			if (logs.size() > 0 && logs.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					accessLogMapper.bulkInsertAccessLog_oracle(list);
				} else {
					accessLogMapper.bulkInsertAccessLog(list);
				}
				logs.clear();
			}
		}

		if (logs.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				accessLogMapper.bulkInsertAccessLog_oracle(list);
			} else {
				accessLogMapper.bulkInsertAccessLog(list);
			}
			logs.clear();
		}

	}

	public int count(AccessLogQuery query) {
		return accessLogMapper.getAccessLogCount(query);
	}

	/**
	 * 删除某个日期以前的日志
	 * 
	 * @param dateBefore
	 */
	@Transactional
	public boolean deleteAccessLogs(Date dateBefore) {
		if (dateBefore != null) {
			if ((System.currentTimeMillis() - dateBefore.getTime()) > DateUtils.DAY) {
				accessLogMapper.deleteAccessLogs(dateBefore);
				return true;
			}
		}
		return false;
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			accessLogMapper.deleteAccessLogById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				accessLogMapper.deleteAccessLogById(id);
			}
		}
	}

	public AccessLog getAccessLog(Long id) {
		if (id == null) {
			return null;
		}
		AccessLog accessLog = accessLogMapper.getAccessLogById(id);
		return accessLog;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getAccessLogCountByQueryCriteria(AccessLogQuery query) {
		return accessLogMapper.getAccessLogCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<AccessLog> getAccessLogsByQueryCriteria(int start, int pageSize, AccessLogQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<AccessLog> rows = sqlSessionTemplate.selectList("getAccessLogs", query, rowBounds);
		return rows;
	}

	/**
	 * 根据条件统计某天的访问数量
	 * 
	 * @param query
	 * @return
	 */
	public List<AccessTotal> getAccessTotal(AccessLogQuery query) {
		if (query.getDay() == null) {
			query.setDay(DateUtils.getNowYearMonthDay());
		}
		return accessLogMapper.getAccessTotal(query);
	}

	/**
	 * 按小时统计某天的访问数量
	 * 
	 * @param day
	 * @return
	 */
	public List<AccessTotal> getHourAccessTotal(int day) {
		return accessLogMapper.getHourAccessTotal(day);
	}

	public List<AccessLog> getLatestLogs(String uri, int offset, int limit) {
		AccessUri model = accessUriMapper.getAccessUriByUri(uri);
		if (model != null) {
			RowBounds rowBounds = new RowBounds(offset, limit);
			AccessLogQuery query = new AccessLogQuery();
			query.setUriRefId(model.getId());
			List<AccessLog> rows = sqlSessionTemplate.selectList("getAccessLogs", query, rowBounds);
			return rows;
		}
		return null;
	}

	/**
	 * 按分钟统计某天的访问数量
	 * 
	 * @param day
	 * @return
	 */
	public List<AccessTotal> getMinuteAccessTotal(int day) {
		return accessLogMapper.getMinuteAccessTotal(day);
	}

	public int getTotal(String uri) {
		AccessUri model = accessUriMapper.getAccessUriByUri(uri);
		if (model != null) {
			AccessLogQuery query = new AccessLogQuery();
			query.setUriRefId(model.getId());
			return this.getAccessLogCountByQueryCriteria(query);
		}
		return 0;
	}

	public List<AccessLog> list(AccessLogQuery query) {
		List<AccessLog> list = accessLogMapper.getAccessLogs(query);
		return list;
	}

	@javax.annotation.Resource
	public void setAccessLogMapper(AccessLogMapper accessLogMapper) {
		this.accessLogMapper = accessLogMapper;
	}

	@javax.annotation.Resource
	public void setAccessUriMapper(AccessUriMapper accessUriMapper) {
		this.accessUriMapper = accessUriMapper;
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
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
