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

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.InfectiousDiseaseCount;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonSickness;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.query.PersonSicknessQuery;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.PersonSicknessService;

public class InfectiousDiseaseCountPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		PersonSicknessService personSicknessService = ContextFactory
				.getBean("com.glaf.heathcare.service.personSicknessService");
		PersonQuery query = new PersonQuery();
		query.tenantId(loginContext.getTenantId());
		query.locked(0);
		List<Person> persons = personService.list(query);

		PersonSicknessQuery q = new PersonSicknessQuery();
		q.tenantId(loginContext.getTenantId());
		q.infectiousFlag("Y");
		q.year(year);
		if (month > 0) {
			q.month(month);
		}

		Map<Integer, InfectiousDiseaseCount> cntMap = new HashMap<Integer, InfectiousDiseaseCount>();
		List<PersonSickness> list = personSicknessService.list(q);
		if (list != null && !list.isEmpty()) {
			for (PersonSickness sick : list) {
				InfectiousDiseaseCount model = cntMap.get(sick.getMonth());
				if (model == null) {
					model = new InfectiousDiseaseCount();
					model.setTenantId(loginContext.getTenantId());
					model.setGradeId(sick.getGradeId());
					model.setPersonTotal(persons.size());
					model.setYear(year);
					model.setMonth(sick.getMonth());
				}
				model.setInfectiousCount(model.getInfectiousCount() + 1);
				if (sick.getInfectiousDisease() != null) {
					switch (sick.getInfectiousDisease()) {
					case "hand-foot-and-mouth":
						model.setItem1(model.getItem1() + 1);
						break;
					case "chicken-pox":
						model.setItem2(model.getItem2() + 1);
					case "mumps":
						model.setItem3(model.getItem3() + 1);
						break;
					case "scarlatina":
						model.setItem4(model.getItem4() + 1);
						break;
					case "acute-hemorrhagic-conjunctivitis":
						model.setItem5(model.getItem5() + 1);
						break;
					case "dysentery":
						model.setItem6(model.getItem6() + 1);
						break;
					case "measles":
						model.setItem7(model.getItem7() + 1);
						break;
					case "rubella":
						model.setItem8(model.getItem8() + 1);
						break;
					case "catarrhal-jaundice":
						model.setItem9(model.getItem9() + 1);
						break;
					default:
						model.setItem10(model.getItem10() + 1);
						break;
					}

					cntMap.put(sick.getMonth(), model);
				} else {
					model.setItem10(model.getItem10() + 1);
					cntMap.put(sick.getMonth(), model);
				}
			}

			params.put("rows", cntMap.values());
		}
	}

}
