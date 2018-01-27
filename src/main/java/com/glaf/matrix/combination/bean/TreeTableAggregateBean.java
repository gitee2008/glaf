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

package com.glaf.matrix.combination.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.DBConfiguration;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.dialect.Dialect;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.Database;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.entity.SqlExecutor;
import com.glaf.core.factory.TableFactory;
import com.glaf.core.jdbc.BatchUpdateBean;
import com.glaf.core.jdbc.BulkInsertBean;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.Authentication;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.JdbcUtils;
import com.glaf.core.util.JsonUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.QueryUtils;
import com.glaf.core.util.StringTools;

import com.glaf.matrix.combination.domain.TreeTableAggregate;
import com.glaf.matrix.combination.domain.TreeTableAggregateRule;

import com.glaf.matrix.combination.service.TreeTableAggregateService;
import com.glaf.matrix.combination.tree.TreeNode;
import com.glaf.matrix.combination.tree.TreeNodeSumBuilder;
import com.glaf.matrix.data.domain.ParameterLog;
import com.glaf.matrix.data.helper.MyBatisHelper;
import com.glaf.matrix.data.helper.SqlCriteriaHelper;
import com.glaf.matrix.data.util.ExceptionUtils;
import com.glaf.matrix.data.util.ParameterLogFactory;
import com.glaf.matrix.util.Constants;

public class TreeTableAggregateBean implements java.lang.Runnable {

	protected static Configuration conf = BaseConfiguration.create();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected SqlCriteriaHelper sqlHelper = new SqlCriteriaHelper();

	protected long databaseId;

	protected long treeTableAggregateId;

	protected boolean splitData;

	protected boolean result;

	protected Map<String, Object> parameter;

	public TreeTableAggregateBean() {

	}

	public TreeTableAggregateBean(long databaseId, long treeTableAggregateId, boolean splitData,
			Map<String, Object> parameter) {
		this.databaseId = databaseId;
		this.treeTableAggregateId = treeTableAggregateId;
		this.splitData = splitData;
		this.parameter = parameter;
	}

	/**
	 * 复制并且执行树表逐级汇总
	 * 
	 * @param databaseId
	 *            数据库编号，大于0是复制并执行，否则只复制不执行
	 * @param treeTableAggregateId
	 *            原始树表逐级汇总编号
	 * @param
	 * @return
	 */
	public long cloneAndExecute(long databaseId, long treeTableAggregateId, String actorId,
			Map<String, Object> parameter) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		TreeTableAggregateService treeTableAggregateService = ContextFactory.getBean("treeTableAggregateService");
		try {
			Database database = null;
			if (databaseId > 0) {
				database = databaseService.getDatabaseById(databaseId);
			}
			if (actorId != null) {
				TreeTableAggregate treeTableAggregate = treeTableAggregateService
						.getTreeTableAggregate(treeTableAggregateId);
				if (treeTableAggregate != null) {
					String sourceTableName = treeTableAggregate.getSourceTableName();
					String targetTableName = treeTableAggregate.getTargetTableName();
					String tmpSourceTable = "TMP_TREE_TABLE_AGGR" + treeTableAggregate.getId() + "_"
							+ String.valueOf(actorId.hashCode());// 临时来源表，数据结构及数据由使用方生成。
					String tmpTargetTable = "TMP2_TREE_TABLE_AGGR" + treeTableAggregate.getId() + "_"
							+ String.valueOf(actorId.hashCode());// 临时目标表，数据结构及数据由本程序生成。
					if (database != null && DBUtils.ORACLE.equals(database.getType())) {
						tmpSourceTable = "TMP_TTA" + treeTableAggregate.getId() + "_"
								+ String.valueOf(actorId.hashCode());// 临时来源表，数据结构及数据由使用方生成。
						tmpTargetTable = "TMP2_TTA" + treeTableAggregate.getId() + "_"
								+ String.valueOf(actorId.hashCode());// 临时目标表，数据结构及数据由本程序生成。
					}

					TreeTableAggregate tmpTarget = treeTableAggregateService
							.getTreeTableAggregateByTargetTableName(tmpTargetTable);
					if (tmpTarget != null) {
						treeTableAggregateService.deleteById(tmpTarget.getId());
					}
					if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
						String sqlCriteria = treeTableAggregate.getSqlCriteria();
						sqlCriteria = StringTools.replaceIgnoreCase(sqlCriteria, sourceTableName, tmpSourceTable);
						sqlCriteria = StringTools.replaceIgnoreCase(sqlCriteria, targetTableName, tmpTargetTable);
						treeTableAggregate.setSqlCriteria(sqlCriteria);
					}
					treeTableAggregate.setCreateBy(actorId);
					treeTableAggregate.setPrivateFlag(1);
					treeTableAggregate.setSourceTableName(tmpSourceTable);
					treeTableAggregate.setTargetTableName(tmpTargetTable);
					treeTableAggregateService.saveAs(treeTableAggregate);

					if (database != null) {
						ColumnDefinition idColumn = null;
						List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(database.getName(),
								targetTableName);// 获取原始目标表结构
						for (ColumnDefinition column : columns) {
							if (column.isPrimaryKey()) {
								idColumn = column;
								columns.remove(column);
								break;
							}
						}

						if (idColumn == null) {
							throw new RuntimeException(targetTableName + " primary key is null");
						}

						TableDefinition tableDefinition = new TableDefinition();
						tableDefinition.setTableName(tmpTargetTable);
						tableDefinition.setIdColumn(idColumn);

						ColumnDefinition col6 = new ColumnDefinition();
						col6.setColumnName("EX_JOBNO_");
						col6.setJavaType("String");
						col6.setLength(80);
						columns.add(col6);

						tableDefinition.setColumns(columns);// 临时表结构和原始目标表结构一致
						DBUtils.dropTable(database.getName(), tmpTargetTable);// 删除临时表结构及数据
						DBUtils.createTable(database.getName(), tableDefinition);// 重建临时表

						this.execute(database.getId(), treeTableAggregate.getId(), false, parameter);
					}
					return treeTableAggregate.getId();
				}
			}
			return -1;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute clone error", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 复制并且执行树表逐级汇总
	 * 
	 * @param databaseId
	 *            数据库编号，大于0是复制并执行，否则只复制不执行
	 * @param tableName
	 *            之前的汇总目标表
	 * @param
	 * @return
	 */
	public long cloneAndExecute(long databaseId, String tableName, String actorId, Map<String, Object> parameter) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		TreeTableAggregateService treeTableAggregateService = ContextFactory.getBean("treeTableAggregateService");
		try {
			Database database = null;
			if (databaseId > 0) {
				database = databaseService.getDatabaseById(databaseId);
			}
			if (actorId != null) {
				TreeTableAggregate treeTableAggregate = treeTableAggregateService
						.getTreeTableAggregateByTargetTableName(tableName);
				if (treeTableAggregate != null) {
					String sourceTableName = treeTableAggregate.getSourceTableName();
					String targetTableName = treeTableAggregate.getTargetTableName();
					String tmpSourceTable = "TMP_TREE_TABLE_AGGR" + treeTableAggregate.getId() + "_"
							+ String.valueOf(actorId.hashCode());// 临时来源表，数据结构及数据由使用方生成。
					String tmpTargetTable = "TMP2_TREE_TABLE_AGGR" + treeTableAggregate.getId() + "_"
							+ String.valueOf(actorId.hashCode());// 临时目标表，数据结构及数据由本程序生成。
					if (database != null && DBUtils.ORACLE.equals(database.getType())) {
						tmpSourceTable = "TMP_TTA" + treeTableAggregate.getId() + "_"
								+ String.valueOf(actorId.hashCode());// 临时来源表，数据结构及数据由使用方生成。
						tmpTargetTable = "TMP2_TTA" + treeTableAggregate.getId() + "_"
								+ String.valueOf(actorId.hashCode());// 临时目标表，数据结构及数据由本程序生成。
					}

					TreeTableAggregate tmpTarget = treeTableAggregateService
							.getTreeTableAggregateByTargetTableName(tmpTargetTable);
					if (tmpTarget != null) {
						treeTableAggregateService.deleteById(tmpTarget.getId());
					}

					if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
						String sqlCriteria = treeTableAggregate.getSqlCriteria();
						sqlCriteria = StringTools.replaceIgnoreCase(sqlCriteria, sourceTableName, tmpSourceTable);
						sqlCriteria = StringTools.replaceIgnoreCase(sqlCriteria, targetTableName, tmpTargetTable);
						treeTableAggregate.setSqlCriteria(sqlCriteria);
					}

					treeTableAggregate.setCreateBy(actorId);
					treeTableAggregate.setPrivateFlag(1);
					treeTableAggregate.setSourceTableName(tmpSourceTable);
					treeTableAggregate.setTargetTableName(tmpTargetTable);
					treeTableAggregateService.saveAs(treeTableAggregate);

					if (database != null) {
						ColumnDefinition idColumn = null;
						List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(database.getName(),
								targetTableName);// 获取原始目标表结构
						for (ColumnDefinition column : columns) {
							if (column.isPrimaryKey()) {
								idColumn = column;
								columns.remove(column);
								break;
							}
						}

						if (idColumn == null) {
							throw new RuntimeException(targetTableName + " primary key is null");
						}

						TableDefinition tableDefinition = new TableDefinition();
						tableDefinition.setTableName(tmpTargetTable);
						tableDefinition.setIdColumn(idColumn);

						ColumnDefinition col6 = new ColumnDefinition();
						col6.setColumnName("EX_JOBNO_");
						col6.setJavaType("String");
						col6.setLength(80);
						columns.add(col6);

						tableDefinition.setColumns(columns);// 临时表结构和原始目标表结构一致
						DBUtils.dropTable(database.getName(), tmpTargetTable);// 删除临时表结构及数据
						DBUtils.createTable(database.getName(), tableDefinition);// 重建临时表

						logger.debug(">----------------------execute-----------------------");
						this.execute(database.getId(), treeTableAggregate.getId(), false, parameter);
					}
					return treeTableAggregate.getId();
				}
			}
			return -1;
		} catch (Exception ex) {
			// ex.printStackTrace();
			logger.error("execute clone error", ex);
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean execute(Database database, TreeTableAggregate treeTableAggregate, String splitColumn, int splitValue,
			Map<String, Object> params) {
		if (!(StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "tree_table_count")
				|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "useradd_")
				|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "etl_")
				|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "sync_")
				|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "tree_table_")
				|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "tmp"))) {
			return false;
		}

		logger.debug("----------------------------------------------------------------");
		logger.debug("----------------------TreeTableAggregateBean execute------------");
		logger.debug("----------------------------------------------------------------");

		Map<String, String> sourceMD5Map = new HashMap<String, String>();
		Map<String, String> targetMD5Map = new HashMap<String, String>();
		List<String> syncColumns = StringTools.splitLowerCase(treeTableAggregate.getSyncColumns());
		List<TreeTableAggregateRule> rules = treeTableAggregate.getRules();
		Collections.sort(syncColumns);// 排序，确保修改字段顺序时不影响结果
		logger.debug("sync columns:" + syncColumns);
		logger.debug("params:" + params);
		logger.debug("rules:" + rules);

		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		String jobNo = treeTableAggregate.getTargetTableName() + "_" + DateUtils.getNowYearMonthDayHHmmss();

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("_date_", now);
		parameter.put("_now_", now);
		parameter.put("_year_", year);
		parameter.put("_month_", month);
		parameter.put("_week_", week);
		parameter.put("_day_", day);
		parameter.put("_nowYearMonth_", DateUtils.getNowYearMonth());
		parameter.put("_nowYearMonthDay_", DateUtils.getNowYearMonthDay());

		if (params != null) {
			params.putAll(parameter);
		} else {
			params = new HashMap<String, Object>();
			params.putAll(parameter);
		}

		int batchSize = conf.getInt("tree_table_aggregate_batchSize", 10000);

		String sql = null;
		String key = null;
		Object value = null;
		String sourceMD5 = null;
		String targetMD5 = null;
		SqlExecutor sqlExecutor = null;
		List<Map<String, Object>> source_list = null;
		List<Map<String, Object>> target_list = null;
		StringBuffer buffer = new StringBuffer();
		MyBatisHelper helper = new MyBatisHelper();
		BulkInsertBean bulkInsertBean = new BulkInsertBean();
		BatchUpdateBean batchUpdateBean = new BatchUpdateBean();
		List<Map<String, Object>> insert_list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> update_list = new ArrayList<Map<String, Object>>();

		SqlExecutor dyncSqlExecutor = null;
		Connection sourceConn = null;
		PreparedStatement sourcePsmt = null;
		ResultSet sourceRS = null;

		Connection targetConn = null;
		PreparedStatement targetPsmt = null;
		ResultSet targetRS = null;
		Dialect dialect = null;
		try {

			dyncSqlExecutor = sqlHelper.buildSql("treetable_aggregate", String.valueOf(treeTableAggregate.getId()),
					params);

			ParameterLog log1 = new ParameterLog();
			log1.setTitle(treeTableAggregate.getTitle());
			log1.setBusinessKey("treetable_aggregate_" + String.valueOf(treeTableAggregate.getId()));
			log1.setContent(JsonUtils.toJSONObject(params).toJSONString());
			log1.setJobNo(jobNo);
			log1.setType("params");
			log1.setCreateBy(Authentication.getAuthenticatedActorId());
			log1.setCreateTime(new Date());
			ParameterLogFactory.getInstance().addLog(log1);

			if (dyncSqlExecutor.getSql() != null && dyncSqlExecutor.getParameter() != null) {
				JSONObject json = new JSONObject();
				json.put("sql", dyncSqlExecutor.getSql());
				json.put("params", dyncSqlExecutor.getParameter());
				ParameterLog log2 = new ParameterLog();
				log2.setTitle(treeTableAggregate.getTitle());
				log2.setBusinessKey("treetable_aggregate_" + String.valueOf(treeTableAggregate.getId()));
				log2.setContent(json.toJSONString());
				log2.setJobNo(jobNo);
				log2.setType("dynamic_sql");
				log2.setCreateBy(Authentication.getAuthenticatedActorId());
				log2.setCreateTime(new Date());
				ParameterLogFactory.getInstance().addLog(log2);
			}

			sourceConn = DBConnectionFactory.getConnection(database.getName());
			targetConn = DBConnectionFactory.getConnection(database.getName());

			dialect = DBConfiguration.getDatabaseDialect(sourceConn);
			if (dialect != null && dialect.supportsPhysicalPage()) {

			}

			logger.debug("目标数据库已经连接：" + database.getDbname());
			List<ColumnDefinition> sourceColumns = DBUtils.getColumnDefinitions(sourceConn,
					treeTableAggregate.getSourceTableName());
			List<ColumnDefinition> targetColumns = DBUtils.getColumnDefinitions(targetConn,
					treeTableAggregate.getTargetTableName());
			ColumnDefinition idColumn = null;

			StringBuilder selectBuffer = new StringBuilder();

			for (ColumnDefinition column : sourceColumns) {
				if (syncColumns.contains(column.getColumnName().toLowerCase())) {
					selectBuffer.append(column.getColumnName()).append(", ");
				}
			}

			selectBuffer.delete(selectBuffer.length() - 2, selectBuffer.length());

			if (idColumn == null) {
				Map<String, ColumnDefinition> columnMap = new HashMap<String, ColumnDefinition>();
				for (ColumnDefinition col : sourceColumns) {
					columnMap.put(col.getColumnName().toLowerCase(), col);
				}
				/**
				 * 物理主键以配置的字段为准，不以来源表的物理主键。
				 */
				if (StringUtils.isNotEmpty(treeTableAggregate.getSourceIdColumn())) {
					idColumn = columnMap.get(treeTableAggregate.getSourceIdColumn().toLowerCase());
				}
			}

			if (idColumn == null) {
				throw new RuntimeException(treeTableAggregate.getSourceTableName() + " primary key is null");
			}

			int maxIndexId = 0;
			int minIndexId = 0;
			int maxForEach = 1;

			String idColumnName = idColumn.getColumnName().toLowerCase();

			if ((StringUtils.equalsIgnoreCase(idColumn.getJavaType(), "Integer")
					|| StringUtils.equalsIgnoreCase(idColumn.getJavaType(), "Long"))) {
				sql = " select max(" + idColumn.getColumnName() + "), min(" + idColumn.getColumnName() + ") from "
						+ treeTableAggregate.getSourceTableName() + " where 1=1 ";

				if (StringUtils.isNotEmpty(splitColumn) && splitValue > 0) {
					sql = sql + " and " + splitColumn + " = " + splitValue;
				}
				if (params != null && !params.isEmpty()) {
					sql = QueryUtils.replaceBlankParas(sql, params);
				}
				sourcePsmt = sourceConn.prepareStatement(sql);
				sourceRS = sourcePsmt.executeQuery();
				if (sourceRS.next()) {
					maxIndexId = sourceRS.getInt(1);
					minIndexId = sourceRS.getInt(2);
				}
				JdbcUtils.close(sourceRS);
				JdbcUtils.close(sourcePsmt);
				if ((maxIndexId - minIndexId) < batchSize * 500) {
					maxForEach = maxIndexId / batchSize + 1;
				}
			}

			sql = " select count(*) from " + treeTableAggregate.getSourceTableName() + " where 1=1 ";
			if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
				sql = sql + " and " + treeTableAggregate.getSqlCriteria();
			}

			if (params != null && !params.isEmpty()) {
				sql = QueryUtils.replaceBlankParas(sql, params);
			}

			if (StringUtils.isNotEmpty(splitColumn) && splitValue > 0) {
				sql = sql + " and " + splitColumn + " = " + splitValue;
			}

			if (!DBUtils.isLegalQuerySql(sql)) {
				throw new RuntimeException("sql statement illegal");
			}

			int totalResult = 0;
			boolean skipResult = false;// 是否使用JDBC游标分页
			// sourcePsmt = sourceConn.prepareStatement(sql);
			logger.debug("dync sql:" + sql + dyncSqlExecutor.getSql());
			sourcePsmt = sourceConn.prepareStatement(sql + dyncSqlExecutor.getSql());
			if (dyncSqlExecutor.getParameter() != null) {
				logger.debug("dync param:" + dyncSqlExecutor.getParameter());
				List<Object> values = (List<Object>) dyncSqlExecutor.getParameter();
				JdbcUtils.fillStatement(sourcePsmt, values);
			}
			sourceRS = sourcePsmt.executeQuery();
			if (sourceRS.next()) {
				totalResult = sourceRS.getInt(1);
			}
			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);

			// skipResult = true;

			if (totalResult > Constants.MAX_TREE_NODE) {// 不能超过最大记录，否则构成树时会造成内存溢出OOM
				// throw new RuntimeException("数据量过大,需要按条件分段处理.");
				ExceptionUtils.addMsg("treetable_aggregate_" + treeTableAggregate.getId(), "数据量过大,需要按条件分段处理.");
				return false;
			}

			/**
			 * 总记录数很小就不分页，一次处理全部记录
			 */
			if (totalResult <= batchSize) {
				maxForEach = 1;
			}

			/**
			 * 如果主键不连续，跨度太大，使用JDBC游标分页
			 */
			if (maxForEach > 200) {
				skipResult = true;
			}

			/**
			 * 如果主键是字符串并且总记录数超过批处理大小，使用JDBC游标分页
			 */
			if (totalResult > batchSize && StringUtils.equals(idColumn.getJavaType(), "String")) {
				skipResult = true;
			}

			// skipResult = true;

			boolean deleteFetch = false;
			if (StringUtils.equalsIgnoreCase(treeTableAggregate.getDeleteFetch(), "Y")) {
				if (StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "useradd_")
						|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "etl_")
						|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "sync_")
						|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "tree_table_")
						|| StringUtils.startsWithIgnoreCase(treeTableAggregate.getTargetTableName(), "tmp")) {
					sql = " delete from " + treeTableAggregate.getTargetTableName();
					if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
						sql = sql + " where 1=1 and " + treeTableAggregate.getSqlCriteria();
					}
					if (params != null && !params.isEmpty()) {
						sql = QueryUtils.replaceBlankParas(sql, params);
					}
					logger.info("execute delete sql:" + sql);
					DBUtils.executeSchemaResourceIgnoreException(targetConn, sql);
					// targetConn.commit();
					deleteFetch = true;

					JSONObject json = new JSONObject();
					json.put("sql", sql);
					ParameterLog log2 = new ParameterLog();
					log2.setTitle(treeTableAggregate.getTitle());
					log2.setBusinessKey("treetable_aggregate_" + String.valueOf(treeTableAggregate.getId()));
					log2.setContent(json.toJSONString());
					log2.setJobNo(jobNo);
					log2.setType("delete_sql");
					log2.setCreateBy(Authentication.getAuthenticatedActorId());
					log2.setCreateTime(new Date());
					ParameterLogFactory.getInstance().addLog(log2);

				}
			}

			if (skipResult) {
				int firstResult = 0;

				logger.debug("totalResult size:" + totalResult);
				List<String> rowKeys = new ArrayList<String>();
				StringBuffer sqlBuffer = new StringBuffer();

				while (firstResult <= totalResult) {
					sourceMD5Map.clear();
					targetMD5Map.clear();
					rowKeys.clear();

					sql = " select " + selectBuffer.toString() + " from " + treeTableAggregate.getSourceTableName()
							+ " where 1=1 ";
					if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
						sql = sql + " and " + treeTableAggregate.getSqlCriteria();
					}

					if (params != null && !params.isEmpty()) {
						sql = QueryUtils.replaceBlankParas(sql, params);
					}

					if (StringUtils.isNotEmpty(splitColumn) && splitValue > 0) {
						sql = sql + " and " + splitColumn + " = " + splitValue;
					}

					sqlExecutor = DBUtils.replaceSQL(sql, params);

					String sql2 = sqlExecutor.getSql();
					if (StringUtils.containsIgnoreCase(sql2, " where ")) {
						sql2 += dyncSqlExecutor.getSql();
					} else {
						sql2 = sql2 + " where 1=1 " + dyncSqlExecutor.getSql();
					}

					logger.debug("source query sql:" + sql2);

					sourcePsmt = sourceConn.prepareStatement(sql2);
					if (sqlExecutor.getParameter() != null) {
						List<Object> values = (List<Object>) sqlExecutor.getParameter();
						if (dyncSqlExecutor != null) {
							List<Object> values2 = (List<Object>) dyncSqlExecutor.getParameter();
							if (values2 != null) {
								values.addAll(values2);
							}
						}
						JdbcUtils.fillStatement(sourcePsmt, values);

						if (values != null) {
							JSONObject json = new JSONObject();
							json.put("sql", sqlExecutor.getSql() + dyncSqlExecutor.getSql());
							json.put("params", values);
							ParameterLog log2 = new ParameterLog();
							log2.setTitle(treeTableAggregate.getTitle());
							log2.setBusinessKey("treetable_aggregate_" + String.valueOf(treeTableAggregate.getId()));
							log2.setContent(json.toJSONString());
							log2.setJobNo(jobNo);
							log2.setType("query_source_sql");
							log2.setCreateBy(Authentication.getAuthenticatedActorId());
							log2.setCreateTime(new Date());
							ParameterLogFactory.getInstance().addLog(log2);
						}
					}

					sourceRS = sourcePsmt.executeQuery();
					// JdbcUtils.skipRows(sourceRS, firstResult);// 跳过其中的记录
					if (sourceRS.getType() != ResultSet.TYPE_FORWARD_ONLY) {
						if (firstResult != 0) {
							sourceRS.absolute(firstResult);
							logger.debug("已经定位到:" + firstResult);
						}
					} else {
						for (int i = 0; i < firstResult; i++) {
							sourceRS.next();
						}
						logger.debug("已经跳过:" + firstResult);
					}
					logger.debug("batchSize:" + batchSize);
					source_list = helper.getResults(sourceRS, batchSize);
					JdbcUtils.close(sourceRS);
					JdbcUtils.close(sourcePsmt);

					logger.debug("firstResult:" + firstResult);

					firstResult = firstResult + batchSize;

					if (source_list != null && !source_list.isEmpty()) {
						logger.debug("偏移量:" + firstResult);
						logger.debug("源表记录数:" + source_list.size());
						for (Map<String, Object> dataMap : source_list) {
							buffer.delete(0, buffer.length());
							key = ParamUtils.getString(dataMap, idColumnName);
							if (StringUtils.isNotEmpty(key)) {
								if (StringUtils.isNumeric(key)) {
									if (key.length() < 8) {
										dataMap.put("ex_syncid_", Double.parseDouble(database.getIntToken()
												+ StringTools.getDigit7Id(Integer.parseInt(key))));
									} else {
										dataMap.put("ex_syncid_",
												Double.parseDouble(database.getIntToken() + key));
									}
								}
								for (String syncColumn : syncColumns) {
									value = dataMap.get(syncColumn);
									if (value != null) {
										if (value instanceof Date) {
											Date date = (Date) value;
											buffer.append(date.getTime()).append("_");
										} else {
											buffer.append(value).append("_");
										}
									} else {
										buffer.append("null").append("_");
									}
								}
								rowKeys.add(key);
								sourceMD5Map.put(key,
										DigestUtils.sha1Hex(buffer.toString()) + "_" + buffer.toString().hashCode());
							}
						}

						if (!deleteFetch) {// 判断目标表是否存在指定记录
							sqlBuffer.delete(0, sqlBuffer.length());
							sqlBuffer.append(" select ").append(selectBuffer.toString()).append(" from ")
									.append(treeTableAggregate.getTargetTableName()).append(" E where 1=1 ");

							if ((StringUtils.equalsIgnoreCase(idColumn.getJavaType(), "Integer")
									|| StringUtils.equalsIgnoreCase(idColumn.getJavaType(), "Long"))) {
								sqlBuffer.append(QueryUtils.getNumSQLCondition(rowKeys, "E", idColumn.getColumnName()));
							} else {
								sqlBuffer.append(QueryUtils.getSQLCondition(rowKeys, "E", idColumn.getColumnName()));
							}

							sql = sqlBuffer.toString();
							// logger.debug("target query sql:" + sql);

							sqlExecutor = DBUtils.replaceSQL(sql, parameter);

							sql2 = sqlExecutor.getSql();

							targetPsmt = targetConn.prepareStatement(sql2);
							targetRS = targetPsmt.executeQuery();
							target_list = helper.getResults(targetRS);
							JdbcUtils.close(targetRS);
							JdbcUtils.close(targetPsmt);

							if (target_list != null && !target_list.isEmpty()) {
								logger.debug("目标表记录数：" + target_list.size());
								for (Map<String, Object> dataMap : target_list) {
									buffer.delete(0, buffer.length());
									key = ParamUtils.getString(dataMap, idColumnName);
									if (StringUtils.isNotEmpty(key)) {
										for (String syncColumn : syncColumns) {
											value = dataMap.get(syncColumn);
											if (value != null) {
												if (value instanceof Date) {
													Date date = (Date) value;
													buffer.append(date.getTime()).append("_");
												} else {
													buffer.append(value).append("_");
												}
											} else {
												buffer.append("null").append("_");
											}
										}
										targetMD5Map.put(key, DigestUtils.sha1Hex(buffer.toString()) + "_"
												+ buffer.toString().hashCode());
									}
								}
							}
						}

						logger.debug("源表记录数：" + source_list.size());
						logger.debug("源表MD5记录数：" + sourceMD5Map.size());
						logger.debug("目标表MD5记录数：" + targetMD5Map.size());
						for (Map<String, Object> dataMap : source_list) {
							key = ParamUtils.getString(dataMap, idColumnName);
							if (StringUtils.isNotEmpty(key)) {
								sourceMD5 = sourceMD5Map.get(key);
								targetMD5 = targetMD5Map.get(key);
								/**
								 * 目标表记录的MD5值存在并且和源表记录的MD5相等，可以直接跳过不用做任何处理
								 */
								if (StringUtils.isNotEmpty(targetMD5) && StringUtils.equals(sourceMD5, targetMD5)) {
									continue;
								}

								if (StringUtils.isNotEmpty(targetMD5) && !StringUtils.equals(sourceMD5, targetMD5)) {// 记录存在MD5不相等
									dataMap.put("jobno_", jobNo);
									dataMap.put("ex_jobno_", jobNo);
									update_list.add(dataMap);
								} else {
									dataMap.put("jobno_", jobNo);
									dataMap.put("ex_jobno_", jobNo);
									insert_list.add(dataMap);// 记录不存在
								}
							}
						}
					}
				}
			} else {
				/**
				 * 根据主键分段导出
				 */
				for (int i = 0; i < maxForEach; i++) {

					sourceMD5Map.clear();
					targetMD5Map.clear();

					if (StringUtils.equals(idColumn.getJavaType(), "String")) {
						sql = " select " + selectBuffer.toString() + " from " + treeTableAggregate.getSourceTableName()
								+ " where 1=1 ";
						if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
							sql = sql + " and " + treeTableAggregate.getSqlCriteria();
						}
					} else {
						sql = " select " + selectBuffer.toString() + " from " + treeTableAggregate.getSourceTableName()
								+ " where " + idColumn.getColumnName() + " >= " + (minIndexId + i * batchSize) + " and "
								+ idColumn.getColumnName() + " < " + (minIndexId + (i + 1) * batchSize);
						if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
							sql = sql + " and " + treeTableAggregate.getSqlCriteria();
						}
					}

					if (maxForEach == 1) {
						sql = " select " + selectBuffer.toString() + " from " + treeTableAggregate.getSourceTableName()
								+ " where 1=1 ";
						if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
							sql = sql + " and " + treeTableAggregate.getSqlCriteria();
						}
					}

					if (params != null && !params.isEmpty()) {
						sql = QueryUtils.replaceBlankParas(sql, params);
					}

					if (StringUtils.isNotEmpty(splitColumn) && splitValue > 0) {
						sql = sql + " and " + splitColumn + " = " + splitValue;
					}

					logger.debug("query sql:" + sql);

					sqlExecutor = DBUtils.replaceSQL(sql, parameter);

					String sql2 = sqlExecutor.getSql();
					if (StringUtils.containsIgnoreCase(sql2, " where ")) {
						sql2 += dyncSqlExecutor.getSql();
					} else {
						sql2 = sql2 + " where 1=1 " + dyncSqlExecutor.getSql();
					}

					logger.debug("dync query sql:" + sql);

					sourcePsmt = sourceConn.prepareStatement(sql2);
					if (sqlExecutor.getParameter() != null) {
						List<Object> values = (List<Object>) sqlExecutor.getParameter();
						if (dyncSqlExecutor != null) {
							List<Object> values2 = (List<Object>) dyncSqlExecutor.getParameter();
							if (values2 != null) {
								values.addAll(values2);
							}
						}
						JdbcUtils.fillStatement(sourcePsmt, values);

						if (values != null) {
							JSONObject json = new JSONObject();
							json.put("sql", sqlExecutor.getSql() + dyncSqlExecutor.getSql());
							json.put("params", values);
							ParameterLog log2 = new ParameterLog();
							log2.setTitle(treeTableAggregate.getTitle());
							log2.setBusinessKey("treetable_aggregate_" + String.valueOf(treeTableAggregate.getId()));
							log2.setContent(json.toJSONString());
							log2.setJobNo(jobNo);
							log2.setType("query_source_sql");
							log2.setCreateBy(Authentication.getAuthenticatedActorId());
							log2.setCreateTime(new Date());
							ParameterLogFactory.getInstance().addLog(log2);
						}
					}
					sourceRS = sourcePsmt.executeQuery();

					source_list = helper.getResults(sourceRS);
					JdbcUtils.close(sourceRS);
					JdbcUtils.close(sourcePsmt);

					if (source_list != null && !source_list.isEmpty()) {
						logger.debug("源表记录数：" + source_list.size());
						for (Map<String, Object> dataMap : source_list) {
							buffer.delete(0, buffer.length());
							key = ParamUtils.getString(dataMap, idColumnName);
							if (StringUtils.isNotEmpty(key)) {
								if (StringUtils.isNumeric(key)) {
									if (key.length() < 8) {
										dataMap.put("ex_syncid_", Double.parseDouble(database.getIntToken()
												+ StringTools.getDigit7Id(Integer.parseInt(key))));
									} else {
										dataMap.put("ex_syncid_",
												Double.parseDouble(database.getIntToken() + key));
									}
								}
								for (String syncColumn : syncColumns) {
									value = dataMap.get(syncColumn);
									if (value != null) {
										if (value instanceof Date) {
											Date date = (Date) value;
											buffer.append(date.getTime()).append("_");
										} else {
											buffer.append(value).append("_");
										}
									} else {
										buffer.append("null").append("_");
									}
								}
								sourceMD5Map.put(key,
										DigestUtils.sha1Hex(buffer.toString()) + "_" + buffer.toString().hashCode());
							}
						}

						if (StringUtils.equals(idColumn.getJavaType(), "String")) {
							sql = " select " + selectBuffer.toString() + " from "
									+ treeTableAggregate.getTargetTableName();
						} else {
							sql = " select " + selectBuffer.toString() + " from "
									+ treeTableAggregate.getTargetTableName() + " where " + idColumn.getColumnName()
									+ " >= " + (minIndexId + i * batchSize) + " and " + idColumn.getColumnName() + " < "
									+ (minIndexId + (i + 1) * batchSize);
						}

						if (maxForEach == 1) {
							sql = " select " + selectBuffer.toString() + " from "
									+ treeTableAggregate.getTargetTableName();
						}

						logger.debug("target query sql:" + sql);

						sqlExecutor = DBUtils.replaceSQL(sql, parameter);

						sql2 = sqlExecutor.getSql();

						targetPsmt = targetConn.prepareStatement(sql2);
						targetRS = targetPsmt.executeQuery();
						target_list = helper.getResults(targetRS);
						JdbcUtils.close(targetRS);
						JdbcUtils.close(targetPsmt);

						if (target_list != null && !target_list.isEmpty()) {
							logger.debug("目标表记录数：" + target_list.size());
							for (Map<String, Object> dataMap : target_list) {
								buffer.delete(0, buffer.length());
								key = ParamUtils.getString(dataMap, idColumnName);
								if (StringUtils.isNotEmpty(key)) {
									for (String syncColumn : syncColumns) {
										value = dataMap.get(syncColumn);
										if (value != null) {
											if (value instanceof Date) {
												Date date = (Date) value;
												buffer.append(date.getTime()).append("_");
											} else {
												buffer.append(value).append("_");
											}
										} else {
											buffer.append("null").append("_");
										}
									}
									targetMD5Map.put(key, DigestUtils.sha1Hex(buffer.toString()) + "_"
											+ buffer.toString().hashCode());
								}
							}
						}

						logger.debug("源表记录数：" + source_list.size());
						logger.debug("源表MD5记录数：" + sourceMD5Map.size());
						logger.debug("目标表MD5记录数：" + targetMD5Map.size());

						for (Map<String, Object> dataMap : source_list) {
							key = ParamUtils.getString(dataMap, idColumnName);
							if (StringUtils.isNotEmpty(key)) {
								sourceMD5 = sourceMD5Map.get(key);
								targetMD5 = targetMD5Map.get(key);
								/**
								 * 目标表记录的MD5值存在并且和源表记录的MD5相等，可以直接跳过不用做任何处理
								 */
								if (StringUtils.isNotEmpty(targetMD5) && StringUtils.equals(sourceMD5, targetMD5)) {
									continue;
								}

								if (StringUtils.isNotEmpty(targetMD5) && !StringUtils.equals(sourceMD5, targetMD5)) {// 记录存在MD5不相等
									dataMap.put("ex_jobno_", jobNo);
									dataMap.put("jobno_", jobNo);
									update_list.add(dataMap);
								} else {
									dataMap.put("ex_jobno_", jobNo);
									dataMap.put("jobno_", jobNo);
									insert_list.add(dataMap);// 记录不存在
								}
							}
						}
					}
				}
			}

			logger.debug("insert list size:" + insert_list.size());
			logger.debug("update list size:" + update_list.size());

			targetConn.setAutoCommit(false);

			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName(treeTableAggregate.getTargetTableName());
			tableDefinition.setColumns(targetColumns);

			if (!insert_list.isEmpty()) {
				Integer intValue = null;
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (int k = 0, len = insert_list.size(); k < len; k++) {
					Map<String, Object> dataMap = insert_list.get(k);
					if (splitColumn != null && dataMap.get(splitColumn) != null) {
						intValue = (Integer) dataMap.get(splitColumn);
						if (intValue != splitValue) {
							continue;
						}
					}
					list.add(dataMap);
				}
				bulkInsertBean.bulkInsert(null, targetConn, tableDefinition, list);
				insert_list.clear();
				list.clear();
			}

			if (!update_list.isEmpty()) {
				Integer intValue = null;
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (int k = 0, len = update_list.size(); k < len; k++) {
					Map<String, Object> dataMap = update_list.get(k);
					if (splitColumn != null && dataMap.get(splitColumn) != null) {
						intValue = (Integer) dataMap.get(splitColumn);
						if (intValue != splitValue) {
							continue;
						}
					}
					list.add(dataMap);
				}
				batchUpdateBean.executeBatch(null, targetConn, tableDefinition, list);
				update_list.clear();
				list.clear();
			}

			targetConn.commit();

			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(sourceConn);

			JdbcUtils.close(targetRS);
			JdbcUtils.close(targetPsmt);
			JdbcUtils.close(targetConn);

			logger.debug("数据库连接关闭成功。");

			insert_list.clear();
			update_list.clear();
			sourceMD5Map.clear();
			targetMD5Map.clear();

			if (rules != null && !rules.isEmpty()) {
				long startTs = System.currentTimeMillis();
				for (TreeTableAggregateRule rule : rules) {
					if (StringUtils.isNotEmpty(rule.getAggregateType())) {
						if (StringUtils.equals("SUM", rule.getAggregateType())) {
							// 处理逐级汇总规则
							this.sum(database, treeTableAggregate, rule, params);
							long ts = System.currentTimeMillis() - startTs;
							logger.debug("处理规则'" + rule.getTitle() + "'用时（毫秒）:" + ts);
							startTs = System.currentTimeMillis();
						}
					}
				}
			}

			return true;
		} catch (Exception ex) {
			ExceptionUtils.addMsg("treetable_aggregate_" + treeTableAggregate.getId(), ex.getMessage());
			logger.error("execute sql error", ex);
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			insert_list.clear();
			update_list.clear();
			sourceMD5Map.clear();
			targetMD5Map.clear();
			insert_list = null;
			update_list = null;
			source_list = null;
			target_list = null;
			sourceMD5Map = null;
			targetMD5Map = null;

			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(sourceConn);

			JdbcUtils.close(targetRS);
			JdbcUtils.close(targetPsmt);
			JdbcUtils.close(targetConn);
		}
	}

	public boolean execute(long databaseId, long treeTableAggregateId, boolean splitData,
			Map<String, Object> parameter) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		TreeTableAggregateService treeTableAggregateService = ContextFactory.getBean("treeTableAggregateService");
		try {
			Database database = databaseService.getDatabaseById(databaseId);

			TreeTableAggregate treeTableAggregate = treeTableAggregateService
					.getTreeTableAggregate(treeTableAggregateId);
			if (treeTableAggregate != null) {
				ColumnDefinition idColumn = null;
				List<ColumnDefinition> sourceColumns = DBUtils.getColumnDefinitions(database.getName(),
						treeTableAggregate.getSourceTableName());
				Map<String, ColumnDefinition> columnMap = new HashMap<String, ColumnDefinition>();
				for (ColumnDefinition col : sourceColumns) {
					columnMap.put(col.getColumnName().toLowerCase(), col);
				}
				TableDefinition tableDefinition = new TableDefinition();
				tableDefinition.setTableName(treeTableAggregate.getTargetTableName());

				List<ColumnDefinition> columns = tableDefinition.getColumns();
				List<String> syncColumns = StringTools.split(treeTableAggregate.getSyncColumns());

				/**
				 * 同步的列将成为目标表的字段，包含树型结构必须的字段和需要抽取的字段
				 */
				for (String syncColumn : syncColumns) {
					ColumnDefinition col = columnMap.get(syncColumn.toLowerCase());
					if (col != null) {
						/**
						 * 新目标表的主键以配置的值为准，数据类型即配置字段的类型
						 */
						if (StringUtils.equalsIgnoreCase(col.getColumnName(), treeTableAggregate.getSourceIdColumn())) {
							col.setPrimaryKey(true);
							idColumn = col;
							tableDefinition.setIdColumn(col);
							continue;
						} else {
							col.setPrimaryKey(false);
						}
						columns.add(col);
					}
				}

				if (idColumn == null) {
					if (StringUtils.isNotEmpty(treeTableAggregate.getSourceIdColumn())) {
						idColumn = columnMap.get(treeTableAggregate.getSourceIdColumn().toLowerCase());
						tableDefinition.setIdColumn(idColumn);
					}
				}

				if (tableDefinition.getIdColumn() != null) {
					/**
					 * 同步规则中定义的目标字段也将成为目标表的字段
					 */
					List<TreeTableAggregateRule> rules = treeTableAggregate.getRules();
					if (rules != null && !rules.isEmpty()) {
						for (TreeTableAggregateRule rule : rules) {
							if (rule.getLocked() == 1) {
								continue;
							}
							ColumnDefinition column = new ColumnDefinition();
							column.setName(rule.getTargetColumnName());
							column.setColumnName(rule.getTargetColumnName());
							column.setJavaType(rule.getTargetColumnType());
							if (StringUtils.equals(rule.getTargetColumnType(), "String")) {
								column.setLength(250);
							}
							columns.add(column);
						}
					}

					ColumnDefinition syntagmatic = new ColumnDefinition();
					syntagmatic.setColumnName("EX_SYNCID_");
					syntagmatic.setJavaType("Double");
					columns.add(syntagmatic);

					tableDefinition.setColumns(columns);

					treeTableAggregate.setColumns(columns);

					if (DBUtils.tableExists(database.getName(), treeTableAggregate.getTargetTableName())) {
						DBUtils.alterTable(database.getName(), tableDefinition);
					} else {
						DBUtils.createTable(database.getName(), tableDefinition);
					}

					TableFactory.clearCache("default", treeTableAggregate.getSourceTableName());
					TableFactory.clearCache(database.getName(), treeTableAggregate.getSourceTableName());

					TableFactory.clearCache("default", treeTableAggregate.getTargetTableName());
					TableFactory.clearCache(database.getName(), treeTableAggregate.getTargetTableName());

					String targetTableName = treeTableAggregate.getTargetTableName();

					/**
					 * 判断是否需要按日期进行切割数据，如果需要切割，先进行列转换，根据转换后的年和年月分别对数据进行处理。
					 */
					if (splitData && StringUtils.isNotEmpty(treeTableAggregate.getSourceTableDateSplitColumn())) {

						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						int now_year = cal.get(Calendar.YEAR);

						if (treeTableAggregate.getStartYear() == 0) {// 如果没有填写开始年份，默认为当年
							treeTableAggregate.setStartYear(now_year);
						}

						if (treeTableAggregate.getStopYear() == 0) {// 如果没有填写结束年份，默认为当年
							treeTableAggregate.setStopYear(now_year);
						}

						// 切割数据
						this.splitData(database, treeTableAggregate.getSourceTableName(),
								treeTableAggregate.getSourceTableDateSplitColumn(), idColumn);

						// 判断年度条件，循环年度条件，抽取数据到年度表
						List<Integer> years = new ArrayList<Integer>();
						if (treeTableAggregate.getStartYear() > 0 && treeTableAggregate.getStopYear() > 0) {
							for (int year = treeTableAggregate.getStartYear(); year <= treeTableAggregate
									.getStopYear(); year++) {
								years.add(year);
							}
						}
						if (years != null && !years.isEmpty()) {
							for (Integer year : years) {
								try {
									tableDefinition.setTableName(targetTableName + "_" + year);
									if (DBUtils.tableExists(database.getName(), tableDefinition.getTableName())) {
										DBUtils.alterTable(database.getName(), tableDefinition);
									} else {
										DBUtils.createTable(database.getName(), tableDefinition);
									}

									if (DBUtils.tableExists(tableDefinition.getTableName())) {
										DBUtils.alterTable(tableDefinition);
									} else {
										DBUtils.createTable(tableDefinition);
									}
									this.splitYearData(database, treeTableAggregate.getSourceTableName(), year);
									treeTableAggregate.setTargetTableName(targetTableName + "_" + year);

									this.execute(database, treeTableAggregate, "ex_year_", year, parameter);

									Thread.sleep(200);
								} catch (Exception ex) {
								}
							}
						}
						// 判断年月条件，循环月份条件，抽取数据到月份表
						List<Integer> yearMonths = new ArrayList<Integer>();
						if (treeTableAggregate.getStartYear() > 0 && treeTableAggregate.getStopYear() > 0) {
							for (int year = treeTableAggregate.getStartYear(); year <= treeTableAggregate
									.getStopYear(); year++) {
								for (int month = 1; month <= 9; month++) {
									yearMonths.add(Integer.parseInt(year + "0" + month));
								}
								yearMonths.add(Integer.parseInt(year + "10"));
								yearMonths.add(Integer.parseInt(year + "11"));
								yearMonths.add(Integer.parseInt(year + "12"));
							}
						}
						if (yearMonths != null && !yearMonths.isEmpty()) {
							for (Integer yearMonth : yearMonths) {
								try {
									tableDefinition.setTableName(targetTableName + "_" + yearMonth);
									if (DBUtils.tableExists(database.getName(), tableDefinition.getTableName())) {
										DBUtils.alterTable(database.getName(), tableDefinition);
									} else {
										DBUtils.createTable(database.getName(), tableDefinition);
									}

									if (DBUtils.tableExists(tableDefinition.getTableName())) {
										DBUtils.alterTable(tableDefinition);
									} else {
										DBUtils.createTable(tableDefinition);
									}
									this.splitYearMonthData(database, treeTableAggregate.getSourceTableName(),
											yearMonth);
									treeTableAggregate.setTargetTableName(targetTableName + "_" + yearMonth);
									this.execute(database, treeTableAggregate, "ex_yearmonth_", yearMonth, parameter);

									Thread.sleep(200);
								} catch (Exception ex) {
								}
							}
						}
					}

					try {
						tableDefinition.setTableName(targetTableName);
						if (DBUtils.tableExists(tableDefinition.getTableName())) {
							DBUtils.alterTable(tableDefinition);
						} else {
							DBUtils.createTable(tableDefinition);
						}
					} catch (Exception ex) {
					}

					treeTableAggregate.setTargetTableName(targetTableName);
					boolean ret = this.execute(database, treeTableAggregate, null, -1, parameter);
					if (ret) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(new Date());
						int month = calendar.get(Calendar.MONTH) + 1;
						int day = calendar.get(Calendar.DAY_OF_MONTH);
						boolean isLastDayOfMonth = false;
						switch (month) {
						case 2:
							if (day == 28 || day == 29) {
								isLastDayOfMonth = true;
							}
							break;
						case 4:
						case 6:
						case 9:
						case 11:
							if (day == 30) {
								isLastDayOfMonth = true;
							}
							break;
						default:
							if (day == 31) {
								isLastDayOfMonth = true;
							}
							break;
						}

						String monthTargetTable = treeTableAggregate.getTargetTableName() + DateUtils.getNowYearMonth();

						if (StringUtils.equalsIgnoreCase(treeTableAggregate.getGenByMonth(), "D") || (isLastDayOfMonth
								&& StringUtils.equalsIgnoreCase(treeTableAggregate.getGenByMonth(), "L"))) {
							tableDefinition.setTableName(monthTargetTable);
							if (DBUtils.tableExists(database.getName(), monthTargetTable)) {
								DBUtils.alterTable(database.getName(), tableDefinition);
							} else {
								DBUtils.createTable(database.getName(), tableDefinition);
							}

							TableFactory.clearCache("default", monthTargetTable);
							TableFactory.clearCache(database.getName(), monthTargetTable);

						}
					}
					return ret;
				}
			}

			return false;
		} catch (Exception ex) {
			logger.error("execute error", ex);
			throw new RuntimeException(ex);
		}
	}

	public boolean getResult() {
		return result;
	}

	protected List<Integer> getSplitCount(Database database, String tableName, String columnName) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = " select distinct " + columnName + " from " + tableName;
		Connection sourceConn = null;
		PreparedStatement sourcePsmt = null;
		ResultSet sourceRS = null;
		try {
			sourceConn = DBConnectionFactory.getConnection(database.getName());
			sourcePsmt = sourceConn.prepareStatement(sql);
			sourceRS = sourcePsmt.executeQuery();
			while (sourceRS.next()) {
				int value = sourceRS.getInt(1);
				if (value > 0) {
					list.add(value);
				}
			}
		} catch (Exception ex) {
			logger.error(sql + " execute error ", ex);
		} finally {
			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(sourceConn);
		}
		return list;
	}

	public void run() {
		result = this.execute(databaseId, treeTableAggregateId, splitData, parameter);
	}

	protected void splitData(Database database, String tableName, String columnName, ColumnDefinition idColumn) {
		String sql = " select " + idColumn.getColumnName() + ", " + columnName + " from " + tableName;
		String updateSql = " update " + tableName + " set EX_YEAR_ = ?, EX_YEARMONTH_ = ? where "
				+ idColumn.getColumnName() + " = ? ";

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy", Locale.getDefault());
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMM", Locale.getDefault());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap = null;
		Connection sourceConn = null;
		PreparedStatement sourcePsmt = null;
		PreparedStatement psmt = null;
		ResultSet sourceRS = null;
		long start = System.currentTimeMillis();
		int index = 0;
		Object id = null;

		try {
			List<ColumnDefinition> columns = DBUtils.getColumnDefinitions(database.getName(), tableName);

			ColumnDefinition yearCol = new ColumnDefinition();
			yearCol.setColumnName("EX_YEAR_");
			yearCol.setJavaType("Integer");
			columns.add(yearCol);

			ColumnDefinition yearMonthCol = new ColumnDefinition();
			yearMonthCol.setColumnName("EX_YEARMONTH_");
			yearMonthCol.setJavaType("Integer");
			columns.add(yearMonthCol);

			TableDefinition tableDefinition = new TableDefinition();
			tableDefinition.setTableName(tableName);
			tableDefinition.setColumns(columns);
			DBUtils.alterTable(database.getName(), tableDefinition);

			sourceConn = DBConnectionFactory.getConnection(database.getName());

			sourcePsmt = sourceConn.prepareStatement(sql);
			sourceRS = sourcePsmt.executeQuery();

			while (sourceRS.next()) {
				id = sourceRS.getObject(1);
				java.sql.Timestamp date = sourceRS.getTimestamp(2);
				if (date != null) {
					dataMap = new HashMap<String, Object>();
					dataMap.put("year", Integer.parseInt(formatter1.format(date)));
					dataMap.put("yearMonth", Integer.parseInt(formatter2.format(date)));
					dataMap.put("id", id);
					resultList.add(dataMap);
				}
			}

			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);

			sourceConn.setAutoCommit(false);
			psmt = sourceConn.prepareStatement(updateSql);
			for (Map<String, Object> rowMap : resultList) {
				psmt.setInt(1, (Integer) rowMap.get("year"));
				psmt.setInt(2, (Integer) rowMap.get("yearMonth"));
				psmt.setObject(3, rowMap.get("id"));
				psmt.addBatch();
				index++;
				if (index % 500 == 0) {
					psmt.executeBatch();
					// psmt.clearBatch();
					logger.debug("process " + index);
				}
			}
			psmt.executeBatch();
			psmt.clearBatch();
			sourceConn.commit();
		} catch (Exception ex) {
			logger.error("split data error", ex);
		} finally {
			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(psmt);
			JdbcUtils.close(sourceConn);
			long ts = System.currentTimeMillis() - start;
			logger.debug("切分全部数据用时（毫秒）:" + ts);
		}
	}

	protected void splitYearData(Database database, String tableName, int year) {
		String sql = " select TREEID_ from " + tableName + " where EX_YEAR_ = " + year;

		long start = System.currentTimeMillis();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap = null;
		StringBuilder buffer = new StringBuilder();

		Connection sourceConn = null;
		PreparedStatement sourcePsmt = null;
		PreparedStatement psmt = null;
		ResultSet sourceRS = null;

		int index = 0;
		String treeId = null;
		String tmpIndexId = null;
		StringTokenizer token = null;
		try {
			sourceConn = DBConnectionFactory.getConnection(database.getName());
			logger.debug("execute query:" + sql);
			sourcePsmt = sourceConn.prepareStatement(sql);
			sourceRS = sourcePsmt.executeQuery();

			while (sourceRS.next()) {
				treeId = sourceRS.getString(1);
				if (StringUtils.isNotEmpty(treeId)) {
					token = new StringTokenizer(treeId, "|");
					while (token.hasMoreTokens()) {
						tmpIndexId = token.nextToken();
						if (StringUtils.isNotEmpty(tmpIndexId) && StringUtils.isNumeric(tmpIndexId)) {
							dataMap = new HashMap<String, Object>();
							dataMap.put("id", Integer.parseInt(tmpIndexId));
							resultList.add(dataMap);
						}
					}
				}
			}

			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);

			sourceConn.setAutoCommit(false);

			buffer.append(" update ").append(tableName);
			buffer.append("  set EX_YEAR_ = ");

			buffer.append(year).append(" where INDEXID_  in ( ");
			int length = buffer.length();
			for (Map<String, Object> rowMap : resultList) {
				buffer.append(rowMap.get("id")).append(", ");
				index++;
				if (index % 500 == 0) {
					buffer.delete(buffer.length() - 2, buffer.length());
					buffer.append(")");
					psmt = sourceConn.prepareStatement(buffer.toString());
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
					buffer.delete(0, buffer.length());
					buffer.append(" update ").append(tableName);
					buffer.append("  set EX_YEAR_ = ");
					buffer.append(year).append(" where INDEXID_  in ( ");
					logger.debug("process " + index);
				}
			}

			if (index > 0 && buffer.length() > length) {
				if (buffer.toString().endsWith(", ")) {
					buffer.delete(buffer.length() - 2, buffer.length());
				}
				buffer.append(")");
				psmt = sourceConn.prepareStatement(buffer.toString());
				psmt.executeUpdate();
				JdbcUtils.close(psmt);
				logger.debug("process " + index);
			}

			sourceConn.commit();
		} catch (Exception ex) {
			logger.error("split year data error", ex);
		} finally {
			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(psmt);
			JdbcUtils.close(sourceConn);
			long ts = System.currentTimeMillis() - start;
			logger.debug("切分年份" + year + "数据用时（毫秒）:" + ts);
		}
	}

	protected void splitYearMonthData(Database database, String tableName, int yearMonth) {
		String sql = " select TREEID_ from " + tableName + " where EX_YEARMONTH_ = " + yearMonth;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap = null;

		StringBuilder buffer = new StringBuilder();

		long start = System.currentTimeMillis();
		Connection sourceConn = null;
		PreparedStatement sourcePsmt = null;
		PreparedStatement psmt = null;
		ResultSet sourceRS = null;

		int index = 0;
		String treeId = null;
		String tmpIndexId = null;
		StringTokenizer token = null;
		try {
			sourceConn = DBConnectionFactory.getConnection(database.getName());
			logger.debug("execute query:" + sql);
			sourcePsmt = sourceConn.prepareStatement(sql);
			sourceRS = sourcePsmt.executeQuery();

			while (sourceRS.next()) {
				treeId = sourceRS.getString(1);
				if (StringUtils.isNotEmpty(treeId)) {
					token = new StringTokenizer(treeId, "|");
					while (token.hasMoreTokens()) {
						tmpIndexId = token.nextToken();
						if (StringUtils.isNotEmpty(tmpIndexId) && StringUtils.isNumeric(tmpIndexId)) {
							dataMap = new HashMap<String, Object>();
							dataMap.put("id", Integer.parseInt(tmpIndexId));
							resultList.add(dataMap);
						}
					}
				}
			}

			sourceConn.setAutoCommit(false);

			buffer.append(" update ").append(tableName);
			buffer.append("  set EX_YEARMONTH_ = ");
			buffer.append(yearMonth).append(" where INDEXID_  in ( ");

			int length = buffer.length();
			for (Map<String, Object> rowMap : resultList) {
				buffer.append(rowMap.get("id")).append(", ");
				index++;
				if (index % 500 == 0) {
					buffer.delete(buffer.length() - 2, buffer.length());
					buffer.append(")");
					psmt = sourceConn.prepareStatement(buffer.toString());
					psmt.executeUpdate();
					JdbcUtils.close(psmt);
					buffer.delete(0, buffer.length());
					buffer.append(" update ").append(tableName);
					buffer.append("  set EX_YEARMONTH_ = ");
					buffer.append(yearMonth).append(" where INDEXID_  in ( ");
					logger.debug("process " + index);
				}
			}

			if (index > 0 && buffer.length() > length) {
				if (buffer.toString().endsWith(", ")) {
					buffer.delete(buffer.length() - 2, buffer.length());
				}
				buffer.append(")");
				psmt = sourceConn.prepareStatement(buffer.toString());
				psmt.executeUpdate();
				JdbcUtils.close(psmt);
				logger.debug("process " + index);
			}

			sourceConn.commit();
		} catch (Exception ex) {
			logger.error("split year month data error", ex);
		} finally {
			JdbcUtils.close(sourceRS);
			JdbcUtils.close(sourcePsmt);
			JdbcUtils.close(psmt);
			JdbcUtils.close(sourceConn);
			long ts = System.currentTimeMillis() - start;
			logger.debug("切分月份" + yearMonth + "数据用时（毫秒）:" + ts);
		}
	}

	@SuppressWarnings("unchecked")
	protected void sum(Database database, TreeTableAggregate treeTableAggregate, TreeTableAggregateRule rule,
			Map<String, Object> params) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select ");
		if (StringUtils.isNotEmpty(treeTableAggregate.getSourceIdColumn())) {
			buffer.append(treeTableAggregate.getSourceIdColumn()).append(", ");
		}
		buffer.append(treeTableAggregate.getSourceIndexIdColumn()).append(", ")
				.append(treeTableAggregate.getSourceParentIdColumn()).append(", ").append(rule.getSourceColumnName())
				.append(" from ").append(treeTableAggregate.getTargetTableName()).append(" where 1=1 ");
		if (StringUtils.isNotEmpty(treeTableAggregate.getSqlCriteria())) {
			buffer.append(" and ").append(treeTableAggregate.getSqlCriteria());
		}
		String sql = buffer.toString();
		if (params != null && !params.isEmpty()) {
			sql = QueryUtils.replaceBlankParas(sql, params);
		}

		SqlExecutor sqlExecutor = sqlHelper.buildSql("treetable_aggregate", String.valueOf(treeTableAggregate.getId()),
				params);

		logger.debug(sql);
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		MyBatisHelper helper = new MyBatisHelper();
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnectionFactory.getConnection(database.getName());
			psmt = conn.prepareStatement(sql + sqlExecutor.getSql());
			if (sqlExecutor.getParameter() != null) {
				List<Object> values = (List<Object>) sqlExecutor.getParameter();
				JdbcUtils.fillStatement(psmt, values);
			}
			rs = psmt.executeQuery();
			List<Map<String, Object>> list = helper.getResults(rs);
			for (Map<String, Object> dataMap : list) {
				TreeNode treeNode = new TreeNode();
				treeNode.setPkValue(
						ParamUtils.getObject(dataMap, treeTableAggregate.getSourceIdColumn().toLowerCase()));
				treeNode.setId(ParamUtils.getLong(dataMap, treeTableAggregate.getSourceIndexIdColumn().toLowerCase()));
				treeNode.setParentId(
						ParamUtils.getLong(dataMap, treeTableAggregate.getSourceParentIdColumn().toLowerCase()));
				treeNode.setValue(ParamUtils.getDouble(dataMap, rule.getSourceColumnName().toLowerCase()));
				treeNodes.add(treeNode);
				// logger.debug("id=["+treeNode.getId()+"]
				// value=["+treeNode.getValue()+"]");
			}

			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);

			logger.debug("准备计算......");

			TreeNodeSumBuilder builder = new TreeNodeSumBuilder();
			builder.sum(treeNodes);

			logger.debug("计算完成。");

			int index = 0;
			String tmp = null;

			logger.debug("更新节点数量:" + treeNodes.size());
			if (StringUtils.isNotEmpty(treeTableAggregate.getSourceIdColumn())) {
				sql = " update " + treeTableAggregate.getTargetTableName() + " set " + rule.getTargetColumnName()
						+ " = ? where " + treeTableAggregate.getSourceIdColumn() + " = ? ";
			} else {
				sql = " update " + treeTableAggregate.getTargetTableName() + " set " + rule.getTargetColumnName()
						+ " = ? where " + treeTableAggregate.getSourceIndexIdColumn() + " = ? ";
			}
			logger.debug("update sql: " + sql);
			conn = DBConnectionFactory.getConnection(database.getName());
			conn.setAutoCommit(false);
			psmt = conn.prepareStatement(sql);
			for (int i = 0, len = treeNodes.size(); i < len; i++) {
				index++;
				TreeNode treeNode = treeNodes.get(i);
				switch (rule.getTargetColumnType()) {
				case "Integer":
					tmp = String.valueOf(treeNode.getSumValue());
					if (tmp.indexOf(".") != -1) {
						tmp = tmp.substring(0, tmp.indexOf("."));
					}
					psmt.setInt(1, Integer.parseInt(tmp));
					break;
				case "Long":
					tmp = String.valueOf(treeNode.getSumValue());
					if (tmp.indexOf(".") != -1) {
						tmp = tmp.substring(0, tmp.indexOf("."));
					}
					psmt.setLong(1, Long.parseLong(tmp));
					break;
				case "Double":
					psmt.setDouble(1, treeNode.getSumValue());
					break;
				default:
					psmt.setString(1, String.valueOf(treeNode.getSumValue()));
					break;
				}
				if (treeNode.getPkValue() != null && StringUtils.isNotEmpty(treeTableAggregate.getSourceIdColumn())) {
					psmt.setObject(2, treeNode.getPkValue());
				} else {
					psmt.setLong(2, treeNode.getId());
				}
				psmt.addBatch();
				if (index % 500 == 0) {
					psmt.executeBatch();
					// psmt.clearBatch();
					logger.debug("update rows: " + index);
				}
			}
			logger.debug("executeBatch...");
			psmt.executeBatch();
			psmt.close();
			conn.commit();
			logger.debug("execute update ok.");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("execute update error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(rs);
			JdbcUtils.close(psmt);
			JdbcUtils.close(conn);
		}
	}

}
