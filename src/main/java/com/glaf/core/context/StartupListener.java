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

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.glaf.core.access.factory.AccessLogFactory;
import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.entity.jpa.EntitySchemaUpdate;
import com.glaf.core.factory.DatabaseFactory;
import com.glaf.core.factory.SysLogFactory;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.startup.BootstrapManager;
import com.glaf.core.util.Constants;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.QuartzUtils;
import com.glaf.core.util.StringTools;

public class StartupListener extends ContextLoaderListener implements ServletContextListener {
	private static final Log logger = LogFactory.getLog(StartupListener.class);

	protected static Configuration conf = BaseConfiguration.create();

	private static class SchedulerRunner extends Thread {
		public void run() {
			try {
				Thread.sleep(20000);// 20秒后执行
			} catch (Exception ex) {
			}

			try {

				SysLogFactory.getInstance().checkAndCreateLogDB();

				SysLogFactory.getInstance().startScheduler();

				AccessLogFactory.getInstance().startScheduler();

			} catch (java.lang.Throwable ex) {
				logger.error(ex);
			}

			/**
			 * 在集群环境里可以设置-Dstart_scheduler=false不启动调度
			 */
			if (!"false".equals(System.getProperty("start_scheduler"))) {
				try {
					QuartzUtils.startup();
					logger.info("系统调度已经成功启动。");
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		}
	}

	public void beforeContextInitialized(ServletContext context) {
		try {
			boolean updateSchema = true;
			List<ColumnDefinition> columns = DBUtils.getColumnDefinitions("SYS_PROPERTY");
			if (columns != null && !columns.isEmpty()) {
				for (ColumnDefinition column : columns) {
					if (StringUtils.equalsIgnoreCase(column.getColumnName(), "LOCKED_")) {
						updateSchema = false;
						break;
					}
				}
			}
			if (updateSchema) {
				logger.debug("-----------------------SchemaUpdate-------------------");
				EntitySchemaUpdate bean = new EntitySchemaUpdate();
				bean.updateDDL();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			BootstrapManager.getInstance().startup(context);
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

	public void contextInitialized(ServletContextEvent event) {
		logger.info("initializing servlet context......");
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("glaf.webapp.root", SystemProperties.getAppPath());
		try {
			com.glaf.core.util.LogUtils.initPlatformLog4j();
		} catch (Exception ex) {
		}
		ServletContext context = event.getServletContext();
		String root = context.getRealPath("/");
		com.glaf.core.context.ApplicationContext.setAppPath(root);
		com.glaf.core.context.ApplicationContext.setContextPath(event.getServletContext().getContextPath());
		String webAppRootKey = event.getServletContext().getInitParameter("webAppRootKey");
		if (StringUtils.isNotEmpty(webAppRootKey)) {
			System.setProperty(webAppRootKey, SystemProperties.getAppPath());
		}
		System.setProperty(Constants.APP_NAME,
				StringTools.replace(event.getServletContext().getContextPath(), "/", ""));
		System.setProperty(Constants.APP_PATH, SystemProperties.getAppPath());
		System.setProperty(Constants.WEBAPP_PATH, SystemProperties.getAppPath());
		System.setProperty(Constants.CONFIG_PATH, SystemProperties.getAppPath() + "/WEB-INF");
		if (DBConnectionFactory.checkConnection()) {
			DatabaseFactory.getInstance().reload();
			this.beforeContextInitialized(context);
			super.contextInitialized(event);
			this.setupContext(context);
		} else {
			logger.error("数据库连接错误，请检查配置。");
		}
		if (SystemProperties.getDeploymentSystemName() != null) {
			String deploymentSystemName = SystemProperties.getDeploymentSystemName();
			String path = SystemProperties.getConfigRootPath() + Constants.DEPLOYMENT_JDBC_PATH + deploymentSystemName
					+ "/jdbc";
			try {
				FileUtils.mkdirs(path);
			} catch (IOException ex) {
			}
			String log_path = SystemProperties.getConfigRootPath() + "/logs/" + deploymentSystemName;
			try {
				FileUtils.mkdirs(log_path);
			} catch (IOException ex) {
			}
		}

		try {
			SystemConfig.reload();
		} catch (Exception ex) {
		}

	}

	public void setupContext(ServletContext context) {
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		if (ctx != null) {
			logger.info("设置应用环境上下文......");
			ContextFactory.setContext(ctx);
			if (conf.getBoolean("scheduler.enabled", true) && ContextFactory.hasBean("scheduler")) {
				SchedulerRunner runner = new SchedulerRunner();
				runner.start();
			}
		}
	}
}