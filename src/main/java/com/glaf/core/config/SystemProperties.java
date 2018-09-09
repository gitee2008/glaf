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

import java.io.File;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.glaf.core.util.Constants;
import com.glaf.core.util.StringTools;

public class SystemProperties {
	protected static final Log logger = LogFactory.getLog(SystemProperties.class);

	private static final Configuration conf = BaseConfiguration.create();

	protected static AtomicBoolean loading = new AtomicBoolean(false);

	protected static final String DEPLOYMENT_SYSTEM_NAME = "deploymentSystemName";

	private static volatile String ROOT_CONF_PATH = null;

	private static volatile String ROOT_APP_PATH = null;

	public static String getAppPath() {
		if (ROOT_APP_PATH == null) {
			reload();
		}
		return ROOT_APP_PATH;
	}

	public static boolean getBoolean(String key) {
		if (hasObject(key)) {
			String value = conf.get(key);
			return Boolean.valueOf(value).booleanValue();
		}
		return false;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		if (hasObject(key)) {
			String value = conf.get(key);
			return Boolean.valueOf(value).booleanValue();
		}
		return defaultValue;
	}

	/**
	 * 返回web应用的WEB-INF目录的全路径
	 * 
	 * @return
	 */
	public static String getConfigRootPath() {
		if (ROOT_CONF_PATH == null) {
			reload();
		}
		return ROOT_CONF_PATH;
	}

	/**
	 * 获取系统名称
	 * 
	 * @return
	 */
	public static String getAppName() {
		if (System.getProperty(Constants.APP_NAME) != null) {
			return System.getProperty(Constants.APP_NAME);
		}
		return "glaf";
	}

	/**
	 * 获取系统部署名称
	 * 
	 * @return
	 */
	public static String getDeploymentSystemName() {
		if (System.getProperty(DEPLOYMENT_SYSTEM_NAME) != null) {
			return System.getProperty(DEPLOYMENT_SYSTEM_NAME);
		}
		return null;
	}

	public static double getDouble(String key) {
		if (hasObject(key)) {
			String value = conf.get(key);
			return Double.parseDouble(value);
		}
		return 0;
	}

	/**
	 * 获取文件存储根路径
	 * 
	 * @return
	 */
	public static String getFileStorageRootPath() {
		if (StringUtils.isNotEmpty(conf.get("fs_storage_path"))) {
			String path = conf.get("fs_storage_path");
			if (StringUtils.startsWith(path, "${webapp.root}")) {
				path = StringTools.replaceIgnoreCase(path, "${webapp.root}", getAppPath());
			}
			if (StringUtils.startsWith(path, "${webapp.root.config}")) {
				path = StringTools.replaceIgnoreCase(path, "${webapp.root.config}", getConfigRootPath());
			}
			return path;
		}
		return getConfigRootPath();
	}

	public static int getInt(String key) {
		if (hasObject(key)) {
			String value = conf.get(key);
			return Integer.parseInt(value);
		}
		return 0;
	}

	public static long getLong(String key) {
		if (hasObject(key)) {
			String value = conf.get(key);
			return Long.parseLong(value);
		}
		return 0;
	}

	/**
	 * 获取主数据库数据源配置文件
	 * 
	 * @return
	 */
	public static String getMasterDataSourceConfigFile() {
		String deploymentSystemName = getDeploymentSystemName();
		if (deploymentSystemName != null && deploymentSystemName.length() > 0) {
			return Constants.DEPLOYMENT_JDBC_PATH + deploymentSystemName + "/jdbc.properties";
		}
		return Constants.DEFAULT_MASTER_JDBC_CONFIG;
	}

	public static String getString(String key) {
		if (hasObject(key)) {
			String value = conf.get(key);
			return value;
		}
		return null;
	}

	public static String getString(String key, String defaultValue) {
		if (hasObject(key)) {
			String value = conf.get(key);
			return value;
		}
		return defaultValue;
	}

	public static boolean hasObject(String key) {
		String value = conf.get(key);
		if (value != null) {
			return true;
		}
		return false;
	}

	public static void reload() {
		if (!loading.get()) {
			Resource resource = null;
			try {
				loading.set(true);
				String path = System.getProperty("config.path");
				if (StringUtils.isEmpty(path)) {
					resource = new ClassPathResource(Constants.SYSTEM_CONFIG);
					ROOT_CONF_PATH = resource.getFile().getParentFile().getParentFile().getAbsolutePath();
					ROOT_APP_PATH = resource.getFile().getParentFile().getParentFile().getParentFile()
							.getAbsolutePath();
				} else {
					File dir = new File(path);
					ROOT_CONF_PATH = dir.getAbsolutePath();
					ROOT_APP_PATH = dir.getAbsolutePath();
				}
				System.out.println("config path:" + path);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			} finally {
				loading.set(false);
			}
		}
	}

	private SystemProperties() {

	}

}