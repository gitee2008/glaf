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

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.domain.ServerEntity;
import com.glaf.core.util.FtpUtils;
import com.glaf.core.util.UUID32;

public class FTPServerValidator implements IServerValidator {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean verify(ServerEntity serverEntity) {
		//logger.debug(serverEntity.toJsonObject().toJSONString());
		FTPClient ftpClient = null;
		try {
			if (FtpUtils.checkConfig(serverEntity.getHost(), serverEntity.getPort(), serverEntity.getUser(),
					serverEntity.getPassword())) {
				ftpClient = FtpUtils.connectServer(serverEntity.getHost(), serverEntity.getPort(),
						serverEntity.getUser(), serverEntity.getPassword());
				String remotePath = "/tmp_" + UUID32.getUUID() + ".txt";
				FtpUtils.upload(ftpClient, remotePath, UUID32.getUUID().getBytes());
				FtpUtils.deleteFile(ftpClient, remotePath);
				return true;
			}
		} catch (Exception ex) {
			
		} finally {
			FtpUtils.closeConnect(ftpClient);
		}
		return false;
	}

	public static void main(String[] args) {
		ServerEntity server = new ServerEntity();
		server.setHost("127.0.0.1");
		server.setPort(21);
		server.setUser("joy");
		server.setPassword("111111");
		server.setPath("/");
		System.out.println(server.toString());
		IServerValidator serverValidator = new FTPServerValidator();
		System.out.println(serverValidator.verify(server));
	}

}
