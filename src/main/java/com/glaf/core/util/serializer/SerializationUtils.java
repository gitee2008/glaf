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

package com.glaf.core.util.serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 对象序列化工具包
 */
public class SerializationUtils {

	private final static Logger log = LoggerFactory.getLogger(SerializationUtils.class);
	private static Serializer g_ser;

	static {
		String ser = SerializerFactory.getSerializer();
		if (ser == null || "".equals(ser.trim()))
			g_ser = new JavaSerializer();
		else {
			if (ser.equals("java")) {
				g_ser = new JavaSerializer();
			} else if (ser.equals("fst")) {
				g_ser = new FSTSerializer();
			} else if (ser.equals("kryo")) {
				g_ser = new KryoSerializer();
			} else if (ser.equals("kryo_pool_ser")) {
				g_ser = new KryoPoolSerializer();
			} else if (ser.equals("fst_snappy")) {
				g_ser = new FstSnappySerializer();
			} else {
				try {
					g_ser = (Serializer) Class.forName(ser).newInstance();
				} catch (Exception e) {
					throw new RuntimeException("Cannot initialize Serializer named [" + ser + ']', e);
				}
			}
		}
		log.info("Using Serializer -> [" + g_ser.name() + ":" + g_ser.getClass().getName() + ']');
	}

	public static Object deserialize(byte[] bytes) throws IOException {
		return g_ser.deserialize(bytes);
	}

	public static byte[] serialize(Object obj) throws IOException {
		return g_ser.serialize(obj);
	}

}
