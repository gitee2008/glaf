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

package com.glaf.matrix.data.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExceptionUtils {

	protected static Map<String, List<String>> msgListMap = new ConcurrentHashMap<String, List<String>>();

	public static void addMsg(String key, String msg) {
		List<String> msgList = msgListMap.get(key);
		if (msgList == null) {
			msgList = new ArrayList<String>();
		}
		msgList.add(msg);
		msgListMap.put(key, msgList);
	}

	public static void clear(String key) {
		msgListMap.remove(key);
	}

	public static StringBuffer getMsgBuffer(String key) {
		List<String> msgList = msgListMap.get(key);
		StringBuffer buffer = new StringBuffer();
		if (msgList != null && msgList.size() > 0) {
			for (String msg : msgList) {
				buffer.append(msg);
				buffer.append("\n");
			}
		}
		return buffer;
	}

	public static List<String> getMsgs(String key) {
		List<String> msgList = msgListMap.get(key);
		return msgList;
	}

	private ExceptionUtils() {

	}
}