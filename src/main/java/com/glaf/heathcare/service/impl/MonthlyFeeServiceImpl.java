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

package com.glaf.heathcare.service.impl;

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

import com.glaf.heathcare.mapper.*;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.MonthlyFeeService;

@Service("com.glaf.heathcare.service.monthlyFeeService")
@Transactional(readOnly = true)
public class MonthlyFeeServiceImpl implements MonthlyFeeService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MonthlyFeeMapper monthlyFeeMapper;

	public MonthlyFeeServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<MonthlyFee> list) {
		for (MonthlyFee monthlyFee : list) {
			if (monthlyFee.getId() == 0) {
				monthlyFee.setId(idGenerator.nextId("HEALTH_MONTHLY_FEE"));
				monthlyFee.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<MonthlyFee> rows = new ArrayList<MonthlyFee>(batch_size);

		for (MonthlyFee bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					monthlyFeeMapper.bulkInsertMonthlyFee_oracle(rows);
				} else {
					monthlyFeeMapper.bulkInsertMonthlyFee(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				monthlyFeeMapper.bulkInsertMonthlyFee_oracle(rows);
			} else {
				monthlyFeeMapper.bulkInsertMonthlyFee(rows);
			}
			rows.clear();
		}
	}

	public int count(MonthlyFeeQuery query) {
		return monthlyFeeMapper.getMonthlyFeeCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			monthlyFeeMapper.deleteMonthlyFeeById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				monthlyFeeMapper.deleteMonthlyFeeById(id);
			}
		}
	}

	public MonthlyFee getMonthlyFee(Long id) {
		if (id == null) {
			return null;
		}
		MonthlyFee monthlyFee = monthlyFeeMapper.getMonthlyFeeById(id);
		return monthlyFee;
	}

	public MonthlyFee getMonthlyFee(String tenantId, int year, int month) {
		MonthlyFeeQuery query = new MonthlyFeeQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);
		List<MonthlyFee> list = monthlyFeeMapper.getMonthlyFees(query);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMonthlyFeeCountByQueryCriteria(MonthlyFeeQuery query) {
		return monthlyFeeMapper.getMonthlyFeeCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MonthlyFee> getMonthlyFeesByQueryCriteria(int start, int pageSize, MonthlyFeeQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MonthlyFee> rows = sqlSessionTemplate.selectList("getMonthlyFees", query, rowBounds);
		return rows;
	}

	public List<MonthlyFee> list(MonthlyFeeQuery query) {
		List<MonthlyFee> list = monthlyFeeMapper.getMonthlyFees(query);
		return list;
	}

	@Transactional
	public void save(MonthlyFee monthlyFee) {
		if (monthlyFee.getId() == 0) {
			monthlyFee.setId(idGenerator.nextId("HEALTH_MONTHLY_FEE"));
			monthlyFee.setCreateTime(new Date());
			monthlyFeeMapper.insertMonthlyFee(monthlyFee);
		} else {
			monthlyFeeMapper.updateMonthlyFee(monthlyFee);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MonthlyFeeMapper")
	public void setMonthlyFeeMapper(MonthlyFeeMapper monthlyFeeMapper) {
		this.monthlyFeeMapper = monthlyFeeMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
