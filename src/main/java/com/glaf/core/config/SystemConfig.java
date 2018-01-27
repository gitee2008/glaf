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

package com.glaf.core.config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.SystemProperty;
import com.glaf.core.domain.util.SystemPropertyJsonFactory;
import com.glaf.core.el.Mvel2ExpressionEvaluator;
import com.glaf.core.service.ISystemPropertyService;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.StringTools;

public class SystemConfig {
	protected static final Log logger = LogFactory.getLog(SystemConfig.class);

	protected static ConcurrentMap<String, SystemProperty> concurrentMap = new ConcurrentHashMap<String, SystemProperty>();

	protected static ConcurrentMap<String, Long> concurrentTimeMap = new ConcurrentHashMap<String, Long>();

	protected static AtomicBoolean loading = new AtomicBoolean(false);

	protected static Configuration conf = BaseConfiguration.create();

	public static final String DEFAULT_ENCODING = "UTF-8";

	public final static String CURR_YYYYMMDD = "#{curr_yyyymmdd}";

	public final static String CURR_YYYYMM = "#{curr_yyyymm}";

	public final static String INPUT_YYYYMMDD = "#{input_yyyymmdd}";

	public final static String INPUT_YYYYMM = "#{input_yyyymm}";

	public final static String LONG_ID = "#{longId}";

	public final static String NOW = "#{now}";

	private static volatile String TOKEN = null;

	private static volatile String SYS_CODE = null;

	private static volatile Integer INT_TOKEN = 0;

	private static volatile AtomicBoolean concurrentAccessLimit = null;

	public static void clear() {
		concurrentAccessLimit = null;
	}

	public static boolean getBoolean(String key) {
		boolean ret = false;
		SystemProperty property = getProperty(key);
		if (property != null) {
			String value = property.getValue();
			if (StringUtils.isEmpty(value)) {
				value = property.getInitValue();
			}
			if (StringUtils.equalsIgnoreCase(value, "true") || StringUtils.equalsIgnoreCase(value, "1")
					|| StringUtils.equalsIgnoreCase(value, "y") || StringUtils.equalsIgnoreCase(value, "yes")) {
				ret = true;
			}
		}
		return ret;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		boolean ret = defaultValue;
		SystemProperty property = concurrentMap.get(key);
		if (property == null) {
			/**
			 * 判断是否需要从数据库获取配置
			 */
			Long ts = concurrentTimeMap.get(key);
			if (ts == null || ((System.currentTimeMillis() - ts.longValue()) > DateUtils.MINUTE * 5)) {
				PropertyHelper propertyHelper = new PropertyHelper();
				property = propertyHelper.getSystemPropertyByKey(key);
				if (property != null) {
					concurrentMap.put(key, property);
				}
				concurrentTimeMap.put(key, System.currentTimeMillis());
			}
		}
		if (property != null) {
			String value = property.getValue();
			if (StringUtils.isEmpty(value)) {
				value = property.getInitValue();
			}
			if (StringUtils.equalsIgnoreCase(value, "true") || StringUtils.equalsIgnoreCase(value, "1")
					|| StringUtils.equalsIgnoreCase(value, "y") || StringUtils.equalsIgnoreCase(value, "yes")) {
				ret = true;
			}
		}
		// logger.debug("key[" + key + "]=" + ret);
		return ret;
	}

	public static synchronized boolean getConcurrentAccessLimit() {
		if (concurrentAccessLimit == null) {
			boolean ret = getBoolean("concurrentAccessLimit");
			concurrentAccessLimit = new AtomicBoolean();
			concurrentAccessLimit.set(ret);
		}
		return concurrentAccessLimit.get();
	}

	public static Map<String, Object> getContextMap() {
		Map<String, Object> dataMap = new java.util.HashMap<String, Object>();
		dataMap.put(CURR_YYYYMMDD, getCurrentYYYYMMDD());
		dataMap.put(CURR_YYYYMM, getCurrentYYYYMM());
		dataMap.put(INPUT_YYYYMMDD, getInputYYYYMMDD());
		dataMap.put(INPUT_YYYYMM, getInputYYYYMM());
		dataMap.put("curr_yyyymmdd", getCurrentYYYYMMDD());
		dataMap.put("curr_yyyymm", getCurrentYYYYMM());
		dataMap.put("input_yyyymmdd", getInputYYYYMMDD());
		dataMap.put("input_yyyymm", getInputYYYYMM());
		dataMap.put("now", getCurrentYYYYMMDD());
		dataMap.put("${now}", getCurrentYYYYMMDD());
		dataMap.put("#{now}", getCurrentYYYYMMDD());
		return dataMap;
	}

	public static String getCurrentYYYYMM() {
		String value = getString("curr_yyyymm");
		if (StringUtils.isEmpty(value) || StringUtils.equals("curr_yyyymm", value)
				|| StringUtils.equals(CURR_YYYYMM, value)) {
			Date now = new Date();
			value = String.valueOf(DateUtils.getYearMonth(now));
		}
		logger.debug("curr_yyyymm:" + value);
		return value;
	}

	public static String getCurrentYYYYMMDD() {
		String value = getString("curr_yyyymmdd");
		if (StringUtils.isEmpty(value) || StringUtils.equals("curr_yyyymmdd", value)
				|| StringUtils.equals(CURR_YYYYMMDD, value)) {
			Date now = new Date();
			value = String.valueOf(DateUtils.getYearMonthDay(now));
		}
		logger.debug("curr_yyyymmdd:" + value);
		return value;
	}

	public static String getDataPath() {
		String dataDir = getString("dataDir");
		if (StringUtils.isEmpty(dataDir)) {
			dataDir = SystemProperties.getConfigRootPath() + "/report/data";
		}
		return dataDir;
	}

	public static String getDefaultEncoding() {
		return DEFAULT_ENCODING;
	}

	public static String getInputYYYYMM() {
		String value = getString("input_yyyymm");
		if (StringUtils.isEmpty(value) || StringUtils.equals("input_yyyymm", value)
				|| StringUtils.equals(INPUT_YYYYMM, value)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.set(year, month, day - 1);

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			month = month + 1;
			logger.debug(year + "-" + month);

			StringBuilder sb = new StringBuilder(50);
			sb.append(year);

			if (month <= 9) {
				sb.append("0").append(month);
			} else {
				sb.append(month);
			}
			value = sb.toString();
		}

		logger.debug("input_yyyymm:" + value);
		return value;
	}

	public static String getInputYYYYMMDD() {
		String value = getString("input_yyyymmdd");
		if (StringUtils.isEmpty(value) || StringUtils.equals("input_yyyymmdd", value)
				|| StringUtils.equals(INPUT_YYYYMMDD, value)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.set(year, month, day - 1);

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			month = month + 1;
			day = calendar.get(Calendar.DAY_OF_MONTH);

			logger.debug(year + "-" + month + "-" + day);

			StringBuilder sb = new StringBuilder(50);
			sb.append(year);

			if (month <= 9) {
				sb.append("0").append(month);
			} else {
				sb.append(month);
			}

			if (day <= 9) {
				sb.append("0").append(day);
			} else {
				sb.append(day);
			}

			value = sb.toString();
		}
		logger.debug("input_yyyymmdd:" + value);
		return value;
	}

	public static int getInt(String key, int defaultValue) {
		int ret = defaultValue;
		SystemProperty prop = getProperty(key);
		if (prop != null) {
			String value = prop.getValue();
			if (StringUtils.isEmpty(value)) {
				value = prop.getInitValue();
			}
			try {
				ret = Integer.parseInt(value);
			} catch (Exception ex) {
			}
		}
		return ret;
	}

	public static int getIntToken() {
		if (INT_TOKEN > 0) {
			return INT_TOKEN;
		}
		PropertyHelper propertyHelper = new PropertyHelper();
		SystemProperty property = propertyHelper.getSystemPropertyById("INT_TOKEN");
		if (property != null && property.getValue() != null) {
			INT_TOKEN = Integer.parseInt(property.getValue());
		} else {
			java.util.Random random = new java.util.Random();
			INT_TOKEN = Math.abs(random.nextInt(99999999)) + 1;
			property = new SystemProperty();
			property.setId("INT_TOKEN");
			property.setCategory("SYS");
			property.setName("INT_TOKEN");
			property.setLocked(0);
			property.setValue(String.valueOf(INT_TOKEN));
			property.setTitle("INT_TOKEN");
			property.setType("String");
			propertyHelper.save(property);
		}
		return INT_TOKEN;
	}

	public static long getLong(String key, long defaultValue) {
		long ret = defaultValue;
		SystemProperty prop = getProperty(key);
		if (prop != null) {
			String value = prop.getValue();
			if (StringUtils.isEmpty(value)) {
				value = prop.getInitValue();
			}
			try {
				ret = Long.parseLong(value);
			} catch (Exception ex) {
			}
		}
		return ret;
	}

	public static String getMappingPath() {
		String mappingDir = SystemProperties.getConfigRootPath() + "/report/mapping";
		return mappingDir;
	}

	public static SystemProperty getProperty(String key) {
		SystemProperty property = null;
		if (concurrentMap.size() == 0) {
			reload();
		}
		property = concurrentMap.get(key);
		if (property == null) {
			/**
			 * 判断是否需要从数据库获取配置
			 */
			Long ts = concurrentTimeMap.get(key);
			if (ts == null || ((System.currentTimeMillis() - ts.longValue()) > DateUtils.MINUTE * 5)) {
				PropertyHelper propertyHelper = new PropertyHelper();
				property = propertyHelper.getSystemPropertyById(key);
				if (property == null) {
					property = propertyHelper.getSystemPropertyByKey(key);
				}
				concurrentTimeMap.put(key, System.currentTimeMillis());
			}
			if (property != null) {
				concurrentMap.put(key, property);
			}
		}
		if (property != null) {
			JSONObject jsonObject = SystemPropertyJsonFactory.toJsonObject(property);
			return SystemPropertyJsonFactory.jsonToObject(jsonObject);
		}
		return null;
	}

	public static String getRegionName(String name) {
		StringBuilder buffer = new StringBuilder(200);
		if (Environment.getCurrentSystemName() != null
				&& !StringUtils.equals(Environment.DEFAULT_SYSTEM_NAME, Environment.getCurrentSystemName())) {
			buffer.append(Environment.getCurrentSystemName()).append("_");
		} else {
			buffer.append("g_");
		}
		buffer.append(getIntToken()).append("_");
		if (name != null) {
			buffer.append(name);
		}
		String regionName = buffer.toString();
		if (regionName.indexOf("/") != -1) {
			regionName = StringTools.replace(regionName, "/", "_");
		}
		if (regionName.indexOf("\\") != -1) {
			regionName = StringTools.replace(regionName, "\\", "_");
		}
		return regionName;
	}

	public static String getReportSavePath() {
		String value = getString("report_save_path");
		if (StringUtils.isEmpty(value)) {
			value = SystemProperties.getConfigRootPath() + "/report";
		}
		return value;
	}

	/**
	 * 获取服务地址
	 * 
	 * @return
	 */
	public static String getServiceUrl() {
		return getString("serviceUrl");
	}

	public static String getString(String key) {
		String ret = null;
		SystemProperty prop = getProperty(key);
		if (prop != null) {
			String value = prop.getValue();
			if (StringUtils.isEmpty(value)) {
				value = prop.getInitValue();
			}
			ret = value;
		}
		return ret;
	}

	public static String getString(String key, String defaultValue) {
		String ret = defaultValue;
		SystemProperty prop = getProperty(key);
		if (prop != null) {
			String value = prop.getValue();
			if (StringUtils.isEmpty(value)) {
				value = prop.getInitValue();
			}
			ret = value;
		}
		return ret;
	}

	public static String getStringValue(String key, String defaultValue) {
		String ret = defaultValue;
		SystemProperty property = concurrentMap.get(key);
		if (property == null) {
			/**
			 * 判断是否需要从数据库获取配置
			 */
			Long ts = concurrentTimeMap.get(key);
			if (ts == null || ((System.currentTimeMillis() - ts.longValue()) > DateUtils.MINUTE * 5)) {
				PropertyHelper propertyHelper = new PropertyHelper();
				property = propertyHelper.getSystemPropertyByKey(key);
				if (property != null) {
					concurrentMap.put(key, property);
				}
				concurrentTimeMap.put(key, System.currentTimeMillis());
			}
		}
		if (property != null) {
			String value = property.getValue();
			if (StringUtils.isEmpty(value)) {
				value = property.getInitValue();
			}
			ret = value;
		}
		return ret;
	}

	public static String getSysCode() {
		if (SYS_CODE != null) {
			return SYS_CODE;
		}
		PropertyHelper propertyHelper = new PropertyHelper();
		SystemProperty property = propertyHelper.getSystemPropertyById("SYS_CODE");
		if (property != null && property.getValue() != null) {
			SYS_CODE = property.getValue();
		} else {
			property = new SystemProperty();
			property.setId("SYS_CODE");
			property.setCategory("SYS");
			property.setName("SYS_CODE");
			property.setLocked(0);
			property.setValue(SYS_CODE);
			property.setTitle("系统编码");
			property.setType("String");
			propertyHelper.save(property);
		}
		return SYS_CODE;
	}

	public static String getToken() {
		if (TOKEN != null) {
			return TOKEN;
		}
		PropertyHelper propertyHelper = new PropertyHelper();
		SystemProperty property = propertyHelper.getSystemPropertyById("TOKEN");
		if (property != null && property.getValue() != null) {
			TOKEN = property.getValue();
		} else {
			java.util.Random random = new java.util.Random();
			TOKEN = Math.abs(random.nextInt(9999)) + com.glaf.core.util.UUID32.getUUID()
					+ Math.abs(random.nextInt(9999));
			property = new SystemProperty();
			property.setId("TOKEN");
			property.setCategory("SYS");
			property.setName("TOKEN");
			property.setLocked(0);
			property.setValue(TOKEN);
			property.setTitle("TOKEN");
			property.setType("String");
			propertyHelper.save(property);
		}
		return TOKEN;
	}

	/**
	 * 直接获取数据库存储的属性值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValueByKey(String key) {
		String ret = null;
		PropertyHelper helper = new PropertyHelper();
		SystemProperty prop = helper.getSystemPropertyByKey(key);
		if (prop != null) {
			String value = prop.getValue();
			if (StringUtils.isEmpty(value)) {
				value = prop.getInitValue();
			}
			ret = value;
		}
		return ret;
	}

	public static void main(String[] args) {
		Date now = new Date();
		Map<String, Object> sysMap = new java.util.HashMap<String, Object>();
		sysMap.put("curr_yyyymmdd", DateUtils.getYearMonthDay(now));
		sysMap.put("curr_yyyymm", DateUtils.getYearMonth(now));
		System.out.println(Mvel2ExpressionEvaluator.evaluate("#{curr_yyyymmdd}-1", sysMap));
	}

	public static void reload() {
		if (!loading.get()) {
			try {
				loading.set(true);
				logger.info("start reload system config...");
				concurrentMap.clear();
				concurrentTimeMap.clear();
				ISystemPropertyService systemPropertyService = ContextFactory.getBean("systemPropertyService");
				List<SystemProperty> list = systemPropertyService.getAllSystemProperties();
				if (list != null && !list.isEmpty()) {
					for (SystemProperty p : list) {
						concurrentMap.put(p.getName(), p);
						if (p.getValue() != null) {
							conf.set(p.getName(), p.getValue());
						}
						// if
						// (SystemConfig.getBoolean("distributed.config.enabled"))
						// {
						// ConfigFactory.put(SystemConfig.class.getSimpleName(),
						// p.getName(),
						// SystemPropertyJsonFactory.toJsonObject(p).toJSONString());
						// }
					}
				}

				SystemProperty property = systemPropertyService.getSystemPropertyById("INT_TOKEN");
				if (property != null && property.getValue() != null) {
					INT_TOKEN = Integer.parseInt(property.getValue());
				}
				concurrentAccessLimit = null;
				logger.info("reload system config end.");
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			} finally {
				loading.set(false);
			}
		}
	}

	public static void setProperty(SystemProperty p) {
		if (p != null && p.getName() != null) {
			String complexKey = p.getName();
			concurrentMap.put(complexKey, p);
			ConfigFactory.put(SystemConfig.class.getSimpleName(), p.getName(),
					SystemPropertyJsonFactory.toJsonObject(p).toJSONString());
		}
	}

}