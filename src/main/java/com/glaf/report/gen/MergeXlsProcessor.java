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

package com.glaf.report.gen;

import java.util.Stack;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSONObject;
import com.glaf.core.util.StringTools;

/**
 * 合并单元格的处理器 com.glaf.report.gen.MergeXlsProcessor
 *
 */
public class MergeXlsProcessor implements XlsProcessor {
	protected static final Log logger = LogFactory.getLog(MergeXlsProcessor.class);

	protected static ThreadLocal<Stack<String>> tokenThreadLocal = new ThreadLocal<Stack<String>>();

	protected static ThreadLocal<Stack<String>> xmlTagThreadLocal = new ThreadLocal<Stack<String>>();

	protected static ThreadLocal<Stack<JSONObject>> jsonDataThreadLocal = new ThreadLocal<Stack<JSONObject>>();

	protected Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
		Cell cell = null;
		Row row = sheet.getRow(rowIndex);
		if (row != null) {
			cell = row.getCell(colIndex);
		}
		return cell;
	}

	protected String getCellComment(Sheet sheet, int rowIndex, int colIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row != null) {
			Cell cell = row.getCell(colIndex);
			if (cell != null && cell.getCellComment() != null && cell.getCellComment().getString() != null) {
				return cell.getCellComment().getString().getString();
			}
		}
		return null;
	}

	protected String getCellValue(Sheet sheet, int rowIndex, int colIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row != null) {
			Cell cell = row.getCell(colIndex);
			if (cell != null) {
				CellType type = cell.getCellTypeEnum();
				if (type == CellType.NUMERIC) {
					return String.valueOf(cell.getNumericCellValue());
				} else if (type == CellType.FORMULA) {
					return "";
				}
				return cell.getStringCellValue();
			}
		}
		return null;
	}

	protected JSONObject getCurrentJsonData() {
		if (jsonDataThreadLocal.get() != null && !jsonDataThreadLocal.get().isEmpty()) {
			return jsonDataThreadLocal.get().pop();
		}
		return null;
	}

	protected String getCurrentToken() {
		if (tokenThreadLocal.get() != null && !tokenThreadLocal.get().isEmpty()) {
			return tokenThreadLocal.get().pop();
		}
		return null;
	}

	protected String getCurrentXmlTag() {
		if (xmlTagThreadLocal.get() != null && !xmlTagThreadLocal.get().isEmpty()) {
			return xmlTagThreadLocal.get().pop();
		}
		return null;
	}

	/**
	 * <xls:merge hiddenBeginRow="1">
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 */
	protected boolean isBeginRow(Sheet sheet, int rowIndex, int colIndex) {
		String text = this.getCellValue(sheet, rowIndex, colIndex);
		if (StringUtils.isNotEmpty(text)) {
			text = text.trim();
			// logger.debug("text:"+text);
			// <xls:merge hiddenBeginRow="1">
			if (StringUtils.startsWith(text, "<xls:merge")) {
				String tmp = text.substring(10, text.length() - 1);
				JSONObject json = new JSONObject();
				StringTokenizer st = new StringTokenizer(tmp, " ");
				while (st.hasMoreTokens()) {
					String item = st.nextToken();
					if (StringUtils.isNotEmpty(item) && StringUtils.contains(item, "=")) {
						String key = item.substring(0, item.indexOf("="));
						String value = item.substring(item.indexOf("=") + 1, item.length());
						value = StringTools.replace(value, "\"", "");
						json.put(key, value);
						// logger.debug(key + "=" + value);
					}
				}
				// logger.debug("json=" + json.size());
				this.setCurrentXmlTag("xml");
				this.setCurrentJsonData(json);
				return true;
			}
		}
		return false;
	}

	/**
	 * </xls:merge>
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 */
	protected boolean isEndRow(Sheet sheet, int rowIndex, int colIndex) {
		String text = this.getCellValue(sheet, rowIndex, colIndex);
		if (StringUtils.isNotEmpty(text)) {
			text = text.trim();

			if (StringUtils.equals(text, "</xls:merge>")) {
				if (StringUtils.equals(this.getCurrentXmlTag(), "xml")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Excel后置处理器，可读取模板信息对Excel目标文件进行加工处理
	 * 
	 * @param template
	 *            Excel模板
	 * @param workbook
	 *            Excel目标文件
	 */
	public void process(Workbook workbook) {
		logger.debug("-----------------MergeXlsProcessor------------------");
		try {
			int sheetCount = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetCount; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				/**
				 * 遍历每个sheet所有的行
				 */
				int rowCount = sheet.getPhysicalNumberOfRows();
				for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					if (row == null) {
						continue;
					}
					boolean isMerge = false;
					int endIndex = 0;
					/**
					 * 遍历每一行的所有列
					 */
					int colCount = row.getPhysicalNumberOfCells();
					for (int colIndex = 0; colIndex < colCount; colIndex++) {
						boolean isBegin = isBeginRow(sheet, rowIndex, colIndex);
						if (isBegin) {
							// 找需要合并的同一列的结束单元格
							for (int k = rowIndex + 1; k < rowCount; k++) {
								boolean isEnd = this.isEndRow(sheet, k, colIndex);
								if (isEnd) {
									// 合并单元格
									CellRangeAddress newMergedRegion = new CellRangeAddress(rowIndex + 1, k - 1,
											colIndex, colIndex);
									sheet.addMergedRegion(newMergedRegion);
									isMerge = true;
									endIndex = k;
									setCellValue(sheet, rowIndex, colIndex, "");
									setCellValue(sheet, k, colIndex, "");
									break;
								}
							}
						}
					}
					if (isMerge) {
						row.setZeroHeight(true);
						sheet.getRow(endIndex).setZeroHeight(true);
						JSONObject json = this.getCurrentJsonData();
						if (json != null) {
							// logger.debug("json->" + json);
							if (json.containsKey("hiddenBeginRow")) {
								int hiddenBeginRow = json.getIntValue("hiddenBeginRow");
								// logger.debug("hiddenBeginRow:" +
								// hiddenBeginRow);
								for (int a = 1; a <= hiddenBeginRow; a++) {
									Row rowxy = sheet.getRow(rowIndex + a);
									if (rowxy != null) {
										rowxy.setZeroHeight(true);
									}
								}
							}
							if (json.containsKey("hiddenEndRow")) {
								int hiddenEndRow = json.getIntValue("hiddenEndRow");
								for (int b = 1; b <= hiddenEndRow; b++) {
									Row rowxy = sheet.getRow(endIndex - b);
									if (rowxy != null) {
										rowxy.setZeroHeight(true);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.removeCurrentTag();
			this.removeCurrentToken();
		}
	}

	protected void removeCurrentTag() {
		xmlTagThreadLocal.set(null);
		jsonDataThreadLocal.set(null);
		xmlTagThreadLocal.remove();
		jsonDataThreadLocal.remove();
	}

	protected void removeCurrentToken() {
		tokenThreadLocal.set(null);
		tokenThreadLocal.remove();
	}

	protected void setCellValue(Sheet sheet, int rowIndex, int colIndex, String value) {
		Row row = sheet.getRow(rowIndex);
		if (row != null) {
			Cell cell = row.getCell(colIndex);
			if (cell != null) {
				cell.setCellValue(value);
			}
		}
	}

	protected void setCurrentJsonData(JSONObject value) {
		Stack<JSONObject> stack = jsonDataThreadLocal.get();
		if (stack == null) {
			stack = new Stack<JSONObject>();
			jsonDataThreadLocal.set(stack);
		}
		stack.push(value);
		jsonDataThreadLocal.set(stack);
	}

	protected void setCurrentToken(String value) {
		Stack<String> stack = tokenThreadLocal.get();
		if (stack == null) {
			stack = new Stack<String>();
			tokenThreadLocal.set(stack);
		}
		stack.push(value);
		tokenThreadLocal.set(stack);
	}

	protected void setCurrentXmlTag(String value) {
		Stack<String> stack = xmlTagThreadLocal.get();
		if (stack == null) {
			stack = new Stack<String>();
			xmlTagThreadLocal.set(stack);
		}
		stack.push(value);
		xmlTagThreadLocal.set(stack);
	}
}
