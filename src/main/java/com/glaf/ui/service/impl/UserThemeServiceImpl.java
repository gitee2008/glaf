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

import com.glaf.ui.mapper.UserThemeMapper;
import com.glaf.ui.model.UserTheme;
import com.glaf.ui.query.UserThemeQuery;
import com.glaf.ui.service.UserThemeService;

@Service("com.glaf.ui.service.userThemeService")
@Transactional(readOnly = true)
public class UserThemeServiceImpl implements UserThemeService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected UserThemeMapper userThemeMapper;

	public UserThemeServiceImpl() {

	}

	@Transactional
	public void deleteById(Integer id) {
		if (id != null) {
			userThemeMapper.deleteUserThemeById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Integer> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Integer id : ids) {
				userThemeMapper.deleteUserThemeById(id);
			}
		}
	}

	public int count(UserThemeQuery query) {
		return userThemeMapper.getUserThemeCount(query);
	}

	public List<UserTheme> list(UserThemeQuery query) {
		List<UserTheme> list = userThemeMapper.getUserThemes(query);
		return list;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getUserThemeCountByQueryCriteria(UserThemeQuery query) {
		return userThemeMapper.getUserThemeCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<UserTheme> getUserThemesByQueryCriteria(int start,
			int pageSize, UserThemeQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<UserTheme> rows = sqlSessionTemplate.selectList("getUserThemes",
				query, rowBounds);
		return rows;
	}

	public UserTheme getUserTheme(Integer id) {
		if (id == null) {
			return null;
		}
		UserTheme userTheme = userThemeMapper.getUserThemeById(id);
		return userTheme;
	}

	@Transactional
	public void save(UserTheme userTheme) {
		if (userTheme.getId() == null) {
			userTheme.setId(idGenerator.nextId("UI_USER_THEME").intValue());
			userTheme.setCreateDate(new Date());
			// userTheme.setDeleteFlag(0);
			userThemeMapper.insertUserTheme(userTheme);
		} else {
			userThemeMapper.updateUserTheme(userTheme);
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

	@javax.annotation.Resource(name = "com.glaf.ui.mapper.UserThemeMapper")
	public void setUserThemeMapper(UserThemeMapper userThemeMapper) {
		this.userThemeMapper = userThemeMapper;
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
