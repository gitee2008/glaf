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

package com.glaf.matrix.data.service.impl;

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
import com.glaf.matrix.data.domain.TableSysPermission;
import com.glaf.matrix.data.mapper.TableSysPermissionMapper;
import com.glaf.matrix.data.query.TableSysPermissionQuery;
import com.glaf.matrix.data.service.TableSysPermissionService;

@Service("tableSysPermissionService")
@Transactional(readOnly = true)
public class TableSysPermissionServiceImpl implements TableSysPermissionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TableSysPermissionMapper tableSysPermissionMapper;

	public TableSysPermissionServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TableSysPermission> list) {
		for (TableSysPermission tableSysPermission : list) {
			if (StringUtils.isEmpty(tableSysPermission.getId())) {
				tableSysPermission.setId(idGenerator.getNextId("SYS_TABLE_PERMISSION"));
			}
		}

		int batch_size = 200;
		List<TableSysPermission> rows = new ArrayList<TableSysPermission>(batch_size);

		for (TableSysPermission bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					tableSysPermissionMapper.bulkInsertTableSysPermission_oracle(rows);
				} else {
					tableSysPermissionMapper.bulkInsertTableSysPermission(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				tableSysPermissionMapper.bulkInsertTableSysPermission_oracle(rows);
			} else {
				tableSysPermissionMapper.bulkInsertTableSysPermission(rows);
			}
			rows.clear();
		}
	}

	public int count(TableSysPermissionQuery query) {
		return tableSysPermissionMapper.getTableSysPermissionCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			tableSysPermissionMapper.deleteTableSysPermissionById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				tableSysPermissionMapper.deleteTableSysPermissionById(id);
			}
		}
	}

	public TableSysPermission getTableSysPermission(String id) {
		if (id == null) {
			return null;
		}
		TableSysPermission tableSysPermission = tableSysPermissionMapper.getTableSysPermissionById(id);
		return tableSysPermission;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTableSysPermissionCountByQueryCriteria(TableSysPermissionQuery query) {
		return tableSysPermissionMapper.getTableSysPermissionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TableSysPermission> getTableSysPermissionsByQueryCriteria(int start, int pageSize,
			TableSysPermissionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TableSysPermission> rows = sqlSessionTemplate.selectList("getTableSysPermissions", query, rowBounds);
		return rows;
	}

	public List<TableSysPermission> list(TableSysPermissionQuery query) {
		List<TableSysPermission> list = tableSysPermissionMapper.getTableSysPermissions(query);
		return list;
	}

	@Transactional
	public void save(TableSysPermission tableSysPermission) {
		if (StringUtils.isEmpty(tableSysPermission.getId())) {
			tableSysPermission.setId(idGenerator.getNextId("SYS_TABLE_PERMISSION"));
			tableSysPermission.setCreateTime(new Date());
			tableSysPermissionMapper.insertTableSysPermission(tableSysPermission);
		} else {
			tableSysPermissionMapper.updateTableSysPermission(tableSysPermission);
		}
	}

	@Transactional
	public void savePrivileges(String tableId, String granteeType, String privilege, String type,
			List<TableSysPermission> rows) {
		TableSysPermissionQuery query = new TableSysPermissionQuery();
		query.tableId(tableId);
		query.privilege(privilege);
		query.granteeType(granteeType);
		query.type(type);
		List<TableSysPermission> list = this.list(query);
		if (list != null && !list.isEmpty()) {
			for (TableSysPermission p : list) {
				tableSysPermissionMapper.deleteTableSysPermissionById(p.getId());
			}
		}

		if (rows != null && !rows.isEmpty()) {
			for (TableSysPermission p : rows) {
				p.setId(idGenerator.getNextId("SYS_TABLE_PERMISSION"));
				p.setCreateTime(new Date());
				p.setTableId(tableId);
				p.setGranteeType(granteeType);
				p.setPrivilege(privilege);
				p.setType(type);
				tableSysPermissionMapper.insertTableSysPermission(p);
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

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTableSysPermissionMapper(TableSysPermissionMapper tableSysPermissionMapper) {
		this.tableSysPermissionMapper = tableSysPermissionMapper;
	}

}
