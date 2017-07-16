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

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.glaf.core.config.SystemConfig;
import com.glaf.core.util.Hex;

public class DefaultHexEncryptor implements Encryptor {

	private PBEKeySpec pbeKeySpec;

	private PBEParameterSpec pbeParamSpec;

	private SecretKeyFactory keyFac;

	private SecretKey pbeKey;

	private byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee,
			(byte) 0x99 };

	private int count = 20;

	static {
		try {
			String provider = "org.bouncycastle.jce.provider.BouncyCastleProvider";
			java.security.Security.addProvider((Provider) Class.forName(provider).newInstance());
		} catch (Exception ex) {
			
		}
	}

	public DefaultHexEncryptor() throws NoSuchAlgorithmException, InvalidKeySpecException {
		pbeParamSpec = new PBEParameterSpec(salt, count);
		pbeKeySpec = new PBEKeySpec("saagar".toCharArray());
		keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		pbeKey = keyFac.generateSecret(pbeKeySpec);
	}

	private byte[] crypt(int cipherMode, byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(cipherMode, pbeKey, pbeParamSpec);
		byte[] cryptext = pbeCipher.doFinal(bytes);
		return cryptext;
	}

	public byte[] decrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		return crypt(Cipher.DECRYPT_MODE, bytes);
	}

	public String decrypt(String str) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] decrypted = Hex.hex2byte(str);
		byte[] decs = crypt(Cipher.DECRYPT_MODE, decrypted);
		String decryptedText;
		try {
			decryptedText = new String(decs, SystemConfig.getDefaultEncoding());
			return decryptedText;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public byte[] encrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		return crypt(Cipher.ENCRYPT_MODE, bytes);
	}

	public String encrypt(String str) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] encryptedBytes = null;
		try {
			encryptedBytes = crypt(Cipher.ENCRYPT_MODE, str.getBytes(SystemConfig.getDefaultEncoding()));
			String encryptedText = Hex.byte2hex(encryptedBytes);
			return encryptedText;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}