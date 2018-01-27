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

package com.glaf.base.modules.sys;

public final class SysConstants {

	public static String BRANCH_PREFIX = "branch_";

	public static String BRANCH_ADMIN = "BranchAdmin";// 分级管理员角色代码

	public static String PROJECT_ADMIN = "ProjectAdmin";// 项目管理员角色代码

	public static String ORG_ADMIN = "TenantAdmin";// 机构管理员角色代码

	public static String SYSTEM_TENANT = "sys";

	public static String SYSTEM_ADMINISTRATOR = "SystemAdministrator";// 分级管理员角色代码

	public static int SORT_FORWARD = 1;// 后移

	public static int SORT_PREVIOUS = 0;// 前移

	public static String TREE_DICTORY = "011";// 数据字典结构树编号

	public static int TREE_ROOT = 1;// 目录根节点

	public static long BILLION = 100000000L;

	public static long TEN_BILLION = 10000000000L;

	public static String USER_HEADSHIP = "UserHeadship";// 用户职位代码，取值为SYS_DICTORY表的code

	public static String USER_ACCOUNTTYPE = "AccountType";// 账户类型，取值为SYS_DICTORY表的code

	public static final int PAGE_SIZE = 10;// 缺省页面大小

	public static final String UPLOAD_DIR = "/WEB-INF/upload/files/";
}