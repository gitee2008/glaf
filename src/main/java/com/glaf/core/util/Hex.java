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

import java.lang.reflect.Constructor;

public class Hex {

	private static final Constructor<String> stringConstructor = getProtectedConstructor(String.class, int.class,
			int.class, char[].class);
	private final static byte[] charToByte = new byte[256];

	// package protected for use by ByteBufferUtil. Do not modify this array !!
	static final char[] byteToChar = new char[16];
	static {
		for (char c = 0; c < charToByte.length; ++c) {
			if (c >= '0' && c <= '9')
				charToByte[c] = (byte) (c - '0');
			else if (c >= 'A' && c <= 'F')
				charToByte[c] = (byte) (c - 'A' + 10);
			else if (c >= 'a' && c <= 'f')
				charToByte[c] = (byte) (c - 'a' + 10);
			else
				charToByte[c] = (byte) -1;
		}

		for (int i = 0; i < 16; ++i) {
			byteToChar[i] = Integer.toHexString(i).charAt(0);
		}
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buff
	 * @return
	 */
	public static String byte2hex(byte buff[]) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buff.length; i++) {
			String hex = Integer.toHexString(buff[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public static String bytesToHex(byte... bytes) {
		char[] c = new char[bytes.length * 2];
		for (int i = 0, len = bytes.length; i < len; i++) {
			int bint = bytes[i];
			c[i * 2] = byteToChar[(bint & 0xf0) >> 4];
			c[1 + i * 2] = byteToChar[bint & 0x0f];
		}

		return wrapCharArray(c);
	}

	/**
	 * Used to get access to protected/private constructor of the specified
	 * class
	 * 
	 * @param klass
	 *            - name of the class
	 * @param paramTypes
	 *            - types of the constructor parameters
	 * @return Constructor if successful, null if the constructor cannot be
	 *         accessed
	 */
	public static Constructor<String> getProtectedConstructor(Class<String> klass, Class<?>... paramTypes) {
		Constructor<String> c;
		try {
			c = klass.getDeclaredConstructor(paramTypes);
			c.setAccessible(true);
			return c;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hex2byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static byte[] hexToBytes(String str) {
		if (Math.abs(str.length()) % 2 == 1) {
			str = "0" + str;
		}
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0, len = bytes.length; i < len; i++) {
			byte halfByte1 = charToByte[str.charAt(i * 2)];
			byte halfByte2 = charToByte[str.charAt(i * 2 + 1)];
			if (halfByte1 == -1 || halfByte2 == -1)
				throw new NumberFormatException("Non-hex characters in " + str);
			bytes[i] = (byte) ((halfByte1 << 4) | halfByte2);
		}
		return bytes;
	}

	/**
	 * Create a String from a char array with zero-copy (if available), using
	 * reflection to access a package-protected constructor of String.
	 */
	public static String wrapCharArray(char[] c) {
		if (c == null)
			return null;

		String s = null;

		if (stringConstructor != null) {
			try {
				s = stringConstructor.newInstance(0, c.length, c);
			} catch (Exception e) {
				// Swallowing as we'll just use a copying constructor
			}
		}
		return s == null ? new String(c) : s;
	}

	public static final byte[] decodeHex(String hex) {
		char[] chars = hex.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			int newByte = 0x00;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = (byte) newByte;
			byteCount++;
		}
		return bytes;
	}

	public static final String encodeHex(byte[] bytes) {
		StringBuilder buff = new StringBuilder(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buff.append("0");
			}
			buff.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buff.toString();
	}

	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case '0':
			return 0x00;
		case '1':
			return 0x01;
		case '2':
			return 0x02;
		case '3':
			return 0x03;
		case '4':
			return 0x04;
		case '5':
			return 0x05;
		case '6':
			return 0x06;
		case '7':
			return 0x07;
		case '8':
			return 0x08;
		case '9':
			return 0x09;
		case 'a':
			return 0x0A;
		case 'b':
			return 0x0B;
		case 'c':
			return 0x0C;
		case 'd':
			return 0x0D;
		case 'e':
			return 0x0E;
		case 'f':
			return 0x0F;
		}
		return 0x00;
	}
}