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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.glaf.base.modules.sys.model.SysTree;
import com.glaf.base.modules.sys.service.SysTreeService;
import com.glaf.core.context.ContextFactory;
import com.glaf.heathcare.domain.FoodComposition;
import com.glaf.heathcare.service.FoodCompositionService;

public class FoodCompositionImport {
	protected final static Log logger = LogFactory.getLog(FoodCompositionImport.class);

	protected final static DecimalFormat df = new DecimalFormat("######0.00");

	public static void main(String[] args) throws Exception {
		// System.setProperty("config.path", ".");
		// com.glaf.core.context.ApplicationContext.setAppPath("E:/iss_develop/Java/glaf-heathcare/WebContent");
		// java.io.InputStream inputStream = new
		// java.io.FileInputStream("./doc/import.xls");
		// FoodCompositionImport bean = new FoodCompositionImport();
		// bean.doImport(inputStream);
		System.out.println(Double.parseDouble("0.759999990463256"));
	}

	public void doImport(java.io.InputStream inputStream) {
		logger.debug("----------------UserImporter------------------");
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(inputStream);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
				}
			}
		}
		XSSFSheet sheet = wb.getSheetAt(0);
		SysTreeService sysTreeService = ContextFactory.getBean("sysTreeService");
		SysTree root = sysTreeService.getSysTreeByCode("FoodCategory");
		if (root != null) {
			List<SysTree> foodCategories = sysTreeService.getSysTreeList(root.getId());
			List<Long> nodeIds = new ArrayList<Long>();
			for (SysTree t : foodCategories) {
				nodeIds.add(t.getId());
			}
			List<FoodComposition> foodList = new ArrayList<FoodComposition>();
			int rows = sheet.getLastRowNum();
			for (int rowIndex = 3; rowIndex <= rows; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				String name = this.getCellValue(row.getCell(1));
				String node = this.getCellValue(row.getCell(19));
				if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(node) && StringUtils.isNumeric(node)) {
					logger.debug("name:" + name);
					long nodeId = Long.parseLong(node);
					if (nodeIds.contains(nodeId)) {
						FoodComposition model = new FoodComposition();
						model.setCreateBy("admin");
						model.setName(name);
						model.setNodeId(nodeId);
						model.setSortNo(rowIndex);
						model.setRadical(this.getDoubleValue(row.getCell(2)));// 可食部分
						model.setHeatEnergy(this.getDoubleValue(row.getCell(3)));// 能量
						model.setProtein(this.getDoubleValue(row.getCell(5)));// 蛋白质
						model.setFat(this.getDoubleValue(row.getCell(6)));// 脂肪
						model.setCarbohydrate(this.getDoubleValue(row.getCell(8)));// 碳水化合物
						model.setVitaminA(this.getDoubleValue(row.getCell(9)));// VitaminA
						model.setVitaminB1(this.getDoubleValue(row.getCell(10)));// VitaminB1
						model.setVitaminB2(this.getDoubleValue(row.getCell(11)));// VitaminB2
						model.setNicotinicCid(this.getDoubleValue(row.getCell(12)));// 烟酸
						model.setVitaminE(this.getDoubleValue(row.getCell(13)));// VitaminE
						model.setIron(this.getDoubleValue(row.getCell(15)));// 铁
						model.setCalcium(this.getDoubleValue(row.getCell(16)));// 钙
						model.setVitaminC(this.getDoubleValue(row.getCell(17)));// VitaminC
						foodList.add(model);
					}
				}
			}

			FoodCompositionService foodCompositionService = ContextFactory
					.getBean("com.glaf.heathcare.service.foodCompositionService");
			foodCompositionService.saveAll(foodList);
		}
	}

	protected String getCellValue(XSSFCell cell) {
		String cellValue = null;
		if (cell != null) {
			cellValue = cell.getStringCellValue();
			if (cellValue == null) {
				cellValue = "";
			}
		}
		return cellValue;
	}

	protected Double getDoubleValue(XSSFCell cell) {
		String cellValue = this.getCellValue(cell);
		if (cellValue != null && StringUtils.isNotEmpty(cellValue)) {
			try {
				double value = Double.parseDouble(cellValue);
				// value = Math.round(value * 10000 ) / 10000.00D;
				value = Double.parseDouble(df.format(value));
				return value;
			} catch (Exception ex) {
			}
		}
		return null;
	}

}
