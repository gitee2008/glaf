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

package com.glaf.matrix.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.glaf.core.util.DateUtils;

public class SysParams {

	protected static final Log logger = LogFactory.getLog(SysParams.class);

	public static final int MAX_SIZE = 2000000;

	private SysParams() {

	}

	public static void main(String[] args) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		SysParams.putInternalParams(parameter);
		System.out.println(parameter);
		logger.debug(parameter);
	}

	public static void putInternalParams(Map<String, Object> parameter) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int runday = DateUtils.getNowYearMonthDay();
		parameter.put("runday", runday);
		parameter.put("today", DateUtils.getDate(now));
		parameter.put("today_start", DateUtils.getDate(now) + " 00:00:00");
		parameter.put("today_end", DateUtils.getDate(now) + " 23:59:59");
		parameter.put("yesterday_start", DateUtils.getDate(DateUtils.getDateBefore(now, 1)) + " 00:00:00");
		parameter.put("yesterday_end", DateUtils.getDate(DateUtils.getDateBefore(now, 1)) + " 23:59:59");
		if (hour <= 9) {
			parameter.put("today_curr_hour_start", DateUtils.getDate(now) + " 0" + hour + ":00:00");
			parameter.put("today_curr_hour_end", DateUtils.getDate(now) + " 0" + hour + ":59:59");
		} else {
			parameter.put("today_curr_hour_start", DateUtils.getDate(now) + " " + hour + ":00:00");
			parameter.put("today_curr_hour_end", DateUtils.getDate(now) + " " + hour + ":59:59");
		}
		if (hour > 0) {
			hour = hour - 1;
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			Date date = calendar.getTime();
			if (hour <= 9) {
				parameter.put("today_previous_hour_start", DateUtils.getDate(date) + " 0" + hour + ":00:00");
				parameter.put("today_previous_hour_end", DateUtils.getDate(date) + " 0" + hour + ":59:59");
			} else {
				parameter.put("today_previous_hour_start", DateUtils.getDate(date) + " " + hour + ":00:00");
				parameter.put("today_previous_hour_end", DateUtils.getDate(date) + " " + hour + ":59:59");
			}
		} else {
			parameter.put("today_previous_hour_start",
					DateUtils.getDate(DateUtils.getDateBefore(now, 1)) + " 23:00:00");
			parameter.put("today_previous_hour_end", DateUtils.getDate(DateUtils.getDateBefore(now, 1)) + " 23:59:59");
		}

		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		switch (calendar.get(Calendar.MONTH) + 1) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			break;
		case 2:
			if (calendar.get(Calendar.YEAR) % 4 == 0) {
				calendar.set(Calendar.DAY_OF_MONTH, 29);
			} else {
				calendar.set(Calendar.DAY_OF_MONTH, 28);
			}
			break;
		default:
			calendar.set(Calendar.DAY_OF_MONTH, 30);
			break;
		}

		parameter.put("the_end_of_last_month", DateUtils.getDate(calendar.getTime()) + " 23:59:59");
	}

}
