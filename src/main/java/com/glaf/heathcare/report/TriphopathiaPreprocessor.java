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

import java.util.List;
import java.util.Map;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;

import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.Triphopathia;
import com.glaf.heathcare.domain.TriphopathiaItem;
import com.glaf.heathcare.query.TriphopathiaItemQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.TriphopathiaItemService;
import com.glaf.heathcare.service.TriphopathiaService;

public class TriphopathiaPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		TriphopathiaService triphopathiaService = ContextFactory
				.getBean("com.glaf.heathcare.service.triphopathiaService");
		TriphopathiaItemService triphopathiaItemService = ContextFactory
				.getBean("com.glaf.heathcare.service.triphopathiaItemService");
		Person person = personService.getPerson(ParamUtils.getString(params, "personId"));
		params.put("person", person);

		GradeInfo gradeInfo = gradeInfoService.getGradeInfo(person.getGradeId());
		if (gradeInfo != null) {
			params.put("gradeInfo", gradeInfo);
			params.put("gradeName", gradeInfo.getName());
		}

		if ("1".equals(person.getSex())) {
			params.put("sex", "男");
		} else {
			params.put("sex", "女");
		}

		Triphopathia triphopathia = triphopathiaService.getTriphopathia(ParamUtils.getLong(params, "triphopathiaId"));

		if (triphopathia != null) {
			params.put("triphopathia", triphopathia);
			if (triphopathia.getDiscoverDate() != null) {
				params.put("discoverDate", DateUtils.getDate(triphopathia.getDiscoverDate()));
			}
			TriphopathiaItemQuery q = new TriphopathiaItemQuery();
			q.triphopathiaId(triphopathia.getId());
			q.personId(person.getId());
			q.setOrderBy(" E.CHECKDATE_ asc  ");
			List<TriphopathiaItem> list = triphopathiaItemService.list(q);
			if (list != null && !list.isEmpty()) {
				params.put("rows", list);
			}
		}
	}

}
