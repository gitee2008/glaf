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

package com.glaf.matrix.data.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class ByteArrayDataCache {

	public class ClearTask implements Runnable {

		public void run() {
			try {
				clear();
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

	}

	protected final static Log logger = LogFactory.getLog(ByteArrayDataCache.class);

	protected static Cache<String, byte[]> concurrentMap = CacheBuilder.newBuilder().concurrencyLevel(8)
			.initialCapacity(10).maximumSize(5000).expireAfterWrite(300, TimeUnit.SECONDS)
			.expireAfterAccess(300, TimeUnit.SECONDS).removalListener(new RemovalListener<Object, Object>() {
				@Override
				public void onRemoval(RemovalNotification<Object, Object> notification) {
					logger.debug(notification.getKey() + " was removed, cause is " + notification.getCause());
				}
			}).build();

	public static void clear() {
		concurrentMap.invalidateAll();
		concurrentMap.cleanUp();
	}

	public static byte[] get(String key) {
		return concurrentMap.getIfPresent(key);
	}

	public static void put(String key, byte[] data) {
		concurrentMap.put(key, data);
	}

	protected ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

	private ByteArrayDataCache() {

	}

	public void startScheduler() {
		ClearTask command = new ClearTask();
		scheduledThreadPool.scheduleAtFixedRate(command, 1, 15, TimeUnit.MINUTES);
	}

}
