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

package com.glaf.matrix.data.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.domain.ServerEntity;
import com.glaf.core.server.IServerValidator;
import com.glaf.matrix.data.mongo.object.MongoDBConfig;
import com.glaf.matrix.data.mongo.object.MongoDBCredential;
import com.glaf.matrix.data.mongo.object.MongoDBDriver;

public class MongoDBServerValidator implements IServerValidator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean verify(ServerEntity serverEntity) {
		String host = serverEntity.getHost();
		int port = serverEntity.getPort();
		String password = serverEntity.getPassword();
		// String key = serverEntity.getKey();
		logger.debug("mongodb database:" + serverEntity.getDbname());
		MongoDBDriver mongoDBDriver = new MongoDBDriver();
		MongoDBConfig mongoDBConfig = new MongoDBConfig();
		mongoDBConfig.setAddresses(host + ":" + port);
		MongoDBCredential credential = new MongoDBCredential();
		credential.setDatabaseName(serverEntity.getDbname());
		credential.setUsername(serverEntity.getUser());
		try {
			// password = SecurityUtils.decode(key, password);
			credential.setPassword(password);
			// logger.debug("password:" + password);
			mongoDBConfig.setCredential(credential);
			mongoDBDriver.setConfiguration(mongoDBConfig);
			mongoDBDriver.init();
			MongoDBClient client = new MongoDBClient();
			client.setMongoDBDriver(mongoDBDriver);
			client.setDatabaseName(serverEntity.getDbname());
			client.getDatabase().getCollection("test").drop();
			logger.debug(client.getDatabase() + " success.");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("mongodb verify error", ex);
		}
		return false;
	}

}
