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

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.sms.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "SMS_VERIFY_MESSAGE")
public class SmsVerifyMessage implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

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
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 验证码
	 */
	@Column(name = "VERIFICATIONCODE_", length = 20)
	protected String verificationCode;

	/**
	 * 发送状态
	 */
	@Column(name = "STATUS_")
	protected int status;

	/**
	 * 发送时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SENDTIME_")
	protected Date sendTime;

	/**
	 * 发送时间戳
	 */
	@Column(name = "SENDTIMEMS_")
	protected long sendTimeMs;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public SmsVerifyMessage() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmsVerifyMessage other = (SmsVerifyMessage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public String getId() {
		return this.id;
	}

	public String getMobile() {
		return this.mobile;
	}

	public String getName() {
		return this.name;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public long getSendTimeMs() {
		return this.sendTimeMs;
	}

	public String getSendTimeString() {
		if (this.sendTime != null) {
			return DateUtils.getDateTime(this.sendTime);
		}
		return "";
	}

	public int getStatus() {
		return this.status;
	}

	public String getType() {
		return this.type;
	}

	public String getVerificationCode() {
		return this.verificationCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public SmsVerifyMessage jsonToObject(JSONObject jsonObject) {
		return SmsVerifyMessageJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public void setSendTimeMs(long sendTimeMs) {
		this.sendTimeMs = sendTimeMs;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public JSONObject toJsonObject() {
		return SmsVerifyMessageJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SmsVerifyMessageJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
