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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.security.LoginContext;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.Paging;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.StringTools;

import com.glaf.matrix.data.domain.SqlCriteria;
import com.glaf.matrix.data.domain.SqlDefinition;
import com.glaf.matrix.data.helper.SqlCriteriaHelper;
import com.glaf.matrix.data.query.SqlCriteriaQuery;
import com.glaf.matrix.data.service.SqlCriteriaService;

public class SqlQueryBean {

	protected static final Log logger = LogFactory.getLog(SqlQueryBean.class);

	protected ITablePageService tablePageService;

	protected SqlCriteriaService sqlCriteriaService;

	public SqlQueryBean() {

	}

	public SqlCriteriaService getSqlCriteriaService() {
		if (sqlCriteriaService == null) {
			sqlCriteriaService = ContextFactory.getBean("sqlCriteriaService");
		}
		return sqlCriteriaService;
	}

	public ITablePageService getTablePageService() {
		if (tablePageService == null) {
			tablePageService = ContextFactory.getBean("tablePageService");
		}
		return tablePageService;
	}

	public SqlExecutor replaceInSQLParas(String str, Map<String, Object> params) {
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.setSql(str);
		if (str == null || params == null) {
			return sqlExecutor;
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		Map<String, Object> dataMap = new LowerLinkedMap();
		dataMap.putAll(params);
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int left = 0;
		int end = 0;
		boolean flag = false; // 匹配标志
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '(') {
				sb.append(str.substring(end, i));
				left = i;
				end = i;
			}
			if (str.charAt(i) == '#' && str.charAt(i + 1) == '{') {
				begin = i + 2;
				flag = true;
			}
			if (flag && str.charAt(i) == '}') {
				String temp = str.substring(begin, i);
				temp = temp.toLowerCase();
				String name = temp;
				String type = "";
				if (temp.indexOf(":") != -1) {
					name = temp.substring(0, temp.lastIndexOf(":"));
					type = temp.substring(temp.lastIndexOf(":") + 1, temp.length());
				}
				name = name.toLowerCase();
				if (dataMap.get(name) != null) {
					Object val = dataMap.get(name);
					String sx = val.toString();
					if (StringUtils.equalsIgnoreCase(type, "int")) {
						parameter.put(name, Integer.parseInt(sx));
					} else if (StringUtils.equalsIgnoreCase(type, "long")) {
						parameter.put(name, Long.parseLong(sx));
					} else if (StringUtils.equalsIgnoreCase(type, "double")) {
						parameter.put(name, Double.parseDouble(sx));
					} else if (StringUtils.equalsIgnoreCase(type, "date")) {
						if (val instanceof Date) {
							parameter.put(name, val);
						} else {
							parameter.put(name, DateUtils.toDate(sx));
						}
					} else {
						parameter.put(name, val);
					}

					sb.append(str.substring(left, begin - 2));
					sb.append("#{").append(name).append("}");

					// String value = dataMap.get(temp).toString();
					// sb.append(str.substring(left, begin - 2));
					// sb.append(value);
					end = i + 1;
					flag = false;
				} else {
					sb.append(str.charAt(left));
					sb.append(" 1=1 ");
					end = str.indexOf(")", i);
				}
			}
			if (i == str.length() - 1) {
				sb.append(str.substring(end, i + 1));
			}
		}
		String newString = sb.toString();
		sqlExecutor.setSql(newString);
		sqlExecutor.setParameter(parameter);
		return sqlExecutor;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson(SqlDefinition sqlDefinition, HttpServletRequest request) throws IOException {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Map<String, Object> params = RequestUtils.getParameterMap(request);
		params.put("tenantId", loginContext.getTenantId());
		params.put("tableSuffix", IdentityFactory.getTenantHash(loginContext.getTenantId()));
		logger.debug("params:" + params);
		JSONObject result = new JSONObject();

		int start = 0;
		int limit = 10;

		int pageNo = ParamUtils.getInt(params, "page");
		limit = ParamUtils.getInt(params, "rows");
		start = (pageNo - 1) * limit;

		if (start < 0) {
			start = 0;
		}

		if (limit <= 0) {
			limit = Paging.DEFAULT_PAGE_SIZE;
		}

		if (request.getAttribute("exportXls") != null) {
			limit = 50000;
		}

		SqlCriteriaQuery query = new SqlCriteriaQuery();
		query.businessKey(sqlDefinition.getUuid());
		query.moduleId("sys_sql");
		List<SqlCriteria> sqlCriterias = getSqlCriteriaService().list(query);
		if (sqlCriterias != null && !sqlCriterias.isEmpty()) {
			for (SqlCriteria col : sqlCriterias) {
				if (StringUtils.equals(col.getColumnType(), "String")) {
					String value = request.getParameter(col.getParamName() + "_enc");
					if (StringUtils.isNotEmpty(value)) {
						params.put(col.getParamName(), RequestUtils.decodeString(value));
					}
				}
			}
		}

		StringBuilder sqlBuffer = new StringBuilder();
		StringBuilder sqlCountBuffer = new StringBuilder();

		SqlExecutor sqlExecutor = null;

		sqlDefinition.setSql(StringTools.replace(sqlDefinition.getSql(), "${tableSuffix}",
				String.valueOf(IdentityFactory.getTenantHash(loginContext.getTenantId()))));
		sqlDefinition.setCountSql(StringTools.replace(sqlDefinition.getCountSql(), "${tableSuffix}",
				String.valueOf(IdentityFactory.getTenantHash(loginContext.getTenantId()))));
		sqlExecutor = this.replaceInSQLParas(sqlDefinition.getSql(), params);
		sqlBuffer.append(sqlExecutor.getSql());
		params.putAll((Map<String, Object>) sqlExecutor.getParameter());

		sqlExecutor = this.replaceInSQLParas(sqlDefinition.getCountSql(), params);
		sqlCountBuffer.append(sqlExecutor.getSql());

		SqlCriteriaHelper helper = new SqlCriteriaHelper();
		sqlExecutor = helper.buildMyBatisSql("sys_sql", sqlDefinition.getUuid(), params);
		if (sqlExecutor.getSql() != null && sqlExecutor.getParameter() != null) {
			String sql = sqlBuffer.toString();
			if (sql.indexOf("${SQL}") != -1) {
				sql = StringTools.replace(sql, "${SQL}", sqlExecutor.getSql());
				sqlBuffer.delete(0, sqlBuffer.length());
				sqlBuffer.append(sql);
			} else {
				sqlBuffer.append(sqlExecutor.getSql());
			}

			sql = sqlCountBuffer.toString();
			if (sql.indexOf("${SQL}") != -1) {
				sql = StringTools.replace(sql, "${SQL}", sqlExecutor.getSql());
				sqlCountBuffer.delete(0, sqlCountBuffer.length());
				sqlCountBuffer.append(sql);
			} else {
				sqlCountBuffer.append(sqlExecutor.getSql());
			}

			params.putAll((Map<String, Object>) sqlExecutor.getParameter());
		}

		try {
			int total = getTablePageService().getQueryCount(sqlCountBuffer.toString(), params);
			if (total > 0) {
				List<Map<String, Object>> dataList = getTablePageService().getListData(sqlBuffer.toString(), params,
						start, limit);
				if (dataList != null && !dataList.isEmpty()) {
					JSONArray array = new JSONArray();
					Map<String, Object> dataMap = null;
					int index = start;
					for (Map<String, Object> rowMap : dataList) {
						dataMap = new LowerLinkedMap();
						dataMap.putAll(rowMap);
						index++;
						JSONObject json = new JSONObject();
						Set<Entry<String, Object>> entrySet = dataMap.entrySet();
						for (Entry<String, Object> entry : entrySet) {
							String key = entry.getKey();
							Object value = entry.getValue();
							if (key != null && value != null) {
								json.put(key, value);
								json.put(key.toUpperCase(), value);
								if (value instanceof Date) {
									Date date = (Date) value;
									json.put(key + "_date", DateUtils.getDate(date));
									json.put(key + "_datetime", DateUtils.getDateTime(date));
									json.put(key.toUpperCase() + "_DATE", DateUtils.getDate(date));
									json.put(key.toUpperCase() + "_DATETIME", DateUtils.getDateTime(date));
								}
							}
						}
						json.put("startIndex", index);
						array.add(json);
					}
					result.put("rows", array);
				}
			}

			result.put("total", total);
			result.put("totalCount", total);
			result.put("totalRecords", total);
			result.put("start", start);
			result.put("startIndex", start);
			result.put("limit", limit);
			result.put("pageSize", limit);

		} catch (Exception ex) {
			logger.error(ex);
		}

		// logger.debug(result.toJSONString());
		return result;
	}

}
