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

package com.glaf.sms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.glaf.core.cache.CacheFactory;
import com.glaf.core.config.SystemConfig;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;

import com.glaf.sms.domain.SmsServer;
import com.glaf.sms.mapper.SmsServerMapper;
import com.glaf.sms.query.SmsServerQuery;
import com.glaf.sms.util.SmsServerJsonFactory;

@Service("com.glaf.sms.service.smsServerService")
@Transactional(readOnly = true)
public class SmsServerServiceImpl implements SmsServerService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SmsServerMapper smsServerMapper;

	public SmsServerServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SmsServer> list) {
		CacheFactory.clear("sms_server");
		for (SmsServer smsServer : list) {
			if (StringUtils.isEmpty(smsServer.getId())) {
				smsServer.setId(idGenerator.getNextId("SMS_SERVER"));
			}
		}

		int batch_size = 50;
		List<SmsServer> rows = new ArrayList<SmsServer>(batch_size);

		for (SmsServer bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					smsServerMapper.bulkInsertSmsServer_oracle(rows);
				} else {
					smsServerMapper.bulkInsertSmsServer(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				smsServerMapper.bulkInsertSmsServer_oracle(rows);
			} else {
				smsServerMapper.bulkInsertSmsServer(rows);
			}
			rows.clear();
		}
	}

	public int count(SmsServerQuery query) {
		return smsServerMapper.getSmsServerCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			CacheFactory.clear("sms_server");
			smsServerMapper.deleteSmsServerById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			CacheFactory.clear("sms_server");
			for (String id : ids) {
				smsServerMapper.deleteSmsServerById(id);
			}
		}
	}

	public SmsServer getSmsServer(String id) {
		if (id == null) {
			return null;
		}
		String cacheKey = "sms_server_" + id;
		if (SystemConfig.getBoolean("use_query_cache")) {
			String text = CacheFactory.getString("sms_server", cacheKey);
			if (StringUtils.isNotEmpty(text)) {
				try {
					JSONObject json = JSON.parseObject(text);
					return SmsServerJsonFactory.jsonToObject(json);
				} catch (Exception ex) {
				}
			}
		}
		SmsServer smsServer = smsServerMapper.getSmsServerById(id);
		if (smsServer != null) {
			if (SystemConfig.getBoolean("use_query_cache")) {
				JSONObject json = smsServer.toJsonObject();
				CacheFactory.put("sms_server", cacheKey, json.toJSONString());
			}
		}
		return smsServer;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSmsServerCountByQueryCriteria(SmsServerQuery query) {
		return smsServerMapper.getSmsServerCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SmsServer> getSmsServersByQueryCriteria(int start, int pageSize, SmsServerQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SmsServer> rows = sqlSessionTemplate.selectList("getSmsServers", query, rowBounds);
		return rows;
	}

	public List<SmsServer> list(SmsServerQuery query) {
		List<SmsServer> list = smsServerMapper.getSmsServers(query);
		return list;
	}

	@Transactional
	public void save(SmsServer smsServer) {
		CacheFactory.clear("sms_server");
		if (StringUtils.isEmpty(smsServer.getId())) {
			smsServer.setId(idGenerator.getNextId("SMS_SERVER"));
			smsServer.setCreateTime(new java.util.Date());
			smsServerMapper.insertSmsServer(smsServer);
		} else {
			SmsServer model = this.getSmsServer(smsServer.getId());
			if (model == null) {
				smsServer.setCreateTime(new java.util.Date());
				smsServerMapper.insertSmsServer(smsServer);
			} else {
				smsServerMapper.updateSmsServer(smsServer);
			}
		}
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
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.sms.mapper.SmsServerMapper")
	public void setSmsServerMapper(SmsServerMapper smsServerMapper) {
		this.smsServerMapper = smsServerMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Transactional
	public void update(SmsServer smsServer) {
		CacheFactory.clear("sms_server");
		smsServerMapper.updateSmsServer(smsServer);
	}

}
