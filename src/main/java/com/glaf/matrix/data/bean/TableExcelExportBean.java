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

package com.glaf.matrix.data.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.context.ContextFactory;

import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;
import com.glaf.matrix.data.service.ITableService;

public class TableExcelExportBean {
	protected static final Log logger = LogFactory.getLog(TableExcelExportBean.class);

	protected ITableService tableService;

	public XSSFWorkbook export(HttpServletRequest request) throws IOException {
		TableDataBean tableDataBean = new TableDataBean();
		JSONObject result = tableDataBean.toJson(request);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Sheet 1");
		XSSFRow row1 = sheet.createRow(0);
		row1.setHeightInPoints(30);

		XSSFFont font1 = workbook.createFont();
		font1.setFontName("宋体");
		font1.setBold(true);
		font1.setFontHeightInPoints((short) 15);// 设置字体大小

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font1);// 选择需要用到的字体格式
		cellStyle.setWrapText(true);// 设置自动换行
		cellStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);

		XSSFFont font2 = workbook.createFont();
		font2.setFontName("宋体");
		font2.setBold(true);
		font2.setFontHeightInPoints((short) 13);// 设置字体大小
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(font2);// 选择需要用到的字体格式
		headerStyle.setWrapText(true);// 设置自动换行
		headerStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		XSSFFont font3 = workbook.createFont();
		font3.setFontName("宋体");
		font3.setBold(false);
		font3.setFontHeightInPoints((short) 12);// 设置字体大小
		XSSFCellStyle txtStyle = workbook.createCellStyle();
		txtStyle.setFont(font3);// 选择需要用到的字体格式
		txtStyle.setWrapText(true);// 设置自动换行
		txtStyle.setBorderTop(BorderStyle.THIN);
		txtStyle.setBorderBottom(BorderStyle.THIN);
		txtStyle.setBorderLeft(BorderStyle.THIN);
		txtStyle.setBorderRight(BorderStyle.THIN);

		XSSFFont font4 = workbook.createFont();
		font4.setFontName("宋体");
		font4.setBold(false);
		font4.setFontHeightInPoints((short) 12);// 设置字体大小
		XSSFCellStyle numStyle = workbook.createCellStyle();
		numStyle.setFont(font4);// 选择需要用到的字体格式
		numStyle.setWrapText(true);// 设置自动换行
		numStyle.setBorderTop(BorderStyle.THIN);
		numStyle.setBorderBottom(BorderStyle.THIN);
		numStyle.setBorderLeft(BorderStyle.THIN);
		numStyle.setBorderRight(BorderStyle.THIN);

		String tableId = request.getParameter("tableId");
		JSONArray array = result.getJSONArray("rows");
		if (array != null && array.size() > 0) {
			if (StringUtils.isNotEmpty(tableId)) {
				SysTable sysTable = getTableService().getSysTableById(tableId);
				if (sysTable != null) {
					List<TableColumn> columns = sysTable.getColumns();
					if (columns != null && !columns.isEmpty()) {
						List<TableColumn> exportColumns = new ArrayList<TableColumn>();
						for (TableColumn col : columns) {
							if (StringUtils.isNotEmpty(col.getExportFlag())) {
								if (StringUtils.equals(col.getExportFlag(), "Y")) {
									exportColumns.add(col);
								}
							} else {
								exportColumns.add(col);
							}
						}

						sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, exportColumns.size()));

						XSSFCell cell1 = row1.createCell(0);
						cell1.setCellValue(sysTable.getTitle());
						cell1.setCellStyle(cellStyle);

						XSSFRow row2 = sheet.createRow(1);
						XSSFRow row = null;
						XSSFCell cell = null;
						int columnIndex = 0;
						int rowIndex = 2;
						int colIndex = 0;
						int HUNDRED = 100;
						cell = row2.createCell(columnIndex++);
						cell.setCellValue("序号");
						cell.setCellStyle(headerStyle);
						sheet.setColumnWidth(colIndex++, HUNDRED * 15);
						for (TableColumn column : exportColumns) {
							cell = row2.createCell(columnIndex++);
							cell.setCellValue(column.getTitle());
							cell.setCellStyle(headerStyle);
							logger.debug(column.getColumnName() + ":" + column.getType());
							switch (column.getType()) {
							case "Date":
								sheet.setColumnWidth(colIndex, HUNDRED * 58);
								break;
							case "Integer":
								sheet.setColumnWidth(colIndex, HUNDRED * 20);
								break;
							case "Long":
								sheet.setColumnWidth(colIndex, HUNDRED * 20);
								break;
							case "Double":
								sheet.setColumnWidth(colIndex, HUNDRED * 20);
								break;
							case "String":
								if (column.getLength() > 500) {
									sheet.setColumnWidth(colIndex, HUNDRED * 200);
								} else {
									sheet.setColumnWidth(colIndex, HUNDRED * 80);
								}
								break;
							default:
								sheet.setColumnWidth(colIndex, HUNDRED * 80);
								break;
							}
							colIndex++;
						}

						String str = null;
						Object object = null;
						JSONObject json = null;
						for (int i = 0, len = array.size(); i < len; i++) {
							json = array.getJSONObject(i);
							row = sheet.createRow(rowIndex++);
							columnIndex = 0;
							cell = row.createCell(columnIndex++);
							cell.setCellValue(String.valueOf(i + 1));
							txtStyle.setAlignment(HorizontalAlignment.CENTER);
							cell.setCellStyle(txtStyle);
							for (TableColumn column : exportColumns) {
								cell = row.createCell(columnIndex++);
								switch (column.getType()) {
								case "Date":
									str = json.getString(column.getColumnName().toLowerCase());
									cell.setCellValue(str);
									txtStyle.setAlignment(HorizontalAlignment.CENTER);
									cell.setCellStyle(txtStyle);
									break;
								case "Integer":
									Integer ix = json.getInteger(column.getColumnName().toLowerCase());
									cell.setCellValue(ix);
									numStyle.setAlignment(HorizontalAlignment.RIGHT);
									cell.setCellStyle(numStyle);
									break;
								case "Long":
									Long lx = json.getLong(column.getColumnName().toLowerCase());
									cell.setCellValue(lx);
									numStyle.setAlignment(HorizontalAlignment.RIGHT);
									cell.setCellStyle(numStyle);
									break;
								case "Double":
									double d = json.getDouble(column.getColumnName().toLowerCase());
									cell.setCellValue(d);
									numStyle.setAlignment(HorizontalAlignment.RIGHT);
									cell.setCellStyle(numStyle);
									break;
								case "String":
									str = json.getString(column.getColumnName().toLowerCase());
									cell.setCellValue(str);
									txtStyle.setAlignment(HorizontalAlignment.LEFT);
									cell.setCellStyle(txtStyle);
									break;
								default:
									object = json.get(column.getColumnName().toLowerCase());
									cell.setCellValue(object != null ? object.toString() : "");
									txtStyle.setAlignment(HorizontalAlignment.LEFT);
									cell.setCellStyle(txtStyle);
									break;
								}
							}
						}
					}
				}
			}
		}
		return workbook;
	}

	public ITableService getTableService() {
		if (tableService == null) {
			tableService = ContextFactory.getBean("tableService");
		}
		return tableService;
	}

}
