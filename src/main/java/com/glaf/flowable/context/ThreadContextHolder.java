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

package com.glaf.flowable.context;

import java.util.HashMap;
import java.util.Map;

import io.netty.util.concurrent.FastThreadLocal;

public class ThreadContextHolder {

	private static FastThreadLocal<Map<String, Object>> dataMapHolder = new FastThreadLocal<Map<String, Object>>();

	public static void addData(String key, Object value) {
		Map<String, Object> dataMap = dataMapHolder.get();
		if (dataMap == null) {
			dataMap = new HashMap<String, Object>();
			dataMapHolder.set(dataMap);
		}
		dataMap.put(key, value);
	}

	public static void clear() {
		dataMapHolder.remove();
	}

	public static Map<String, Object> getDataMap() {
		return dataMapHolder.get();
	}

	private ThreadContextHolder() {

	}
}
