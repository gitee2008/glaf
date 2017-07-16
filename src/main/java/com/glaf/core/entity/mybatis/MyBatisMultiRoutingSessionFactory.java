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

package com.glaf.core.entity.mybatis;

import java.io.Reader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;

public class MyBatisMultiRoutingSessionFactory {

	protected static volatile ConcurrentMap<String, SqlSessionFactory> factoryMap = new ConcurrentHashMap<String, SqlSessionFactory>();

	protected static volatile Configuration conf = BaseConfiguration.create();

	protected static volatile SqlSessionFactory sqlSessionFactory;

	protected static Reader reader;

	static {
		try {
			reader = Resources.getResourceAsReader("configuration.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception ex) {
			
		}
	}

	public static SqlSessionFactory getSessionFactory() {
		return sqlSessionFactory;
	}

	public static SqlSessionFactory getSessionFactory(String name) {
		if (factoryMap.get(name) == null) {
			String location = conf.get(name);
			if (StringUtils.isNotEmpty(location)) {
				try {
					Reader readerx = Resources.getResourceAsReader(location);
					SqlSessionFactory sqlSessionFactoryx = new SqlSessionFactoryBuilder().build(readerx);
					factoryMap.put(name, sqlSessionFactoryx);
				} catch (Exception ex) {
					
					throw new RuntimeException(ex);
				}
			}
		}
		return factoryMap.get(name);
	}

	private MyBatisMultiRoutingSessionFactory() {

	}

}
