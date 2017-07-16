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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import com.glaf.core.domain.ServerEntity;

public class SSHServerValidator implements IServerValidator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean verify(ServerEntity serverEntity) {
		Session session = null;
		Channel channel = null;
		try {
			String host = serverEntity.getHost();
			int port = serverEntity.getPort();
			String userName = serverEntity.getUser();
			String password = serverEntity.getPassword();

			JSch jsch = new JSch(); // 创建JSch对象
			session = jsch.getSession(userName, host, port); // 根据用户名，主机ip，端口获取一个Session对象
			logger.debug("Session created.");
			if (password != null && password.trim().length() > 0) {
				session.setPassword(password); // 设置密码
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config); // 为Session对象设置properties
			session.setTimeout(1000 * 60); // 设置timeout时间，毫秒
			session.connect(); // 通过Session建立链接
			logger.debug("Session connected.");
			logger.debug("Opening Channel.");
			channel = session.openChannel("sftp"); // 打开SFTP通道
			channel.connect(); // 建立SFTP通道的连接
			logger.debug("Connected successfully to host = " + host + ",as userName = " + userName + ", returning: "
					+ channel);
			return true;
		} catch (Exception ex) {
			
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ServerEntity server = new ServerEntity();
		server.setHost("192.168.10.122");
		server.setPort(22);
		server.setUser("root");
		server.setPassword("888888");
		server.setPath("/");
		System.out.println(server.toString());
		IServerValidator serverValidator = new SSHServerValidator();
		System.out.println(serverValidator.verify(server));
	}

}
