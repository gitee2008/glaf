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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.core.util.Tools;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonAbsence;
import com.glaf.heathcare.domain.PersonAttendance;
import com.glaf.heathcare.query.PersonAbsenceQuery;
import com.glaf.heathcare.service.PersonAbsenceService;

public class PersonAttendancePreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		String tenantId = loginContext.getTenantId();
		String gradeId = ParamUtils.getString(params, "gradeId");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		PersonAbsenceService personAbsenceService = ContextFactory
				.getBean("com.glaf.heathcare.service.personAbsenceService");
		List<Person> list = personService.getPersons(gradeId);
		if (list != null && !list.isEmpty()) {
			Map<String, PersonAttendance> personMap = new HashMap<String, PersonAttendance>();
			for (Person person : list) {
				PersonAttendance pa = new PersonAttendance();
				pa.setGradeId(person.getGradeId());
				pa.setPersonId(person.getId());
				pa.setName(person.getName());
				personMap.put(person.getId(), pa);
			}

			Map<String, Object> dataMap = new HashMap<String, Object>();
			Map<String, Integer> dataMap2 = new HashMap<String, Integer>();
			Map<String, Integer> dataMap3 = new HashMap<String, Integer>();
			Map<String, Integer> dataMap4 = new HashMap<String, Integer>();
			Map<String, Integer> dataMap5 = new HashMap<String, Integer>();
			PersonAbsenceQuery query = new PersonAbsenceQuery();
			query.tenantId(tenantId);
			query.gradeId(gradeId);
			query.year(year);
			query.month(month);
			List<PersonAbsence> absences = personAbsenceService.list(query);
			if (absences != null && !absences.isEmpty()) {
				for (PersonAbsence a : absences) {
					PersonAttendance pa = personMap.get(a.getPersonId());
					dataMap.clear();
					if (StringUtils.equals(a.getSection(), "day")) {
						int day = a.getDay();
						day = day % 100;
						String str = "√";
						int status = a.getStatus();
						switch (status) {
						case 2:
							str = "〇";
							Integer i2 = dataMap2.get(a.getPersonId());
							if (i2 == null) {
								i2 = new Integer(0);
							}
							i2 = i2 + 1;
							dataMap2.put(a.getPersonId(), i2);
							break;
						case 3:
							str = "△";
							Integer i3 = dataMap3.get(a.getPersonId());
							if (i3 == null) {
								i3 = new Integer(0);
							}
							i3 = i3 + 1;
							dataMap3.put(a.getPersonId(), i3);
							break;
						case 4:
							str = "×";
							Integer i4 = dataMap4.get(a.getPersonId());
							if (i4 == null) {
								i4 = new Integer(0);
							}
							i4 = i4 + 1;
							dataMap4.put(a.getPersonId(), i4);
							break;
						default:
							Integer i5 = dataMap5.get(a.getPersonId());
							if (i5 == null) {
								i5 = new Integer(0);
							}
							i5 = i5 + 1;
							dataMap5.put(a.getPersonId(), i5);
							break;
						}
						dataMap.put("status" + day, str);
					}
					Tools.populate(pa, dataMap);
				}
			}

			Set<Entry<String, PersonAttendance>> entrySet = personMap.entrySet();
			for (Entry<String, PersonAttendance> entry : entrySet) {
				String key = entry.getKey();
				PersonAttendance person = entry.getValue();
				Integer days2 = dataMap2.get(key);
				if (days2 != null) {
					person.setDays2(days2);
				}

				Integer days3 = dataMap3.get(key);
				if (days3 != null) {
					person.setDays3(days3);
				}

				Integer days4 = dataMap4.get(key);
				if (days4 != null) {
					person.setDays4(days4);
				}

				Integer days5 = dataMap5.get(key);
				if (days5 != null) {
					person.setDays1(days5);
				}
			}

			params.put("persons", personMap.values());
		}
	}

}
