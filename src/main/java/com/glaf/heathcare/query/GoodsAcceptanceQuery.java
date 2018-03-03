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

public class GoodsAcceptanceQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected long id;
	protected List<Long> ids;
	protected List<String> tenantIds;
	protected Long goodsId;
	protected List<Long> goodsIds;
	protected String goodsNameLike;
	protected Long goodsNodeId;
	protected Date acceptanceTimeGreaterThanOrEqual;
	protected Date acceptanceTimeLessThanOrEqual;
	protected Integer semester;
	protected Integer year;
	protected Integer month;
	protected Integer day;
	protected Integer week;
	protected Integer fullDay;
	protected Integer fullDayGreaterThanOrEqual;
	protected Integer fullDayLessThanOrEqual;
	protected Double quantityGreaterThanOrEqual;
	protected Double quantityLessThanOrEqual;
	protected String unit;
	protected String receiverId;
	protected List<String> receiverIds;
	protected String receiverNameLike;
	protected String remarkLike;
	protected Integer businessStatus;
	protected String confirmBy;
	protected Date confirmTimeGreaterThanOrEqual;
	protected Date confirmTimeLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String tableSuffix;

	public GoodsAcceptanceQuery() {

	}

	public GoodsAcceptanceQuery businessStatus(Integer businessStatus) {
		if (businessStatus == null) {
			throw new RuntimeException("businessStatus is null");
		}
		this.businessStatus = businessStatus;
		return this;
	}

	public GoodsAcceptanceQuery confirmBy(String confirmBy) {
		if (confirmBy == null) {
			throw new RuntimeException("confirmBy is null");
		}
		this.confirmBy = confirmBy;
		return this;
	}

	public GoodsAcceptanceQuery confirmTimeGreaterThanOrEqual(Date confirmTimeGreaterThanOrEqual) {
		if (confirmTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeGreaterThanOrEqual = confirmTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery confirmTimeLessThanOrEqual(Date confirmTimeLessThanOrEqual) {
		if (confirmTimeLessThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeLessThanOrEqual = confirmTimeLessThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public GoodsAcceptanceQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public Integer getBusinessStatus() {
		return businessStatus;
	}

	public String getConfirmBy() {
		return confirmBy;
	}

	public Date getConfirmTimeGreaterThanOrEqual() {
		return confirmTimeGreaterThanOrEqual;
	}

	public Date getConfirmTimeLessThanOrEqual() {
		return confirmTimeLessThanOrEqual;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public Integer getDay() {
		return day;
	}

	public Integer getFullDay() {
		return fullDay;
	}

	public Integer getFullDayGreaterThanOrEqual() {
		return fullDayGreaterThanOrEqual;
	}

	public Integer getFullDayLessThanOrEqual() {
		return fullDayLessThanOrEqual;
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

	public long getId() {
		return id;
	}

	public List<Long> getIds() {
		return ids;
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

			if ("tenantId".equals(sortColumn)) {
				orderBy = "E.TENANTID_" + a_x;
			}

			if ("goodsId".equals(sortColumn)) {
				orderBy = "E.GOODSID_" + a_x;
			}

			if ("goodsName".equals(sortColumn)) {
				orderBy = "E.GOODSNAME_" + a_x;
			}

			if ("acceptanceTime".equals(sortColumn)) {
				orderBy = "E.ACCEPTANCE_TIME_" + a_x;
			}

			if ("quantity".equals(sortColumn)) {
				orderBy = "E.QUANTITY_" + a_x;
			}

			if ("unit".equals(sortColumn)) {
				orderBy = "E.UNIT_" + a_x;
			}

			if ("receiverId".equals(sortColumn)) {
				orderBy = "E.RECEIVERID_" + a_x;
			}

			if ("receiverName".equals(sortColumn)) {
				orderBy = "E.RECEIVERNAME_" + a_x;
			}

			if ("remark".equals(sortColumn)) {
				orderBy = "E.REMARK_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public Date getOutStockTimeGreaterThanOrEqual() {
		return acceptanceTimeGreaterThanOrEqual;
	}

	public Date getOutStockTimeLessThanOrEqual() {
		return acceptanceTimeLessThanOrEqual;
	}

	public Double getQuantityGreaterThanOrEqual() {
		return quantityGreaterThanOrEqual;
	}

	public Double getQuantityLessThanOrEqual() {
		return quantityLessThanOrEqual;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public List<String> getReceiverIds() {
		return receiverIds;
	}

	public String getReceiverNameLike() {
		if (receiverNameLike != null && receiverNameLike.trim().length() > 0) {
			if (!receiverNameLike.startsWith("%")) {
				receiverNameLike = "%" + receiverNameLike;
			}
			if (!receiverNameLike.endsWith("%")) {
				receiverNameLike = receiverNameLike + "%";
			}
		}
		return receiverNameLike;
	}

	public String getRemarkLike() {
		if (remarkLike != null && remarkLike.trim().length() > 0) {
			if (!remarkLike.startsWith("%")) {
				remarkLike = "%" + remarkLike;
			}
			if (!remarkLike.endsWith("%")) {
				remarkLike = remarkLike + "%";
			}
		}
		return remarkLike;
	}

	public Integer getSemester() {
		return semester;
	}

	public String getTableSuffix() {
		return tableSuffix;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public String getUnit() {
		return unit;
	}

	public Integer getWeek() {
		return week;
	}

	public Integer getYear() {
		return year;
	}

	public GoodsAcceptanceQuery goodsId(Long goodsId) {
		if (goodsId == null) {
			throw new RuntimeException("goodsId is null");
		}
		this.goodsId = goodsId;
		return this;
	}

	public GoodsAcceptanceQuery goodsIds(List<Long> goodsIds) {
		if (goodsIds == null) {
			throw new RuntimeException("goodsIds is empty ");
		}
		this.goodsIds = goodsIds;
		return this;
	}

	public GoodsAcceptanceQuery goodsNameLike(String goodsNameLike) {
		if (goodsNameLike == null) {
			throw new RuntimeException("goodsName is null");
		}
		this.goodsNameLike = goodsNameLike;
		return this;
	}

	public GoodsAcceptanceQuery goodsNodeId(Long goodsNodeId) {
		if (goodsNodeId == null) {
			throw new RuntimeException("goodsNodeId is null");
		}
		this.goodsNodeId = goodsNodeId;
		return this;
	}

	public GoodsAcceptanceQuery ids(List<Long> ids) {
		if (ids == null) {
			throw new RuntimeException("ids is null");
		}
		this.ids = ids;
		return this;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("tenantId", "TENANTID_");
		addColumn("goodsId", "GOODSID_");
		addColumn("goodsName", "GOODSNAME_");
		addColumn("acceptanceTime", "ACCEPTANCE_TIME_");
		addColumn("quantity", "QUANTITY_");
		addColumn("unit", "UNIT_");
		addColumn("receiverId", "RECEIVERID_");
		addColumn("receiverName", "RECEIVERNAME_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public GoodsAcceptanceQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public GoodsAcceptanceQuery acceptanceTimeGreaterThanOrEqual(Date acceptanceTimeGreaterThanOrEqual) {
		if (acceptanceTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("acceptanceTime is null");
		}
		this.acceptanceTimeGreaterThanOrEqual = acceptanceTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery acceptanceTimeLessThanOrEqual(Date acceptanceTimeLessThanOrEqual) {
		if (acceptanceTimeLessThanOrEqual == null) {
			throw new RuntimeException("acceptanceTime is null");
		}
		this.acceptanceTimeLessThanOrEqual = acceptanceTimeLessThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery quantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		if (quantityGreaterThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery quantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		if (quantityLessThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
		return this;
	}

	public GoodsAcceptanceQuery receiverId(String receiverId) {
		if (receiverId == null) {
			throw new RuntimeException("receiverId is null");
		}
		this.receiverId = receiverId;
		return this;
	}

	public GoodsAcceptanceQuery receiverIds(List<String> receiverIds) {
		if (receiverIds == null) {
			throw new RuntimeException("receiverIds is empty ");
		}
		this.receiverIds = receiverIds;
		return this;
	}

	public GoodsAcceptanceQuery receiverNameLike(String receiverNameLike) {
		if (receiverNameLike == null) {
			throw new RuntimeException("receiverName is null");
		}
		this.receiverNameLike = receiverNameLike;
		return this;
	}

	public GoodsAcceptanceQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public GoodsAcceptanceQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setBusinessStatus(Integer businessStatus) {
		this.businessStatus = businessStatus;
	}

	public void setConfirmBy(String confirmBy) {
		this.confirmBy = confirmBy;
	}

	public void setConfirmTimeGreaterThanOrEqual(Date confirmTimeGreaterThanOrEqual) {
		this.confirmTimeGreaterThanOrEqual = confirmTimeGreaterThanOrEqual;
	}

	public void setConfirmTimeLessThanOrEqual(Date confirmTimeLessThanOrEqual) {
		this.confirmTimeLessThanOrEqual = confirmTimeLessThanOrEqual;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public void setFullDay(Integer fullDay) {
		this.fullDay = fullDay;
	}

	public void setFullDayGreaterThanOrEqual(Integer fullDayGreaterThanOrEqual) {
		this.fullDayGreaterThanOrEqual = fullDayGreaterThanOrEqual;
	}

	public void setFullDayLessThanOrEqual(Integer fullDayLessThanOrEqual) {
		this.fullDayLessThanOrEqual = fullDayLessThanOrEqual;
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

	public void setId(long id) {
		this.id = id;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setOutStockTimeGreaterThanOrEqual(Date acceptanceTimeGreaterThanOrEqual) {
		this.acceptanceTimeGreaterThanOrEqual = acceptanceTimeGreaterThanOrEqual;
	}

	public void setOutStockTimeLessThanOrEqual(Date acceptanceTimeLessThanOrEqual) {
		this.acceptanceTimeLessThanOrEqual = acceptanceTimeLessThanOrEqual;
	}

	public void setQuantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
	}

	public void setQuantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public void setReceiverIds(List<String> receiverIds) {
		this.receiverIds = receiverIds;
	}

	public void setReceiverNameLike(String receiverNameLike) {
		this.receiverNameLike = receiverNameLike;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public GoodsAcceptanceQuery tenantId(String tenantId) {
		if (tenantId == null) {
			throw new RuntimeException("tenantId is null");
		}
		this.tenantId = tenantId;
		return this;
	}

	public GoodsAcceptanceQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public GoodsAcceptanceQuery unit(String unit) {
		if (unit == null) {
			throw new RuntimeException("unit is null");
		}
		this.unit = unit;
		return this;
	}

	public GoodsAcceptanceQuery week(Integer week) {
		if (week == null) {
			throw new RuntimeException("week is null");
		}
		this.week = week;
		return this;
	}

	public GoodsAcceptanceQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}