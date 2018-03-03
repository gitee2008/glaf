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

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.heathcare.domain.GoodsInStock;
import com.glaf.heathcare.domain.GoodsStock;
import com.glaf.heathcare.mapper.GoodsInStockMapper;
import com.glaf.heathcare.mapper.GoodsStockMapper;
import com.glaf.heathcare.query.GoodsStockQuery;
import com.glaf.heathcare.service.GoodsStockService;

@Service("com.glaf.heathcare.service.goodsStockService")
@Transactional(readOnly = true)
public class GoodsStockServiceImpl implements GoodsStockService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsInStockMapper goodsInStockMapper;

	protected GoodsStockMapper goodsStockMapper;

	protected ITableDataService tableDataService;

	public GoodsStockServiceImpl() {

	}

	public int count(GoodsStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsStockMapper.getGoodsStockCount(query);
	}

	public GoodsStock getGoodsStockById(String tenantId, String id) {
		if (tenantId == null || id == null) {
			return null;
		}
		GoodsStockQuery query = new GoodsStockQuery();
		query.tenantId(tenantId);
		query.setId(id);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		GoodsStock goodsStock = goodsStockMapper.getGoodsStockById(query);
		return goodsStock;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsStockCountByQueryCriteria(GoodsStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return goodsStockMapper.getGoodsStockCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsStock> getGoodsStocksByQueryCriteria(int start, int pageSize, GoodsStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GoodsStock> rows = sqlSessionTemplate.selectList("getGoodsStocks", query, rowBounds);
		return rows;
	}

	public List<GoodsStock> getGoodsStocksByTenantId(String tenantId) {
		GoodsStockQuery query = new GoodsStockQuery();
		query.tenantId(tenantId);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsStock> list = goodsStockMapper.getGoodsStocks(query);
		return list;
	}

	@Transactional
	public void inStock(GoodsStock goodsStock) {
		if (StringUtils.isEmpty(goodsStock.getId())) {
			goodsStock.setId(goodsStock.getTenantId() + "_" + goodsStock.getGoodsId());
		}
		if (StringUtils.isNotEmpty(goodsStock.getTenantId())) {
			goodsStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsStock.getTenantId())));
		}
		GoodsStock model = this.getGoodsStockById(goodsStock.getTenantId(), goodsStock.getId());
		if (model == null) {
			goodsStockMapper.insertGoodsStock(goodsStock);
		} else {
			if (goodsStock.getExpiryDate() != null) {
				if (model.getExpiryDate() != null) {
					if (goodsStock.getExpiryDate().getTime() < model.getExpiryDate().getTime()) {
						model.setExpiryDate(goodsStock.getExpiryDate());
					}
				} else {
					model.setExpiryDate(goodsStock.getExpiryDate());
				}
			}
			if (goodsStock.getQuantity() > 0) {
				model.setQuantity(goodsStock.getQuantity() + model.getQuantity());// 加库存
			}
			if (StringUtils.isNotEmpty(model.getTenantId())) {
				model.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(model.getTenantId())));
			}
			goodsStockMapper.updateGoodsStock(model);
		}
	}

	public List<GoodsStock> list(GoodsStockQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<GoodsStock> list = goodsStockMapper.getGoodsStocks(query);
		return list;
	}

	/**
	 * 保存出库记录
	 * 
	 * @return
	 */
	@Transactional
	public void outStock(GoodsStock goodsStock) {
		if (StringUtils.isEmpty(goodsStock.getId())) {
			goodsStock.setId(goodsStock.getTenantId() + "_" + goodsStock.getGoodsId());
		}
		if (StringUtils.isNotEmpty(goodsStock.getTenantId())) {
			goodsStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(goodsStock.getTenantId())));
		}
		GoodsStock model = this.getGoodsStockById(goodsStock.getTenantId(), goodsStock.getId());
		if (model != null && goodsStock.getQuantity() > 0 && model.getQuantity() >= goodsStock.getQuantity()) {
			model.setQuantity(model.getQuantity() - goodsStock.getQuantity());// 减库存
			model.setLatestOutStockTime(goodsStock.getLatestOutStockTime());
			if (StringUtils.isNotEmpty(model.getTenantId())) {
				model.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(model.getTenantId())));
			}
			goodsStockMapper.updateGoodsStock(model);
		}
		if (model != null && model.getQuantity() == 0) {
			GoodsInStock inStock = new GoodsInStock();
			inStock.setAvailable(-1);// 设置入库数据已经用完
			inStock.setBusinessStatus(9);
			inStock.setGoodsId(model.getGoodsId());
			inStock.setTenantId(model.getTenantId());
			inStock.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(model.getTenantId())));
			goodsInStockMapper.updateGoodsInStockAvailable(inStock);// 修改入库数据状态为已经用完
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsStockMapper")
	public void setGoodsStockMapper(GoodsStockMapper goodsStockMapper) {
		this.goodsStockMapper = goodsStockMapper;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

}
