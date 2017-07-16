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
package com.glaf.core.factory;

import java.util.ServiceLoader;

public class BeanFactory {

	private static class BeanFactoryHolder {
		private static final BeanFactory instance = new BeanFactory();
	}

	public static BeanFactory getInstance() {
		return BeanFactoryHolder.instance;
	}

	private BeanFactory() {

	}

	public <T> T getBeanInstance(Class<T> type) {
		ServiceLoader<T> serviceLoad = ServiceLoader.load(type, getInstance().getClass().getClassLoader());
		if (serviceLoad != null) {
			for (T svc : serviceLoad) {
				return svc;
			}
		}
		return null;
	}
}
