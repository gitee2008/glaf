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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.context.ContextFactory;
import com.glaf.core.security.LoginContext;
import com.glaf.core.util.ParamUtils;
import com.glaf.heathcare.bean.MedicalExaminationHelper;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.GrowthStandard;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.MedicalExamination;
import com.glaf.heathcare.domain.MedicalExaminationCount;
import com.glaf.heathcare.query.GradeInfoQuery;
import com.glaf.heathcare.query.GrowthStandardQuery;
import com.glaf.heathcare.query.PersonQuery;
import com.glaf.heathcare.query.MedicalExaminationQuery;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.GrowthStandardService;
import com.glaf.heathcare.service.PersonService;
import com.glaf.heathcare.service.MedicalExaminationService;

public class MedicalExaminationCountPreprocessor implements IReportPreprocessor {

	@Override
	public void prepare(LoginContext loginContext, int year, int month, Map<String, Object> params) {
		GradeInfoService gradeInfoService = ContextFactory.getBean("com.glaf.heathcare.service.gradeInfoService");
		PersonService personService = ContextFactory.getBean("com.glaf.heathcare.service.personService");
		MedicalExaminationService medicalExaminationService = ContextFactory
				.getBean("com.glaf.heathcare.service.medicalExaminationService");
		GrowthStandardService growthStandardService = ContextFactory
				.getBean("com.glaf.heathcare.service.growthStandardService");
		PersonQuery query = new PersonQuery();
		query.tenantId(loginContext.getTenantId());
		query.locked(0);
		List<Person> persons = personService.list(query);

		GradeInfoQuery qx = new GradeInfoQuery();
		qx.tenantId(loginContext.getTenantId());
		qx.locked(0);
		List<GradeInfo> grades = gradeInfoService.list(qx);

		MedicalExaminationQuery q = new MedicalExaminationQuery();
		q.tenantId(loginContext.getTenantId());
		q.year(year);
		if (month > 0) {
			q.month(month);
		}

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

		Map<String, MedicalExaminationCount> mmap = new LinkedHashMap<String, MedicalExaminationCount>();
		Map<String, GradeInfo> gradeMap = new HashMap<String, GradeInfo>();
		if (grades != null && !grades.isEmpty()) {
			for (GradeInfo g : grades) {
				gradeMap.put(g.getId(), g);
				MedicalExaminationCount cnt = mmap.get(g.getId());
				if (cnt == null) {
					cnt = new MedicalExaminationCount();
					cnt.setTenantId(loginContext.getTenantId());
					cnt.setGradeId(g.getId());
					cnt.setGradeName(g.getName());
					if (persons != null && !persons.isEmpty()) {
						for (Person p : persons) {
							if (StringUtils.equals(p.getGradeId(), g.getId())) {
								cnt.setTotalPerson(cnt.getTotalPerson() + 1);
							}
						}
					}
					mmap.put(g.getId(), cnt);
				}
			}
		}

		List<MedicalExamination> list = medicalExaminationService.list(q);
		if (list != null && !list.isEmpty()) {
			Person person = null;
			GrowthStandardQuery q2 = new GrowthStandardQuery();
			List<GrowthStandard> rows = growthStandardService.list(q2);
			Map<String, GrowthStandard> gsMap = new HashMap<String, GrowthStandard>();
			if (rows != null && !rows.isEmpty()) {
				for (GrowthStandard gs : rows) {
					gsMap.put(gs.getAgeOfTheMoon() + "_" + gs.getSex() + "_" + gs.getType(), gs);
				}
			}
			MedicalExaminationHelper helper = new MedicalExaminationHelper();
			for (MedicalExamination ex : list) {
				person = personMap.get(ex.getPersonId());
				if (person != null) {
					MedicalExaminationCount cnt = mmap.get(person.getGradeId());
					if (cnt != null) {
						cnt.setCheckPerson(cnt.getCheckPerson() + 1);// 实检人数

						helper.evaluate(gsMap, ex);// 均值离差法

						if (ex.getBmiIndex() >= 2) {
							cnt.setMeanWeightObesity(cnt.getMeanWeightObesity() + 1);// 肥胖人数
						} else if (ex.getBmiIndex() == 1.0) {
							cnt.setMeanOverWeight(cnt.getMeanOverWeight() + 1);// 超重人数
						} else if (ex.getBmiIndex() <= -1) {
							cnt.setMeanWeightSkinny(cnt.getMeanWeightSkinny() + 1);// 消瘦人数
						} else {
							cnt.setMeanWeightNormal(cnt.getMeanWeightNormal() + 1);// 体重正常人数
						}

						if (ex.getWeightLevel() <= -2) {
							cnt.setMeanWeightLow(cnt.getMeanWeightLow() + 1);// 体重低于2SD人数
						}

						if (ex.getHeightLevel() <= -2) {
							cnt.setMeanHeightLow(cnt.getMeanHeightLow() + 1);// 身高低于2SD人数
						}

						if (ex.getHeightLevel() >= 0) {
							cnt.setMeanHeightNormal(cnt.getMeanHeightNormal() + 1);// 身高正常人数
						}

						helper.evaluateByPrctile(gsMap, ex);// 百分位数评价法

						if (ex.getBmiIndex() > 2) {
							cnt.setPrctileWeightObesity(cnt.getPrctileWeightObesity() + 1);// 肥胖人数
						} else if (ex.getBmiIndex() == 2) {
							cnt.setPrctileOverWeight(cnt.getPrctileOverWeight() + 1);// 超重人数
						} else if (ex.getBmiIndex() == -3) {
							cnt.setPrctileWeightHeightLow(cnt.getPrctileWeightHeightLow() + 1);// W/H小于P3人数(身高别体重)
						} else if (ex.getBmiIndex() == 0) {
							cnt.setPrctileWeightHeightNormal(cnt.getPrctileWeightHeightNormal() + 1);// W/H正常人数(身高别体重)
						}

						if (ex.getWeightLevel() <= -2) {
							cnt.setPrctileWeightAgeLow(cnt.getPrctileWeightAgeLow() + 1); // W/A小于P3人数(年龄别体重)
						}

						if (ex.getWeightLevel() == 0) {
							cnt.setPrctileWeightAgeNormal(cnt.getPrctileWeightAgeNormal() + 1); // W/A正常人数(年龄别体重)
						}

						if (ex.getHeightLevel() <= -2) {
							cnt.setPrctileHeightAgeLow(cnt.getPrctileHeightAgeLow() + 1); // H/A小于P3人数(年龄别身高)
						}

						if (ex.getWeightLevel() >= 0) {
							cnt.setPrctileHeightAgeNormal(cnt.getPrctileHeightAgeNormal() + 1);// H/A正常人数(年龄别身高)
						}
					}
				}
			}

			Collection<MedicalExaminationCount> values = mmap.values();
			for (MedicalExaminationCount cnt : values) {
				if (cnt.getCheckPerson() > 0) {
					cnt.setCheckPercent(cnt.getCheckPerson() * 1.0D / cnt.getTotalPerson() * 1.0D);
					cnt.setMeanHeightLowPercent(cnt.getMeanHeightLow() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setMeanHeightNormalPercent(cnt.getMeanHeightNormal() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setMeanOverWeightPercent(cnt.getMeanOverWeight() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setMeanWeightLowPercent(cnt.getMeanWeightLow() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setMeanWeightNormalPercent(cnt.getMeanWeightNormal() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setMeanWeightObesityPercent(cnt.getMeanWeightObesity() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setMeanWeightSkinnyPercent(cnt.getMeanWeightSkinny() * 1.0 / cnt.getCheckPerson() * 1.0D);

					cnt.setPrctileHeightAgeLowPercent(cnt.getPrctileHeightAgeLow() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setPrctileHeightAgeNormalPercent(
							cnt.getPrctileHeightAgeNormal() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setPrctileOverWeightPercent(cnt.getPrctileOverWeight() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setPrctileWeightAgeLowPercent(cnt.getPrctileWeightAgeLow() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setPrctileWeightAgeNormalPercent(
							cnt.getPrctileWeightAgeNormal() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setPrctileWeightHeightLowPercent(
							cnt.getPrctileWeightHeightLow() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setPrctileWeightHeightNormalPercent(
							cnt.getPrctileWeightHeightNormal() * 1.0 / cnt.getCheckPerson() * 1.0D);
					cnt.setPrctileWeightObesityPercent(
							cnt.getPrctileWeightObesity() * 1.0 / cnt.getCheckPerson() * 1.0D);

				}
			}

			params.put("rows", mmap.values());
		}
	}

}
