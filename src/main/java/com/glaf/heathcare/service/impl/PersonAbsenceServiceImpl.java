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

import com.glaf.core.base.ListModel;
import com.glaf.core.base.TableModel;
import com.glaf.core.dao.EntityDAO;
import com.glaf.core.id.IdGenerator;
import com.glaf.core.jdbc.DBConnectionFactory;
import com.glaf.core.security.IdentityFactory;
import com.glaf.core.service.ITableDataService;
import com.glaf.core.service.ITablePageService;
import com.glaf.core.util.DBUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.PersonAbsence;
import com.glaf.heathcare.mapper.PersonAbsenceMapper;
import com.glaf.heathcare.query.PersonAbsenceQuery;
import com.glaf.heathcare.service.PersonAbsenceService;

@Service("com.glaf.heathcare.service.personAbsenceService")
@Transactional(readOnly = true)
public class PersonAbsenceServiceImpl implements PersonAbsenceService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected EntityDAO entityDAO;

	protected IdGenerator idGenerator;

	protected JdbcTemplate jdbcTemplate;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected PersonAbsenceMapper personAbsenceMapper;

	protected ITableDataService tableDataService;

	protected ITablePageService tablePageService;

	public PersonAbsenceServiceImpl() {

	}

	@Transactional
	public void bulkInsert(String tenantId, List<PersonAbsence> list) {
		for (PersonAbsence personAbsence : list) {
			if (StringUtils.isEmpty(personAbsence.getId())) {
				personAbsence.setId(idGenerator.getNextId("HEALTH_PERSON_ABSENCE"));
			}
		}

		int batch_size = 50;
		List<PersonAbsence> rows = new ArrayList<PersonAbsence>(batch_size);

		for (PersonAbsence bean : list) {
			rows.add(bean);
			if (rows.size() > 0 && rows.size() % batch_size == 0) {
				if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
					personAbsenceMapper.bulkInsertPersonAbsence_oracle(rows);
				} else {
					ListModel listModel = new ListModel();
					if (StringUtils.isNotEmpty(tenantId)) {
						listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
					}
					listModel.setList(rows);
					personAbsenceMapper.bulkInsertPersonAbsence(listModel);
				}
				rows.clear();
			}
		}

		if (rows.size() > 0) {
			if (StringUtils.equals(DBUtils.ORACLE, DBConnectionFactory.getDatabaseType())) {
				personAbsenceMapper.bulkInsertPersonAbsence_oracle(rows);
			} else {
				ListModel listModel = new ListModel();
				if (StringUtils.isNotEmpty(tenantId)) {
					listModel.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(tenantId)));
				}
				listModel.setList(rows);
				personAbsenceMapper.bulkInsertPersonAbsence(listModel);
			}
			rows.clear();
		}
	}

	public int count(PersonAbsenceQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return personAbsenceMapper.getPersonAbsenceCount(query);
	}

	@Transactional
	public void deleteById(String tenantId, String id) {
		if (id != null) {
			PersonAbsenceQuery query = new PersonAbsenceQuery();
			query.setId(id);
			query.tenantId(tenantId);
			if (StringUtils.isNotEmpty(query.getTenantId())) {
				query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
			}
			personAbsenceMapper.deletePersonAbsenceById(query);
		}
	}

	@Transactional
	public void deleteByIds(String tenantId, List<String> ids) {
		if (ids != null && !ids.isEmpty()) {
			for (String id : ids) {
				PersonAbsenceQuery query = new PersonAbsenceQuery();
				query.setId(id);
				query.tenantId(tenantId);
				if (StringUtils.isNotEmpty(query.getTenantId())) {
					query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
				}
				personAbsenceMapper.deletePersonAbsenceById(query);
			}
		}
	}

	public List<Integer> getAbsenceDays(String tenantId, String gradeId, int year) {
		String tableSuffix = String.valueOf(IdentityFactory.getTenantHash(tenantId));
		List<Integer> days = new ArrayList<Integer>();
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "  select E.DAY_ as \"day\" from HEALTH_PERSON_ABSENCE" + tableSuffix
				+ " E where ( E.TENANTID_ = #{tenantId} ) and ( DAY_ / 10000 = #{year} ) ";
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

	public PersonAbsence getPersonAbsence(String tenantId, String id) {
		if (id == null) {
			return null;
		}
		PersonAbsenceQuery query = new PersonAbsenceQuery();
		query.setId(id);
		query.tenantId(tenantId);
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		PersonAbsence personAbsence = personAbsenceMapper.getPersonAbsenceById(query);
		return personAbsence;
	}

	/**
	 * 根据查询参数获取记录总数
	 * 
	 * @return
	 */
	public int getPersonAbsenceCountByQueryCriteria(PersonAbsenceQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		return personAbsenceMapper.getPersonAbsenceCount(query);
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
	public List<PersonAbsence> getPersonAbsences(String tenantId, String gradeId, int day, String section) {
		PersonAbsenceQuery query = new PersonAbsenceQuery();
		if (tenantId != null) {
			query.tenantId(tenantId);
		}
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		query.gradeId(gradeId);
		query.day(day);
		query.section(section);
		List<PersonAbsence> list = personAbsenceMapper.getPersonAbsences(query);
		return list;
	}

	/**
	 * 根据查询参数获取一页的数据
	 * 
	 * @return
	 */
	public List<PersonAbsence> getPersonAbsencesByQueryCriteria(int start, int pageSize, PersonAbsenceQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<PersonAbsence> rows = sqlSessionTemplate.selectList("getPersonAbsences", query, rowBounds);
		return rows;
	}

	public List<PersonAbsence> list(PersonAbsenceQuery query) {
		if (StringUtils.isNotEmpty(query.getTenantId())) {
			query.setTableSuffix(String.valueOf(IdentityFactory.getTenantHash(query.getTenantId())));
		}
		List<PersonAbsence> list = personAbsenceMapper.getPersonAbsences(query);
		return list;
	}

	@Transactional
	public void saveAll(String tenantId, String gradeId, int day, String section, String createBy,
			List<PersonAbsence> personAbsences) {
		String tableSuffix = String.valueOf(IdentityFactory.getTenantHash(tenantId));
		TableModel table = new TableModel();
		table.setTableName("HEALTH_PERSON_ABSENCE" + tableSuffix);
		table.addStringColumn("GRADEID_", gradeId);
		table.addStringColumn("TENANTID_", tenantId);
		table.addIntegerColumn("DAY_", day);
		table.addStringColumn("SECTION_", section);
		table.addStringColumn("CREATEBY_", createBy);
		tableDataService.deleteTableData(table);

		if (personAbsences != null && !personAbsences.isEmpty()) {
			for (PersonAbsence absence : personAbsences) {
				absence.setId(idGenerator.getNextId("HEALTH_PERSON_ABSENCE"));
				absence.setTenantId(tenantId);
				absence.setGradeId(gradeId);
				absence.setDay(day);
				absence.setSection(section);
				absence.setCreateBy(createBy);
				absence.setCreateTime(new Date());
				absence.setTableSuffix(tableSuffix);
				personAbsenceMapper.insertPersonAbsence(absence);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.mapper.PersonAbsenceMapper")
	public void setPersonAbsenceMapper(PersonAbsenceMapper personAbsenceMapper) {
		this.personAbsenceMapper = personAbsenceMapper;
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
