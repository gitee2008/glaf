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

package com.glaf.matrix.export.service;

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
import com.glaf.core.util.UUID32;
import com.glaf.matrix.export.domain.XmlExportItem;
import com.glaf.matrix.export.factory.XmlExportFactory;
import com.glaf.matrix.export.mapper.XmlExportItemMapper;
import com.glaf.matrix.export.query.XmlExportItemQuery;

@Service("com.glaf.matrix.export.service.xmlExportItemService")
@Transactional(readOnly = true)
public class XmlExportItemServiceImpl implements XmlExportItemService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected XmlExportItemMapper xmlExportItemMapper;

	public XmlExportItemServiceImpl() {

	}

	public int count(XmlExportItemQuery query) {
		return xmlExportItemMapper.getXmlExportItemCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			xmlExportItemMapper.deleteXmlExportItemById(id);
			XmlExportFactory.clearAll();
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			XmlExportFactory.clearAll();
			for (String id : ids) {
				xmlExportItemMapper.deleteXmlExportItemById(id);
			}
		}
	}

	public XmlExportItem getXmlExportItem(String id) {
		if (id == null) {
			return null;
		}
		XmlExportItem xmlExportItem = xmlExportItemMapper.getXmlExportItemById(id);
		return xmlExportItem;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getXmlExportItemCountByQueryCriteria(XmlExportItemQuery query) {
		return xmlExportItemMapper.getXmlExportItemCount(query);
	}

	@Override
	public List<XmlExportItem> getXmlExportItemsByExpId(String expId) {
		return xmlExportItemMapper.getXmlExportItemsByExpId(expId);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<XmlExportItem> getXmlExportItemsByQueryCriteria(int start, int pageSize, XmlExportItemQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<XmlExportItem> rows = sqlSessionTemplate.selectList("getXmlExportItems", query, rowBounds);
		return rows;
	}

	public List<XmlExportItem> list(XmlExportItemQuery query) {
		List<XmlExportItem> list = xmlExportItemMapper.getXmlExportItems(query);
		return list;
	}

	@Transactional
	public void save(XmlExportItem xmlExportItem) {
		if (xmlExportItem.getId() == null) {
			xmlExportItem.setId(UUID32.getUUID());
			xmlExportItem.setCreateTime(new Date());

			xmlExportItemMapper.insertXmlExportItem(xmlExportItem);
		} else {
			xmlExportItemMapper.updateXmlExportItem(xmlExportItem);
		}

		XmlExportFactory.clearAll();
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

	@javax.annotation.Resource(name = "com.glaf.matrix.export.mapper.XmlExportItemMapper")
	public void setXmlExportItemMapper(XmlExportItemMapper xmlExportItemMapper) {
		this.xmlExportItemMapper = xmlExportItemMapper;
	}

}
