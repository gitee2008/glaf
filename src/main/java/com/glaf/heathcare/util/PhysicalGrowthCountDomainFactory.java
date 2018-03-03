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
public class PhysicalGrowthCountDomainFactory {

	public static final String TABLENAME = "HEALTH_PHYSICAL_GROWTH_COUNT";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("checkId", "CHECKID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("type", "TYPE_");
		columnMap.put("level1", "LEVEL1_");
		columnMap.put("level2", "LEVEL2_");
		columnMap.put("level3", "LEVEL3_");
		columnMap.put("level5", "LEVEL5_");
		columnMap.put("level7", "LEVEL7_");
		columnMap.put("level8", "LEVEL8_");
		columnMap.put("level9", "LEVEL9_");
		columnMap.put("level1Percent", "LEVEL1PERCENT_");
		columnMap.put("level2Percent", "LEVEL2PERCENT_");
		columnMap.put("level3Percent", "LEVEL3PERCENT_");
		columnMap.put("level5Percent", "LEVEL5PERCENT_");
		columnMap.put("level7Percent", "LEVEL7PERCENT_");
		columnMap.put("level8Percent", "LEVEL8PERCENT_");
		columnMap.put("level9Percent", "LEVEL9PERCENT_");
		columnMap.put("normal", "NORMAL_");
		columnMap.put("normalPercent", "NORMALPERCENT_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("checkId", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("level1", "Integer");
		javaTypeMap.put("level2", "Integer");
		javaTypeMap.put("level3", "Integer");
		javaTypeMap.put("level5", "Integer");
		javaTypeMap.put("level7", "Integer");
		javaTypeMap.put("level8", "Integer");
		javaTypeMap.put("level9", "Integer");
		javaTypeMap.put("level1Percent", "Double");
		javaTypeMap.put("level2Percent", "Double");
		javaTypeMap.put("level3Percent", "Double");
		javaTypeMap.put("level5Percent", "Double");
		javaTypeMap.put("level7Percent", "Double");
		javaTypeMap.put("level8Percent", "Double");
		javaTypeMap.put("level9Percent", "Double");
		javaTypeMap.put("normal", "Integer");
		javaTypeMap.put("normalPercent", "Double");
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
		tableDefinition.setName("PhysicalGrowthCount");

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

		ColumnDefinition level1 = new ColumnDefinition();
		level1.setName("level1");
		level1.setColumnName("LEVEL1_");
		level1.setJavaType("Integer");
		tableDefinition.addColumn(level1);

		ColumnDefinition level2 = new ColumnDefinition();
		level2.setName("level2");
		level2.setColumnName("LEVEL2_");
		level2.setJavaType("Integer");
		tableDefinition.addColumn(level2);

		ColumnDefinition level3 = new ColumnDefinition();
		level3.setName("level3");
		level3.setColumnName("LEVEL3_");
		level3.setJavaType("Integer");
		tableDefinition.addColumn(level3);

		ColumnDefinition level5 = new ColumnDefinition();
		level5.setName("level5");
		level5.setColumnName("LEVEL5_");
		level5.setJavaType("Integer");
		tableDefinition.addColumn(level5);

		ColumnDefinition level7 = new ColumnDefinition();
		level7.setName("level7");
		level7.setColumnName("LEVEL7_");
		level7.setJavaType("Integer");
		tableDefinition.addColumn(level7);

		ColumnDefinition level8 = new ColumnDefinition();
		level8.setName("level8");
		level8.setColumnName("LEVEL8_");
		level8.setJavaType("Integer");
		tableDefinition.addColumn(level8);

		ColumnDefinition level9 = new ColumnDefinition();
		level9.setName("level9");
		level9.setColumnName("LEVEL9_");
		level9.setJavaType("Integer");
		tableDefinition.addColumn(level9);

		ColumnDefinition level1Percent = new ColumnDefinition();
		level1Percent.setName("level1Percent");
		level1Percent.setColumnName("LEVEL1PERCENT_");
		level1Percent.setJavaType("Double");
		tableDefinition.addColumn(level1Percent);

		ColumnDefinition level2Percent = new ColumnDefinition();
		level2Percent.setName("level2Percent");
		level2Percent.setColumnName("LEVEL2PERCENT_");
		level2Percent.setJavaType("Double");
		tableDefinition.addColumn(level2Percent);

		ColumnDefinition level3Percent = new ColumnDefinition();
		level3Percent.setName("level3Percent");
		level3Percent.setColumnName("LEVEL3PERCENT_");
		level3Percent.setJavaType("Double");
		tableDefinition.addColumn(level3Percent);

		ColumnDefinition level5Percent = new ColumnDefinition();
		level5Percent.setName("level5Percent");
		level5Percent.setColumnName("LEVEL5PERCENT_");
		level5Percent.setJavaType("Double");
		tableDefinition.addColumn(level5Percent);

		ColumnDefinition level7Percent = new ColumnDefinition();
		level7Percent.setName("level7Percent");
		level7Percent.setColumnName("LEVEL7PERCENT_");
		level7Percent.setJavaType("Double");
		tableDefinition.addColumn(level7Percent);

		ColumnDefinition level8Percent = new ColumnDefinition();
		level8Percent.setName("level8Percent");
		level8Percent.setColumnName("LEVEL8PERCENT_");
		level8Percent.setJavaType("Double");
		tableDefinition.addColumn(level8Percent);

		ColumnDefinition level9Percent = new ColumnDefinition();
		level9Percent.setName("level9Percent");
		level9Percent.setColumnName("LEVEL9PERCENT_");
		level9Percent.setJavaType("Double");
		tableDefinition.addColumn(level9Percent);

		ColumnDefinition normal = new ColumnDefinition();
		normal.setName("normal");
		normal.setColumnName("NORMAL_");
		normal.setJavaType("Integer");
		tableDefinition.addColumn(normal);

		ColumnDefinition normalPercent = new ColumnDefinition();
		normalPercent.setName("normalPercent");
		normalPercent.setColumnName("NORMALPERCENT_");
		normalPercent.setJavaType("Double");
		tableDefinition.addColumn(normalPercent);

		return tableDefinition;
	}

	private PhysicalGrowthCountDomainFactory() {

	}

}
