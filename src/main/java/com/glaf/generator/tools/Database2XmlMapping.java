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

package com.glaf.generator.tools;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.Dom4jUtils;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.StringTools;
import com.glaf.generator.xml.XmlWriter;

public class Database2XmlMapping {

	public final static String newline = System.getProperty("line.separator");

	public static void main(String[] args) {
		Database2XmlMapping gen = new Database2XmlMapping();
		if (args != null && args.length > 0) {
			gen.setTodir(args[0]);
		} else {
			gen.setTodir("codegen/mapping");
		}
		gen.execute();
		System.out.println("code gen " + gen.getTodir() + "");
		System.exit(0);
	}

	public String todir;

	public String systemName;

	public void execute() {
		try {
			List<String> tables = DBUtils.getTables();
			for (String tableName : tables) {
				System.out.println("process " + tableName);
				List<ColumnDefinition> fields = DBUtils
						.getColumnDefinitions(tableName);

				TableDefinition tableDefinition = new TableDefinition();
				tableDefinition.setTableName(tableName.toLowerCase());
				tableDefinition.setTitle(StringTools.upper(StringTools
						.camelStyle(tableName)));
				tableDefinition.setEntityName(StringTools.upper(StringTools
						.camelStyle(tableName)));
				tableDefinition
						.setEnglishTitle(tableDefinition.getEntityName());
				// tableDefinition.setPackageName("com.glaf.apps."
				// + StringTools.camelStyle(tableName));

				tableDefinition.setPackageName("com.glaf.apps");

				List<String> primaryKeys = DBUtils.getPrimaryKeys(tableName);

				for (ColumnDefinition f : fields) {
					System.out.println(tableName+"--"+f.getName());
					f.setTitle(f.getName());
					if (!primaryKeys.isEmpty()) {
						if (primaryKeys.contains(f.getColumnName()) || primaryKeys.contains(f.getColumnName().toLowerCase()) || primaryKeys.contains(f.getColumnName().toUpperCase())) {
							tableDefinition.setIdField(f);
						} else {
							f.setEditable(true);
							tableDefinition.addField(f);
						}
					} else {
						if (StringUtils.equalsIgnoreCase(f.getColumnName(),
								"id")) {
							tableDefinition.setIdField(f);
						} else {
							f.setEditable(true);
							tableDefinition.addField(f);
						}
					}
				}

				OutputFormat format = OutputFormat.createPrettyPrint();

				format.setPadText(true);
				format.setNewlines(true);
				format.setIndentSize(4);
				format.setEncoding("UTF-8");
				format.setLineSeparator(newline);
				format.setNewLineAfterDeclaration(true);
				format.setSuppressDeclaration(true);

				String filename = tableDefinition.getEntityName()+ ".mapping.xml";

				String toFile = todir + "/" + filename;

				XmlWriter xmlWriter = new XmlWriter();
				Document d = xmlWriter.write(tableDefinition);
				byte[] bytes = Dom4jUtils.getBytesFromDocument(d, format);
				FileUtils.save(toFile, bytes);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getSystemName() {
		return systemName;
	}

	public String getTodir() {
		return todir;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public void setTodir(String todir) {
		this.todir = todir;
	}

}
