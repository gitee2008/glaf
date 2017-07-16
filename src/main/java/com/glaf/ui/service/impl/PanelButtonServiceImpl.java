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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.util.JdbcUtils;
import com.glaf.ui.mapper.PanelButtonMapper;
import com.glaf.ui.model.PanelButton;
import com.glaf.ui.query.PanelButtonQuery;
import com.glaf.ui.service.PanelButtonService;

@Service("com.glaf.ui.service.panelButtonService")
@Transactional(readOnly = true) 
public class PanelButtonServiceImpl implements PanelButtonService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
 
        protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PanelButtonMapper panelButtonMapper;

	public PanelButtonServiceImpl() {

	}

	@Transactional
	public void deleteById(String id) {
	     if(id != null ){
		panelButtonMapper.deletePanelButtonById(id);
	     }
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
	    if(ids != null && !ids.isEmpty()){
		for(String id : ids){
		    panelButtonMapper.deletePanelButtonById(id);
		}
	    }
	}

	public int count(PanelButtonQuery query) {
		return panelButtonMapper.getPanelButtonCount(query);
	}

	public List<PanelButton> list(PanelButtonQuery query) {
		List<PanelButton> list = panelButtonMapper.getPanelButtons(query);
		return list;
	}

    /**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */     
	public int getPanelButtonCountByQueryCriteria(PanelButtonQuery query) {
		return panelButtonMapper.getPanelButtonCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PanelButton> getPanelButtonsByQueryCriteria(int start, int pageSize,
			PanelButtonQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PanelButton> rows = sqlSessionTemplate.selectList(
				"getPanelButtons", query, rowBounds);
		return rows;
	}


	public PanelButton getPanelButton(String id) {
	        if(id == null){
		    return null;
		}
		PanelButton panelButton = panelButtonMapper.getPanelButtonById(id);
		return panelButton;
	}

	@Transactional
	public void save(PanelButton panelButton) {
           if (StringUtils.isEmpty(panelButton.getId())) {
	        panelButton.setId(idGenerator.getNextId("UI_PANEL_BUTTON"));
		//panelButton.setCreateDate(new Date());
		//panelButton.setDeleteFlag(0);
		panelButtonMapper.insertPanelButton(panelButton);
	       } else {
		panelButtonMapper.updatePanelButton(panelButton);
	      }
	}


	@Transactional
	public void runBatch() {
		logger.debug("-------------------start run-------------------");
 		String sql = "  ";//要运行的SQL语句
		Connection connection = null;
		PreparedStatement psmt = null;
		try {
			connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
			psmt = connection.prepareStatement(sql);
			for (int i = 0; i < 2; i++) {
			    psmt.addBatch();
			}
			psmt.executeBatch();
			psmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("run batch error", ex);
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.close(psmt);
		}
		logger.debug("-------------------end run-------------------");
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}
	 
	@javax.annotation.Resource
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@javax.annotation.Resource(name = "com.glaf.ui.mapper.PanelButtonMapper")
	public void setPanelButtonMapper(PanelButtonMapper panelButtonMapper) {
		this.panelButtonMapper = panelButtonMapper;
	}

	@javax.annotation.Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

        @javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

		@Override
		public List<PanelButton> getPanelButtonByParentId(String pid) {
			return panelButtonMapper.getPanelButtonByParentId(pid);
		}

}
