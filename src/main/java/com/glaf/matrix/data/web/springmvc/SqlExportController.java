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

package com.glaf.matrix.data.web.springmvc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;

import com.glaf.matrix.data.bean.SqlQueryBean;
import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.service.SqlDefinitionService;
import com.glaf.matrix.data.util.ByteArrayDataCache;

@Controller("/sql/export")
@RequestMapping("/sql/export")
public class SqlExportController {

	protected static final Log logger = LogFactory.getLog(SqlExportController.class);

	protected IDatabaseService databaseService;

	protected SqlDefinitionService sqlDefinitionService;

	public SqlExportController() {

	}

	@ResponseBody
	@RequestMapping("/exportXls")
	public void exportXls(HttpServletRequest request, HttpServletResponse response) {
		SqlDefinition sqlDefinition = null;
		long id = RequestUtils.getLong(request, "id");
		String code = request.getParameter("code");
		String uuid = request.getParameter("uuid");
		if (StringUtils.isNotEmpty(uuid)) {
			sqlDefinition = sqlDefinitionService.getSqlDefinitionByUUID(uuid);
		}
		if (StringUtils.isNotEmpty(code)) {
			sqlDefinition = sqlDefinitionService.getSqlDefinitionByCode(code);
		}
		if (sqlDefinition == null) {
			if (id > 0) {
				sqlDefinition = sqlDefinitionService.getSqlDefinition(id);
			}
		}
		if (sqlDefinition != null && StringUtils.isNotEmpty(sqlDefinition.getExportTemplate())) {
			String filename = SystemProperties.getConfigRootPath() + sqlDefinition.getExportTemplate();
			byte[] data = null;
			try {
				data = ByteArrayDataCache.get(sqlDefinition.getExportTemplate());
				if (data == null) {
					logger.debug("read excel template:" + filename);
					data = FileUtils.getBytes(filename);
					if (data != null) {
						ByteArrayDataCache.put(sqlDefinition.getExportTemplate(), data);
					}
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				logger.error(ex);
			}
			if (data != null) {
				SqlQueryBean queryBean = new SqlQueryBean();
				Workbook workbook = null;
				InputStream bis = null;
				ByteArrayInputStream bais = null;
				ByteArrayOutputStream baos = null;
				BufferedOutputStream bos = null;
				try {
					JSONObject result = queryBean.toJson(sqlDefinition, request);
					bais = new ByteArrayInputStream(data);
					bis = new BufferedInputStream(bais);
					baos = new ByteArrayOutputStream();
					bos = new BufferedOutputStream(baos);

					Context context2 = PoiTransformer.createInitialContext();

					Map<String, Object> params = RequestUtils.getParameterMap(request);
					Set<Entry<String, Object>> entrySet = params.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String key = entry.getKey();
						Object value = entry.getValue();
						context2.putVar(key, value);
						// logger.debug(key);
					}

					JSONArray array = result.getJSONArray("rows");
					if (array != null && array.size() > 0) {
						List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
						JSONObject json = null;
						Iterator<Entry<String, Object>> iterator = null;
						for (int i = 0, len = array.size(); i < len; i++) {
							json = array.getJSONObject(i);
							Map<String, Object> dataMap = new HashMap<String, Object>();
							iterator = json.entrySet().iterator();
							while (iterator.hasNext()) {
								Entry<String, Object> entry = iterator.next();
								String key = (String) entry.getKey();
								Object value = entry.getValue();
								if (value != null) {
									dataMap.put(key, value);
								}
							}
							dataList.add(dataMap);
						}
						context2.putVar("items", dataList);
						org.jxls.util.JxlsHelper.getInstance().processTemplate(bis, bos, context2);

						bos.flush();
						baos.flush();
						data = baos.toByteArray();
						ResponseUtils.download(request, response, data,
								"export" + DateUtils.getNowYearMonthDayHHmmss() + ".xls");
					}
				} catch (Exception ex) {
					// ex.printStackTrace();
					logger.error(ex);
				} finally {
					data = null;
					IOUtils.closeQuietly(bis);
					IOUtils.closeQuietly(bais);
					IOUtils.closeQuietly(baos);
					IOUtils.closeQuietly(bos);
					if (workbook != null) {
						try {
							workbook.close();
						} catch (IOException e) {
						}
						workbook = null;
					}
				}
			}
		}
	}

	@javax.annotation.Resource
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@javax.annotation.Resource
	public void setSqlDefinitionService(SqlDefinitionService sqlDefinitionService) {
		this.sqlDefinitionService = sqlDefinitionService;
	}

}
