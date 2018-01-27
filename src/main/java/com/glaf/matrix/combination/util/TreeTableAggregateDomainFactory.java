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

package com.glaf.matrix.combination.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.base.DataRequest;
import com.glaf.core.base.DataRequest.FilterDescriptor;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class TreeTableAggregateDomainFactory {

	public static final String TABLENAME = "SYS_TREETABLE_AGGREGATE";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("name", "NAME_");
		columnMap.put("title", "TITLE_");
		columnMap.put("type", "TYPE_");
		columnMap.put("sourceTableName", "SOURCETABLENAME_");
		columnMap.put("sourceIdColumn", "SOURCEIDCOLUMN_");
		columnMap.put("sourceIndexIdColumn", "SOURCEINDEXIDCOLUMN_");
		columnMap.put("sourceParentIdColumn", "SOURCEPARENTIDCOLUMN_");
		columnMap.put("sourceTreeIdColumn", "SOURCETREEIDCOLUMN_");
		columnMap.put("sourceTextColumn", "SOURCETEXTCOLUMN_");
		columnMap.put("sourceWbsIndexColumn", "SOURCEWBSINDEXCOLUMN_");
		columnMap.put("sourceTableDateSplitColumn", "SOURCETABLEDATESPLITCOLUMN_");
		columnMap.put("sourceTableExecutionIds", "SOURCETABLEEXECUTIONIDS_");
		columnMap.put("databaseIds", "DATABASEIDS_");
		columnMap.put("targetTableName", "TARGETTABLENAME_");
		columnMap.put("targetDatabaseId", "TARGETDATABASEID_");
		columnMap.put("targetTableExecutionIds", "TARGETTABLEEXECUTIONIDS_");
		columnMap.put("createTableFlag", "CREATETABLEFLAG_");
		columnMap.put("aggregateFlag", "AGGREGATEFLAG_");
		columnMap.put("scheduleFlag", "SCHEDULEFLAG_");
		columnMap.put("genByMonth", "GENBYMONTH_");
		columnMap.put("privateFlag", "PRIVATEFLAG_");
		columnMap.put("deleteFetch", "DELETEFETCH_");
		columnMap.put("jobNo", "JOBNO_");
		columnMap.put("syncColumns", "SYNCCOLUMNS_");
		columnMap.put("syncStatus", "SYNCSTATUS_");
		columnMap.put("syncTime", "SYNCTIME_");
		columnMap.put("dynamicCondition", "DYNAMICCONDITION_");
		columnMap.put("sqlCriteria", "SQLCRITERIA_");
		columnMap.put("startYear", "STARTYEAR_");
		columnMap.put("stopYear", "STOPYEAR_");
		columnMap.put("sortNo", "SORTNO_");
		columnMap.put("locked", "LOCKED_");
		columnMap.put("deleteFlag", "DELETEFLAG_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("title", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("sourceTableName", "String");
		javaTypeMap.put("sourceIdColumn", "String");
		javaTypeMap.put("sourceIndexIdColumn", "String");
		javaTypeMap.put("sourceParentIdColumn", "String");
		javaTypeMap.put("sourceTreeIdColumn", "String");
		javaTypeMap.put("sourceTextColumn", "String");
		javaTypeMap.put("sourceWbsIndexColumn", "String");
		javaTypeMap.put("sourceTableDateSplitColumn", "String");
		javaTypeMap.put("sourceTableExecutionIds", "String");
		javaTypeMap.put("databaseIds", "String");
		javaTypeMap.put("targetTableName", "String");
		javaTypeMap.put("targetDatabaseId", "Long");
		javaTypeMap.put("targetTableExecutionIds", "String");
		javaTypeMap.put("createTableFlag", "String");
		javaTypeMap.put("aggregateFlag", "String");
		javaTypeMap.put("scheduleFlag", "String");
		javaTypeMap.put("genByMonth", "String");
		javaTypeMap.put("privateFlag", "Integer");
		javaTypeMap.put("deleteFetch", "String");
		javaTypeMap.put("jobNo", "String");
		javaTypeMap.put("syncColumns", "String");
		javaTypeMap.put("syncStatus", "Integer");
		javaTypeMap.put("syncTime", "Date");
		javaTypeMap.put("dynamicCondition", "String");
		javaTypeMap.put("sqlCriteria", "String");
		javaTypeMap.put("startYear", "Integer");
		javaTypeMap.put("stopYear", "Integer");
		javaTypeMap.put("sortNo", "Integer");
		javaTypeMap.put("locked", "Integer");
		javaTypeMap.put("deleteFlag", "Integer");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
	}

	public static Map<String, String> getColumnMap() {
		return columnMap;
	}

	public static Map<String, String> getJavaTypeMap() {
		return javaTypeMap;
	}

	public static TableDefinition getTableDefinition() {
		return getTableDefinition(TABLENAME);
	}

	public static TableDefinition getTableDefinition(String tableName) {
		tableName = tableName.toUpperCase();
		TableDefinition tableDefinition = new TableDefinition();
		tableDefinition.setTableName(tableName);
		tableDefinition.setName("TreeTableAggregate");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(50);
		tableDefinition.addColumn(name);

		ColumnDefinition title = new ColumnDefinition();
		title.setName("title");
		title.setColumnName("TITLE_");
		title.setJavaType("String");
		title.setLength(200);
		tableDefinition.addColumn(title);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(20);
		tableDefinition.addColumn(type);

		ColumnDefinition sourceTableName = new ColumnDefinition();
		sourceTableName.setName("sourceTableName");
		sourceTableName.setColumnName("SOURCETABLENAME_");
		sourceTableName.setJavaType("String");
		sourceTableName.setLength(50);
		tableDefinition.addColumn(sourceTableName);

		ColumnDefinition sourceIdColumn = new ColumnDefinition();
		sourceIdColumn.setName("sourceIdColumn");
		sourceIdColumn.setColumnName("SOURCEIDCOLUMN_");
		sourceIdColumn.setJavaType("String");
		sourceIdColumn.setLength(50);
		tableDefinition.addColumn(sourceIdColumn);

		ColumnDefinition sourceIndexIdColumn = new ColumnDefinition();
		sourceIndexIdColumn.setName("sourceIndexIdColumn");
		sourceIndexIdColumn.setColumnName("SOURCEINDEXIDCOLUMN_");
		sourceIndexIdColumn.setJavaType("String");
		sourceIndexIdColumn.setLength(50);
		tableDefinition.addColumn(sourceIndexIdColumn);

		ColumnDefinition sourceParentIdColumn = new ColumnDefinition();
		sourceParentIdColumn.setName("sourceParentIdColumn");
		sourceParentIdColumn.setColumnName("SOURCEPARENTIDCOLUMN_");
		sourceParentIdColumn.setJavaType("String");
		sourceParentIdColumn.setLength(50);
		tableDefinition.addColumn(sourceParentIdColumn);

		ColumnDefinition sourceTreeIdColumn = new ColumnDefinition();
		sourceTreeIdColumn.setName("sourceTreeIdColumn");
		sourceTreeIdColumn.setColumnName("SOURCETREEIDCOLUMN_");
		sourceTreeIdColumn.setJavaType("String");
		sourceTreeIdColumn.setLength(50);
		tableDefinition.addColumn(sourceTreeIdColumn);

		ColumnDefinition sourceTextColumn = new ColumnDefinition();
		sourceTextColumn.setName("sourceTextColumn");
		sourceTextColumn.setColumnName("SOURCETEXTCOLUMN_");
		sourceTextColumn.setJavaType("String");
		sourceTextColumn.setLength(50);
		tableDefinition.addColumn(sourceTextColumn);

		ColumnDefinition sourceWbsIndexColumn = new ColumnDefinition();
		sourceWbsIndexColumn.setName("sourceWbsIndexColumn");
		sourceWbsIndexColumn.setColumnName("SOURCEWBSINDEXCOLUMN_");
		sourceWbsIndexColumn.setJavaType("String");
		sourceWbsIndexColumn.setLength(50);
		tableDefinition.addColumn(sourceWbsIndexColumn);

		ColumnDefinition sourceTableDateSplitColumn = new ColumnDefinition();
		sourceTableDateSplitColumn.setName("sourceTableDateSplitColumn");
		sourceTableDateSplitColumn.setColumnName("SOURCETABLEDATESPLITCOLUMN_");
		sourceTableDateSplitColumn.setJavaType("String");
		sourceTableDateSplitColumn.setLength(50);
		tableDefinition.addColumn(sourceTableDateSplitColumn);

		ColumnDefinition sourceTableExecutionIds = new ColumnDefinition();
		sourceTableExecutionIds.setName("sourceTableExecutionIds");
		sourceTableExecutionIds.setColumnName("SOURCETABLEEXECUTIONIDS_");
		sourceTableExecutionIds.setJavaType("String");
		sourceTableExecutionIds.setLength(800);
		tableDefinition.addColumn(sourceTableExecutionIds);

		ColumnDefinition databaseIds = new ColumnDefinition();
		databaseIds.setName("databaseIds");
		databaseIds.setColumnName("DATABASEIDS_");
		databaseIds.setJavaType("String");
		databaseIds.setLength(2000);
		tableDefinition.addColumn(databaseIds);

		ColumnDefinition targetTableName = new ColumnDefinition();
		targetTableName.setName("targetTableName");
		targetTableName.setColumnName("TARGETTABLENAME_");
		targetTableName.setJavaType("String");
		targetTableName.setLength(50);
		tableDefinition.addColumn(targetTableName);

		ColumnDefinition targetDatabaseId = new ColumnDefinition();
		targetDatabaseId.setName("targetDatabaseId");
		targetDatabaseId.setColumnName("TARGETDATABASEID_");
		targetDatabaseId.setJavaType("Long");
		tableDefinition.addColumn(targetDatabaseId);

		ColumnDefinition targetTableExecutionIds = new ColumnDefinition();
		targetTableExecutionIds.setName("targetTableExecutionIds");
		targetTableExecutionIds.setColumnName("TARGETTABLEEXECUTIONIDS_");
		targetTableExecutionIds.setJavaType("String");
		targetTableExecutionIds.setLength(800);
		tableDefinition.addColumn(targetTableExecutionIds);

		ColumnDefinition createTableFlag = new ColumnDefinition();
		createTableFlag.setName("createTableFlag");
		createTableFlag.setColumnName("CREATETABLEFLAG_");
		createTableFlag.setJavaType("String");
		createTableFlag.setLength(1);
		tableDefinition.addColumn(createTableFlag);

		ColumnDefinition aggregateFlag = new ColumnDefinition();
		aggregateFlag.setName("aggregateFlag");
		aggregateFlag.setColumnName("AGGREGATEFLAG_");
		aggregateFlag.setJavaType("String");
		aggregateFlag.setLength(1);
		tableDefinition.addColumn(aggregateFlag);

		ColumnDefinition scheduleFlag = new ColumnDefinition();
		scheduleFlag.setName("scheduleFlag");
		scheduleFlag.setColumnName("SCHEDULEFLAG_");
		scheduleFlag.setJavaType("String");
		scheduleFlag.setLength(1);
		tableDefinition.addColumn(scheduleFlag);

		ColumnDefinition genByMonth = new ColumnDefinition();
		genByMonth.setName("genByMonth");
		genByMonth.setColumnName("GENBYMONTH_");
		genByMonth.setJavaType("String");
		genByMonth.setLength(1);
		tableDefinition.addColumn(genByMonth);

		ColumnDefinition privateFlag = new ColumnDefinition();
		privateFlag.setName("privateFlag");
		privateFlag.setColumnName("PRIVATEFLAG_");
		privateFlag.setJavaType("Integer");
		tableDefinition.addColumn(privateFlag);

		ColumnDefinition deleteFetch = new ColumnDefinition();
		deleteFetch.setName("deleteFetch");
		deleteFetch.setColumnName("DELETEFETCH_");
		deleteFetch.setJavaType("String");
		deleteFetch.setLength(1);
		tableDefinition.addColumn(deleteFetch);

		ColumnDefinition jobNo = new ColumnDefinition();
		jobNo.setName("jobNo");
		jobNo.setColumnName("JOBNO_");
		jobNo.setJavaType("String");
		jobNo.setLength(50);
		tableDefinition.addColumn(jobNo);

		ColumnDefinition syncColumns = new ColumnDefinition();
		syncColumns.setName("syncColumns");
		syncColumns.setColumnName("SYNCCOLUMNS_");
		syncColumns.setJavaType("String");
		syncColumns.setLength(2000);
		tableDefinition.addColumn(syncColumns);

		ColumnDefinition syncStatus = new ColumnDefinition();
		syncStatus.setName("syncStatus");
		syncStatus.setColumnName("SYNCSTATUS_");
		syncStatus.setJavaType("Integer");
		tableDefinition.addColumn(syncStatus);

		ColumnDefinition syncTime = new ColumnDefinition();
		syncTime.setName("syncTime");
		syncTime.setColumnName("SYNCTIME_");
		syncTime.setJavaType("Date");
		tableDefinition.addColumn(syncTime);

		ColumnDefinition sqlCriteria = new ColumnDefinition();
		sqlCriteria.setName("sqlCriteria");
		sqlCriteria.setColumnName("SQLCRITERIA_");
		sqlCriteria.setJavaType("String");
		sqlCriteria.setLength(4000);
		tableDefinition.addColumn(sqlCriteria);

		ColumnDefinition dynamicCondition = new ColumnDefinition();
		dynamicCondition.setName("dynamicCondition");
		dynamicCondition.setColumnName("DYNAMICCONDITION_");
		dynamicCondition.setJavaType("String");
		dynamicCondition.setLength(1);
		tableDefinition.addColumn(dynamicCondition);

		ColumnDefinition startYear = new ColumnDefinition();
		startYear.setName("startYear");
		startYear.setColumnName("STARTYEAR_");
		startYear.setJavaType("Integer");
		tableDefinition.addColumn(startYear);

		ColumnDefinition stopYear = new ColumnDefinition();
		stopYear.setName("stopYear");
		stopYear.setColumnName("STOPYEAR_");
		stopYear.setJavaType("Integer");
		tableDefinition.addColumn(stopYear);

		ColumnDefinition sortNo = new ColumnDefinition();
		sortNo.setName("sortNo");
		sortNo.setColumnName("SORTNO_");
		sortNo.setJavaType("Integer");
		tableDefinition.addColumn(sortNo);

		ColumnDefinition locked = new ColumnDefinition();
		locked.setName("locked");
		locked.setColumnName("LOCKED_");
		locked.setJavaType("Integer");
		tableDefinition.addColumn(locked);

		ColumnDefinition deleteFlag = new ColumnDefinition();
		deleteFlag.setName("deleteFlag");
		deleteFlag.setColumnName("DELETEFLAG_");
		deleteFlag.setJavaType("Integer");
		tableDefinition.addColumn(deleteFlag);

		ColumnDefinition createBy = new ColumnDefinition();
		createBy.setName("createBy");
		createBy.setColumnName("CREATEBY_");
		createBy.setJavaType("String");
		createBy.setLength(50);
		tableDefinition.addColumn(createBy);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		ColumnDefinition updateBy = new ColumnDefinition();
		updateBy.setName("updateBy");
		updateBy.setColumnName("UPDATEBY_");
		updateBy.setJavaType("String");
		updateBy.setLength(50);
		tableDefinition.addColumn(updateBy);

		ColumnDefinition updateTime = new ColumnDefinition();
		updateTime.setName("updateTime");
		updateTime.setColumnName("UPDATETIME_");
		updateTime.setJavaType("Date");
		tableDefinition.addColumn(updateTime);

		return tableDefinition;
	}

	public static TableDefinition createTable() {
		TableDefinition tableDefinition = getTableDefinition(TABLENAME);
		if (!DBUtils.tableExists(TABLENAME)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static TableDefinition createTable(String tableName) {
		TableDefinition tableDefinition = getTableDefinition(tableName);
		if (!DBUtils.tableExists(tableName)) {
			DBUtils.createTable(tableDefinition);
		} else {
			DBUtils.alterTable(tableDefinition);
		}
		return tableDefinition;
	}

	public static void processDataRequest(DataRequest dataRequest) {
		if (dataRequest != null) {
			if (dataRequest.getFilter() != null) {
				if (dataRequest.getFilter().getField() != null) {
					dataRequest.getFilter().setColumn(columnMap.get(dataRequest.getFilter().getField()));
					dataRequest.getFilter().setJavaType(javaTypeMap.get(dataRequest.getFilter().getField()));
				}

				List<FilterDescriptor> filters = dataRequest.getFilter().getFilters();
				for (FilterDescriptor filter : filters) {
					filter.setParent(dataRequest.getFilter());
					if (filter.getField() != null) {
						filter.setColumn(columnMap.get(filter.getField()));
						filter.setJavaType(javaTypeMap.get(filter.getField()));
					}

					List<FilterDescriptor> subFilters = filter.getFilters();
					for (FilterDescriptor f : subFilters) {
						f.setColumn(columnMap.get(f.getField()));
						f.setJavaType(javaTypeMap.get(f.getField()));
						f.setParent(filter);
					}
				}
			}
		}
	}

	private TreeTableAggregateDomainFactory() {

	}

}
