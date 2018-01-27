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

package com.glaf.matrix.transform.handler;

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
import com.glaf.matrix.data.util.ExecutionUtils;
import com.glaf.matrix.handler.DataExecutionHandler;
import com.glaf.matrix.transform.domain.TableTransform;
import com.glaf.matrix.transform.service.TableTransformService;
import com.glaf.matrix.transform.task.TableTransformTask;

public class TableTransformHandler implements DataExecutionHandler {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void execute(Object id, Map<String, Object> context) {
		IDatabaseService databaseService = ContextFactory.getBean("databaseService");
		TableTransformService tableTransformService = ContextFactory.getBean("tableTransformService");
		TableTransform tableTransform = tableTransformService.getTableTransform(id.toString());
		if (tableTransform != null && StringUtils.isNotEmpty(tableTransform.getDatabaseIds())) {
			int errorCount = 0;
			String jobNo = null;
			Database database = null;
			TableTransformTask task = null;
			tableTransform.setTransformTime(new Date());
			int runDay = DateUtils.getNowYearMonthDay();
			StringTokenizer token = new StringTokenizer(tableTransform.getDatabaseIds(), ",");
			while (token.hasMoreTokens()) {
				String x = token.nextToken();
				if (StringUtils.isNotEmpty(x) && StringUtils.isNumeric(x)) {
					jobNo = "table_transform_" + tableTransform.getTransformId() + "_" + x + "_" + runDay;
					try {
						database = databaseService.getDatabaseById(Long.parseLong(x));
						if (database != null) {
							ExecutionUtils.put(jobNo, new Date());
							task = new TableTransformTask(database.getId(), tableTransform.getTransformId(),
									jobNo, null);
							if (!task.execute()) {
								errorCount++;
							}
						}
					} catch (Exception ex) {
						errorCount++;
						logger.error("table transform error", ex);
					} finally {
						ExecutionUtils.remove(jobNo);
					}
				}
			}

			if (errorCount == 0) {
				tableTransform.setTransformStatus(1);
			} else {
				tableTransform.setTransformStatus(-1);
			}
			tableTransformService.updateTableTransformStatus(tableTransform);
		}
	}

}
