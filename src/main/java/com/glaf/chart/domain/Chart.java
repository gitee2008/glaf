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

package com.glaf.chart.domain;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.chart.util.ChartJsonFactory;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.JSONable;

@Entity
@Table(name = "SYS_CHART")
public class Chart implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	@Column(name = "NODEID_")
	protected Long nodeId;

	/**
	 * 查询编号
	 */
	@Column(name = "QUERYIDS_")
	protected String queryIds;

	@Column(name = "STATEMENTID_", length = 50)
	protected String statementId;

	/**
	 * 标题
	 */
	@Column(name = "SUBJECT_")
	protected String subject;

	/**
	 * 图表名称
	 */
	@Column(name = "CHARTNAME_")
	protected String chartName;

	/**
	 * 图表类型
	 */
	@Column(name = "CHARTTYPE_")
	protected String chartType;

	/**
	 * 图表字体
	 */
	@Column(name = "CHARTFONT_")
	protected String chartFont;

	/**
	 * 图表字体大小
	 */
	@Column(name = "CHARTFONTSIZE_")
	protected Integer chartFontSize;

	/**
	 * 图表主题
	 */
	@Column(name = "CHARTTITLE_")
	protected String chartTitle;

	/**
	 * 图表次主题
	 */
	@Column(name = "CHARTSUBTITLE_")
	protected String chartSubTitle;

	/**
	 * 图表标题栏字体
	 */
	@Column(name = "CHARTTITLEFONT_")
	protected String chartTitleFont;

	/**
	 * 图表标题栏字体大小
	 */
	@Column(name = "CHARTTITLEFONTSIZE_")
	protected Integer chartTitleFontSize;

	/**
	 * 图表次标题栏字体大小
	 */
	@Column(name = "CHARTSUBTITLEFONTSIZE_")
	protected Integer chartSubTitleFontSize;

	/**
	 * 图表宽带
	 */
	@Column(name = "CHARTWIDTH_")
	protected Integer chartWidth;

	/**
	 * 图表高度
	 */
	@Column(name = "CHARTHEIGHT_")
	protected Integer chartHeight;

	/**
	 * 是否显示图例
	 */
	@Column(name = "LEGEND_")
	protected String legend;

	/**
	 * 是否显示tooltip
	 */
	@Column(name = "TOOLTIP_", length = 100)
	protected String tooltip;

	/**
	 * 映射名称
	 */
	@Column(name = "MAPPING_", length = 50)
	protected String mapping;

	/**
	 * X坐标标签
	 */
	@Column(name = "COORDINATEX_")
	protected String coordinateX;

	/**
	 * Y坐标标签
	 */
	@Column(name = "COORDINATEY_")
	protected String coordinateY;

	/**
	 * 绘制方向
	 */
	@Column(name = "PLOTORIENTATION_")
	protected String plotOrientation;

	/**
	 * 生成图像类型
	 */
	@Column(name = "IMAGETYPE_")
	protected String imageType;

	/**
	 * 是否启用3D效果
	 */
	@Column(name = "ENABLE3DFLAG_", length = 1)
	protected String enable3DFlag;

	/**
	 * 是否启用
	 */
	@Column(name = "ENABLEFLAG_", length = 1)
	protected String enableFlag;

	@Column(name = "DATABASEID_")
	protected Long databaseId;

	@Column(name = "MAXROWCOUNT_")
	protected Integer maxRowCount;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE_")
	protected Date createDate;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_")
	protected String createBy;

	@javax.persistence.Transient
	public List<ColumnModel> columns = new java.util.ArrayList<ColumnModel>();

	public Chart() {

	}

	public void addCellData(ColumnModel cell) {
		if (columns == null) {
			columns = new java.util.ArrayList<ColumnModel>();
		}
		columns.add(cell);
	}

	public void addColumn(ColumnModel cell) {
		if (columns == null) {
			columns = new java.util.ArrayList<ColumnModel>();
		}
		if (!columns.contains(cell)) {
			columns.add(cell);
		}
	}

	public String getChartFont() {
		return this.chartFont;
	}

	public Integer getChartFontSize() {
		return this.chartFontSize;
	}

	public Integer getChartHeight() {
		return this.chartHeight;
	}

	public String getChartName() {
		return this.chartName;
	}

	public String getChartSubTitle() {
		return chartSubTitle;
	}

	public Integer getChartSubTitleFontSize() {
		return chartSubTitleFontSize;
	}

	public String getChartTitle() {
		return this.chartTitle;
	}

	public String getChartTitleFont() {
		return chartTitleFont;
	}

	public Integer getChartTitleFontSize() {
		return chartTitleFontSize;
	}

	public String getChartType() {
		return this.chartType;
	}

	public Integer getChartWidth() {
		return this.chartWidth;
	}

	public List<ColumnModel> getColumns() {
		if (columns == null) {
			columns = new java.util.ArrayList<ColumnModel>();
		}
		return columns;
	}

	public String getCoordinateX() {
		return this.coordinateX;
	}

	public String getCoordinateY() {
		return this.coordinateY;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public String getEnable3DFlag() {
		return enable3DFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public String getId() {
		return this.id;
	}

	public String getImageType() {
		return imageType;
	}

	public String getLegend() {
		return this.legend;
	}

	public String getMapping() {
		return mapping;
	}

	public Integer getMaxRowCount() {
		return maxRowCount;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public String getPlotOrientation() {
		return this.plotOrientation;
	}

	public String getQueryIds() {
		return this.queryIds;
	}

	public String getStatementId() {
		return statementId;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public Chart jsonToObject(JSONObject jsonObject) {
		return ChartJsonFactory.jsonToObject(jsonObject);
	}

	public void removeColumn(ColumnModel cell) {
		if (columns != null) {
			if (columns.contains(cell)) {
				columns.remove(cell);
			}
		}
	}

	public void setChartFont(String chartFont) {
		this.chartFont = chartFont;
	}

	public void setChartFontSize(Integer chartFontSize) {
		this.chartFontSize = chartFontSize;
	}

	public void setChartHeight(Integer chartHeight) {
		this.chartHeight = chartHeight;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public void setChartSubTitle(String chartSubTitle) {
		this.chartSubTitle = chartSubTitle;
	}

	public void setChartSubTitleFontSize(Integer chartSubTitleFontSize) {
		this.chartSubTitleFontSize = chartSubTitleFontSize;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public void setChartTitleFont(String chartTitleFont) {
		this.chartTitleFont = chartTitleFont;
	}

	public void setChartTitleFontSize(Integer chartTitleFontSize) {
		this.chartTitleFontSize = chartTitleFontSize;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public void setChartWidth(Integer chartWidth) {
		this.chartWidth = chartWidth;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	public void setCoordinateX(String coordinateX) {
		this.coordinateX = coordinateX;
	}

	public void setCoordinateY(String coordinateY) {
		this.coordinateY = coordinateY;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}

	public void setEnable3DFlag(String enable3dFlag) {
		enable3DFlag = enable3dFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public void setMaxRowCount(Integer maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setPlotOrientation(String plotOrientation) {
		this.plotOrientation = plotOrientation;
	}

	public void setQueryIds(String queryIds) {
		this.queryIds = queryIds;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public JSONObject toJsonObject() {
		return ChartJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return ChartJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}