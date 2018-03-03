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

public class GoodsPurchaseQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected long id;
	protected List<Long> ids;
	protected List<String> tenantIds;
	protected Long goodsId;
	protected List<Long> goodsIds;
	protected String goodsNameLike;
	protected Long goodsNodeId;
	protected Date purchaseTimeGreaterThanOrEqual;
	protected Date purchaseTimeLessThanOrEqual;
	protected Integer semester;
	protected Integer year;
	protected Integer month;
	protected Integer week;
	protected Integer day;
	protected Integer fullDay;
	protected Integer fullDayGreaterThanOrEqual;
	protected Integer fullDayLessThanOrEqual;
	protected Double quantityGreaterThanOrEqual;
	protected Double quantityLessThanOrEqual;
	protected String unit;
	protected Double priceGreaterThanOrEqual;
	protected Double priceLessThanOrEqual;
	protected Double totalPriceGreaterThanOrEqual;
	protected Double totalPriceLessThanOrEqual;
	protected String proposerId;
	protected List<String> proposerIds;
	protected String proposerNameLike;
	protected String batchNoLike;
	protected String contactLike;
	protected String standard;
	protected String addressLike;
	protected String supplierLike;
	protected String invoiceFlag;
	protected String remarkLike;
	protected Integer businessStatus;
	protected String confirmBy;
	protected Date expiryDateGreaterThanOrEqual;
	protected Date expiryDateLessThanOrEqual;
	protected Date confirmTimeGreaterThanOrEqual;
	protected Date confirmTimeLessThanOrEqual;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected String tableSuffix;

	public GoodsPurchaseQuery() {

	}

	public GoodsPurchaseQuery addressLike(String addressLike) {
		if (addressLike == null) {
			throw new RuntimeException("address is null");
		}
		this.addressLike = addressLike;
		return this;
	}

	public GoodsPurchaseQuery batchNoLike(String batchNoLike) {
		if (batchNoLike == null) {
			throw new RuntimeException("batchNo is null");
		}
		this.batchNoLike = batchNoLike;
		return this;
	}

	public GoodsPurchaseQuery businessStatus(Integer businessStatus) {
		if (businessStatus == null) {
			throw new RuntimeException("businessStatus is null");
		}
		this.businessStatus = businessStatus;
		return this;
	}

	public GoodsPurchaseQuery confirmBy(String confirmBy) {
		if (confirmBy == null) {
			throw new RuntimeException("confirmBy is null");
		}
		this.confirmBy = confirmBy;
		return this;
	}

	public GoodsPurchaseQuery confirmTimeGreaterThanOrEqual(Date confirmTimeGreaterThanOrEqual) {
		if (confirmTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeGreaterThanOrEqual = confirmTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery confirmTimeLessThanOrEqual(Date confirmTimeLessThanOrEqual) {
		if (confirmTimeLessThanOrEqual == null) {
			throw new RuntimeException("confirmTime is null");
		}
		this.confirmTimeLessThanOrEqual = confirmTimeLessThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery contactLike(String contactLike) {
		if (contactLike == null) {
			throw new RuntimeException("contact is null");
		}
		this.contactLike = contactLike;
		return this;
	}

	public GoodsPurchaseQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public GoodsPurchaseQuery fullDay(Integer fullDay) {
		if (fullDay == null) {
			throw new RuntimeException("fullDay is null");
		}
		this.fullDay = fullDay;
		return this;
	}

	public String getAddressLike() {
		if (addressLike != null && addressLike.trim().length() > 0) {
			if (!addressLike.startsWith("%")) {
				addressLike = "%" + addressLike;
			}
			if (!addressLike.endsWith("%")) {
				addressLike = addressLike + "%";
			}
		}
		return addressLike;
	}

	public String getBatchNoLike() {
		if (batchNoLike != null && batchNoLike.trim().length() > 0) {
			if (!batchNoLike.startsWith("%")) {
				batchNoLike = "%" + batchNoLike;
			}
			if (!batchNoLike.endsWith("%")) {
				batchNoLike = batchNoLike + "%";
			}
		}
		return batchNoLike;
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

	public String getContactLike() {
		if (contactLike != null && contactLike.trim().length() > 0) {
			if (!contactLike.startsWith("%")) {
				contactLike = "%" + contactLike;
			}
			if (!contactLike.endsWith("%")) {
				contactLike = contactLike + "%";
			}
		}
		return contactLike;
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

	public Date getExpiryDateGreaterThanOrEqual() {
		return expiryDateGreaterThanOrEqual;
	}

	public Date getExpiryDateLessThanOrEqual() {
		return expiryDateLessThanOrEqual;
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

	public Date getInStockTimeGreaterThanOrEqual() {
		return purchaseTimeGreaterThanOrEqual;
	}

	public Date getInStockTimeLessThanOrEqual() {
		return purchaseTimeLessThanOrEqual;
	}

	public String getInvoiceFlag() {
		return invoiceFlag;
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

			if ("purchaseTime".equals(sortColumn)) {
				orderBy = "E.PURCHASE_TIME_" + a_x;
			}

			if ("quantity".equals(sortColumn)) {
				orderBy = "E.QUANTITY_" + a_x;
			}

			if ("unit".equals(sortColumn)) {
				orderBy = "E.UNIT_" + a_x;
			}

			if ("price".equals(sortColumn)) {
				orderBy = "E.PRICE_" + a_x;
			}

			if ("totalPrice".equals(sortColumn)) {
				orderBy = "E.TOTALPRICE_" + a_x;
			}

			if ("expiryDate".equals(sortColumn)) {
				orderBy = "E.EXPIRYDATE_" + a_x;
			}

			if ("proposerId".equals(sortColumn)) {
				orderBy = "E.PROPOSERID_" + a_x;
			}

			if ("proposerName".equals(sortColumn)) {
				orderBy = "E.PROPOSERNAME_" + a_x;
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

	public Double getPriceGreaterThanOrEqual() {
		return priceGreaterThanOrEqual;
	}

	public Double getPriceLessThanOrEqual() {
		return priceLessThanOrEqual;
	}

	public String getProposerId() {
		return proposerId;
	}

	public List<String> getProposerIds() {
		return proposerIds;
	}

	public String getProposerNameLike() {
		if (proposerNameLike != null && proposerNameLike.trim().length() > 0) {
			if (!proposerNameLike.startsWith("%")) {
				proposerNameLike = "%" + proposerNameLike;
			}
			if (!proposerNameLike.endsWith("%")) {
				proposerNameLike = proposerNameLike + "%";
			}
		}
		return proposerNameLike;
	}

	public Date getPurchaseTimeGreaterThanOrEqual() {
		return purchaseTimeGreaterThanOrEqual;
	}

	public Date getPurchaseTimeLessThanOrEqual() {
		return purchaseTimeLessThanOrEqual;
	}

	public Double getQuantityGreaterThanOrEqual() {
		return quantityGreaterThanOrEqual;
	}

	public Double getQuantityLessThanOrEqual() {
		return quantityLessThanOrEqual;
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

	public String getStandard() {
		return standard;
	}

	public String getSupplierLike() {
		if (supplierLike != null && supplierLike.trim().length() > 0) {
			if (!supplierLike.startsWith("%")) {
				supplierLike = "%" + supplierLike;
			}
			if (!supplierLike.endsWith("%")) {
				supplierLike = supplierLike + "%";
			}
		}
		return supplierLike;
	}

	public String getTableSuffix() {
		return tableSuffix;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public Double getTotalPriceGreaterThanOrEqual() {
		return totalPriceGreaterThanOrEqual;
	}

	public Double getTotalPriceLessThanOrEqual() {
		return totalPriceLessThanOrEqual;
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

	public GoodsPurchaseQuery goodsId(Long goodsId) {
		if (goodsId == null) {
			throw new RuntimeException("goodsId is null");
		}
		this.goodsId = goodsId;
		return this;
	}

	public GoodsPurchaseQuery goodsIds(List<Long> goodsIds) {
		if (goodsIds == null) {
			throw new RuntimeException("goodsIds is empty ");
		}
		this.goodsIds = goodsIds;
		return this;
	}

	public GoodsPurchaseQuery goodsNameLike(String goodsNameLike) {
		if (goodsNameLike == null) {
			throw new RuntimeException("goodsName is null");
		}
		this.goodsNameLike = goodsNameLike;
		return this;
	}

	public GoodsPurchaseQuery goodsNodeId(Long goodsNodeId) {
		if (goodsNodeId == null) {
			throw new RuntimeException("goodsNodeId is null");
		}
		this.goodsNodeId = goodsNodeId;
		return this;
	}

	public GoodsPurchaseQuery ids(List<Long> ids) {
		if (ids == null) {
			throw new RuntimeException("ids is empty ");
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
		addColumn("purchaseTime", "PURCHASE_TIME_");
		addColumn("quantity", "QUANTITY_");
		addColumn("unit", "UNIT_");
		addColumn("price", "PRICE_");
		addColumn("totalPrice", "TOTALPRICE_");
		addColumn("expiryDate", "EXPIRYDATE_");
		addColumn("proposerId", "PROPOSERID_");
		addColumn("proposerName", "PROPOSERNAME_");
		addColumn("remark", "REMARK_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public GoodsPurchaseQuery invoiceFlag(String invoiceFlag) {
		if (invoiceFlag == null) {
			throw new RuntimeException("invoiceFlag is null");
		}
		this.invoiceFlag = invoiceFlag;
		return this;
	}

	public GoodsPurchaseQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public GoodsPurchaseQuery priceGreaterThanOrEqual(Double priceGreaterThanOrEqual) {
		if (priceGreaterThanOrEqual == null) {
			throw new RuntimeException("price is null");
		}
		this.priceGreaterThanOrEqual = priceGreaterThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery priceLessThanOrEqual(Double priceLessThanOrEqual) {
		if (priceLessThanOrEqual == null) {
			throw new RuntimeException("price is null");
		}
		this.priceLessThanOrEqual = priceLessThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery proposerId(String proposerId) {
		if (proposerId == null) {
			throw new RuntimeException("proposerId is null");
		}
		this.proposerId = proposerId;
		return this;
	}

	public GoodsPurchaseQuery proposerIds(List<String> proposerIds) {
		if (proposerIds == null) {
			throw new RuntimeException("proposerIds is empty ");
		}
		this.proposerIds = proposerIds;
		return this;
	}

	public GoodsPurchaseQuery proposerNameLike(String proposerNameLike) {
		if (proposerNameLike == null) {
			throw new RuntimeException("proposerName is null");
		}
		this.proposerNameLike = proposerNameLike;
		return this;
	}

	public GoodsPurchaseQuery purchaseTimeGreaterThanOrEqual(Date purchaseTimeGreaterThanOrEqual) {
		if (purchaseTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("purchaseTime is null");
		}
		this.purchaseTimeGreaterThanOrEqual = purchaseTimeGreaterThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery purchaseTimeLessThanOrEqual(Date purchaseTimeLessThanOrEqual) {
		if (purchaseTimeLessThanOrEqual == null) {
			throw new RuntimeException("purchaseTime is null");
		}
		this.purchaseTimeLessThanOrEqual = purchaseTimeLessThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery quantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		if (quantityGreaterThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery quantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		if (quantityLessThanOrEqual == null) {
			throw new RuntimeException("quantity is null");
		}
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery remarkLike(String remarkLike) {
		if (remarkLike == null) {
			throw new RuntimeException("remark is null");
		}
		this.remarkLike = remarkLike;
		return this;
	}

	public GoodsPurchaseQuery semester(Integer semester) {
		if (semester == null) {
			throw new RuntimeException("semester is null");
		}
		this.semester = semester;
		return this;
	}

	public void setAddressLike(String addressLike) {
		this.addressLike = addressLike;
	}

	public void setBatchNoLike(String batchNoLike) {
		this.batchNoLike = batchNoLike;
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

	public void setContactLike(String contactLike) {
		this.contactLike = contactLike;
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

	public void setExpiryDateGreaterThanOrEqual(Date expiryDateGreaterThanOrEqual) {
		this.expiryDateGreaterThanOrEqual = expiryDateGreaterThanOrEqual;
	}

	public void setExpiryDateLessThanOrEqual(Date expiryDateLessThanOrEqual) {
		this.expiryDateLessThanOrEqual = expiryDateLessThanOrEqual;
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

	public void setInStockTimeGreaterThanOrEqual(Date purchaseTimeGreaterThanOrEqual) {
		this.purchaseTimeGreaterThanOrEqual = purchaseTimeGreaterThanOrEqual;
	}

	public void setInStockTimeLessThanOrEqual(Date purchaseTimeLessThanOrEqual) {
		this.purchaseTimeLessThanOrEqual = purchaseTimeLessThanOrEqual;
	}

	public void setInvoiceFlag(String invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
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

	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}

	public void setProposerIds(List<String> proposerIds) {
		this.proposerIds = proposerIds;
	}

	public void setProposerNameLike(String proposerNameLike) {
		this.proposerNameLike = proposerNameLike;
	}

	public void setPurchaseTimeGreaterThanOrEqual(Date purchaseTimeGreaterThanOrEqual) {
		this.purchaseTimeGreaterThanOrEqual = purchaseTimeGreaterThanOrEqual;
	}

	public void setPurchaseTimeLessThanOrEqual(Date purchaseTimeLessThanOrEqual) {
		this.purchaseTimeLessThanOrEqual = purchaseTimeLessThanOrEqual;
	}

	public void setQuantityGreaterThanOrEqual(Double quantityGreaterThanOrEqual) {
		this.quantityGreaterThanOrEqual = quantityGreaterThanOrEqual;
	}

	public void setQuantityLessThanOrEqual(Double quantityLessThanOrEqual) {
		this.quantityLessThanOrEqual = quantityLessThanOrEqual;
	}

	public void setRemarkLike(String remarkLike) {
		this.remarkLike = remarkLike;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public void setSupplierLike(String supplierLike) {
		this.supplierLike = supplierLike;
	}

	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

	public void setTenantIds(List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public void setTotalPriceGreaterThanOrEqual(Double totalPriceGreaterThanOrEqual) {
		this.totalPriceGreaterThanOrEqual = totalPriceGreaterThanOrEqual;
	}

	public void setTotalPriceLessThanOrEqual(Double totalPriceLessThanOrEqual) {
		this.totalPriceLessThanOrEqual = totalPriceLessThanOrEqual;
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

	public GoodsPurchaseQuery standard(String standard) {
		if (standard == null) {
			throw new RuntimeException("standard is null");
		}
		this.standard = standard;
		return this;
	}

	public GoodsPurchaseQuery supplierLike(String supplierLike) {
		if (supplierLike == null) {
			throw new RuntimeException("supplier is null");
		}
		this.supplierLike = supplierLike;
		return this;
	}

	public GoodsPurchaseQuery tenantIds(List<String> tenantIds) {
		if (tenantIds == null) {
			throw new RuntimeException("tenantIds is empty ");
		}
		this.tenantIds = tenantIds;
		return this;
	}

	public GoodsPurchaseQuery totalPriceGreaterThanOrEqual(Double totalPriceGreaterThanOrEqual) {
		if (totalPriceGreaterThanOrEqual == null) {
			throw new RuntimeException("totalPrice is null");
		}
		this.totalPriceGreaterThanOrEqual = totalPriceGreaterThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery totalPriceLessThanOrEqual(Double totalPriceLessThanOrEqual) {
		if (totalPriceLessThanOrEqual == null) {
			throw new RuntimeException("totalPrice is null");
		}
		this.totalPriceLessThanOrEqual = totalPriceLessThanOrEqual;
		return this;
	}

	public GoodsPurchaseQuery unit(String unit) {
		if (unit == null) {
			throw new RuntimeException("unit is null");
		}
		this.unit = unit;
		return this;
	}

	public GoodsPurchaseQuery week(Integer week) {
		if (week == null) {
			throw new RuntimeException("week is null");
		}
		this.week = week;
		return this;
	}

	public GoodsPurchaseQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}