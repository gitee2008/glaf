package com.glaf.heathcare.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;

/**
 * 
 * 实体数据工厂类
 *
 */
public class GrowthRateCountDomainFactory {

	public static final String TABLENAME = "HEALTH_GROWTH_RATE_COUNT";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("checkId", "CHECKID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("type", "TYPE_");
		columnMap.put("increase", "INCREASE_");
		columnMap.put("increasePercent", "INCREASEPERCENT_");
		columnMap.put("standard", "STANDARD_");
		columnMap.put("standardPercent", "STANDARDPERCENT_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("checkId", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("increase", "Integer");
		javaTypeMap.put("increasePercent", "Double");
		javaTypeMap.put("standard", "Integer");
		javaTypeMap.put("standardPercent", "Double");
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
		tableDefinition.setName("GrowthRateCount");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition checkId = new ColumnDefinition();
		checkId.setName("checkId");
		checkId.setColumnName("CHECKID_");
		checkId.setJavaType("String");
		checkId.setLength(50);
		tableDefinition.addColumn(checkId);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition gradeId = new ColumnDefinition();
		gradeId.setName("gradeId");
		gradeId.setColumnName("GRADEID_");
		gradeId.setJavaType("String");
		gradeId.setLength(50);
		tableDefinition.addColumn(gradeId);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition increase = new ColumnDefinition();
		increase.setName("increase");
		increase.setColumnName("INCREASE_");
		increase.setJavaType("Integer");
		tableDefinition.addColumn(increase);

		ColumnDefinition increasePercent = new ColumnDefinition();
		increasePercent.setName("increasePercent");
		increasePercent.setColumnName("INCREASEPERCENT_");
		increasePercent.setJavaType("Double");
		tableDefinition.addColumn(increasePercent);

		ColumnDefinition standard = new ColumnDefinition();
		standard.setName("standard");
		standard.setColumnName("STANDARD_");
		standard.setJavaType("Integer");
		tableDefinition.addColumn(standard);

		ColumnDefinition standardPercent = new ColumnDefinition();
		standardPercent.setName("standardPercent");
		standardPercent.setColumnName("STANDARDPERCENT_");
		standardPercent.setJavaType("Double");
		tableDefinition.addColumn(standardPercent);

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

	private GrowthRateCountDomainFactory() {

	}

}
