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
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.domain.GoodsStock;
import com.glaf.heathcare.mapper.GoodsInStockMapper;
import com.glaf.heathcare.query.GoodsInStockQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsPurchaseService;
import com.glaf.heathcare.service.GoodsStockService;

@Service("com.glaf.heathcare.service.goodsInStockService")
@Transactional(readOnly = true)
public class GoodsInStockServiceImpl implements GoodsInStockService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsInStockMapper goodsInStockMapper;

	protected GoodsPurchaseService goodsPurchaseService;

	protected GoodsStockService goodsStockService;

	public GoodsInStockServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<GoodsInStock> list) {
		Calendar calendar = Calendar.getInstance();
		for (GoodsInStock goodsInStock : list) {
			if (goodsInStock.getId() == 0) {
				goodsInStock.setId(idGenerator.nextId("GOODS_IN_STOCK"));
				goodsInStock.setCreateTime(new Date());

				calendar.setTime(goodsInStock.getInStockTime());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				goodsInStock.setYear(year);
				goodsInStock.setMonth(month);
				goodsInStock.setDay(day);
				goodsInStock.setFullDay(DateUtils.getYearMonthDay(goodsInStock.getInStockTime()));

				if (StringUtils.isNotEmpty(tenantId)) {
					goodsInStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
		}

		int batch_size = 50;
		List<GoodsInStock> rows = new ArrayList<GoodsInStock>(batch_size);

		for (GoodsInStock bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsInStockMapper.bulkInsertGoodsInStock_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					goodsInStockMapper.bulkInsertGoodsInStock(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsInStockMapper.bulkInsertGoodsInStock_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				goodsInStockMapper.bulkInsertGoodsInStock(listModel);
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

		GoodsInStockQuery q = new GoodsInStockQuery();
		q.tenantId(tenantId);
		q.fullDay(fullDay);
		List<GoodsInStock> list2 = this.list(q);
		if (list2 != null && !list2.isEmpty()) {
			for (GoodsInStock p : list2) {
				purchaseMap.remove(p.getGoodsId());
			}
		}

		Collection<GoodsPurchase> purchaseList = purchaseMap.values();
		if (purchaseList != null && !purchaseList.isEmpty()) {
			List<GoodsInStock> insertlist = new ArrayList<GoodsInStock>();
			for (GoodsPurchase purchase : purchaseList) {
				GoodsInStock model = new GoodsInStock();
				model.setTenantId(tenantId);
				model.setGoodsId(purchase.getGoodsId());
				model.setGoodsName(purchase.getGoodsName());
				model.setGoodsNodeId(purchase.getGoodsNodeId());
				model.setQuantity(purchase.getQuantity());
				model.setPrice(purchase.getPrice());
				model.setTotalPrice(purchase.getTotalPrice());
				model.setAddress(purchase.getAddress());
				model.setBatchNo(purchase.getBatchNo());
				model.setContact(purchase.getContact());
				model.setExpiryDate(purchase.getExpiryDate());
				model.setStandard(purchase.getStandard());
				model.setSupplier(purchase.getSupplier());
				model.setUnit(purchase.getUnit());
				model.setInStockTime(purchase.getPurchaseTime());
				model.setFullDay(fullDay);
				model.setYear(purchase.getYear());
				model.setMonth(purchase.getMonth());
				model.setSemester(purchase.getSemester());
				model.setWeek(purchase.getWeek());
				model.setDay(purchase.getDay());
				model.setCreateBy(userId);
				model.setCreateTime(new java.util.Date());
				insertlist.add(model);
			}
			this.bulkInsert(tenantId, insertlist);
		}
	}

	public int count(GoodsInStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsInStockMapper.getGoodsInStockCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			GoodsInStockQuery query = new GoodsInStockQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			goodsInStockMapper.deleteGoodsInStockById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				GoodsInStockQuery query = new GoodsInStockQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				goodsInStockMapper.deleteGoodsInStockById(query);
			}
		}
	}

	public GoodsInStock getGoodsInStock(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		GoodsInStockQuery query = new GoodsInStockQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		GoodsInStock goodsInStock = goodsInStockMapper.getGoodsInStockById(query);
		return goodsInStock;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsInStockCountByQueryCriteria(GoodsInStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsInStockMapper.getGoodsInStockCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsInStock> getGoodsInStocksByQueryCriteria(int start, int pageSize, GoodsInStockQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsInStock> rows = sqlSessionTemplate.selectList("getGoodsInStocks", query, rowBounds);
		return rows;
	}

	public List<GoodsInStock> list(GoodsInStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsInStock> list = goodsInStockMapper.getGoodsInStocks(query);
		return list;
	}

	@Transactional
	public void save(GoodsInStock goodsInStock) {
		if (StringUtils.isNotEmpty(goodsInStock.getTenantId())) {
			goodsInStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsInStock.getTenantId())));
		}
		if (goodsInStock.getId() == 0) {
			goodsInStock.setId(idGenerator.nextId("GOODS_IN_STOCK"));
			goodsInStock.setCreateTime(new Date());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(goodsInStock.getInStockTime());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			goodsInStock.setYear(year);
			goodsInStock.setMonth(month);
			goodsInStock.setDay(day);
			goodsInStock.setFullDay(DateUtils.getYearMonthDay(goodsInStock.getInStockTime()));

			goodsInStockMapper.insertGoodsInStock(goodsInStock);
		} else {
			if (goodsInStock.getBusinessStatus() == 9) {
				GoodsStock goodsStock = new GoodsStock();
				goodsStock.setTenantId(goodsInStock.getTenantId());
				goodsStock.setExpiryDate(goodsInStock.getExpiryDate());
				goodsStock.setGoodsId(goodsInStock.getGoodsId());
				goodsStock.setGoodsName(goodsInStock.getGoodsName());
				goodsStock.setGoodsNodeId(goodsInStock.getGoodsNodeId());
				goodsStock.setQuantity(goodsInStock.getQuantity());
				goodsStock.setUnit(goodsInStock.getUnit());
				goodsStockService.inStock(goodsStock);
			} else {
				if (goodsInStock.getBusinessStatus() == 9) {
					goodsInStock.setAvailable(1);
				}
				goodsInStockMapper.updateGoodsInStock(goodsInStock);
			}
		}

	}

	@Transactional
	public void saveAll(List<GoodsInStock> list) {
		for (GoodsInStock model : list) {
			this.save(model);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsInStockMapper")
	public void setGoodsInStockMapper(GoodsInStockMapper goodsInStockMapper) {
		this.goodsInStockMapper = goodsInStockMapper;
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
	public void updateGoodsInStockStatus(GoodsInStock goodsInStock) {
		if (StringUtils.isNotEmpty(goodsInStock.getTenantId())) {
			goodsInStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsInStock.getTenantId())));
		}
		if (goodsInStock.getBusinessStatus() == 9) {
			goodsInStock.setAvailable(1);
		}
		goodsInStockMapper.updateGoodsInStockStatus(goodsInStock);
		if (goodsInStock.getBusinessStatus() == 9) {
			GoodsStock goodsStock = new GoodsStock();
			goodsStock.setTenantId(goodsInStock.getTenantId());
			goodsStock.setExpiryDate(goodsInStock.getExpiryDate());
			goodsStock.setGoodsId(goodsInStock.getGoodsId());
			goodsStock.setGoodsName(goodsInStock.getGoodsName());
			goodsStock.setGoodsNodeId(goodsInStock.getGoodsNodeId());
			goodsStock.setQuantity(goodsInStock.getQuantity());
			goodsStock.setUnit(goodsInStock.getUnit());
			goodsStockService.inStock(goodsStock);
		}
	}

	@Transactional
	public void updateGoodsInStockStatus(List<GoodsInStock> list) {
		for (GoodsInStock goodsInStock : list) {
			if (StringUtils.isNotEmpty(goodsInStock.getTenantId())) {
				goodsInStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsInStock.getTenantId())));
			}
			if (goodsInStock.getBusinessStatus() == 9) {
				goodsInStock.setAvailable(1);
			}
			goodsInStockMapper.updateGoodsInStockStatus(goodsInStock);
			if (goodsInStock.getBusinessStatus() == 9) {
				GoodsStock goodsStock = new GoodsStock();
				goodsStock.setTenantId(goodsInStock.getTenantId());
				goodsStock.setExpiryDate(goodsInStock.getExpiryDate());
				goodsStock.setGoodsId(goodsInStock.getGoodsId());
				goodsStock.setGoodsName(goodsInStock.getGoodsName());
				goodsStock.setGoodsNodeId(goodsInStock.getGoodsNodeId());
				goodsStock.setQuantity(goodsInStock.getQuantity());
				goodsStock.setUnit(goodsInStock.getUnit());
				goodsStockService.inStock(goodsStock);
			}
		}
	}

}
