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

import java.util.Date;
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

import com.glaf.ui.mapper.ThemeMapper;
import com.glaf.ui.model.Theme;
import com.glaf.ui.query.ThemeQuery;
import com.glaf.ui.service.ThemeService;

@Service("com.glaf.ui.service.themeService")
@Transactional(readOnly = true)
public class ThemeServiceImpl implements ThemeService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected ThemeMapper themeMapper;

	public ThemeServiceImpl() {

	}

	@Transactional
	public void deleteById(Integer id) {
		if (id != null) {
			themeMapper.deleteThemeById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Integer> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Integer id : ids) {
				themeMapper.deleteThemeById(id);
			}
		}
	}

	public int count(ThemeQuery query) {
		return themeMapper.getThemeCount(query);
	}

	public List<Theme> list(ThemeQuery query) {
		List<Theme> list = themeMapper.getThemes(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getThemeCountByQueryCriteria(ThemeQuery query) {
		return themeMapper.getThemeCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Theme> getThemesByQueryCriteria(int start, int pageSize,
			ThemeQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Theme> rows = sqlSessionTemplate.selectList("getThemes", query,
				rowBounds);
		return rows;
	}

	public Theme getTheme(Integer id) {
		if (id == null) {
			return null;
		}
		Theme theme = themeMapper.getThemeById(id);
		return theme;
	}

	@Transactional
	public void save(Theme theme) {
		if (theme.getId() == null) {
			theme.setId(idGenerator.nextId("UI_THEME").intValue());
			theme.setCreateDate(new Date());
			themeMapper.insertTheme(theme);
		} else {
			themeMapper.updateTheme(theme);
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

	@javax.annotation.Resource(name = "com.glaf.ui.mapper.ThemeMapper")
	public void setThemeMapper(ThemeMapper themeMapper) {
		this.themeMapper = themeMapper;
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
