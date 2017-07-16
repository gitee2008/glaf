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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.domain.DictoryDefinition;
import com.glaf.core.domain.util.DictoryDefinitionJsonFactory;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.mapper.DictoryDefinitionMapper;
import com.glaf.core.query.DictoryDefinitionQuery;
import com.glaf.core.service.DictoryDefinitionService;

@Service("dictoryDefinitionService")
@Transactional(readOnly = true)
public class DictoryDefinitionServiceImpl implements DictoryDefinitionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DictoryDefinitionMapper dictoryDefinitionMapper;

	public DictoryDefinitionServiceImpl() {

	}

	public int count(DictoryDefinitionQuery query) {
		return dictoryDefinitionMapper.getDictoryDefinitionCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			dictoryDefinitionMapper.deleteDictoryDefinitionById(id);
		}
	}

	public DictoryDefinition getDictoryDefinition(Long id) {
		if (id == null) {
			return null;
		}
		String cacheKey = "sys_dict_def_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("dict_def", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return DictoryDefinitionJsonFactory.jsonToObject(json);
				} catch (Exception ex) {

				}
			}
		}

		DictoryDefinition dictoryDefinition = dictoryDefinitionMapper.getDictoryDefinitionById(id);
		if (dictoryDefinition != null && SystemConfig.getBoolean("use_query_cache")) {
			JSONObject json = dictoryDefinition.toJsonObject();
			CacheFactory.put("dict_def", cacheKey, json.toJSONString());
		}
		return dictoryDefinition;
	}

	public int getDictoryDefinitionCountByQueryCriteria(DictoryDefinitionQuery query) {
		return dictoryDefinitionMapper.getDictoryDefinitionCount(query);
	}

	public List<DictoryDefinition> getDictoryDefinitions(Long nodeId, String target) {
		String cacheKey = "sys_dict_def_" + nodeId + "_" + target;
		if (SystemConfig.getBoolean("use_query_cache") && CacheFactory.getString("dict_def", cacheKey) != null) {
			String text = CacheFactory.getString("dict_def", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONArray array = JSON.parseArray(text);
					return DictoryDefinitionJsonFactory.arrayToList(array);
				} catch (Exception ex) {

				}
			}
		}

		DictoryDefinitionQuery query = new DictoryDefinitionQuery();
		query.nodeId(nodeId);
		query.target(target);
		List<DictoryDefinition> list = dictoryDefinitionMapper.getDictoryDefinitions(query);
		if (list != null && !list.isEmpty()) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONArray array = DictoryDefinitionJsonFactory.listToArray(list);
				CacheFactory.put("dict_def", cacheKey, array.toJSONString());
			}
		}

		return list;
	}

	public List<DictoryDefinition> getDictoryDefinitionsByQueryCriteria(int start, int pageSize,
			DictoryDefinitionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DictoryDefinition> rows = sqlSessionTemplate.selectList("getDictoryDefinitions", query, rowBounds);
		return rows;
	}

	public List<DictoryDefinition> list(DictoryDefinitionQuery query) {
		List<DictoryDefinition> list = dictoryDefinitionMapper.getDictoryDefinitions(query);
		return list;
	}

	@Transactional
	public void save(DictoryDefinition dictoryDefinition) {
		if (dictoryDefinition.getId() == 0) {
			dictoryDefinition.setId(idGenerator.nextId());
			dictoryDefinition.setCreateDate(new Date());
			dictoryDefinitionMapper.insertDictoryDefinition(dictoryDefinition);
		} else {
			dictoryDefinitionMapper.updateDictoryDefinition(dictoryDefinition);
			String cacheKey = "sys_dict_def_" + dictoryDefinition.getId();
			CacheFactory.remove("dict_def", cacheKey);
		}
	}

	@Transactional
	public void saveAll(Long nodeId, String target, List<DictoryDefinition> dictoryDefinitions) {
		DictoryDefinitionQuery query = new DictoryDefinitionQuery();
		query.nodeId(nodeId);
		query.target(target);
		List<DictoryDefinition> list = dictoryDefinitionMapper.getDictoryDefinitions(query);
		if (list != null && !list.isEmpty()) {
			for (DictoryDefinition m : list) {
				this.deleteById(m.getId());
				String cacheKey = "sys_dict_def_" + m.getId();
				CacheFactory.remove("dict_def", cacheKey);
			}
		}

		if (dictoryDefinitions != null && !dictoryDefinitions.isEmpty()) {
			for (DictoryDefinition m : dictoryDefinitions) {
				m.setId(idGenerator.nextId());
				m.setNodeId(nodeId);
				m.setTarget(target);
				dictoryDefinitionMapper.insertDictoryDefinition(m);
			}
		}

		if (SystemConfig.getBoolean("use_query_cache")) {
			String cacheKey = "sys_dict_def_" + nodeId + "_" + target;
			CacheFactory.remove("dict_def", cacheKey);
		}
	}

	@javax.annotation.Resource
	public void setDictoryDefinitionMapper(DictoryDefinitionMapper dictoryDefinitionMapper) {
		this.dictoryDefinitionMapper = dictoryDefinitionMapper;
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
