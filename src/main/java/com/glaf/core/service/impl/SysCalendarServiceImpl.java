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

import com.glaf.core.domain.SysCalendar;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.mapper.SysCalendarMapper;
import com.glaf.core.query.SysCalendarQuery;
import com.glaf.core.service.ISysCalendarService;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sysCalendarService")
@Transactional(readOnly = true)
public class SysCalendarServiceImpl implements ISysCalendarService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private SysCalendarMapper sysCalendarMapper;

	public SysCalendarServiceImpl() {

	}

	public int count(SysCalendarQuery query) {
		return sysCalendarMapper.getSysCalendarCount(query);
	}

	public SysCalendar getSysCalendar(Long id) {
		if (id == null) {
			return null;
		}
		return sysCalendarMapper.getSysCalendarById(id);
	}

	public SysCalendar getSysCalendar(String productionLine, int year, int month, int day) {
		SysCalendarQuery query = new SysCalendarQuery();
		query.setYear(year);
		query.setMonth(month);
		query.setDay(day);
		query.setProductionLine(productionLine);
		List<SysCalendar> list = this.list(query);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public int getSysCalendarCountByQueryCriteria(SysCalendarQuery query) {
		return sysCalendarMapper.getSysCalendarCount(query);
	}

	public List<SysCalendar> getSysCalendarsByQueryCriteria(int start, int pageSize, SysCalendarQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getSysCalendars", query, rowBounds);
	}

	public List<SysCalendar> list(SysCalendarQuery query) {
		return sysCalendarMapper.getSysCalendars(query);
	}

	@Transactional
	public void save(SysCalendar sysCalendar) {
		if (sysCalendar.getId() == null) {
			sysCalendar.setId(idGenerator.nextId("SYS_CALENDAR"));
			sysCalendarMapper.insertSysCalendar(sysCalendar);
		} else {
			sysCalendarMapper.updateSysCalendar(sysCalendar);
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
	public void setSysCalendarMapper(SysCalendarMapper sysCalendarMapper) {
		this.sysCalendarMapper = sysCalendarMapper;
	}

}
