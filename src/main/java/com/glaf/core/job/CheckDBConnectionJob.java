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
package com.glaf.core.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.glaf.core.base.ConnectionDefinition;
import com.glaf.core.config.DatabaseConnectionConfig;
import com.glaf.core.jdbc.CheckDBConnectionThread;

public class CheckDBConnectionJob extends BaseJob {

	public void runJob(JobExecutionContext context)
			throws JobExecutionException {
		DatabaseConnectionConfig config = new DatabaseConnectionConfig();
		List<ConnectionDefinition> list = config.getConnectionDefinitions();
		if (list != null && !list.isEmpty()) {
			for (ConnectionDefinition connectionDefinition : list) {
				CheckDBConnectionThread thread = new CheckDBConnectionThread(
						connectionDefinition);
				com.glaf.core.util.threads.ThreadFactory.execute(thread);
			}
		}
	}

}
