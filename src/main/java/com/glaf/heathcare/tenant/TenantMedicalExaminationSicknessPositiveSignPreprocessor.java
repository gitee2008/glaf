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
package com.glaf.heathcare.tenant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.glaf.base.modules.sys.model.SysTenant;
import com.glaf.core.context.ContextFactory;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationSicknessPositiveSign;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.MedicalExaminationService;
import com.glaf.heathcare.service.PersonService;

public class TenantMedicalExaminationSicknessPositiveSignPreprocessor implements ITenantReportPreprocessor {

	@Override
	public void prepare(SysTenant tenant, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		PersonQuery query = new PersonQuery();
		query.tenantId(tenant.getTenantId());
		query.locked(0);
		List<Person> persons = personService.list(query);

		GradeInfoQuery qx = new GradeInfoQuery();
		qx.tenantId(tenant.getTenantId());
		qx.locked(0);
		List<GradeInfo> grades = gradeInfoService.list(qx);

		MedicalExaminationQuery q = new MedicalExaminationQuery();
		q.tenantId(tenant.getTenantId());

		if (params.get("gradeId") != null && StringUtils.isNotEmpty(ParamUtils.getString(params, "gradeId"))) {
			q.gradeId(ParamUtils.getString(params, "gradeId"));
		}

		if (params.get("checkId") != null && StringUtils.isNotEmpty(ParamUtils.getString(params, "checkId"))) {
			q.checkId(ParamUtils.getString(params, "checkId"));
		}

		Map<String, Person> personMap = new HashMap<String, Person>();
		if (persons != null && !persons.isEmpty()) {
			for (Person p : persons) {
				personMap.put(p.getId(), p);
			}
		}

		Map<String, MedicalExaminationSicknessPositiveSign> mmap = new LinkedHashMap<String, MedicalExaminationSicknessPositiveSign>();
		Map<String, GradeInfo> gradeMap = new HashMap<String, GradeInfo>();
		if (grades != null && !grades.isEmpty()) {
			for (GradeInfo g : grades) {
				gradeMap.put(g.getId(), g);
				MedicalExaminationSicknessPositiveSign sign = mmap.get(g.getId());
				if (sign == null) {
					sign = new MedicalExaminationSicknessPositiveSign();
					sign.setTenantId(tenant.getTenantId());
					sign.setGradeId(g.getId());
					sign.setGradeName(g.getName());
					if (persons != null && !persons.isEmpty()) {
						for (Person p : persons) {
							if (StringUtils.equals(p.getGradeId(), g.getId())) {
								sign.setPersonTotal(sign.getPersonTotal() + 1);
							}
						}
					}
					mmap.put(g.getId(), sign);
				}
			}
		}

		List<MedicalExamination> list = medicalExaminationService.list(q);
		if (list != null && !list.isEmpty()) {
			Person person = null;
			for (MedicalExamination me : list) {
				person = personMap.get(me.getPersonId());
				if (person != null) {
					MedicalExaminationSicknessPositiveSign sign = mmap.get(person.getGradeId());
					if (sign != null) {
						sign.setPersonCheckTotal(sign.getPersonCheckTotal() + 1);// 实检人数
						if (me.getSaprodontia() > 0) {
							sign.setSaprodontiaTotal(sign.getSaprodontiaTotal() + 1);// 龋齿人数
						}
						if (StringUtils.equals(me.getEyeLeft(), "T") || StringUtils.equals(me.getEyeRight(), "T")) {
							sign.setTrachomaTotal(sign.getTrachomaTotal() + 1);// 沙眼人数
						}
						if (StringUtils.equals(me.getEyeLeft(), "A") || StringUtils.equals(me.getEyeRight(), "A")) {
							sign.setAmblyopiaTotal(sign.getAmblyopiaTotal() + 1);// 弱视人数
						}
						if (StringUtils.equals(me.getHbsab(), "X")) {
							sign.setHbsabTotal(sign.getHbsabTotal() + 1);// 乙肝表面抗体阳性人数
						}
						if (StringUtils.equals(me.getSgpt(), "N")) {
							sign.setSgptTotal(sign.getSgptTotal() + 1);// 肝功超标人数
						}
						if (StringUtils.equals(me.getHvaigm(), "X")) {
							sign.setHvaigmTotal(sign.getHvaigmTotal() + 1);// HVAIgM阳性人数
						}
						if (StringUtils.isNotEmpty(me.getHemoglobin()) && StringUtils.isNumeric(me.getHemoglobin())) {
							int hb = Integer.parseInt(me.getHemoglobin());
							if (hb <= 90) {
								sign.setHemoglobin90Total(sign.getHemoglobin90Total() + 1);// Hb≤90克人数
							}
							if (hb <= 110) {
								sign.setHemoglobin110Total(sign.getHemoglobin110Total() + 1);// Hb≤110克人数
							}
						}
					}
				}
			}

			params.put("rows", mmap.values());
		}
	}

}
