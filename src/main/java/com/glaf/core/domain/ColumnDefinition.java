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

package com.glaf.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.BaseItem;
import com.glaf.core.domain.util.ColumnDefinitionJsonFactory;

/**
 * 数据字段定义
 * 
 */
public class ColumnDefinition implements java.lang.Comparable<ColumnDefinition>, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	protected String id;

	/**
	 * 数据库中表的别名
	 */
	protected String alias;

	protected String align;

	/**
	 * 数据库字段标签
	 */
	protected String columnLabel;

	/**
	 * 数据库字段名称
	 */
	protected String columnName;

	protected String dataCode;

	protected int index;

	/**
	 * 数据库类型
	 */
	protected int dataType;

	/**
	 * 默认值
	 */
	protected String defaultValue;

	/**
	 * 鉴别类型,C-数据库字段，P-查询参数
	 */
	protected String discriminator;

	/**
	 * 显示类型 0-不显示，1-表单，2-表单及列表
	 */
	protected int displayType;

	protected String editableField;

	/**
	 * ENGLISH标题
	 */
	protected String englishTitle;

	/**
	 * 显示格式
	 */
	protected String formatter;

	/**
	 * 公式
	 */
	protected String formula;

	/**
	 * 是否冻结列
	 */
	protected String frozenField;

	protected String height;

	/**
	 * 是否隐藏列
	 */
	protected String hiddenField;

	/**
	 * 输入类型（文本框、数值输入、日期输入、下拉列表、复选框）
	 */
	protected String inputType;

	/**
	 * 参数是否为集合类型
	 */
	protected String isCollectionField;

	/**
	 * Java类型
	 */
	protected String javaType;

	/**
	 * 字段长度
	 */
	protected int length;

	protected String link;

	protected String mask;

	/**
	 * 属性名称
	 */
	protected String name;

	/**
	 * 是否为空
	 */
	protected String nullableField = "1";

	/**
	 * 字段顺序号
	 */
	protected int ordinal;

	/**
	 * 字段精度
	 */
	protected int precision;

	/**
	 * 是否主键
	 */
	protected String primaryKeyField;

	/**
	 * 查询编号
	 */
	protected String queryId;

	/**
	 * 正则表达式
	 */
	protected String regex;

	protected String renderer;

	protected String renderType;

	/**
	 * 是否必填
	 */
	protected String requiredField = "0";

	/**
	 * 是否可调整列宽
	 */
	protected String resizableField;

	/**
	 * 小数位数
	 */
	protected int scale;

	protected String searchableField;

	/**
	 * 是否可排序
	 */
	protected String sortableField;

	/**
	 * 排序类型 int-整形、number-数值、date-日期时间
	 */
	protected String sortType;

	/**
	 * 汇总表达式
	 */
	protected String summaryExpr;

	/**
	 * 汇总类型，sum-求和、count-算个数
	 */
	protected String summaryType;

	protected String systemFlag;

	protected String tableId;

	/**
	 * 表名
	 */
	protected String tableName;

	/**
	 * 目标ID
	 */
	protected String targetId;

	/**
	 * 下拉列表的文本字段
	 */
	protected String textField;

	/**
	 * 标题
	 */
	protected String title;

	/**
	 * 在列头部显示的提示文字
	 */
	protected String tooltip;

	protected String uniqueField;

	protected String updatableField;

	/**
	 * 下拉列表的取数URL
	 */
	protected String url;

	/**
	 * 验证类型
	 */
	protected String validType;

	protected Object value;

	protected String placeholder;

	protected String valueExpression;

	/**
	 * 下拉列表的值字段
	 */
	protected String valueField;

	protected String width;

	protected Integer position;

	protected List<BaseItem> items = null;

	protected Map<String, String> properties = new java.util.HashMap<String, String>();

	public ColumnDefinition() {

	}

	public void addItem(BaseItem item) {
		if (items == null) {
			items = new ArrayList<BaseItem>();
		}
		items.add(item);
	}

	public void addProperty(String key, String value) {
		if (properties == null) {
			properties = new java.util.HashMap<String, String>();
		}
		properties.put(key, value);
	}

	public int compareTo(ColumnDefinition o) {
		if (o == null) {
			return -1;
		}

		ColumnDefinition field = o;

		int l = this.ordinal - field.getSortNo();

		int ret = 0;

		if (l > 0) {
			ret = 1;
		} else if (l < 0) {
			ret = -1;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnDefinition other = (ColumnDefinition) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!getColumnName().equals(other.getColumnName()))
			return false;
		return true;
	}

	public String getAlias() {
		return alias;
	}

	public String getAlign() {
		return align;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public String getColumnName() {
		if (columnName != null) {
			columnName = columnName.trim().toUpperCase();
		}
		return columnName;
	}

	public String getDataCode() {
		return dataCode;
	}

	public int getDataType() {
		return dataType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public int getDisplayType() {
		return displayType;
	}

	public String getEditableField() {
		return editableField;
	}

	public String getEnglishTitle() {
		return englishTitle;
	}

	public String getFirstLowerName() {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	public String getFirstUpperName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String getFormatter() {
		return formatter;
	}

	public String getFormula() {
		return formula;
	}

	public String getFrozenField() {
		return frozenField;
	}

	public String getHeight() {
		return height;
	}

	public String getHiddenField() {
		return hiddenField;
	}

	public String getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public String getInputType() {
		return inputType;
	}

	public String getIsCollection() {
		return isCollectionField;
	}

	public String getIsCollectionField() {
		return isCollectionField;
	}

	public List<BaseItem> getItems() {
		return items;
	}

	public String getJavaType() {
		return javaType;
	}

	public int getLength() {
		return length;
	}

	public String getLink() {
		return link;
	}

	public String getLowerCaseType() {
		return javaType.toLowerCase();
	}

	public String getLowerColumnName() {
		if (columnName != null) {
			columnName = columnName.trim().toLowerCase();
		}
		return columnName;
	}

	public String getMask() {
		return mask;
	}

	public int getMaxLength() {
		return length;
	}

	public int getMinLength() {
		return 0;
	}

	public String getName() {
		return name;
	}

	public String getNullableField() {
		return nullableField;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public Integer getPosition() {
		return position;
	}

	public int getPrecision() {
		return precision;
	}

	public String getPrimaryKeyField() {
		return primaryKeyField;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String getQueryId() {
		return queryId;
	}

	public String getRegex() {
		return regex;
	}

	public String getRenderer() {
		return renderer;
	}

	public String getRenderType() {
		return renderType;
	}

	public String getRequiredField() {
		return requiredField;
	}

	public String getResizableField() {
		return resizableField;
	}

	public int getScale() {
		return scale;
	}

	public String getSearchableField() {
		return searchableField;
	}

	public String getSortableField() {
		return sortableField;
	}

	public int getSortNo() {
		return this.ordinal;
	}

	public String getSortType() {
		return sortType;
	}

	public String getSummaryExpr() {
		return summaryExpr;
	}

	public String getSummaryType() {
		return summaryType;
	}

	public String getSystemFlag() {
		return systemFlag;
	}

	public String getTableId() {
		return tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTargetId() {
		return targetId;
	}

	public String getTextField() {
		return textField;
	}

	public String getTitle() {
		return title;
	}

	public String getTooltip() {
		return tooltip;
	}

	public String getType() {
		return javaType;
	}

	public String getUniqueField() {
		return uniqueField;
	}

	public String getUpdatableField() {
		return updatableField;
	}

	public String getUrl() {
		return url;
	}

	public String getValidType() {
		return validType;
	}

	public Object getValue() {
		return value;
	}

	public String getValueExpression() {
		return valueExpression;
	}

	public String getValueField() {
		return valueField;
	}

	public String getWidth() {
		return width;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getColumnName() == null) ? 0 : getColumnName().hashCode());
		return result;
	}

	public boolean isCollection() {
		if (StringUtils.equalsIgnoreCase(isCollectionField, "1") || StringUtils.equalsIgnoreCase(isCollectionField, "Y")
				|| StringUtils.equalsIgnoreCase(isCollectionField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isEditable() {
		if (StringUtils.equalsIgnoreCase(editableField, "1") || StringUtils.equalsIgnoreCase(editableField, "Y")
				|| StringUtils.equalsIgnoreCase(editableField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isFrozen() {
		if (StringUtils.equalsIgnoreCase(frozenField, "1") || StringUtils.equalsIgnoreCase(frozenField, "Y")
				|| StringUtils.equalsIgnoreCase(frozenField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isHidden() {
		if (StringUtils.equalsIgnoreCase(hiddenField, "1") || StringUtils.equalsIgnoreCase(hiddenField, "Y")
				|| StringUtils.equalsIgnoreCase(hiddenField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isNullable() {
		if (StringUtils.equalsIgnoreCase(nullableField, "1") || StringUtils.equalsIgnoreCase(nullableField, "Y")
				|| StringUtils.equalsIgnoreCase(nullableField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isPrimaryKey() {
		if (StringUtils.equalsIgnoreCase(primaryKeyField, "1") || StringUtils.equalsIgnoreCase(primaryKeyField, "Y")
				|| StringUtils.equalsIgnoreCase(primaryKeyField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isRequired() {
		if (StringUtils.equalsIgnoreCase(requiredField, "1") || StringUtils.equalsIgnoreCase(requiredField, "Y")
				|| StringUtils.equalsIgnoreCase(requiredField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isResizable() {
		if (StringUtils.equalsIgnoreCase(resizableField, "1") || StringUtils.equalsIgnoreCase(resizableField, "Y")
				|| StringUtils.equalsIgnoreCase(resizableField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isSearchable() {
		if (StringUtils.equalsIgnoreCase(searchableField, "1") || StringUtils.equalsIgnoreCase(searchableField, "Y")
				|| StringUtils.equalsIgnoreCase(searchableField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isSortable() {
		if (StringUtils.equalsIgnoreCase(sortableField, "1") || StringUtils.equalsIgnoreCase(sortableField, "Y")
				|| StringUtils.equalsIgnoreCase(sortableField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isUnique() {
		if (StringUtils.equalsIgnoreCase(uniqueField, "1") || StringUtils.equalsIgnoreCase(uniqueField, "Y")
				|| StringUtils.equalsIgnoreCase(uniqueField, "true")) {
			return true;
		}
		return false;
	}

	public boolean isUpdatable() {
		if (StringUtils.equalsIgnoreCase(updatableField, "1") || StringUtils.equalsIgnoreCase(updatableField, "Y")
				|| StringUtils.equalsIgnoreCase(updatableField, "true")) {
			return true;
		}
		return false;
	}

	public ColumnDefinition jsonToObject(JSONObject jsonObject) {
		return ColumnDefinitionJsonFactory.jsonToObject(jsonObject);
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setCollection(boolean isCollection) {
		if (isCollection) {
			this.isCollectionField = "1";
		} else {
			this.isCollectionField = "0";
		}
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public void setEditable(boolean editable) {
		if (editable) {
			this.editableField = "1";
		} else {
			this.editableField = "0";
		}
	}

	public void setEditableField(String editableField) {
		this.editableField = editableField;
	}

	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public void setFrozen(boolean frozen) {
		if (frozen) {
			this.frozenField = "1";
		} else {
			this.frozenField = "0";
		}
	}

	public void setFrozenField(String frozenField) {
		this.frozenField = frozenField;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setHidden(boolean hidden) {
		if (hidden) {
			this.hiddenField = "1";
		} else {
			this.hiddenField = "0";
		}
	}

	public void setHiddenField(String hiddenField) {
		this.hiddenField = hiddenField;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public void setIsCollectionField(String isCollectionField) {
		this.isCollectionField = isCollectionField;
	}

	public void setItems(List<BaseItem> items) {
		this.items = items;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public void setMaxLength(int maxLength) {
		this.length = maxLength;
	}

	public void setMinLength(int minLength) {

	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNullable(boolean nullable) {
		if (nullable) {
			this.nullableField = "1";
		} else {
			this.nullableField = "0";
		}
	}

	public void setNullableField(String nullableField) {
		this.nullableField = nullableField;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void setPrimaryKey(boolean primaryKey) {
		if (primaryKey) {
			this.primaryKeyField = "1";
		} else {
			this.primaryKeyField = "0";
		}
	}

	public void setPrimaryKeyField(String primaryKeyField) {
		this.primaryKeyField = primaryKeyField;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public void setRenderType(String renderType) {
		this.renderType = renderType;
	}

	public void setRequired(boolean required) {
		if (required) {
			this.requiredField = "1";
		} else {
			this.requiredField = "0";
		}
	}

	public void setRequiredField(String requiredField) {
		this.requiredField = requiredField;
	}

	public void setResizable(boolean resizable) {
		if (resizable) {
			this.resizableField = "1";
		} else {
			this.resizableField = "0";
		}
	}

	public void setResizableField(String resizableField) {
		this.resizableField = resizableField;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public void setSearchable(boolean searchable) {
		if (searchable) {
			this.searchableField = "1";
		} else {
			this.searchableField = "0";
		}
	}

	public void setSearchableField(String searchableField) {
		this.searchableField = searchableField;
	}

	public void setSortable(boolean sortable) {
		if (sortable) {
			this.sortableField = "1";
		} else {
			this.sortableField = "0";
		}
	}

	public void setSortableField(String sortableField) {
		this.sortableField = sortableField;
	}

	public void setSortNo(int sortNo) {
		this.ordinal = sortNo;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public void setSummaryExpr(String summaryExpr) {
		this.summaryExpr = summaryExpr;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public void setType(String javaType) {
		this.javaType = javaType;
	}

	public void setUnique(boolean unique) {
		if (unique) {
			this.uniqueField = "1";
		} else {
			this.uniqueField = "0";
		}
	}

	public void setUniqueField(String uniqueField) {
		this.uniqueField = uniqueField;
	}

	public void setUpdatable(boolean updatable) {
		if (updatable) {
			this.updatableField = "1";
		} else {
			this.updatableField = "0";
		}
	}

	public void setUpdatableField(String updatableField) {
		this.updatableField = updatableField;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setValidType(String validType) {
		this.validType = validType;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setValueExpression(String valueExpression) {
		this.valueExpression = valueExpression;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public JSONObject toJsonObject() {
		return ColumnDefinitionJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return ColumnDefinitionJsonFactory.toObjectNode(this);
	}

	public String toString() {
		return toJsonObject().toJSONString();
	}

}