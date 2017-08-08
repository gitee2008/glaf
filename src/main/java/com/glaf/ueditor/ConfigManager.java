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

package com.glaf.ueditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.*;

import com.glaf.core.config.SystemProperties;
import com.glaf.ueditor.define.ActionMap;

/**
 * 配置管理器
 * 
 *
 */
public final class ConfigManager {
	protected static ConcurrentMap<String, String> jsonProperties = new ConcurrentHashMap<String, String>();
	// 涂鸦上传filename定义
	private final static String SCRAWL_FILE_NAME = "scrawl";
	// 远程图片抓取filename定义
	private final static String REMOTE_FILE_NAME = "remote";
	// 配置文件
	private static final String configFileName = "config.json";

	private JSONObject jsonConfig = null;
	private final String rootPath;

	/*
	 * 通过一个给定的路径构建一个配置管理器
	 */
	private ConfigManager(String rootPath, String contextPath, String uri) throws FileNotFoundException, IOException {
		rootPath = rootPath.replace("\\", "/");
		this.rootPath = rootPath;
		this.initEnv();
	}

	/**
	 * 配置管理器构造工厂
	 * 
	 * @param rootPath
	 *            服务器根路径
	 * @param contextPath
	 *            服务器所在项目路径
	 * @param uri
	 *            当前访问的uri
	 * @return 配置管理器实例或者null
	 */
	public static ConfigManager getInstance(String rootPath, String contextPath, String uri) {
		try {
			return new ConfigManager(rootPath, contextPath, uri);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// 验证配置文件加载是否正确
	public boolean valid() {
		return this.jsonConfig != null;
	}

	public JSONObject getAllConfig() {
		return this.jsonConfig;
	}

	public Map<String, Object> getConfig(int type) {

		Map<String, Object> conf = new HashMap<String, Object>();
		String savePath = null;

		switch (type) {

		case ActionMap.UPLOAD_FILE:
			conf.put("isBase64", "false");
			conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
			conf.put("allowFiles", this.getArray("fileAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("fileFieldName"));
			savePath = this.jsonConfig.getString("filePathFormat");
			break;

		case ActionMap.UPLOAD_IMAGE:
			conf.put("isBase64", "false");
			conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
			conf.put("allowFiles", this.getArray("imageAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
			savePath = this.jsonConfig.getString("imagePathFormat");
			break;

		case ActionMap.UPLOAD_VIDEO:
			conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
			conf.put("allowFiles", this.getArray("videoAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("videoFieldName"));
			savePath = this.jsonConfig.getString("videoPathFormat");
			break;

		case ActionMap.UPLOAD_SCRAWL:
			conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
			conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
			conf.put("fieldName", this.jsonConfig.getString("scrawlFieldName"));
			conf.put("isBase64", "true");
			savePath = this.jsonConfig.getString("scrawlPathFormat");
			break;

		case ActionMap.CATCH_IMAGE:
			conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
			conf.put("filter", this.getArray("catcherLocalDomain"));
			conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
			conf.put("allowFiles", this.getArray("catcherAllowFiles"));
			conf.put("fieldName", this.jsonConfig.getString("catcherFieldName") + "[]");
			savePath = this.jsonConfig.getString("catcherPathFormat");
			break;

		case ActionMap.LIST_IMAGE:
			conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
			conf.put("dir", this.jsonConfig.getString("imageManagerListPath"));
			conf.put("count", this.jsonConfig.getInteger("imageManagerListSize"));
			break;

		case ActionMap.LIST_FILE:
			conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
			conf.put("dir", this.jsonConfig.getString("fileManagerListPath"));
			conf.put("count", this.jsonConfig.getInteger("fileManagerListSize"));
			break;

		}

		conf.put("savePath", savePath);
		conf.put("rootPath", this.rootPath);

		return conf;

	}

	private void initEnv() throws FileNotFoundException, IOException {
		String content = jsonProperties.get(this.getConfigPath());
		if (content == null) {
			content = this.readFile(this.getConfigPath());
			if (content != null) {
				jsonProperties.putIfAbsent(this.getConfigPath(), content);
			}
		}
		try {
			JSONObject jsonConfig = JSON.parseObject(content);
			this.jsonConfig = jsonConfig;
		} catch (Exception ex) {
			ex.printStackTrace();
			this.jsonConfig = null;
		}
	}

	private String getConfigPath() {
		return SystemProperties.getConfigRootPath() + File.separator + "classes" + File.separator
				+ ConfigManager.configFileName;
	}

	private String[] getArray(String key) {
		JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
		String[] result = new String[jsonArray.size()];
		for (int i = 0, len = jsonArray.size(); i < len; i++) {
			result[i] = jsonArray.getString(i);
		}
		return result;
	}

	private String readFile(String path) throws IOException {
		StringBuilder builder = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
			BufferedReader bfReader = new BufferedReader(reader);
			String tmpContent = null;
			while ((tmpContent = bfReader.readLine()) != null) {
				builder.append(tmpContent);
			}
			bfReader.close();
		} catch (UnsupportedEncodingException e) {
		}
		return this.filter(builder.toString());
	}

	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private String filter(String input) {
		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
	}

}
