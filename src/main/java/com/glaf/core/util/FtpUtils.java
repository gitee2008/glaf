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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.config.BaseConfiguration;
import com.glaf.core.config.Configuration;

public class FtpUtils {

	protected final static Logger logger = LoggerFactory.getLogger(FtpUtils.class);

	protected static Configuration conf = BaseConfiguration.create();

	private static void changeToDirectory(FTPClient ftpClient, String remoteFile) {
		if (!remoteFile.startsWith("/")) {
			throw new RuntimeException(" path must start with '/'");
		}
		if (remoteFile.startsWith("/") && remoteFile.indexOf("/") > 0) {
			try {
				ftpClient.changeWorkingDirectory("/");
				String tmp = "";
				remoteFile = remoteFile.substring(0, remoteFile.lastIndexOf("/"));
				StringTokenizer token = new StringTokenizer(remoteFile, "/");
				while (token.hasMoreTokens()) {
					String str = token.nextToken();
					tmp = tmp + "/" + str;
					ftpClient.changeWorkingDirectory(tmp);
				}
			} catch (IOException ex) {

			}
		}
	}

	/**
	 * 
	 * @param ip
	 *            服务器名或IP地址
	 * @param port
	 *            端口
	 * @param user
	 *            用户名
	 * @param password
	 *            密码
	 */
	public static boolean checkConfig(String ip, int port, String user, String password) {
		try {
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect(ip, port);
			boolean success = ftpClient.login(user, password);
			if (success) {
				logger.info("login success!");
			} else {
				logger.info("login failed");
			}
			return success;
		} catch (IOException ex) {
			logger.error("login failed", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 关闭FTP
	 */
	public static void closeConnect(FTPClient ftpClient) {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
				logger.info("disconnect success");
			} catch (IOException ex) {
				logger.error("disconnect error", ex);
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * 
	 * @param ip
	 *            服务器名或IP地址
	 * @param port
	 *            端口
	 * @param user
	 *            用户名
	 * @param password
	 *            密码
	 */
	public static FTPClient connectServer(String ip, int port, String user, String password) {
		try {
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect(ip, port);
			boolean success = ftpClient.login(user, password);
			if (success) {
				logger.info("login success!");
			} else {
				logger.info("login failed");
			}
			return ftpClient;
		} catch (IOException ex) {
			logger.error("login failed", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 删除FTP文件
	 * 
	 * @param remotePath
	 *            FTP路径，必须以"/"开头
	 */
	public static void deleteFile(FTPClient ftpClient, String remotePath) {
		if (!remotePath.startsWith("/")) {
			throw new RuntimeException(" path must start with '/'");
		}
		try {
			if (remotePath.indexOf("/") != -1) {
				String tmp = "";
				List<String> dirs = new ArrayList<String>();
				StringTokenizer token = new StringTokenizer(remotePath, "/");
				while (token.hasMoreTokens()) {
					String str = token.nextToken();
					tmp = tmp + "/" + str;
					dirs.add(tmp);
				}
				for (int i = 0; i < dirs.size() - 1; i++) {
					ftpClient.changeWorkingDirectory(dirs.get(i));
				}
				String dir = remotePath.substring(remotePath.lastIndexOf("/") + 1, remotePath.length());
				logger.debug("rm " + dir);
				ftpClient.deleteFile(dir);
			} else {
				ftpClient.deleteFile(remotePath);
			}
		} catch (IOException ex) {
			logger.error("deleteFile error", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param remoteFile
	 *            FTP文件，必须以"/"开头
	 * @param localFile
	 *            本地文件全路径
	 */
	public static void download(FTPClient ftpClient, String remoteFile, String localFile) {
		if (!remoteFile.startsWith("/")) {
			throw new RuntimeException(" path must start with '/'");
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			if (remoteFile.startsWith("/") && remoteFile.indexOf("/") > 0) {
				changeToDirectory(ftpClient, remoteFile);
				remoteFile = remoteFile.substring(remoteFile.lastIndexOf("/") + 1, remoteFile.length());
			}
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] files = ftpClient.listFiles(new String(remoteFile.getBytes("GBK"), "ISO-8859-1"));
			if (files.length != 1) {
				logger.warn("remote file is not exists");
				return;
			}
			long lRemoteSize = files[0].getSize();
			File file = new File(localFile);
			out = new FileOutputStream(file);
			in = ftpClient.retrieveFileStream(new String(remoteFile.getBytes("GBK"), "ISO-8859-1"));
			byte[] bytes = new byte[4096];
			long step = lRemoteSize / 100;
			if (step == 0) {
				step = 1;
			}
			long progress = 0;
			long localSize = 0L;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProgress = localSize / step;
				if (nowProgress > progress) {
					progress = nowProgress;
					if (progress % 10 == 0) {
						logger.debug("download progress:" + progress);
					}
				}
			}
			out.flush();
		} catch (Exception ex) {
			logger.error("download error", ex);
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param remoteFile
	 *            FTP文件，必须以"/"开头
	 */
	public static byte[] getBytes(FTPClient ftpClient, String remoteFile) {
		byte[] bytes = null;
		InputStream in = null;
		ByteArrayOutputStream out = null;
		BufferedOutputStream bos = null;
		try {
			if (remoteFile.startsWith("/") && remoteFile.indexOf("/") > 0) {
				changeToDirectory(ftpClient, remoteFile);
				remoteFile = remoteFile.substring(remoteFile.lastIndexOf("/") + 1, remoteFile.length());
			}
			// 设置被动模式
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] files = ftpClient.listFiles(new String(remoteFile.getBytes("GBK"), "ISO-8859-1"));
			if (files.length != 1) {
				logger.warn("remote file is not exists");
				return null;
			}
			long lRemoteSize = files[0].getSize();

			out = new ByteArrayOutputStream();
			bos = new BufferedOutputStream(out);
			in = ftpClient.retrieveFileStream(new String(remoteFile.getBytes("GBK"), "ISO-8859-1"));
			byte[] buff = new byte[4096];
			long step = lRemoteSize / 100;
			if (step == 0) {
				step = 1;
			}
			long progress = 0;
			long localSize = 0L;
			int c;
			while ((c = in.read(buff)) != -1) {
				out.write(buff, 0, c);
				localSize += c;
				long nowProgress = localSize / step;
				if (nowProgress > progress) {
					progress = nowProgress;
					if (progress % 10 == 0) {
						logger.debug("download progress:" + progress);
					}
				}
			}
			out.flush();
			bos.flush();
			bytes = out.toByteArray();
		} catch (Exception ex) {
			logger.error("download error", ex);
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return bytes;
	}

	/**
	 * 创建FTP文件目录
	 * 
	 * @param remotePath
	 *            FTP路径，必须以"/"开头
	 */
	public static void mkdirs(FTPClient ftpClient, String remotePath) {
		if (remotePath.startsWith("/")) {
			try {
				ftpClient.changeWorkingDirectory("/");

				if (remotePath.indexOf("/") != -1) {
					String tmp = "";
					StringTokenizer token = new StringTokenizer(remotePath, "/");
					while (token.hasMoreTokens()) {
						String str = token.nextToken();
						tmp = tmp + "/" + str;
						ftpClient.mkd(str);
						ftpClient.changeWorkingDirectory(tmp);
					}
				}
			} catch (IOException ex) {
				logger.error("mkdirs error", ex);
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * 删除FTP文件目录
	 * 
	 * @param remotePath
	 *            FTP路径，必须以"/"开头
	 */
	public static void removeDirectory(FTPClient ftpClient, String remotePath) {
		if (!remotePath.startsWith("/")) {
			throw new RuntimeException(" path must start with '/'");
		}
		try {

			ftpClient.changeWorkingDirectory("/");

			if (remotePath.indexOf("/") != -1) {
				String tmp = "";
				List<String> dirs = new ArrayList<String>();
				StringTokenizer token = new StringTokenizer(remotePath, "/");
				while (token.hasMoreTokens()) {
					String str = token.nextToken();
					tmp = tmp + "/" + str;
					dirs.add(tmp);
				}
				for (int i = 0; i < dirs.size() - 1; i++) {
					ftpClient.changeWorkingDirectory(dirs.get(i));
				}
				String dir = remotePath.substring(remotePath.lastIndexOf("/") + 1, remotePath.length());
				logger.debug("rm " + dir);
				ftpClient.removeDirectory(dir);
			} else {
				ftpClient.rmd(remotePath);
			}
		} catch (IOException ex) {
			logger.error("removeDirectory error", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 根据文件路径上传
	 * 
	 * @param remoteFile
	 *            FTP文件，必须以"/"开头
	 * @param input
	 *            本地输入流
	 */
	public static boolean upload(FTPClient ftpClient, String remoteFile, byte[] bytes) {
		if (!remoteFile.startsWith("/")) {
			throw new RuntimeException(remoteFile + " path must start with '/'");
		}
		ByteArrayInputStream bais = null;
		BufferedInputStream bis = null;
		try {

			if (remoteFile.substring(0, remoteFile.lastIndexOf("/")).length() > 0) {
				mkdirs(ftpClient, remoteFile.substring(0, remoteFile.lastIndexOf("/")));
			}

			if (remoteFile.startsWith("/") && remoteFile.indexOf("/") > 0) {
				changeToDirectory(ftpClient, remoteFile);
				remoteFile = remoteFile.substring(remoteFile.lastIndexOf("/") + 1, remoteFile.length());
			}

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			bais = new ByteArrayInputStream(bytes);
			bis = new BufferedInputStream(bais);
			logger.debug("remoteFile:" + remoteFile);
			boolean flag = ftpClient.storeFile(remoteFile, bis);
			if (flag) {
				logger.info("upload success");
			} else {
				logger.info("upload failure");
			}
			return flag;
		} catch (IOException ex) {
			logger.error("upload error", ex);
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(bais);
		}
	}

	/**
	 * 根据文件路径上传
	 * 
	 * @param remoteFile
	 *            FTP文件，必须以"/"开头
	 * @param input
	 *            本地文件流
	 */
	public static boolean upload(FTPClient ftpClient, String remoteFile, InputStream input) {
		if (!remoteFile.startsWith("/")) {
			throw new RuntimeException(" path must start with '/'");
		}
		try {

			if (remoteFile.substring(0, remoteFile.lastIndexOf("/")).length() > 0) {
				mkdirs(ftpClient, remoteFile.substring(0, remoteFile.lastIndexOf("/")));
			}

			if (remoteFile.startsWith("/") && remoteFile.indexOf("/") > 0) {
				changeToDirectory(ftpClient, remoteFile);
				remoteFile = remoteFile.substring(remoteFile.lastIndexOf("/") + 1, remoteFile.length());
			}
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			boolean flag = ftpClient.storeFile(remoteFile, input);
			if (flag) {
				logger.info("upload success");
			} else {
				logger.info("upload failure");
			}
			return flag;
		} catch (IOException ex) {
			logger.error("upload error", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 根据文件路径上传
	 * 
	 * @param remoteFile
	 *            FTP文件，必须以"/"开头
	 * @param localFile
	 *            本地文件全路径
	 */
	public static boolean upload(FTPClient ftpClient, String remoteFile, String localFile) {
		if (!remoteFile.startsWith("/")) {
			throw new RuntimeException(" path must start with '/'");
		}
		InputStream input = null;
		try {

			if (remoteFile.substring(0, remoteFile.lastIndexOf("/")).length() > 0) {
				mkdirs(ftpClient, remoteFile.substring(0, remoteFile.lastIndexOf("/")));
			}

			if (remoteFile.startsWith("/") && remoteFile.indexOf("/") > 0) {
				changeToDirectory(ftpClient, remoteFile);
				remoteFile = remoteFile.substring(remoteFile.lastIndexOf("/") + 1, remoteFile.length());
			}

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
			File file = new File(localFile);
			input = new FileInputStream(file);
			boolean flag = ftpClient.storeFile(remoteFile, input);
			if (flag) {
				logger.info("upload success");
			} else {
				logger.info("upload failure");
			}
			return flag;
		} catch (IOException ex) {
			logger.error("upload error", ex);
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

}