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

package com.glaf.sms.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glaf.core.base.JSONable;
import com.glaf.core.util.DateUtils;
import com.glaf.sms.util.SmsMessageJsonFactory;

/**
 * 
 * 短信体
 *
 */

@Entity
@Table(name = "SMS_MESSAGE")
public class SmsMessage implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 系统编号
	 */
	@Column(name = "CLIENTID_", length = 50)
	protected String clientId;

	/**
	 * 服务器系统编号
	 */
	@Column(name = "SERVERID_", length = 50)
	protected String serverId;

	/**
	 * 姓名
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 手机号码
	 */
	@Column(name = "MOBILE_", length = 20)
	protected String mobile;

	/**
	 * 标题
	 */
	@Column(name = "SUBJECT_", length = 200)
	protected String subject;

	/**
	 * 消息体
	 */
	@Column(name = "MESSAGE_", length = 4000)
	protected String message;

	/**
	 * 重试次数
	 */
	@Column(name = "RETRYTIMES_")
	protected int retryTimes;

	/**
	 * 待发送日期，即等到什么时候之后才发送短信
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SENDLATERTIME_")
	protected Date sendLaterTime;

	/**
	 * 发送日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SENDTIME_")
	protected Date sendTime;

	/**
	 * 发送状态
	 */
	@Column(name = "STATUS_")
	protected int status;

	/**
	 * 年
	 */
	@Column(name = "YEAR_")
	protected int year;

	/**
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

	/**
	 * 年月日
	 */
	@Column(name = "FULLDAY_")
	protected int fullDay;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	/**
	 * 返回结果
	 */
	@Column(name = "RESULT_", length = 200)
	protected String result;

	/**
	 * 限制条数
	 */
	@Column(name = "LIMIT_")
	protected int limit;

	public SmsMessage() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmsMessage other = (SmsMessage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getClientId() {
		return this.clientId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public String getCreateTimeString() {
		if (this.createTime != null) {
			return DateUtils.getDateTime(this.createTime);
		}
		return "";
	}

	public int getFullDay() {
		return this.fullDay;
	}

	public String getId() {
		return this.id;
	}

	public int getLimit() {
		return limit;
	}

	public String getMessage() {
		return this.message;
	}

	public String getMobile() {
		return this.mobile;
	}

	public int getMonth() {
		return this.month;
	}

	public String getName() {
		return this.name;
	}

	public String getResult() {
		return result;
	}

	public int getRetryTimes() {
		return this.retryTimes;
	}

	public Date getSendLaterTime() {
		return this.sendLaterTime;
	}

	public String getSendLaterTimeString() {
		if (this.sendLaterTime != null) {
			return DateUtils.getDateTime(this.sendLaterTime);
		}
		return "";
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public String getSendTimeString() {
		if (this.sendTime != null) {
			return DateUtils.getDateTime(this.sendTime);
		}
		return "";
	}

	public String getServerId() {
		return this.serverId;
	}

	public int getStatus() {
		return this.status;
	}

	public String getSubject() {
		return this.subject;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public SmsMessage jsonToObject(JSONObject jsonObject) {
		return SmsMessageJsonFactory.jsonToObject(jsonObject);
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFullDay(int fullDay) {
		this.fullDay = fullDay;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public void setSendLaterTime(Date sendLaterTime) {
		this.sendLaterTime = sendLaterTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public JSONObject toJsonObject() {
		return SmsMessageJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SmsMessageJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
