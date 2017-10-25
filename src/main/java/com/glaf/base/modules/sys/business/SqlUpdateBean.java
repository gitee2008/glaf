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

package com.glaf.base.modules.sys.business;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import com.glaf.base.modules.sys.model.SqlStatement;
import com.glaf.base.modules.sys.util.SqlStatementDomainFactory;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.IOUtils;
import com.glaf.core.util.JdbcUtils;

public class SqlUpdateBean {

	protected static final Log logger = LogFactory.getLog(SqlUpdateBean.class);

	public void execute() {
		java.io.InputStream inputStream = null;
		try {
			inputStream = SqlUpdateBean.class.getResourceAsStream("/update_sql.xml");
			if (inputStream != null) {
				logger.info("准备执行数据库升级脚本......");
				List<SqlStatement> statements = this.read(inputStream);
				if (statements != null && !statements.isEmpty()) {
					if (!DBUtils.tableExists("SYS_SQL_STATEMENT")) {
						TableDefinition tableDefinition = SqlStatementDomainFactory.getTableDefinition();
						DBUtils.createTable(tableDefinition);
					}
					this.insert(0, statements);
					this.update(0, statements);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			IOUtils.closeStream(inputStream);
		}
	}

	public void insert(long databaseId, List<SqlStatement> statements) {
		String sql = " select STATUS_ from SYS_SQL_STATEMENT where ID_ = ? ";
		String sql_insert = " insert into SYS_SQL_STATEMENT(ID_, DATABASEID_, TITLE_, SQL_, STATUS_) values(?, ?, ?, ?, ?) ";
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
			Database database = cfg.getDatabase(databaseId);
			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			for (SqlStatement statement : statements) {
				try {
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, statement.getId());
					rs = psmt.executeQuery();
					if (rs.next()) {
						continue;
					}
					JdbcUtils.close(rs);
					JdbcUtils.close(psmt);
					int index = 1;
					psmt = conn.prepareStatement(sql_insert);
					psmt.setString(index++, statement.getId());
					psmt.setLong(index++, statement.getDatabaseId());
					psmt.setString(index++, statement.getTitle());
					psmt.setString(index++, statement.getSql());
					psmt.setInt(index++, 0);
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
			conn.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(conn);
		}
	}

	public List<SqlStatement> read(java.io.InputStream inputStream) {
		List<SqlStatement> statements = new ArrayList<SqlStatement>();
		SAXReader xmlReader = new SAXReader();
		try {
			Document doc = xmlReader.read(inputStream);
			Element root = doc.getRootElement();
			List<?> list = root.elements("statement");
			if (list != null && !list.isEmpty()) {
				Iterator<?> iterator = list.iterator();
				while (iterator.hasNext()) {
					Element elem = (Element) iterator.next();
					SqlStatement model = new SqlStatement();
					model.setId(elem.attributeValue("id"));
					model.setTitle(elem.attributeValue("title"));
					model.setSql(elem.getStringValue());
					statements.add(model);
				}
			}
			return statements;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void update(long databaseId, List<SqlStatement> statements) {
		String sql = " select STATUS_ from SYS_SQL_STATEMENT where ID_ = ? ";
		String sql_update = " update SYS_SQL_STATEMENT set STATUS_ = ?, EXECUTETIME_ = ?, SQL_ = ? where ID_ = ? ";
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			DatabaseConnectionConfig cfg = new DatabaseConnectionConfig();
			Database database = cfg.getDatabase(databaseId);
			if (database != null) {
				conn = DBConnectionFactory.getConnection(database.getName());
			} else {
				conn = DBConnectionFactory.getConnection();
			}
			conn.setAutoCommit(false);
			for (SqlStatement statement : statements) {
				try {
					psmt = conn.prepareStatement(sql);
					psmt.setString(1, statement.getId());
					rs = psmt.executeQuery();
					if (rs.next()) {
						int status = rs.getInt(1);
						if (status == 1) {
							continue;
						}
					}
					JdbcUtils.close(rs);
					JdbcUtils.close(psmt);
					DBUtils.executeSchemaResource(conn, statement.getSql());
					psmt = conn.prepareStatement(sql_update);
					psmt.setInt(1, 1);
					psmt.setTimestamp(2, DateUtils.toTimestamp(new java.util.Date()));
					psmt.setString(3, statement.getSql());
					psmt.setString(4, statement.getId());
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
			conn.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(conn);
		}
	}

}
