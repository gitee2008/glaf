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

package com.glaf.heathcare.service.impl;

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
import com.glaf.core.dao.*;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.util.*;

import com.glaf.heathcare.mapper.*;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;
import com.glaf.heathcare.service.*;

@Service("com.glaf.heathcare.service.goodsPriceAvgService")
@Transactional(readOnly = true)
public class GoodsPriceAvgServiceImpl implements GoodsPriceAvgService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected GoodsPriceAvgMapper goodsPriceAvgMapper;

	public GoodsPriceAvgServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<GoodsPriceAvg> list) {
		Date now = new Date();
		for (GoodsPriceAvg goodsPriceAvg : list) {
			if (goodsPriceAvg.getId() == 0) {
				goodsPriceAvg.setId(idGenerator.nextId("GOODS_PRICE_AVG"));
				goodsPriceAvg.setCreateTime(now);
			}
		}

		int batch_size = 50;
		List<GoodsPriceAvg> rows = new ArrayList<GoodsPriceAvg>(batch_size);

		for (GoodsPriceAvg bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					goodsPriceAvgMapper.bulkInsertGoodsPriceAvg_oracle(rows);
				} else {
					goodsPriceAvgMapper.bulkInsertGoodsPriceAvg(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				goodsPriceAvgMapper.bulkInsertGoodsPriceAvg_oracle(rows);
			} else {
				goodsPriceAvgMapper.bulkInsertGoodsPriceAvg(rows);
			}
			rows.clear();
		}
	}

	public int count(GoodsPriceAvgQuery query) {
		return goodsPriceAvgMapper.getGoodsPriceAvgCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			goodsPriceAvgMapper.deleteGoodsPriceAvgById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				goodsPriceAvgMapper.deleteGoodsPriceAvgById(id);
			}
		}
	}

	public GoodsPriceAvg getGoodsPriceAvg(Long id) {
		if (id == null) {
			return null;
		}
		GoodsPriceAvg goodsPriceAvg = goodsPriceAvgMapper.getGoodsPriceAvgById(id);
		return goodsPriceAvg;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getGoodsPriceAvgCountByQueryCriteria(GoodsPriceAvgQuery query) {
		return goodsPriceAvgMapper.getGoodsPriceAvgCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<GoodsPriceAvg> getGoodsPriceAvgsByQueryCriteria(int start, int pageSize, GoodsPriceAvgQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<GoodsPriceAvg> rows = sqlSessionTemplate.selectList("getGoodsPriceAvgs", query, rowBounds);
		return rows;
	}

	public List<GoodsPriceAvg> list(GoodsPriceAvgQuery query) {
		List<GoodsPriceAvg> list = goodsPriceAvgMapper.getGoodsPriceAvgs(query);
		return list;
	}

	@Transactional
	public void save(GoodsPriceAvg goodsPriceAvg) {
		if (goodsPriceAvg.getId() == 0) {
			goodsPriceAvg.setId(idGenerator.nextId("GOODS_PRICE_AVG"));
			goodsPriceAvg.setCreateTime(new Date());
			goodsPriceAvgMapper.insertGoodsPriceAvg(goodsPriceAvg);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.GoodsPriceAvgMapper")
	public void setGoodsPriceAvgMapper(GoodsPriceAvgMapper goodsPriceAvgMapper) {
		this.goodsPriceAvgMapper = goodsPriceAvgMapper;
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

}
