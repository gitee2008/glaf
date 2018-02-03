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
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;
import com.glaf.sms.domain.SmsHistoryMessage;
import com.glaf.sms.mapper.SmsHistoryMessageMapper;
import com.glaf.sms.query.SmsHistoryMessageQuery;

@Service("com.glaf.sms.service.smsHistoryMessageService")
@Transactional(readOnly = true)
public class SmsHistoryMessageServiceImpl implements SmsHistoryMessageService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SmsHistoryMessageMapper smsHistoryMessageMapper;

	public SmsHistoryMessageServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SmsHistoryMessage> list) {
		for (SmsHistoryMessage smsHistoryMessage : list) {
			if (StringUtils.isEmpty(smsHistoryMessage.getId())) {
				if (StringUtils.isEmpty(smsHistoryMessage.getId())) {
					if (StringUtils.isNotEmpty(smsHistoryMessage.getMessage())) {
						smsHistoryMessage.setId(
								DigestUtils.md5Hex(smsHistoryMessage.getMobile() + smsHistoryMessage.getMessage()));
					}
				}
			}
		}

		int batch_size = 50;
		List<SmsHistoryMessage> rows = new ArrayList<SmsHistoryMessage>(batch_size);

		for (SmsHistoryMessage bean : list) {
			if (StringUtils.isNotEmpty(bean.getMobile()) && StringUtils.isNotEmpty(bean.getMessage())) {
				rows.add(bean);
			}
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					smsHistoryMessageMapper.bulkInsertSmsHistoryMessage_oracle(rows);
				} else {
					smsHistoryMessageMapper.bulkInsertSmsHistoryMessage(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				smsHistoryMessageMapper.bulkInsertSmsHistoryMessage_oracle(rows);
			} else {
				smsHistoryMessageMapper.bulkInsertSmsHistoryMessage(rows);
			}
			rows.clear();
		}
	}

	public int count(SmsHistoryMessageQuery query) {
		return smsHistoryMessageMapper.getSmsHistoryMessageCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			smsHistoryMessageMapper.deleteSmsHistoryMessageById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				smsHistoryMessageMapper.deleteSmsHistoryMessageById(id);
			}
		}
	}

	public SmsHistoryMessage getSmsHistoryMessage(String id) {
		if (id == null) {
			return null;
		}
		SmsHistoryMessage smsHistoryMessage = smsHistoryMessageMapper.getSmsHistoryMessageById(id);
		return smsHistoryMessage;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSmsHistoryMessageCountByQueryCriteria(SmsHistoryMessageQuery query) {
		return smsHistoryMessageMapper.getSmsHistoryMessageCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SmsHistoryMessage> getSmsHistoryMessagesByQueryCriteria(int start, int pageSize,
			SmsHistoryMessageQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SmsHistoryMessage> rows = sqlSessionTemplate.selectList("getSmsHistoryMessages", query, rowBounds);
		return rows;
	}

	public List<SmsHistoryMessage> list(SmsHistoryMessageQuery query) {
		List<SmsHistoryMessage> list = smsHistoryMessageMapper.getSmsHistoryMessages(query);
		return list;
	}

	@Transactional
	public void save(SmsHistoryMessage smsHistoryMessage) {
		if (StringUtils.isEmpty(smsHistoryMessage.getId())) {
			if (StringUtils.isNotEmpty(smsHistoryMessage.getMobile())
					&& StringUtils.isNotEmpty(smsHistoryMessage.getMessage())) {
				smsHistoryMessage
						.setId(DigestUtils.md5Hex(smsHistoryMessage.getMobile() + smsHistoryMessage.getMessage()));
				smsHistoryMessage.setCreateTime(new Date());
				smsHistoryMessageMapper.insertSmsHistoryMessage(smsHistoryMessage);
			}
			// smsHistoryMessageMapper.insertSmsHistoryMessage(smsHistoryMessage);
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

	@javax.annotation.Resource(name = "com.glaf.sms.mapper.SmsHistoryMessageMapper")
	public void setSmsHistoryMessageMapper(SmsHistoryMessageMapper smsHistoryMessageMapper) {
		this.smsHistoryMessageMapper = smsHistoryMessageMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
