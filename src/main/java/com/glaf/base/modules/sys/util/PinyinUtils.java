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

package com.glaf.base.modules.sys.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.StringTools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class PinyinUtils {
	private final static Log logger = LogFactory.getLog(PinyinUtils.class);

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines, boolean wipeOut) {
		String pinyinName = "";
		if (StringUtils.isNotEmpty(chines)) {
			if (wipeOut) {
				chines = StringTools.replace(chines, "-", "");
				chines = StringTools.replace(chines, "_", "");
				chines = StringTools.replace(chines, "|", "");
				chines = StringTools.replace(chines, "、", "");
				chines = StringTools.replace(chines, "（", "");
				chines = StringTools.replace(chines, "）", "");
				chines = StringTools.replace(chines, "(", "");
				chines = StringTools.replace(chines, ")", "");
				chines = StringTools.replace(chines, "，", "");
				chines = StringTools.replace(chines, ",", "");
				chines = StringTools.replace(chines, "[", "");
				chines = StringTools.replace(chines, "]", "");
				chines = StringTools.replace(chines, "【", "");
				chines = StringTools.replace(chines, "】", "");
			}
			char[] nameChar = chines.toCharArray();
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
			defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			for (int i = 0; i < nameChar.length; i++) {
				if (nameChar[i] > 128) {
					try {
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
					} catch (Exception ex) {
					}
				} else {
					pinyinName += nameChar[i];
				}
			}
		}
		return pinyinName;
	}

	public static void processSysApplication() {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName("SYS_APPLICATION");

			ColumnDefinition idColumn = new ColumnDefinition();
			idColumn.setColumnName("ID");
			idColumn.setJavaType("Long");
			tableDefinition.setIdColumn(idColumn);

			ColumnDefinition short_hypyColumn = new ColumnDefinition();
			short_hypyColumn.setColumnName("NAMEPINYIN");
			short_hypyColumn.setJavaType("String");
			short_hypyColumn.setLength(200);
			tableDefinition.addColumn(short_hypyColumn);

			DBUtils.alterTable(conn, tableDefinition);
			conn.commit();

			Map<Long, String> dataMap = new HashMap<Long, String>();
			String sql = " select id, name from SYS_APPLICATION ";
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				dataMap.put(rs.getLong(1), rs.getString(2));
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			conn.setAutoCommit(false);

			psmt = conn.prepareStatement(" update SYS_APPLICATION set NAMEPINYIN = ? where ID = ? ");
			Set<Entry<Long, String>> entrySet = dataMap.entrySet();
			for (Entry<Long, String> entry : entrySet) {
				Long key = entry.getKey();
				String value = entry.getValue();
				psmt.setString(1, converterToFirstSpell(value, true));
				psmt.setLong(2, key);
				psmt.executeUpdate();
			}
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	public static void processSysOrganization() {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName("SYS_ORGANIZATION");

			ColumnDefinition idColumn = new ColumnDefinition();
			idColumn.setColumnName("ID");
			idColumn.setJavaType("Long");
			tableDefinition.setIdColumn(idColumn);

			ColumnDefinition short_hypyColumn = new ColumnDefinition();
			short_hypyColumn.setColumnName("NAMEPINYIN");
			short_hypyColumn.setJavaType("String");
			short_hypyColumn.setLength(200);
			tableDefinition.addColumn(short_hypyColumn);

			DBUtils.alterTable(conn, tableDefinition);
			conn.commit();

			Map<Long, String> dataMap = new HashMap<Long, String>();
			String sql = " select id, name from SYS_ORGANIZATION ";
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				dataMap.put(rs.getLong(1), rs.getString(2));
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			conn.setAutoCommit(false);

			psmt = conn.prepareStatement(" update SYS_ORGANIZATION set NAMEPINYIN = ? where ID = ? ");
			Set<Entry<Long, String>> entrySet = dataMap.entrySet();
			for (Entry<Long, String> entry : entrySet) {
				Long key = entry.getKey();
				String value = entry.getValue();
				psmt.setString(1, converterToFirstSpell(value, true));
				psmt.setLong(2, key);
				psmt.executeUpdate();
			}
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	public static void processSysTenant() {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName("SYS_TENANT");

			ColumnDefinition idColumn = new ColumnDefinition();
			idColumn.setColumnName("ID_");
			idColumn.setJavaType("Long");
			tableDefinition.setIdColumn(idColumn);

			ColumnDefinition short_hypyColumn = new ColumnDefinition();
			short_hypyColumn.setColumnName("NAMEPINYIN_");
			short_hypyColumn.setJavaType("String");
			short_hypyColumn.setLength(200);
			tableDefinition.addColumn(short_hypyColumn);

			DBUtils.alterTable(conn, tableDefinition);
			conn.commit();

			Map<Long, String> dataMap = new HashMap<Long, String>();
			String sql = " select ID_, NAME_ from SYS_TENANT ";
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				dataMap.put(rs.getLong(1), rs.getString(2));
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			logger.debug("size:" + dataMap.size());
			conn.setAutoCommit(false);
			psmt = conn.prepareStatement(" update SYS_TENANT set NAMEPINYIN_ = ? where ID_ = ? ");
			Set<Entry<Long, String>> entrySet = dataMap.entrySet();
			for (Entry<Long, String> entry : entrySet) {
				Long key = entry.getKey();
				String value = entry.getValue();
				psmt.setString(1, converterToFirstSpell(value, true));
				psmt.setLong(2, key);
				psmt.executeUpdate();
			}
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	public static void processSysTree() {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName("sys_tree");

			ColumnDefinition idColumn = new ColumnDefinition();
			idColumn.setColumnName("ID");
			idColumn.setJavaType("Long");
			tableDefinition.setIdColumn(idColumn);

			ColumnDefinition short_hypyColumn = new ColumnDefinition();
			short_hypyColumn.setColumnName("NAMEPINYIN");
			short_hypyColumn.setJavaType("String");
			short_hypyColumn.setLength(200);
			tableDefinition.addColumn(short_hypyColumn);

			DBUtils.alterTable(conn, tableDefinition);
			conn.commit();

			Map<Long, String> dataMap = new HashMap<Long, String>();
			String sql = " select id, name from sys_tree ";
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				dataMap.put(rs.getLong(1), rs.getString(2));
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			logger.debug("size:" + dataMap.size());
			conn.setAutoCommit(false);
			psmt = conn.prepareStatement(" update sys_tree set NAMEPINYIN = ? where ID = ? ");
			Set<Entry<Long, String>> entrySet = dataMap.entrySet();
			for (Entry<Long, String> entry : entrySet) {
				Long key = entry.getKey();
				String value = entry.getValue();
				psmt.setString(1, converterToFirstSpell(value, true));
				psmt.setLong(2, key);
				psmt.executeUpdate();
			}
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

	public static void processSysUser() {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName("SYS_USER");

			ColumnDefinition idColumn = new ColumnDefinition();
			idColumn.setColumnName("USERID");
			idColumn.setJavaType("String");
			tableDefinition.setIdColumn(idColumn);

			ColumnDefinition short_hypyColumn = new ColumnDefinition();
			short_hypyColumn.setColumnName("NAMEPINYIN");
			short_hypyColumn.setJavaType("String");
			short_hypyColumn.setLength(50);
			tableDefinition.addColumn(short_hypyColumn);

			DBUtils.alterTable(conn, tableDefinition);
			conn.commit();

			Map<String, String> dataMap = new HashMap<String, String>();
			String sql = " select USERID, USERNAME from SYS_USER ";
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next()) {
				dataMap.put(rs.getString(1), rs.getString(2));
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);

			conn.setAutoCommit(false);

			psmt = conn.prepareStatement(" update SYS_USER set NAMEPINYIN = ? where USERID = ? ");
			Set<Entry<String, String>> entrySet = dataMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				String value = entry.getValue();
				psmt.setString(1, converterToFirstSpell(value, true));
				psmt.setString(2, key);
				psmt.executeUpdate();
			}
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}
}
