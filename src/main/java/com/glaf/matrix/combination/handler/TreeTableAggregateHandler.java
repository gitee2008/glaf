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

package com.glaf.matrix.combination.handler;

import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.domain.Database;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.DateUtils;

import com.glaf.matrix.combination.domain.TreeTableAggregate;
import com.glaf.matrix.combination.service.TreeTableAggregateService;
import com.glaf.matrix.combination.task.TreeTableAggregateTask;
import com.glaf.matrix.data.util.ExecutionUtils;
import com.glaf.matrix.handler.DataExecutionHandler;

public class TreeTableAggregateHandler implements DataExecutionHandler {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void execute(Object id, Map<String, Object> context) {
		logger.debug("-----------------------TreeTableAggregateHandler--------------");
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		TreeTableAggregateService treeTableAggregateService = ContextFactory.getBean("treeTableAggregateService");
		long aggregateId = Long.parseLong(id.toString());
		TreeTableAggregate treeTableAggregate = treeTableAggregateService.getTreeTableAggregate(aggregateId);
		if (treeTableAggregate != null && StringUtils.isNotEmpty(treeTableAggregate.getDatabaseIds())) {
			if (StringUtils.isNotEmpty(treeTableAggregate.getDatabaseIds())) {
				int errorCount = 0;
				String jobNo = null;
				Database database = null;
				TreeTableAggregateTask task = null;
				treeTableAggregate.setSyncTime(new Date());
				int runDay = DateUtils.getNowYearMonthDay();
				StringTokenizer token = new StringTokenizer(treeTableAggregate.getDatabaseIds(), ",");
				while (token.hasMoreTokens()) {
					String x = token.nextToken();
					if (StringUtils.isNotEmpty(x) && StringUtils.isNumeric(x)) {
						jobNo = "treetable_aggregate_" + treeTableAggregate.getId() + "_" + x + "_" + runDay;
						try {
							database = databaseService.getDatabaseById(Long.parseLong(x));
							if (database != null) {
								ExecutionUtils.put(jobNo, new Date());
								task = new TreeTableAggregateTask(database.getId(), treeTableAggregate.getId(), false,
										jobNo, null);
								if (!task.execute()) {
									errorCount++;
								}
							}
						} catch (Exception ex) {
							errorCount++;
							logger.error("tree table aggregate error", ex);
						} finally {
							ExecutionUtils.remove(jobNo);
						}
					}
				}

				if (errorCount == 0) {
					treeTableAggregate.setSyncStatus(1);
				} else {
					treeTableAggregate.setSyncStatus(-1);
				}
				treeTableAggregateService.updateTreeTableAggregateStatus(treeTableAggregate);
			}
		}
	}

}
