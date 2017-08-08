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
package com.glaf.matrix.resource.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.UUID32;
import com.glaf.matrix.resource.domain.PageResource;
import com.glaf.matrix.resource.mapper.PageResourceMapper;
import com.glaf.matrix.resource.query.PageResourceQuery;

@Service("com.glaf.matrix.resource.service.pageResourceService")
@Transactional(readOnly = true)
public class PageResourceServiceImpl implements PageResourceService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PageResourceMapper pageResourceMapper;

	public PageResourceServiceImpl() {

	}

	public int count(PageResourceQuery query) {
		return pageResourceMapper.getPageResourceCount(query);
	}

	@Transactional
	public void deleteById(long id) {
		if (id != 0) {
			pageResourceMapper.deletePageResourceById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				pageResourceMapper.deletePageResourceById(id);
			}
		}
	}

	public PageResource getPageResource(long id) {
		if (id == 0) {
			return null;
		}
		PageResource pageResource = pageResourceMapper.getPageResourceById(id);
		return pageResource;
	}

	@Override
	public PageResource getPageResourceByFileId(String fileId) {
		return pageResourceMapper.getPageResourceByFileId(fileId);
	}

	public PageResource getPageResourceByPath(String path) {
		return pageResourceMapper.getPageResourceByPath(path);
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPageResourceCountByQueryCriteria(PageResourceQuery query) {
		return pageResourceMapper.getPageResourceCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PageResource> getPageResourcesByQueryCriteria(int start, int pageSize, PageResourceQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PageResource> rows = sqlSessionTemplate.selectList("getPageResources", query, rowBounds);
		return rows;
	}

	public List<PageResource> list(PageResourceQuery query) {
		List<PageResource> list = pageResourceMapper.getPageResources(query);
		return list;
	}

	@Transactional
	public void save(PageResource pageResource) {
		if (pageResource.getId() == 0) {
			pageResource.setId(idGenerator.nextId("PAGE_RESOURCE"));
			pageResource.setCreateTime(new Date());
			if (StringUtils.isEmpty(pageResource.getResFileId())) {
				pageResource.setResFileId(UUID32.getUUID());
			}
			pageResourceMapper.insertPageResource(pageResource);
		} else {
			pageResourceMapper.updatePageResource(pageResource);
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

	@javax.annotation.Resource(name = "com.glaf.matrix.resource.mapper.PageResourceMapper")
	public void setPageResourceMapper(PageResourceMapper pageResourceMapper) {
		this.pageResourceMapper = pageResourceMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
