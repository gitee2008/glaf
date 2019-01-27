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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.mapper.TenantFollowMapper;
import com.glaf.base.modules.sys.model.TenantFollow;
import com.glaf.base.modules.sys.query.TenantFollowQuery;
import com.glaf.base.modules.sys.service.TenantFollowService;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.DBUtils;

@Service("tenantFollowService")
@Transactional(readOnly = true)
public class TenantFollowServiceImpl implements TenantFollowService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private TenantFollowMapper tenantFollowMapper;

	public TenantFollowServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TenantFollow> list) {
		for (TenantFollow tenantFollow : list) {
			if (tenantFollow.getId() == 0) {
				tenantFollow.setId(idGenerator.nextId("SYS_TENANT_FOLLOW"));
			}
		}

		int batch_size = 100;
		List<TenantFollow> rows = new ArrayList<TenantFollow>(batch_size);

		for (TenantFollow bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					tenantFollowMapper.bulkInsertTenantFollow_oracle(rows);
				} else {
					tenantFollowMapper.bulkInsertTenantFollow(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				tenantFollowMapper.bulkInsertTenantFollow_oracle(rows);
			} else {
				tenantFollowMapper.bulkInsertTenantFollow(rows);
			}
			rows.clear();
		}
	}

	public int count(TenantFollowQuery query) {
		return tenantFollowMapper.getTenantFollowCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id > 0) {
			tenantFollowMapper.deleteTenantFollowById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				tenantFollowMapper.deleteTenantFollowById(id);
			}
		}
	}

	public TenantFollow getTenantFollow(long id) {
		if (id <= 0) {
			return null;
		}
		return tenantFollowMapper.getTenantFollowById(id);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTenantFollowCountByQueryCriteria(TenantFollowQuery query) {
		return tenantFollowMapper.getTenantFollowCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TenantFollow> getTenantFollowsByQueryCriteria(int start, int pageSize, TenantFollowQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getTenantFollows", query, rowBounds);
	}

	public List<TenantFollow> list(TenantFollowQuery query) {
		return tenantFollowMapper.getTenantFollows(query);
	}

	@Transactional
	public void save(TenantFollow tenantFollow) {
		if (tenantFollow.getId() == 0) {
			tenantFollow.setId(idGenerator.nextId("SYS_TENANT_FOLLOW"));
			tenantFollow.setCreateTime(new Date());
			tenantFollowMapper.insertTenantFollow(tenantFollow);
		}
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTenantFollowMapper(TenantFollowMapper tenantFollowMapper) {
		this.tenantFollowMapper = tenantFollowMapper;
	}

}
