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

package com.glaf.setup.conf;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.*;
import java.security.PrivilegedAction;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;

public class DatabaseConfig {

	public final static String sp = System.getProperty("file.separator");

	public DatabaseConfig() {

	}

	public List<Database> getDatabases(String appPath) {
		List<Database> rows = new ArrayList<Database>();
		String configPath = appPath + sp + "WEB-INF" + sp + "conf" + sp + "templates" + sp + "jdbc";
		File file = new File(configPath);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			if (filelist != null) {
				for (int i = 0; i < filelist.length; i++) {
					String name = filelist[i];
					if (!name.toLowerCase().endsWith(".properties")) {
						continue;
					}
					String filename = configPath + sp + name;
					Properties properties = null;
					FileInputStream inputStream = null;
					try {
						inputStream = new FileInputStream(filename);
						properties = PropertiesLoader.loadProperties(inputStream);
						Database database = new Database();
						database.setUrl(properties.getProperty("jdbc.url"));
						int port = 0;
						if (properties.getProperty("port") != null
								&& StringUtils.isNumeric(properties.getProperty("port"))) {
							port = Integer.parseInt(properties.getProperty("port"));
						}
						database.setPort(port);
						database.setDatabaseName(properties.getProperty("databaseName"));
						database.setDriverClassName(properties.getProperty("jdbc.driver"));
						database.setSubject(properties.getProperty("subject"));
						database.setName(properties.getProperty("jdbc.name"));
						Set<Entry<Object, Object>> entrySet = properties.entrySet();
						for (Entry<Object, Object> entry : entrySet) {
							String key = (String) entry.getKey();
							Object value = entry.getValue();
							if (value != null) {
								database.getDataMap().put(key, value);
							}
						}
						rows.add(database);
					} catch (IOException ex) {
					}
				}
			}
		}
		return rows;
	}

	public void reconfig(String appPath, Database database) {
		this.check(appPath, database);
		this.writeJdbcProperties(appPath, database);
	}

	public void check(String appPath, Database database) {
		String path = appPath + sp + "WEB-INF" + sp + "lib";
		String url = database.getUrl();
		if (url != null) {
			if (database.getHost() == null || database.getHost().equals("")) {
				database.setHost("localhost");
			}
			url = ConfigTools.replaceIgnoreCase(url, "${host}", database.getHost());
			url = ConfigTools.replaceIgnoreCase(url, "${port}", String.valueOf(database.getPort()));
			url = ConfigTools.replaceIgnoreCase(url, "${databaseName}", database.getDatabaseName());
			database.setUrl(url);
		}

		JarLoader ucl = AccessController.doPrivileged(new PrivilegedAction<JarLoader>() {
			public JarLoader run() {
				return new JarLoader();
			}
		});
		try {
			File file = new File(path);
			if (file.isDirectory()) {
				String[] filelist = file.list();
				if (filelist != null) {
					for (int i = 0; i < filelist.length; i++) {
						String name = filelist[i];
						if (!name.toLowerCase().endsWith(".jar")) {
							continue;
						}
						String filename = path + sp + name;
						ucl.addURL("file:" + filename);
					}
				}
			}
		} catch (Exception ex) {
			javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
		}

		try {
			ucl.loadClass(database.getDriverClassName());
		} catch (ClassNotFoundException ex) {
			try {
				Thread.currentThread().getContextClassLoader().loadClass(database.getDriverClassName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		java.sql.Driver driver = null;
		try {
			driver = (java.sql.Driver) ucl.loadClass(database.getDriverClassName()).newInstance();
		} catch (Exception ex) {
			try {
				driver = (java.sql.Driver) Thread.currentThread().getContextClassLoader()
						.loadClass(database.getDriverClassName()).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		Connection con = null;
		try {
			java.util.Properties info = new java.util.Properties();
			info.put("user", database.getUsername());
			info.put("password", database.getPassword());
			con = driver.connect(database.getUrl(), info);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("不能连接数据库，请检查主机、数据库名称及用户名密码是否正确。", ex);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void writeJdbcProperties(String appPath, Database database) {
		String filename = appPath + sp + "WEB-INF" + sp + "conf" + sp + "jdbc.properties";
		Properties properties = null;
		FileInputStream inputStream = null;
		try {
			setFileWritable(filename);
			inputStream = new FileInputStream(filename);
			properties = PropertiesLoader.loadProperties(inputStream);
		} catch (IOException ex) {
			throw new RuntimeException("不能读取jdbc配置文件", ex);
		}

		if (database.getDatasourceName() != null && database.getDatasourceName().trim().length() > 0) {
			properties.put("jdbc.datasource", database.getDatasourceName());
		} else {
			if (database.getDriverClassName() != null) {
				properties.put("jdbc.driver", database.getDriverClassName());
			}

			String url = database.getUrl();
			if (url != null) {
				properties.put("jdbc.type", getDatabaseType(url));
				properties.put("jdbc.url", url);
			}

			if (database.getUsername() != null) {
				properties.put("jdbc.user", database.getUsername());
			}

			if (database.getPassword() != null) {
				properties.put("jdbc.password", database.getPassword());
			} else {
				properties.put("jdbc.password", "");
			}

			if (database.getDatabaseName() != null) {
				properties.put("database", database.getDatabaseName());
			}

			if (database.getHost() != null) {
				properties.put("host", database.getHost());
			}

			if (database.getPort() != 0) {
				properties.put("port", String.valueOf(database.getPort()));
			} else {
				properties.remove("port");
			}

			properties.remove("jdbc.datasource");
			System.out.println(properties);
		}
		try {
			ConfigTools.save(filename, properties);
		} catch (IOException ex) {
			throw new RuntimeException("不能保存jdbc配置文件", ex);
		}

	}

	public String getDatabaseType(String url) {
		String dbType = null;
		if (StringUtils.contains(url, "jdbc:mysql:")) {
			dbType = "mysql";
		} else if (StringUtils.contains(url, "jdbc:postgresql:")) {
			dbType = "postgresql";
		} else if (StringUtils.contains(url, "jdbc:h2:")) {
			dbType = "h2";
		} else if (StringUtils.contains(url, "jdbc:jtds:sqlserver:")) {
			dbType = "sqlserver";
		} else if (StringUtils.contains(url, "jdbc:sqlserver:")) {
			dbType = "sqlserver";
		} else if (StringUtils.contains(url, "jdbc:oracle:")) {
			dbType = "oracle";
		} else if (StringUtils.contains(url, "jdbc:db2:")) {
			dbType = "db2";
		} else if (StringUtils.contains(url, "jdbc:sqlite:")) {
			dbType = "sqlite";
		} else if (StringUtils.contains(url, "jdbc:phoenix:")) {
			dbType = "hbase";
		}
		return dbType;
	}

	private void setFileWritable(String fileName) {
		try {
			File file = new File(fileName);
			if (!file.canWrite()) {
				file.setWritable(true);
			}
		} catch (Exception re) {
			throw new RuntimeException("配置文件不能执行更新操作，请去掉文件的只读属性。", re);
		}
	}

	private static class JarLoader extends URLClassLoader {
		private JarLoader() {
			super(new URL[0], ClassLoader.getSystemClassLoader());
		}

		public void addURL(String url) throws MalformedURLException {
			this.addURL(new URL(url));
		}

	}

}