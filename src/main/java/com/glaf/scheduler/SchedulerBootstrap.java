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

package com.glaf.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.SystemProperties;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.IDatabaseService;
import com.glaf.core.util.QuartzUtils;

public class SchedulerBootstrap {
	private static final Log logger = LogFactory.getLog(SchedulerBootstrap.class);

	public static void main(String[] args) {
		System.setProperty("config.path", ".");
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("glaf.webapp.root", SystemProperties.getAppPath());
		System.out.println("app path:" + SystemProperties.getAppPath());
		if (DBConnectionFactory.checkConnection()) {
			System.out.println("start spring ......");
			IDatabaseService databaseService = ContextFactory.getBean("databaseService");
			if (databaseService != null) {
				try {
					QuartzUtils.startup();
					System.out.println("系统调度已经成功启动。");
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
	}

	public static void close(String[] args) {
		try {
			QuartzUtils.shutdown();
			System.out.println("系统调度已经成功停止。");
			System.exit(0);
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

}
