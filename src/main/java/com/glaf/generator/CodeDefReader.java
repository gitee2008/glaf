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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.glaf.generator.CodeDef;

public class CodeDefReader {

	public List<CodeDef> read(java.io.InputStream inputStream) {
		List<CodeDef> rows = new ArrayList<CodeDef>();
		SAXReader xmlReader = new SAXReader();
		try {
			Document doc = xmlReader.read(inputStream);
			Element root = doc.getRootElement();
			List<?> elements = root.elements("definition");
			if (elements != null) {
				Iterator<?> iterator = elements.iterator();
				while (iterator.hasNext()) {
					Element element = (Element) iterator.next();
					CodeDef def = new CodeDef();
					def.setName(element.attributeValue("name"));
					def.setEncoding(element.elementText("encoding"));
					def.setSaveName(element.elementText("saveName"));
					def.setSavePath(element.elementText("savePath"));
					def.setTemplate(element.elementText("template"));
					def.setProcessor(element.elementText("processor"));

					Element elem = element.element("properties");
					if (elem != null) {
						List<?> properties = elem.elements("property");
						if (properties != null && properties.size() > 0) {
							Iterator<?> iter = properties.iterator();
							while (iter.hasNext()) {
								Element em = (Element) iter.next();
								String propertyName = em.attributeValue("name");
								String propertyValue = null;
								if (StringUtils.isNotEmpty(em
										.attributeValue("value"))) {
									propertyValue = em.attributeValue("value");
								} else {
									propertyValue = em.getTextTrim();
								}
								def.addProperty(propertyName, propertyValue);
							}
						}
					}
					rows.add(def);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return rows;
	}
}
