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

package com.glaf.core.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	public static final long SECOND = 1000L;

	public static final long MINUTE = 60L * SECOND;

	public static final long HOUR = 60L * MINUTE;

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final long DAY = 24L * HOUR;

	public static final String DAY_FORMAT = "yyyy-MM-dd";

	public static final String HOUR_FORMAT = "yyyy-MM-dd HH";

	public static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

	public static final String MONTH_FORMAT = "yyyy-MM";

	public static final String SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String MILSECOND_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static final long WEEK = 7L * DAY;

	public static final String YEAR_FORMAT = "yyyy";

	public static final String YEAR_MONTH_DAY_FORMAT = "yyyyMMdd";

	public static final String FULL_DATE_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 判断某个时间是否在另一个时间之后
	 * 
	 * @param beforeTime
	 * @param afterTime
	 * @return
	 */
	public static boolean afterTime(Date beforeTime, Date afterTime) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(beforeTime);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(afterTime);

		return calendar1.after(calendar2);
	}

	/**
	 * 判断某个时间是否在另一个时间之前
	 * 
	 * @param beforeTime
	 * @param afterTime
	 * @return
	 */
	public static boolean beforeTime(Date beforeTime, Date afterTime) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(beforeTime);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(afterTime);

		return calendar1.before(calendar2);
	}

	/**
	 * 取两日期差异天数
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static long dateDiff(Date fromDate, Date toDate) {
		return dateDiff(getDateTime(DATE_PATTERN, fromDate), getDateTime(DATE_PATTERN, toDate));
	}

	public static long dateDiff(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (java.text.ParseException ex) {

		}
		return day;
	}

	public static BigDecimal getCurrentTimeAsNumber() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		returnStr = f.format(date);
		return new BigDecimal(returnStr);
	}

	/**
	 * 按指定格式将java.util.Date日期转换为字符串 例如:2009-10-01
	 * 
	 * @param date
	 * @return
	 */
	public static String getDate(java.util.Date date) {
		return getDateTime(DATE_PATTERN, date);
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 按指定格式将java.util.Date日期转换为字符串 例如:2009-01-01 15:02:01
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTime(java.util.Date date) {
		return getDateTime(DATE_TIME_PATTERN, date);
	}

	/**
	 * 按给定格式转换java.util.Date日期为字符串
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String getDateTime(String pattern, java.util.Date date) {
		if (date == null) {
			return "";
		}
		if (pattern == null) {
			pattern = DATE_TIME_PATTERN;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
		String ret = formatter.format(date);
		return ret;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDaysBetween(Calendar startDate, Calendar endDate) {
		if (startDate.after(endDate)) {
			java.util.Calendar swap = startDate;
			startDate = endDate;
			endDate = swap;
		}
		int days = endDate.get(java.util.Calendar.DAY_OF_YEAR) - startDate.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = endDate.get(java.util.Calendar.YEAR);
		if (startDate.get(java.util.Calendar.YEAR) != y2) {
			startDate = (java.util.Calendar) startDate.clone();
			do {
				days += startDate.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				startDate.add(java.util.Calendar.YEAR, 1);
			} while (startDate.get(java.util.Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDaysBetween(Date startDate, Date endDate) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal2.setTime(endDate);
		return getDaysBetween(cal1, cal2);
	}

	/**
	 * 计算两个日期之间的假期天数（仅仅是正常休息日即周六周日）
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getHolidays(Calendar startDate, Calendar endDate) {
		return getDaysBetween(startDate, endDate) - getWorkingDay(startDate, endDate);
	}

	/**
	 * 获得指定日期的下一个星期一的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getNextMonday(Calendar date) {
		Calendar result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	public static int getNowYear() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy");
		Date date = new Date();
		returnStr = f.format(date);
		return Integer.parseInt(returnStr);
	}

	public static int getNowYearMonth() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		returnStr = f.format(date);
		return Integer.parseInt(returnStr);
	}

	public static int getNowYearMonthDay() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		returnStr = f.format(date);
		return Integer.parseInt(returnStr);
	}

	public static String getNowYearMonthDayHHmm() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	public static String getNowYearMonthDayHour() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	public static String getNowYearMonthDayHHmmss() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	public static List<Integer> getYearDays(int year) {
		List<Integer> list = new ArrayList<Integer>();
		StringBuilder buff = new StringBuilder();
		for (int i = 1; i <= 12; i++) {
			int day = 30;
			switch (i) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;
				break;
			case 2:
				if (year % 4 == 0 || year % 400 == 0) {
					day = 29;
				} else {
					day = 28;
				}
				break;
			default:
				day = 30;
				break;
			}
			for (int j = 1; j <= day; j++) {
				buff.delete(0, buff.length());
				buff.append(year);
				if (i < 10) {
					buff.append("0");
				}
				buff.append(i);
				if (j < 10) {
					buff.append("0");
				}
				buff.append(j);
				list.add(Integer.valueOf(buff.toString()));
			}
		}
		return list;
	}

	/**
	 * 计算两个日期之间的工作天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getWorkingDay(Calendar startDate, Calendar endDate) {
		int result = -1;
		if (startDate.after(endDate)) {
			java.util.Calendar swap = startDate;
			startDate = endDate;
			endDate = swap;
		}

		int charge_start_date = 0;// 开始日期的日期偏移量
		int charge_end_date = 0;// 结束日期的日期偏移量
		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - startDate.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - endDate.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
			charge_end_date = etmp - 1;
		}

		result = (getDaysBetween(getNextMonday(startDate), getNextMonday(endDate)) / 7) * 5 + charge_start_date
				- charge_end_date;
		return result;
	}

	/**
	 * 计算两个日期之间的工作天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getWorkingDay(Date startDate, Date endDate) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal2.setTime(endDate);
		return getWorkingDay(cal1, cal2);
	}

	public static int getYearMonth(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());
		String ret = formatter.format(date);
		return Integer.parseInt(ret);
	}

	public static int getYearMonthDay(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		String ret = formatter.format(date);
		return Integer.parseInt(ret);
	}

	/**
	 * 根据年月取每月的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getYearMonthDays(int year, int month) {
		int days = 31;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:
			if (year % 4 == 0 || year % 100 == 0 || year % 400 == 0) {
				days = 29;
				break;
			}
			days = 28;
			break;
		}
		return days;
	}

	/**
	 * Current system time. Do not use this to calculate a duration or interval to
	 * sleep, because it will be broken by settimeofday. Instead, use monotonicNow.
	 * 
	 * @return current time in msec.
	 */
	public static long now() {
		return System.currentTimeMillis();
	}

	/**
	 * Current time from some arbitrary time base in the past, counting in
	 * milliseconds, and not affected by settimeofday or similar system clock
	 * changes. This is appropriate to use when computing how much longer to wait
	 * for an interval to expire.
	 * 
	 * @return a monotonic clock that counts in milliseconds.
	 */
	public static long monotonicNow() {
		final long NANOSECONDS_PER_MILLISECOND = 1000000;

		return System.nanoTime() / NANOSECONDS_PER_MILLISECOND;
	}

	public static void main(String[] args) {
		System.out.println(DateUtils.beforeTime(DateUtils.toDate("2017-09-01"), new Date()));
		System.out.println("2018年05月09日".length());
		System.out.println(DateUtils.getDate(new Date()));
		System.out.println(DateUtils.getDateTime(new Date()));
		System.out.println(DateUtils.getYearMonthDay(new Date()));

		System.out.println("2014-10-22 00:00:00.0".substring(0, 19));

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, day - 7);
		Date dateBefore = calendar.getTime();
		System.out.println("day:" + year + "-" + month + "-" + day);
		System.out.println(DateUtils.getDate(dateBefore));

		calendar.set(year - 1, month, day);
		dateBefore = calendar.getTime();
		System.out.println(DateUtils.getDate(dateBefore));

		calendar.set(year, month - 15, day);
		dateBefore = calendar.getTime();
		System.out.println(">>" + DateUtils.getDate(dateBefore));

		calendar.set(year, month, day - 365);
		dateBefore = calendar.getTime();
		System.out.println(">>>>" + DateUtils.getDate(dateBefore));

		String[] parsePatterns = { DateUtils.DATE_TIME_PATTERN, DateUtils.DATE_PATTERN };
		System.out.println(DateUtils.parseDate("2009-12-25", parsePatterns));
		System.out.println(DateUtils.parseDate("2009-12-25 12:00:05", parsePatterns));
		System.out.println(DateUtils.parseDate("2009-12-25 23:59:59", parsePatterns));
		System.out.println(DateUtils.parseDate("2009-12-25 00:00:00", parsePatterns));
		System.out.println(Locale.getDefault());
		System.out.println(DateUtils.toDate("2009"));
		System.out.println(DateUtils.toDate("2009-12"));
		System.out.println(DateUtils.toDate("2009-12-25"));
		System.out.println(DateUtils.toDate("2009-12-25 13"));
		System.out.println(DateUtils.toDate("2009-12-25 10:45"));
		System.out.println(DateUtils.toDate("2009-12-25 22:45:50"));

		System.out.println(dateDiff(DateUtils.toDate("2013-10-25"), DateUtils.toDate("2013-10-29")));
		Date toDate = DateUtils.getDateAfter(DateUtils.toDate("2013-03-21"), 60);
		System.out.println(getDateTime(toDate));
		long daysDiff = DateUtils.dateDiff(new Date(), toDate);
		System.out.println(daysDiff);

		System.out.println(DateUtils.getYearDays(2015));

		System.out.println(DateUtils.toDate("2015-12-25 22:45:50.0"));

		System.out.println(DateUtils.toDate("1474473600000"));
		System.out.println(DateUtils.toDate("20160101142323"));

	}

	public static Date parseDate(String str, String[] parsePatterns) {
		if (str == null || parsePatterns == null) {
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}
		SimpleDateFormat parser = null;
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < parsePatterns.length; i++) {
			if (i == 0) {
				parser = new SimpleDateFormat(parsePatterns[0]);
			} else {
				parser.applyPattern(parsePatterns[i]);
			}
			pos.setIndex(0);
			Date date = parser.parse(str, pos);
			if (date != null && pos.getIndex() == str.length()) {
				return date;
			}
		}
		throw new RuntimeException("Unable to parse the date: " + str);
	}

	public static Timestamp removeTime(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Timestamp(cal.getTimeInMillis());
	}

	public static java.util.Date toDate(long ts) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ts);
		return calendar.getTime();
	}

	public static Date toDate(String dateString) {
		String[] parsePatterns = new String[1];
		try {
			if (dateString.length() == 4) {
				parsePatterns[0] = YEAR_FORMAT;
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else if (dateString.length() == 6) {
				parsePatterns[0] = YEAR_MONTH_DAY_FORMAT;
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else if (dateString.length() == 7) {
				parsePatterns[0] = MONTH_FORMAT;
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else if (dateString.length() == 8) {
				parsePatterns[0] = YEAR_MONTH_DAY_FORMAT;
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else if (dateString.length() == 10) {
				parsePatterns[0] = DAY_FORMAT;
				try {
					return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
				} catch (Exception ex) {
					parsePatterns[0] = "MM/dd/yyyy";
					return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
				}
			} else if (dateString.length() == 11) {
				parsePatterns[0] = "yyyy年MM月dd日";
				try {
					return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
				} catch (Exception ex) {
					java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(parsePatterns[0]);
					return format.parse(dateString);
				}
			} else if (dateString.length() == 13) {
				parsePatterns[0] = HOUR_FORMAT;
				if (StringUtils.isNumeric(dateString)) {
					try {
						// 如果是long时间类型
						long ts = Long.parseLong(dateString);
						return DateUtils.toDate(ts);
					} catch (Exception e) {
						return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
					}
				} else {
					return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
				}
			} else if (dateString.length() == 14) {
				if (StringUtils.isNumeric(dateString)) {
					try {
						return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, FULL_DATE_FORMAT);
					} catch (Exception e) {
						return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
					}
				}
			} else if (dateString.length() == 16) {
				parsePatterns[0] = MINUTE_FORMAT;
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else if (dateString.length() == 19) {
				parsePatterns[0] = SECOND_FORMAT;
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else if (dateString.length() == 21) {
				parsePatterns[0] = SECOND_FORMAT;
				dateString = dateString.substring(0, 19);
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else if (dateString.length() == 24) {
				parsePatterns[0] = MILSECOND_FORMAT;
				Date date = org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
				// 需要增加时区
				if (dateString.endsWith("Z")) {
					Calendar ca = Calendar.getInstance();
					ca.setTime(date);
					ca.add(Calendar.HOUR_OF_DAY, 8);
					return ca.getTime();
				}
				return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
			} else {
				if (dateString.length() > 20) {
					parsePatterns[0] = SECOND_FORMAT;
					dateString = dateString.substring(0, 19);
					return org.apache.commons.lang3.time.DateUtils.parseDate(dateString, parsePatterns);
				}
			}
		} catch (ParseException ex) {

			throw new RuntimeException(" parse date string error: " + ex);
		}
		throw new RuntimeException("Input is not valid date string: " + dateString);
	}

	public static java.sql.Timestamp toTimestamp(java.util.Date date) {
		if (date != null) {
			return new java.sql.Timestamp(date.getTime());
		} else {
			return null;
		}
	}

	public static java.sql.Timestamp toTimestamp(String dateTime) {
		java.util.Date newDate = toDate(dateTime);
		if (newDate != null) {
			return new java.sql.Timestamp(newDate.getTime());
		} else {
			return null;
		}
	}

	private DateUtils() {
	}

}