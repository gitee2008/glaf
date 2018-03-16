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

import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.base.DataFile;
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.matrix.data.mapper.*;
import com.glaf.matrix.data.domain.*;
import com.glaf.matrix.data.query.*;
import com.glaf.matrix.data.service.*;

@Service("statsHistoryService")
@Transactional(readOnly = true)
public class StatsHistoryServiceImpl implements StatsHistoryService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected StatsHistoryMapper statsHistoryMapper;

	protected IDataFileService dataFileService;

	public StatsHistoryServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<StatsHistory> list) {
		for (StatsHistory statsHistory : list) {
			if (StringUtils.isEmpty(statsHistory.getId())) {
				statsHistory.setId(idGenerator.getNextId("SYS_STATS_HISTORY"));
				statsHistory.setCreateTime(new Date());
				statsHistory.setUpdateTime(new Date());
			}
		}

		int batch_size = 50;
		List<StatsHistory> rows = new ArrayList<StatsHistory>(batch_size);

		for (StatsHistory bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					statsHistoryMapper.bulkInsertStatsHistory_oracle(rows);
				} else {
					statsHistoryMapper.bulkInsertStatsHistory(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				statsHistoryMapper.bulkInsertStatsHistory_oracle(rows);
			} else {
				statsHistoryMapper.bulkInsertStatsHistory(rows);
			}
			rows.clear();
		}
	}

	public int count(StatsHistoryQuery query) {
		return statsHistoryMapper.getStatsHistoryCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			statsHistoryMapper.deleteStatsHistoryById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				statsHistoryMapper.deleteStatsHistoryById(id);
			}
		}
	}

	public StatsHistory getStatsHistory(String id) {
		if (id == null) {
			return null;
		}
		StatsHistory statsHistory = statsHistoryMapper.getStatsHistoryById(id);
		return statsHistory;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getStatsHistoryCountByQueryCriteria(StatsHistoryQuery query) {
		return statsHistoryMapper.getStatsHistoryCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<StatsHistory> getStatsHistorysByQueryCriteria(int start, int pageSize, StatsHistoryQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<StatsHistory> rows = sqlSessionTemplate.selectList("getStatsHistorys", query, rowBounds);
		return rows;
	}

	public List<StatsHistory> list(StatsHistoryQuery query) {
		List<StatsHistory> list = statsHistoryMapper.getStatsHistorys(query);
		return list;
	}

	@Transactional
	public void save(StatsHistory statsHistory) {
		if (StringUtils.isEmpty(statsHistory.getId())) {
			statsHistory.setId(idGenerator.getNextId("SYS_STATS_HISTORY"));
			statsHistory.setCreateTime(new Date());
			statsHistory.setUpdateTime(new Date());
			statsHistoryMapper.insertStatsHistory(statsHistory);
		} else {
			StatsHistory model = this.getStatsHistory(statsHistory.getId());
			if (model == null) {
				statsHistory.setCreateTime(new Date());
				statsHistory.setUpdateTime(new Date());
				statsHistoryMapper.insertStatsHistory(statsHistory);
			} else {
				statsHistory.setUpdateTime(new Date());
				statsHistoryMapper.updateStatsHistory(statsHistory);
			}
		}

		if (statsHistory.getData() != null) {
			dataFileService.deleteById(statsHistory.getTenantId(), statsHistory.getId());
			DataFile dataFile = new DataFileEntity();
			dataFile.setId(statsHistory.getId());
			dataFile.setBusinessKey(statsHistory.getId());
			dataFile.setCreateBy(statsHistory.getCreateBy());
			dataFile.setCreateDate(statsHistory.getCreateTime());
			dataFile.setData(statsHistory.getData());
			dataFile.setFilename(statsHistory.getFilename());
			dataFile.setName(statsHistory.getName());
			dataFile.setServiceKey("stats_his");
			dataFile.setLastModified(System.currentTimeMillis());
			dataFile.setStatus(5);
			dataFile.setTenantId(statsHistory.getTenantId());
			dataFile.setType(statsHistory.getType());
			dataFileService.insertDataFile(statsHistory.getTenantId(), dataFile);
		}

	}

	@javax.annotation.Resource
	public void setDataFileService(IDataFileService dataFileService) {
		this.dataFileService = dataFileService;
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

	@javax.annotation.Resource(name = "com.glaf.matrix.data.mapper.StatsHistoryMapper")
	public void setStatsHistoryMapper(StatsHistoryMapper statsHistoryMapper) {
		this.statsHistoryMapper = statsHistoryMapper;
	}

}
