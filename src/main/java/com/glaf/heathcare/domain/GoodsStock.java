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

package com.glaf.heathcare.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "GOODS_STOCK")
public class GoodsStock implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64, nullable = false)
	protected String id;

	/**
	 * 租户编号
	 */
	@Column(name = "TENANTID_", length = 50)
	protected String tenantId;

	/**
	 * 物品编号
	 */
	@Column(name = "GOODSID_")
	protected long goodsId;

	/**
	 * 物品名称
	 */
	@Column(name = "GOODSNAME_", length = 200)
	protected String goodsName;

	/**
	 * 物品分类编号
	 */
	@Column(name = "GOODSNODEID_")
	protected long goodsNodeId;

	/**
	 * 数量
	 */
	@Column(name = "QUANTITY_")
	protected double quantity;

	/**
	 * 计量单位
	 */
	@Column(name = "UNIT_", length = 20)
	protected String unit;

	/**
	 * 有效期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRYDATE_")
	protected Date expiryDate;

	/**
	 * 最近出库日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LATESTOUTSTOCKTIME_")
	protected Date latestOutStockTime;

	/**
	 * 表后缀
	 */
	@javax.persistence.Transient
	protected String tableSuffix;

	public GoodsStock() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsStock other = (GoodsStock) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public String getExpiryDateString() {
		if (this.expiryDate != null) {
			return DateUtils.getDateTime(this.expiryDate);
		}
		return "";
	}

	public long getGoodsId() {
		return this.goodsId;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public long getGoodsNodeId() {
		return this.goodsNodeId;
	}

	public String getId() {
		return this.id;
	}

	public Date getLatestOutStockTime() {
		return this.latestOutStockTime;
	}

	public String getLatestOutStockTimeString() {
		if (this.latestOutStockTime != null) {
			return DateUtils.getDateTime(this.latestOutStockTime);
		}
		return "";
	}

	public double getQuantity() {
		if (quantity > 0) {
			quantity = Math.round(quantity * 10D) / 10D;
		}
		return this.quantity;
	}

	public String getTableSuffix() {
		if (tableSuffix == null) {
			tableSuffix = "";
		}
		return tableSuffix;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public String getUnit() {
		return this.unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public GoodsStock jsonToObject(JSONObject jsonObject) {
		return GoodsStockJsonFactory.jsonToObject(jsonObject);
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsNodeId(long goodsNodeId) {
		this.goodsNodeId = goodsNodeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLatestOutStockTime(Date latestOutStockTime) {
		this.latestOutStockTime = latestOutStockTime;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public JSONObject toJsonObject() {
		return GoodsStockJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GoodsStockJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
