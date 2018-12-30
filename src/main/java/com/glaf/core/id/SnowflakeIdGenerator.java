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

package com.glaf.core.id;

import org.apache.commons.lang3.StringUtils;

public class SnowflakeIdGenerator implements IdGenerator {

	private static SnowflakeIdWorker snowflakeIdWorker = null;

	SnowflakeIdGenerator() {
		String workerId = System.getProperty("workerId");
		String datacenterId = System.getProperty("datacenterId");
		if (StringUtils.isNotEmpty(workerId) && StringUtils.isNotEmpty(datacenterId)) {
			snowflakeIdWorker = new SnowflakeIdWorker(Long.parseLong(workerId), Long.parseLong(datacenterId));
		} else {
			snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
		}
	}

	SnowflakeIdGenerator(long workerId, long datacenterId) {
		snowflakeIdWorker = new SnowflakeIdWorker(workerId, datacenterId);
	}

	@Override
	public synchronized String getNextId() {
		return Long.toString(snowflakeIdWorker.nextId());
	}

	@Override
	public synchronized String getNextId(String name) {
		return Long.toString(snowflakeIdWorker.nextId());
	}

	@Override
	public synchronized String getNextId(String tablename, String idColumn, String createBy) {
		return getNextId();
	}

	@Override
	public synchronized long nextId() {
		return snowflakeIdWorker.nextId();
	}

	@Override
	public synchronized long nextId(String name) {
		return nextId();
	}

	@Override
	public synchronized long nextId(String tablename, String idColumn) {
		return nextId();
	}

}
