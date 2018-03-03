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

package com.glaf.heathcare.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class GoodsStockQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String id;
	protected List<String> tenantIds;
	protected Long goodsId;
	protected List<Long> goodsIds;
	protected String goodsName;
	protected String goodsNameLike;
	protected Long goodsNodeId;
	protected List<Long> goodsNodeIds;
	protected Double quantityGreaterThan;
	protected Double quantityGreaterThanOrEqual;
	protected Double quantityLessThanOrEqual;
	protected String unit;
	protected Date expiryDateGreaterThanOrEqual;
	protected Date expiryDateLessThanOrEqual;
	protected Date latestOutStockTimeGreaterThanOrEqual;
	protected Date latestOutStockTimeLessThanOrEqual;
	protected String tableSuffix;

	public GoodsStockQuery() {

	}

	public GoodsStockQuery expiryDateGreaterThanOrEqual(Date expiryDateGreaterThanOrEqual) {
		if (expiryDateGreaterThanOrEqual == null) {
			throw new RuntimeException("expiryDate is null");
		}
		this.expiryDateGreaterThanOrEqual = expiryDateGreaterThanOrEqual;
		return this;
	}

	public GoodsStockQuery expiryDateLessThanOrEqual(Date expiryDateLessThanOrEqual) {
		if (expiryDateLessThanOrEqual == null) {
			throw new RuntimeException("expiryDate is null");
		}
		this.expiryDateLessThanOrEqual = expiryDateLessThanOrEqual;
		return this;
	}

	public Date getExpiryDateGreaterThanOrEqual() {
		return expiryDateGreaterThanOrEqual;
	}

	public Date getExpiryDateLessThanOrEqual() {
		return expiryDateLessThanOrEqual;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public List<Long> getGoodsIds() {
		return goodsIds;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getGoodsNameLike() {
		if (goodsNameLike != null && goodsNameLike.trim().length() > 0) {
			if (!goodsNameLike.startsWith("%")) {
				goodsNameLike = "%" + goodsNameLike;
			}
			if (!goodsNameLike.endsWith("%")) {
				goodsNameLike = goodsNameLike + "%";
			}
		}
		return goodsNameLike;
	}

	public Long getGoodsNodeId() {
		return goodsNodeId;
	}

	public List<Long> getGoodsNodeIds() {
		return goodsNodeIds;
	}

	public String getId() {
		return id;
	}

	public Date getLatestOutStockTimeGreaterThanOrEqual() {
		return latestOutStockTimeGreaterThanOrEqual;
	}

	public Date getLatestOutStockTimeLessThanOrEqual() {
		return latestOutStockTimeLessThanOrEqual;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("goodsId".equals(sortColumn)) {
				orderBy = "E.GOODSID_" + a_x;
			}

			if ("goodsName".equals(sortColumn)) {
				orderBy = "E.GOODSNAME_" + a_x;
			}

			if ("goodsNodeId".equals(sortColumn)) {
				orderBy = "E.GOODSNODEID_" + a_x;
			}

			if ("quantity".equals(sortColumn)) {
				orderBy = "E.QUANTITY_" + a_x;
			}

			if ("unit".equals(sortColumn)) {
				orderBy = "E.UNIT_" + a_x;
			}

			if ("expiryDate".equals(sortColumn)) {
				orderBy = "E.EXPIRYDATE_" + a_x;
			}

			if ("latestOutStockTime".equals(sortColumn)) {
				orderBy = "E.LATESTOUTSTOCKTIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Double getQuantityGreaterThan() {
		return quantityGreaterThan;
	}

	public Double getQuantityGreaterThanOrEqual() {
		return quantityGreaterThanOrEqual;
	}

	public Double getQuantityLessThanOrEqual() {
		return quantityLessThanOrEqual;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getUnit() {
		return unit;
	}

	public GoodsStockQuery goodsId(Long goodsId) {
		if (goodsId == null) {
			throw new RuntimeException("goodsId is null");
		}
		this.goodsId = goodsId;
		return this;
	}

	public GoodsStockQuery goodsIds(List<Long> goodsIds) {
		if (goodsIds == null) {
			throw new RuntimeException("goodsIds is empty ");
		}
		this.goodsIds = goodsIds;
		return this;
	}

	public GoodsStockQuery goodsName(String goodsName) {
		if (goodsName == null) {
			throw new RuntimeException("goodsName is null");
		}
		this.goodsName = goodsName;
		return this;
	}

	public GoodsStockQuery goodsNameLike(String goodsNameLike) {
		if (goodsNameLike == null) {
			throw new RuntimeException("goodsName is null");
		}
		this.goodsNameLike = goodsNameLike;
		return this;
	}

	public GoodsStockQuery goodsNodeId(Long goodsNodeId) {
		if (goodsNodeId == null) {
			throw new RuntimeException("goodsNodeId is null");
		}
		this.goodsNodeId = goodsNodeId;
		return this;
	}

	public GoodsStockQuery goodsNodeIds(List<Long> goodsNodeIds) {
		if (goodsNodeIds == null) {
			throw new RuntimeException("goodsNodeIds is empty ");
		}
		this.goodsNodeIds = goodsNodeIds;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("goodsId", "GOODSID_");
		addColumn("goodsName", "GOODSNAME_");
		addColumn("goodsNodeId", "GOODSNODEID_");
		addColumn("quantity", "QUANTITY_");
		addColumn("unit", "UNIT_");
		addColumn("expiryDate", "EXPIRYDATE_");
		addColumn("latestOutStockTime", "LATESTOUTSTOCKTIME_");
	}

	public GoodsStockQuery latestOutStockTimeGreaterThanOrEqual(Date latestOutStockTimeGreaterThanOrEqual) {
		if (latestOutStockTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("latestOutStockTime is null");
		}
		this.latestOutStockTimeGreaterThanOrEqual = latestOutStockTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsStockQuery latestOutStockTimeLessThanOrEqual(Date latestOutStockTimeLessThanOrEqual) {
		if (latestOutStockTimeLessThanOrEqual == null) {
			throw new RuntimeException("latestOutStockTime is null");
		}
		this.latestOutStockTimeLessThanOrEqual = latestOutStockTimeLessThanOrEqual;
		return this;
	}

	public GoodsStockQuery quantityGreaterThan(Double quantityGreaterThan) {
		if (quantityGreaterThan == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityGreaterThan = quantityGreaterThan;
		return this;
	}

	public GoodsStockQuery quantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		if (quantityGreaterThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
		return this;
	}

	public GoodsStockQuery quantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		if (quantityLessThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
		return this;
	}

	public void setExpiryDateGreaterThanOrEqual(Date expiryDateGreaterThanOrEqual) {
		this.expiryDateGreaterThanOrEqual = expiryDateGreaterThanOrEqual;
	}

	public void setExpiryDateLessThanOrEqual(Date expiryDateLessThanOrEqual) {
		this.expiryDateLessThanOrEqual = expiryDateLessThanOrEqual;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public void setGoodsIds(List<Long> goodsIds) {
		this.goodsIds = goodsIds;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsNameLike(String goodsNameLike) {
		this.goodsNameLike = goodsNameLike;
	}

	public void setGoodsNodeId(Long goodsNodeId) {
		this.goodsNodeId = goodsNodeId;
	}

	public void setGoodsNodeIds(List<Long> goodsNodeIds) {
		this.goodsNodeIds = goodsNodeIds;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLatestOutStockTimeGreaterThanOrEqual(Date latestOutStockTimeGreaterThanOrEqual) {
		this.latestOutStockTimeGreaterThanOrEqual = latestOutStockTimeGreaterThanOrEqual;
	}

	public void setLatestOutStockTimeLessThanOrEqual(Date latestOutStockTimeLessThanOrEqual) {
		this.latestOutStockTimeLessThanOrEqual = latestOutStockTimeLessThanOrEqual;
	}

	public void setQuantityGreaterThan(Double quantityGreaterThan) {
		this.quantityGreaterThan = quantityGreaterThan;
	}

	public void setQuantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
	}

	public void setQuantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public GoodsStockQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public GoodsStockQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public GoodsStockQuery unit(String unit) {
		if (unit == null) {
			throw new RuntimeException("unit is null");
		}
		this.unit = unit;
		return this;
	}

}