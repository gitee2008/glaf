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
package com.glaf.shiro.redis;

import java.util.Set;

import com.glaf.core.factory.RedisFactory;

public class RedisManager {

	// 0 - never expire
	private int expire = 0;

	public RedisManager() {

	}

	/**
	 * size
	 */
	public Long dbSize() {
		Long dbSize = 0L;
		return dbSize;
	}

	/**
	 * del
	 * 
	 * @param key
	 */
	public void del(byte[] key) {
		RedisFactory.getInstance().del(key);
	}

	/**
	 * get value from redis
	 * 
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key) {
		byte[] value = RedisFactory.getInstance().getBytes(key);
		return value;
	}

	public int getExpire() {
		return expire;
	}

	/**
	 * 初始化方法
	 */
	public void init() {

	}

	/**
	 * keys
	 * 
	 * @param regex
	 * @return
	 */
	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = RedisFactory.getInstance().byteHKeys(pattern);
		return keys;
	}

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public byte[] set(byte[] key, byte[] value) {
		RedisFactory.getInstance().set(key, value);
		if (this.expire != 0) {
			RedisFactory.getInstance().expire(key, this.expire);
		}
		return value;
	}

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public byte[] set(byte[] key, byte[] value, int expire) {
		RedisFactory.getInstance().set(key, value);
		if (this.expire != 0) {
			RedisFactory.getInstance().expire(key, this.expire);
		}
		return value;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

}
