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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

	public static List<String> arrayToList(JSONArray array) {
		List<String> list = new ArrayList<String>();
		if (array != null && !array.isEmpty()) {
			int len = array.size();
			for (int index = 0; index < len; index++) {
				list.add(array.getString(index));
			}
		}
		return list;
	}

	public static Map<String, Object> decode(String str) {
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		if (StringUtils.isNotEmpty(str) && (str.length() > 0 && str.charAt(0) == '{') && str.endsWith("}")) {
			try {
				JSONObject jsonObject = (JSONObject) JSON.parse(str);
				Iterator<Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					String key = (String) entry.getKey();
					Object value = entry.getValue();
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] array = (Object[]) value;
							Collection<Object> collection = new java.util.ArrayList<Object>();
							for (int i = 0; i < array.length; i++) {
								collection.add(array[i]);
							}
							dataMap.put(key, collection);
						} else if (value instanceof JSONArray) {
							JSONArray array = (JSONArray) value;
							Collection<Object> collection = new java.util.ArrayList<Object>();
							for (int i = 0, len = array.size(); i < len; i++) {
								collection.add(array.get(i));
							}
							dataMap.put(key, collection);
						} else if (value instanceof Collection<?>) {
							Collection<?> collection = (Collection<?>) value;
							dataMap.put(key, collection);
						} else if (value instanceof Map<?, ?>) {
							Map<?, ?> map = (Map<?, ?>) value;
							dataMap.put(key, map);
						} else {
							if ((!key.startsWith("x_filter_")) && key.toLowerCase().endsWith("date")) {
								String datetime = value.toString();
								try {
									java.util.Date date = DateUtils.toDate(datetime);
									dataMap.put(key, date);
								} catch (Exception ex) {

									dataMap.put(key, value);
								}
							} else {
								dataMap.put(key, value);
							}
						}
					}
				}
			} catch (JSONException ex) {

			}
		}
		return dataMap;
	}

	public static Map<String, String> decodeStringMap(String str) {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		if (StringUtils.isNotEmpty(str) && (str.length() > 0 && str.charAt(0) == '{') && str.endsWith("}")) {
			try {
				JSONObject jsonObject = (JSONObject) JSON.parse(str);
				Iterator<Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Object> entry = iterator.next();
					String key = (String) entry.getKey();
					Object value = entry.getValue();
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] array = (Object[]) value;
							Collection<Object> collection = new java.util.ArrayList<Object>();
							for (int i = 0; i < array.length; i++) {
								collection.add(array[i]);
							}

						} else if (value instanceof JSONArray) {
							JSONArray array = (JSONArray) value;
							Collection<Object> collection = new java.util.ArrayList<Object>();
							for (int i = 0, len = array.size(); i < len; i++) {
								collection.add(array.get(i));
							}

						} else if (value instanceof Collection<?>) {

						} else if (value instanceof Map<?, ?>) {

						} else {

							dataMap.put(key, value.toString());

						}
					}
				}
			} catch (JSONException ex) {

			}
		}
		return dataMap;
	}

	public static String encode(Map<String, Object> params) {
		JSONObject jsonObject = null;
		String str = null;
		if (params != null) {
			jsonObject = new JSONObject();
			Set<Entry<String, Object>> entrySet = params.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				try {
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] array = (Object[]) value;
							Collection<Object> collection = new java.util.ArrayList<Object>();
							for (int i = 0; i < array.length; i++) {
								collection.add(array[i]);
							}
							jsonObject.put(key, collection);
						} else if (value instanceof Collection<?>) {
							Collection<?> collection = (Collection<?>) value;
							jsonObject.put(key, collection);
						} else if (value instanceof Map<?, ?>) {
							Map<?, ?> map = (Map<?, ?>) value;
							jsonObject.put(key, map);
						} else if (value instanceof java.util.Date) {
							java.util.Date date = (java.util.Date) value;
							String datetime = DateUtils.getDateTime(date);
							jsonObject.put(key, datetime);
						} else {
							jsonObject.put(key, value);
						}
					}
				} catch (JSONException ex) {

				}
			}
			str = jsonObject.toString();
		}
		return str;
	}

	public static JSONArray listToArray(List<String> list) {
		JSONArray array = new JSONArray();
		if (list != null && !list.isEmpty()) {
			for (String str : list) {
				array.add(str);
			}
		}
		return array;
	}

	public static JSONObject toJSONObject(Map<String, Object> params) {
		JSONObject jsonObject = null;
		if (params != null) {
			jsonObject = new JSONObject();
			Set<Entry<String, Object>> entrySet = params.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				try {
					if (value != null) {
						if (value instanceof Object[]) {
							Object[] array = (Object[]) value;
							Collection<Object> collection = new java.util.ArrayList<Object>();
							for (int i = 0; i < array.length; i++) {
								collection.add(array[i]);
							}
							jsonObject.put(key, collection);
						} else if (value instanceof Collection<?>) {
							Collection<?> collection = (Collection<?>) value;
							jsonObject.put(key, collection);
						} else if (value instanceof Map<?, ?>) {
							Map<?, ?> map = (Map<?, ?>) value;
							jsonObject.put(key, map);
						} else if (value instanceof java.util.Date) {
							java.util.Date date = (java.util.Date) value;
							String datetime = DateUtils.getDateTime(date);
							jsonObject.put(key, datetime);
						} else {
							jsonObject.put(key, value);
						}
					}
				} catch (JSONException ex) {

				}
			}
		}
		return jsonObject;
	}

}