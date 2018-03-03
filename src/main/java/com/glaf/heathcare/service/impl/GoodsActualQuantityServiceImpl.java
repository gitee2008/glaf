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
import com.glaf.heathcare.domain.GoodsActualQuantity;
import com.glaf.heathcare.domain.GoodsOutStock;
import com.glaf.heathcare.domain.GoodsPlanQuantity;
import com.glaf.heathcare.domain.GoodsPurchase;
import com.glaf.heathcare.mapper.GoodsActualQuantityMapper;
import com.glaf.heathcare.query.GoodsActualQuantityQuery;
import com.glaf.heathcare.query.GoodsOutStockQuery;
import com.glaf.heathcare.query.GoodsPlanQuantityQuery;
import com.glaf.heathcare.query.GoodsPurchaseQuery;
import com.glaf.heathcare.service.GoodsActualQuantityService;
import com.glaf.heathcare.service.GoodsOutStockService;
import com.glaf.heathcare.service.GoodsPlanQuantityService;
import com.glaf.heathcare.service.GoodsPurchaseService;

@Service("com.glaf.heathcare.service.goodsActualQuantityService")
@Transactional(readOnly = true)
public class GoodsActualQuantityServiceImpl implements GoodsActualQuantityService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsActualQuantityMapper goodsActualQuantityMapper;

	protected GoodsOutStockService goodsOutStockService;

	protected GoodsPlanQuantityService goodsPlanQuantityService;

	protected GoodsPurchaseService goodsPurchaseService;

	public GoodsActualQuantityServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<GoodsActualQuantity> list) {
		for (GoodsActualQuantity goodsActualQuantity : list) {
			if (goodsActualQuantity.getId() == 0) {
				goodsActualQuantity.setId(idGenerator.nextId("GOODS_ACTUAL_QUANTITY"));
				goodsActualQuantity.setCreateTime(new Date());
				if (StringUtils.isNotEmpty(tenantId)) {
					goodsActualQuantity.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
			}
		}

		int batch_size = 50;
		List<GoodsActualQuantity> rows = new ArrayList<GoodsActualQuantity>(batch_size);

		for (GoodsActualQuantity bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsActualQuantityMapper.bulkInsertGoodsActualQuantity_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					goodsActualQuantityMapper.bulkInsertGoodsActualQuantity(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsActualQuantityMapper.bulkInsertGoodsActualQuantity_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				goodsActualQuantityMapper.bulkInsertGoodsActualQuantity(listModel);
			}
			rows.clear();
		}
	}

	/**
	 * 复制某天的出库到用量表
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	public void copyOutStock(String tenantId, int fullDay, String userId, boolean includePlan) {
		GoodsOutStockQuery query = new GoodsOutStockQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);
		query.businessStatus(9);

		List<GoodsOutStock> outStocks = goodsOutStockService.list(query);
		Map<Long, GoodsOutStock> outStockMap = new HashMap<Long, GoodsOutStock>();
		if (outStocks != null && !outStocks.isEmpty()) {
			for (GoodsOutStock outStock : outStocks) {
				outStockMap.put(outStock.getGoodsId(), outStock);
			}
		}

		GoodsActualQuantityQuery q = new GoodsActualQuantityQuery();
		q.tenantId(tenantId);
		q.fullDay(fullDay);
		List<GoodsActualQuantity> list2 = this.list(q);
		if (list2 != null && !list2.isEmpty()) {
			for (GoodsActualQuantity p : list2) {
				outStockMap.remove(p.getGoodsId());
			}
		}

		GoodsPlanQuantityQuery q2 = new GoodsPlanQuantityQuery();
		q2.tenantId(tenantId);
		q2.fullDay(fullDay);

		Collection<GoodsOutStock> outStockList = new ArrayList<GoodsOutStock>();
		if (includePlan) {
			List<GoodsPlanQuantity> list3 = goodsPlanQuantityService.list(q2);
			if (list3 != null && !list3.isEmpty()) {
				for (GoodsPlanQuantity m : list3) {
					if (outStockMap.get(m.getGoodsId()) != null) {
						outStockList.add(outStockMap.get(m.getGoodsId()));
					}
				}
			}
		} else {
			outStockList.addAll(outStockMap.values());
		}

		if (outStockList != null && !outStockList.isEmpty()) {
			List<GoodsActualQuantity> insertlist = new ArrayList<GoodsActualQuantity>();
			for (GoodsOutStock outStock : outStockList) {
				GoodsActualQuantity model = new GoodsActualQuantity();
				model.setTenantId(tenantId);
				model.setGoodsId(outStock.getGoodsId());
				model.setGoodsName(outStock.getGoodsName());
				model.setGoodsNodeId(outStock.getGoodsNodeId());
				model.setQuantity(outStock.getQuantity());
				model.setTotalPrice(outStock.getTotalPrice());
				model.setUnit(outStock.getUnit());
				model.setUsageTime(outStock.getOutStockTime());
				model.setFullDay(fullDay);
				model.setYear(outStock.getYear());
				model.setMonth(outStock.getMonth());
				model.setSemester(outStock.getSemester());
				model.setWeek(outStock.getWeek());
				model.setDay(outStock.getDay());
				model.setCreateBy(userId);
				model.setCreateTime(new java.util.Date());
				insertlist.add(model);
			}
			this.bulkInsert(tenantId, insertlist);
		}
	}

	/**
	 * 复制某天的采购到用量表
	 * 
	 * @param tenantId
	 * @param fullDay
	 */
	@Transactional
	public void copyPurchase(String tenantId, int fullDay, String userId, boolean includePlan) {
		GoodsPurchaseQuery query = new GoodsPurchaseQuery();
		query.tenantId(tenantId);
		query.fullDay(fullDay);
		query.businessStatus(9);

		List<GoodsPurchase> purchases = goodsPurchaseService.list(query);
		Map<Long, GoodsPurchase> purchaseMap = new HashMap<Long, GoodsPurchase>();
		if (purchases != null && !purchases.isEmpty()) {
			for (GoodsPurchase purchase : purchases) {
				purchaseMap.put(purchase.getGoodsId(), purchase);
			}
		}

		GoodsActualQuantityQuery q = new GoodsActualQuantityQuery();
		q.tenantId(tenantId);
		q.fullDay(fullDay);
		List<GoodsActualQuantity> list2 = this.list(q);
		if (list2 != null && !list2.isEmpty()) {
			for (GoodsActualQuantity p : list2) {
				purchaseMap.remove(p.getGoodsId());
			}
		}

		GoodsPlanQuantityQuery q2 = new GoodsPlanQuantityQuery();
		q2.tenantId(tenantId);
		q2.fullDay(fullDay);

		Collection<GoodsPurchase> purchaseList = new ArrayList<GoodsPurchase>();
		if (includePlan) {
			List<GoodsPlanQuantity> list3 = goodsPlanQuantityService.list(q2);
			if (list3 != null && !list3.isEmpty()) {
				for (GoodsPlanQuantity m : list3) {
					if (purchaseMap.get(m.getGoodsId()) != null) {
						purchaseList.add(purchaseMap.get(m.getGoodsId()));
					}
				}
			}
		} else {
			purchaseList.addAll(purchaseMap.values());
		}

		if (purchaseList != null && !purchaseList.isEmpty()) {
			List<GoodsActualQuantity> insertlist = new ArrayList<GoodsActualQuantity>();
			for (GoodsPurchase purchase : purchaseList) {
				GoodsActualQuantity model = new GoodsActualQuantity();
				model.setTenantId(tenantId);
				model.setGoodsId(purchase.getGoodsId());
				model.setGoodsName(purchase.getGoodsName());
				model.setGoodsNodeId(purchase.getGoodsNodeId());
				model.setQuantity(purchase.getQuantity());
				model.setPrice(purchase.getPrice());
				model.setTotalPrice(purchase.getTotalPrice());
				model.setUnit(purchase.getUnit());
				model.setUsageTime(purchase.getPurchaseTime());
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

	public int count(GoodsActualQuantityQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsActualQuantityMapper.getGoodsActualQuantityCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, long id) {
		if (id != 0) {
			GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
			query.setId(id);
			if (StringUtils.isNotEmpty(tenantId)) {
				query.tenantId(tenantId);
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
			}
			goodsActualQuantityMapper.deleteGoodsActualQuantityById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
				query.setId(id);
				if (StringUtils.isNotEmpty(tenantId)) {
					query.tenantId(tenantId);
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				goodsActualQuantityMapper.deleteGoodsActualQuantityById(query);
			}
		}
	}

	public GoodsActualQuantity getGoodsActualQuantity(String tenantId, long id) {
		if (id == 0) {
			return null;
		}
		GoodsActualQuantityQuery query = new GoodsActualQuantityQuery();
		query.setId(id);
		if (StringUtils.isNotEmpty(tenantId)) {
			query.tenantId(tenantId);
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
		}
		GoodsActualQuantity goodsActualQuantity = goodsActualQuantityMapper.getGoodsActualQuantityById(query);
		return goodsActualQuantity;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsActualQuantityCountByQueryCriteria(GoodsActualQuantityQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsActualQuantityMapper.getGoodsActualQuantityCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsActualQuantity> getGoodsActualQuantitysByQueryCriteria(int start, int pageSize,
			GoodsActualQuantityQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsActualQuantity> rows = sqlSessionTemplate.selectList("getGoodsActualQuantitys", query, rowBounds);
		return rows;
	}

	public List<GoodsActualQuantity> list(GoodsActualQuantityQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsActualQuantity> list = goodsActualQuantityMapper.getGoodsActualQuantitys(query);
		return list;
	}

	@Transactional
	public void save(GoodsActualQuantity goodsActualQuantity) {
		if (StringUtils.isNotEmpty(goodsActualQuantity.getTenantId())) {
			goodsActualQuantity
					.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsActualQuantity.getTenantId())));
		}
		if (goodsActualQuantity.getId() == 0) {
			goodsActualQuantity.setId(idGenerator.nextId("GOODS_ACTUAL_QUANTITY"));
			goodsActualQuantity.setCreateTime(new Date());
			goodsActualQuantityMapper.insertGoodsActualQuantity(goodsActualQuantity);
		} else {
			goodsActualQuantityMapper.updateGoodsActualQuantity(goodsActualQuantity);
		}
	}

	@Transactional
	public void saveAll(List<GoodsActualQuantity> list) {
		for (GoodsActualQuantity goodsActualQuantity : list) {
			this.save(goodsActualQuantity);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsActualQuantityMapper")
	public void setGoodsActualQuantityMapper(GoodsActualQuantityMapper goodsActualQuantityMapper) {
		this.goodsActualQuantityMapper = goodsActualQuantityMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsOutStockService")
	public void setGoodsOutStockService(GoodsOutStockService goodsOutStockService) {
		this.goodsOutStockService = goodsOutStockService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPlanQuantityService")
	public void setGoodsPlanQuantityService(GoodsPlanQuantityService goodsPlanQuantityService) {
		this.goodsPlanQuantityService = goodsPlanQuantityService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.goodsPurchaseService")
	public void setGoodsPurchaseService(GoodsPurchaseService goodsPurchaseService) {
		this.goodsPurchaseService = goodsPurchaseService;
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
	public void updateGoodsActualQuantityStatus(GoodsActualQuantity goodsActualQuantity) {
		goodsActualQuantity
				.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsActualQuantity.getTenantId())));
		goodsActualQuantityMapper.updateGoodsActualQuantity(goodsActualQuantity);
	}

	@Transactional
	public void updateGoodsActualQuantityStatus(List<GoodsActualQuantity> list) {
		for (GoodsActualQuantity goodsActualQuantity : list) {
			if (StringUtils.isNotEmpty(goodsActualQuantity.getTenantId())) {
				goodsActualQuantity.setTableSuffix(
						String.valueOf(IdentityFactory.getTenantHash(goodsActualQuantity.getTenantId())));
			}
			goodsActualQuantityMapper.updateGoodsActualQuantity(goodsActualQuantity);
		}
	}

}
