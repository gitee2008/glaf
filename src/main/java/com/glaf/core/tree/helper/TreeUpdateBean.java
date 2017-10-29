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

package com.glaf.core.tree.helper;

import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.base.BaseTree;
import com.glaf.core.base.TreeModel;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.JdbcUtils;

public class TreeUpdateBean {
	protected static final Log logger = LogFactory.getLog(TreeUpdateBean.class);

	public String getTreeId(Map<Long, TreeModel> dataMap, TreeModel tree) {
		long parentId = tree.getParentId();
		TreeModel parent = dataMap.get(parentId);
		if (parent != null && parent.getId() > 0) {
			if (StringUtils.isEmpty(parent.getTreeId())) {
				return getTreeId(dataMap, parent) + tree.getId() + "|";
			}
			if (!parent.getTreeId().endsWith("|")) {
				parent.setTreeId(parent.getTreeId() + "|");
			}
			if (parent.getTreeId() != null) {
				return parent.getTreeId() + tree.getId() + "|";
			}
		} else {
			if (parentId == 0 || parentId == -1) {
				tree.setTreeId(tree.getId() + "|");
			}
		}
		return tree.getTreeId();
	}

	/**
	 * 
	 * @param conn
	 *            数据库连接
	 * @param tableName
	 *            树表名称
	 * @param primaryKeyColumn
	 *            主键列，如果和树id列是一样的，可以为空，否则必须是字符串类型的主键
	 * @param idColumn
	 *            树表的id字段，Long类型，必须的
	 * @param parentIdColumn
	 *            树表的parentId字段，Long类型，必须的
	 * @param treeIdColumn
	 *            树表的treeId字段，String类型，必须的
	 * @param levelColumn
	 *            树表的level字段，Integer类型，可以为空
	 * @param sqlCondition
	 *            过滤的SQL条件，可以为空
	 */
	public void updateTreeIds(Connection conn, String tableName, String primaryKeyColumn, String idColumn,
			String parentIdColumn, String treeIdColumn, String levelColumn, String sqlCondition) {
		boolean primaryKeyString = false;
		StringBuilder sqlBuffer = new StringBuilder();
		sqlBuffer.append(" select ").append(idColumn).append(" as \"id\", ").append(parentIdColumn)
				.append(" as \"parentId\", ").append(treeIdColumn).append(" as \"treeId\" ");
		if (primaryKeyColumn != null && !StringUtils.equalsIgnoreCase(primaryKeyColumn, idColumn)) {
			sqlBuffer.append(", ").append(primaryKeyColumn).append(" as \"pk\"");
			primaryKeyString = true;
		}

		sqlBuffer.append(" from ").append(tableName);

		if (StringUtils.isNotEmpty(sqlCondition)) {
			if (!StringUtils.startsWithIgnoreCase(sqlCondition.trim(), "where")) {
				sqlBuffer.append(" where 1=1 ");
			}
			if (StringUtils.startsWithIgnoreCase(sqlCondition.trim(), "and")) {
				sqlBuffer.append(sqlCondition);
			} else {
				sqlBuffer.append(" and " + sqlCondition);
			}
		}
		List<BaseTree> trees = new ArrayList<BaseTree>();
		Map<Long, String> treeIdMap = new HashMap<Long, String>();
		Map<Long, TreeModel> treeMap = new HashMap<Long, TreeModel>();
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			pstmt1 = conn.prepareStatement(sqlBuffer.toString());
			rs = pstmt1.executeQuery();
			while (rs.next()) {
				BaseTree tree = new BaseTree();
				BaseTree tree2 = new BaseTree();
				tree.setId(rs.getLong(1));
				tree.setParentId(rs.getLong(2));
				tree.setTreeId(rs.getString(3));
				if (primaryKeyString) {
					tree.setUid(rs.getString(4));
				}
				trees.add(tree);
				if (tree.getTreeId() != null) {
					treeIdMap.put(tree.getId(), tree.getTreeId());
				}
				tree2.setId(tree.getId());
				tree2.setParentId(tree.getParentId());
				treeMap.put(tree.getId(), tree2);
			}
			JdbcUtils.close(rs);
			JdbcUtils.close(pstmt1);

			if (trees.size() > 0) {
				List<BaseTree> updateList = new ArrayList<BaseTree>();
				for (BaseTree tree : trees) {
					if (tree.getId() == tree.getParentId()) {
						continue;
					}
					String treeId = this.getTreeId(treeMap, tree);
					if (StringUtils.isNotEmpty(treeId) && !StringUtils.equals(tree.getTreeId(), treeId)) {
						tree.setTreeId(treeId);
						updateList.add(tree);
					} else {
						if (tree.getParentId() == 0 || tree.getParentId() == -1) {
							tree.setTreeId(tree.getId() + "|");
							updateList.add(tree);
						}
					}
				}
				logger.debug("prepare update record size " + updateList.size());
				if (updateList.size() > 0) {
					sqlBuffer.delete(0, sqlBuffer.length());
					sqlBuffer.append(" update ").append(tableName).append(" set ").append(treeIdColumn).append(" = ? ");
					if (StringUtils.isNotEmpty(levelColumn)) {
						sqlBuffer.append(", ").append(levelColumn).append(" = ? ");
					}
					sqlBuffer.append(" where ");
					if (primaryKeyString) {
						sqlBuffer.append(primaryKeyColumn).append(" = ? ");
					} else {
						sqlBuffer.append(idColumn).append(" = ? ");
					}
					int index = 0;
					StringTokenizer token = null;
					pstmt2 = conn.prepareStatement(sqlBuffer.toString());
					for (BaseTree tree : updateList) {
						index++;
						pstmt2.setString(1, tree.getTreeId());
						if (StringUtils.isNotEmpty(levelColumn)) {
							token = new StringTokenizer(tree.getTreeId(), "|");
							pstmt2.setInt(2, token.countTokens());
						}
						if (StringUtils.isNotEmpty(levelColumn)) {
							if (primaryKeyString) {
								pstmt2.setString(3, tree.getUid());
							} else {
								pstmt2.setLong(3, tree.getId());
							}
						} else {
							if (primaryKeyString) {
								pstmt2.setString(2, tree.getUid());
							} else {
								pstmt2.setLong(2, tree.getId());
							}
						}
						pstmt2.addBatch();
						if (index > 0 && index % 500 == 0) {
							pstmt2.executeBatch();
						}
					}
					pstmt2.executeBatch();
					JdbcUtils.close(pstmt2);
				}
			}
			conn.commit();
		} catch (Exception ex) {
			logger.error("update treeid error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(pstmt1);
			JdbcUtils.close(pstmt2);
			JdbcUtils.close(rs);
		}
	}

	public void updateTreeIds(String systemName, String tableName, String primaryKeyColumn, String idColumn,
			String parentIdColumn, String treeIdColumn, String levelColumn, String sqlCondition) {
		long start = System.currentTimeMillis();
		Connection conn = null;
		try {
			conn = DBConnectionFactory.getConnection(systemName);
			this.updateTreeIds(conn, tableName, primaryKeyColumn, idColumn, parentIdColumn, treeIdColumn, levelColumn,
					sqlCondition);
		} catch (Exception ex) {
			logger.error("update treeid error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(conn);
		}
		long ts = System.currentTimeMillis() - start;
		logger.debug("总共用时(ms):" + ts);
	}

}
