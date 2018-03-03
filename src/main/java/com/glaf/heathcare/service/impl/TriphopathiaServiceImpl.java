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
import com.glaf.heathcare.service.TriphopathiaService;

@Service("com.glaf.heathcare.service.triphopathiaService")
@Transactional(readOnly = true)
public class TriphopathiaServiceImpl implements TriphopathiaService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected TriphopathiaMapper triphopathiaMapper;

	protected GrowthStandardService growthStandardService;

	public TriphopathiaServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<Triphopathia> list) {
		for (Triphopathia triphopathia : list) {
			if (triphopathia.getId() == 0) {
				triphopathia.setId(idGenerator.nextId("HEALTH_TRIPHOPATHIA"));
			}
		}

		int batch_size = 100;
		List<Triphopathia> rows = new ArrayList<Triphopathia>(batch_size);

		for (Triphopathia bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					triphopathiaMapper.bulkInsertTriphopathia_oracle(rows);
				} else {
					triphopathiaMapper.bulkInsertTriphopathia(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				triphopathiaMapper.bulkInsertTriphopathia_oracle(rows);
			} else {
				triphopathiaMapper.bulkInsertTriphopathia(rows);
			}
			rows.clear();
		}
	}

	public int count(TriphopathiaQuery query) {
		return triphopathiaMapper.getTriphopathiaCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			triphopathiaMapper.deleteTriphopathiaById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				triphopathiaMapper.deleteTriphopathiaById(id);
			}
		}
	}

	public Triphopathia getTriphopathia(Long id) {
		if (id == null) {
			return null;
		}
		Triphopathia triphopathia = triphopathiaMapper.getTriphopathiaById(id);
		return triphopathia;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getTriphopathiaCountByQueryCriteria(TriphopathiaQuery query) {
		return triphopathiaMapper.getTriphopathiaCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<Triphopathia> getTriphopathiasByQueryCriteria(int start, int pageSize, TriphopathiaQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<Triphopathia> rows = sqlSessionTemplate.selectList("getTriphopathias", query, rowBounds);
		return rows;
	}

	public List<Triphopathia> list(TriphopathiaQuery query) {
		List<Triphopathia> list = triphopathiaMapper.getTriphopathias(query);
		return list;
	}

	@Transactional
	public void save(Triphopathia triphopathia) {
		GrowthStandardQuery query = new GrowthStandardQuery();
		query.ageOfTheMoon(triphopathia.getAgeOfTheMoon());
		query.sex(triphopathia.getSex());
		List<GrowthStandard> rows = growthStandardService.list(query);
		Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
		if (rows != null && !rows.isEmpty()) {
			for (GrowthStandard gs : rows) {
				gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
			}
		}
		if (triphopathia.getHeight() > 0 && triphopathia.getWeight() > 0) {
			double BMI = triphopathia.getWeight() / (triphopathia.getHeight() * triphopathia.getHeight() / 10000D);
			triphopathia.setBmi(BMI);
			GrowthStandard gs = gsMap.get(triphopathia.getAgeOfTheMoon() + "_" + triphopathia.getSex() + "_5");// BMI
			if (gs != null) {
				if (BMI > gs.getThreeDSDeviation()) {
					triphopathia.setBmiIndex(3);
					triphopathia.setBmiEvaluate("严重肥胖");
				} else if (BMI > gs.getTwoDSDeviation() && BMI <= gs.getThreeDSDeviation()) {
					triphopathia.setBmiIndex(2);
					triphopathia.setBmiEvaluate("肥胖");
				} else if (BMI > gs.getOneDSDeviation() && BMI <= gs.getTwoDSDeviation()) {
					triphopathia.setBmiIndex(1);
					triphopathia.setBmiEvaluate("超重");
				} else if (BMI < gs.getNegativeThreeDSDeviation()) {
					triphopathia.setBmiIndex(-3);
					triphopathia.setBmiEvaluate("消瘦");
				} else if (BMI < gs.getNegativeTwoDSDeviation() && BMI >= gs.getNegativeThreeDSDeviation()) {
					triphopathia.setBmiIndex(-2);
					triphopathia.setBmiEvaluate("较瘦");
				} else if (BMI < gs.getNegativeOneDSDeviation() && BMI >= gs.getNegativeTwoDSDeviation()) {
					triphopathia.setBmiIndex(-1);
					triphopathia.setBmiEvaluate("偏瘦");
				} else {
					triphopathia.setBmiIndex(0);
					triphopathia.setBmiEvaluate("正常");
				}
			}
		}

		if (triphopathia.getHeight() > 0) {
			double index = triphopathia.getHeight();
			GrowthStandard gs = gsMap.get(triphopathia.getAgeOfTheMoon() + "_" + triphopathia.getSex() + "_2");// 年龄别身高
			if (gs != null) {
				if (index > gs.getThreeDSDeviation()) {
					triphopathia.setHeightLevel(3);
					triphopathia.setHeightEvaluate("上");
				} else if (index > gs.getTwoDSDeviation() && index <= gs.getThreeDSDeviation()) {
					triphopathia.setHeightLevel(2);
					triphopathia.setHeightEvaluate("中上");
				} else if (index > gs.getOneDSDeviation() && index <= gs.getTwoDSDeviation()) {
					triphopathia.setHeightLevel(1);
					triphopathia.setHeightEvaluate("中+");
				} else if (index < gs.getNegativeThreeDSDeviation()) {
					triphopathia.setHeightLevel(-3);
					triphopathia.setHeightEvaluate("下");
				} else if (index < gs.getNegativeTwoDSDeviation() && index >= gs.getNegativeThreeDSDeviation()) {
					triphopathia.setHeightLevel(-2);
					triphopathia.setHeightEvaluate("中下");
				} else if (index < gs.getNegativeOneDSDeviation() && index >= gs.getNegativeTwoDSDeviation()) {
					triphopathia.setHeightLevel(-1);
					triphopathia.setHeightEvaluate("中-");
				} else {
					triphopathia.setHeightLevel(0);
					triphopathia.setHeightEvaluate("中");
				}
			}
		}

		if (triphopathia.getWeight() > 0) {
			double index = triphopathia.getWeight();
			GrowthStandard gs = gsMap.get(triphopathia.getAgeOfTheMoon() + "_" + triphopathia.getSex() + "_3");// 年龄别体重
			if (gs != null) {
				if (index > gs.getThreeDSDeviation()) {
					triphopathia.setWeightLevel(3);
					triphopathia.setWeightEvaluate("上");
				} else if (index > gs.getTwoDSDeviation() && index <= gs.getThreeDSDeviation()) {
					triphopathia.setWeightLevel(2);
					triphopathia.setWeightEvaluate("中上");
				} else if (index > gs.getOneDSDeviation() && index <= gs.getTwoDSDeviation()) {
					triphopathia.setWeightLevel(1);
					triphopathia.setWeightEvaluate("中+");
				} else if (index < gs.getNegativeThreeDSDeviation()) {
					triphopathia.setWeightLevel(-3);
					triphopathia.setWeightEvaluate("下");
				} else if (index < gs.getNegativeTwoDSDeviation() && index >= gs.getNegativeThreeDSDeviation()) {
					triphopathia.setWeightLevel(-2);
					triphopathia.setWeightEvaluate("中下");
				} else if (index < gs.getNegativeOneDSDeviation() && index >= gs.getNegativeTwoDSDeviation()) {
					triphopathia.setWeightLevel(-1);
					triphopathia.setWeightEvaluate("中-");
				} else {
					triphopathia.setWeightLevel(0);
					triphopathia.setWeightEvaluate("中");
				}
			}
		}

		if (triphopathia.getId() == 0) {
			triphopathia.setId(idGenerator.nextId("HEALTH_TRIPHOPATHIA"));
			triphopathia.setCreateTime(new Date());

			triphopathiaMapper.insertTriphopathia(triphopathia);
		} else {
			triphopathiaMapper.updateTriphopathia(triphopathia);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.TriphopathiaMapper")
	public void setTriphopathiaMapper(TriphopathiaMapper triphopathiaMapper) {
		this.triphopathiaMapper = triphopathiaMapper;
	}

}
