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

package com.glaf.core.base;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.glaf.core.util.DateUtils;

public class SqlParameter implements java.io.Serializable, Parameter {

	private static final long serialVersionUID = -5679409772240725858L;

	protected String id;

	protected String name;

	protected boolean autoTypeConvert;

	protected Date dateVal;

	protected Double doubleVal;

	protected Integer intVal;

	protected Long longVal;

	protected String stringVal;

	public SqlParameter() {

	}

	public Date getDateVal() {
		if (dateVal == null) {
			if (autoTypeConvert && StringUtils.isNotEmpty(stringVal)) {
				dateVal = DateUtils.toDate(stringVal);
			}
		}
		return dateVal;
	}

	public Double getDoubleVal() {
		if (doubleVal == null) {
			if (autoTypeConvert && StringUtils.isNotEmpty(stringVal)) {
				doubleVal = Double.parseDouble(stringVal);
			}
		}
		return doubleVal;
	}

	public String getId() {
		return id;
	}

	public Integer getIntVal() {
		if (intVal == null) {
			if (autoTypeConvert && StringUtils.isNotEmpty(stringVal)) {
				intVal = Integer.parseInt(stringVal);
			}
		}
		return intVal;
	}

	public Long getLongVal() {
		if (longVal == null) {
			if (autoTypeConvert && StringUtils.isNotEmpty(stringVal)) {
				longVal = Long.parseLong(stringVal);
			}
		}
		return longVal;
	}

	public String getName() {
		return name;
	}

	public String getStringVal() {
		return stringVal;
	}

	public boolean isAutoTypeConvert() {
		return autoTypeConvert;
	}

	public void setAutoTypeConvert(boolean autoTypeConvert) {
		this.autoTypeConvert = autoTypeConvert;
	}

	public void setDateVal(Date dateVal) {
		this.dateVal = dateVal;
	}

	public void setDoubleVal(Double doubleVal) {
		this.doubleVal = doubleVal;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIntVal(Integer intVal) {
		this.intVal = intVal;
	}

	public void setLongVal(Long longVal) {
		this.longVal = longVal;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStringVal(String stringVal) {
		this.stringVal = stringVal;
	}

	@Override
	public String toString() {
		return "SqlParameter [id=" + id + ", name=" + name + ", autoTypeConvert=" + autoTypeConvert + ", dateVal="
				+ dateVal + ", doubleVal=" + doubleVal + ", intVal=" + intVal + ", longVal=" + longVal + ", stringVal="
				+ stringVal + "]";
	}

}
