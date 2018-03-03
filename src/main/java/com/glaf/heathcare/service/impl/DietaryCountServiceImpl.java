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
import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;
import com.glaf.heathcare.domain.DietaryCount;
import com.glaf.heathcare.mapper.DietaryCountMapper;
import com.glaf.heathcare.query.DietaryCountQuery;
import com.glaf.heathcare.service.DietaryCountService;

@Service("com.glaf.heathcare.service.dietaryCountService")
@Transactional(readOnly = true)
public class DietaryCountServiceImpl implements DietaryCountService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DietaryCountMapper dietaryCountMapper;

	protected ITableDataService tableDataService;

	public DietaryCountServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<DietaryCount> list) {
		for (DietaryCount dietaryCount : list) {
			if (dietaryCount.getId() == 0) {
				dietaryCount.setId(idGenerator.nextId("HEALTH_DIETARY_COUNT"));
				if (StringUtils.isNotEmpty(tenantId)) {
					dietaryCount.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
		}

		int batch_size = 50;
		List<DietaryCount> rows = new ArrayList<DietaryCount>(batch_size);

		for (DietaryCount bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					dietaryCountMapper.bulkInsertDietaryCount_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					dietaryCountMapper.bulkInsertDietaryCount(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				dietaryCountMapper.bulkInsertDietaryCount_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				dietaryCountMapper.bulkInsertDietaryCount(listModel);
			}
			rows.clear();
		}
	}

	public int count(DietaryCountQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return dietaryCountMapper.getDietaryCountCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			DietaryCountQuery query = new DietaryCountQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			dietaryCountMapper.deleteDietaryCountById(query);
		}
	}

	public DietaryCount getDietaryCount(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		DietaryCountQuery query = new DietaryCountQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		DietaryCount dietaryCount = dietaryCountMapper.getDietaryCountById(query);
		return dietaryCount;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDietaryCountCountByQueryCriteria(DietaryCountQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return dietaryCountMapper.getDietaryCountCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DietaryCount> getDietaryCountsByQueryCriteria(int start, int pageSize, DietaryCountQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<DietaryCount> rows = sqlSessionTemplate.selectList("getDietaryCounts", query, rowBounds);
		return rows;
	}

	public List<DietaryCount> list(DietaryCountQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<DietaryCount> list = dietaryCountMapper.getDietaryCounts(query);
		return list;
	}

	@Transactional
	public void save(DietaryCount dietaryCount) {
		if (dietaryCount.getId() == 0) {
			dietaryCount.setId(idGenerator.nextId("HEALTH_DIETARY_COUNT"));
			dietaryCount.setCreateTime(new Date());
			if (StringUtils.isNotEmpty(dietaryCount.getTenantId())) {
				dietaryCount.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(dietaryCount.getTenantId())));
			}
			dietaryCountMapper.insertDietaryCount(dietaryCount);
		}
	}

	@Transactional
	public void saveAll(String tenantId, int year, int semester, int week, String type, List<DietaryCount> list) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_DIETARY_COUNT" + IdentityFactory.getTenantHash(tenantId));
		table.addIntegerColumn("YEAR_", year);
		table.addIntegerColumn("SEMESTER_", semester);
		table.addIntegerColumn("WEEK_", week);
		table.addStringColumn("TYPE_", type);
		table.addStringColumn("TENANTID_", tenantId);
		tableDataService.deleteTableData(table);

		this.bulkInsert(tenantId, list);
	}

	@Transactional
	public void saveDay(String tenantId, int year, int semester, int week, int fullDay, String type,
			List<DietaryCount> list) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_DIETARY_COUNT" + IdentityFactory.getTenantHash(tenantId));
		table.addIntegerColumn("YEAR_", year);
		table.addIntegerColumn("SEMESTER_", semester);
		table.addIntegerColumn("WEEK_", week);
		table.addIntegerColumn("FULLDAY_", fullDay);
		table.addStringColumn("TYPE_", type);
		table.addStringColumn("TENANTID_", tenantId);
		tableDataService.deleteTableData(table);

		this.bulkInsert(tenantId, list);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.DietaryCountMapper")
	public void setDietaryCountMapper(DietaryCountMapper dietaryCountMapper) {
		this.dietaryCountMapper = dietaryCountMapper;
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

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

}
