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
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.TriphopathiaItemService;

@Service("com.glaf.heathcare.service.triphopathiaItemService")
@Transactional(readOnly = true)
public class TriphopathiaItemServiceImpl implements TriphopathiaItemService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TriphopathiaItemMapper triphopathiaItemMapper;

	protected GrowthStandardService growthStandardService;

	public TriphopathiaItemServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<TriphopathiaItem> list) {
		for (TriphopathiaItem triphopathiaItem : list) {
			if (triphopathiaItem.getId() == 0) {
				triphopathiaItem.setId(idGenerator.nextId("HEALTH_TRIPHOPATHIA_ITEM"));
			}
		}

		int batch_size = 100;
		List<TriphopathiaItem> rows = new ArrayList<TriphopathiaItem>(batch_size);

		for (TriphopathiaItem bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					triphopathiaItemMapper.bulkInsertTriphopathiaItem_oracle(rows);
				} else {
					triphopathiaItemMapper.bulkInsertTriphopathiaItem(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				triphopathiaItemMapper.bulkInsertTriphopathiaItem_oracle(rows);
			} else {
				triphopathiaItemMapper.bulkInsertTriphopathiaItem(rows);
			}
			rows.clear();
		}
	}

	public int count(TriphopathiaItemQuery query) {
		return triphopathiaItemMapper.getTriphopathiaItemCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			triphopathiaItemMapper.deleteTriphopathiaItemById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				triphopathiaItemMapper.deleteTriphopathiaItemById(id);
			}
		}
	}

	public TriphopathiaItem getTriphopathiaItem(Long id) {
		if (id == null) {
			return null;
		}
		TriphopathiaItem triphopathiaItem = triphopathiaItemMapper.getTriphopathiaItemById(id);
		return triphopathiaItem;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTriphopathiaItemCountByQueryCriteria(TriphopathiaItemQuery query) {
		return triphopathiaItemMapper.getTriphopathiaItemCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<TriphopathiaItem> getTriphopathiaItemsByQueryCriteria(int start, int pageSize, TriphopathiaItemQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<TriphopathiaItem> rows = sqlSessionTemplate.selectList("getTriphopathiaItems", query, rowBounds);
		return rows;
	}

	public List<TriphopathiaItem> list(TriphopathiaItemQuery query) {
		List<TriphopathiaItem> list = triphopathiaItemMapper.getTriphopathiaItems(query);
		return list;
	}

	@Transactional
	public void save(TriphopathiaItem triphopathiaItem) {
		GrowthStandardQuery query = new GrowthStandardQuery();
		query.ageOfTheMoon(triphopathiaItem.getAgeOfTheMoon());
		query.sex(triphopathiaItem.getSex());
		List<GrowthStandard> rows = growthStandardService.list(query);
		Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
		if (rows != null && !rows.isEmpty()) {
			for (GrowthStandard gs : rows) {
				gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
			}
		}
		if (triphopathiaItem.getHeight() > 0 && triphopathiaItem.getWeight() > 0) {
			double BMI = triphopathiaItem.getWeight() / (triphopathiaItem.getHeight() * triphopathiaItem.getHeight() / 10000D);
			triphopathiaItem.setBmi(BMI);
			GrowthStandard gs = gsMap.get(triphopathiaItem.getAgeOfTheMoon() + "_" + triphopathiaItem.getSex() + "_5");// BMI
			if (gs != null) {
				if (BMI > gs.getThreeDSDeviation()) {
					triphopathiaItem.setBmiIndex(3);
					triphopathiaItem.setBmiEvaluate("严重肥胖");
				} else if (BMI > gs.getTwoDSDeviation() && BMI <= gs.getThreeDSDeviation()) {
					triphopathiaItem.setBmiIndex(2);
					triphopathiaItem.setBmiEvaluate("肥胖");
				} else if (BMI > gs.getOneDSDeviation() && BMI <= gs.getTwoDSDeviation()) {
					triphopathiaItem.setBmiIndex(1);
					triphopathiaItem.setBmiEvaluate("超重");
				} else if (BMI < gs.getNegativeThreeDSDeviation()) {
					triphopathiaItem.setBmiIndex(-3);
					triphopathiaItem.setBmiEvaluate("消瘦");
				} else if (BMI < gs.getNegativeTwoDSDeviation() && BMI >= gs.getNegativeThreeDSDeviation()) {
					triphopathiaItem.setBmiIndex(-2);
					triphopathiaItem.setBmiEvaluate("较瘦");
				} else if (BMI < gs.getNegativeOneDSDeviation() && BMI >= gs.getNegativeTwoDSDeviation()) {
					triphopathiaItem.setBmiIndex(-1);
					triphopathiaItem.setBmiEvaluate("偏瘦");
				} else {
					triphopathiaItem.setBmiIndex(0);
					triphopathiaItem.setBmiEvaluate("正常");
				}
			}
		}

		if (triphopathiaItem.getHeight() > 0) {
			double index = triphopathiaItem.getHeight();
			GrowthStandard gs = gsMap.get(triphopathiaItem.getAgeOfTheMoon() + "_" + triphopathiaItem.getSex() + "_2");// 年龄别身高
			if (gs != null) {
				if (index > gs.getThreeDSDeviation()) {
					triphopathiaItem.setHeightLevel(3);
					triphopathiaItem.setHeightEvaluate("上");
				} else if (index > gs.getTwoDSDeviation() && index <= gs.getThreeDSDeviation()) {
					triphopathiaItem.setHeightLevel(2);
					triphopathiaItem.setHeightEvaluate("中上");
				} else if (index > gs.getOneDSDeviation() && index <= gs.getTwoDSDeviation()) {
					triphopathiaItem.setHeightLevel(1);
					triphopathiaItem.setHeightEvaluate("中+");
				} else if (index < gs.getNegativeThreeDSDeviation()) {
					triphopathiaItem.setHeightLevel(-3);
					triphopathiaItem.setHeightEvaluate("下");
				} else if (index < gs.getNegativeTwoDSDeviation() && index >= gs.getNegativeThreeDSDeviation()) {
					triphopathiaItem.setHeightLevel(-2);
					triphopathiaItem.setHeightEvaluate("中下");
				} else if (index < gs.getNegativeOneDSDeviation() && index >= gs.getNegativeTwoDSDeviation()) {
					triphopathiaItem.setHeightLevel(-1);
					triphopathiaItem.setHeightEvaluate("中-");
				} else {
					triphopathiaItem.setHeightLevel(0);
					triphopathiaItem.setHeightEvaluate("中");
				}
			}
		}

		if (triphopathiaItem.getWeight() > 0) {
			double index = triphopathiaItem.getWeight();
			GrowthStandard gs = gsMap.get(triphopathiaItem.getAgeOfTheMoon() + "_" + triphopathiaItem.getSex() + "_3");// 年龄别体重
			if (gs != null) {
				if (index > gs.getThreeDSDeviation()) {
					triphopathiaItem.setWeightLevel(3);
					triphopathiaItem.setWeightEvaluate("上");
				} else if (index > gs.getTwoDSDeviation() && index <= gs.getThreeDSDeviation()) {
					triphopathiaItem.setWeightLevel(2);
					triphopathiaItem.setWeightEvaluate("中上");
				} else if (index > gs.getOneDSDeviation() && index <= gs.getTwoDSDeviation()) {
					triphopathiaItem.setWeightLevel(1);
					triphopathiaItem.setWeightEvaluate("中+");
				} else if (index < gs.getNegativeThreeDSDeviation()) {
					triphopathiaItem.setWeightLevel(-3);
					triphopathiaItem.setWeightEvaluate("下");
				} else if (index < gs.getNegativeTwoDSDeviation() && index >= gs.getNegativeThreeDSDeviation()) {
					triphopathiaItem.setWeightLevel(-2);
					triphopathiaItem.setWeightEvaluate("中下");
				} else if (index < gs.getNegativeOneDSDeviation() && index >= gs.getNegativeTwoDSDeviation()) {
					triphopathiaItem.setWeightLevel(-1);
					triphopathiaItem.setWeightEvaluate("中-");
				} else {
					triphopathiaItem.setWeightLevel(0);
					triphopathiaItem.setWeightEvaluate("中");
				}
			}
		}

		if (triphopathiaItem.getId() == 0) {
			triphopathiaItem.setId(idGenerator.nextId("HEALTH_TRIPHOPATHIA_ITEM"));
			triphopathiaItem.setCreateTime(new Date());

			triphopathiaItemMapper.insertTriphopathiaItem(triphopathiaItem);
		} else {
			triphopathiaItemMapper.updateTriphopathiaItem(triphopathiaItem);
		}
	}

	@javax.annotation.Resource
	public void setEntityDAO(EntityDAO entityDAO) {
		this.entityDAO = entityDAO;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.growthStandardService")
	public void setGrowthStandardService(GrowthStandardService growthStandardService) {
		this.growthStandardService = growthStandardService;
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.TriphopathiaItemMapper")
	public void setTriphopathiaItemMapper(TriphopathiaItemMapper triphopathiaItemMapper) {
		this.triphopathiaItemMapper = triphopathiaItemMapper;
	}

}
