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

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.base.modules.sys.mapper.TenantGrantMapper;
import com.glaf.base.modules.sys.model.TenantGrant;
import com.glaf.base.modules.sys.query.TenantGrantQuery;
import com.glaf.base.modules.sys.service.TenantGrantService;
import com.glaf.core.base.ColumnModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.util.DBUtils;

@Service("tenantGrantService")
@Transactional(readOnly = true)
public class TenantGrantServiceImpl implements TenantGrantService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private IdGenerator idGenerator;

	private SqlSessionTemplate sqlSessionTemplate;

	private TenantGrantMapper tenantGrantMapper;

	private ITableDataService tableDataService;

	public TenantGrantServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TenantGrant> list) {
		for (TenantGrant tenantGrant : list) {
			if (StringUtils.isEmpty(tenantGrant.getId())) {
				tenantGrant.setId(idGenerator.getNextId("SYS_TENANT_GRANT"));
				tenantGrant.setCreateTime(new Date());
			}
		}

		int batch_size = 100;
		List<TenantGrant> rows = new ArrayList<TenantGrant>(batch_size);

		for (TenantGrant bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					tenantGrantMapper.bulkInsertTenantGrant_oracle(rows);
				} else {
					tenantGrantMapper.bulkInsertTenantGrant(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				tenantGrantMapper.bulkInsertTenantGrant_oracle(rows);
			} else {
				tenantGrantMapper.bulkInsertTenantGrant(rows);
			}
			rows.clear();
		}
	}

	public int count(TenantGrantQuery query) {
		return tenantGrantMapper.getTenantGrantCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			tenantGrantMapper.deleteTenantGrantById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				tenantGrantMapper.deleteTenantGrantById(id);
			}
		}
	}

	public TenantGrant getTenantGrant(String id) {
		if (id == null) {
			return null;
		}
		return tenantGrantMapper.getTenantGrantById(id);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTenantGrantCountByQueryCriteria(TenantGrantQuery query) {
		return tenantGrantMapper.getTenantGrantCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TenantGrant> getTenantGrantsByQueryCriteria(int start, int pageSize, TenantGrantQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return sqlSessionTemplate.selectList("getTenantGrants", query, rowBounds);
	}

	public List<TenantGrant> list(TenantGrantQuery query) {
		return tenantGrantMapper.getTenantGrants(query);
	}

	@Transactional
	public void save(TenantGrant tenantGrant) {
		if (StringUtils.isEmpty(tenantGrant.getId())) {
			tenantGrant.setId(idGenerator.getNextId("SYS_TENANT_GRANT"));
			tenantGrant.setCreateTime(new Date());

			tenantGrantMapper.insertTenantGrant(tenantGrant);
		} else {
			tenantGrantMapper.updateTenantGrant(tenantGrant);
		}
	}

	@Transactional
	public void saveAll(String userId, String type, String privilege, List<TenantGrant> list) {
		TableModel tableModel = new TableModel();
		tableModel.setTableName("SYS_TENANT_GRANT");
		ColumnModel userIdColumn = new ColumnModel();
		userIdColumn.setColumnName("GRANTEE_");
		userIdColumn.setJavaType("String");
		userIdColumn.setValue(userId);
		tableModel.addColumn(userIdColumn);

		ColumnModel typeColumn = new ColumnModel();
		typeColumn.setColumnName("TYPE_");
		typeColumn.setJavaType("String");
		typeColumn.setValue(type);
		tableModel.addColumn(typeColumn);

		ColumnModel privilegeColumn = new ColumnModel();
		privilegeColumn.setColumnName("PRIVILEGE_");
		privilegeColumn.setJavaType("String");
		privilegeColumn.setValue(privilege);
		tableModel.addColumn(privilegeColumn);

		tableDataService.deleteTableData(tableModel);

		for (TenantGrant m : list) {
			m.setGrantee(userId);
			m.setPrivilege(privilege);
			m.setType(type);
		}

		this.bulkInsert(list);
	}

	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTenantGrantMapper(TenantGrantMapper tenantGrantMapper) {
		this.tenantGrantMapper = tenantGrantMapper;
	}

}
