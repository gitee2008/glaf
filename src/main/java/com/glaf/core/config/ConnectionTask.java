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

import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.domain.Database;

public class ConnectionTask extends RecursiveTask<Database> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = 1L;

	protected Database database;

	public ConnectionTask(Database database) {
		this.database = database;
	}

	protected Database compute() {
		DatabaseConnectionConfig config = new DatabaseConnectionConfig();
		if ("1".equals(database.getActive())) {
			if (config.checkConfig(database)) {
				logger.info(database.getName() + " check connection ok.");
				return database;
			}
		}
		return null;
	}

}
