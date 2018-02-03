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
import com.glaf.sms.util.SmsClientJsonFactory;

/**
 * 
 * 短信应用端
 *
 */

@Entity
@Table(name = "SMS_CLIENT")
public class SmsClient implements Serializable, JSONable {
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
	 * 发送端IP地址
	 */
	@Column(name = "REMOTEIP_", length = 200)
	protected String remoteIP;

	/**
	 * 系统代码
	 */
	@Column(name = "SYSCODE_", length = 200)
	protected String sysCode;

	/**
	 * 系统密码
	 */
	@Column(name = "SYSPWD_", length = 500)
	protected String sysPwd;

	/**
	 * 加密标识 ALL-全部,DATA-仅数据
	 */
	@Column(name = "ENCRYPTIONFLAG_", length = 50)
	protected String encryptionFlag;

	/**
	 * 公钥
	 */
	@Column(name = "PUBLICKEY_", length = 4000)
	protected String publicKey;

	/**
	 * 私钥
	 */
	@Column(name = "PRIVATEKEY_", length = 4000)
	protected String privateKey;

	/**
	 * 对方公钥
	 */
	@Column(name = "PEERPUBLICKEY_", length = 4000)
	protected String peerPublicKey;

	/**
	 * 动态令牌
	 */
	@Column(name = "TOKEN_", length = 200)
	protected String token;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 发送频率
	 */
	@Column(name = "FREQUENCE_")
	protected int frequence;

	/**
	 * 限制条数
	 */
	@Column(name = "LIMIT_")
	protected int limit;

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

	public SmsClient() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmsClient other = (SmsClient) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public String getEncryptionFlag() {
		return encryptionFlag;
	}

	public int getFrequence() {
		return this.frequence;
	}

	public String getId() {
		return this.id;
	}

	public int getLimit() {
		return this.limit;
	}

	public int getLocked() {
		return this.locked;
	}

	public String getPeerPublicKey() {
		return this.peerPublicKey;
	}

	public String getPrivateKey() {
		return this.privateKey;
	}

	public String getPublicKey() {
		return this.publicKey;
	}

	public String getRemoteIP() {
		return this.remoteIP;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getSysCode() {
		return this.sysCode;
	}

	public String getSysPwd() {
		return this.sysPwd;
	}

	public String getToken() {
		return this.token;
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

	public SmsClient jsonToObject(JSONObject jsonObject) {
		return SmsClientJsonFactory.jsonToObject(jsonObject);
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setEncryptionFlag(String encryptionFlag) {
		this.encryptionFlag = encryptionFlag;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}

	public void setPeerPublicKey(String peerPublicKey) {
		this.peerPublicKey = peerPublicKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public void setSysPwd(String sysPwd) {
		this.sysPwd = sysPwd;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject toJsonObject() {
		return SmsClientJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return SmsClientJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
