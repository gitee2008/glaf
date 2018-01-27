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

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 加解密工具类
 */
public class RSA {
	/**
	 * 定义加密方式
	 */
	private final static String KEY_RSA = "RSA";
	/**
	 * 定义签名算法
	 */
	private final static String KEY_RSA_SIGNATURE = "SHA512withRSA";
	/**
	 * 定义公钥算法
	 */
	private final static String KEY_RSA_PUBLICKEY = "RSAPublicKey";
	/**
	 * 定义私钥算法
	 */
	private final static String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";

	static {
		try {
			String provider = "org.bouncycastle.jce.provider.BouncyCastleProvider";
			java.security.Security.addProvider((Provider) Class.forName(provider).newInstance());
		} catch (Exception ex) {

		}
	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 */
	public static Map<String, Object> init() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
			generator.initialize(1024);
			KeyPair keyPair = generator.generateKeyPair();
			// 公钥
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			// 私钥
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			map.put(KEY_RSA_PUBLICKEY, publicKey);
			map.put(KEY_RSA_PRIVATEKEY, privateKey);
		} catch (NoSuchAlgorithmException ex) {

		}
		return map;
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static String sign(byte[] data, String privateKey) {
		String str = "";
		try {
			// 解密由base64编码的私钥
			byte[] bytes = decodeBase64(privateKey);
			// 构造PKCS8EncodedKeySpec对象
			PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
			// 指定的加密算法
			KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
			// 取私钥对象
			PrivateKey key = factory.generatePrivate(pkcs);
			// 用私钥对信息生成数字签名
			Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
			signature.initSign(key);
			signature.update(data);
			str = encodeBase64String(signature.sign());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * @return 校验成功返回true，失败返回false
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) {
		boolean flag = false;
		try {
			// 解密由base64编码的公钥
			byte[] bytes = decodeBase64(publicKey);
			// 构造X509EncodedKeySpec对象
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
			// 指定的加密算法
			KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
			// 取公钥对象
			PublicKey key = factory.generatePublic(keySpec);
			// 用公钥验证数字签名
			Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
			signature.initVerify(key);
			signature.update(data);
			flag = signature.verify(decodeBase64(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            私钥
	 * @return
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) {
		byte[] result = null;
		try {
			// 对私钥解密
			byte[] bytes = decodeBase64(key);
			// 取得私钥
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
			KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
			PrivateKey privateKey = factory.generatePrivate(keySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			result = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            公钥
	 * @return
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) {
		byte[] result = null;
		try {
			// 对公钥解密
			byte[] bytes = decodeBase64(key);
			// 取得公钥
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
			KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
			PublicKey publicKey = factory.generatePublic(keySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			result = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            公钥
	 * @return
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key) {
		byte[] result = null;
		try {
			byte[] bytes = decodeBase64(key);
			// 取得公钥
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
			KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
			PublicKey publicKey = factory.generatePublic(keySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			result = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            私钥
	 * @return
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) {
		byte[] result = null;
		try {
			byte[] bytes = decodeBase64(key);
			// 取得私钥
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
			KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
			PrivateKey privateKey = factory.generatePrivate(keySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			result = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取公钥
	 * 
	 * @param map
	 * @return
	 */
	public static String getPublicKey(Map<String, Object> map) {
		String str = "";
		try {
			Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
			str = encodeBase64String(key.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 获取私钥
	 * 
	 * @param map
	 * @return
	 */
	public static String getPrivateKey(Map<String, Object> map) {
		String str = "";
		try {
			Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);
			str = encodeBase64String(key.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * BASE64 解码
	 * 
	 * @param data
	 *            需要解码的字符串
	 * @return 字节数组
	 */
	public static byte[] decodeBase64(String data) {
		return org.apache.commons.codec.binary.Base64.decodeBase64(data);
	}

	/**
	 * BASE64 编码
	 * 
	 * @param data
	 *            需要编码的字节数组
	 * @return 字符串
	 */
	public static String encodeBase64String(byte[] data) {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(data);
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		for (Provider provider : Security.getProviders()) {
			System.out.println("Provider: " + provider.getName());
			for (Provider.Service service : provider.getServices()) {
				System.out.println("  Algorithm: " + service.getAlgorithm());
			}
			System.out.println("\n");
		}

		String privateKey = "";
		String publicKey = "";
		// 生成公钥私钥
		Map<String, Object> map = init();
		publicKey = getPublicKey(map);
		privateKey = getPrivateKey(map);
		System.out.println("公钥: \n\r" + publicKey);
		System.out.println("私钥： \n\r" + privateKey);
		System.out.println("公钥加密--------私钥解密");
		String word = "你好，世界！";
		byte[] encWord = encryptByPublicKey(word.getBytes(), publicKey);
		String decWord = new String(decryptByPrivateKey(encWord, privateKey));
		System.out.println("加密前: " + word + "\n\r" + "解密后: " + decWord);
		System.out.println("私钥加密--------公钥解密");
		String english = "Hello, World!";
		byte[] encEnglish = encryptByPrivateKey(english.getBytes(), privateKey);
		String decEnglish = new String(decryptByPublicKey(encEnglish, publicKey));
		System.out.println("加密前: " + english + "\n\r" + "解密后: " + decEnglish);
		System.out.println("私钥签名——公钥验证签名");
		// 产生签名
		String sign = sign(encEnglish, privateKey);
		System.out.println("签名:\r" + sign);
		// 验证签名
		boolean status = verify(encEnglish, publicKey, sign);
		System.out.println("状态:\r" + status);
	}
}