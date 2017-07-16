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
package com.glaf.core.server;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.domain.ServerEntity;

public class ZooKeeperServerValidator implements IServerValidator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean verify(ServerEntity serverEntity) {
		// logger.debug(serverEntity.toJsonObject().toJSONString());
		CuratorFramework zkClient = null;
		try {
			String servers = serverEntity.getHost() + ":" + serverEntity.getPort();
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000, 10);
			zkClient = CuratorFrameworkFactory.newClient(servers, retryPolicy);
			zkClient.start();
			String path = "/tmp";
			Stat stat = zkClient.checkExists().forPath(path);
			if (stat == null) {
				zkClient.create().forPath(path);
				logger.debug("zk create root path:" + path);
			}
			byte[] data = "TEST".getBytes();
			path = path + "/data";
			zkClient.create().withMode(CreateMode.PERSISTENT).inBackground().forPath(path, data);
			byte[] bytes = zkClient.getData().forPath(path);
			zkClient.delete().forPath(path);
			logger.debug("data:" + new String(bytes));
			if (StringUtils.equals(new String(bytes), "TEST")) {
				return true;
			}
		} catch (Exception ex) {
			
		} finally {
			if (zkClient != null) {
				zkClient.close();
				zkClient = null;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ServerEntity server = new ServerEntity();
		server.setHost("192.168.10.121");
		server.setPort(2181);
		server.setPath("/");
		//System.out.println(server.toString());
		IServerValidator serverValidator = new ZooKeeperServerValidator();
		System.out.println(serverValidator.verify(server));
	}

}
