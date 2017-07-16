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
package com.glaf.core.access.domain;

import java.io.Serializable;

public class AccessTotal implements Serializable{

	private static final long serialVersionUID = 1L;

	protected String userId;
	
	protected int day;
	
	protected int hour;
	
	protected int minute;
	
	protected int quantity;
	
	public AccessTotal(){
		
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getUserId() {
		return userId;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
