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
package com.glaf.core.config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.domain.SystemProperty;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.JdbcUtils;

public class PropertyHelper {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 获取属性列表
	 * 
	 * @return
	 */
	public List<SystemProperty> getAllSystemProperties() {
		List<SystemProperty> rows = new ArrayList<SystemProperty>();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			psmt = conn.prepareStatement(" select * from SYS_PROPERTY where LOCKED_ = ? ");
			psmt.setInt(1, 0);
			rs = psmt.executeQuery();
			while (rs.next()) {
				SystemProperty model = new SystemProperty();
				this.populdate(rs, model);
				rows.add(model);
			}
		} catch (SQLException ex) {
			logger.error("get property list error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
		return rows;
	}

	/**
	 * 获取系统属性
	 * 
	 * @param id
	 *            属性编号
	 * @return
	 */
	public SystemProperty getSystemPropertyById(String id) {
		SystemProperty property = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			psmt = conn.prepareStatement(" select * from SYS_PROPERTY where ID_ = ? ");
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				property = new SystemProperty();
				this.populdate(rs, property);
			}
		} catch (SQLException ex) {
			logger.error("get property error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
		return property;
	}

	/**
	 * 获取系统属性
	 * 
	 * @param key
	 *            属性Key
	 * @return
	 */
	public SystemProperty getSystemPropertyByKey(String key) {
		SystemProperty property = null;
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			psmt = conn.prepareStatement(" select * from SYS_PROPERTY where NAME_ = ? ");
			psmt.setString(1, key);
			rs = psmt.executeQuery();
			if (rs.next()) {
				property = new SystemProperty();
				this.populdate(rs, property);
			}
		} catch (SQLException ex) {
			logger.error("get property error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
		return property;
	}

	public void populdate(ResultSet rs, SystemProperty model) throws SQLException {
		model.setId(rs.getString("ID_"));
		model.setTitle(rs.getString("TITLE_"));
		model.setType(rs.getString("TYPE_"));
		model.setName(rs.getString("NAME_"));
		model.setCategory(rs.getString("CATEGORY_"));
		model.setDescription(rs.getString("DESCRIPTION_"));
		model.setInitValue(rs.getString("INITVALUE_"));
		model.setValue(rs.getString("VALUE_"));
		model.setInputType(rs.getString("INPUTTYPE_"));
		model.setMaxValue(rs.getDouble("MAXVALUE_"));
		model.setMinValue(rs.getDouble("MINVALUE_"));
		model.setLocked(rs.getInt("LOCKED_"));
	}

	public void save(SystemProperty model) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(" insert into SYS_PROPERTY ");
		buffer.append(
				" ( ID_, NAME_, TITLE_, CATEGORY_, INPUTTYPE_, TYPE_, DESCRIPTION_, VALUE_, INITVALUE_, MAXVALUE_, MINVALUE_, LOCKED_ )");
		buffer.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		int index = 1;
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			psmt = conn.prepareStatement(buffer.toString());
			psmt.setString(index++, model.getId());
			psmt.setString(index++, model.getName());
			psmt.setString(index++, model.getTitle());
			psmt.setString(index++, model.getCategory());
			psmt.setString(index++, model.getInputType());
			psmt.setString(index++, model.getType());
			psmt.setString(index++, model.getDescription());
			psmt.setString(index++, model.getValue());
			psmt.setString(index++, model.getInitValue());
			if (model.getMaxValue() != null) {
				psmt.setDouble(index++, model.getMaxValue());
			} else {
				psmt.setObject(index++, null);
			}
			if (model.getMinValue() != null) {
				psmt.setDouble(index++, model.getMinValue());
			} else {
				psmt.setObject(index++, null);
			}
			psmt.setInt(index++, model.getLocked());
			psmt.executeUpdate();
			psmt.close();
			conn.commit();
			logger.debug("--------save property ok.");
		} catch (SQLException ex) {
			logger.error("save property error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	public void update(SystemProperty model) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(" update SYS_PROPERTY set VALUE_ = ? where ID_ = ? ");
		int index = 1;
		Connection conn = null;
		PreparedStatement psmt = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			psmt = conn.prepareStatement(buffer.toString());
			psmt.setString(index++, model.getValue());
			psmt.setString(index++, model.getId());
			psmt.executeUpdate();
			psmt.close();
			conn.commit();
			logger.debug("--------update property ok.");
		} catch (SQLException ex) {
			logger.error("update property error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

}
