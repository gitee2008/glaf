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

package com.glaf.heathcare.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.glaf.base.modules.sys.model.Dictory;
import com.glaf.base.modules.sys.service.DictoryService;
import com.glaf.core.context.ContextFactory;
import com.glaf.heathcare.domain.PersonInfo;
import com.glaf.heathcare.service.PersonInfoService;

public class PersonInfoHelper {

	protected DictoryService dictoryService;

	protected PersonInfoService personInfoService;

	public PersonInfoHelper() {

	}

	public DictoryService getDictoryService() {
		if (dictoryService == null) {
			dictoryService = ContextFactory.getBean("dictoryService");
		}
		return dictoryService;
	}

	public double getPersonFoodAgeQuantity(String tenantId, double factorPerHundred) {
		double totalQuantity = 0.0;
		List<PersonInfo> persons = getPersonInfoService().getPersonInfos(tenantId);
		if (persons != null && !persons.isEmpty()) {
			List<Dictory> dicts = getDictoryService().getDictoryList("PersonFoodAgeFactor");
			if (dicts != null && !dicts.isEmpty()) {
				Map<Integer, Double> factorMap = new HashMap<Integer, Double>();
				for (Dictory dict : dicts) {
					if (StringUtils.isNumeric(dict.getCode())) {
						factorMap.put(Integer.parseInt(dict.getCode()), Double.parseDouble(dict.getValue()));
					}
				}
				for (PersonInfo info : persons) {
					if (factorMap.get(info.getAge()) != null) {
						int age = info.getAge();
						double factor = factorMap.get(age);
						int male = info.getMale();
						int female = info.getFemale();
						totalQuantity = totalQuantity + male * factor;// 男生标准
						totalQuantity = totalQuantity + female * factor * 0.95;// 女生标准
						if (factorPerHundred > 1) {
							totalQuantity = totalQuantity
									+ (male * factor + female * factor * 0.95) * (factorPerHundred - 1);
						}
					}
				}
			}
		}

		return totalQuantity;
	}

	public PersonInfoService getPersonInfoService() {
		if (personInfoService == null) {
			personInfoService = ContextFactory.getBean("com.glaf.heathcare.service.personInfoService");
		}
		return personInfoService;
	}

	public void setDictoryService(DictoryService dictoryService) {
		this.dictoryService = dictoryService;
	}

	public void setPersonInfoService(PersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}

}
