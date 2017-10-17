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

package com.glaf.base.online.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class UserOnlineLogQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String searchWord;
	protected Date loginDateGreaterThanOrEqual;
	protected Date loginDateLessThanOrEqual;
	protected String loginIP;
	protected Integer year;
	protected Integer month;
	protected Integer quarter;
	protected Integer day;

	public UserOnlineLogQuery() {

	}

	public UserOnlineLogQuery day(Integer day) {
		if (day == null) {
			throw new RuntimeException("day is null");
		}
		this.day = day;
		return this;
	}

	public String getActorId() {
		return actorId;
	}

	public Integer getDay() {
		return day;
	}

	public Date getLoginDateGreaterThanOrEqual() {
		return loginDateGreaterThanOrEqual;
	}

	public Date getLoginDateLessThanOrEqual() {
		return loginDateLessThanOrEqual;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public Integer getMonth() {
		return month;
	}

	public String getName() {
		return name;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("actorId".equals(sortColumn)) {
				orderBy = "E.ACTORID_" + a_x;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME_" + a_x;
			}

			if ("loginDate".equals(sortColumn)) {
				orderBy = "E.LOGINDATE_" + a_x;
			}

			if ("loginIP".equals(sortColumn)) {
				orderBy = "E.LOGINIP_" + a_x;
			}

		}
		return orderBy;
	}

	public Integer getQuarter() {
		return quarter;
	}

	public String getSearchWord() {
		if (searchWord != null && searchWord.trim().length() > 0) {
			if (!searchWord.startsWith("%")) {
				searchWord = "%" + searchWord;
			}
			if (!searchWord.endsWith("%")) {
				searchWord = searchWord + "%";
			}
		}
		return searchWord;
	}

	public Integer getYear() {
		return year;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("actorId", "ACTORID_");
		addColumn("name", "NAME_");
		addColumn("loginDate", "LOGINDATE_");
		addColumn("loginIP", "LOGINIP_");
	}

	public UserOnlineLogQuery loginDateGreaterThanOrEqual(Date loginDateGreaterThanOrEqual) {
		if (loginDateGreaterThanOrEqual == null) {
			throw new RuntimeException("loginDate is null");
		}
		this.loginDateGreaterThanOrEqual = loginDateGreaterThanOrEqual;
		return this;
	}

	public UserOnlineLogQuery loginDateLessThanOrEqual(Date loginDateLessThanOrEqual) {
		if (loginDateLessThanOrEqual == null) {
			throw new RuntimeException("loginDate is null");
		}
		this.loginDateLessThanOrEqual = loginDateLessThanOrEqual;
		return this;
	}

	public UserOnlineLogQuery loginIP(String loginIP) {
		if (loginIP == null) {
			throw new RuntimeException("loginIP is null");
		}
		this.loginIP = loginIP;
		return this;
	}

	public UserOnlineLogQuery month(Integer month) {
		if (month == null) {
			throw new RuntimeException("month is null");
		}
		this.month = month;
		return this;
	}

	public UserOnlineLogQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public UserOnlineLogQuery quarter(Integer quarter) {
		if (quarter == null) {
			throw new RuntimeException("quarter is null");
		}
		this.quarter = quarter;
		return this;
	}

	public UserOnlineLogQuery searchWord(String searchWord) {
		if (searchWord == null) {
			throw new RuntimeException("searchWord is null");
		}
		this.searchWord = searchWord;
		return this;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setActorIds(List<String> actorIds) {
		this.actorIds = actorIds;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public void setLoginDateGreaterThanOrEqual(Date loginDateGreaterThanOrEqual) {
		this.loginDateGreaterThanOrEqual = loginDateGreaterThanOrEqual;
	}

	public void setLoginDateLessThanOrEqual(Date loginDateLessThanOrEqual) {
		this.loginDateLessThanOrEqual = loginDateLessThanOrEqual;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public UserOnlineLogQuery year(Integer year) {
		if (year == null) {
			throw new RuntimeException("year is null");
		}
		this.year = year;
		return this;
	}

}