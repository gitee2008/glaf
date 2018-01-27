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

package com.glaf.core.util.security;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.glaf.core.security.SecurityUtils;

public class DESUtils {

	public static final String KEY_ALGORITHM = "DES";

	public static final String ECB_CIPHER_ALGORITHM = "DES/ECB/PKCS7Padding";

	public static final String CBC_CIPHER_ALGORITHM = "DES/CBC/PKCS7Padding";

	static {
		try {
			String provider = "org.bouncycastle.jce.provider.BouncyCastleProvider";
			java.security.Security.addProvider((Provider) Class.forName(provider).newInstance());
		} catch (Exception ex) {
			
		}
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				hs.append('0');
			}
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

	/**
	 * DES算法，解密
	 * 
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @param data
	 *            待解密数据
	 * @return 解密后的数据
	 */
	public static byte[] decode(byte[] key, byte[] data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES", "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * DES解密
	 * 
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @param secretIv
	 *            密锁向量
	 * @param data
	 *            待解密字节流
	 * @return 解密后的数据
	 */
	public static byte[] decode(byte[] key, byte[] secretIv, byte[] data) {
		if (data == null || key == null || secretIv == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM, "BC");
			IvParameterSpec iv = new IvParameterSpec(secretIv);
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * DES解密
	 * 
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @param data
	 *            待解密字节流
	 * @return 解密后的字符串
	 */
	public static byte[] decode(String key, byte[] data) {
		if (data == null || key == null) {
			return null;
		}
		if (key.length() == 24) {
			return decode(key.getBytes(), data);
		}
		try {
			DESKeySpec dks = new DESKeySpec(getKeyBytes(key));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM, "BC");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * DES解密
	 * 
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @param secretIv
	 *            密锁向量
	 * @param data
	 *            待解密字节流
	 * @return 解密后的数据
	 */
	public static byte[] decode(String key, String secretIv, byte[] data) {
		if (data == null || key == null || secretIv == null) {
			return null;
		}
		if (key.length() == 24 && secretIv.length() == 8) {
			return decode(key.getBytes(), secretIv.getBytes(), data);
		}
		try {
			DESKeySpec dks = new DESKeySpec(getKeyBytes(key));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM, "BC");
			IvParameterSpec iv = new IvParameterSpec(getKeyIvBytes(secretIv));
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * DES解密
	 * 
	 * @param key
	 *            解密密钥
	 * @param data
	 *            待解密内容
	 * @return
	 */
	public static byte[] decrypt(byte[] key, byte[] data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(key);
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM, "BC");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			// 真正开始解密操作
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 3DES解密
	 * 
	 * @param input
	 *            需要解密的字节流
	 * @param secretKey
	 *            密钥
	 * @param secretIv
	 *            向量
	 * @return
	 */
	public static byte[] decrypt3DES(byte[] input, byte[] secretKey, byte[] secretIv) {
		if (input == null || secretKey == null || secretIv == null) {
			return null;
		}
		byte[] res = null;
		try {
			DESedeKeySpec spec = new DESedeKeySpec(secretKey);
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			Key deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS7Padding");
			IvParameterSpec ips = new IvParameterSpec(secretIv);
			cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
			res = cipher.doFinal(input);
			return res;
		} catch (Exception ex) {
			throw new RuntimeException("3DES CBC解密过程出现错误！", ex);
		}
	}

	/**
	 * 3DES解密
	 * 
	 * @param input
	 *            需要解密的字节流
	 * @param secretKey
	 *            密钥
	 * @param secretIv
	 *            向量
	 * @return
	 */
	public static byte[] decrypt3DES(byte[] input, String secretKey, String secretIv) {
		if (input == null || secretKey == null || secretIv == null) {
			return null;
		}
		if (secretKey.length() == 24 && secretIv.length() == 8) {
			return decrypt3DES(input, secretKey.getBytes(), secretIv.getBytes());
		}
		byte[] res = null;
		try {
			DESedeKeySpec spec = new DESedeKeySpec(getKeyBytes(secretKey));
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			Key deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS7Padding");
			IvParameterSpec ips = new IvParameterSpec(getKeyIvBytes(secretIv));
			cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
			res = cipher.doFinal(input);
			return res;
		} catch (Exception ex) {
			throw new RuntimeException("3DES CBC解密过程出现错误！", ex);
		}
	}

	/**
	 * 3DES CBC加密
	 * 
	 * @param input
	 *            需要加密的数据
	 * @param secretKey
	 *            密钥
	 * @param secretIv
	 *            向量
	 * @return
	 */
	public static byte[] ecrypt3DES(byte[] input, byte[] secretKey, byte[] secretIv) {
		if (input == null || secretKey == null || secretIv == null) {
			return null;
		}
		byte[] res = null;
		try {
			// 根据给定的字节数组和算法构造一个密钥
			SecretKey deskey = new SecretKeySpec(secretKey, "desede");
			// 加密
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS7Padding");
			IvParameterSpec ips = new IvParameterSpec(secretIv);
			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
			res = cipher.doFinal(input);
			return res;
		} catch (Exception ex) {
			throw new RuntimeException("3DES CBC加密过程出现错误！", ex);
		}
	}

	/**
	 * DES算法，加密
	 * 
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @param data
	 *            待加密数据
	 * 
	 * @return 加密后的字符串,16进制字符串
	 */
	public static String encode(byte[] key, byte[] data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES", "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data);
			return byte2hex(bytes);
		} catch (Exception ex) {
			throw new RuntimeException("加密过程出现错误！", ex);
		}
	}

	/**
	 * DES算法，加密
	 * 
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @param secretIv
	 *            密锁向量
	 * @param data
	 *            待加密数据
	 * @return 加密后的字节流
	 */
	public static byte[] encode(byte[] key, byte[] secretIv, byte[] data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM, "BC");
			IvParameterSpec iv = new IvParameterSpec(secretIv);
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException("加密过程出现错误！", ex);
		}
	}

	/**
	 * DES算法，加密
	 * 
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @param data
	 *            待加密数据
	 * 
	 * @return 加密后的字节流
	 */
	public static byte[] encode(String key, byte[] data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(getKeyBytes(key));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM, "BC");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException("加密过程出现错误！", ex);
		}
	}

	/**
	 * DES算法，加密
	 * 
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @param secretIv
	 *            密锁向量
	 * @param data
	 *            待加密数据
	 * @return 加密后的字节流
	 */
	public static byte[] encode(String key, String secretIv, byte[] data) {
		if (key == null || secretIv == null || data == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(getKeyBytes(key));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM, "BC");
			IvParameterSpec iv = new IvParameterSpec(getKeyIvBytes(secretIv));
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException("加密过程出现错误！", ex);
		}
	}

	/**
	 * DES加密
	 * 
	 * @param key
	 *            加密密钥
	 * @param data
	 *            需要加密的数据
	 * @return
	 */
	public static byte[] encrypt(byte[] key, byte[] data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, "BC");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM, "BC");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 3DES加密
	 * 
	 * @param key
	 *            加密密钥
	 * @param secretIv
	 *            向量
	 * @param data
	 *            需要加密的数据
	 * @return
	 */
	public static byte[] encrypt3DES(byte[] key, byte[] secretIv, byte[] data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			SecretKey deskey = new SecretKeySpec(key, "desede");
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS7Padding");
			IvParameterSpec ips = new IvParameterSpec(secretIv);
			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
			// 正式执行加密操作
			return cipher.doFinal(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 3DES CBC加密
	 * 
	 * @param secretKey
	 *            密钥
	 * @param secretIv
	 *            向量
	 * @param data
	 *            需要加密的数据
	 * @return
	 */
	public static byte[] encrypt3DES(String secretKey, String secretIv, byte[] data) {
		if (data == null || secretKey == null || secretIv == null) {
			return null;
		}
		try {
			// 根据给定的字节数组和算法构造一个密钥
			// SecretKey deskey = new SecretKeySpec(getKeyBytes(secretKey),
			// "desede");
			SecretKey deskey = getKeySpec(secretKey, "desede");
			// 加密
			Cipher cipher = Cipher.getInstance("desede/CBC/PKCS7Padding");
			IvParameterSpec ips = new IvParameterSpec(getKeyIvBytes(secretIv));
			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
			byte[] cryptData = cipher.doFinal(data);
			return cryptData;
		} catch (Exception ex) {
			throw new RuntimeException("3DES CBC加密过程出现错误！", ex);
		}
	}

	/**
	 * 3DES ECB 加密
	 *
	 * @param secretKey
	 *            密钥
	 * @param data
	 *            需要加密的字节流
	 * @return
	 */
	public static byte[] encrypt3DESECB(byte[] secretKey, byte[] data) {
		if (data == null || secretKey == null) {
			return null;
		}
		try {
			DESedeKeySpec spec = new DESedeKeySpec(secretKey);
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			Key deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/ECB/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			byte[] cryptData = cipher.doFinal(data);
			return cryptData;
		} catch (Exception ex) {
			throw new RuntimeException("3DES ECB加密过程出现错误！", ex);
		}
	}

	/**
	 * 3DES ECB 加密
	 *
	 * @param secretKey
	 *            密钥
	 * @param data
	 *            需要加密的字节流
	 * @return
	 */
	public static byte[] encrypt3DESECB(String secretKey, byte[] data) {
		if (data == null || secretKey == null) {
			return null;
		}
		try {
			DESedeKeySpec spec = new DESedeKeySpec(getKeyBytes(secretKey));
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			Key deskey = keyfactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance("desede/ECB/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			byte[] cryptData = cipher.doFinal(data);
			return cryptData;
		} catch (Exception ex) {
			throw new RuntimeException("3DES ECB加密过程出现错误！", ex);
		}
	}

	/**
	 * 获取24位密锁
	 * 
	 * @param strKey
	 * @return
	 */
	public static byte[] getKeyBytes(String strKey) {
		if (null == strKey || strKey.length() < 1) {
			throw new RuntimeException("key is null or empty!");
		}
		java.security.MessageDigest alg = null;
		try {
			alg = java.security.MessageDigest.getInstance("MD5");
			alg.update(strKey.getBytes());
			byte[] bkey = alg.digest();
			int start = bkey.length;
			byte[] bkey24 = new byte[24];
			for (int i = 0; i < start; i++) {
				bkey24[i] = bkey[i];
			}
			for (int i = start; i < 24; i++) {
				bkey24[i] = bkey[i - start];
			}
			return bkey24;
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 获取8位密锁向量
	 * 
	 * @param strKey
	 * @return
	 */
	public static byte[] getKeyIvBytes(String strKey) {
		if (null == strKey || strKey.length() < 1) {
			throw new RuntimeException("key is null or empty!");
		}
		java.security.MessageDigest alg = null;
		try {
			alg = java.security.MessageDigest.getInstance("MD5");
			alg.update(strKey.getBytes());
			byte[] bkey = alg.digest();
			int start = bkey.length;
			byte[] bkey8 = new byte[8];
			for (int i = 0; i < start && i < 8; i++) {
				bkey8[i] = bkey[i];
			}
			for (int i = start; i < 8; i++) {
				bkey8[i] = bkey[i - start];
			}
			return bkey8;
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Generates a SecretKeySpec for given password
	 *
	 * @param password
	 * @param algorithm
	 * @return SecretKeySpec
	 */
	public static SecretKeySpec getKeySpec(String password, String algorithm) throws IOException {
		// You can change it to 128 if you wish
		int keyLength = 256;
		byte[] keyBytes = new byte[keyLength / 8];
		// explicitly fill with zeros
		Arrays.fill(keyBytes, (byte) 0x0);
		// if password is shorter then key length, it will be zero-padded
		// to key length
		byte[] passwordBytes = password.getBytes("UTF-8");
		int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
		System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
		SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);
		return key;
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static byte[] initkey() throws Exception {
		// 实例化密钥生成器
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM, "BC");
		// kg.init(64);
		// kg.init(128);
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	public static void main(String[] args) throws Exception {
		// byte[] key = DESUtils.initkey();
		// byte[] secretIv = DESUtils.initkey();
		byte[] secretKey = DESUtils.getKeyBytes("ce3dc86d5dee4f0088e8e130376b1991");
		byte[] secretIv = DESUtils.getKeyIvBytes("12345678");
		System.out.println("key:" + new String(secretKey));
		System.out.println("key length:" + secretKey.length);
		String data = "基础应用开发平台";
		byte[] encryptData = DESUtils.encrypt(data.getBytes(), secretKey);
		System.out.println("加密后：" + byte2hex(encryptData));
		encryptData = DESUtils.decrypt(encryptData, secretKey);
		System.out.println("解密后：" + new String(encryptData));

		encryptData = DESUtils.encrypt3DES(data.getBytes(), secretKey, secretIv);
		System.out.println("->加密后：" + byte2hex(encryptData));
		encryptData = DESUtils.decrypt3DES(encryptData, secretKey, secretIv);
		System.out.println("->解密后：" + new String(encryptData));

		String key = com.glaf.core.util.UUID32.getUUID() + com.glaf.core.util.UUID32.getUUID()
				+ com.glaf.core.util.UUID32.getUUID() + com.glaf.core.util.UUID32.getUUID();
		String iv = com.glaf.core.util.UUID32.getUUID();

		encryptData = DESUtils.encode(key, iv, data.getBytes());
		System.out.println("@->加密后：" + byte2hex(encryptData));
		encryptData = DESUtils.decode(key, iv, encryptData);
		System.out.println("@->解密后：" + new String(encryptData));

		data = "DA363AB2CBBF857AC3BC50A3038C19F6EE04524FD2AEBF2FEA7A4723B88CE6D07EEED61FB80477113E06B12296C3208CE158B75BD92503E29EE6FF515AB81CAB48739A77EE252C36FAF4D33DD5B2CAB1F4DD195C607EF62ACC24B62BE89425864481072A3E6001880A333A638FE55ACD045C091AB18A37872EBD6C96EF8B8807C414E0554362278F7698E011604B930D883D120F96C0955EA5C34720147D0BF1FACED2A236AD54336DC10842443C1565D8493E426B2A2C50";
		byte[] data2 = DESUtils.hex2byte(data.getBytes());
		byte[] binaryData = DESUtils.decrypt3DES(data2, SecurityUtils.getKeyBytes("ce3dc86d5dee4f0088e8e130376b1991"),
				SecurityUtils.getKeyIvBytes("12345678"));
		String message = new String(binaryData);
		System.out.println("3DES解密后的消息:" + message);

	}

}
