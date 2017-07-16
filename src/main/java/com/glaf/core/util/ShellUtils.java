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

package com.glaf.core.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ShellUtils {
	protected final static Logger logger = LoggerFactory.getLogger(ShellUtils.class);

	protected static void checkCommand(String command) {
		if (StringUtils.isNotEmpty(command)) {
			if (StringUtils.containsIgnoreCase(command, " rm ") || StringUtils.containsIgnoreCase(command, " passwd ")
					|| StringUtils.containsIgnoreCase(command, " mv ")
					|| StringUtils.containsIgnoreCase(command, " reboot ")
					|| StringUtils.containsIgnoreCase(command, " shutdown ")) {
				throw new RuntimeException(command + " execute deny");
			}
		}
	}

	/**
	 * 执行相关的命令
	 * 
	 * @throws JSchException
	 */
	public static String exec(Session session, String command) {
		checkCommand(command);
		Channel channel = null;
		BufferedReader reader = null;
		OutputStream outputSteam = null;
		ByteArrayOutputStream baos = null;
		StringBuilder buffer = new StringBuilder();
		try {
			if (StringUtils.isNotEmpty(command)) {
				channel = session.openChannel("exec");
				logger.debug("Opening Channel.");
				logger.debug("Execute Command:" + command);
				((ChannelExec) channel).setCommand(command);
				channel.setInputStream(null);
				baos = new ByteArrayOutputStream();
				outputSteam = new BufferedOutputStream(baos);
				((ChannelExec) channel).setErrStream(outputSteam);
				channel.connect();
				logger.debug("Channel connected.");
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in));
				String buff = null;
				while ((buff = reader.readLine()) != null) {
					buffer.append(buff);
					buffer.append(FileUtils.newline);
				}
				if (baos.toByteArray().length > 0) {
					buffer.append(new String(baos.toByteArray()));
				}
			}

			return buffer.toString();
		} catch (IOException ex) {
			
			throw new RuntimeException(ex);
		} catch (JSchException ex) {
			
			throw new RuntimeException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
				}
			}
			if (outputSteam != null) {
				try {
					outputSteam.close();
				} catch (IOException ex) {
				}
			}
			if (channel != null) {
				channel.disconnect();
			}
		}
	}

	/**
	 * 执行相关的命令
	 * 
	 * @throws JSchException
	 */
	public static String exec(String host, int port, String user, String password, String command) {
		checkCommand(command);
		JSch jsch = null;
		Channel channel = null;
		Session session = null;
		BufferedReader reader = null;
		ByteArrayOutputStream baos = null;
		OutputStream outputSteam = null;
		StringBuilder buffer = new StringBuilder();
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			logger.debug("Session created.");
			if (password != null && password.trim().length() > 0) {
				session.setPassword(password); // 设置密码
			}

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(1000 * 60);
			session.connect();
			logger.debug("Session connected.");

			if (StringUtils.isNotEmpty(command)) {
				channel = session.openChannel("exec");
				logger.debug("Opening Channel.");
				logger.debug("Execute Command:" + command);
				((ChannelExec) channel).setCommand(command);
				channel.setInputStream(null);
				baos = new ByteArrayOutputStream();
				outputSteam = new BufferedOutputStream(baos);
				((ChannelExec) channel).setErrStream(outputSteam);
				channel.connect();
				logger.debug("Channel connected.");
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in));
				String buff = null;
				while ((buff = reader.readLine()) != null) {
					buffer.append(buff);
					buffer.append(FileUtils.newline);
				}
				if (baos.toByteArray().length > 0) {
					buffer.append(new String(baos.toByteArray()));
				}
			}

			return buffer.toString();
		} catch (IOException ex) {
			
			throw new RuntimeException(ex);
		} catch (JSchException ex) {
			
			throw new RuntimeException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
				}
			}
			if (outputSteam != null) {
				try {
					outputSteam.close();
				} catch (IOException ex) {
				}
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	/**
	 * 执行相关的命令
	 * 
	 * @throws JSchException
	 */
	public static boolean execCmd(Session session, String command) {
		checkCommand(command);
		Channel channel = null;
		BufferedReader reader = null;
		ByteArrayOutputStream baos = null;
		OutputStream outputSteam = null;
		try {
			if (StringUtils.isNotEmpty(command)) {
				channel = session.openChannel("exec");
				logger.debug("Opening Channel.");
				logger.debug("Execute Command:" + command);
				((ChannelExec) channel).setCommand(command);
				channel.setInputStream(null);
				baos = new ByteArrayOutputStream();
				outputSteam = new BufferedOutputStream(baos);
				((ChannelExec) channel).setErrStream(outputSteam);
				channel.connect();
				logger.debug("Channel connected.");
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in));
				String buff = null;
				while ((buff = reader.readLine()) != null) {
					logger.debug(buff);
				}

				if (baos.toByteArray().length > 0) {
					logger.error(new String(baos.toByteArray()));
					return false;
				}

				return true;
			}

			return false;
		} catch (IOException ex) {
			
			throw new RuntimeException(ex);
		} catch (JSchException ex) {
			
			throw new RuntimeException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
				}
			}
			if (outputSteam != null) {
				try {
					outputSteam.close();
				} catch (IOException ex) {
				}
			}
			if (channel != null) {
				channel.disconnect();
			}
		}
	}

	/**
	 * 执行相关的命令
	 * 
	 * @throws JSchException
	 */
	public static Map<String, Boolean> execCmd(String host, int port, String user, String password,
			List<String> commands) {
		JSch jsch = null;
		Channel channel = null;
		Session session = null;
		BufferedReader reader = null;
		ByteArrayOutputStream baos = null;
		OutputStream outputSteam = null;
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			logger.debug("Session created.");
			if (password != null && password.trim().length() > 0) {
				session.setPassword(password); // 设置密码
			}

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(1000 * 60);
			session.connect();
			logger.debug("Session connected.");

			for (String command : commands) {
				if (StringUtils.isNotEmpty(command)) {
					checkCommand(command);
					logger.debug("Execute Command:" + command);
					channel = session.openChannel("exec");
					logger.debug("Opening Channel.");
					((ChannelExec) channel).setCommand(command);
					channel.setInputStream(null);
					baos = new ByteArrayOutputStream();
					outputSteam = new BufferedOutputStream(baos);
					((ChannelExec) channel).setErrStream(outputSteam);
					channel.connect();
					logger.debug("Channel connected.");
					InputStream in = channel.getInputStream();
					reader = new BufferedReader(new InputStreamReader(in));
					String buff = null;
					while ((buff = reader.readLine()) != null) {
						logger.debug(buff);
					}
					channel.disconnect();
					resultMap.put(command, true);
					if (baos.toByteArray().length > 0) {
						logger.error(new String(baos.toByteArray()));
						resultMap.put(command, false);
					}
				}
			}

			return resultMap;
		} catch (IOException ex) {
			
			throw new RuntimeException(ex);
		} catch (JSchException ex) {
			
			throw new RuntimeException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
				}
			}
			if (outputSteam != null) {
				try {
					outputSteam.close();
				} catch (IOException ex) {
				}
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	/**
	 * 执行相关的命令
	 * 
	 * @throws JSchException
	 */
	public static boolean execCmd(String host, int port, String user, String password, String command) {
		checkCommand(command);
		JSch jsch = null;
		Channel channel = null;
		Session session = null;
		BufferedReader reader = null;
		ByteArrayOutputStream baos = null;
		OutputStream outputSteam = null;
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			logger.debug("Session created.");
			if (password != null && password.trim().length() > 0) {
				session.setPassword(password); // 设置密码
			}

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(1000 * 60);
			session.connect();
			logger.debug("Session connected.");

			if (StringUtils.isNotEmpty(command)) {
				channel = session.openChannel("exec");
				logger.debug("Opening Channel.");
				logger.debug("Execute Command:" + command);
				((ChannelExec) channel).setCommand(command);
				channel.setInputStream(null);
				baos = new ByteArrayOutputStream();
				outputSteam = new BufferedOutputStream(baos);
				((ChannelExec) channel).setErrStream(outputSteam);
				channel.connect();
				logger.debug("Channel connected.");
				InputStream in = channel.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in));
				String buff = null;
				while ((buff = reader.readLine()) != null) {
					logger.debug(buff);
				}

				if (baos.toByteArray().length > 0) {
					logger.error(new String(baos.toByteArray()));
					return false;
				}

				return true;
			}

			return false;
		} catch (IOException ex) {
			
			throw new RuntimeException(ex);
		} catch (JSchException ex) {
			
			throw new RuntimeException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
				}
			}
			if (outputSteam != null) {
				try {
					outputSteam.close();
				} catch (IOException ex) {
				}
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.out.println(ShellUtils.execCmd("192.168.10.122", 22, "root", "888888",
				"/usr/sbin/rabbitmqctl add_user  adminmq  mtxx87668438 "));
		System.out.println(ShellUtils.execCmd("192.168.10.122", 22, "root", "888888",
				"/usr/sbin/rabbitmqctl change_password  adminmq  mtxx87668438 "));
		System.out.println(ShellUtils.execCmd("192.168.10.122", 22, "root", "888888",
				"/usr/sbin/rabbitmqctl set_user_tags adminmq administrator "));
		System.out.println(ShellUtils.execCmd("192.168.10.122", 22, "root", "888888",
				"/usr/sbin/rabbitmqctl list_user_permissions  adminmq "));
		long ts = System.currentTimeMillis() - start;
		System.out.println("ts1:" + ts);
		start = System.currentTimeMillis();
		Session session = ShellUtils.openSession("192.168.10.122", 22, "root", "888888");
		System.out.println(ShellUtils.execCmd(session, "/usr/sbin/rabbitmqctl add_user  adminmq  mtxx87668438 "));
		System.out
				.println(ShellUtils.execCmd(session, "/usr/sbin/rabbitmqctl change_password  adminmq  mtxx87668438 "));
		System.out.println(ShellUtils.execCmd(session, "/usr/sbin/rabbitmqctl set_user_tags  adminmq  administrator "));
		System.out.println(ShellUtils.execCmd(session, "/usr/sbin/rabbitmqctl list_user_permissions  adminmq "));
		session.disconnect();
		ts = System.currentTimeMillis() - start;
		System.out.println("ts2:" + ts);
		start = System.currentTimeMillis();
		List<String> cmds = new ArrayList<String>();
		cmds.add("/usr/sbin/rabbitmqctl add_user  adminmq  mtxx87668438");
		cmds.add("/usr/sbin/rabbitmqctl change_password  adminmq  mtxx87668438");
		cmds.add("/usr/sbin/rabbitmqctl set_user_tags  adminmq  administrator");
		cmds.add("/usr/sbin/rabbitmqctl list_user_permissions  adminmq");
		Map<String, Boolean> resultMap = ShellUtils.execCmd("192.168.10.122", 22, "root", "888888", cmds);
		System.out.println(resultMap);
		ts = System.currentTimeMillis() - start;
		System.out.println("ts3:" + ts);

	}

	public static Session openSession(String host, int port, String user, String password) {
		JSch jsch = null;
		Session session = null;
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			logger.debug("Session created.");
			if (password != null && password.trim().length() > 0) {
				session.setPassword(password); // 设置密码
			}

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(1000 * 60);
			session.connect();
			logger.debug("Session connected.");

			return session;
		} catch (JSchException ex) {
			
			throw new RuntimeException(ex);
		}
	}

}
