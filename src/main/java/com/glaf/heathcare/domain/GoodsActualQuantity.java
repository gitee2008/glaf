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
 * 物品实际用量表
 *
 */

@Entity
@Table(name = "GOODS_ACTUAL_QUANTITY")
public class GoodsActualQuantity implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

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
	 * 物品分类码
	 */
	@Column(name = "GOODSNODEID_")
	protected long goodsNodeId;

	/**
	 * 学期
	 */
	@Column(name = "SEMESTER_")
	protected int semester;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

	/**
	 * 日
	 */
	@Column(name = "DAY_")
	protected int day;

	/**
	 * 周
	 */
	@Column(name = "WEEK_")
	protected int week;

	/**
	 * 年月日
	 */
	@Column(name = "FULLDAY_")
	protected int fullDay;

	/**
	 * 数量
	 */
	@Column(name = "QUANTITY_")
	protected double quantity;

	@javax.persistence.Transient
	protected String quantityString;

	/**
	 * 均量
	 */
	@javax.persistence.Transient
	protected double avgQuantity;

	@javax.persistence.Transient
	protected String avgQuantityString;

	/**
	 * 总量
	 */
	@javax.persistence.Transient
	protected double totalQuantity;

	@javax.persistence.Transient
	protected String totalQuantityString;

	/**
	 * 采购单价
	 */
	@Column(name = "PRICE_")
	protected double price;

	@javax.persistence.Transient
	protected String priceString;

	/**
	 * 采购总价格
	 */
	@Column(name = "TOTALPRICE_")
	protected double totalPrice;

	@javax.persistence.Transient
	protected String totalPriceString;
	/**
	 * 计量单位
	 */
	@Column(name = "UNIT_", length = 20)
	protected String unit;

	/**
	 * 使用时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "USAGETIME_")
	protected Date usageTime;

	/**
	 * 业务状态
	 */
	@Column(name = "BUSINESSSTATUS_")
	protected int businessStatus;

	/**
	 * 确认人
	 */
	@Column(name = "CONFIRMBY_", length = 50)
	protected String confirmBy;

	/**
	 * 确认时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONFIRMTIME_")
	protected Date confirmTime;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	/**
	 * 表后缀
	 */
	@javax.persistence.Transient
	protected String tableSuffix;

	public GoodsActualQuantity() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsActualQuantity other = (GoodsActualQuantity) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public double getAvgQuantity() {
		if (avgQuantity > 0) {
			avgQuantity = Math.round(avgQuantity * 10D) / 10D;
		}
		return this.avgQuantity;
	}

	public String getAvgQuantityString() {
		avgQuantityString = "";
		if (avgQuantity > 0) {
			avgQuantityString = String.valueOf(getAvgQuantity());
		}
		return avgQuantityString;
	}

	public int getBusinessStatus() {
		return this.businessStatus;
	}

	public String getConfirmBy() {
		return this.confirmBy;
	}

	public Date getConfirmTime() {
		return this.confirmTime;
	}

	public String getConfirmTimeString() {
		if (this.confirmTime != null) {
			return DateUtils.getDateTime(this.confirmTime);
		}
		return "";
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public int getDay() {
		return this.day;
	}

	public int getFullDay() {
		return this.fullDay;
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

	public long getId() {
		return this.id;
	}

	public int getMonth() {
		return this.month;
	}

	public double getPrice() {
		if (price > 0) {
			price = Math.round(price * 100D) / 100D;
		}
		return price;
	}

	public String getPriceString() {
		priceString = "";
		if (price > 0) {
			priceString = String.valueOf(getPrice());
		}
		return priceString;
	}

	public double getQuantity() {
		if (quantity > 0) {
			quantity = Math.round(quantity * 10D) / 10D;
		}
		return this.quantity;
	}

	public String getQuantityString() {
		quantityString = "";
		if (quantity > 0) {
			quantityString = String.valueOf(getQuantity());
		}
		return quantityString;
	}

	public int getSemester() {
		return semester;
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

	public double getTotalPrice() {
		if (totalPrice > 0) {
			totalPrice = Math.round(totalPrice * 100D) / 100D;
		}
		return totalPrice;
	}

	public String getTotalPriceString() {
		totalPriceString = "";
		if (totalPrice > 0) {
			totalPriceString = String.valueOf(getTotalPrice());
		}
		return totalPriceString;
	}

	public double getTotalQuantity() {
		if (totalQuantity > 0) {
			totalQuantity = Math.round(totalQuantity * 10D) / 10D;
		}
		return this.totalQuantity;
	}

	public String getTotalQuantityString() {
		totalQuantityString = "";
		if (totalQuantity > 0) {
			totalQuantityString = String.valueOf(getTotalQuantity());
		}
		return totalQuantityString;
	}

	public String getUnit() {
		return this.unit;
	}

	public Date getUsageTime() {
		return this.usageTime;
	}

	public String getUsageTimeString() {
		if (this.usageTime != null) {
			return DateUtils.getDateTime(this.usageTime);
		}
		return "";
	}

	public int getWeek() {
		return this.week;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public GoodsActualQuantity jsonToObject(JSONObject jsonObject) {
		return GoodsActualQuantityJsonFactory.jsonToObject(jsonObject);
	}

	public void setAvgQuantity(double avgQuantity) {
		this.avgQuantity = avgQuantity;
	}

	public void setAvgQuantityString(String avgQuantityString) {
		this.avgQuantityString = avgQuantityString;
	}

	public void setBusinessStatus(int businessStatus) {
		this.businessStatus = businessStatus;
	}

	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
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

	public void setId(long id) {
		this.id = id;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setQuantityString(String quantityString) {
		this.quantityString = quantityString;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setTotalPriceString(String totalPriceString) {
		this.totalPriceString = totalPriceString;
	}

	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public void setTotalQuantityString(String totalQuantityString) {
		this.totalQuantityString = totalQuantityString;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setUsageTime(Date usageTime) {
		this.usageTime = usageTime;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return GoodsActualQuantityJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GoodsActualQuantityJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
