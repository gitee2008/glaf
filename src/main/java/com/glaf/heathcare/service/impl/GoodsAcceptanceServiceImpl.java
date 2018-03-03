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

import com.glaf.heathcare.domain.GoodsAcceptance;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.mapper.GoodsAcceptanceMapper;
import com.glaf.heathcare.query.GoodsAcceptanceQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsPurchaseService;
import com.glaf.heathcare.service.GoodsAcceptanceService;
import com.glaf.heathcare.service.GoodsStockService;

@Service("com.glaf.heathcare.service.goodsAcceptanceService")
@Transactional(readOnly = true)
public class GoodsAcceptanceServiceImpl implements GoodsAcceptanceService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsAcceptanceMapper goodsAcceptanceMapper;

	protected GoodsInStockService goodsInStockService;

	protected GoodsPurchaseService goodsPurchaseService;

	protected GoodsStockService goodsStockService;

	public GoodsAcceptanceServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<GoodsAcceptance> list) {
		Calendar calendar = Calendar.getInstance();
		for (GoodsAcceptance goodsAcceptance : list) {
			if (goodsAcceptance.getId() == 0) {
				goodsAcceptance.setId(idGenerator.nextId("GOODS_ACCEPTANCE"));
				goodsAcceptance.setCreateTime(new Date());
				calendar.setTime(goodsAcceptance.getAcceptanceTime());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				goodsAcceptance.setYear(year);
				goodsAcceptance.setMonth(month);
				goodsAcceptance.setDay(day);
				goodsAcceptance.setFullDay(DateUtils.getYearMonthDay(goodsAcceptance.getAcceptanceTime()));
				if (StringUtils.isNotEmpty(tenantId)) {
					goodsAcceptance.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
		}

		int batch_size = 50;
		List<GoodsAcceptance> rows = new ArrayList<GoodsAcceptance>(batch_size);

		for (GoodsAcceptance bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsAcceptanceMapper.bulkInsertGoodsAcceptance_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					goodsAcceptanceMapper.bulkInsertGoodsAcceptance(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsAcceptanceMapper.bulkInsertGoodsAcceptance_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				goodsAcceptanceMapper.bulkInsertGoodsAcceptance(listModel);
			}
			rows.clear();
		}
	}

	/**
	 * 复制某天的采购到入库
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	public void copyPurchase(String tenantId, int fullDay, String userId) {
		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);
		query.setBusinessStatus(9);// 必须是已经确认了的
		List<GoodsPurchase> purchases = goodsPurchaseService.list(query);
		Map<Long, GoodsPurchase> purchaseMap = new HashMap<Long, GoodsPurchase>();
		if (purchases != null && !purchases.isEmpty()) {
			for (GoodsPurchase purchase : purchases) {
				purchaseMap.put(purchase.getGoodsId(), purchase);
			}
		}

		GoodsAcceptanceQuery q = new GoodsAcceptanceQuery();
		q.tenantId(tenantId);
		q.fullDay(fullDay);
		List<GoodsAcceptance> list2 = this.list(q);
		if (list2 != null && !list2.isEmpty()) {
			for (GoodsAcceptance p : list2) {
				purchaseMap.remove(p.getGoodsId());
			}
		}

		Collection<GoodsPurchase> purchaseList = purchaseMap.values();
		if (purchaseList != null && !purchaseList.isEmpty()) {
			List<GoodsAcceptance> insertlist = new ArrayList<GoodsAcceptance>();
			for (GoodsPurchase purchase : purchaseList) {
				GoodsAcceptance model = new GoodsAcceptance();
				model.setTenantId(tenantId);
				model.setGoodsId(purchase.getGoodsId());
				model.setGoodsName(purchase.getGoodsName());
				model.setGoodsNodeId(purchase.getGoodsNodeId());
				model.setQuantity(purchase.getQuantity());
				model.setTotalPrice(purchase.getTotalPrice());
				model.setUnit(purchase.getUnit());
				model.setFullDay(fullDay);
				model.setYear(purchase.getYear());
				model.setMonth(purchase.getMonth());
				model.setSemester(purchase.getSemester());
				model.setWeek(purchase.getWeek());
				model.setDay(purchase.getDay());
				model.setAcceptanceTime(purchase.getPurchaseTime());
				model.setCreateBy(userId);
				model.setCreateTime(new java.util.Date());
				insertlist.add(model);
			}
			this.bulkInsert(tenantId, insertlist);
		}
	}

	public int count(GoodsAcceptanceQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsAcceptanceMapper.getGoodsAcceptanceCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			GoodsAcceptanceQuery query = new GoodsAcceptanceQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			goodsAcceptanceMapper.deleteGoodsAcceptanceById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				GoodsAcceptanceQuery query = new GoodsAcceptanceQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				goodsAcceptanceMapper.deleteGoodsAcceptanceById(query);
			}
		}
	}

	public GoodsAcceptance getGoodsAcceptance(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		GoodsAcceptanceQuery query = new GoodsAcceptanceQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		GoodsAcceptance goodsAcceptance = goodsAcceptanceMapper.getGoodsAcceptanceById(query);
		return goodsAcceptance;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsAcceptanceCountByQueryCriteria(GoodsAcceptanceQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsAcceptanceMapper.getGoodsAcceptanceCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsAcceptance> getGoodsAcceptancesByQueryCriteria(int start, int pageSize,
			GoodsAcceptanceQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsAcceptance> rows = sqlSessionTemplate.selectList("getGoodsAcceptances", query, rowBounds);
		return rows;
	}

	public List<GoodsAcceptance> list(GoodsAcceptanceQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsAcceptance> list = goodsAcceptanceMapper.getGoodsAcceptances(query);
		return list;
	}

	@Transactional
	public void save(GoodsAcceptance goodsAcceptance) {
		if (StringUtils.isNotEmpty(goodsAcceptance.getTenantId())) {
			goodsAcceptance
					.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsAcceptance.getTenantId())));
		}
		if (goodsAcceptance.getId() == 0) {
			goodsAcceptance.setId(idGenerator.nextId("GOODS_ACCEPTANCE"));
			goodsAcceptance.setCreateTime(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(goodsAcceptance.getAcceptanceTime());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			goodsAcceptance.setYear(year);
			goodsAcceptance.setMonth(month);
			goodsAcceptance.setDay(day);
			goodsAcceptance.setFullDay(DateUtils.getYearMonthDay(goodsAcceptance.getAcceptanceTime()));
			goodsAcceptanceMapper.insertGoodsAcceptance(goodsAcceptance);
		} else {
			goodsAcceptanceMapper.updateGoodsAcceptance(goodsAcceptance);
		}
	}

	@Transactional
	public void saveAll(List<GoodsAcceptance> list) {
		for (GoodsAcceptance model : list) {
			this.save(model);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsAcceptanceMapper")
	public void setGoodsAcceptanceMapper(GoodsAcceptanceMapper goodsAcceptanceMapper) {
		this.goodsAcceptanceMapper = goodsAcceptanceMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsInStockService")
	public void setGoodsInStockService(GoodsInStockService goodsInStockService) {
		this.goodsInStockService = goodsInStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPurchaseService")
	public void setGoodsPurchaseService(GoodsPurchaseService goodsPurchaseService) {
		this.goodsPurchaseService = goodsPurchaseService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsStockService")
	public void setGoodsStockService(GoodsStockService goodsStockService) {
		this.goodsStockService = goodsStockService;
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
	public void updateGoodsAcceptanceStatus(GoodsAcceptance goodsAcceptance) {
		if (StringUtils.isNotEmpty(goodsAcceptance.getTenantId())) {
			goodsAcceptance
					.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsAcceptance.getTenantId())));
		}
		goodsAcceptanceMapper.updateGoodsAcceptanceStatus(goodsAcceptance);
	}

	@Transactional
	public void updateGoodsAcceptanceStatus(List<GoodsAcceptance> list) {
		for (GoodsAcceptance goodsAcceptance : list) {
			if (StringUtils.isNotEmpty(goodsAcceptance.getTenantId())) {
				goodsAcceptance
						.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsAcceptance.getTenantId())));
			}
			goodsAcceptanceMapper.updateGoodsAcceptanceStatus(goodsAcceptance);
		}
	}

}
