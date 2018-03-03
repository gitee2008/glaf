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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.domain.GoodsPurchasePlan;
import com.glaf.heathcare.mapper.GoodsPurchaseMapper;
import com.glaf.heathcare.query.GoodsPurchasePlanQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.GoodsPurchasePlanService;
import com.glaf.heathcare.service.GoodsPurchaseService;

@Service("com.glaf.heathcare.service.goodsPurchaseService")
@Transactional(readOnly = true)
public class GoodsPurchaseServiceImpl implements GoodsPurchaseService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsPurchaseMapper goodsPurchaseMapper;

	protected GoodsPurchasePlanService goodsPurchasePlanService;

	public GoodsPurchaseServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<GoodsPurchase> list) {
		List<GoodsPurchase> purchaseList = new ArrayList<GoodsPurchase>();
		for (GoodsPurchase goodsPurchase : list) {
			if (goodsPurchase.getId() == 0) {
				goodsPurchase.setId(idGenerator.nextId("GOODS_PURCHASE"));
				goodsPurchase.setCreateTime(new Date());
				if (StringUtils.isNotEmpty(tenantId)) {
					goodsPurchase.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
			purchaseList.add(goodsPurchase);
		}

		int batch_size = 50;
		List<GoodsPurchase> rows = new ArrayList<GoodsPurchase>(batch_size);

		for (GoodsPurchase bean : purchaseList) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsPurchaseMapper.bulkInsertGoodsPurchase_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					goodsPurchaseMapper.bulkInsertGoodsPurchase(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsPurchaseMapper.bulkInsertGoodsPurchase_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				goodsPurchaseMapper.bulkInsertGoodsPurchase(listModel);
			}
			rows.clear();
		}
	}

	/**
	 * 复制某天的采购计划
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	public void copyPurchasePlan(String tenantId, int fullDay, String userId) {
		GoodsPurchasePlanQuery query = new GoodsPurchasePlanQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);
		query.setBusinessStatus(9);// 必须是已经确认了的
		List<GoodsPurchasePlan> plans = goodsPurchasePlanService.list(query);
		Map<Long, GoodsPurchasePlan> planMap = new HashMap<Long, GoodsPurchasePlan>();
		if (plans != null && !plans.isEmpty()) {
			for (GoodsPurchasePlan plan : plans) {
				planMap.put(plan.getGoodsId(), plan);
			}
		}

		GoodsPurchaseQuery q2 = new GoodsPurchaseQuery();
		q2.tenantId(tenantId);
		q2.fullDay(fullDay);
		List<GoodsPurchase> list2 = this.list(q2);
		if (list2 != null && !list2.isEmpty()) {
			for (GoodsPurchase gp : list2) {
				planMap.remove(gp.getGoodsId());
			}
		}

		Collection<GoodsPurchasePlan> planList = planMap.values();
		if (planList != null && !planList.isEmpty()) {
			List<GoodsPurchase> insertlist = new ArrayList<GoodsPurchase>();
			for (GoodsPurchasePlan plan : planList) {
				GoodsPurchase model = new GoodsPurchase();
				model.setTenantId(tenantId);
				model.setGoodsId(plan.getGoodsId());
				model.setGoodsName(plan.getGoodsName());
				model.setGoodsNodeId(plan.getGoodsNodeId());
				model.setQuantity(plan.getQuantity());
				model.setUnit(plan.getUnit());
				model.setPurchaseTime(plan.getPurchaseTime());
				model.setFullDay(fullDay);
				model.setYear(plan.getYear());
				model.setMonth(plan.getMonth());
				model.setSemester(plan.getSemester());
				model.setWeek(plan.getWeek());
				model.setDay(plan.getDay());
				model.setCreateBy(userId);
				model.setCreateTime(new java.util.Date());
				insertlist.add(model);
			}
			this.bulkInsert(tenantId, insertlist);
		}

	}

	public int count(GoodsPurchaseQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsPurchaseMapper.getGoodsPurchaseCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			GoodsPurchaseQuery query = new GoodsPurchaseQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			goodsPurchaseMapper.deleteGoodsPurchaseById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				GoodsPurchaseQuery query = new GoodsPurchaseQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				goodsPurchaseMapper.deleteGoodsPurchaseById(query);
			}
		}
	}

	public GoodsPurchase getGoodsPurchase(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		GoodsPurchase goodsPurchase = goodsPurchaseMapper.getGoodsPurchaseById(query);
		return goodsPurchase;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsPurchaseCountByQueryCriteria(GoodsPurchaseQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsPurchaseMapper.getGoodsPurchaseCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsPurchase> getGoodsPurchasesByQueryCriteria(int start, int pageSize, GoodsPurchaseQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsPurchase> rows = sqlSessionTemplate.selectList("getGoodsPurchases", query, rowBounds);
		return rows;
	}

	public List<GoodsPurchase> list(GoodsPurchaseQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsPurchase> list = goodsPurchaseMapper.getGoodsPurchases(query);
		return list;
	}

	@Transactional
	public void save(GoodsPurchase goodsPurchase) {
		if (StringUtils.isNotEmpty(goodsPurchase.getTenantId())) {
			goodsPurchase.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsPurchase.getTenantId())));
		}
		if (goodsPurchase.getId() == 0) {
			goodsPurchase.setId(idGenerator.nextId("GOODS_PURCHASE"));
			goodsPurchase.setCreateTime(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(goodsPurchase.getPurchaseTime());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			goodsPurchase.setYear(year);
			goodsPurchase.setMonth(month);
			goodsPurchase.setDay(day);
			goodsPurchase.setFullDay(DateUtils.getYearMonthDay(goodsPurchase.getPurchaseTime()));
			goodsPurchaseMapper.insertGoodsPurchase(goodsPurchase);
		} else {
			if (goodsPurchase.getBusinessStatus() != 9) {
				goodsPurchaseMapper.updateGoodsPurchase(goodsPurchase);
			}
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsPurchaseMapper")
	public void setGoodsPurchaseMapper(GoodsPurchaseMapper goodsPurchaseMapper) {
		this.goodsPurchaseMapper = goodsPurchaseMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPurchasePlanService")
	public void setGoodsPurchasePlanService(GoodsPurchasePlanService goodsPurchasePlanService) {
		this.goodsPurchasePlanService = goodsPurchasePlanService;
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
	public void updateGoodsPurchaseStatus(GoodsPurchase model) {
		if (StringUtils.isNotEmpty(model.getTenantId())) {
			model.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(model.getTenantId())));
		}
		goodsPurchaseMapper.updateGoodsPurchaseStatus(model);
	}

	@Transactional
	public void updateGoodsPurchaseStatus(List<GoodsPurchase> list) {
		for (GoodsPurchase model : list) {
			if (StringUtils.isNotEmpty(model.getTenantId())) {
				model.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(model.getTenantId())));
			}
			goodsPurchaseMapper.updateGoodsPurchaseStatus(model);
		}
	}

}
