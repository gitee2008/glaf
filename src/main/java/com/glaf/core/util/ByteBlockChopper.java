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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ByteBlockChopper {

	private static final Log log = LogFactory.getLog(ByteBlockChopper.class);

	public static List<byte[]> chopItUp(byte[] byteArray, int blockSize) {
		List<byte[]> bytes = null;
		if (byteArray != null) {
			int byteCount = byteArray.length;
			if (byteCount > blockSize) {
				log.debug("chopping " + byteCount + " bytes");
				bytes = new ArrayList<byte[]>();
				int offset;
				for (offset = 0; byteCount - offset > blockSize; offset += blockSize) {
					bytes.add(subArray(byteArray, offset, blockSize));
				}
				bytes.add(subArray(byteArray, offset, byteCount - offset));
			} else if (byteCount > 0) {
				log.debug("no need to chop " + byteCount + " bytes");
				bytes = Collections.singletonList(byteArray);
			}
		}
		return bytes;
	}

	public static byte[] glueChopsBackTogether(List<byte[]> byteBlocks, int blockSize) {
		byte[] byteArray = null;
		if (byteBlocks != null) {
			int blockCount = byteBlocks.size();
			switch (blockCount) {
			case 0:
				break;
			case 1:
				byteArray = (byte[]) byteBlocks.get(0);
				log.debug("no need to glue " + byteArray.length + " bytes");
				break;
			default:
				byte[] lastBlock = (byte[]) byteBlocks.get(blockCount - 1);
				int byteCount = blockSize * (blockCount - 1) + lastBlock.length;
				log.debug("gluing " + byteCount + " bytes");

				byteArray = new byte[byteCount];
				int offset = 0;
				for (int i = 0, n = blockCount; i < n; i++) {
					byte[] block = (byte[]) byteBlocks.get(i);
					int length = block.length;
					System.arraycopy(block, 0, byteArray, offset, length);
					log.debug("glued " + length + " bytes beggining at " + offset);
					if (length < blockSize && i < n - 1) {
						Arrays.fill(byteArray, offset + length, offset + blockSize, (byte) 0);
						log.debug("zero filled " + (blockSize - length) + " trailing bytes");
						offset += blockSize;
					} else {
						offset += length;
					}
				}
			}
		}
		return byteArray;
	}

	private static byte[] subArray(byte[] array, int offset, int length) {
		byte[] subArray = new byte[length];
		System.arraycopy(array, offset, subArray, 0, length);
		log.debug("chopped " + length + " bytes beggining at " + offset);
		return subArray;
	}

	private ByteBlockChopper() {

	}
}
