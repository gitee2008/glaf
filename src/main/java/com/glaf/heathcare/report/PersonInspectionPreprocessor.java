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
package com.glaf.heathcare.report;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonInspection;
import com.glaf.heathcare.domain.PersonInspectionCount;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.PersonInspectionQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonInspectionService;
import com.glaf.heathcare.service.PersonService;

public class PersonInspectionPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonInspectionService personInspectionService = ContextFactory
				.getBean("com.glaf.heathcare.service.personInspectionService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		PersonInspectionQuery query = new PersonInspectionQuery();
		query.tenantId(loginContext.getTenantId());
		query.locked(0);
		String gradeId = (String) params.get("gradeId");
		if (gradeId != null && gradeId != "") {
			query.gradeId(gradeId);
		}
		Date startTime = ParamUtils.getDate(params, "startTime");
		Date endTime = ParamUtils.getDate(params, "endTime");
		if (startTime != null) {
			query.createTimeGreaterThanOrEqual(startTime);
		}
		if (endTime != null) {
			query.createTimeLessThanOrEqual(endTime);
		}

		List<PersonInspection> personInspections = personInspectionService.list(query);

		GradeInfoQuery qx = new GradeInfoQuery();
		qx.tenantId(loginContext.getTenantId());
		qx.locked(0);
		List<GradeInfo> grades = gradeInfoService.list(qx);

		Map<String, GradeInfo> gradeMap = new HashMap<String, GradeInfo>();
		if (grades != null && !grades.isEmpty()) {
			for (GradeInfo g : grades) {
				gradeMap.put(g.getId(), g);
				if (StringUtils.equals(g.getId(), gradeId)) {
					params.put("grade", g);
				}
			}
		}

		PersonQuery query3 = new PersonQuery();
		query3.tenantId(loginContext.getTenantId());
		query3.locked(0);
		if (gradeId != null && gradeId != "") {
			query3.gradeId(gradeId);
		}
		List<Person> persons = personService.list(query3);

		Map<String, Person> personMap = new HashMap<String, Person>();
		if (persons != null && !persons.isEmpty()) {
			for (Person p : persons) {
				personMap.put(p.getId(), p);
			}
		}

		Map<String, PersonInspectionCount> cntMap = new HashMap<String, PersonInspectionCount>();

		if (personInspections != null && !personInspections.isEmpty()) {
			int totalPerson = persons.size();
			for (PersonInspection px : personInspections) {
				if ("am".equals(px.getSection())) {
					Person person = personMap.get(px.getPersonId());
					if (person != null) {
						if ("1".equals(person.getSex())) {
							px.setSex("男");
						} else {
							px.setSex("女");
						}
					}

					PersonInspectionCount pcnt = cntMap.get(String.valueOf(px.getDay()));
					if (pcnt == null) {
						pcnt = new PersonInspectionCount();
						pcnt.setTotal(totalPerson);
						pcnt.setDateString(DateUtils.getDate(DateUtils.toDate(String.valueOf(px.getDay()))));
					}
					pcnt.addChild1(px);
					pcnt.setAbnormal1(pcnt.getAbnormal1() + 1);
					cntMap.put(String.valueOf(px.getDay()), pcnt);
				}

				if ("day".equals(px.getSection())) {
					Person person = personMap.get(px.getPersonId());
					if (person != null) {
						if ("1".equals(person.getSex())) {
							px.setSex("男");
						} else {
							px.setSex("女");
						}
					}

					PersonInspectionCount pcnt2 = cntMap.get(String.valueOf(px.getDay()));
					if (pcnt2 == null) {
						pcnt2 = new PersonInspectionCount();
						pcnt2.setTotal(totalPerson);
						pcnt2.setDateString(DateUtils.getDate(DateUtils.toDate(String.valueOf(px.getDay()))));
					}
					pcnt2.addChild2(px);
					pcnt2.setAbnormal2(pcnt2.getAbnormal2() + 1);
					cntMap.put(String.valueOf(px.getDay()), pcnt2);
				}
			}
		}

		Collection<PersonInspectionCount> values = cntMap.values();
		if (values != null && !values.isEmpty()) {
			for (PersonInspectionCount pcnt : values) {
				pcnt.setNomal1(pcnt.getTotal() - pcnt.getAbnormal1());
				pcnt.setNomal2(pcnt.getTotal() - pcnt.getAbnormal2());
			}
		}

		params.put("rows", values);

	}

}
