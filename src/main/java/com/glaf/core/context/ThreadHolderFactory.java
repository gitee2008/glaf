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

package com.glaf.core.context;

import java.util.ArrayList;
import java.util.List;

public class ThreadHolderFactory {

	private static class ThreadHolderFactoryHolder {
		public static ThreadHolderFactory instance = new ThreadHolderFactory();
	}

	public static ThreadHolderFactory getInstance() {
		return ThreadHolderFactoryHolder.instance;
	}

	public List<ThreadHolder> holders = new ArrayList<ThreadHolder>();

	private ThreadHolderFactory() {

	}

	public void addHolder(ThreadHolder holder) {
		if (holders == null) {
			holders = new ArrayList<ThreadHolder>();
		}
		holders.add(holder);
	}

	/**
	 * 关闭并清理
	 */
	public void closeAndClear() {
		if (holders != null && !holders.isEmpty()) {
			for (ThreadHolder holder : holders) {
				try {
					holder.closeAndClear();
				} catch (Exception ex) {
				}
			}
		}
	}

}
