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
import com.glaf.heathcare.domain.GoodsOutStock;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.domain.GoodsStock;
import com.glaf.heathcare.mapper.GoodsOutStockMapper;
import com.glaf.heathcare.query.GoodsInStockQuery;
import com.glaf.heathcare.query.GoodsOutStockQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.GoodsInStockService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPurchaseService;
import com.glaf.heathcare.service.GoodsStockService;

@Service("com.glaf.heathcare.service.goodsOutStockService")
@Transactional(readOnly = true)
public class GoodsOutStockServiceImpl implements GoodsOutStockService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsOutStockMapper goodsOutStockMapper;

	protected GoodsInStockService goodsInStockService;

	protected GoodsPurchaseService goodsPurchaseService;

	protected GoodsStockService goodsStockService;

	public GoodsOutStockServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<GoodsOutStock> list) {
		Calendar calendar = Calendar.getInstance();
		for (GoodsOutStock goodsOutStock : list) {
			if (goodsOutStock.getId() == 0) {
				goodsOutStock.setId(idGenerator.nextId("GOODS_OUT_STOCK"));
				goodsOutStock.setCreateTime(new Date());
				calendar.setTime(goodsOutStock.getOutStockTime());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				goodsOutStock.setYear(year);
				goodsOutStock.setMonth(month);
				goodsOutStock.setDay(day);
				goodsOutStock.setFullDay(DateUtils.getYearMonthDay(goodsOutStock.getOutStockTime()));
				if (StringUtils.isNotEmpty(tenantId)) {
					goodsOutStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
		}

		int batch_size = 50;
		List<GoodsOutStock> rows = new ArrayList<GoodsOutStock>(batch_size);

		for (GoodsOutStock bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsOutStockMapper.bulkInsertGoodsOutStock_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					goodsOutStockMapper.bulkInsertGoodsOutStock(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsOutStockMapper.bulkInsertGoodsOutStock_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				goodsOutStockMapper.bulkInsertGoodsOutStock(listModel);
			}
			rows.clear();
		}
	}

	/**
	 * 复制某天的入库到出库
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	public void copyInStock(String tenantId, int fullDay, String userId) {
		GoodsInStockQuery query = new GoodsInStockQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);
		query.setBusinessStatus(9);// 必须是已经确认了的
		List<GoodsInStock> inStocks = goodsInStockService.list(query);
		Map<Long, GoodsInStock> purchaseMap = new HashMap<Long, GoodsInStock>();
		if (inStocks != null && !inStocks.isEmpty()) {
			for (GoodsInStock inStock : inStocks) {
				purchaseMap.put(inStock.getGoodsId(), inStock);
			}
		}

		GoodsOutStockQuery q = new GoodsOutStockQuery();
		q.tenantId(tenantId);
		q.fullDay(fullDay);
		List<GoodsOutStock> list2 = this.list(q);
		if (list2 != null && !list2.isEmpty()) {
			for (GoodsOutStock p : list2) {
				purchaseMap.remove(p.getGoodsId());
			}
		}

		Collection<GoodsInStock> inStockList = purchaseMap.values();
		if (inStockList != null && !inStockList.isEmpty()) {
			List<GoodsOutStock> insertlist = new ArrayList<GoodsOutStock>();
			for (GoodsInStock inStock : inStockList) {
				GoodsOutStock model = new GoodsOutStock();
				model.setTenantId(tenantId);
				model.setGoodsId(inStock.getGoodsId());
				model.setGoodsName(inStock.getGoodsName());
				model.setGoodsNodeId(inStock.getGoodsNodeId());
				model.setQuantity(inStock.getQuantity());
				model.setTotalPrice(inStock.getTotalPrice());
				model.setUnit(inStock.getUnit());
				model.setFullDay(fullDay);
				model.setYear(inStock.getYear());
				model.setMonth(inStock.getMonth());
				model.setSemester(inStock.getSemester());
				model.setWeek(inStock.getWeek());
				model.setDay(inStock.getDay());
				model.setOutStockTime(inStock.getInStockTime());
				model.setCreateBy(userId);
				model.setCreateTime(new java.util.Date());
				insertlist.add(model);
			}
			this.bulkInsert(tenantId, insertlist);
		}
	}

	/**
	 * 复制某天的采购到出库
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

		GoodsOutStockQuery q = new GoodsOutStockQuery();
		q.tenantId(tenantId);
		q.fullDay(fullDay);
		List<GoodsOutStock> list2 = this.list(q);
		if (list2 != null && !list2.isEmpty()) {
			for (GoodsOutStock p : list2) {
				purchaseMap.remove(p.getGoodsId());
			}
		}

		Collection<GoodsPurchase> purchaseList = purchaseMap.values();
		if (purchaseList != null && !purchaseList.isEmpty()) {
			List<GoodsOutStock> insertlist = new ArrayList<GoodsOutStock>();
			for (GoodsPurchase purchase : purchaseList) {
				GoodsOutStock model = new GoodsOutStock();
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
				model.setOutStockTime(purchase.getPurchaseTime());
				model.setCreateBy(userId);
				model.setCreateTime(new java.util.Date());
				insertlist.add(model);
			}
			this.bulkInsert(tenantId, insertlist);
		}
	}

	public int count(GoodsOutStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsOutStockMapper.getGoodsOutStockCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			GoodsOutStockQuery query = new GoodsOutStockQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			goodsOutStockMapper.deleteGoodsOutStockById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				GoodsOutStockQuery query = new GoodsOutStockQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				goodsOutStockMapper.deleteGoodsOutStockById(query);
			}
		}
	}

	public GoodsOutStock getGoodsOutStock(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		GoodsOutStockQuery query = new GoodsOutStockQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		GoodsOutStock goodsOutStock = goodsOutStockMapper.getGoodsOutStockById(query);
		return goodsOutStock;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsOutStockCountByQueryCriteria(GoodsOutStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsOutStockMapper.getGoodsOutStockCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsOutStock> getGoodsOutStocksByQueryCriteria(int start, int pageSize, GoodsOutStockQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsOutStock> rows = sqlSessionTemplate.selectList("getGoodsOutStocks", query, rowBounds);
		return rows;
	}

	public List<GoodsOutStock> list(GoodsOutStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsOutStock> list = goodsOutStockMapper.getGoodsOutStocks(query);
		return list;
	}

	@Transactional
	public void save(GoodsOutStock goodsOutStock) {
		if (StringUtils.isNotEmpty(goodsOutStock.getTenantId())) {
			goodsOutStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsOutStock.getTenantId())));
		}
		long goodsId = goodsOutStock.getGoodsId();
		GoodsInStockQuery query1 = new GoodsInStockQuery();
		query1.tenantId(goodsOutStock.getTenantId());
		query1.setGoodsId(goodsId);
		query1.businessStatus(9);// 已经确定入库的物品
		query1.available(1);// 确保库存中存在
		List<GoodsInStock> goods1 = goodsInStockService.list(query1);
		if (goods1 != null && !goods1.isEmpty()) {
			double sumQty = 0.0;
			double totalPrice = 0.0;
			double realQuantity = 0.0;
			for (GoodsInStock m : goods1) {
				sumQty = sumQty + m.getQuantity();
				if (goodsOutStock.getQuantity() <= sumQty) {// 当本次出库数量在累计数据之内
					realQuantity = realQuantity + m.getQuantity();// 计算实际数量
					totalPrice = totalPrice + m.getTotalPrice();// 计算总价
				} else {
					totalPrice = totalPrice + m.getPrice() * (goodsOutStock.getQuantity() - realQuantity);
					break;
				}
			}
			goodsOutStock.setTotalPrice(totalPrice);
		}

		if (goodsOutStock.getId() == 0) {
			goodsOutStock.setId(idGenerator.nextId("GOODS_OUT_STOCK"));
			goodsOutStock.setCreateTime(new Date());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(goodsOutStock.getOutStockTime());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			goodsOutStock.setYear(year);
			goodsOutStock.setMonth(month);
			goodsOutStock.setDay(day);
			goodsOutStock.setFullDay(DateUtils.getYearMonthDay(goodsOutStock.getOutStockTime()));
			goodsOutStockMapper.insertGoodsOutStock(goodsOutStock);
		} else {
			if (goodsOutStock.getBusinessStatus() == 9) {
				GoodsStock goodsStock = new GoodsStock();
				goodsStock.setTenantId(goodsOutStock.getTenantId());
				goodsStock.setGoodsId(goodsOutStock.getGoodsId());
				goodsStock.setGoodsName(goodsOutStock.getGoodsName());
				goodsStock.setGoodsNodeId(goodsOutStock.getGoodsNodeId());
				goodsStock.setQuantity(goodsOutStock.getQuantity());
				goodsStock.setUnit(goodsOutStock.getUnit());
				goodsStock.setLatestOutStockTime(goodsOutStock.getOutStockTime());
				goodsStockService.outStock(goodsStock);
			} else {
				goodsOutStockMapper.updateGoodsOutStock(goodsOutStock);
			}
		}
	}

	@Transactional
	public void saveAll(List<GoodsOutStock> list) {
		for (GoodsOutStock model : list) {
			this.save(model);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsInStockService")
	public void setGoodsInStockService(GoodsInStockService goodsInStockService) {
		this.goodsInStockService = goodsInStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsOutStockMapper")
	public void setGoodsOutStockMapper(GoodsOutStockMapper goodsOutStockMapper) {
		this.goodsOutStockMapper = goodsOutStockMapper;
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
	public void updateGoodsOutStockStatus(GoodsOutStock goodsOutStock) {
		if (StringUtils.isNotEmpty(goodsOutStock.getTenantId())) {
			goodsOutStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsOutStock.getTenantId())));
		}
		goodsOutStockMapper.updateGoodsOutStockStatus(goodsOutStock);
		if (goodsOutStock.getBusinessStatus() == 9) {
			GoodsStock goodsStock = new GoodsStock();
			goodsStock.setTenantId(goodsOutStock.getTenantId());
			goodsStock.setGoodsId(goodsOutStock.getGoodsId());
			goodsStock.setGoodsName(goodsOutStock.getGoodsName());
			goodsStock.setGoodsNodeId(goodsOutStock.getGoodsNodeId());
			goodsStock.setQuantity(goodsOutStock.getQuantity());
			goodsStock.setUnit(goodsOutStock.getUnit());
			goodsStock.setLatestOutStockTime(goodsOutStock.getOutStockTime());
			goodsStockService.outStock(goodsStock);
		}
	}

	@Transactional
	public void updateGoodsOutStockStatus(List<GoodsOutStock> list) {
		for (GoodsOutStock goodsOutStock : list) {
			if (StringUtils.isNotEmpty(goodsOutStock.getTenantId())) {
				goodsOutStock
						.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsOutStock.getTenantId())));
			}
			goodsOutStockMapper.updateGoodsOutStockStatus(goodsOutStock);
			if (goodsOutStock.getBusinessStatus() == 9) {
				GoodsStock goodsStock = new GoodsStock();
				goodsStock.setTenantId(goodsOutStock.getTenantId());
				goodsStock.setGoodsId(goodsOutStock.getGoodsId());
				goodsStock.setGoodsName(goodsOutStock.getGoodsName());
				goodsStock.setGoodsNodeId(goodsOutStock.getGoodsNodeId());
				goodsStock.setQuantity(goodsOutStock.getQuantity());
				goodsStock.setUnit(goodsOutStock.getUnit());
				goodsStock.setLatestOutStockTime(goodsOutStock.getOutStockTime());
				goodsStockService.outStock(goodsStock);
			}
		}
	}

}
