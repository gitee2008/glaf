/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.core.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.glaf.core.util.StringTools;
import com.glaf.core.util.UUID32;
import com.glaf.core.util.security.DESUtils;

public class SecurityUtils {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

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
	 *            待解密16进制字符串
	 * @return 解密后的字符串
	 */
	public static String decode(String key, String data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES", "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES, "BC");
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return new String(cipher.doFinal(hex2byte(data.getBytes())));
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	/**
	 * DES算法，加密
	 * 
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @param data
	 *            待加密字符串
	 * @return 加密后的字节流转16进制字符串
	 */
	public static String encode(String key, String data) {
		if (key == null || data == null) {
			return null;
		}
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES", "BC");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES, "BC");
			IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data.getBytes());
			return byte2hex(bytes);
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	/**
	 * 用发送发方公钥加密前面用的加密用对称密钥,形成数字信封
	 * 
	 * @param ctx
	 *            上下文环境
	 * @param symmetryKey
	 *            对称密钥
	 * @param pubKey
	 *            公钥
	 * @return String(经base64编码)
	 */
	public static String generateDigitalEnvelope(SecurityContext ctx, Key symmetryKey, byte[] pubKey) {
		String result = null;
		InputStream inputStream = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			inputStream = new ByteArrayInputStream(pubKey);
			java.security.cert.Certificate cert = cf.generateCertificate(inputStream);
			inputStream.close();
			PublicKey publicKey = cert.getPublicKey();
			Cipher cipher = Cipher.getInstance(ctx.getAsymmetryAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			result = Base64.encodeBase64String(cipher.doFinal(symmetryKey.getEncoded()));
			return result;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 生成对称加密用密钥
	 * 
	 * @param ctx
	 *            上下文环境
	 * @return key
	 */
	public static Key generateSecretKey(SecurityContext ctx) {
		try {
			KeyGenerator skg = KeyGenerator.getInstance(ctx.getSymmetryKeyAlgorithm(), ctx.getJceProvider());
			SecureRandom secureRandom = SecureRandom.getInstance(ctx.getSecureRandomAlgorithm());
			skg.init(ctx.getSymmetryKeySize(), secureRandom);
			SecretKey key = skg.generateKey();
			return key;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	public static String genKey() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < 31; i++) {
			buffer.append(UUID32.getUUID());
		}
		return buffer.toString();
	}

	public static String genKey2048() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < 64; i++) {
			buffer.append(UUID32.getUUID());
		}
		return buffer.toString();
	}

	/**
	 * 从客户端的keystore得到证书
	 * 
	 * @return X509Certificate 证书
	 */
	public static X509Certificate getCertFromKeystore(InputStream keystoreInputStream, String alias, String password) {
		try {
			X509Certificate x509cert = null;
			KeyStore ks = KeyStore.getInstance("JKS", "BC");
			ks.load(keystoreInputStream, password.toCharArray());
			x509cert = (X509Certificate) ks.getCertificate(alias);
			return x509cert;
		} catch (Exception ex) {
			throw new SecurityException(ex);
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
	 * 从客户端的keystore得到私钥
	 * 
	 * @return key 私钥
	 */
	public static Key getPrivateKeyFromKeystore(InputStream ksInputStream, String password, String alias) {
		try {
			KeyStore ks = KeyStore.getInstance("JKS", "BC");
			ks.load(ksInputStream, password.toCharArray());
			Key privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
			return privateKey;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	public static String hash(String plaintext) {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA"); // SHA-1 generator instance
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}

		try {
			md.update(plaintext.getBytes("UTF-8")); // Message summary
			// generation
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}

		byte raw[] = md.digest(); // Message summary reception
		try {
			String hash = new String(org.apache.commons.codec.binary.Base64.encodeBase64(raw), "UTF-8");
			return hash;
		} catch (UnsupportedEncodingException use) {
			throw new RuntimeException(use);
		}
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException();
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 接收方用自己的私钥解开数字信封，得到对称密钥
	 * 
	 * @param ctx
	 *            上下文环境
	 * @param envelope
	 *            数字信封
	 * @param privateKey
	 *            私钥
	 * @return key 对称密钥
	 */
	public static Key openDigitalEnvelope(SecurityContext ctx, String envelope, Key privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(ctx.getAsymmetryAlgorithm(), ctx.getJceProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			envelope = StringTools.replaceIgnoreCase(envelope, " ", "");
			byte[] key = cipher.doFinal(Base64.decodeBase64(envelope));
			SecretKeyFactory skf = SecretKeyFactory.getInstance(ctx.getSymmetryKeyAlgorithm(), ctx.getJceProvider());
			DESKeySpec keySpec = new DESKeySpec(key);
			Key symmetryKey = skf.generateSecret(keySpec);
			return symmetryKey;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	/**
	 * 用私钥对待签名内容进行签名，形成签名流。
	 * 
	 * @param ctx
	 *            上下文环境
	 * @param content
	 *            待签名内容
	 * @param privateKey
	 *            私钥
	 * @return byte[] 签名流
	 */
	public static byte[] sign(SecurityContext ctx, byte[] content, Key privateKey) {
		try {
			Signature sign = Signature.getInstance(ctx.getSignatureAlgorithm(), ctx.getJceProvider());
			PrivateKey pk = (PrivateKey) privateKey;
			sign.initSign(pk);
			sign.update(content);
			byte[] signed = sign.sign();
			return signed;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	/**
	 * 进行对称解密
	 * 
	 * @param ctx
	 *            上下文环境
	 * @param cipherContent
	 *            待解密密文。
	 * @param key
	 *            密钥
	 * @return byte[] 解密后明文
	 * 
	 */
	public static byte[] symmetryDecrypt(SecurityContext ctx, byte[] cipherContent, Key key) {
		try {
			byte[] tContent = null;
			Cipher cipher = Cipher.getInstance(ctx.getSymmetryAlgorithm(), ctx.getJceProvider());
			SecureRandom secureRandom = SecureRandom.getInstance(ctx.getSecureRandomAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, key, secureRandom);
			tContent = cipher.doFinal(cipherContent);
			return tContent;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	/**
	 * 进行对称加密
	 * 
	 * @param ctx
	 *            上下文环境
	 * @param content
	 *            待加密明文。
	 * @param key
	 *            加密密钥
	 * @return byte[] 加密后密文
	 */
	public static byte[] symmetryEncrypt(SecurityContext ctx, byte[] content, Key key) {
		try {
			byte[] cipherContent = null;
			Cipher cipher = Cipher.getInstance(ctx.getSymmetryAlgorithm(), ctx.getJceProvider());
			SecureRandom secureRandom = SecureRandom.getInstance(ctx.getSecureRandomAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, key, secureRandom);
			cipherContent = cipher.doFinal(content);
			return cipherContent;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	/**
	 * 公钥验证签名
	 * 
	 * @param ctx
	 *            上下文环境
	 * @param source
	 *            原文
	 * @param signed
	 *            签名信息
	 * @param pubKey
	 *            公钥
	 * @return boolean
	 */
	public static boolean verify(SecurityContext ctx, byte[] source, byte[] signed, PublicKey publicKey) {
		try {
			boolean verify = false;
			Signature sign = Signature.getInstance(ctx.getSignatureAlgorithm(), ctx.getJceProvider());
			sign.initVerify(publicKey);
			sign.update(source);
			verify = sign.verify(signed);
			return verify;
		} catch (Exception ex) {
			throw new SecurityException(ex);
		}
	}

	private SecurityUtils() {

	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		long times = System.currentTimeMillis() - start;
		System.out.println("总共耗时(毫秒):" + times);
		System.out.println(SecurityUtils.hash("12345678"));
		System.out.println(SecurityUtils.hash("111111"));
		System.out.println(SecurityUtils.genKey2048());
		byte[] secretKey = SecurityUtils.getKeyBytes(SecurityUtils.genKey2048());
		byte[] secretIv = SecurityUtils.getKeyIvBytes(UUID32.getUUID());
		byte[] text = "glaf基础开发框架".getBytes();
		System.out.println("secretKey length:" + secretKey.length);
		System.out.println("secretIv length:" + secretIv.length);
		byte[] binaryData = DESUtils.ecrypt3DES(text, secretKey, secretIv);
		System.out.println("加密后:" + Base64.encodeBase64String(binaryData));
		byte[] str2 = DESUtils.decrypt3DES(binaryData, secretKey, secretIv);
		System.out.println("解密后:" + new String(str2));

		System.out.println("1)解密密码:" + SecurityUtils.decode(
				"bda6dee69b8f4246b01a4778146aeace4e01d5168ebe484e912a8dd5e5d7b7332ea1ee14c99b4fceb3f0260d2abaf0fe7434261dd56f44258ed7ad7b314c68c513a1a96ab2dd42b6b1b90d588cce12a40e7c578ec65f4986890a2afbe2c5adadcb146cdf58194c298aff2b954b93d74e529f7067ceaf4fb6b0af17e0e9d329b5a07a02d3175148c0b245b33f5b145924b3915ba584e746f6a249f7455d6a281db0922d1162f74021b5fb932d87d218ac620ced32198a450c814543e62f9aebcad51e1f7ab43d4b02ac15945e84cac35289feda2acbf04e43a169852f56c3aad8ed0d78ab8efc49d3847f1b4789a0742c22460ae5c4a44ebca569670c08d5bfca5456175003a84173ab38d0476bbb12910d28e4fb1de94a47936c90d5c0b0db5723e1226a18f94796aa85361bb26175f85c8290cada994027b6ff3d27f4b5ac25ba55ceeccec64cef8928abaf2a5450c3e836dbb43cde44cdaed8aec8754f2b50290dd27c59804c569d008512650e731c3190ed1fb0e8415c88f58917d04715e81f647a5146834067b0807286575d2590330a924a527548d1a001092fb87f0562cf2d39f0312841c59675c16432bd8a9ad8179f5ad89144a5ae928ab6cc2800ff19bb994e820842369e8f3e6b34ba5ec653d3d587740e42fab1aa210f0656e195ef036da6c3c84bd8817cf19f266a09b6",
				"5180E2FD182406ACF6609CEAAE40A3897B80D57A84FAB8C584C95DB6595465BA3858543E8962E381"));
		System.out.println("2)解密密码:" + SecurityUtils.decode(
				"eaf2bc88a8fa45fe84ce0c8d67c8cc4f657a1f3ffdfe405dab3121afac935839c26ec3cdf8314545af0c92d2df36e21e0d0e0997631443d792da3d416acb6cdc16c639f1048046319619238c01d385ec68bc7767edf64955b13e93419557c813b88935a473ff4448a59239f87cb97601102a2e91a18441429d67be2128b3322a36d67f2b9e94427da5e46ecc392fca08a6d93aa3e3974ac4a8ed46dd9fd1eceb08b83f36f51f43d4a705dfb0a84918c802bc15c9f617497eada07ed6e42fe15d8f2748a7ad9d434ea3b8f6f27e9a137c30252520845c4ddfb96ebe7f86e1c767f9fd29ee6691413d8658b69759ba2d2dda8d4f591a8f4a11be8015c5839e6e03855d6cfb79a24305b1f8bd90fbd9d082f2a2bccbd6754a2b8a6263de1e9b7cca993875c299bd461fbb5edbb86f49076dfce138f624d44cfca41d8479498582303385b183001d482caf93cb8c404f9fc6970f3115a55248fd8b09bee29800c0a04c5fceb39f984911861c9d332623aff3123151f9cc774df8b0496ac1bc6d3aabf06aa73a79fe4708aaa76934c414c4bb39b31ddc064e4ae688d461484bc1fa457ccbe7a53d2d472da590e8039b33361f23eb06e8151c40f387073495c84c3bc8e280bf06a2674f7cb902f4cd8b6a492f4cb620bdc9e0482ba9fdd0eadb14f32d9d0230a3744e43dabc88d3345f3ae9df",
				"381CAB94320B4E22"));
	}

}