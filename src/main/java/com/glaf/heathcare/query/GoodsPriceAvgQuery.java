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

public class GoodsPriceAvgQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String province;
	protected Long provinceId;
	protected List<Long> provinceIds;
	protected String city;
	protected Long cityId;
	protected List<Long> cityIds;
	protected String area;
	protected Long areaId;
	protected List<Long> areaIds;
	protected Long goodsId;
	protected List<Long> goodsIds;
	protected String goodsNameLike;
	protected Long goodsNodeId;
	protected List<Long> goodsNodeIds;
	protected Double priceGreaterThanOrEqual;
	protected Double priceLessThanOrEqual;
	protected Integer year;
	protected Integer month;
	protected Integer fullDay;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public GoodsPriceAvgQuery() {

	}

	public GoodsPriceAvgQuery area(String area) {
		if (area == null) {
			throw new RuntimeException("area is null");
		}
		this.area = area;
		return this;
	}

	public GoodsPriceAvgQuery areaId(Long areaId) {
		if (areaId == null) {
			throw new RuntimeException("areaId is null");
		}
		this.areaId = areaId;
		return this;
	}

	public GoodsPriceAvgQuery areaIds(List<Long> areaIds) {
		if (areaIds == null) {
			throw new RuntimeException("areaIds is empty ");
		}
		this.areaIds = areaIds;
		return this;
	}

	public GoodsPriceAvgQuery city(String city) {
		if (city == null) {
			throw new RuntimeException("city is null");
		}
		this.city = city;
		return this;
	}

	public GoodsPriceAvgQuery cityId(Long cityId) {
		if (cityId == null) {
			throw new RuntimeException("cityId is null");
		}
		this.cityId = cityId;
		return this;
	}

	public GoodsPriceAvgQuery cityIds(List<Long> cityIds) {
		if (cityIds == null) {
			throw new RuntimeException("cityIds is empty ");
		}
		this.cityIds = cityIds;
		return this;
	}

	public GoodsPriceAvgQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsPriceAvgQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public GoodsPriceAvgQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public String getArea() {
		return area;
	}

	public Long getAreaId() {
		return areaId;
	}

	public List<Long> getAreaIds() {
		return areaIds;
	}

	public String getCity() {
		return city;
	}

	public Long getCityId() {
		return cityId;
	}

	public List<Long> getCityIds() {
		return cityIds;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Integer getFullDay() {
		return fullDay;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public List<Long> getGoodsIds() {
		return goodsIds;
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

	public Integer getMonth() {
		return month;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("province".equals(sortColumn)) {
				orderBy = "E.PROVINCE_" + a_x;
			}

			if ("provinceId".equals(sortColumn)) {
				orderBy = "E.PROVINCEID_" + a_x;
			}

			if ("city".equals(sortColumn)) {
				orderBy = "E.CITY_" + a_x;
			}

			if ("cityId".equals(sortColumn)) {
				orderBy = "E.CITYID_" + a_x;
			}

			if ("area".equals(sortColumn)) {
				orderBy = "E.AREA_" + a_x;
			}

			if ("areaId".equals(sortColumn)) {
				orderBy = "E.AREAID_" + a_x;
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

			if ("price".equals(sortColumn)) {
				orderBy = "E.PRICE_" + a_x;
			}

			if ("year".equals(sortColumn)) {
				orderBy = "E.YEAR_" + a_x;
			}

			if ("month".equals(sortColumn)) {
				orderBy = "E.MONTH_" + a_x;
			}

			if ("fullDay".equals(sortColumn)) {
				orderBy = "E.FULLDAY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Double getPriceGreaterThanOrEqual() {
		return priceGreaterThanOrEqual;
	}

	public Double getPriceLessThanOrEqual() {
		return priceLessThanOrEqual;
	}

	public String getProvince() {
		return province;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public List<Long> getProvinceIds() {
		return provinceIds;
	}

	public Integer getYear() {
		return year;
	}

	public GoodsPriceAvgQuery goodsId(Long goodsId) {
		if (goodsId == null) {
			throw new RuntimeException("goodsId is null");
		}
		this.goodsId = goodsId;
		return this;
	}

	public GoodsPriceAvgQuery goodsIds(List<Long> goodsIds) {
		if (goodsIds == null) {
			throw new RuntimeException("goodsIds is empty ");
		}
		this.goodsIds = goodsIds;
		return this;
	}

	public GoodsPriceAvgQuery goodsNameLike(String goodsNameLike) {
		if (goodsNameLike == null) {
			throw new RuntimeException("goodsName is null");
		}
		this.goodsNameLike = goodsNameLike;
		return this;
	}

	public GoodsPriceAvgQuery goodsNodeId(Long goodsNodeId) {
		if (goodsNodeId == null) {
			throw new RuntimeException("goodsNodeId is null");
		}
		this.goodsNodeId = goodsNodeId;
		return this;
	}

	public GoodsPriceAvgQuery goodsNodeIds(List<Long> goodsNodeIds) {
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
		addColumn("province", "PROVINCE_");
		addColumn("provinceId", "PROVINCEID_");
		addColumn("city", "CITY_");
		addColumn("cityId", "CITYID_");
		addColumn("area", "AREA_");
		addColumn("areaId", "AREAID_");
		addColumn("goodsId", "GOODSID_");
		addColumn("goodsName", "GOODSNAME_");
		addColumn("goodsNodeId", "GOODSNODEID_");
		addColumn("price", "PRICE_");
		addColumn("year", "YEAR_");
		addColumn("month", "MONTH_");
		addColumn("fullDay", "FULLDAY_");
		addColumn("createTime", "CREATETIME_");
	}

	public GoodsPriceAvgQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public GoodsPriceAvgQuery priceGreaterThanOrEqual(Double priceGreaterThanOrEqual) {
		if (priceGreaterThanOrEqual == null) {
			throw new RuntimeException("price is null");
		}
		this.priceGreaterThanOrEqual = priceGreaterThanOrEqual;
		return this;
	}

	public GoodsPriceAvgQuery priceLessThanOrEqual(Double priceLessThanOrEqual) {
		if (priceLessThanOrEqual == null) {
			throw new RuntimeException("price is null");
		}
		this.priceLessThanOrEqual = priceLessThanOrEqual;
		return this;
	}

	public GoodsPriceAvgQuery province(String province) {
		if (province == null) {
			throw new RuntimeException("province is null");
		}
		this.province = province;
		return this;
	}

	public GoodsPriceAvgQuery provinceId(Long provinceId) {
		if (provinceId == null) {
			throw new RuntimeException("provinceId is null");
		}
		this.provinceId = provinceId;
		return this;
	}

	public GoodsPriceAvgQuery provinceIds(List<Long> provinceIds) {
		if (provinceIds == null) {
			throw new RuntimeException("provinceIds is empty ");
		}
		this.provinceIds = provinceIds;
		return this;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public void setAreaIds(List<Long> areaIds) {
		this.areaIds = areaIds;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public void setCityIds(List<Long> cityIds) {
		this.cityIds = cityIds;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public void setGoodsIds(List<Long> goodsIds) {
		this.goodsIds = goodsIds;
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

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setPriceGreaterThanOrEqual(Double priceGreaterThanOrEqual) {
		this.priceGreaterThanOrEqual = priceGreaterThanOrEqual;
	}

	public void setPriceLessThanOrEqual(Double priceLessThanOrEqual) {
		this.priceLessThanOrEqual = priceLessThanOrEqual;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public void setProvinceIds(List<Long> provinceIds) {
		this.provinceIds = provinceIds;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public GoodsPriceAvgQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}