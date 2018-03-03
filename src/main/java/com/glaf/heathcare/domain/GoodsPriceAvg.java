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
@Table(name = "GOODS_PRICE_AVG")
public class GoodsPriceAvg implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 省/直辖市
	 */
	@Column(name = "PROVINCE_", length = 100)
	protected String province;

	/**
	 * 省/直辖市编号
	 */
	@Column(name = "PROVINCEID_")
	protected long provinceId;

	/**
	 * 市
	 */
	@Column(name = "CITY_", length = 200)
	protected String city;

	/**
	 * 市编号
	 */
	@Column(name = "CITYID_")
	protected long cityId;

	/**
	 * 区/县
	 */
	@Column(name = "AREA_", length = 200)
	protected String area;

	/**
	 * 区/县编号
	 */
	@Column(name = "AREAID_")
	protected long areaId;

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
	 * 平均单价
	 */
	@Column(name = "PRICE_")
	protected double price;

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
	 * 年月日
	 */
	@Column(name = "FULLDAY_")
	protected int fullDay;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public GoodsPriceAvg() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsPriceAvg other = (GoodsPriceAvg) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getArea() {
		return this.area;
	}

	public long getAreaId() {
		return this.areaId;
	}

	public String getCity() {
		return this.city;
	}

	public long getCityId() {
		return this.cityId;
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
		return this.price;
	}

	public String getProvince() {
		return this.province;
	}

	public long getProvinceId() {
		return this.provinceId;
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

	public GoodsPriceAvg jsonToObject(JSONObject jsonObject) {
		return GoodsPriceAvgJsonFactory.jsonToObject(jsonObject);
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return GoodsPriceAvgJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GoodsPriceAvgJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
