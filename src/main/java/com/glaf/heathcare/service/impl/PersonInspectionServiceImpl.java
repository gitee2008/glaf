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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.PersonInspection;
import com.glaf.heathcare.mapper.PersonInspectionMapper;
import com.glaf.heathcare.query.PersonInspectionQuery;
import com.glaf.heathcare.service.PersonInspectionService;

@Service("com.glaf.heathcare.service.personInspectionService")
@Transactional(readOnly = true)
public class PersonInspectionServiceImpl implements PersonInspectionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonInspectionMapper personInspectionMapper;

	protected ITableDataService tableDataService;

	protected ITablePageService tablePageService;

	public PersonInspectionServiceImpl() {

	}

	@Transactional
	public void bulkInsert(List<PersonInspection> list) {
		for (PersonInspection personInspection : list) {
			if (StringUtils.isEmpty(personInspection.getId())) {
				personInspection.setId(idGenerator.getNextId("HEALTH_PERSON_INSPECTION"));
			}
		}

		int batch_size = 50;
		List<PersonInspection> rows = new ArrayList<PersonInspection>(batch_size);

		for (PersonInspection bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personInspectionMapper.bulkInsertPersonInspection_oracle(rows);
				} else {
					personInspectionMapper.bulkInsertPersonInspection(rows);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personInspectionMapper.bulkInsertPersonInspection_oracle(rows);
			} else {
				personInspectionMapper.bulkInsertPersonInspection(rows);
			}
			rows.clear();
		}
	}

	public int count(PersonInspectionQuery query) {
		return personInspectionMapper.getPersonInspectionCount(query);
	}

	@Transactional
	public void deleteById(String id) {
		if (id != null) {
			personInspectionMapper.deletePersonInspectionById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				personInspectionMapper.deletePersonInspectionById(id);
			}
		}
	}

	public List<Integer> getAbsenceDays(String tenantId, String gradeId, int year) {
		List<Integer> days = new ArrayList<Integer>();
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "  select E.DAY_ as \"day\" from HEALTH_PERSON_INSPECTION E where ( E.TENANTID_ = #{tenantId} ) and ( DAY_ / 10000 = #{year} ) ";
		params.put("tenantId", tenantId);
		params.put("year", year);

		if (StringUtils.isNotEmpty(gradeId)) {
			params.put("gradeId", gradeId);
			sql = sql + " and ( E.GRADEID_ = #{gradeId} ) ";
		}

		List<Map<String, Object>> datalist = tablePageService.getListData(sql, params);
		if (datalist != null && !datalist.isEmpty()) {
			for (Map<String, Object> dataMap : datalist) {
				days.add(ParamUtils.getInt(dataMap, "day"));
			}
		}

		return days;
	}

	public PersonInspection getPersonInspection(String id) {
		if (id == null) {
			return null;
		}
		PersonInspection personInspection = personInspectionMapper.getPersonInspectionById(id);
		return personInspection;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonInspectionCountByQueryCriteria(PersonInspectionQuery query) {
		return personInspectionMapper.getPersonInspectionCount(query);
	}

	/**
	 * 获取某个班级某天某个时段的缺勤记录
	 * 
	 * @param tenantId
	 *            租户编号
	 * @param gradeId
	 *            班级编号
	 * @param day
	 *            某天
	 * @param section
	 *            某时段
	 * @return
	 */
	public List<PersonInspection> getPersonInspections(String tenantId, String gradeId, int day, String section,
			String type, String inspectionFlag) {
		PersonInspectionQuery query = new PersonInspectionQuery();
		if (tenantId != null) {
			query.tenantId(tenantId);
		}
		query.gradeId(gradeId);
		query.day(day);
		query.section(section);
		query.type(type);
		query.inspectionFlag(inspectionFlag);
		List<PersonInspection> list = personInspectionMapper.getPersonInspections(query);
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonInspection> getPersonInspectionsByQueryCriteria(int start, int pageSize,
			PersonInspectionQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonInspection> rows = sqlSessionTemplate.selectList("getPersonInspections", query, rowBounds);
		return rows;
	}

	public List<PersonInspection> list(PersonInspectionQuery query) {
		List<PersonInspection> list = personInspectionMapper.getPersonInspections(query);
		return list;
	}

	@Transactional
	public void saveAll(String tenantId, String gradeId, int day, String section, String type, String inspectionFlag,
			String createBy, List<PersonInspection> personInspections) {
		TableModel table = new TableModel();
		table.setTableName("HEALTH_PERSON_INSPECTION");
		table.addStringColumn("GRADEID_", gradeId);
		table.addStringColumn("TENANTID_", tenantId);
		table.addIntegerColumn("DAY_", day);
		table.addStringColumn("SECTION_", section);
		table.addStringColumn("TYPE_", type);
		table.addStringColumn("INSPECTIONFLAG_", inspectionFlag);
		table.addStringColumn("CREATEBY_", createBy);
		tableDataService.deleteTableData(table);

		if (personInspections != null && !personInspections.isEmpty()) {
			for (PersonInspection inspection : personInspections) {
				inspection.setId(idGenerator.getNextId("HEALTH_PERSON_INSPECTION"));
				inspection.setTenantId(tenantId);
				inspection.setGradeId(gradeId);
				inspection.setDay(day);
				inspection.setSection(section);
				inspection.setType(type);
				inspection.setInspectionFlag(inspectionFlag);
				inspection.setCreateTime(new Date());
				inspection.setCreateBy(createBy);
				personInspectionMapper.insertPersonInspection(inspection);
			}
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonInspectionMapper")
	public void setPersonInspectionMapper(PersonInspectionMapper personInspectionMapper) {
		this.personInspectionMapper = personInspectionMapper;
	}

	@javax.annotation.Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@javax.annotation.Resource
	public void setTableDataService(ITableDataService tableDataService) {
		this.tableDataService = tableDataService;
	}

	@javax.annotation.Resource
	public void setTablePageService(ITablePageService tablePageService) {
		this.tablePageService = tablePageService;
	}

}
