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

import org.apache.commons.codec.binary.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonService;

public class PersonExportPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");

		PersonQuery query = new PersonQuery();
		query.tenantId(loginContext.getTenantId());
		String gradeId = (String) params.get("gradeId");
		if (gradeId != null && gradeId != "") {
			query.gradeId(gradeId);
		}
		query.locked(0);
		List<Person> persons = personService.list(query);

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

		if (persons != null && !persons.isEmpty()) {
			for (Person p : persons) {
				if ("1".equals(p.getSex())) {
					p.setSex("男");
				} else {
					p.setSex("女");
				}
				p.setGradeName(gradeMap.get(p.getGradeId()) != null ? gradeMap.get(p.getGradeId()).getName() : "");
			}
		}

		params.put("rows", persons);

	}

}
