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

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

public class GrowthPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
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

		MedicalExaminationQuery q = new MedicalExaminationQuery();
		q.personId(person.getId());
		q.setOrderBy(" E.CHECKDATE_ asc  ");
		List<MedicalExamination> list = medicalExaminationService.list(q);
		if (list != null && !list.isEmpty()) {
			int i = 0;
			int index = 1;
			int len = list.size();
			for (MedicalExamination me : list) {
				i++;
				if (len > 6 && (len - i) >= 6) {
					continue;
				}
				if (StringUtils.equals(me.getEyeLeft(), "T") || StringUtils.equals(me.getEyeRight(), "T")) {
					me.setEye("沙眼");
				}
				if (StringUtils.equals(me.getEyeLeft(), "A") || StringUtils.equals(me.getEyeRight(), "A")) {
					me.setEye("弱视");
				}
				if (StringUtils.equals(me.getEarLeft(), "N") || StringUtils.equals(me.getEarRight(), "N")) {
					me.setEar("异常");
				}
				if (StringUtils.equals(me.getHbsab(), "X")) {
					me.setHbsab("阳性");
				}
				if (StringUtils.equals(me.getHvaigm(), "X")) {
					me.setHvaigm("阳性");
				}
				if (StringUtils.equals(me.getSgpt(), "N")) {
					me.setSgpt("异常");
				}
				if (StringUtils.isNotEmpty(me.getHemoglobin()) && StringUtils.isNumeric(me.getHemoglobin())) {

				}
				if (person.getBirthday() != null && me.getCheckDate() != null) {
					int days = DateUtils.getDaysBetween(person.getBirthday(), me.getCheckDate());
					int age = (int) Math.ceil(days / 365D);
					me.setAge(age);
				}
				params.put("row" + index, me);
				index++;
			}
			// params.put("rows", list);
		}
	}

}
