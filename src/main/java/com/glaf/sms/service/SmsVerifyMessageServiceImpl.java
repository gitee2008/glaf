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
import com.glaf.core.util.DateUtils;
import com.glaf.sms.domain.SmsVerifyMessage;
import com.glaf.sms.mapper.SmsVerifyMessageMapper;
import com.glaf.sms.query.SmsVerifyMessageQuery;

@Service("com.glaf.sms.service.smsVerifyMessageService")
@Transactional(readOnly = true)
public class SmsVerifyMessageServiceImpl implements SmsVerifyMessageService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SmsVerifyMessageMapper smsVerifyMessageMapper;

	public SmsVerifyMessageServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SmsVerifyMessage> list) {
		for (SmsVerifyMessage smsVerifyMessage : list) {
			if (StringUtils.isEmpty(smsVerifyMessage.getId())) {
				smsVerifyMessage.setId(smsVerifyMessage.getMobile() + "_" + DateUtils.getNowYearMonthDayHHmm());
			}
		}

		int batch_size = 50;
		List<SmsVerifyMessage> rows = new ArrayList<SmsVerifyMessage>(batch_size);

		for (SmsVerifyMessage bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					smsVerifyMessageMapper.bulkInsertSmsVerifyMessage_oracle(rows);
				} else {
					smsVerifyMessageMapper.bulkInsertSmsVerifyMessage(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				smsVerifyMessageMapper.bulkInsertSmsVerifyMessage_oracle(rows);
			} else {
				smsVerifyMessageMapper.bulkInsertSmsVerifyMessage(rows);
			}
			rows.clear();
		}
	}

	public int count(SmsVerifyMessageQuery query) {
		return smsVerifyMessageMapper.getSmsVerifyMessageCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			smsVerifyMessageMapper.deleteSmsVerifyMessageById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				smsVerifyMessageMapper.deleteSmsVerifyMessageById(id);
			}
		}
	}

	public int getSmsCount(String mobile, long dateAfter) {
		SmsVerifyMessageQuery query = new SmsVerifyMessageQuery();
		query.mobile(mobile);
		query.sendTimeMsGreaterThanOrEqual(dateAfter);
		return smsVerifyMessageMapper.getSmsVerifyMessageCount(query);
	}

	public SmsVerifyMessage getSmsVerifyMessage(String id) {
		if (id == null) {
			return null;
		}
		SmsVerifyMessage smsVerifyMessage = smsVerifyMessageMapper.getSmsVerifyMessageById(id);
		return smsVerifyMessage;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSmsVerifyMessageCountByQueryCriteria(SmsVerifyMessageQuery query) {
		return smsVerifyMessageMapper.getSmsVerifyMessageCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SmsVerifyMessage> getSmsVerifyMessagesByQueryCriteria(int start, int pageSize,
			SmsVerifyMessageQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SmsVerifyMessage> rows = sqlSessionTemplate.selectList("getSmsVerifyMessages", query, rowBounds);
		return rows;
	}

	public List<SmsVerifyMessage> list(SmsVerifyMessageQuery query) {
		List<SmsVerifyMessage> list = smsVerifyMessageMapper.getSmsVerifyMessages(query);
		return list;
	}

	@Transactional
	public void save(SmsVerifyMessage smsVerifyMessage) {
		if (StringUtils.isEmpty(smsVerifyMessage.getId())) {
			smsVerifyMessage.setId(smsVerifyMessage.getMobile() + "_" + DateUtils.getNowYearMonthDayHHmm());
			smsVerifyMessage.setCreateTime(new Date());

			smsVerifyMessageMapper.insertSmsVerifyMessage(smsVerifyMessage);
		} else {
			smsVerifyMessageMapper.updateSmsVerifyMessage(smsVerifyMessage);
		}
	}

	@Transactional
	public void update(SmsVerifyMessage smsVerifyMessage) {
		smsVerifyMessageMapper.updateSmsVerifyMessage(smsVerifyMessage);
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

	@javax.annotation.Resource(name = "com.glaf.sms.mapper.SmsVerifyMessageMapper")
	public void setSmsVerifyMessageMapper(SmsVerifyMessageMapper smsVerifyMessageMapper) {
		this.smsVerifyMessageMapper = smsVerifyMessageMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public boolean verify(String mobile, String verificationCode, long dateAfter) {
		SmsVerifyMessageQuery query = new SmsVerifyMessageQuery();
		query.mobile(mobile);
		query.sendTimeMsGreaterThanOrEqual(dateAfter);
		query.status(9);
		List<SmsVerifyMessage> list = smsVerifyMessageMapper.getSmsVerifyMessages(query);
		if (list != null && !list.isEmpty()) {
			for (SmsVerifyMessage msg : list) {
				if (StringUtils.equals(msg.getVerificationCode(), verificationCode)) {
					return true;
				}
			}
		}
		return false;
	}

}
