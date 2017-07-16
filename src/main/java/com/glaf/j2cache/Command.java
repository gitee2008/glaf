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

package com.glaf.j2cache;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glaf.core.util.SerializationUtils;

/**
 * 命令消息封装 格式： 第1个字节为命令代码，长度1 [OPT] 第2、3个字节为region长度，长度2 [R_LEN] 第4、N 为 region
 * 值，长度为 [R_LEN] 第N+1、N+2 为 key 长度，长度2 [K_LEN] 第N+3、M为 key值，长度为 [K_LEN]
 * 
 * @author oschina.net
 */
public class Command {

	private final static Logger log = LoggerFactory.getLogger(Command.class);

	private final static int SRC_ID = genRandomSrc(); // 命令源标识，随机生成

	public final static byte OPT_DELETE_KEY = 0x01; // 删除缓存
	public final static byte OPT_CLEAR_KEY = 0x02; // 清除缓存

	private static int bytes2int(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}
	private static int genRandomSrc() {
		long ct = System.currentTimeMillis();
		Random rnd_seed = new Random(ct);
		return (int) (rnd_seed.nextInt(10000) * 1000 + ct % 1000);
	}
	private static byte[] int2bytes(int i) {
		byte[] b = new byte[4];

		b[0] = (byte) (0xff & i);
		b[1] = (byte) ((0xff00 & i) >> 8);
		b[2] = (byte) ((0xff0000 & i) >> 16);
		b[3] = (byte) ((0xff000000 & i) >> 24);

		return b;
	}
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			Command cmd = new Command(OPT_DELETE_KEY, "users", "ld" + i);
			byte[] bufs = cmd.toBuffers();
			System.out.print(cmd.getSrc() + ":");
			for (byte b : bufs) {
				System.out.printf("[%s]", Integer.toHexString(b));
			}
			System.out.println();
			Command cmd2 = Command.parse(bufs);
			System.out.printf("%d -> %d:%s:%s(%s)\n", cmd2.getSrc(), cmd2.getOperator(), cmd2.getRegion(),
					cmd2.getKey(), cmd2.isLocalCommand());
		}
	}

	public static Command parse(byte[] buffers) {
		Command cmd = null;
		try {
			int idx = 4;
			byte opt = buffers[idx];
			int r_len = buffers[++idx] << 8;
			r_len += Math.abs(buffers[++idx]);
			if (r_len > 0) {
				String region = new String(buffers, ++idx, r_len);
				idx += r_len;
				int k_len = buffers[idx++] << 8;
				k_len += Math.abs(buffers[idx++]);
				if (k_len > 0) {
					// String key = new String(buffers, idx, k_len);
					byte[] keyBuffers = new byte[k_len];
					System.arraycopy(buffers, idx, keyBuffers, 0, k_len);
					Object key = SerializationUtils.unserialize(keyBuffers);
					cmd = new Command(opt, region, key);
					cmd.src = bytes2int(buffers);
				}
			}
		} catch (Exception e) {
			log.error("Unabled to parse received command.", e);
		}
		return cmd;
	}

	private int src;

	private byte operator;

	private String region;

	private Object key;

	public Command(byte o, String r, Object k) {
		this.operator = o;
		this.region = r;
		this.key = k;
		this.src = SRC_ID;
	}

	public Object getKey() {
		return key;
	}

	public byte getOperator() {
		return operator;
	}

	public String getRegion() {
		return region;
	}

	public int getSrc() {
		return src;
	}

	public boolean isLocalCommand() {
		return this.src == SRC_ID;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public void setOperator(byte operator) {
		this.operator = operator;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public byte[] toBuffers() {
		byte[] keyBuffers = null;
		try {
			keyBuffers = SerializationUtils.serialize(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		int r_len = region.getBytes().length;
		int k_len = keyBuffers.length;

		byte[] buffers = new byte[9 + r_len + k_len];
		System.arraycopy(int2bytes(this.src), 0, buffers, 0, 4);
		int idx = 4;
		buffers[idx] = operator;
		buffers[++idx] = (byte) (r_len >> 8);
		buffers[++idx] = (byte) (r_len & 0xFF);
		System.arraycopy(region.getBytes(), 0, buffers, ++idx, r_len);
		idx += r_len;
		buffers[idx++] = (byte) (k_len >> 8);
		buffers[idx++] = (byte) (k_len & 0xFF);
		System.arraycopy(keyBuffers, 0, buffers, idx, k_len);
		return buffers;
	}
}