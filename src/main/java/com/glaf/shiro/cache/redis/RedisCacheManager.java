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
package com.glaf.shiro.cache.redis;

import org.apache.shiro.ShiroException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.SystemConfig;

/**
 * Redis 分布式缓存实现
 */
public class RedisCacheManager implements CacheManager, Initializable, Destroyable {
	private final static Logger log = LoggerFactory.getLogger(RedisCacheManager.class);

	private String sessionPrefix;

	public void destroy() throws Exception {

	}

	public String getSessionPrefix() {
		if (sessionPrefix == null) {
			sessionPrefix = "shiro_session";
		}
		sessionPrefix = sessionPrefix + "_" + SystemConfig.getRegionName("shiro");
		return sessionPrefix;
	}

	public void setSessionPrefix(String sessionPrefix) {
		this.sessionPrefix = sessionPrefix;
	}

	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new RedisCache<K, V>(getSessionPrefix() + "_" + name);
	}

	public void init() throws ShiroException {
		log.info("-----------------------init redis cache manager------------");
	}

}
