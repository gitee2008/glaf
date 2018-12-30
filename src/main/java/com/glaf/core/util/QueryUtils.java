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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.SystemConfig;
import com.glaf.core.entity.SqlExecutor;

public class QueryUtils {
	protected static final Log logger = LogFactory.getLog(QueryUtils.class);

	public final static String newline = System.getProperty("line.separator");

	public static String getIntegerParameterSQLCondition(Collection<Integer> rowKeys, String alias, String columnName) {
		if (rowKeys == null || rowKeys.size() <= 0) {
			return "";
		}
		int index = 1;
		StringBuilder conditionBuffer = new StringBuilder();
		conditionBuffer.append(" and ( ").append(alias).append(".").append(columnName).append(" in (-1) ");
		StringBuilder idsBuffer = new StringBuilder();
		Iterator<Integer> iterator = rowKeys.iterator();
		while (iterator.hasNext()) {
			Integer x = iterator.next();
			idsBuffer.append(x);
			if (index == 500) {
				conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
						.append(idsBuffer.toString()).append(")");
				index = 0;
				idsBuffer.delete(0, idsBuffer.length());
			}
			if (iterator.hasNext() && index > 0) {
				idsBuffer.append(",");
			}
			index++;
		}
		if (idsBuffer.length() > 0) {
			conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
					.append(idsBuffer.toString()).append(")");
			idsBuffer.delete(0, idsBuffer.length());
		}
		conditionBuffer.append(" ) ");
		return conditionBuffer.toString();
	}

	public static String getLongParameterSQLCondition(Collection<Long> rowKeys, String alias, String columnName) {
		if (rowKeys == null || rowKeys.size() <= 0) {
			return "";
		}
		int index = 1;
		StringBuilder conditionBuffer = new StringBuilder();
		conditionBuffer.append(" and ( ").append(alias).append(".").append(columnName).append(" in (-1) ");
		StringBuilder idsBuffer = new StringBuilder();
		Iterator<Long> iterator = rowKeys.iterator();
		while (iterator.hasNext()) {
			Long x = iterator.next();
			idsBuffer.append(x);
			if (index == 500) {
				conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
						.append(idsBuffer.toString()).append(")");
				index = 0;
				idsBuffer.delete(0, idsBuffer.length());
			}
			if (iterator.hasNext() && index > 0) {
				idsBuffer.append(",");
			}
			index++;
		}
		if (idsBuffer.length() > 0) {
			conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
					.append(idsBuffer.toString()).append(")");
			idsBuffer.delete(0, idsBuffer.length());
		}
		conditionBuffer.append(" ) ");
		return conditionBuffer.toString();
	}

	public static String getNumSQLCondition(Collection<Object> rowKeys, String alias, String columnName) {
		if (rowKeys == null || rowKeys.size() <= 0) {
			return "";
		}
		int index = 1;
		StringBuilder conditionBuffer = new StringBuilder();
		conditionBuffer.append(" and ( ").append(alias).append(".").append(columnName).append(" in (-1) ");
		StringBuilder idsBuffer = new StringBuilder();
		Iterator<Object> iterator = rowKeys.iterator();
		while (iterator.hasNext()) {
			Object x = iterator.next();
			idsBuffer.append(x);
			if (index == 500) {
				conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
						.append(idsBuffer.toString()).append(")");
				index = 0;
				idsBuffer.delete(0, idsBuffer.length());
			}
			if (iterator.hasNext() && index > 0) {
				idsBuffer.append(",");
			}
			index++;
		}
		if (idsBuffer.length() > 0) {
			conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
					.append(idsBuffer.toString()).append(")");
			idsBuffer.delete(0, idsBuffer.length());
		}
		conditionBuffer.append(" ) ");
		return conditionBuffer.toString();
	}

	public static String getNumSQLCondition(List<String> rowKeys, String alias, String columnName) {
		if (rowKeys == null || rowKeys.size() <= 0) {
			return "";
		}
		int index = 1;
		StringBuffer conditionBuffer = new StringBuffer();
		conditionBuffer.append(" and ( ").append(alias).append(".").append(columnName).append(" in (0) ");
		StringBuffer idsBuffer = new StringBuffer();
		Iterator<String> iterator = rowKeys.iterator();
		while (iterator.hasNext()) {
			String x = iterator.next();
			idsBuffer.append(x);
			if (index == 500) {
				conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
						.append(idsBuffer.toString()).append(")");
				index = 0;
				idsBuffer.delete(0, idsBuffer.length());
			}
			if (iterator.hasNext() && index > 0) {
				idsBuffer.append(",");
			}
			index++;
		}
		if (idsBuffer.length() > 0) {
			conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
					.append(idsBuffer.toString()).append(")");
			idsBuffer.delete(0, idsBuffer.length());
		}
		conditionBuffer.append(" ) ");
		return conditionBuffer.toString();
	}

	public static String getSelectFilter(String name) {
		return getSelectFilter(name, true);
	}

	public static String getSelectFilter(String name, boolean isString) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(newline).append("		 <select id=\"x_filter_").append(name).append("\" name=\"x_filter_")
				.append(name).append("\"	class=\"span2\">");
		buffer.append(newline).append("			<option value=\"\">----请选择----</option>");
		buffer.append(newline).append("			<option value=\"=\">等于</option>");
		buffer.append(newline).append("			<option value=\"!=\">不等于</option>");
		if (!isString) {
			buffer.append(newline).append("			<option value=\">=\">大于或等于</option>");
			buffer.append(newline).append("			<option value=\">\">大于</option>");
			buffer.append(newline).append("			<option value=\"<=\">小于或等于</option>");
			buffer.append(newline).append("			<option value=\"<\">小于</option>");
		} else {
			buffer.append(newline).append("			<option value=\"LIKE\">包含</option>");
			buffer.append(newline).append("			<option value=\"NOT LIKE\">不包含</option>");
		}
		buffer.append(newline).append("		 </select>");
		return buffer.toString();
	}

	public static String getSQLCondition(Collection<String> rowKeys, String alias, String columnName) {
		if (rowKeys == null || rowKeys.size() <= 0) {
			return "";
		}
		int index = 1;
		StringBuilder conditionBuffer = new StringBuilder();
		conditionBuffer.append(" and ( ").append(alias).append(".").append(columnName).append(" in ('-1') ");
		StringBuilder idsBuffer = new StringBuilder();
		Iterator<String> iterator = rowKeys.iterator();
		while (iterator.hasNext()) {
			String x = iterator.next();
			idsBuffer.append('\'').append(x).append('\'');
			if (index == 500) {
				conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
						.append(idsBuffer.toString()).append(")");
				index = 0;
				idsBuffer.delete(0, idsBuffer.length());
			}
			if (iterator.hasNext() && index > 0) {
				idsBuffer.append(",");
			}
			index++;
		}
		if (idsBuffer.length() > 0) {
			conditionBuffer.append(" or ").append(alias).append(".").append(columnName).append(" in (")
					.append(idsBuffer.toString()).append(")");
			idsBuffer.delete(0, idsBuffer.length());
		}
		conditionBuffer.append(" ) ");
		return conditionBuffer.toString();
	}

	public static boolean isNotEmpty(Map<String, Object> paramMap, String name) {
		if (paramMap != null && paramMap.get(name) != null) {
			Object obj = paramMap.get(name);
			if (obj instanceof Collection<?>) {
				Collection<?> rows = (Collection<?>) obj;
				if (rows != null && rows.size() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static Map<String, Object> lowerKeyMap(Map<String, Object> paramMap) {
		Map<String, Object> dataMap = new LowerLinkedMap();
		dataMap.putAll(paramMap);
		return dataMap;
	}

	public static void main(String[] args) {
		String sql = " select * from USER_${xx} where 1=1 and ( status = ${status} ) and ( userId in (${userIds})) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xx", "1");
		Collection<String> userIds = new ArrayList<String>();
		userIds.add("kermit");
		userIds.add("joe");
		params.put("userIds", userIds);
		System.out.println(QueryUtils.replaceSQLVars(sql, params));
		System.out.println(QueryUtils.replaceDollarSQLParas(sql, params));
	}

	public static String replaceBlankParas(String str, Map<String, Object> params) {
		if (str == null || params == null) {
			return str;
		}
		Map<String, Object> dataMap = lowerKeyMap(params);
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int end = 0;
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '#' && str.charAt(i + 1) == '{') {
				sb.append(str.substring(end, i));
				begin = i + 2;
				flag = true;
			}
			if (flag && str.charAt(i) == '}') {
				String temp = str.substring(begin, i);
				temp = temp.toLowerCase();
				Object value = dataMap.get(temp);
				if (value != null) {
					if (value instanceof java.util.Date) {
						java.util.Date date = (java.util.Date) value;
						sb.append(DateUtils.getDateTime(date));
					} else {
						sb.append(value);
					}
					end = i + 1;
					flag = false;
				} else {
					sb.append("");
					end = i + 1;
					flag = false;
				}
			}
			if (i == str.length() - 1) {
				sb.append(str.substring(end, i + 1));
			}
		}
		return sb.toString();
	}

	public static String replaceDollarSQLParas(String str, Map<String, Object> params) {
		if (str == null || params == null) {
			return str;
		}
		Map<String, Object> dataMap = lowerKeyMap(params);
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
			if (str.charAt(i) == '$' && str.charAt(i + 1) == '{') {
				begin = i + 2;
				flag = true;
			}
			if (flag && str.charAt(i) == '}') {
				String temp = str.substring(begin, i);
				temp = temp.toLowerCase();
				if (dataMap.get(temp) != null) {
					Object value = dataMap.get(temp);
					if (value instanceof Collection) {
						sb.append(str.substring(left, begin - 2));
						Collection<?> coll = (Collection<?>) value;
						int index = 0;
						for (Object val : coll) {
							if (val instanceof String) {
								if (val.toString().indexOf("'") != -1) {
									sb.append(val);
								} else {
									sb.append("'").append(val).append("'");
								}
							} else {
								sb.append(val);
							}
							if (index < coll.size() - 1) {
								sb.append(", ");
							}
							index++;
						}
					} else {
						String s = value.toString();
						sb.append(str.substring(left, begin - 2));
						sb.append(s);
					}
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
		return newString;
	}

	public static String replaceInSQLParas(String str, Map<String, Object> params) {
		if (str == null || params == null) {
			return str;
		}
		Map<String, Object> dataMap = lowerKeyMap(params);
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
				if (dataMap.get(temp) != null) {
					Object value = dataMap.get(temp);
					if (value instanceof Collection) {
						sb.append(str.substring(left, begin - 2));
						Collection<?> coll = (Collection<?>) value;
						int index = 0;
						for (Object val : coll) {
							if (val instanceof String) {
								if (val.toString().indexOf("'") != -1) {
									sb.append(val);
								} else {
									sb.append("'").append(val).append("'");
								}
							} else {
								sb.append(val);
							}
							if (index < coll.size() - 1) {
								sb.append(", ");
							}
							index++;
						}
					} else {
						String s = value.toString();
						sb.append(str.substring(left, begin - 2));
						sb.append(s);
					}
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
		return newString;
	}

	public static SqlExecutor replaceMyBatisInSQLParas(String str, Map<String, Object> params) {
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.setSql(str);
		if (str == null || params == null) {
			return sqlExecutor;
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.putAll(params);
		Map<String, Object> dataMap = new LowerLinkedMap();
		dataMap.putAll(params);
		// logger.debug("->dataMap:" + dataMap);
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
				if (temp.indexOf(":") > 0) {
					name = temp.substring(0, temp.lastIndexOf(":"));
					type = temp.substring(temp.lastIndexOf(":") + 1, temp.length());
				}
				name = name.trim().toLowerCase();
				type = type.trim().toLowerCase();
				// logger.debug("name:" + name + "\t type:" + type);
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
		logger.debug("#sql:" + newString);
		logger.debug("#parameter:" + parameter);
		return sqlExecutor;
	}

	public static String replaceMyTextParas(String str, Map<String, Object> params) {
		if (str == null || params == null) {
			return str;
		}
		Map<String, Object> dataMap = lowerKeyMap(params);
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int end = 0;
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '#' && str.charAt(i + 1) == '{') {
				sb.append(str.substring(end, i));
				begin = i + 2;
				flag = true;
			}
			if (flag && str.charAt(i) == '}') {
				String temp = str.substring(begin, i);
				temp = temp.toLowerCase();
				if (dataMap.get(temp) != null) {
					Object value = dataMap.get(temp);
					if (value instanceof Date) {
						String s = DateUtils.getDate((Date) value);
						sb.append(s);
					} else {
						sb.append(value.toString());
					}
					end = i + 1;
					flag = false;
				} else {
					sb.append("#{").append(temp).append('}');
					end = i + 1;
					flag = false;
				}
			}
			if (i == str.length() - 1) {
				sb.append(str.substring(end, i + 1));
			}
		}
		return sb.toString();
	}

	public static SqlExecutor replaceSQL(String sql, Map<String, Object> params) {
		SqlExecutor sqlExecutor = new SqlExecutor();
		sqlExecutor.setSql(sql);
		if (sql == null || params == null) {
			return sqlExecutor;
		}

		List<Object> values = new java.util.ArrayList<Object>();
		Map<String, Object> dataMap = lowerKeyMap(params);
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int end = 0;
		boolean flag = false;
		for (int i = 0; i < sql.length(); i++) {
			if (sql.charAt(i) == '#' && sql.charAt(i + 1) == '{') {
				sb.append(sql.substring(end, i));
				begin = i + 2;
				flag = true;
			}
			if (flag && sql.charAt(i) == '}') {
				String temp = sql.substring(begin, i);
				temp = temp.toLowerCase();
				if (dataMap.get(temp) != null) {
					Object value = null;
					if (StringUtils.endsWith(temp, "_integer") || StringUtils.endsWith(temp, "_int")) {
						value = ParamUtils.getIntValue(dataMap, temp);
					} else if (StringUtils.endsWith(temp, "_double") || StringUtils.endsWith(temp, "_float")) {
						value = ParamUtils.getDoubleValue(dataMap, temp);
					} else if (StringUtils.endsWith(temp, "_long")) {
						value = ParamUtils.getLongValue(dataMap, temp);
					} else if (StringUtils.endsWith(temp, "_date")) {
						value = ParamUtils.getDate(dataMap, temp);
					} else {
						value = dataMap.get(temp);
					}
					/**
					 * 如果是Collection参数，必须至少有一个值
					 */
					if (value != null && value instanceof Collection) {
						Collection<?> coll = (Collection<?>) value;
						if (coll != null && !coll.isEmpty()) {
							Iterator<?> iter = coll.iterator();
							while (iter.hasNext()) {
								values.add(iter.next());
								sb.append(" ? ");
								if (iter.hasNext()) {
									sb.append(", ");
								}
							}
						}
					} else {
						sb.append(" ? ");
						values.add(value);
					}
					end = i + 1;
					flag = false;
				} else {
					sb.append(" ? ");
					end = i + 1;
					flag = false;
					values.add(null);
				}
			}
			if (i == sql.length() - 1) {
				sb.append(sql.substring(end, i + 1));
			}
		}
		sqlExecutor.setParameter(values);
		sqlExecutor.setSql(sb.toString());
		return sqlExecutor;
	}

	public static String replaceSQLParas(String str, Map<String, Object> params) {
		if (str == null || params == null) {
			return str;
		}
		Map<String, Object> dataMap = lowerKeyMap(params);
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
				if (dataMap.get(temp) != null) {
					Object value = dataMap.get(temp);
					if (value instanceof Collection) {
						sb.append(str.substring(left, begin - 2));
						Collection<?> coll = (Collection<?>) value;
						int index = 0;
						for (Object val : coll) {
							if (val instanceof String) {
								if (val.toString().indexOf("'") != -1) {
									sb.append(val);
								} else {
									sb.append("'").append(val).append("'");
								}
							} else {
								sb.append(val);
							}
							if (index < coll.size() - 1) {
								sb.append(", ");
							}
							index++;
						}
					} else {
						String s = value.toString();
						sb.append(str.substring(left, begin - 2));
						sb.append(s);
					}
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
		return newString;
	}

	public static String replaceSQLVars(String sql) {
		sql = StringTools.replace(sql, "${now}", SystemConfig.getCurrentYYYYMMDD());
		sql = StringTools.replace(sql, "#{now}", SystemConfig.getCurrentYYYYMMDD());
		sql = StringTools.replace(sql, ExpressionConstants.INPUT_YYYYMM_EXPRESSION, SystemConfig.getInputYYYYMM());
		sql = StringTools.replace(sql, ExpressionConstants.INPUT_YYYYMMDD_EXPRESSION, SystemConfig.getInputYYYYMMDD());
		sql = StringTools.replace(sql, ExpressionConstants.CURRENT_YYYYMM_EXPRESSION, SystemConfig.getCurrentYYYYMM());
		sql = StringTools.replace(sql, ExpressionConstants.CURRENT_YYYYMMDD_EXPRESSION,
				SystemConfig.getCurrentYYYYMMDD());
		return sql;
	}

	public static String replaceSQLVars(String str, Map<String, Object> params) {
		if (str == null || params == null) {
			return str;
		}
		Map<String, Object> dataMap = lowerKeyMap(params);
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int end = 0;
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '$' && str.charAt(i + 1) == '{') {
				sb.append(str.substring(end, i));
				begin = i + 2;
				flag = true;
			}
			if (flag && str.charAt(i) == '}') {
				String temp = str.substring(begin, i);
				temp = temp.toLowerCase();
				if (dataMap.get(temp) != null) {
					String value = dataMap.get(temp).toString();
					sb.append(value);
					end = i + 1;
					flag = false;
				} else {
					sb.append("${").append(temp).append('}');
					end = i + 1;
					flag = false;
				}
			}
			if (i == str.length() - 1) {
				sb.append(str.substring(end, i + 1));
			}
		}
		return sb.toString();
	}

	public static String replaceTextParas(String str, Map<String, Object> params) {
		if (str == null || params == null) {
			return str;
		}
		Map<String, Object> dataMap = lowerKeyMap(params);
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int end = 0;
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '#' && str.charAt(i + 1) == '{') {
				sb.append(str.substring(end, i));
				begin = i + 2;
				flag = true;
			}
			if (flag && str.charAt(i) == '}') {
				String temp = str.substring(begin, i);
				temp = temp.toLowerCase();
				if (dataMap.get(temp) != null) {
					String value = dataMap.get(temp).toString();
					sb.append(value);
					end = i + 1;
					flag = false;
				} else {
					sb.append("#{").append(temp).append('}');
					end = i + 1;
					flag = false;
				}
			}
			if (i == str.length() - 1) {
				sb.append(str.substring(end, i + 1));
			}
		}
		return sb.toString();
	}

	private QueryUtils() {
	}

}