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

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;

public class MongoDBConfig {

	private String addresses;

	private MongoDBCredential credential;

	private List<MongoDBCredential> credentials;

	public MongoDBConfig() {

	}

	public List<ServerAddress> buildAddresses() throws Exception {
		List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
		String[] sources = addresses.split(";");
		for (String item : sources) {
			String[] hp = item.split(":");
			serverAddresses.add(new ServerAddress(hp[0], Integer.valueOf(hp[1])));
		}
		return serverAddresses;
	}

	public MongoCredential buildCredential() {
		if (credential.getPassword() != null) {
			MongoCredential _credential = MongoCredential.createCredential(credential.getUsername(),
					credential.getDatabaseName(), credential.getPassword().toCharArray());
			return _credential;
		} else {
			MongoCredential _credential = MongoCredential.createCredential(credential.getUsername(),
					credential.getDatabaseName(), null);
			return _credential;
		}
	}

	public List<MongoCredential> buildCredentials() {
		List<MongoCredential> mongoCredentials = new ArrayList<MongoCredential>();
		for (MongoDBCredential item : this.credentials) {
			if (item != null && item.getUsername() != null && item.getDatabaseName() != null) {
				if (item.getPassword() != null) {
					MongoCredential credential = MongoCredential.createCredential(item.getUsername(),
							item.getDatabaseName(), item.getPassword().toCharArray());
					mongoCredentials.add(credential);
				} else {
					MongoCredential credential = MongoCredential.createCredential(item.getUsername(),
							item.getDatabaseName(), null);
					mongoCredentials.add(credential);
				}
			}
		}
		return mongoCredentials;
	}

	public String getAddresses() {
		return addresses;
	}

	public MongoDBCredential getCredential() {
		return credential;
	}

	public List<MongoDBCredential> getCredentials() {
		return credentials;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public void setCredential(MongoDBCredential credential) {
		this.credential = credential;
	}

	public void setCredentials(List<MongoDBCredential> credentials) {
		this.credentials = credentials;
	}

}
