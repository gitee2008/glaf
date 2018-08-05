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
package com.glaf.matrix.data.bean;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.matrix.data.domain.SysTable;
import com.glaf.matrix.data.domain.TableColumn;

public class BatchInsertBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 批量插入数据
	 * 
	 * @param conn
	 * @param sysTable
	 * @param dataList
	 */
	public void bulkInsert(LoginContext loginContext, Connection conn, SysTable sysTable,
			List<Map<String, Object>> dataList) {
		List<TableColumn> columns = sysTable.getColumns();
		if (columns == null || columns.isEmpty()) {
			throw new RuntimeException("table columns is null");
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int len = columns.size();
		int batchSize = 1000 / len;
		for (int k = 0, l = dataList.size(); k < l; k++) {
			list.add(dataList.get(k));
			if (list.size() > 0 && list.size() % batchSize == 0) {
				this.bulkInsertInner(loginContext, conn, sysTable, list);
				list.clear();
			}
		}
		if (list.size() > 0) {
			this.bulkInsertInner(loginContext, conn, sysTable, list);
			list.clear();
		}
	}

	public void bulkInsertInner(LoginContext loginContext, Connection conn, SysTable sysTable,
			List<Map<String, Object>> dataList) {
		List<TableColumn> columns = sysTable.getColumns();
		StringBuilder insertBuffer = new StringBuilder();
		insertBuffer.append(" insert into ").append(sysTable.getTableName()).append(" (");
		for (TableColumn column : columns) {
			insertBuffer.append(column.getColumnName()).append(", ");
		}
		insertBuffer.delete(insertBuffer.length() - 2, insertBuffer.length());
		insertBuffer.append(" ) values ");
		for (int k = 0, l = dataList.size(); k < l; k++) {
			if (k > 0) {
				insertBuffer.append(", ");
			}
			insertBuffer.append(" ( ");
			for (int i = 0, len = columns.size(); i < len; i++) {
				insertBuffer.append("? ");
				if (i < len - 1) {
					insertBuffer.append(",");
				}
			}
			insertBuffer.append(" ) ");
		}
		int index = 1;
		String columnName = null;
		String javaType = null;
		PreparedStatement psmt = null;
		ByteArrayInputStream bais = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		try {
			String dbType = DBConnectionFactory.getDatabaseType(conn);
			psmt = conn.prepareStatement(insertBuffer.toString());
			for (int k = 0, l = dataList.size(); k < l; k++) {
				Map<String, Object> dataMap = dataList.get(k);
				// logger.debug("dataMap:" + dataMap);
				for (TableColumn column : columns) {
					columnName = column.getColumnName().toLowerCase();
					javaType = column.getJavaType();
					switch (javaType) {
					case "Integer":
						if (dataMap.get(columnName) != null) {
							psmt.setInt(index++, ParamUtils.getInt(dataMap, columnName));
						} else {
							psmt.setInt(index++, Types.NULL);
						}
						break;
					case "Long":
						if (dataMap.get(columnName) != null) {
							psmt.setLong(index++, ParamUtils.getLong(dataMap, columnName));
						} else {
							psmt.setLong(index++, Types.NULL);
						}
						break;
					case "Double":
						if (dataMap.get(columnName) != null) {
							psmt.setDouble(index++, ParamUtils.getDouble(dataMap, columnName));
						} else {
							psmt.setDouble(index++, Types.NULL);
						}
						break;
					case "Date":
						Timestamp t = ParamUtils.getTimestamp(dataMap, columnName);
						if (t != null) {
							psmt.setTimestamp(index++, t);
						} else {
							psmt.setNull(index++, java.sql.Types.TIMESTAMP);
						}
						break;
					case "String":
						psmt.setString(index++, ParamUtils.getString(dataMap, columnName));
						break;
					case "Clob":
						String text = ParamUtils.getString(dataMap, columnName);
						if (text != null) {
							if ("oracle".equals(dbType)) {
								Reader clobReader = new StringReader(text);
								psmt.setCharacterStream(index++, clobReader, text.length());
							} else {
								psmt.setString(index++, text);
							}
						} else {
							if ("oracle".equals(dbType)) {
								psmt.setCharacterStream(index++, null);
							} else {
								psmt.setString(index++, null);
							}
						}
						break;
					case "Blob":
						int index2 = index++;
						Object object = dataMap.get(columnName);
						if (object != null) {
							if (object instanceof byte[]) {
								byte[] data = (byte[]) object;
								if (data != null) {
									try {
										bais = new ByteArrayInputStream(data);
										bis = new BufferedInputStream(bais);
										psmt.setBinaryStream(index2, bis);
									} catch (SQLException ex) {
										psmt.setBytes(index2, data);
									} finally {
										com.glaf.core.util.IOUtils.closeStream(bais);
										com.glaf.core.util.IOUtils.closeStream(bis);
									}
								}
							} else if (object instanceof java.sql.Blob) {
								Blob blob = (Blob) object;
								is = blob.getBinaryStream();
								byte[] data = FileUtils.getBytes(is);
								if (data != null) {
									try {
										bais = new ByteArrayInputStream(data);
										bis = new BufferedInputStream(bais);
										psmt.setBinaryStream(index2, bis);
									} catch (SQLException ex) {
										psmt.setBytes(index2, data);
									} finally {
										com.glaf.core.util.IOUtils.closeStream(bais);
										com.glaf.core.util.IOUtils.closeStream(bis);
									}
								}
								com.glaf.core.util.IOUtils.closeStream(is);
							} else if (object instanceof InputStream) {
								is = (InputStream) object;
								byte[] data = FileUtils.getBytes(is);
								if (data != null) {
									try {
										bais = new ByteArrayInputStream(data);
										bis = new BufferedInputStream(bais);
										psmt.setBinaryStream(index2, bis);
									} catch (SQLException ex) {
										psmt.setBytes(index2, data);
									} finally {
										com.glaf.core.util.IOUtils.closeStream(bais);
										com.glaf.core.util.IOUtils.closeStream(bis);
									}
								}
								com.glaf.core.util.IOUtils.closeStream(is);
							}
						} else {
							psmt.setNull(index2, Types.NULL);
						}
						break;
					default:
						psmt.setObject(index++, ParamUtils.getObject(dataMap, columnName));
						break;
					}
				}
			}
			psmt.executeUpdate();
			psmt.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			com.glaf.core.util.IOUtils.closeStream(is);
			com.glaf.core.util.IOUtils.closeStream(bais);
			com.glaf.core.util.IOUtils.closeStream(bis);
			JdbcUtils.close(psmt);
		}
	}

}
