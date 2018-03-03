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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.ListModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.MealFeeCount;
import com.glaf.heathcare.mapper.MealFeeCountMapper;
import com.glaf.heathcare.query.MealFeeCountQuery;
import com.glaf.heathcare.service.MealFeeCountService;

@Service("com.glaf.heathcare.service.mealFeeCountService")
@Transactional(readOnly = true)
public class MealFeeCountServiceImpl implements MealFeeCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MealFeeCountMapper mealFeeCountMapper;

	public MealFeeCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<MealFeeCount> list) {
		for (MealFeeCount mealFeeCount : list) {
			if (mealFeeCount.getId() == 0) {
				mealFeeCount.setId(idGenerator.nextId("HEALTH_MEAL_FEE_COUNT"));
				mealFeeCount.setCreateTime(new Date());
				if (StringUtils.isNotEmpty(tenantId)) {
					mealFeeCount.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
		}

		int batch_size = 100;
		List<MealFeeCount> rows = new ArrayList<MealFeeCount>(batch_size);

		for (MealFeeCount bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					mealFeeCountMapper.bulkInsertMealFeeCount_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					mealFeeCountMapper.bulkInsertMealFeeCount(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				mealFeeCountMapper.bulkInsertMealFeeCount_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				mealFeeCountMapper.bulkInsertMealFeeCount(listModel);
			}
			rows.clear();
		}
	}

	public int count(MealFeeCountQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return mealFeeCountMapper.getMealFeeCountCount(query);
	}

	@Transactional
	public void delete(String tenantId, int year, int month, String type) {
		MealFeeCountQuery query = new MealFeeCountQuery();
		query.tenantId(tenantId);
		query.year(year);
		query.month(month);
		query.type(type);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		mealFeeCountMapper.deleteMealFeeCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			MealFeeCountQuery query = new MealFeeCountQuery();
			query.tenantId(tenantId);
			query.setId(id);
			if (StringUtils.isNotEmpty(query.getTenantId())) {
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
			}
			mealFeeCountMapper.deleteMealFeeCountById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				MealFeeCountQuery query = new MealFeeCountQuery();
				query.tenantId(tenantId);
				query.setId(id);
				if (StringUtils.isNotEmpty(query.getTenantId())) {
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
				}
				mealFeeCountMapper.deleteMealFeeCountById(query);
			}
		}
	}

	public MealFeeCount getMealFeeCount(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		MealFeeCountQuery query = new MealFeeCountQuery();
		query.tenantId(tenantId);
		query.setId(id);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		MealFeeCount mealFeeCount = mealFeeCountMapper.getMealFeeCountById(query);
		return mealFeeCount;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMealFeeCountCountByQueryCriteria(MealFeeCountQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return mealFeeCountMapper.getMealFeeCountCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MealFeeCount> getMealFeeCountsByQueryCriteria(int start, int pageSize, MealFeeCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<MealFeeCount> rows = sqlSessionTemplate.selectList("getMealFeeCounts", query, rowBounds);
		return rows;
	}

	public List<MealFeeCount> list(MealFeeCountQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<MealFeeCount> list = mealFeeCountMapper.getMealFeeCounts(query);
		return list;
	}

	@Transactional
	public void save(MealFeeCount mealFeeCount) {
		if (StringUtils.isNotEmpty(mealFeeCount.getTenantId())) {
			mealFeeCount.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(mealFeeCount.getTenantId())));
		}
		if (mealFeeCount.getId() == 0) {
			mealFeeCount.setId(idGenerator.nextId("HEALTH_MEAL_FEE_COUNT"));
			mealFeeCount.setCreateTime(new Date());
			mealFeeCountMapper.insertMealFeeCount(mealFeeCount);
		} else {
			mealFeeCountMapper.updateMealFeeCount(mealFeeCount);
		}
	}

	@Transactional
	public void saveAll(String tenantId, int year, int month, String type, List<MealFeeCount> list) {
		this.delete(tenantId, year, month, type);
		this.bulkInsert(tenantId, list);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MealFeeCountMapper")
	public void setMealFeeCountMapper(MealFeeCountMapper mealFeeCountMapper) {
		this.mealFeeCountMapper = mealFeeCountMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
