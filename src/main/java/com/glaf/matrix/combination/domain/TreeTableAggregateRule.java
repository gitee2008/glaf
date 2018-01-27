package com.glaf.matrix.combination.domain;

import java.io.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.matrix.combination.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_TREETABLE_AGGREGATE_RULE")
public class TreeTableAggregateRule implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 树表聚合编号
	 */
	@Column(name = "AGGREGATEID_")
	protected long aggregateId;

	/**
	 * 聚合类型
	 */
	@Column(name = "AGGREGATETYPE_", length = 50)
	protected String aggregateType;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 50)
	protected String name;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 200)
	protected String title;

	/**
	 * 源字段名
	 */
	@Column(name = "SOURCECOLUMNNAME_", length = 50)
	protected String sourceColumnName;

	/**
	 * 源字段中文名称
	 */
	@Column(name = "SOURCECOLUMNTITLE_", length = 50)
	protected String sourceColumnTitle;

	/**
	 * 目标字段名
	 */
	@Column(name = "TARGETCOLUMNNAME_", length = 50)
	protected String targetColumnName;

	/**
	 * 目标字段中文名称
	 */
	@Column(name = "TARGETCOLUMNTITLE_", length = 50)
	protected String targetColumnTitle;

	/**
	 * 目标字段类型
	 */
	@Column(name = "TARGETCOLUMNTYPE_", length = 50)
	protected String targetColumnType;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

	public TreeTableAggregateRule() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeTableAggregateRule other = (TreeTableAggregateRule) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getAggregateId() {
		return this.aggregateId;
	}

	public String getAggregateType() {
		return this.aggregateType;
	}

	public long getId() {
		return this.id;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getName() {
		return this.name;
	}

	public String getSourceColumnName() {
		return this.sourceColumnName;
	}

	public String getSourceColumnTitle() {
		return this.sourceColumnTitle;
	}

	public String getTargetColumnName() {
		return this.targetColumnName;
	}

	public String getTargetColumnTitle() {
		return this.targetColumnTitle;
	}

	public String getTargetColumnType() {
		return this.targetColumnType;
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public TreeTableAggregateRule jsonToObject(JSONObject jsonObject) {
		return TreeTableAggregateRuleJsonFactory.jsonToObject(jsonObject);
	}

	public void setAggregateId(long aggregateId) {
		this.aggregateId = aggregateId;
	}

	public void setAggregateType(String aggregateType) {
		this.aggregateType = aggregateType;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	public void setSourceColumnTitle(String sourceColumnTitle) {
		this.sourceColumnTitle = sourceColumnTitle;
	}

	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}

	public void setTargetColumnTitle(String targetColumnTitle) {
		this.targetColumnTitle = targetColumnTitle;
	}

	public void setTargetColumnType(String targetColumnType) {
		this.targetColumnType = targetColumnType;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JSONObject toJsonObject() {
		return TreeTableAggregateRuleJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TreeTableAggregateRuleJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
