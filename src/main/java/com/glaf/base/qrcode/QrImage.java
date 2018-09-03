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

package com.glaf.base.qrcode;

/**
 * 二维码图片对象
 *
 */
public class QrImage {

	/**
	 * 二维码的内容
	 */
	private String qrContent;

	/**
	 * 二维码的宽度
	 */
	private int qrWidth;

	/**
	 * 二维码的高度
	 */
	private int qrHeight;

	/**
	 * 二维码中间图标的文件路径
	 */
	private String qrIconFilePath;

	/**
	 * 二维码中间小图标的边长
	 */
	private int qrIconWidth;

	/**
	 * 顶部文字的高度
	 */
	private int topWrodHeight;

	/**
	 * 文字的大小
	 */
	private int wordSize;

	/**
	 * 文字的内容
	 */
	private String wordContent;

	/**
	 * 文件的输出路径
	 */
	private String fileOutputPath;

	public static class Builder {
		private String qrContent;
		private int qrWidth;
		private int qrHeight;
		private String qrIconFilePath;
		private int topWrodHeight;
		private int wordSize;
		private String wordContent;
		private String fileOutputPath;
		private int qrIconWidth;

		public Builder() {
		}

		public Builder setQrContent(String qrContent) {
			this.qrContent = qrContent;
			return this;
		}

		public Builder setQrWidth(int qrWidth) {
			this.qrWidth = qrWidth;
			return this;
		}

		public Builder setQrHeight(int qrHeight) {
			this.qrHeight = qrHeight;
			return this;
		}

		public Builder setQrIconFilePath(String qrIconFilePath) {
			this.qrIconFilePath = qrIconFilePath;
			return this;
		}

		public Builder setTopWrodHeight(int topWrodHeight) {
			this.topWrodHeight = topWrodHeight;
			return this;
		}

		public Builder setWordSize(int wordSize) {
			this.wordSize = wordSize;
			return this;
		}

		public Builder setWordContent(String wordContent) {
			this.wordContent = wordContent;
			return this;
		}

		public Builder setFileOutputPath(String fileOutputPath) {
			this.fileOutputPath = fileOutputPath;
			return this;
		}

		public Builder setQrIconWidth(int qrIconWidth) {
			this.qrIconWidth = qrIconWidth;
			return this;
		}

		public QrImage build() {
			return new QrImage(this.qrContent, this.qrWidth, this.qrHeight, this.qrIconFilePath, this.qrIconWidth,
					this.topWrodHeight, this.wordSize, this.wordContent, this.fileOutputPath);
		}
	}

	public QrImage(String qrContent, int qrWidth, int qrHeight, String qrIconFilePath, int qrIconWidth,
			int topWrodHeight, int wordSize, String wordContent, String fileOutputPath) {
		super();
		this.qrContent = qrContent;
		this.qrWidth = qrWidth;
		this.qrHeight = qrHeight;
		this.qrIconFilePath = qrIconFilePath;
		this.qrIconWidth = qrIconWidth;
		this.topWrodHeight = topWrodHeight;
		this.wordSize = wordSize;
		this.wordContent = wordContent;
		this.fileOutputPath = fileOutputPath;
	}

	public String getQrContent() {
		return qrContent;
	}

	public int getQrWidth() {
		return qrWidth;
	}

	public int getQrHeight() {
		return qrHeight;
	}

	public String getQrIconFilePath() {
		return qrIconFilePath;
	}

	public int getTopWrodHeight() {
		return topWrodHeight;
	}

	public int getWordSize() {
		return wordSize;
	}

	public String getWordContent() {
		return wordContent;
	}

	public String getFileOutputPath() {
		return fileOutputPath;
	}

	public int getQrIconWidth() {
		return qrIconWidth;
	}
}
