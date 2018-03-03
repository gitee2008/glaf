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
import com.glaf.heathcare.domain.PersonAbsence;
import com.glaf.heathcare.service.GradeInfoService;
import com.glaf.heathcare.service.PersonAbsenceService;
import com.glaf.heathcare.service.PersonService;

/**
 * 
 * SpringMVC控制器
 *
 */

@Controller("/heathcare/personAbsence")
@RequestMapping("/heathcare/personAbsence")
public class PersonAbsenceController {
	protected static final Log logger = LogFactory.getLog(PersonAbsenceController.class);

	protected GradeInfoService gradeInfoService;

	protected PersonService personService;

	protected PersonAbsenceService personAbsenceService;

	public PersonAbsenceController() {

	}

	@RequestMapping
	public ModelAndView list(HttpServletRequest request, ModelMap modelMap) {
		RequestUtils.setRequestParameterToAttribute(request);
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Date absenceDate = RequestUtils.getDate(request, "absenceDate");
		int day = DateUtils.getNowYearMonthDay();
		String gradeId = request.getParameter("gradeId");
		String section = request.getParameter("section");
		if (StringUtils.isEmpty(section)) {
			section = "day";
		}
		if (absenceDate != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			String str = f.format(absenceDate);
			day = Integer.parseInt(str);
		} else {
			absenceDate = new Date();
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
					List<PersonAbsence> list = personAbsenceService.getPersonAbsences(loginContext.getTenantId(),
							gradeId, day, section);

					List<Person> persons = personService.getPersons(gradeId);

					if (persons != null && !persons.isEmpty()) {
						if (list != null && !list.isEmpty()) {
							Map<String, PersonAbsence> dataMap = new HashMap<String, PersonAbsence>();
							for (PersonAbsence a : list) {
								dataMap.put(a.getPersonId(), a);
							}
							PersonAbsence absence = null;
							for (Person person : persons) {
								if (dataMap.get(person.getId()) != null) {
									absence = dataMap.get(person.getId());
									person.setMemo(absence.getRemark());
									person.setStatus(absence.getStatus());
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
			// ex.printStackTrace();
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

		request.setAttribute("absenceDate", absenceDate);

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

		return new ModelAndView("/heathcare/personAbsence/list", modelMap);
	}

	@ResponseBody
	@RequestMapping("/saveAll")
	public byte[] saveAll(HttpServletRequest request) {
		LoginContext loginContext = RequestUtils.getLoginContext(request);
		Date absenceDate = RequestUtils.getDate(request, "absenceDate");
		int day = DateUtils.getNowYearMonthDay();
		String gradeId = request.getParameter("gradeId");
		String section = request.getParameter("section");
		if (StringUtils.isEmpty(section)) {
			section = "day";
		}
		if (absenceDate != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			String str = f.format(absenceDate);
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
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(absenceDate);
						int year = calendar.get(Calendar.YEAR);
						int month = calendar.get(Calendar.MONTH) + 1;

						List<PersonAbsence> list = new ArrayList<PersonAbsence>();
						for (Person person : persons) {
							int absenceStatus = RequestUtils.getInt(request, "status_" + person.getId());
							if (absenceStatus == 0) {
								absenceStatus = 1;
							}
							PersonAbsence absence = new PersonAbsence();
							absence.setPersonId(person.getId());
							absence.setName(person.getName());
							absence.setStatus(absenceStatus);
							absence.setYear(year);
							absence.setMonth(month);
							absence.setCreateBy(loginContext.getActorId());
							absence.setRemark(request.getParameter("remark_" + person.getId()));
							list.add(absence);
							personAbsenceService.saveAll(loginContext.getTenantId(), gradeId, day, section,
									loginContext.getActorId(), list);
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

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personAbsenceService")
	public void setPersonAbsenceService(PersonAbsenceService personAbsenceService) {
		this.personAbsenceService = personAbsenceService;
	}

	@javax.annotation.Resource(name = "com.glaf.heathcare.service.personService")
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
