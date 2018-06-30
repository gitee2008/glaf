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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.w3c.dom.Document;

public class ExcelToHtml {

	public static void main(String args[]) throws Exception {
		String path = "./output/";
		String file = "test.xls";
		InputStream input = new FileInputStream(path + file);
		String content = new String(toHtml(input), "UTF-8");
		FileUtils.writeStringToFile(new File(path, "exportExcel.html"), content, "utf-8");
	}

	public static byte[] toHtml(InputStream input) throws Exception {
		return toHtml(input, 2);
	}

	public static byte[] toHtml(InputStream input, int precision) throws Exception {
		HSSFWorkbook workbook = null;
		FormulaEvaluator evaluator = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		try {
			workbook = new HSSFWorkbook(input);
			evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			evaluator.evaluateAll();
			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
				HSSFRow row = sheet.getRow(rowIndex);
				int cols = row.getPhysicalNumberOfCells();
				for (int colIndex = 0; colIndex < cols; colIndex++) {
					HSSFCell cell = row.getCell(colIndex);
					if (cell != null) {
						String text = ExcelUtils.getValue(evaluator, cell, precision);
						cell.setCellValue(text);
					}
				}
			}
			ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
			excelToHtmlConverter.setOutputColumnHeaders(false);
			excelToHtmlConverter.setOutputRowNumbers(false);
			excelToHtmlConverter.processWorkbook(workbook);
			Document htmlDocument = excelToHtmlConverter.getDocument();
			baos = new ByteArrayOutputStream();
			bos = new BufferedOutputStream(baos);
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(bos);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			serializer.transform(domSource, streamResult);
			IOUtils.closeStream(bos);
			IOUtils.closeStream(baos);
			return baos.toByteArray();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeStream(bos);
			IOUtils.closeStream(baos);
			if (evaluator != null) {
				evaluator.clearAllCachedResultValues();
				evaluator = null;
			}
			if (workbook != null) {
				workbook.close();
				workbook = null;
			}
		}
	}
}