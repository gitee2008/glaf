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

package com.glaf.matrix.data.helper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.matrix.util.Constants;

public class MyBatisHelper {

	protected static TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public List<Map<String, Object>> getResults(ResultSet rs) {
		return this.getResults(rs, Constants.MAX_RECORD_SIZE);
	}

	public List<Map<String, Object>> getResults(ResultSet rs, int limit) {
		logger.debug("--------------use mybatis results----------------");
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<String> columns = new ArrayList<String>();
			List<TypeHandler<?>> typeHandlers = new ArrayList<TypeHandler<?>>();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0, n = rsmd.getColumnCount(); i < n; i++) {
				columns.add(rsmd.getColumnLabel(i + 1));
				try {
					Class<?> type = Resources.classForName(rsmd.getColumnClassName(i + 1));
					TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(type);
					if (typeHandler == null) {
						typeHandler = typeHandlerRegistry.getTypeHandler(Object.class);
					}
					typeHandlers.add(typeHandler);
				} catch (Exception ex) {
					typeHandlers.add(typeHandlerRegistry.getTypeHandler(Object.class));
				}
			}
			int index = 0;
			while (rs.next() && index++ < limit) {
				Map<String, Object> row = new HashMap<String, Object>();
				for (int i = 0, n = columns.size(); i < n; i++) {
					String name = columns.get(i);
					TypeHandler<?> handler = typeHandlers.get(i);
					Object value = handler.getResult(rs, name);
					if (value != null) {
						row.put(name.toLowerCase(), value);
					}
				}
				list.add(row);
			}
			return list;
		} catch (SQLException ex) {
			logger.error("getResults error", ex);
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}
