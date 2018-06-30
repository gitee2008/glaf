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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLUtils {

	/**
	 * 标准连接. 如果服务器证书已经通过CA认证, 可以使用此方式.
	 *
	 * @return 支持 SSL 的 Socket
	 * @throws IOException
	 * 
	 */
	public static Socket createSocketDefault() throws IOException {
		return SSLSocketFactory.getDefault().createSocket();
	}

	/**
	 * 无证书连接. 跳过信任检查, 无法防止服务器伪造
	 *
	 * @return 支持 SSL 的 Socket
	 * @throws IOException
	 * 
	 */
	public static Socket createSocketSkipCheckTrusted() throws IOException {
		// 构造伪造的 TrustManager
		X509TrustManager[] trustManagers = { new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

			}

			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} };

		// 使用 TrustManager 构造 SSLContext 和 SSLSocket
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustManagers, null);
			return sslContext.getSocketFactory().createSocket();
		} catch (GeneralSecurityException e) {
			// 一般情况下不应该发生 SecurityException, 转换为 IOException
			SSLException exception = new SSLException(e.toString());
			exception.initCause(e);
			throw exception;
		}
	}

	/**
	 * 通过证书 (certificate) 连接.
	 *
	 * @param certFile
	 *            存放服务器证书的文件路径, 必须是 X.509 证书格式
	 * @return 支持 SSL 的 Socket
	 * @throws IOException
	 */
	public static Socket createSocketWithCertificate(String certFile) throws IOException {
		try {
			// 载入证书
			InputStream inStream = new FileInputStream(certFile);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate cert = cf.generateCertificate(inStream);
			inStream.close();

			// 建立 KeyStore
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, null);
			keyStore.setCertificateEntry("mycert", cert);

			// 使用证书建立 TrustManager
			TrustManagerFactory managerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			managerFactory.init(keyStore);

			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, managerFactory.getTrustManagers(), null);

			return sslContext.getSocketFactory().createSocket();

		} catch (GeneralSecurityException e) {
			// 一般情况下不应该发生 SecurityException, 转换为 IOException
			SSLException exception = new SSLException(e.toString());
			exception.initCause(e);
			throw exception;
		}
	}

	/**
	 * 通过证书 (keystore) 连接 - 复杂方式
	 *
	 * @param jksFile
	 *            存放 keystore 的文件路径, 必须是 jks 格式
	 * @return 支持 SSL 的 Socket
	 * @throws IOException
	 * 
	 */
	public static Socket createSocketWithTrustStore(String jksFile) throws IOException {
		try {
			// 载入证书 (使用KeyStore)
			FileInputStream inStream = new FileInputStream(jksFile);
			KeyStore keyStore = KeyStore.getInstance("jks");
			keyStore.load(inStream, null);
			inStream.close();

			// 使用证书建立 TrustManager
			TrustManagerFactory managerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			managerFactory.init(keyStore);

			// 使用 TrustManager 构造 SSLContext 和 SSLSocket
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, managerFactory.getTrustManagers(), null);
			return sslContext.getSocketFactory().createSocket();
		} catch (GeneralSecurityException e) {
			// 一般情况下不应该发生 SecurityException, 转换为 IOException
			SSLException exception = new SSLException(e.toString());
			exception.initCause(e);
			throw exception;
		}
	}

	/**
	 * 通过证书 (keystore) 连接 - 简单方式.
	 *
	 * @param jksFile
	 *            存放 keystore 的文件路径, 必须是 jks 格式
	 * @return 支持 SSL 的 Socket
	 * @throws IOException
	 * 
	 */
	public static Socket createSocketWithTrustStoreSimple(String jksFile) throws IOException {
		System.setProperty("javax.net.ssl.trustStore", jksFile);
		return SSLSocketFactory.getDefault().createSocket();
	}

}
