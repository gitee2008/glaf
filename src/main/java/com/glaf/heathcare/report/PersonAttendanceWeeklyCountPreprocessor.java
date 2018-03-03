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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;

import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonAbsence;

import com.glaf.heathcare.domain.PersonAttendanceCount;
import com.glaf.heathcare.query.PersonAbsenceQuery;
import com.glaf.heathcare.service.PersonAbsenceService;

public class PersonAttendanceWeeklyCountPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		String tenantId = loginContext.getTenantId();
		String gradeId = ParamUtils.getString(params, "gradeId");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		PersonAbsenceService personAbsenceService = ContextFactory
				.getBean("com.glaf.heathcare.service.personAbsenceService");
		List<Person> list = personService.getPersons(gradeId);
		if (list != null && !list.isEmpty()) {
			int total = list.size();
			PersonAbsenceQuery query = new PersonAbsenceQuery();
			query.tenantId(tenantId);
			query.gradeId(gradeId);

			if (year > 0) {
				query.year(year);
			}

			if (month > 0) {
				query.month(month);
			}

			Date startTime = ParamUtils.getDate(params, "startTime");
			Date endTime = ParamUtils.getDate(params, "endTime");
			if (startTime != null) {
				query.dayGreaterThanOrEqual(DateUtils.getYearMonthDay(startTime));
			}
			if (endTime != null) {
				query.dayLessThanOrEqual(DateUtils.getYearMonthDay(endTime));
			}

			List<PersonAbsence> absences = personAbsenceService.list(query);
			if (absences != null && !absences.isEmpty()) {
				Map<Integer, PersonAttendanceCount> cntMap = new HashMap<Integer, PersonAttendanceCount>();
				for (PersonAbsence a : absences) {
					if (StringUtils.equals(a.getSection(), "day")) {
						PersonAttendanceCount cnt = cntMap.get(a.getDay());
						if (cnt == null) {
							cnt = new PersonAttendanceCount();
							cnt.setGradeId(gradeId);
							cnt.setTotalPerson(total);
						}
						int status = a.getStatus();
						switch (status) {
						case 1:// 正常出勤
							cnt.setActualPerson(cnt.getActualPerson() + 1);
							break;
						case 2:// 病假
							cnt.setSicknessPerson(cnt.getSicknessPerson() + 1);
							break;
						case 3:// 事假
							cnt.setActualPerson(cnt.getAffairPerson() + 1);
							break;
						default:
							cnt.setOtherPerson(cnt.getOtherPerson() + 1);
							break;
						}
						cntMap.put(a.getDay(), cnt);
					}
				}

				Set<Entry<Integer, PersonAttendanceCount>> entrySet = cntMap.entrySet();
				for (Entry<Integer, PersonAttendanceCount> entry : entrySet) {
					Integer key = entry.getKey();
					PersonAttendanceCount m = entry.getValue();
					m.setActualPercent(m.getActualPerson() * 1.0D / total * 1.0D);
					m.setSicknessPercent(m.getSicknessPerson() * 1.0D / total * 1.0D);
					m.setAffairPercent(m.getAffairPerson() * 1.0D / total * 1.0D);
					m.setOtherPercent(m.getOtherPerson() * 1.0D / total * 1.0D);
					m.setDate(DateUtils.toDate(String.valueOf(key)));
				}
				params.put("rows", cntMap.values());
				params.put("startDate", DateUtils.getDateTime("yyyy年MM月dd日", startTime));
				params.put("endDate", DateUtils.getDateTime("yyyy年MM月dd日", endTime));
			}
		}
	}

}
