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
package com.glaf.heathcare;

import java.util.Calendar;
import java.util.Date;

public class SysConfig {

	public static int SEMESTER = 0;

	/**
	 * 获取学期，默认当年2月至7月为下学期，下半年的8月到第二年的1月为上学期
	 * 
	 * @return
	 */
	public static int getSemester() {
		if (SEMESTER > 0) {
			return SEMESTER;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int month = calendar.get(Calendar.MONTH);
		switch (month) {
		case Calendar.AUGUST:// 8月份
		case Calendar.SEPTEMBER:// 9月份
		case Calendar.OCTOBER:// 10月份
		case Calendar.NOVEMBER:// 11月份
		case Calendar.DECEMBER:// 12月份
		case Calendar.JANUARY:// 1月份
			SEMESTER = 1;
			break;
		default:
			SEMESTER = 2;
			break;
		}
		return SEMESTER;
	}

	/**
	 * 获取学期，默认当年2月至7月为下学期，下半年的8月到第二年的1月为上学期
	 * 
	 * @return
	 */
	public static int getSemester(int month) {
		month = month - 1;
		switch (month) {
		case Calendar.AUGUST:// 8月份
		case Calendar.SEPTEMBER:// 9月份
		case Calendar.OCTOBER:// 10月份
		case Calendar.NOVEMBER:// 11月份
		case Calendar.DECEMBER:// 12月份
		case Calendar.JANUARY:// 1月份
			return 1;
		default:
			break;
		}
		return 2;
	}

}
