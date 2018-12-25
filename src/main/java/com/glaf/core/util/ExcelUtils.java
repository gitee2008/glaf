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

package com.glaf.core.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class ExcelUtils {

	public static String getCellValue(CellValue cell, int precision) {
		String cellValue = null;
		if (cell.getCellType() == CellType.NUMERIC) {
			double value = cell.getNumberValue();
			if (precision > 0) {
				value = Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
			}
			cellValue = String.valueOf(value);
			if (cellValue != null && cellValue.trim().endsWith(".0")) {
				cellValue = cellValue.substring(0, cellValue.length() - 2);
			}
		} else if (cell.getCellType() == CellType.FORMULA) {

		} else {
			cellValue = cell.getStringValue();
		}
		return cellValue;
	}

	public static String getString(HSSFCell cell, int precision) {
		String strValue = null;
		if (cell.getCellType() == CellType.NUMERIC) {
			double value = cell.getNumericCellValue();
			DecimalFormat nf = new DecimalFormat("###");
			return nf.format(value);
		} else if (cell.getCellType() == CellType.STRING) {
			strValue = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.FORMULA) {

		}
		if (strValue != null) {
			return strValue;
		}
		return "";
	}

	public static String getStringOrDateValue(HSSFCell cell, int precision) {
		String strValue = null;
		if (cell.getCellType() == CellType.NUMERIC) {
			short format = cell.getCellStyle().getDataFormat();
			SimpleDateFormat sdf = null;
			if (format == 14 || format == 31 || format == 57 || format == 58 || (176 <= format && format <= 178)
					|| (182 <= format && format <= 196) || (210 <= format && format <= 213) || (208 == format)) { // 日期
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			} else if (format == 20 || format == 32 || format == 183 || (200 <= format && format <= 209)) { // 时间
				sdf = new SimpleDateFormat("HH:mm");
			} else { // 不是日期格式
				double value = cell.getNumericCellValue();
				if (precision > 0) {
					value = Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
				}
				strValue = String.valueOf(value);
				if (strValue != null && strValue.trim().endsWith(".0")) {
					strValue = strValue.substring(0, strValue.length() - 2);
				}
				return strValue;
			}
			double value = cell.getNumericCellValue();
			Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
			if (date == null) {
				return "";
			}
			String result = "";
			try {
				result = sdf.format(date);
			} catch (Exception e) {
				return "";
			}
			// logger.debug(result);
			return result;
		} else if (cell.getCellType() == CellType.STRING) {
			strValue = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.FORMULA) {

		}
		if (strValue != null) {
			return strValue;
		}
		return "";
	}

	public static String getValue(FormulaEvaluator evaluator, HSSFCell cell, int precision) {
		String strValue = null;
		if (cell.getCellType() == CellType.NUMERIC) {
			double value = cell.getNumericCellValue();
			if (precision > 0) {
				value = Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
			}
			strValue = String.valueOf(value);
			if (strValue != null && strValue.trim().endsWith(".0")) {
				strValue = strValue.substring(0, strValue.length() - 2);
			}
			return strValue;
		} else if (cell.getCellType() == CellType.STRING) {
			strValue = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.FORMULA) {
			CellValue cellValue = evaluator.evaluate(cell);
			strValue = getCellValue(cellValue, precision);
		}
		if (strValue != null) {
			return strValue;
		}
		return "";
	}

	public static String getValue(HSSFCell cell, int precision) {
		String strValue = null;
		if (cell.getCellType() == CellType.NUMERIC) {
			double value = cell.getNumericCellValue();
			if (precision > 0) {
				value = Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
			}
			strValue = String.valueOf(value);
			if (strValue != null && strValue.trim().endsWith(".0")) {
				strValue = strValue.substring(0, strValue.length() - 2);
			}
			return strValue;
		} else if (cell.getCellType() == CellType.STRING) {
			strValue = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.FORMULA) {

		}
		if (strValue != null) {
			return strValue;
		}
		return "";
	}

	private ExcelUtils() {

	}
}
