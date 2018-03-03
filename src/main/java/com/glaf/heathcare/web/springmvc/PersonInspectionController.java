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

package com.glaf.heathcare.web.springmvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glaf.core.security.LoginContext;
import com.glaf.core.util.DateUtils;
import com.glaf.core.util.RequestUtils;
import com.glaf.core.util.ResponseUtils;
import com.glaf.heathcare.domain.GradeInfo;
import com.glaf.heathcare.domain.Person;
import com.glaf.heathcare.domain.PersonInspection;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonInspectionService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/personInspection")
@RequestMapping("/heathcare/personInspection")
public class PersonInspectionController {
	protected static final Log logger = LogFactory.getLog(PersonInspectionController.class);

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected PersonInspectionService personInspectionService;

	public PersonInspectionController() {

	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Date inspectionDate = RequestUtils.getDate(request, "inspectionDate");
		String inspectionFlag = request.getParameter("inspectionFlag");
		int day = DateUtils.getNowYearMonthDay();
		String gradeId = request.getParameter("gradeId");
		String section = request.getParameter("section");
		String type = request.getParameter("type");
		if (StringUtils.isEmpty(section)) {
			section = "day";
		}
		if (StringUtils.isEmpty(type)) {
			type = "0";
		}
		if (inspectionDate != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			String str = f.format(inspectionDate);
			day = Integer.parseInt(str);
		} else {
			inspectionDate = new Date();
		}
		request.setAttribute("privilege_write", false);
		try {
			GradeInfo gradeInfo = gradeInfoService.getGradeInfo(gradeId);
			if (gradeInfo != null) {
				if (((loginContext.getRoles().contains("AttendanceManagement")
						|| loginContext.getRoles().contains("HealthPhysician")
						|| loginContext.getRoles().contains("TenantAdmin"))
						&& StringUtils.equals(gradeInfo.getTenantId(), loginContext.getTenantId()))
						|| (loginContext.getRoles().contains("Teacher")
								&& loginContext.getGradeIds().contains(gradeId))) {
					List<PersonInspection> list = personInspectionService.getPersonInspections(
							loginContext.getTenantId(), gradeId, day, section, type, inspectionFlag);

					List<Person> persons = personService.getPersons(gradeId);

					if (persons != null && !persons.isEmpty()) {
						if (list != null && !list.isEmpty()) {
							Map<String, PersonInspection> aMap = new HashMap<String, PersonInspection>();
							for (PersonInspection a : list) {
								aMap.put(a.getPersonId(), a);
							}
							PersonInspection inspection = null;
							for (Person person : persons) {
								if (aMap.get(person.getId()) != null) {
									inspection = aMap.get(person.getId());
									person.setMemo(inspection.getRemark());
									person.setStatus(inspection.getStatus());
									person.setTreat(inspection.getTreat());
								}
							}
						}
						request.setAttribute("persons", persons);
						request.setAttribute("gradeInfo", gradeInfo);

						if (day >= (DateUtils.getNowYearMonthDay() - 30) && day <= DateUtils.getNowYearMonthDay()) {
							request.setAttribute("privilege_write", true);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}

		int year = RequestUtils.getInt(request, "year");
		int month = RequestUtils.getInt(request, "month");

		if (year == 0) {
			year = DateUtils.getNowYear();
			request.setAttribute("year", year);
		}
		if (month == 0) {
			month = DateUtils.getNowYearMonth() % 100;
			request.setAttribute("month", month);
		}

		request.setAttribute("inspectionDate", inspectionDate);

		logger.debug("month:" + month);
		Calendar calendar = Calendar.getInstance();
		if (year > 0) {
			calendar.set(Calendar.YEAR, year);
		}
		calendar.set(Calendar.MONTH, month - 1);
		int day2 = calendar.get(Calendar.DAY_OF_MONTH);
		StringBuilder buff = new StringBuilder();
		buff.append(year);
		if (month < 10) {
			buff.append("0");
		}
		buff.append(month);
		List<String> days = new ArrayList<String>();
		for (int i = 1; i < day2; i++) {
			if (i < 10) {
				days.add(buff.toString() + "0" + i);
			} else {
				days.add(buff.toString() + i);
			}
		}
		request.setAttribute("days", days);

		String view = request.getParameter("view");
		if (StringUtils.isNotEmpty(view)) {
			return new ModelAndView(view, modelMap);
		}

		return new ModelAndView("/heathcare/personInspection/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAll")
	public byte[] saveAll(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Date inspectionDate = RequestUtils.getDate(request, "inspectionDate");
		int day = DateUtils.getNowYearMonthDay();
		String gradeId = request.getParameter("gradeId");
		String section = request.getParameter("section");
		String type = request.getParameter("type");
		if (StringUtils.isEmpty(section)) {
			section = "day";
		}
		if (StringUtils.isEmpty(type)) {
			type = "0";
		}
		if (inspectionDate != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			String str = f.format(inspectionDate);
			day = Integer.parseInt(str);
		}
		try {
			GradeInfo gradeInfo = gradeInfoService.getGradeInfo(gradeId);
			if (gradeInfo != null && day >= (DateUtils.getNowYearMonthDay() - 30)
					&& day <= DateUtils.getNowYearMonthDay()) {
				if (((loginContext.getRoles().contains("AttendanceManagement")
						|| loginContext.getRoles().contains("HealthPhysician")
						|| loginContext.getRoles().contains("TenantAdmin"))
						&& StringUtils.equals(gradeInfo.getTenantId(), loginContext.getTenantId()))
						|| (loginContext.getRoles().contains("Teacher")
								&& loginContext.getGradeIds().contains(gradeId))) {
					List<Person> persons = personService.getPersons(gradeId);
					if (persons != null && !persons.isEmpty()) {
						String inspectionFlag = request.getParameter("inspectionFlag");
						List<PersonInspection> list = new ArrayList<PersonInspection>();
						for (Person person : persons) {
							int inspectionStatus = RequestUtils.getInt(request, "status_" + person.getId());
							if (inspectionStatus > 0) {
								PersonInspection inspection = new PersonInspection();
								inspection.setPersonId(person.getId());
								inspection.setName(person.getName());
								inspection.setStatus(inspectionStatus);
								inspection.setInspectionFlag(inspectionFlag);
								inspection.setCreateBy(loginContext.getActorId());
								inspection.setTreat(request.getParameter("treat_" + person.getId()));
								inspection.setRemark(request.getParameter("remark_" + person.getId()));
								list.add(inspection);
							}
							personInspectionService.saveAll(loginContext.getTenantId(), gradeId, day, section, type,
									inspectionFlag, loginContext.getActorId(), list);
						}
						return ResponseUtils.responseJsonResult(true);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		return ResponseUtils.responseJsonResult(false);
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.gradeInfoService")
	public void setGradeInfoService(GradeInfoService gradeInfoService) {
		this.gradeInfoService = gradeInfoService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personInspectionService")
	public void setPersonInspectionService(PersonInspectionService personInspectionService) {
		this.personInspectionService = personInspectionService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
