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
import com.glaf.sms.util.SmsServerJsonFactory;

/**
 * 
 * 短信服务器或网关配置
 *
 */

@Entity
@Table(name = "SMS_SERVER")
public class SmsServer implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 50, nullable = false)
	protected String id;

	/**
	 * 主题
	 */
	@Column(name = "SUBJECT_", length = 200)
	protected String subject;

	/**
	 * SMS短信服务器IP地址
	 */
	@Column(name = "SERVERIP_", length = 200)
	protected String serverIP;

	/**
	 * 端口
	 */
	@Column(name = "PORT_")
	protected int port;

	/**
	 * 路径
	 */
	@Column(name = "PATH_", length = 500)
	protected String path;

	/**
	 * 请求消息体
	 */
	@Column(name = "REQUESTBODY_", length = 4000)
	protected String requestBody;

	/**
	 * 响应消息体
	 */
	@Column(name = "RESPONSEBODY_", length = 4000)
	protected String responseBody;

	/**
	 * 响应结果
	 */
	@Column(name = "RESPONSERESULT_", length = 200)
	protected String responseResult;

	/**
	 * 发送频率
	 */
	@Column(name = "FREQUENCE_")
	protected int frequence;

	/**
	 * 重试次数
	 */
	@Column(name = "RETRYTIMES_")
	protected int retryTimes;

	/**
	 * 密锁
	 */
	@Column(name = "KEY_", length = 500)
	protected String key;

	/**
	 * 访问Key
	 */
	@Column(name = "ACCESSKEYID_", length = 200)
	protected String accessKeyId;

	/**
	 * 访问密锁
	 */
	@Column(name = "ACCESSKEYSECRET_", length = 200)
	protected String accessKeySecret;

	/**
	 * 签名
	 */
	@Column(name = "SIGNNAME_", length = 200)
	protected String signName;

	/**
	 * 模板代码
	 */
	@Column(name = "TEMPLATECODE_", length = 500)
	protected String templateCode;

	/**
	 * 厂家
	 */
	@Column(name = "PROVIDER_", length = 50)
	protected String provider;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 是否锁定
	 */
	@Column(name = "LOCKED_")
	protected int locked;

	/**
	 * 创建人
	 */
	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	public SmsServer() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmsServer other = (SmsServer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public String getCreateBy() {
		return this.createBy;
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

	public int getFrequence() {
		return frequence;
	}

	public String getId() {
		return this.id;
	}

	public String getKey() {
		return this.key;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getPath() {
		return this.path;
	}

	public int getPort() {
		return this.port;
	}

	public String getProvider() {
		return provider;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public String getResponseResult() {
		return responseResult;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public String getServerIP() {
		return this.serverIP;
	}

	public String getSignName() {
		return signName;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public SmsServer jsonToObject(JSONObject jsonObject) {
		return SmsServerJsonFactory.jsonToObject(jsonObject);
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public void setResponseResult(String responseResult) {
		this.responseResult = responseResult;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return SmsServerJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SmsServerJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
