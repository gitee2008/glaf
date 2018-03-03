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

@Service("com.glaf.heathcare.service.medicalExaminationDefService")
@Transactional(readOnly = true)
public class MedicalExaminationDefServiceImpl implements MedicalExaminationDefService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected MedicalExaminationDefMapper medicalExaminationDefMapper;

	public MedicalExaminationDefServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<MedicalExaminationDef> list) {
		for (MedicalExaminationDef medicalExaminationDef : list) {
			if (medicalExaminationDef.getId() == 0) {
				medicalExaminationDef.setId(idGenerator.nextId("HEALTH_MEDICAL_EXAMINATION_DEF"));
				medicalExaminationDef.setCheckId(UUID32.getUUID());
				medicalExaminationDef.setCreateTime(new Date());
			}
		}

		int batch_size = 50;
		List<MedicalExaminationDef> rows = new ArrayList<MedicalExaminationDef>(batch_size);

		for (MedicalExaminationDef bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % 100 == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					medicalExaminationDefMapper.bulkInsertMedicalExaminationDef_oracle(rows);
				} else {
					medicalExaminationDefMapper.bulkInsertMedicalExaminationDef(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				medicalExaminationDefMapper.bulkInsertMedicalExaminationDef_oracle(rows);
			} else {
				medicalExaminationDefMapper.bulkInsertMedicalExaminationDef(rows);
			}
			rows.clear();
		}
	}

	public int count(MedicalExaminationDefQuery query) {
		return medicalExaminationDefMapper.getMedicalExaminationDefCount(query);
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			medicalExaminationDefMapper.deleteMedicalExaminationDefById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				medicalExaminationDefMapper.deleteMedicalExaminationDefById(id);
			}
		}
	}

	public MedicalExaminationDef getMedicalExaminationDef(Long id) {
		if (id == null) {
			return null;
		}
		MedicalExaminationDef medicalExaminationDef = medicalExaminationDefMapper.getMedicalExaminationDefById(id);
		return medicalExaminationDef;
	}

	public MedicalExaminationDef getMedicalExaminationDefByCheckId(String checkId) {
		if (checkId == null) {
			return null;
		}
		MedicalExaminationDef medicalExaminationDef = medicalExaminationDefMapper
				.getMedicalExaminationDefByCheckId(checkId);
		return medicalExaminationDef;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getMedicalExaminationDefCountByQueryCriteria(MedicalExaminationDefQuery query) {
		return medicalExaminationDefMapper.getMedicalExaminationDefCount(query);
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<MedicalExaminationDef> getMedicalExaminationDefsByQueryCriteria(int start, int pageSize,
			MedicalExaminationDefQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<MedicalExaminationDef> rows = sqlSessionTemplate.selectList("getMedicalExaminationDefs", query, rowBounds);
		return rows;
	}

	public List<MedicalExaminationDef> list(MedicalExaminationDefQuery query) {
		List<MedicalExaminationDef> list = medicalExaminationDefMapper.getMedicalExaminationDefs(query);
		return list;
	}

	@Transactional
	public void save(MedicalExaminationDef medicalExaminationDef) {
		if (medicalExaminationDef.getId() == 0) {
			medicalExaminationDef.setId(idGenerator.nextId("HEALTH_MEDICAL_EXAMINATION_DEF"));
			medicalExaminationDef.setCheckId(UUID32.getUUID());
			medicalExaminationDef.setCreateTime(new Date());
			// medicalExaminationDef.setDeleteFlag(0);
			medicalExaminationDefMapper.insertMedicalExaminationDef(medicalExaminationDef);
		} else {
			medicalExaminationDefMapper.updateMedicalExaminationDef(medicalExaminationDef);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.MedicalExaminationDefMapper")
	public void setMedicalExaminationDefMapper(MedicalExaminationDefMapper medicalExaminationDefMapper) {
		this.medicalExaminationDefMapper = medicalExaminationDefMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
