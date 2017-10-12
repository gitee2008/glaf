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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.domain.ServerEntity;
import com.glaf.core.util.UUID32;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisServerValidator implements IServerValidator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean verify(ServerEntity serverEntity) {
		logger.debug("----------------------------redis verify-------------------------------");
		int timeout = Protocol.DEFAULT_TIMEOUT;
		String host = serverEntity.getHost();
		int port = serverEntity.getPort();
		String password = serverEntity.getPassword();
		Jedis jedis = null;
		JedisPool pool = null;
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
			config.setMaxTotal(2);
			config.setMaxIdle(5);
			config.setMaxWaitMillis(5000L);
			config.setTestOnBorrow(false);
			//logger.debug("p:" + password);
			if (StringUtils.isNotEmpty(password) && !StringUtils.equals(password, "88888888")) {
				pool = new JedisPool(config, host, port, timeout, password);
			} else {
				pool = new JedisPool(config, host, port, timeout);
			}
			jedis = pool.getResource();
			if (StringUtils.isNotEmpty(serverEntity.getDbname())) {
				jedis.select(Integer.parseInt(serverEntity.getDbname()));
			}
			String value = UUID32.getUUID();
			logger.debug("value:" + value);
			jedis.set("test".getBytes(), value.getBytes());
			if (StringUtils.equals(jedis.get("test"), value)) {
				// jedis.del("test");
				return true;
			}
		} catch (Exception ex) {
			logger.error("redis connection error", ex);
		} finally {
			if (pool != null) {
				pool.close();
			}
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

}
