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

package com.glaf.matrix.data.mongo.object;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;

public class MongoDBDriver {
	private Properties properties;
	private MongoClient mongoClient = null;
	private Integer connectionsPerHost = 32;
	private Integer threadsAllowedToBlockForConnectionMultiplier = 5;
	private Integer maxWaitTime = 30000;
	private Integer connectTimeout = 30000;
	private Integer socketTimeout = 30000;
	private Integer maxConnectionIdle = 6000;
	private MongoDBConfig configuration;

	public MongoDBDriver() {

	}

	public void close() {
		mongoClient.close();
	}

	public MongoDatabase getDatabase(String dbName) {
		return mongoClient.getDatabase(dbName);
	}

	@SuppressWarnings("deprecation")
	public DB getDB(String dbName) {
		return mongoClient.getDB(dbName);
	}

	public MongoClient getMongo() {
		return mongoClient;
	}

	public void init() throws Exception {
		if (properties != null) {

			String perHost = properties.getProperty("mongodb.driver.connectionsPerHost");

			if (StringUtils.isNotEmpty(perHost)) {
				connectionsPerHost = Integer.valueOf(perHost);
			}

			String multiplier = properties.getProperty("mongodb.driver.threadsAllowedToBlockForConnectionMultiplier");
			if (StringUtils.isNotEmpty(multiplier)) {
				threadsAllowedToBlockForConnectionMultiplier = Integer.valueOf(multiplier);
			}

			String waitTime = properties.getProperty("mongodb.driver.maxWaitTime");
			if (StringUtils.isNotEmpty(waitTime)) {
				maxWaitTime = Integer.valueOf(waitTime);
			}

			String ctimeout = properties.getProperty("mongodb.driver.connectTimeout");
			if (StringUtils.isNotEmpty(ctimeout)) {
				connectTimeout = Integer.valueOf(ctimeout);
			}

			String stimeout = properties.getProperty("mongodb.driver.socketTimeout");
			if (StringUtils.isNotEmpty(stimeout)) {
				socketTimeout = Integer.valueOf(stimeout);
			}

			String mci = properties.getProperty("mongodb.driver.maxConnectionIdle");
			if (StringUtils.isNotEmpty(mci)) {
				maxConnectionIdle = Integer.valueOf(mci);
			}
		}

		MongoClientOptions.Builder builder = MongoClientOptions.builder();
		builder.connectionsPerHost(this.connectionsPerHost);
		builder.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
		builder.maxWaitTime(this.maxWaitTime);
		builder.connectTimeout(this.connectTimeout);
		builder.socketTimeout(this.socketTimeout);
		builder.maxConnectionIdleTime(maxConnectionIdle);

		MongoClientOptions options = builder.build();

		this.mongoClient = new MongoClient(configuration.buildAddresses(), configuration.buildCredential(), options);

	}

	public void setConfiguration(MongoDBConfig configuration) {
		this.configuration = configuration;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}