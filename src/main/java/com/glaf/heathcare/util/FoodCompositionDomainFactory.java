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
public class FoodCompositionDomainFactory {

	public static final String TABLENAME = "HEALTH_FOOD_COMPOSITION";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("nodeId", "NODEID_");
		columnMap.put("treeId", "TREEID_");
		columnMap.put("name", "NAME_");
		columnMap.put("alias", "ALIAS_");
		columnMap.put("code", "CODE_");
		columnMap.put("discriminator", "DISCRIMINATOR_");
		columnMap.put("description", "DESCRIPTION_");
		columnMap.put("radical", "RADICAL_");
		columnMap.put("heatEnergy", "HEATENERGY_");
		columnMap.put("protein", "PROTEIN_");
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
		columnMap.put("copper", "COPPER_");
		columnMap.put("magnesium", "MAGNESIUM_");
		columnMap.put("manganese", "MANGANESE_");
		columnMap.put("potassium", "POTASSIUM_");
		columnMap.put("selenium", "SELENIUM_");
		columnMap.put("sortNo", "SORTNO_");
		columnMap.put("dailyFlag", "DAILYFLAG_");
		columnMap.put("colorFlag", "COLORFLAG_");
		columnMap.put("cerealFlag", "CEREALFLAG_");
		columnMap.put("beansFlag", "BEANSFLAG_");
		columnMap.put("enableFlag", "ENABLEFLAG_");
		columnMap.put("sysFlag", "SYSFLAG_");
		columnMap.put("useFlag", "USEFLAG_");
		columnMap.put("createBy", "CREATEBY_");
		columnMap.put("createTime", "CREATETIME_");
		columnMap.put("updateBy", "UPDATEBY_");
		columnMap.put("updateTime", "UPDATETIME_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("nodeId", "Long");
		javaTypeMap.put("treeId", "String");
		javaTypeMap.put("name", "String");
		javaTypeMap.put("alias", "String");
		javaTypeMap.put("code", "String");
		javaTypeMap.put("discriminator", "String");
		javaTypeMap.put("description", "String");
		javaTypeMap.put("radical", "Double");
		javaTypeMap.put("heatEnergy", "Double");
		javaTypeMap.put("protein", "Double");
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
		javaTypeMap.put("copper", "Double");
		javaTypeMap.put("magnesium", "Double");
		javaTypeMap.put("manganese", "Double");
		javaTypeMap.put("potassium", "Double");
		javaTypeMap.put("selenium", "Double");
		javaTypeMap.put("sortNo", "Integer");
		javaTypeMap.put("dailyFlag", "String");
		javaTypeMap.put("colorFlag", "String");
		javaTypeMap.put("cerealFlag", "String");
		javaTypeMap.put("beansFlag", "String");
		javaTypeMap.put("enableFlag", "String");
		javaTypeMap.put("sysFlag", "String");
		javaTypeMap.put("useFlag", "String");
		javaTypeMap.put("createBy", "String");
		javaTypeMap.put("createTime", "Date");
		javaTypeMap.put("updateBy", "String");
		javaTypeMap.put("updateTime", "Date");
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
		tableDefinition.setName("FoodComposition");

		ColumnDefinition idColumn = new ColumnDefinition();
		idColumn.setName("id");
		idColumn.setColumnName("ID_");
		idColumn.setJavaType("Long");
		tableDefinition.setIdColumn(idColumn);

		ColumnDefinition tenantId = new ColumnDefinition();
		tenantId.setName("tenantId");
		tenantId.setColumnName("TENANTID_");
		tenantId.setJavaType("String");
		tenantId.setLength(50);
		tableDefinition.addColumn(tenantId);

		ColumnDefinition nodeId = new ColumnDefinition();
		nodeId.setName("nodeId");
		nodeId.setColumnName("NODEID_");
		nodeId.setJavaType("Long");
		tableDefinition.addColumn(nodeId);

		ColumnDefinition treeId = new ColumnDefinition();
		treeId.setName("treeId");
		treeId.setColumnName("TREEID_");
		treeId.setJavaType("String");
		treeId.setLength(200);
		tableDefinition.addColumn(treeId);

		ColumnDefinition name = new ColumnDefinition();
		name.setName("name");
		name.setColumnName("NAME_");
		name.setJavaType("String");
		name.setLength(200);
		tableDefinition.addColumn(name);

		ColumnDefinition alias = new ColumnDefinition();
		alias.setName("alias");
		alias.setColumnName("ALIAS_");
		alias.setJavaType("String");
		alias.setLength(200);
		tableDefinition.addColumn(alias);

		ColumnDefinition code = new ColumnDefinition();
		code.setName("code");
		code.setColumnName("CODE_");
		code.setJavaType("String");
		code.setLength(50);
		tableDefinition.addColumn(code);

		ColumnDefinition discriminator = new ColumnDefinition();
		discriminator.setName("discriminator");
		discriminator.setColumnName("DISCRIMINATOR_");
		discriminator.setJavaType("String");
		discriminator.setLength(50);
		tableDefinition.addColumn(discriminator);

		ColumnDefinition description = new ColumnDefinition();
		description.setName("description");
		description.setColumnName("DESCRIPTION_");
		description.setJavaType("String");
		description.setLength(4000);
		tableDefinition.addColumn(description);

		ColumnDefinition radical = new ColumnDefinition();
		radical.setName("radical");
		radical.setColumnName("RADICAL_");
		radical.setJavaType("Double");
		tableDefinition.addColumn(radical);

		ColumnDefinition heatEnergy = new ColumnDefinition();
		heatEnergy.setName("heatEnergy");
		heatEnergy.setColumnName("HEATENERGY_");
		heatEnergy.setJavaType("Double");
		tableDefinition.addColumn(heatEnergy);

		ColumnDefinition protein = new ColumnDefinition();
		protein.setName("protein");
		protein.setColumnName("PROTEIN_");
		protein.setJavaType("Double");
		tableDefinition.addColumn(protein);

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

		ColumnDefinition copper = new ColumnDefinition();
		copper.setName("copper");
		copper.setColumnName("COPPER_");
		copper.setJavaType("Double");
		tableDefinition.addColumn(copper);

		ColumnDefinition magnesium = new ColumnDefinition();
		magnesium.setName("magnesium");
		magnesium.setColumnName("MAGNESIUM_");
		magnesium.setJavaType("Double");
		tableDefinition.addColumn(magnesium);

		ColumnDefinition manganese = new ColumnDefinition();
		manganese.setName("manganese");
		manganese.setColumnName("MANGANESE_");
		manganese.setJavaType("Double");
		tableDefinition.addColumn(manganese);

		ColumnDefinition potassium = new ColumnDefinition();
		potassium.setName("potassium");
		potassium.setColumnName("POTASSIUM_");
		potassium.setJavaType("Double");
		tableDefinition.addColumn(potassium);

		ColumnDefinition selenium = new ColumnDefinition();
		selenium.setName("selenium");
		selenium.setColumnName("SELENIUM_");
		selenium.setJavaType("Double");
		tableDefinition.addColumn(selenium);

		ColumnDefinition sortNo = new ColumnDefinition();
		sortNo.setName("sortNo");
		sortNo.setColumnName("SORTNO_");
		sortNo.setJavaType("Integer");
		tableDefinition.addColumn(sortNo);

		ColumnDefinition dailyFlag = new ColumnDefinition();
		dailyFlag.setName("dailyFlag");
		dailyFlag.setColumnName("DAILYFLAG_");
		dailyFlag.setJavaType("String");
		dailyFlag.setLength(1);
		tableDefinition.addColumn(dailyFlag);

		ColumnDefinition colorFlag = new ColumnDefinition();
		colorFlag.setName("colorFlag");
		colorFlag.setColumnName("COLORFLAG_");
		colorFlag.setJavaType("String");
		colorFlag.setLength(1);
		tableDefinition.addColumn(colorFlag);

		ColumnDefinition cerealFlag = new ColumnDefinition();
		cerealFlag.setName("cerealFlag");
		cerealFlag.setColumnName("CEREALFLAG_");
		cerealFlag.setJavaType("String");
		cerealFlag.setLength(1);
		tableDefinition.addColumn(cerealFlag);

		ColumnDefinition beansFlag = new ColumnDefinition();
		beansFlag.setName("beansFlag");
		beansFlag.setColumnName("BEANSFLAG_");
		beansFlag.setJavaType("String");
		beansFlag.setLength(1);
		tableDefinition.addColumn(beansFlag);

		ColumnDefinition enableFlag = new ColumnDefinition();
		enableFlag.setName("enableFlag");
		enableFlag.setColumnName("ENABLEFLAG_");
		enableFlag.setJavaType("String");
		enableFlag.setLength(1);
		tableDefinition.addColumn(enableFlag);

		ColumnDefinition sysFlag = new ColumnDefinition();
		sysFlag.setName("sysFlag");
		sysFlag.setColumnName("SYSFLAG_");
		sysFlag.setJavaType("String");
		sysFlag.setLength(1);
		tableDefinition.addColumn(sysFlag);

		ColumnDefinition useFlag = new ColumnDefinition();
		useFlag.setName("useFlag");
		useFlag.setColumnName("USEFLAG_");
		useFlag.setJavaType("String");
		useFlag.setLength(1);
		tableDefinition.addColumn(useFlag);

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

	private FoodCompositionDomainFactory() {

	}

}
