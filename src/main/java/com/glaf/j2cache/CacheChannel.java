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

package com.glaf.j2cache;

import java.util.List;

/**
 * Cache Channel
 * 
 * @author oschina.net
 */
public interface CacheChannel {

	public final static byte LEVEL_1 = 1;

	public final static byte LEVEL_2 = 2;

	/**
	 * 批量删除缓存
	 * 
	 * @param region:
	 *            Cache region name
	 * @param keys:
	 *            Cache key
	 */
	@SuppressWarnings({ "rawtypes" })
	void batchEvict(String region, List keys);

	/**
	 * Clear the cache
	 * 
	 * @param region:
	 *            Cache region name
	 */
	void clear(String region);

	/**
	 * 关闭到通道的连接
	 */
	void close();

	/**
	 * 删除缓存
	 * 
	 * @param region:
	 *            Cache Region name
	 * @param key:
	 *            Cache key
	 */
	void evict(String region, Object key);

	/**
	 * 获取缓存中的数据
	 * 
	 * @param region:
	 *            Cache Region name
	 * @param key:
	 *            Cache key
	 * @return cache object
	 */
	CacheObject get(String region, Object key);

	/**
	 * Get cache region keys
	 * 
	 * @param region:
	 *            Cache region name
	 * @return key list
	 */
	@SuppressWarnings("rawtypes")
	List keys(String region);

	/**
	 * 写入缓存
	 * 
	 * @param region:
	 *            Cache Region name
	 * @param key:
	 *            Cache key
	 * @param value:
	 *            Cache value
	 */
	void set(String region, Object key, Object value);
}
