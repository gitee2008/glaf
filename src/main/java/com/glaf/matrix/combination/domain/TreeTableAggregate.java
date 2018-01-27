package com.glaf.matrix.combination.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;
import com.glaf.matrix.combination.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SYS_TREETABLE_AGGREGATE")
public class TreeTableAggregate implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected Long id;

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
	 * 类型
	 */
	@Column(name = "TYPE_", length = 20)
	protected String type;

	/**
	 * 来源表名
	 */
	@Column(name = "SOURCETABLENAME_", length = 50)
	protected String sourceTableName;

	/**
	 * 源树表Id字段
	 */
	@Column(name = "SOURCEIDCOLUMN_", length = 50)
	protected String sourceIdColumn;

	/**
	 * 源树表IndexId字段
	 */
	@Column(name = "SOURCEINDEXIDCOLUMN_", length = 50)
	protected String sourceIndexIdColumn;

	/**
	 * 源树表ParentId字段
	 */
	@Column(name = "SOURCEPARENTIDCOLUMN_", length = 50)
	protected String sourceParentIdColumn;

	/**
	 * 源树表TreeId字段
	 */
	@Column(name = "SOURCETREEIDCOLUMN_", length = 50)
	protected String sourceTreeIdColumn;

	/**
	 * 源树表文本字段
	 */
	@Column(name = "SOURCETEXTCOLUMN_", length = 50)
	protected String sourceTextColumn;

	/**
	 * 源树表WBS模板字段
	 */
	@Column(name = "SOURCEWBSINDEXCOLUMN_", length = 50)
	protected String sourceWbsIndexColumn;

	/**
	 * 源表日期分割字段
	 */
	@Column(name = "SOURCETABLEDATESPLITCOLUMN_", length = 50)
	protected String sourceTableDateSplitColumn;

	/**
	 * 来源表执行
	 */
	@Column(name = "SOURCETABLEEXECUTIONIDS_", length = 800)
	protected String sourceTableExecutionIds;

	/**
	 * 来源数据库编号
	 */
	@Column(name = "DATABASEIDS_", length = 2000)
	protected String databaseIds;

	/**
	 * 目标表名
	 */
	@Column(name = "TARGETTABLENAME_", length = 50)
	protected String targetTableName;

	/**
	 * 目标表执行
	 */
	@Column(name = "TARGETTABLEEXECUTIONIDS_", length = 800)
	protected String targetTableExecutionIds;

	/**
	 * 是否需要建表
	 */
	@Column(name = "CREATETABLEFLAG_", length = 1)
	protected String createTableFlag;

	/**
	 * 聚合标识
	 */
	@Column(name = "AGGREGATEFLAG_", length = 1)
	protected String aggregateFlag;

	/**
	 * 是否调度
	 */
	@Column(name = "SCHEDULEFLAG_", length = 1)
	protected String scheduleFlag;

	/**
	 * 按月生成数据
	 */
	@Column(name = "GENBYMONTH_", length = 1)
	protected String genByMonth;

	/**
	 * 每次抓取前删除
	 */
	@Column(name = "DELETEFETCH_", length = 1)
	protected String deleteFetch;

	/**
	 * 同步列
	 */
	@Column(name = "SYNCCOLUMNS_", length = 2000)
	protected String syncColumns;

	/**
	 * 同步状态
	 */
	@Column(name = "SYNCSTATUS_")
	protected Integer syncStatus;

	/**
	 * 最后同步时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SYNCTIME_")
	protected Date syncTime;

	/**
	 * 动态SQL
	 */
	@Column(name = "DYNAMICCONDITION_", length = 1)
	protected String dynamicCondition;

	/**
	 * SQL条件
	 */
	@Column(name = "SQLCRITERIA_", length = 4000)
	protected String sqlCriteria;

	/**
	 * 开始年份
	 */
	@Column(name = "STARTYEAR_")
	protected int startYear;

	/**
	 * 结束年份
	 */
	@Column(name = "STOPYEAR_")
	protected int stopYear;

	@Column(name = "JOBNO_", length = 50)
	protected String jobNo;

	/**
	 * 顺序
	 */
	@Column(name = "SORTNO_")
	protected int sortNo;

	/**
	 * PrivateFlag
	 */
	@Column(name = "PRIVATEFLAG_")
	protected int privateFlag;

	/**
	 * DeleteFlag
	 */
	@Column(name = "DELETEFLAG_")
	protected int deleteFlag;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

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
	 * 修改人
	 */
	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	/**
	 * 修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

	@javax.persistence.Transient
	protected List<TreeTableAggregateRule> rules = new ArrayList<TreeTableAggregateRule>();

	public TreeTableAggregate() {

	}

	public void addRule(TreeTableAggregateRule rule) {
		if (rules == null) {
			rules = new ArrayList<TreeTableAggregateRule>();
		}
		rules.add(rule);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeTableAggregate other = (TreeTableAggregate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAggregateFlag() {
		return this.aggregateFlag;
	}

	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public String getCreateTableFlag() {
		return this.createTableFlag;
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

	public String getDatabaseIds() {
		return this.databaseIds;
	}

	public String getDeleteFetch() {
		return this.deleteFetch;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public String getDynamicCondition() {
		return dynamicCondition;
	}

	public String getGenByMonth() {
		return genByMonth;
	}

	public Long getId() {
		return this.id;
	}

	public String getJobNo() {
		return jobNo;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getName() {
		return this.name;
	}

	public int getPrivateFlag() {
		return privateFlag;
	}

	public List<TreeTableAggregateRule> getRules() {
		if (rules == null) {
			rules = new ArrayList<TreeTableAggregateRule>();
		}
		return rules;
	}

	public String getScheduleFlag() {
		return scheduleFlag;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public String getSourceIdColumn() {
		if (sourceIdColumn != null) {
			sourceIdColumn = sourceIdColumn.trim();
			sourceIdColumn = sourceIdColumn.toLowerCase();
		}
		return this.sourceIdColumn;
	}

	public String getSourceIndexIdColumn() {
		if (sourceIndexIdColumn != null) {
			sourceIndexIdColumn = sourceIndexIdColumn.trim();
			sourceIndexIdColumn = sourceIndexIdColumn.toLowerCase();
		}
		return sourceIndexIdColumn;
	}

	public String getSourceParentIdColumn() {
		if (sourceParentIdColumn != null) {
			sourceParentIdColumn = sourceParentIdColumn.trim();
			sourceParentIdColumn = sourceParentIdColumn.toLowerCase();
		}
		return this.sourceParentIdColumn;
	}

	public String getSourceTableDateSplitColumn() {
		if (sourceTableDateSplitColumn != null) {
			sourceTableDateSplitColumn = sourceTableDateSplitColumn.trim();
			sourceTableDateSplitColumn = sourceTableDateSplitColumn.toLowerCase();
		}
		return sourceTableDateSplitColumn;
	}

	public String getSourceTableExecutionIds() {
		return sourceTableExecutionIds;
	}

	public String getSourceTableName() {
		return this.sourceTableName;
	}

	public String getSourceTextColumn() {
		if (sourceTextColumn != null) {
			sourceTextColumn = sourceTextColumn.trim();
			sourceTextColumn = sourceTextColumn.toLowerCase();
		}
		return this.sourceTextColumn;
	}

	public String getSourceTreeIdColumn() {
		if (sourceTreeIdColumn != null) {
			sourceTreeIdColumn = sourceTreeIdColumn.trim();
			sourceTreeIdColumn = sourceTreeIdColumn.toLowerCase();
		}
		return this.sourceTreeIdColumn;
	}

	public String getSourceWbsIndexColumn() {
		if (sourceWbsIndexColumn != null) {
			sourceWbsIndexColumn = sourceWbsIndexColumn.trim();
			sourceWbsIndexColumn = sourceWbsIndexColumn.toLowerCase();
		}
		return sourceWbsIndexColumn;
	}

	public String getSqlCriteria() {
		if (StringUtils.isNotEmpty(sqlCriteria)) {
			sqlCriteria = StringTools.replace(sqlCriteria, "‘", "'");
			sqlCriteria = StringTools.replace(sqlCriteria, "’", "'");
		}
		return sqlCriteria;
	}

	public int getStartYear() {
		Calendar c = java.util.Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		if (!(startYear > year - 5 && startYear < year + 5)) {
			startYear = year;
		}
		return startYear;
	}

	public int getStopYear() {
		Calendar c = java.util.Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		if (!(stopYear > year - 5 && stopYear < year + 5)) {
			stopYear = year;
		}
		return stopYear;
	}

	public String getSyncColumns() {
		if (syncColumns != null) {
			syncColumns = syncColumns.trim();
			syncColumns = syncColumns.toLowerCase();

			List<String> columns = StringTools.split(syncColumns);
			if (columns != null && !columns.isEmpty()) {
				if (StringUtils.isNotEmpty(sourceIdColumn)) {
					if (!columns.contains(sourceIdColumn.toLowerCase())) {
						syncColumns = sourceIdColumn + ", " + syncColumns;
					}
				}

				if (StringUtils.isNotEmpty(sourceIndexIdColumn)) {
					if (!columns.contains(sourceIndexIdColumn.toLowerCase())) {
						syncColumns = syncColumns + ", " + sourceIndexIdColumn;
					}
				}

				if (StringUtils.isNotEmpty(sourceParentIdColumn)) {
					if (!columns.contains(sourceParentIdColumn.toLowerCase())) {
						syncColumns = syncColumns + ", " + sourceParentIdColumn;
					}
				}

				if (StringUtils.isNotEmpty(sourceTreeIdColumn)) {
					if (!columns.contains(sourceTreeIdColumn.toLowerCase())) {
						syncColumns = syncColumns + ", " + sourceTreeIdColumn;
					}
				}

				if (StringUtils.isNotEmpty(sourceTextColumn)) {
					if (!columns.contains(sourceTextColumn.toLowerCase())) {
						syncColumns = syncColumns + ", " + sourceTextColumn;
					}
				}

				if (StringUtils.isNotEmpty(sourceWbsIndexColumn)) {
					if (!columns.contains(sourceWbsIndexColumn.toLowerCase())) {
						syncColumns = syncColumns + ", " + sourceWbsIndexColumn;
					}
				}
			}

		}
		return syncColumns;
	}

	public Integer getSyncStatus() {
		return this.syncStatus;
	}

	public Date getSyncTime() {
		return this.syncTime;
	}

	public String getSyncTimeString() {
		if (this.syncTime != null) {
			return DateUtils.getDateTime(this.syncTime);
		}
		return "";
	}

	public String getTargetTableExecutionIds() {
		return targetTableExecutionIds;
	}

	public String getTargetTableName() {
		return this.targetTableName;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String getUpdateTimeString() {
		if (this.updateTime != null) {
			return DateUtils.getDateTime(this.updateTime);
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public TreeTableAggregate jsonToObject(JSONObject jsonObject) {
		return TreeTableAggregateJsonFactory.jsonToObject(jsonObject);
	}

	public void setAggregateFlag(String aggregateFlag) {
		this.aggregateFlag = aggregateFlag;
	}

	public void setColumns(List<ColumnDefinition> columns) {
		this.columns = columns;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTableFlag(String createTableFlag) {
		this.createTableFlag = createTableFlag;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDatabaseIds(String databaseIds) {
		this.databaseIds = databaseIds;
	}

	public void setDeleteFetch(String deleteFetch) {
		this.deleteFetch = deleteFetch;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setDynamicCondition(String dynamicCondition) {
		this.dynamicCondition = dynamicCondition;
	}

	public void setGenByMonth(String genByMonth) {
		this.genByMonth = genByMonth;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrivateFlag(int privateFlag) {
		this.privateFlag = privateFlag;
	}

	public void setRules(List<TreeTableAggregateRule> rules) {
		this.rules = rules;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setSourceIdColumn(String sourceIdColumn) {
		this.sourceIdColumn = sourceIdColumn;
	}

	public void setSourceIndexIdColumn(String sourceIndexIdColumn) {
		this.sourceIndexIdColumn = sourceIndexIdColumn;
	}

	public void setSourceParentIdColumn(String sourceParentIdColumn) {
		this.sourceParentIdColumn = sourceParentIdColumn;
	}

	public void setSourceTableDateSplitColumn(String sourceTableDateSplitColumn) {
		this.sourceTableDateSplitColumn = sourceTableDateSplitColumn;
	}

	public void setSourceTableExecutionIds(String sourceTableExecutionIds) {
		this.sourceTableExecutionIds = sourceTableExecutionIds;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public void setSourceTextColumn(String sourceTextColumn) {
		this.sourceTextColumn = sourceTextColumn;
	}

	public void setSourceTreeIdColumn(String sourceTreeIdColumn) {
		this.sourceTreeIdColumn = sourceTreeIdColumn;
	}

	public void setSourceWbsIndexColumn(String sourceWbsIndexColumn) {
		this.sourceWbsIndexColumn = sourceWbsIndexColumn;
	}

	public void setSqlCriteria(String sqlCriteria) {
		this.sqlCriteria = sqlCriteria;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public void setStopYear(int stopYear) {
		this.stopYear = stopYear;
	}

	public void setSyncColumns(String syncColumns) {
		this.syncColumns = syncColumns;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public void setTargetTableExecutionIds(String targetTableExecutionIds) {
		this.targetTableExecutionIds = targetTableExecutionIds;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public JSONObject toJsonObject() {
		return TreeTableAggregateJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return TreeTableAggregateJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
