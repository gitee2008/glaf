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
import java.util.Calendar;
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
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.domain.GoodsPurchasePlan;
import com.glaf.heathcare.mapper.GoodsPurchasePlanMapper;
import com.glaf.heathcare.query.GoodsPurchasePlanQuery;
import com.glaf.heathcare.service.GoodsPurchasePlanService;

@Service("com.glaf.heathcare.service.goodsPurchasePlanService")
@Transactional(readOnly = true)
public class GoodsPurchasePlanServiceImpl implements GoodsPurchasePlanService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsPurchasePlanMapper goodsPurchasePlanMapper;

	public GoodsPurchasePlanServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<GoodsPurchasePlan> list) {
		List<GoodsPurchasePlan> purchaseList = new ArrayList<GoodsPurchasePlan>();
		for (GoodsPurchasePlan goodsPurchasePlan : list) {
			if (goodsPurchasePlan.getId() == 0) {
				goodsPurchasePlan.setId(idGenerator.nextId("GOODS_PURCHASE_PLAN"));
				goodsPurchasePlan.setCreateTime(new Date());
				if (StringUtils.isNotEmpty(tenantId)) {
					goodsPurchasePlan.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
			purchaseList.add(goodsPurchasePlan);
		}

		int batch_size = 50;
		List<GoodsPurchasePlan> rows = new ArrayList<GoodsPurchasePlan>(batch_size);

		for (GoodsPurchasePlan bean : purchaseList) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsPurchasePlanMapper.bulkInsertGoodsPurchasePlan_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					goodsPurchasePlanMapper.bulkInsertGoodsPurchasePlan(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsPurchasePlanMapper.bulkInsertGoodsPurchasePlan_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				goodsPurchasePlanMapper.bulkInsertGoodsPurchasePlan(listModel);
			}
			rows.clear();
		}
	}

	public int count(GoodsPurchasePlanQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsPurchasePlanMapper.getGoodsPurchasePlanCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			GoodsPurchasePlanQuery query = new GoodsPurchasePlanQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			goodsPurchasePlanMapper.deleteGoodsPurchasePlanById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				GoodsPurchasePlanQuery query = new GoodsPurchasePlanQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				goodsPurchasePlanMapper.deleteGoodsPurchasePlanById(query);
			}
		}
	}

	public GoodsPurchasePlan getGoodsPurchasePlan(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		GoodsPurchasePlanQuery query = new GoodsPurchasePlanQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		GoodsPurchasePlan goodsPurchasePlan = goodsPurchasePlanMapper.getGoodsPurchasePlanById(query);
		return goodsPurchasePlan;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsPurchasePlanCountByQueryCriteria(GoodsPurchasePlanQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsPurchasePlanMapper.getGoodsPurchasePlanCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsPurchasePlan> getGoodsPurchasePlansByQueryCriteria(int start, int pageSize, GoodsPurchasePlanQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsPurchasePlan> rows = sqlSessionTemplate.selectList("getGoodsPurchasePlans", query, rowBounds);
		return rows;
	}

	public List<GoodsPurchasePlan> list(GoodsPurchasePlanQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsPurchasePlan> list = goodsPurchasePlanMapper.getGoodsPurchasePlans(query);
		return list;
	}

	@Transactional
	public void save(GoodsPurchasePlan goodsPurchasePlan) {
		if (StringUtils.isNotEmpty(goodsPurchasePlan.getTenantId())) {
			goodsPurchasePlan.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsPurchasePlan.getTenantId())));
		}

		if (goodsPurchasePlan.getId() == 0) {
			 
			goodsPurchasePlan.setId(idGenerator.nextId("GOODS_PURCHASE_PLAN"));
			goodsPurchasePlan.setCreateTime(new Date());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(goodsPurchasePlan.getPurchaseTime());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			goodsPurchasePlan.setYear(year);
			goodsPurchasePlan.setMonth(month);
			goodsPurchasePlan.setDay(day);
			goodsPurchasePlan.setFullDay(DateUtils.getYearMonthDay(goodsPurchasePlan.getPurchaseTime()));

			goodsPurchasePlanMapper.insertGoodsPurchasePlan(goodsPurchasePlan);
		} else {
			if (goodsPurchasePlan.getBusinessStatus() != 9) {
				goodsPurchasePlanMapper.updateGoodsPurchasePlan(goodsPurchasePlan);
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsPurchasePlanMapper")
	public void setGoodsPurchasePlanMapper(GoodsPurchasePlanMapper goodsPurchasePlanMapper) {
		this.goodsPurchasePlanMapper = goodsPurchasePlanMapper;
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

	@Transactional
	public void updateGoodsPurchasePlanStatus(GoodsPurchasePlan model) {
		if (StringUtils.isNotEmpty(model.getTenantId())) {
			model.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(model.getTenantId())));
		}
		goodsPurchasePlanMapper.updateGoodsPurchasePlanStatus(model);
	}

	@Transactional
	public void updateGoodsPurchasePlanStatus(List<GoodsPurchasePlan> list) {
		for (GoodsPurchasePlan model : list) {
			if (StringUtils.isNotEmpty(model.getTenantId())) {
				model.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(model.getTenantId())));
			}
			goodsPurchasePlanMapper.updateGoodsPurchasePlanStatus(model);
		}
	}

}
