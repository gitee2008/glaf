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
import com.glaf.core.util.RSA;
import com.glaf.sms.domain.SmsClient;
import com.glaf.sms.mapper.SmsClientMapper;
import com.glaf.sms.query.SmsClientQuery;

@Service("com.glaf.sms.service.smsClientService")
@Transactional(readOnly = true)
public class SmsClientServiceImpl implements SmsClientService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SmsClientMapper smsClientMapper;

	protected SmsPersonService smsPersonService;

	public SmsClientServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<SmsClient> list) {
		for (SmsClient smsClient : list) {
			if (StringUtils.isEmpty(smsClient.getId())) {
				smsClient.setId(idGenerator.getNextId("SMS_CLIENT"));
			}
		}

		int batch_size = 50;
		List<SmsClient> rows = new ArrayList<SmsClient>(batch_size);

		for (SmsClient bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					smsClientMapper.bulkInsertSmsClient_oracle(rows);
				} else {
					smsClientMapper.bulkInsertSmsClient(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				smsClientMapper.bulkInsertSmsClient_oracle(rows);
			} else {
				smsClientMapper.bulkInsertSmsClient(rows);
			}
			rows.clear();
		}
	}

	public int count(SmsClientQuery query) {
		return smsClientMapper.getSmsClientCount(query);
	}

	/**
	 * 用自己的私钥解密数据
	 * 
	 * @param id
	 * @param data
	 * @return
	 */
	public byte[] decryptText(String id, byte[] data) {
		SmsClient client = this.getSmsClient(id);
		String privateKey = client.getPrivateKey();// 用自己的私钥解密数据
		if (StringUtils.isNotEmpty(privateKey)) {
			byte[] bytes = RSA.decryptByPrivateKey(data, privateKey);
			return bytes;
		}
		return null;
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			smsPersonService.deleteByClientId(id);
			smsClientMapper.deleteSmsClientById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				smsPersonService.deleteByClientId(id);
				smsClientMapper.deleteSmsClientById(id);
			}
		}
	}

	/**
	 * 用对方提供的公钥加密数据
	 * 
	 * @param id
	 * @param data
	 * @return
	 */
	public byte[] encryptText(String id, byte[] data) {
		SmsClient client = this.getSmsClient(id);
		String publicKey = client.getPeerPublicKey();// 用对方提供的公钥加密数据
		if (StringUtils.isNotEmpty(publicKey)) {
			byte[] bytes = RSA.encryptByPublicKey(data, publicKey);
			return bytes;
		}
		return null;
	}

	public SmsClient getSmsClient(String id) {
		if (id == null) {
			return null;
		}
		SmsClient smsClient = smsClientMapper.getSmsClientById(id);
		return smsClient;
	}

	public SmsClient getSmsClientBySysCode(String sysCode) {
		if (sysCode == null) {
			return null;
		}
		SmsClient smsClient = smsClientMapper.getSmsClientBySysCode(sysCode);
		return smsClient;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getSmsClientCountByQueryCriteria(SmsClientQuery query) {
		return smsClientMapper.getSmsClientCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<SmsClient> getSmsClientsByQueryCriteria(int start, int pageSize, SmsClientQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SmsClient> rows = sqlSessionTemplate.selectList("getSmsClients", query, rowBounds);
		return rows;
	}

	public List<SmsClient> list(SmsClientQuery query) {
		List<SmsClient> list = smsClientMapper.getSmsClients(query);
		return list;
	}

	@Transactional
	public void save(SmsClient smsClient) {
		if (StringUtils.isEmpty(smsClient.getId())) {
			smsClient.setId(smsClient.getSysCode());
			String sysPwd = DigestUtils.sha512Hex(smsClient.getSysCode() + smsClient.getSysPwd());
			smsClient.setSysPwd(sysPwd);
			smsClientMapper.insertSmsClient(smsClient);
		} else {
			if (!StringUtils.equals(smsClient.getSysPwd(), "88888888")) {
				if (smsClient.getSysPwd() != null && smsClient.getSysPwd().length() < 100) {
					String sysPwd = DigestUtils.sha512Hex(smsClient.getSysCode() + smsClient.getSysPwd());
					smsClient.setSysPwd(sysPwd);
				}
			}
			smsClientMapper.updateSmsClient(smsClient);
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

	@javax.annotation.Resource(name = "com.glaf.sms.mapper.SmsClientMapper")
	public void setSmsClientMapper(SmsClientMapper smsClientMapper) {
		this.smsClientMapper = smsClientMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource(name = "com.glaf.sms.service.smsPersonService")
	public void setSmsPersonService(SmsPersonService smsPersonService) {
		this.smsPersonService = smsPersonService;
	}

}
