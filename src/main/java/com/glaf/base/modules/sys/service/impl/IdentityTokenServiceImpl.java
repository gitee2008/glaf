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

package com.glaf.base.modules.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.mapper.IdentityTokenMapper;
import com.glaf.base.modules.sys.model.IdentityToken;
import com.glaf.base.modules.sys.query.IdentityTokenQuery;
import com.glaf.base.modules.sys.service.IdentityTokenService;
import com.glaf.core.util.UUID32;

@Service("identityTokenService")
@Transactional(readOnly = true)
public class IdentityTokenServiceImpl implements IdentityTokenService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private SqlSessionTemplate sqlSessionTemplate;

	private IdentityTokenMapper identityTokenMapper;

	public IdentityTokenServiceImpl() {

	}

	public int count(IdentityTokenQuery query) {
		return identityTokenMapper.getIdentityTokenCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			identityTokenMapper.deleteIdentityTokenById(id);
		}
	}

	public IdentityToken getIdentityToken(String id) {
		if (id == null) {
			return null;
		}
		return identityTokenMapper.getIdentityTokenById(id);
	}

	public IdentityToken getIdentityTokenByToken(String token) {
		if (token == null) {
			return null;
		}
		return identityTokenMapper.getIdentityTokenByToken(token);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getIdentityTokenCountByQueryCriteria(IdentityTokenQuery query) {
		return identityTokenMapper.getIdentityTokenCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<IdentityToken> getIdentityTokensByQueryCriteria(int start, int pageSize, IdentityTokenQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getIdentityTokens", query, rowBounds);
	}

	public List<IdentityToken> list(IdentityTokenQuery query) {
		return identityTokenMapper.getIdentityTokens(query);
	}

	@Transactional
	public void save(IdentityToken identityToken) {
		if (StringUtils.isEmpty(identityToken.getId())) {
			identityToken.setId(UUID32.getUUID());
			identityToken.setCreateTime(new Date());
			identityToken.setTimeMillis(System.currentTimeMillis());
			identityTokenMapper.insertIdentityToken(identityToken);
		} else {
			IdentityToken model = identityTokenMapper.getIdentityTokenById(identityToken.getId());
			if (model == null) {
				identityToken.setCreateTime(new Date());
				identityToken.setTimeMillis(System.currentTimeMillis());
				identityTokenMapper.insertIdentityToken(identityToken);
			} else {
				identityToken.setCreateTime(new Date());
				identityToken.setTimeMillis(System.currentTimeMillis());
				identityTokenMapper.updateIdentityToken(identityToken);
			}
		}
	}

	@javax.annotation.Resource
	public void setIdentityTokenMapper(IdentityTokenMapper identityTokenMapper) {
		this.identityTokenMapper = identityTokenMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
