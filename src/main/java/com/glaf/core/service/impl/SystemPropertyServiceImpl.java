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

package com.glaf.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.domain.SystemProperty;
import com.glaf.core.domain.util.SystemPropertyJsonFactory;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.mapper.SystemPropertyMapper;
import com.glaf.core.query.SystemPropertyQuery;
import com.glaf.core.service.ISystemPropertyService;
import com.glaf.core.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("systemPropertyService")
@Transactional(readOnly = true)
public class SystemPropertyServiceImpl implements ISystemPropertyService {
	protected final static Log logger = LogFactory.getLog(SystemPropertyServiceImpl.class);

	private IdGenerator idGenerator;

	private SystemPropertyMapper systemPropertyMapper;

	public int count(SystemPropertyQuery query) {
		return systemPropertyMapper.getSystemPropertyCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		systemPropertyMapper.deleteSystemPropertyById(id);
	}

	public List<SystemProperty> getAllSystemProperties() {
		String cacheKey = Constants.CACHE_PROPERTY_REGION + "_all";
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_PROPERTY_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return SystemPropertyJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SystemPropertyQuery query = new SystemPropertyQuery();
		List<SystemProperty> list = this.list(query);
		List<SystemProperty> rows = new ArrayList<SystemProperty>();
		if (list != null && !list.isEmpty()) {
			for (SystemProperty p : list) {
				if (!StringUtils.equals("TOKEN", p.getId())) {
					rows.add(p);
				}
			}
		}

		if (SystemConfig.getBoolean("use_query_cache")) {
			JSONArray jsonArray = SystemPropertyJsonFactory.listToArray(rows);
			CacheFactory.put(Constants.CACHE_PROPERTY_REGION, cacheKey, jsonArray.toJSONString());
		}

		return rows;
	}

	public Map<String, SystemProperty> getProperyMap() {
		List<SystemProperty> list = this.getAllSystemProperties();
		Map<String, SystemProperty> dataMap = new java.util.HashMap<String, SystemProperty>();
		Iterator<SystemProperty> iterator = list.iterator();
		while (iterator.hasNext()) {
			SystemProperty p = iterator.next();
			dataMap.put(p.getName(), p);
		}
		return dataMap;
	}

	public List<SystemProperty> getSystemProperties(String category) {
		String cacheKey = Constants.CACHE_PROPERTY_REGION + "_" + category;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_PROPERTY_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray jsonArray = JSON.parseArray(text);
					return SystemPropertyJsonFactory.arrayToList(jsonArray);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SystemPropertyQuery query = new SystemPropertyQuery();
		query.category(category);
		List<SystemProperty> list = this.list(query);
		List<SystemProperty> rows = new ArrayList<SystemProperty>();
		if (list != null && !list.isEmpty()) {
			for (SystemProperty p : list) {
				if (!StringUtils.equals("TOKEN", p.getId())) {
					rows.add(p);
				}
			}
		}

		if (SystemConfig.getBoolean("use_query_cache")) {
			JSONArray jsonArray = SystemPropertyJsonFactory.listToArray(rows);
			CacheFactory.put(Constants.CACHE_PROPERTY_REGION, cacheKey, jsonArray.toJSONString());
		}

		return rows;
	}

	public SystemProperty getSystemProperty(String category, String name) {
		String cacheKey = Constants.CACHE_PROPERTY_REGION + "_" + category + "_" + name;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_PROPERTY_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SystemPropertyJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}

		SystemPropertyQuery query = new SystemPropertyQuery();
		query.category(category);
		query.name(name);
		List<SystemProperty> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			SystemProperty property = list.get(0);
			if (SystemConfig.getBoolean("use_query_cache")) {
				CacheFactory.put(Constants.CACHE_PROPERTY_REGION, cacheKey, property.toJsonObject().toJSONString());
			}
			return property;
		}
		return null;
	}

	public SystemProperty getSystemPropertyById(String id) {
		String cacheKey = Constants.CACHE_PROPERTY_REGION + "_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString(Constants.CACHE_PROPERTY_REGION, cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SystemPropertyJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
					// Ignore error
				}
			}
		}
		SystemProperty property = systemPropertyMapper.getSystemPropertyById(id);
		if (property != null && SystemConfig.getBoolean("use_query_cache")) {
			CacheFactory.put(Constants.CACHE_PROPERTY_REGION, cacheKey, property.toJsonObject().toJSONString());
		}
		return property;
	}

	private List<SystemProperty> list(SystemPropertyQuery query) {
		List<SystemProperty> list = systemPropertyMapper.getSystemProperties(query);
		List<SystemProperty> rows = new ArrayList<SystemProperty>();
		if (list != null && !list.isEmpty()) {
			for (SystemProperty p : list) {
				if (!StringUtils.equals("TOKEN", p.getId())) {
					rows.add(p);
				}
			}
		}
		return rows;
	}

	@Transactional
	public void save(SystemProperty property) {
		CacheFactory.clear(Constants.CACHE_PROPERTY_REGION);
		if (StringUtils.isNotEmpty(property.getId())) {
			SystemProperty bean = this.getSystemPropertyById(property.getId());
			if (bean != null) {
				bean.setDescription(property.getDescription());
				bean.setValue(property.getValue());
				bean.setInitValue(property.getInitValue());
				bean.setInputType(property.getInputType());
				bean.setLocked(property.getLocked());
				bean.setTitle(property.getTitle());
				bean.setType(property.getType());
				systemPropertyMapper.updateSystemProperty(bean);
				SystemConfig.setProperty(bean);
			} else {
				systemPropertyMapper.insertSystemProperty(property);
				SystemConfig.setProperty(property);
			}
		} else {
			SystemPropertyQuery query = new SystemPropertyQuery();
			query.category(property.getCategory());
			query.name(property.getName());
			List<SystemProperty> list = this.list(query);
			if (list != null && !list.isEmpty()) {
				SystemProperty bean = list.get(0);
				bean.setDescription(property.getDescription());
				bean.setValue(property.getValue());
				bean.setInitValue(property.getInitValue());
				bean.setInputType(property.getInputType());
				bean.setLocked(property.getLocked());
				bean.setTitle(property.getTitle());
				bean.setType(property.getType());
				systemPropertyMapper.updateSystemProperty(bean);
				SystemConfig.setProperty(bean);
			} else {
				if (property.getId() == null) {
					property.setId(idGenerator.getNextId());
				}
				systemPropertyMapper.insertSystemProperty(property);
				SystemConfig.setProperty(property);
			}
		}
	}

	@Transactional
	public void saveAll(List<SystemProperty> props) {
		CacheFactory.clear(Constants.CACHE_PROPERTY_REGION);
		Map<String, SystemProperty> propertyMap = this.getProperyMap();
		if (props != null && props.size() > 0) {
			Map<String, String> dataMap = new TreeMap<String, String>();
			Iterator<SystemProperty> iterator = props.iterator();
			while (iterator.hasNext()) {
				SystemProperty p = iterator.next();
				dataMap.put(p.getName(), p.getValue());

				if (propertyMap.get(p.getName()) != null) {
					SystemProperty model = propertyMap.get(p.getName());
					model.setDescription(p.getDescription());
					model.setTitle(p.getTitle());
					model.setValue(p.getValue());
					systemPropertyMapper.updateSystemProperty(model);
				} else {
					p.setId(idGenerator.getNextId());
					systemPropertyMapper.insertSystemProperty(p);
				}
			}
			SystemConfig.reload();
		}
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSystemPropertyMapper(SystemPropertyMapper systemPropertyMapper) {
		this.systemPropertyMapper = systemPropertyMapper;
	}

}