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

import org.bson.Document;

import com.glaf.matrix.data.mongo.object.MongoDBDriver;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBClient {

	protected MongoDBDriver mongoDBDriver;

	protected String databaseName;

	public void setMongoDBDriver(MongoDBDriver mongoDBDriver) {
		this.mongoDBDriver = mongoDBDriver;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public MongoCollection<Document> getCollection(String collectionName) {
		MongoDatabase db = mongoDBDriver.getDatabase(this.databaseName);
		return db.getCollection(collectionName);
	}

	public MongoDatabase getDatabase() {
		return mongoDBDriver.getDatabase(this.databaseName);
	}

}
