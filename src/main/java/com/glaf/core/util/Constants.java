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

public final class Constants {

	public final static String PRIVILEGE_READ = "r";// 读取权限

	public final static String PRIVILEGE_READ_WRITE = "rw";// 读写权限

	public final static String PRIVILEGE_AUDIT = "rwa";// 读写及审核权限

	public final static String PRIVILEGE_EXPORT = "rx";// 读取及导出权限

	public final static String PRIVILEGE_MANAGE = "admin";// 管理权限

	public final static String BUSINESS_KEY = "businessKey";

	public static final String CACHE_LOGIN_CONTEXT_KEY = "cache_login_context_";

	public static final String CACHE_ROLE_KEY = "cache_role_";

	public static final String CACHE_USER_KEY = "cache_user_";

	public static final String CACHE_TENANT_KEY = "cache_tenant_";

	public static final String CACHE_MGR_TENANT_KEY = "cache_mgr_tenant_";

	public static final String CACHE_USER_ROLE_KEY = "cache_user_role_";

	public static final String CACHE_USER_ROLE_CODE_KEY = "cache_user_role_code_";

	public static final String CACHE_ROLE_REGION = "role";

	public static final String CACHE_USER_REGION = "user";

	public static final String CACHE_TENANT_REGION = "tenant";
	
	public static final String CACHE_MENU_REGION = "menu";
	
	public static final String CACHE_PROPERTY_REGION = "property";

	public static final String CACHE_LOGIN_CONTEXT_REGION = "login_context";

	public static final String CACHE_USER_ROLE_REGION = "user_role";

	public static final String CACHE_USER_ROLE_CODE_REGION = "user_role_code";

	public final static String COOKIE_NAME = "GLAF_COOKIE";

	public final static String AUTH_NAME = "GLAF_AUTH";

	public final static String LOGIN_INFO = "LOGIN_INFO";

	public static final String LOGIN_ACTORID = "GLAF_LOGIN_ACTORID";

	public static final String LOGIN_IP = "LOGIN_IP";

	public static final String LOGIN_DATABASEID = "LOGIN_DATABASEID";

	public static final String LOGIN_TOKEN = "LOGIN_TOKEN";

	public static final String DEFAULT_MASTER_JDBC_CONFIG = "/conf/jdbc.properties";

	public static final String DEPLOYMENT_JDBC_PATH = "/conf/deployment/";

	public final static String APP_NAME = "app.name";

	public final static String APP_PATH = "app.path";

	public final static String CONFIG_PATH = "config.path";

	public final static String WEBAPP_PATH = "webapp.root";

	public static final String JDBC_CONFIG = "/conf/jdbc";

	public final static String LOOP_COUNTER = "loopCounter";

	public static final String SERVER_CONFIG = "/conf/server.properties";

	public final static String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";

	public final static String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";

	public final static String NUMBER_OF_INSTANCES = "nrOfInstances";

	public final static String OUTCOME = "outcome";

	public final static String PROCESS_DEFINITION_XML = ".bpmn20.xml";

	public final static String PROCESS_NAME = "process_name";

	public final static String PROCESS_START_DATE = "start_date";

	public final static String PROCESS_STARTER = "process_starter";

	public static final String SPRING_APPLICATION_CONTEXT = "/conf/spring/spring-config.xml";

	public static final String SYSTEM_CONFIG = "/global.properties";

	public static final String SYSTEM_NAME = "systemName";

	public static final String SYSTEM_PERMISSION_IDS = "SYSTEM_PERMISSION_IDS";

	public final static int TM_DEPT_ROLE_USER_TYPE = 0;

	public final static int TM_PROCESS_STARTER_LEADER_TYPE = 22;

	public final static int TM_PROCESS_STARTER_LEADERS_TYPE = 25;

	public final static int TM_PROCESS_STARTER_TYPE = 20;

	public final static String TM_RUNTIME_ACTORIDS = "dynamicActors";

	public final static int TM_RUNTIME_TYPE = 10;

	public final static int TM_USER_TYPE = 5;

	public static final String TS = "TS";

	/**
	 * 数据库分区数
	 */
	public final static int DB_PARTITION = 32;

	/**
	 * 表分区数
	 */
	public final static int TABLE_PARTITION = 256;

	public static final String LAYOUT_THEME_COOKIE = "GLAF_LAYOUT_THEME_COOKIE";

	public static final String THEME_COOKIE = "GLAF_THEME_COOKIE";

	public static final String HOME_THEME_COOKIE = "GLAF_HOME_THEME_COOKIE";

	public static final String IO_NATIVE_LIB_AVAILABLE_KEY = "glaf.native.lib";

	/** Default value for IO_NATIVE_LIB_AVAILABLE_KEY */
	public static final boolean IO_NATIVE_LIB_AVAILABLE_DEFAULT = true;

	/** Internal buffer size for Snappy compressor/decompressors */
	public static final String IO_COMPRESSION_CODEC_SNAPPY_BUFFERSIZE_KEY = "io.compression.codec.snappy.buffersize";

	/** Default value for IO_COMPRESSION_CODEC_SNAPPY_BUFFERSIZE_KEY */
	public static final int IO_COMPRESSION_CODEC_SNAPPY_BUFFERSIZE_DEFAULT = 256 * 1024;
}