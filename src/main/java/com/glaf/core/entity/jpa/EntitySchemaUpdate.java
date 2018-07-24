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

package com.glaf.core.entity.jpa;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.config.DBConfiguration;
import com.glaf.core.config.SystemProperties;
import com.glaf.core.entity.hibernate.HibernateBeanFactory;
import com.glaf.core.util.FileUtils;
import com.glaf.core.util.Resources;
import com.glaf.core.util.StringTools;

public class EntitySchemaUpdate {
	protected final static Log logger = LogFactory.getLog(EntitySchemaUpdate.class);
	private static final String PERSISTENCE_UNIT_NAME = "glaf";

	public void updateDDL() {
		Properties props = DBConfiguration.getDefaultDataSourceProperties();
		if (props != null) {
			boolean success = false;
			try {
				success = this.updateDDL(props);
				success = true;
			} catch (Exception ex) {
				success = false;
			}
			if (!success) {
				HibernateBeanFactory.reload();
			}
		}
	}

	public boolean updateDDL(java.util.Properties props) {
		boolean success = false;
		EntityManagerFactory factory = null;
		EntityManager em = null;
		java.io.InputStream input = null;
		try {
			input = Resources.getResourceAsStream("com/glaf/core/entity/jpa/persistence.xml");
			if (input != null) {
				String content = new String(FileUtils.getBytes(input));

				String provider = "org.hibernate.jpa.HibernatePersistenceProvider";
				if (StringUtils.equals("eclipselink", System.getProperty("jpa.provider"))) {
					provider = "org.eclipse.persistence.jpa.PersistenceProvider";
				}

				content = StringTools.replace(content, "#{provider}", provider);

				String url = props.getProperty("jdbc.url");
				url = StringTools.replace(url, "&", "&amp;");
				content = StringTools.replace(content, "#{jdbc.driver}", props.getProperty("jdbc.driver"));
				content = StringTools.replace(content, "#{jdbc.url}", url);
				content = StringTools.replace(content, "#{jdbc.user}", props.getProperty("jdbc.user"));
				content = StringTools.replace(content, "#{jdbc.password}", props.getProperty("jdbc.password"));

				String path = SystemProperties.getConfigRootPath() + "/classes/META-INF";
				FileUtils.mkdirs(path);
				String filename = path + "/persistence.xml";
				logger.info("persistence filename:" + filename);
				//logger.info("\n\n" + content);
				FileUtils.save(filename, content.getBytes());
				factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
				em = factory.createEntityManager();
				em.getTransaction().begin();
				em.getTransaction().commit();
				success = true;
			} else {
				logger.info("template persistence.xml not found.");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (em != null) {
				em.close();
			}
			if (factory != null) {
				factory.close();
			}
		}
		return success;
	}

	public void updateDDL(String systemName) {
		Properties props = DBConfiguration.getDataSourcePropertiesByName(systemName);
		if (props != null) {
			this.updateDDL(props);
		}
	}

}
