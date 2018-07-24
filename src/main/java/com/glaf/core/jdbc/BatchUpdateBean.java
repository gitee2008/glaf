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

package com.glaf.core.jdbc;

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

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.LowerLinkedMap;
import com.glaf.core.util.ParamUtils;

public class BatchUpdateBean {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 批量更新数据
	 * 
	 * @param conn
	 * @param tableDefinition
	 * @param dataList
	 */
	public void dynamicUpdate(LoginContext loginContext, Connection conn, TableDefinition tableDefinition,
			List<Map<String, Object>> dataList) {
		StringBuilder buffer = new StringBuilder();
		ColumnDefinition idColumn = tableDefinition.getIdColumn();
		List<ColumnDefinition> columns = tableDefinition.getColumns();
		List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();
		for (ColumnDefinition col : columns) {
			if (col.isPrimaryKey()) {
				if (idColumn == null) {
					idColumn = col;
				}
			} else {
				cols.add(col);
			}
		}

		int index = 1;
		String columnName = null;
		String javaType = null;
		LowerLinkedMap dataMap = null;
		PreparedStatement psmt = null;
		ByteArrayInputStream bais = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		try {
			String dbType = DBConnectionFactory.getDatabaseType(conn);
			for (int k = 0, l = dataList.size(); k < l; k++) {
				index = 1;
				buffer.delete(0, buffer.length());
				// Map<String, Object> dataMap = dataList.get(k);
				Map<String, Object> rowMap = dataList.get(k);
				dataMap = new LowerLinkedMap();
				dataMap.putAll(rowMap);
				buffer.append(" update ").append(tableDefinition.getTableName()).append(" set ");
				for (ColumnDefinition column : cols) {
					if (dataMap.get(column.getColumnName().toLowerCase()) != null) {
						buffer.append(column.getColumnName()).append(" = ?, ");
					}
				}

				buffer.delete(buffer.length() - 2, buffer.length());
				buffer.append(" where ").append(idColumn.getColumnName()).append(" = ? ");
				psmt = conn.prepareStatement(buffer.toString());

				for (ColumnDefinition column : cols) {
					if (dataMap.get(column.getColumnName().toLowerCase()) != null) {
						columnName = column.getColumnName().toLowerCase();
						javaType = column.getJavaType();
						switch (javaType) {
						case "Integer":
							psmt.setInt(index++, ParamUtils.getInt(dataMap, columnName));
							break;
						case "Long":
							psmt.setLong(index++, ParamUtils.getLong(dataMap, columnName));
							break;
						case "Double":
							psmt.setDouble(index++, ParamUtils.getDouble(dataMap, columnName));
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
											IOUtils.closeStream(bais);
											IOUtils.closeStream(bis);
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
											IOUtils.closeStream(bais);
											IOUtils.closeStream(bis);
										}
									}
									IOUtils.closeStream(is);
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
											IOUtils.closeStream(bais);
											IOUtils.closeStream(bis);
										}
									}
									IOUtils.closeStream(is);
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

				columnName = idColumn.getColumnName().toLowerCase();
				javaType = idColumn.getJavaType();
				switch (javaType) {
				case "Integer":
					psmt.setInt(index++, ParamUtils.getInt(dataMap, columnName));
					break;
				case "Long":
					psmt.setLong(index++, ParamUtils.getLong(dataMap, columnName));
					break;
				case "Double":
					psmt.setDouble(index++, ParamUtils.getDouble(dataMap, columnName));
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
				default:
					psmt.setObject(index++, ParamUtils.getObject(dataMap, columnName));
					break;
				}

				psmt.executeUpdate();
				JdbcUtils.close(psmt);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeStream(is);
			IOUtils.closeStream(bais);
			IOUtils.closeStream(bis);
			JdbcUtils.close(psmt);
		}
	}

	/**
	 * 批量更新数据
	 * 
	 * @param conn
	 * @param tableDefinition
	 * @param dataList
	 */
	public void executeBatch(LoginContext loginContext, Connection conn, TableDefinition tableDefinition,
			List<Map<String, Object>> dataList) {
		StringBuilder buffer = new StringBuilder();
		ColumnDefinition idColumn = tableDefinition.getIdColumn();
		List<ColumnDefinition> columns = tableDefinition.getColumns();
		List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();
		for (ColumnDefinition col : columns) {
			if (col.isPrimaryKey()) {
				if (idColumn == null) {
					idColumn = col;
				}
			} else {
				cols.add(col);
			}
		}

		buffer.append(" update ").append(tableDefinition.getTableName()).append(" set ");
		for (ColumnDefinition column : cols) {
			buffer.append(column.getColumnName()).append(" = ?, ");
		}

		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append(" where ").append(idColumn.getColumnName()).append(" = ? ");

		cols.add(idColumn);

		logger.debug(buffer.toString());

		int index = 1;
		String columnName = null;
		String javaType = null;
		PreparedStatement psmt = null;
		ByteArrayInputStream bais = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		try {
			String dbType = DBConnectionFactory.getDatabaseType(conn);
			psmt = conn.prepareStatement(buffer.toString());
			for (int k = 0, l = dataList.size(); k < l; k++) {
				index = 1;
				Map<String, Object> dataMap = dataList.get(k);
				for (ColumnDefinition column : cols) {
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
										IOUtils.closeStream(bais);
										IOUtils.closeStream(bis);
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
										IOUtils.closeStream(bais);
										IOUtils.closeStream(bis);
									}
								}
								IOUtils.closeStream(is);
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
										IOUtils.closeStream(bais);
										IOUtils.closeStream(bis);
									}
								}
								IOUtils.closeStream(is);
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
				psmt.addBatch();
			}
			psmt.executeBatch();
			psmt.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeStream(is);
			IOUtils.closeStream(bais);
			IOUtils.closeStream(bis);
			JdbcUtils.close(psmt);
		}
	}
}
