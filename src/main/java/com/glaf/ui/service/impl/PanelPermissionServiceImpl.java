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
package com.glaf.ui.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.ui.mapper.PanelPermissionMapper;
import com.glaf.ui.model.PanelPermission;
import com.glaf.ui.query.PanelPermissionQuery;
import com.glaf.ui.service.PanelPermissionService;

@Service("panelPermissionService")
@Transactional(readOnly = true)
public class PanelPermissionServiceImpl implements PanelPermissionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PanelPermissionMapper panelPermissionMapper;

	public PanelPermissionServiceImpl() {

	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			panelPermissionMapper.deletePanelPermissionById(id);
		}
	}

	@Transactional
	public void deleteByPanelId(String panelId) {
		if (panelId != null) {
			panelPermissionMapper.deletePanelPermissionsByPanelId(panelId);
		}
	}

	public int count(PanelPermissionQuery query) {
		return panelPermissionMapper.getPanelPermissionCount(query);
	}

	public List<PanelPermission> list(PanelPermissionQuery query) {
		List<PanelPermission> list = panelPermissionMapper
				.getPanelPermissions(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPanelPermissionCountByQueryCriteria(PanelPermissionQuery query) {
		return panelPermissionMapper.getPanelPermissionCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PanelPermission> getPanelPermissionsByQueryCriteria(int start,
			int pageSize, PanelPermissionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PanelPermission> rows = sqlSessionTemplate.selectList(
				"getPanelPermissions", query, rowBounds);
		return rows;
	}

	public PanelPermission getPanelPermission(String id) {
		if (id == null) {
			return null;
		}
		PanelPermission panelPermission = panelPermissionMapper
				.getPanelPermissionById(id);
		return panelPermission;
	}

	@Transactional
	public void save(PanelPermission panelPermission) {
		panelPermission.setId(idGenerator.getNextId("UI_PANEL_PERM"));
		panelPermissionMapper.insertPanelPermission(panelPermission);
	}

	/**
	 * 保存多条记录
	 * 
	 * @param panelId
	 * @param panelPermissions
	 */
	@Transactional
	public void saveAll(String panelId, List<PanelPermission> panelPermissions) {
		panelPermissionMapper.deletePanelPermissionsByPanelId(panelId);
		if(panelPermissions != null && panelPermissions.size() > 0){
			for (PanelPermission m : panelPermissions) {
				m.setId(idGenerator.getNextId("UI_PANEL_PERM"));
				panelPermissionMapper.insertPanelPermission(m);
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
	public void setPanelPermissionMapper(
			PanelPermissionMapper panelPermissionMapper) {
		this.panelPermissionMapper = panelPermissionMapper;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
