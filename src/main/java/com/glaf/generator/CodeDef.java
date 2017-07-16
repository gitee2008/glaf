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

package com.glaf.generator;

import java.util.*;

public class CodeDef {

	private String name;

	private String template;

	private String encoding;

	private String saveName;

	private String savePath;

	private String processor;

	private String content;

	private String expression;

	private Map<String, String> properties = new HashMap<String, String>();

	public CodeDef() {

	}

	public void addProperty(String name, String value) {
		properties.put(name, value);
	}

	public String getContent() {
		return content;
	}

	public String getEncoding() {
		return encoding;
	}

	public String getExpression() {
		return expression;
	}

	public String getName() {
		return name;
	}

	public String getProcessor() {
		return processor;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String getSaveName() {
		return saveName;
	}

	public String getSavePath() {
		return savePath;
	}

	public String getTemplate() {
		return template;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
