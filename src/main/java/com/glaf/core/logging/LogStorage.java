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

package com.glaf.core.logging;

import java.util.List;

import com.glaf.core.domain.SysDataLog;

public interface LogStorage {
	
	/**
	 * 获取某个模块最新的指定条数的日志列表
	 * 
	 * @param moduleId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<SysDataLog> getLatestLogs(String moduleId, int offset, int limit);

	/**
	 * 获取某个模块日志总数
	 * @param moduleId
	 * @return
	 */
	int getTotal(String moduleId);

	/**
	 * 存储日志
	 * 
	 * @param logs
	 */
	void saveLogs(List<SysDataLog> logs);

}
