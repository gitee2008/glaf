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

import java.util.Map;

import com.glaf.core.domain.Database;
import com.glaf.matrix.combination.domain.TreeTableAggregate;

public class TreeTableAggregateThread implements java.lang.Runnable {

	protected Database database;

	protected TreeTableAggregate treeTableAggregate;

	protected String splitColumn;

	protected int splitValue;

	protected Map<String, Object> parameter;

	public TreeTableAggregateThread(Database database, TreeTableAggregate treeTableAggregate, String splitColumn,
			int splitValue, Map<String, Object> parameter) {
		this.database = database;
		this.treeTableAggregate = treeTableAggregate;
		this.splitColumn = splitColumn;
		this.splitValue = splitValue;
		this.parameter = parameter;
	}

	@Override
	public void run() {
		TreeTableAggregateBean bean = new TreeTableAggregateBean();
		bean.execute(database, treeTableAggregate, splitColumn, splitValue, parameter);
	}

}
