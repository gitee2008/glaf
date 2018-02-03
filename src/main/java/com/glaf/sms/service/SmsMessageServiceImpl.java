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
import java.util.Calendar;
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
import com.glaf.core.util.DateUtils;
import com.glaf.sms.domain.SmsMessage;
import com.glaf.sms.mapper.SmsMessageMapper;
import com.glaf.sms.query.SmsMessageQuery;

@Service("com.glaf.sms.service.smsMessageService")
@Transactional(readOnly = true)
public class SmsMessageServiceImpl implements SmsMessageService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SmsMessageMapper smsMessageMapper;

	protected SmsClientService smsClientService;

	protected SmsServerService smsServerService;

	public SmsMessageServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SmsMessage> list) {
		Calendar calendar = Calendar.getInstance();
		for (SmsMessage smsMessage : list) {
			if (StringUtils.isEmpty(smsMessage.getId())) {
				if (StringUtils.isNotEmpty(smsMessage.getMessage())) {
					smsMessage.setCreateTime(new Date());
					calendar.setTime(smsMessage.getCreateTime());
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH) + 1;

					smsMessage.setYear(year);
					smsMessage.setMonth(month);
					smsMessage.setFullDay(DateUtils.getYearMonthDay(smsMessage.getCreateTime()));
					smsMessage.setId(DigestUtils.md5Hex(smsMessage.getMobile() + smsMessage.getMessage()));
				}
			}
		}

		int batch_size = 50;
		List<SmsMessage> rows = new ArrayList<SmsMessage>(batch_size);

		for (SmsMessage bean : list) {
			if (StringUtils.isNotEmpty(bean.getMobile()) && StringUtils.isNotEmpty(bean.getMessage())) {
				rows.add(bean);
			}
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					smsMessageMapper.bulkInsertSmsMessage_oracle(rows);
				} else {
					smsMessageMapper.bulkInsertSmsMessage(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				smsMessageMapper.bulkInsertSmsMessage_oracle(rows);
			} else {
				smsMessageMapper.bulkInsertSmsMessage(rows);
			}
			rows.clear();
		}
	}

	public int count(SmsMessageQuery query) {
		return smsMessageMapper.getSmsMessageCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			smsMessageMapper.deleteSmsMessageById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				smsMessageMapper.deleteSmsMessageById(id);
			}
		}
	}

	public SmsMessage getSmsMessage(String id) {
		if (id == null) {
			return null;
		}
		SmsMessage smsMessage = smsMessageMapper.getSmsMessageById(id);
		return smsMessage;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSmsMessageCountByQueryCriteria(SmsMessageQuery query) {
		return smsMessageMapper.getSmsMessageCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SmsMessage> getSmsMessagesByQueryCriteria(int start, int pageSize, SmsMessageQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SmsMessage> rows = sqlSessionTemplate.selectList("getSmsMessages", query, rowBounds);
		return rows;
	}

	public List<SmsMessage> list(SmsMessageQuery query) {
		List<SmsMessage> list = smsMessageMapper.getSmsMessages(query);
		return list;
	}

	@Transactional
	public void save(SmsMessage smsMessage) {
		if (StringUtils.isEmpty(smsMessage.getId())) {
			if (StringUtils.isNotEmpty(smsMessage.getMobile()) && StringUtils.isNotEmpty(smsMessage.getMessage())) {
				smsMessage.setId(DigestUtils.md5Hex(smsMessage.getMobile() + smsMessage.getMessage()));
				smsMessage.setCreateTime(new Date());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(smsMessage.getCreateTime());
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;

				smsMessage.setYear(year);
				smsMessage.setMonth(month);
				smsMessage.setFullDay(DateUtils.getYearMonthDay(smsMessage.getCreateTime()));

				smsMessageMapper.insertSmsMessage(smsMessage);
			}
		} else {
			smsMessageMapper.updateSmsMessage(smsMessage);
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

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsClientService")
	public void setSmsClientService(SmsClientService smsClientService) {
		this.smsClientService = smsClientService;
	}

	@javax.annotation.Resource(name = "com.glaf.sms.mapper.SmsMessageMapper")
	public void setSmsMessageMapper(SmsMessageMapper smsMessageMapper) {
		this.smsMessageMapper = smsMessageMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsServerService")
	public void setSmsServerService(SmsServerService smsServerService) {
		this.smsServerService = smsServerService;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public List<SmsMessage> getSmsMessageAbleSend() {
		return this.smsMessageMapper.getSmsMessageAbleSend();
	}

	@Override
	public int getSmsMessageAbleSendCount() {
		return this.smsMessageMapper.getSmsMessageAbleSendCount();
	}

}
