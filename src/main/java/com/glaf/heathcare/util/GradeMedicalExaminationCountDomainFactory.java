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
public class GradeMedicalExaminationCountDomainFactory {

	public static final String TABLENAME = "HEALTH_MEDICAL_EXAMINATIONCOUNT";

	public static final ConcurrentMap<String, String> columnMap = new ConcurrentHashMap<String, String>();

	public static final ConcurrentMap<String, String> javaTypeMap = new ConcurrentHashMap<String, String>();

	static {
		columnMap.put("id", "ID_");
		columnMap.put("checkId", "CHECKID_");
		columnMap.put("tenantId", "TENANTID_");
		columnMap.put("gradeId", "GRADEID_");
		columnMap.put("female", "FEMALE_");
		columnMap.put("male", "MALE_");
		columnMap.put("personCount", "PERSONCOUNT_");
		columnMap.put("checkPerson", "CHECKPERSON_");
		columnMap.put("checkPercent", "CHECKPERCENT_");
		columnMap.put("growthRatePerson", "GROWTHRATEPERSON_");
		columnMap.put("growthRatePersonPercent", "GROWTHRATEPERSONPERCENT_");
		columnMap.put("weightLow", "WEIGHTLOW_");
		columnMap.put("weightSkinny", "WEIGHTSKINNY_");
		columnMap.put("weightRetardation", "WEIGHTRETARDATION_");
		columnMap.put("weightSevereMalnutrition", "WEIGHTSEVEREMALNUTRITION_");
		columnMap.put("overWeight", "OVERWEIGHT_");
		columnMap.put("weightObesityLow", "WEIGHTOBESITYLOW_");
		columnMap.put("weightObesityMid", "WEIGHTOBESITYMID_");
		columnMap.put("weightObesityHigh", "WEIGHTOBESITYHIGH_");
		columnMap.put("anemiaCheck", "ANEMIACHECK_");
		columnMap.put("anemiaCheckNormal", "ANEMIACHECKNORMAL_");
		columnMap.put("anemiaCheckNormalPercent", "ANEMIACHECKNORMALPERCENT_");
		columnMap.put("anemiaLow", "ANEMIALOW_");
		columnMap.put("anemiaMid", "ANEMIAMID_");
		columnMap.put("anemiaHigh", "ANEMIAHIGH_");
		columnMap.put("bloodLead", "BLOODLEAD_");
		columnMap.put("year", "YEAR_");
		columnMap.put("semester", "SEMESTER_");
		columnMap.put("fullDay", "FULLDAY_");
		columnMap.put("sortNo", "SORTNO_");

		javaTypeMap.put("id", "Long");
		javaTypeMap.put("checkId", "String");
		javaTypeMap.put("tenantId", "String");
		javaTypeMap.put("gradeId", "String");
		javaTypeMap.put("female", "Integer");
		javaTypeMap.put("male", "Integer");
		javaTypeMap.put("personCount", "Integer");
		javaTypeMap.put("checkPerson", "Integer");
		javaTypeMap.put("checkPercent", "Double");
		javaTypeMap.put("growthRatePerson", "Integer");
		javaTypeMap.put("growthRatePersonPercent", "Double");
		javaTypeMap.put("weightLow", "Integer");
		javaTypeMap.put("weightSkinny", "Integer");
		javaTypeMap.put("weightRetardation", "Integer");
		javaTypeMap.put("weightSevereMalnutrition", "Integer");
		javaTypeMap.put("overWeight", "Integer");
		javaTypeMap.put("weightObesityLow", "Integer");
		javaTypeMap.put("weightObesityMid", "Integer");
		javaTypeMap.put("weightObesityHigh", "Integer");
		javaTypeMap.put("anemiaCheck", "Integer");
		javaTypeMap.put("anemiaCheckNormal", "Integer");
		javaTypeMap.put("anemiaCheckNormalPercent", "Double");
		javaTypeMap.put("anemiaLow", "Integer");
		javaTypeMap.put("anemiaMid", "Integer");
		javaTypeMap.put("anemiaHigh", "Integer");
		javaTypeMap.put("bloodLead", "Integer");
		javaTypeMap.put("year", "Integer");
		javaTypeMap.put("semester", "Integer");
		javaTypeMap.put("fullDay", "Integer");
		javaTypeMap.put("sortNo", "Integer");
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
		tableDefinition.setName("GradeMedicalExaminationCount");

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

		ColumnDefinition female = new ColumnDefinition();
		female.setName("female");
		female.setColumnName("FEMALE_");
		female.setJavaType("Integer");
		tableDefinition.addColumn(female);

		ColumnDefinition male = new ColumnDefinition();
		male.setName("male");
		male.setColumnName("MALE_");
		male.setJavaType("Integer");
		tableDefinition.addColumn(male);

		ColumnDefinition personCount = new ColumnDefinition();
		personCount.setName("personCount");
		personCount.setColumnName("PERSONCOUNT_");
		personCount.setJavaType("Integer");
		tableDefinition.addColumn(personCount);

		ColumnDefinition checkPerson = new ColumnDefinition();
		checkPerson.setName("checkPerson");
		checkPerson.setColumnName("CHECKPERSON_");
		checkPerson.setJavaType("Integer");
		tableDefinition.addColumn(checkPerson);

		ColumnDefinition checkPercent = new ColumnDefinition();
		checkPercent.setName("checkPercent");
		checkPercent.setColumnName("CHECKPERCENT_");
		checkPercent.setJavaType("Double");
		tableDefinition.addColumn(checkPercent);

		ColumnDefinition growthRatePerson = new ColumnDefinition();
		growthRatePerson.setName("growthRatePerson");
		growthRatePerson.setColumnName("GROWTHRATEPERSON_");
		growthRatePerson.setJavaType("Integer");
		tableDefinition.addColumn(growthRatePerson);

		ColumnDefinition growthRatePersonPercent = new ColumnDefinition();
		growthRatePersonPercent.setName("growthRatePersonPercent");
		growthRatePersonPercent.setColumnName("GROWTHRATEPERSONPERCENT_");
		growthRatePersonPercent.setJavaType("Double");
		tableDefinition.addColumn(growthRatePersonPercent);

		ColumnDefinition weightLow = new ColumnDefinition();
		weightLow.setName("weightLow");
		weightLow.setColumnName("WEIGHTLOW_");
		weightLow.setJavaType("Integer");
		tableDefinition.addColumn(weightLow);

		ColumnDefinition weightSkinny = new ColumnDefinition();
		weightSkinny.setName("weightSkinny");
		weightSkinny.setColumnName("WEIGHTSKINNY_");
		weightSkinny.setJavaType("Integer");
		tableDefinition.addColumn(weightSkinny);

		ColumnDefinition weightRetardation = new ColumnDefinition();
		weightRetardation.setName("weightRetardation");
		weightRetardation.setColumnName("WEIGHTRETARDATION_");
		weightRetardation.setJavaType("Integer");
		tableDefinition.addColumn(weightRetardation);

		ColumnDefinition weightSevereMalnutrition = new ColumnDefinition();
		weightSevereMalnutrition.setName("weightSevereMalnutrition");
		weightSevereMalnutrition.setColumnName("WEIGHTSEVEREMALNUTRITION_");
		weightSevereMalnutrition.setJavaType("Integer");
		tableDefinition.addColumn(weightSevereMalnutrition);

		ColumnDefinition overWeight = new ColumnDefinition();
		overWeight.setName("overWeight");
		overWeight.setColumnName("OVERWEIGHT_");
		overWeight.setJavaType("Integer");
		tableDefinition.addColumn(overWeight);

		ColumnDefinition weightObesityLow = new ColumnDefinition();
		weightObesityLow.setName("weightObesityLow");
		weightObesityLow.setColumnName("WEIGHTOBESITYLOW_");
		weightObesityLow.setJavaType("Integer");
		tableDefinition.addColumn(weightObesityLow);

		ColumnDefinition weightObesityMid = new ColumnDefinition();
		weightObesityMid.setName("weightObesityMid");
		weightObesityMid.setColumnName("WEIGHTOBESITYMID_");
		weightObesityMid.setJavaType("Integer");
		tableDefinition.addColumn(weightObesityMid);

		ColumnDefinition weightObesityHigh = new ColumnDefinition();
		weightObesityHigh.setName("weightObesityHigh");
		weightObesityHigh.setColumnName("WEIGHTOBESITYHIGH_");
		weightObesityHigh.setJavaType("Integer");
		tableDefinition.addColumn(weightObesityHigh);

		ColumnDefinition anemiaCheck = new ColumnDefinition();
		anemiaCheck.setName("anemiaCheck");
		anemiaCheck.setColumnName("ANEMIACHECK_");
		anemiaCheck.setJavaType("Integer");
		tableDefinition.addColumn(anemiaCheck);

		ColumnDefinition anemiaCheckNormal = new ColumnDefinition();
		anemiaCheckNormal.setName("anemiaCheckNormal");
		anemiaCheckNormal.setColumnName("ANEMIACHECKNORMAL_");
		anemiaCheckNormal.setJavaType("Integer");
		tableDefinition.addColumn(anemiaCheckNormal);

		ColumnDefinition anemiaCheckNormalPercent = new ColumnDefinition();
		anemiaCheckNormalPercent.setName("anemiaCheckNormalPercent");
		anemiaCheckNormalPercent.setColumnName("ANEMIACHECKNORMALPERCENT_");
		anemiaCheckNormalPercent.setJavaType("Double");
		tableDefinition.addColumn(anemiaCheckNormalPercent);

		ColumnDefinition anemiaLow = new ColumnDefinition();
		anemiaLow.setName("anemiaLow");
		anemiaLow.setColumnName("ANEMIALOW_");
		anemiaLow.setJavaType("Integer");
		tableDefinition.addColumn(anemiaLow);

		ColumnDefinition anemiaMid = new ColumnDefinition();
		anemiaMid.setName("anemiaMid");
		anemiaMid.setColumnName("ANEMIAMID_");
		anemiaMid.setJavaType("Integer");
		tableDefinition.addColumn(anemiaMid);

		ColumnDefinition anemiaHigh = new ColumnDefinition();
		anemiaHigh.setName("anemiaHigh");
		anemiaHigh.setColumnName("ANEMIAHIGH_");
		anemiaHigh.setJavaType("Integer");
		tableDefinition.addColumn(anemiaHigh);

		ColumnDefinition bloodLead = new ColumnDefinition();
		bloodLead.setName("bloodLead");
		bloodLead.setColumnName("BLOODLEAD_");
		bloodLead.setJavaType("Integer");
		tableDefinition.addColumn(bloodLead);

		ColumnDefinition year = new ColumnDefinition();
		year.setName("year");
		year.setColumnName("YEAR_");
		year.setJavaType("Integer");
		tableDefinition.addColumn(year);

		ColumnDefinition semester = new ColumnDefinition();
		semester.setName("semester");
		semester.setColumnName("SEMESTER_");
		semester.setJavaType("Integer");
		tableDefinition.addColumn(semester);

		ColumnDefinition fullDay = new ColumnDefinition();
		fullDay.setName("fullDay");
		fullDay.setColumnName("FULLDAY_");
		fullDay.setJavaType("Integer");
		tableDefinition.addColumn(fullDay);

		ColumnDefinition sortNo = new ColumnDefinition();
		sortNo.setName("sortNo");
		sortNo.setColumnName("SORTNO_");
		sortNo.setJavaType("Integer");
		tableDefinition.addColumn(sortNo);

		return tableDefinition;
	}

	private GradeMedicalExaminationCountDomainFactory() {

	}

}
