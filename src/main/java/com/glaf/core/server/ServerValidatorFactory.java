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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.domain.ServerEntity;

public class ServerValidatorFactory {

	private static class ServerValidatorHolder {
		public static ServerValidatorFactory instance = new ServerValidatorFactory();
	}

	public static ServerValidatorFactory getInstance() {
		return ServerValidatorHolder.instance;
	}

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerValidatorFactory() {

	}

	/**
	 * 验证配置是否正确
	 * 
	 * @param serverEntity
	 * @return
	 */
	public boolean verify(ServerEntity serverEntity) {
		//ServerProperties.reload();
		String verifyClass = ServerProperties.getString(serverEntity.getType() + ".verifyClass");
		logger.debug("verifyClass:"+verifyClass);
		if (StringUtils.isNotEmpty(verifyClass)) {
			Object object = com.glaf.core.util.ReflectUtils.instantiate(verifyClass);
			if (object instanceof IServerValidator) {
				IServerValidator validator = (IServerValidator) object;
				return validator.verify(serverEntity);
			}
		}
		return false;
	}

}
