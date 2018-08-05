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
import com.glaf.core.util.UUID32;
import com.glaf.matrix.export.domain.DataExport;
import com.glaf.matrix.export.domain.DataExportItem;
import com.glaf.matrix.export.mapper.DataExportMapper;
import com.glaf.matrix.export.mapper.DataExportItemMapper;
import com.glaf.matrix.export.query.DataExportQuery;
import com.glaf.matrix.export.query.DataExportItemQuery;

@Service("com.glaf.matrix.export.service.dataExportService")
@Transactional(readOnly = true)
public class DataExportServiceImpl implements DataExportService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected DataExportMapper dataExportMapper;

	protected DataExportItemMapper dataExportItemMapper;

	public DataExportServiceImpl() {

	}

	public int count(DataExportQuery query) {
		return dataExportMapper.getDataExportCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			dataExportMapper.deleteDataExportById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				dataExportMapper.deleteDataExportById(id);
			}
		}
	}

	public DataExport getDataExport(String expId) {
		if (expId == null) {
			return null;
		}
		DataExport dataExport = dataExportMapper.getDataExportById(expId);
		if (dataExport != null) {
			DataExportItemQuery query = new DataExportItemQuery();
			query.expId(expId);
			List<DataExportItem> items = dataExportItemMapper.getDataExportItems(query);
			dataExport.setItems(items);
		}
		return dataExport;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getDataExportCountByQueryCriteria(DataExportQuery query) {
		return dataExportMapper.getDataExportCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<DataExport> getDataExportsByQueryCriteria(int start, int pageSize, DataExportQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<DataExport> rows = sqlSessionTemplate.selectList("getDataExports", query, rowBounds);
		return rows;
	}

	public List<DataExport> list(DataExportQuery query) {
		List<DataExport> list = dataExportMapper.getDataExports(query);
		return list;
	}

	@Transactional
	public void save(DataExport dataExport) {
		if (dataExport.getId() == null) {
			dataExport.setId(UUID32.getUUID());
			dataExport.setCreateTime(new Date());
			if (StringUtils.isEmpty(dataExport.getDeploymentId())) {
				dataExport.setDeploymentId(UUID32.getUUID());
			}
			dataExportMapper.insertDataExport(dataExport);
		} else {
			dataExportMapper.updateDataExport(dataExport);
		}
	}

	@Transactional
	public String saveAs(String expId, String createBy) {
		DataExport model = this.getDataExport(expId);
		if (model != null) {
			model.setId(UUID32.getUUID());
			model.setCreateTime(new Date());
			model.setCreateBy(createBy);
			dataExportMapper.insertDataExport(model);

			if (model.getItems() != null && !model.getItems().isEmpty()) {
				for (DataExportItem item : model.getItems()) {
					item.setId(UUID32.getUUID());
					item.setCreateTime(new Date());
					item.setCreateBy(createBy);
					item.setExpId(model.getId());
					dataExportItemMapper.insertDataExportItem(item);
				}
			}

			return model.getId();
		}
		return null;
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

	@javax.annotation.Resource(name = "com.glaf.matrix.export.mapper.DataExportMapper")
	public void setDataExportMapper(DataExportMapper dataExportMapper) {
		this.dataExportMapper = dataExportMapper;
	}

	@javax.annotation.Resource(name = "com.glaf.matrix.export.mapper.DataExportItemMapper")
	public void setDataExportItemMapper(DataExportItemMapper dataExportItemMapper) {
		this.dataExportItemMapper = dataExportItemMapper;
	}

}
