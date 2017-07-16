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

package com.glaf.generator.xml;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.glaf.core.domain.ColumnDefinition;
import com.glaf.core.domain.TableDefinition;

public class XmlWriter {

	public Document write(TableDefinition tableDefinition) {
		List<TableDefinition> rows = new java.util.ArrayList<TableDefinition>();
		rows.add(tableDefinition);
		return this.write(rows);
	}

	public Document write(List<TableDefinition> rows) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("mapping");
		String entityName, moduleName, packageName, propName;
		String[] entityNameArr, propNameArr;
		for (TableDefinition tableDefinition : rows) {
			entityName = tableDefinition.getEntityName().toLowerCase();
			entityNameArr = entityName.split("_");
			if (entityNameArr.length > 1) {
				moduleName = entityNameArr[0];
			} else {
				moduleName = tableDefinition.getModuleName();
			}
			packageName = "com.glaf.apps." + moduleName;
			entityName = "";
			for (int i = 0; i < entityNameArr.length; i++) {
				if (entityNameArr[i].length() > 1)
					entityName += entityNameArr[i].substring(0, 1).toUpperCase()
							+ entityNameArr[i].substring(1, entityNameArr[i].length());
			}
			Element element = root.addElement("entity");
			element.addAttribute("name", entityName);
			element.addAttribute("package", packageName);
			element.addAttribute("moduleName", moduleName);
			element.addAttribute("table", tableDefinition.getTableName());
			element.addAttribute("title", tableDefinition.getTitle());
			element.addAttribute("englishTitle", entityName);
			ColumnDefinition idField = tableDefinition.getIdField();
			if (idField != null) {
				Element idElement = element.addElement("id");
				propName = idField.getColumnName().toLowerCase();
				propNameArr = propName.split("_");
				propName = "";
				for (int j = 0; j < propNameArr.length; j++) {
					if (j == 0) {
						propName = propNameArr[j];
					} else {
						propName += propNameArr[j].substring(0, 1).toUpperCase()
								+ propNameArr[j].substring(1, propNameArr[j].length());
					}
				}
				idElement.addAttribute("name", propName);
				idElement.addAttribute("column", idField.getColumnName());
				idElement.addAttribute("type", idField.getType());
				idElement.addAttribute("title", idField.getTitle());
				idElement.addAttribute("englishTitle", propName);
				if (idField.getLength() > 0) {
					idElement.addAttribute("length", String.valueOf(idField.getLength()));
				}
			}

			Map<String, ColumnDefinition> fields = tableDefinition.getFields();
			Set<Entry<String, ColumnDefinition>> entrySet = fields.entrySet();
			for (Entry<String, ColumnDefinition> entry : entrySet) {
				String name = entry.getKey();
				ColumnDefinition field = entry.getValue();
				if (idField != null && StringUtils.equalsIgnoreCase(idField.getColumnName(), field.getColumnName())) {
					continue;
				}
				Element elem = element.addElement("property");
				elem.addAttribute("name", name);
				elem.addAttribute("column", field.getColumnName());
				elem.addAttribute("type", field.getType());
				elem.addAttribute("title", field.getTitle());
				elem.addAttribute("englishTitle", field.getEnglishTitle());

				if (StringUtils.equals(field.getType(), "String") && field.getLength() > 0) {
					elem.addAttribute("length", String.valueOf(field.getLength()));
				}
				if (field.isUnique()) {
					elem.addAttribute("unique", String.valueOf(field.isUnique()));
				}
				if (field.isSearchable()) {
					elem.addAttribute("searchable", String.valueOf(field.isSearchable()));
				}
				if (!field.isNullable()) {
					elem.addAttribute("nullable", String.valueOf(field.isNullable()));
				}
				if (field.isEditable()) {
					elem.addAttribute("editable", String.valueOf(field.isEditable()));
				}
				if (field.getDisplayType() > 0) {
					elem.addAttribute("displayType", String.valueOf(field.getDisplayType()));
				}
			}
		}
		return doc;
	}
}
