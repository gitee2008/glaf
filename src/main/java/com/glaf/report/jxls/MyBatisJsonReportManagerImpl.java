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

package com.glaf.report.jxls;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.sf.jxls.report.ReportManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.factory.EntityFactory;
import com.glaf.core.util.Tools;

public class MyBatisJsonReportManagerImpl implements ReportManager {
	protected final Log log = LogFactory.getLog(getClass());
	protected Connection connection;
	protected Map<String, Object> beans;

	public MyBatisJsonReportManagerImpl(Connection connection) {
		this.connection = connection;
	}

	public MyBatisJsonReportManagerImpl(Connection connection, Map<String, Object> beans) {
		this.connection = connection;
		this.beans = beans;
	}

	public List<?> exec(String json) throws SQLException {
		List<?> rows = null;
		SqlSession session = null;
		SqlSessionFactory sqlSessionFactory = EntityFactory.getInstance().getSqlSessionFactory();
		Object parameter = beans;
		JSONObject jsonObject = JSON.parseObject(json);
		try {
			String parameterType = jsonObject.getString("parameterType");
			if (StringUtils.isNotEmpty(parameterType)) {
				Object object = com.glaf.core.util.ClassUtils.instantiateObject(parameterType);
				Tools.populate(object, beans);
				parameter = object;
			}
			session = sqlSessionFactory.openSession(connection);
			rows = session.selectList(jsonObject.getString("id"), parameter);
			return rows;
		} catch (Exception ex) {
			throw new SQLException(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}