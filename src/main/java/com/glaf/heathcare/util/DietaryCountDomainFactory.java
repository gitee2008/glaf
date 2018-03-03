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

package com.glaf.heathcare.util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.heathcare.domain.CompositionCount;
import com.glaf.heathcare.domain.DietaryCount;

/**
 * 
 * 实体数据工厂类
 *
 */
public class DietaryCountDomainFactory {

	public static final String TABLENAME = "HEALTH_DIETARY_COUNT";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("parentId", "PARENTID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("heatEnergy", "HEATENERGY_");
		columnMap.put("heatEnergyCarbohydrate", "HEATENERGYCARBOHYDRATE_");
		columnMap.put("heatEnergyFat", "HEATENERGYFAT_");
		columnMap.put("protein", "PROTEIN_");
		columnMap.put("proteinAnimal", "PROTEINANIMAL_");
		columnMap.put("proteinAnimalBeans", "PROTEINANIMALBEANS_");
		columnMap.put("fat", "FAT_");
		columnMap.put("carbohydrate", "CARBOHYDRATE_");
		columnMap.put("vitaminA", "VITAMINA_");
		columnMap.put("vitaminB1", "VITAMINB1_");
		columnMap.put("vitaminB2", "VITAMINB2_");
		columnMap.put("vitaminB6", "VITAMINB6_");
		columnMap.put("vitaminB12", "VITAMINB12_");
		columnMap.put("vitaminC", "VITAMINC_");
		columnMap.put("vitaminE", "VITAMINE_");
		columnMap.put("carotene", "CAROTENE_");
		columnMap.put("retinol", "RETINOL_");
		columnMap.put("nicotinicCid", "NICOTINICCID_");
		columnMap.put("calcium", "CALCIUM_");
		columnMap.put("iron", "IRON_");
		columnMap.put("zinc", "ZINC_");
		columnMap.put("iodine", "IODINE_");
		columnMap.put("phosphorus", "PHOSPHORUS_");
		columnMap.put("mealType", "MEALTYPE_");
		columnMap.put("type", "TYPE_");
		columnMap.put("nodeId", "NODEID_");
		columnMap.put("semester", "SEMESTER_");
		columnMap.put("year", "YEAR_");
		columnMap.put("month", "MONTH_");
		columnMap.put("day", "DAY_");
		columnMap.put("dayOfWeek", "DAYOFWEEK_");
		columnMap.put("week", "WEEK_");
		columnMap.put("fullDay", "FULLDAY_");
		columnMap.put("createTime", "CREATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("parentId", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("heatEnergy", "Double");
		javaTypeMap.put("heatEnergyCarbohydrate", "Double");
		javaTypeMap.put("heatEnergyFat", "Double");
		javaTypeMap.put("protein", "Double");
		javaTypeMap.put("proteinAnimal", "Double");
		javaTypeMap.put("proteinAnimalBeans", "Double");
		javaTypeMap.put("fat", "Double");
		javaTypeMap.put("carbohydrate", "Double");
		javaTypeMap.put("vitaminA", "Double");
		javaTypeMap.put("vitaminB1", "Double");
		javaTypeMap.put("vitaminB2", "Double");
		javaTypeMap.put("vitaminB6", "Double");
		javaTypeMap.put("vitaminB12", "Double");
		javaTypeMap.put("vitaminC", "Double");
		javaTypeMap.put("vitaminE", "Double");
		javaTypeMap.put("carotene", "Double");
		javaTypeMap.put("retinol", "Double");
		javaTypeMap.put("nicotinicCid", "Double");
		javaTypeMap.put("calcium", "Double");
		javaTypeMap.put("iron", "Double");
		javaTypeMap.put("zinc", "Double");
		javaTypeMap.put("iodine", "Double");
		javaTypeMap.put("phosphorus", "Double");
		javaTypeMap.put("mealType", "String");
		javaTypeMap.put("type", "String");
		javaTypeMap.put("nodeId", "Long");
		javaTypeMap.put("semester", "Integer");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("month", "Integer");
		javaTypeMap.put("day", "Integer");
		javaTypeMap.put("dayOfWeek", "Integer");
		javaTypeMap.put("week", "Integer");
		javaTypeMap.put("fullDay", "Integer");
		javaTypeMap.put("createTime", "Date");
	}

	public static void alterTables(long databaseId) {
		TableDefinition tableDefinition = null;
		Connection conn = null;
		Statement statement = null;
		Database database = null;
		try {
			if (databaseId > 0) {
				IDatabaseService databaseService = ContextFactory.getBean("databaseService");
				database = databaseService.getDatabaseById(databaseId);
			}

			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}

			conn.setAutoCommit(false);
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition = getTableDefinition(TABLENAME + i);
				DBUtils.alterTable(conn, tableDefinition);
			}
			conn.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
	}

	/**
	 * 将汇总的属性复制到实例
	 * 
	 * @param dietary
	 * @param cnt
	 */
	public static void copyProperties(DietaryCount dietary, CompositionCount cnt) {
		dietary.setCalcium(cnt.getCalcium());
		dietary.setCarbohydrate(cnt.getCarbohydrate());
		dietary.setCarotene(cnt.getCarotene());
		dietary.setFat(cnt.getFat());
		dietary.setHeatEnergy(cnt.getHeatEnergy());
		dietary.setHeatEnergyCarbohydrate(cnt.getHeatEnergyCarbohydrate());
		dietary.setHeatEnergyFat(cnt.getHeatEnergyFat());
		dietary.setIodine(cnt.getIodine());
		dietary.setIron(cnt.getIron());
		dietary.setZinc(cnt.getZinc());
		dietary.setNicotinicCid(cnt.getNicotinicCid());
		dietary.setPhosphorus(cnt.getPhosphorus());
		dietary.setProtein(cnt.getProtein());
		dietary.setProteinAnimal(cnt.getProteinAnimal());
		dietary.setProteinAnimalBeans(cnt.getProteinAnimalBeans());
		dietary.setRetinol(cnt.getRetinol());
		dietary.setVitaminA(cnt.getVitaminA());
		dietary.setVitaminB1(cnt.getVitaminB1());
		dietary.setVitaminB2(cnt.getVitaminB2());
		dietary.setVitaminB6(cnt.getVitaminB6());
		dietary.setVitaminB12(cnt.getVitaminB12());
		dietary.setVitaminC(cnt.getVitaminC());
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

	public static void createTables(long databaseId) {
		String sqlStatement = null;
		TableDefinition tableDefinition = null;

		Connection conn = null;
		Statement statement = null;
		Database database = null;
		try {
			if (databaseId > 0) {
				IDatabaseService databaseService = ContextFactory.getBean("databaseService");
				database = databaseService.getDatabaseById(databaseId);
			}

			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}

			String dbType = DBConnectionFactory.getDatabaseType(conn);
			conn.setAutoCommit(false);
			for (int i = 0; i < com.glaf.core.util.Constants.TABLE_PARTITION; i++) {
				tableDefinition = getTableDefinition(TABLENAME + i);
				sqlStatement = DBUtils.getCreateTableScript(dbType, tableDefinition);
				statement = conn.createStatement();
				statement.executeUpdate(sqlStatement);
				JdbcUtils.close(statement);
			}
			conn.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(statement);
			JdbcUtils.close(conn);
		}
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
		tableDefinition.setName("DietaryCount");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition parentId = new ColumnDefinition();
		parentId.setName("parentId");
		parentId.setColumnName("PARENTID_");
		parentId.setJavaType("Long");
		tableDefinition.addColumn(parentId);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition heatEnergy = new ColumnDefinition();
		heatEnergy.setName("heatEnergy");
		heatEnergy.setColumnName("HEATENERGY_");
		heatEnergy.setJavaType("Double");
		tableDefinition.addColumn(heatEnergy);

		ColumnDefinition heatEnergyCarbohydrate = new ColumnDefinition();
		heatEnergyCarbohydrate.setName("heatEnergyCarbohydrate");
		heatEnergyCarbohydrate.setColumnName("HEATENERGYCARBOHYDRATE_");
		heatEnergyCarbohydrate.setJavaType("Double");
		tableDefinition.addColumn(heatEnergyCarbohydrate);

		ColumnDefinition heatEnergyFat = new ColumnDefinition();
		heatEnergyFat.setName("heatEnergyFat");
		heatEnergyFat.setColumnName("HEATENERGYFAT_");
		heatEnergyFat.setJavaType("Double");
		tableDefinition.addColumn(heatEnergyFat);

		ColumnDefinition protein = new ColumnDefinition();
		protein.setName("protein");
		protein.setColumnName("PROTEIN_");
		protein.setJavaType("Double");
		tableDefinition.addColumn(protein);

		ColumnDefinition proteinAnimal = new ColumnDefinition();
		proteinAnimal.setName("proteinAnimal");
		proteinAnimal.setColumnName("PROTEINANIMAL_");
		proteinAnimal.setJavaType("Double");
		tableDefinition.addColumn(proteinAnimal);

		ColumnDefinition proteinAnimalBeans = new ColumnDefinition();
		proteinAnimalBeans.setName("proteinAnimalBeans");
		proteinAnimalBeans.setColumnName("PROTEINANIMALBEANS_");
		proteinAnimalBeans.setJavaType("Double");
		tableDefinition.addColumn(proteinAnimalBeans);

		ColumnDefinition fat = new ColumnDefinition();
		fat.setName("fat");
		fat.setColumnName("FAT_");
		fat.setJavaType("Double");
		tableDefinition.addColumn(fat);

		ColumnDefinition carbohydrate = new ColumnDefinition();
		carbohydrate.setName("carbohydrate");
		carbohydrate.setColumnName("CARBOHYDRATE_");
		carbohydrate.setJavaType("Double");
		tableDefinition.addColumn(carbohydrate);

		ColumnDefinition vitaminA = new ColumnDefinition();
		vitaminA.setName("vitaminA");
		vitaminA.setColumnName("VITAMINA_");
		vitaminA.setJavaType("Double");
		tableDefinition.addColumn(vitaminA);

		ColumnDefinition vitaminB1 = new ColumnDefinition();
		vitaminB1.setName("vitaminB1");
		vitaminB1.setColumnName("VITAMINB1_");
		vitaminB1.setJavaType("Double");
		tableDefinition.addColumn(vitaminB1);

		ColumnDefinition vitaminB2 = new ColumnDefinition();
		vitaminB2.setName("vitaminB2");
		vitaminB2.setColumnName("VITAMINB2_");
		vitaminB2.setJavaType("Double");
		tableDefinition.addColumn(vitaminB2);

		ColumnDefinition vitaminB6 = new ColumnDefinition();
		vitaminB6.setName("vitaminB6");
		vitaminB6.setColumnName("VITAMINB6_");
		vitaminB6.setJavaType("Double");
		tableDefinition.addColumn(vitaminB6);

		ColumnDefinition vitaminB12 = new ColumnDefinition();
		vitaminB12.setName("vitaminB12");
		vitaminB12.setColumnName("VITAMINB12_");
		vitaminB12.setJavaType("Double");
		tableDefinition.addColumn(vitaminB12);

		ColumnDefinition vitaminC = new ColumnDefinition();
		vitaminC.setName("vitaminC");
		vitaminC.setColumnName("VITAMINC_");
		vitaminC.setJavaType("Double");
		tableDefinition.addColumn(vitaminC);

		ColumnDefinition vitaminE = new ColumnDefinition();
		vitaminE.setName("vitaminE");
		vitaminE.setColumnName("VITAMINE_");
		vitaminE.setJavaType("Double");
		tableDefinition.addColumn(vitaminE);

		ColumnDefinition carotene = new ColumnDefinition();
		carotene.setName("carotene");
		carotene.setColumnName("CAROTENE_");
		carotene.setJavaType("Double");
		tableDefinition.addColumn(carotene);

		ColumnDefinition retinol = new ColumnDefinition();
		retinol.setName("retinol");
		retinol.setColumnName("RETINOL_");
		retinol.setJavaType("Double");
		tableDefinition.addColumn(retinol);

		ColumnDefinition nicotinicCid = new ColumnDefinition();
		nicotinicCid.setName("nicotinicCid");
		nicotinicCid.setColumnName("NICOTINICCID_");
		nicotinicCid.setJavaType("Double");
		tableDefinition.addColumn(nicotinicCid);

		ColumnDefinition calcium = new ColumnDefinition();
		calcium.setName("calcium");
		calcium.setColumnName("CALCIUM_");
		calcium.setJavaType("Double");
		tableDefinition.addColumn(calcium);

		ColumnDefinition iron = new ColumnDefinition();
		iron.setName("iron");
		iron.setColumnName("IRON_");
		iron.setJavaType("Double");
		tableDefinition.addColumn(iron);

		ColumnDefinition zinc = new ColumnDefinition();
		zinc.setName("zinc");
		zinc.setColumnName("ZINC_");
		zinc.setJavaType("Double");
		tableDefinition.addColumn(zinc);

		ColumnDefinition iodine = new ColumnDefinition();
		iodine.setName("iodine");
		iodine.setColumnName("IODINE_");
		iodine.setJavaType("Double");
		tableDefinition.addColumn(iodine);

		ColumnDefinition phosphorus = new ColumnDefinition();
		phosphorus.setName("phosphorus");
		phosphorus.setColumnName("PHOSPHORUS_");
		phosphorus.setJavaType("Double");
		tableDefinition.addColumn(phosphorus);

		ColumnDefinition mealType = new ColumnDefinition();
		mealType.setName("mealType");
		mealType.setColumnName("MEALTYPE_");
		mealType.setJavaType("String");
		mealType.setLength(50);
		tableDefinition.addColumn(mealType);

		ColumnDefinition type = new ColumnDefinition();
		type.setName("type");
		type.setColumnName("TYPE_");
		type.setJavaType("String");
		type.setLength(50);
		tableDefinition.addColumn(type);

		ColumnDefinition nodeId = new ColumnDefinition();
		nodeId.setName("nodeId");
		nodeId.setColumnName("NODEID_");
		nodeId.setJavaType("Long");
		tableDefinition.addColumn(nodeId);

		ColumnDefinition semester = new ColumnDefinition();
		semester.setName("semester");
		semester.setColumnName("SEMESTER_");
		semester.setJavaType("Integer");
		tableDefinition.addColumn(semester);

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition month = new ColumnDefinition();
		month.setName("month");
		month.setColumnName("MONTH_");
		month.setJavaType("Integer");
		tableDefinition.addColumn(month);

		ColumnDefinition day = new ColumnDefinition();
		day.setName("day");
		day.setColumnName("DAY_");
		day.setJavaType("Integer");
		tableDefinition.addColumn(day);

		ColumnDefinition dayOfWeek = new ColumnDefinition();
		dayOfWeek.setName("dayOfWeek");
		dayOfWeek.setColumnName("DAYOFWEEK_");
		dayOfWeek.setJavaType("Integer");
		tableDefinition.addColumn(dayOfWeek);

		ColumnDefinition week = new ColumnDefinition();
		week.setName("week");
		week.setColumnName("WEEK_");
		week.setJavaType("Integer");
		tableDefinition.addColumn(week);

		ColumnDefinition fullDay = new ColumnDefinition();
		fullDay.setName("fullDay");
		fullDay.setColumnName("FULLDAY_");
		fullDay.setJavaType("Integer");
		tableDefinition.addColumn(fullDay);

		ColumnDefinition createTime = new ColumnDefinition();
		createTime.setName("createTime");
		createTime.setColumnName("CREATETIME_");
		createTime.setJavaType("Date");
		tableDefinition.addColumn(createTime);

		return tableDefinition;
	}

	private DietaryCountDomainFactory() {

	}

}
