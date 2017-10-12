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

package com.glaf.core.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.glaf.core.base.*;
import com.glaf.core.domain.util.*;

/**
 * 
 * 实体对象
 * 
 */

@Entity
@Table(name = "SYS_DATABASE")
public class Database implements java.lang.Comparable<Database>, Cloneable, Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "PARENTID_")
	protected long parentId;

	/**
	 * 名称
	 */
	@Column(name = "NAME_", length = 200)
	protected String name;

	/**
	 * 代码
	 */
	@Column(name = "CODE_", length = 50)
	protected String code;

	/**
	 * 鉴别符
	 */
	@Column(name = "DISCRIMINATOR_", length = 10)
	protected String discriminator;

	/**
	 * 映射名
	 */
	@Column(name = "MAPPING_", length = 50)
	protected String mapping;

	/**
	 * 标段
	 */
	@Column(name = "SECTION_", length = 50)
	protected String section;

	/**
	 * 标题
	 */
	@Column(name = "TITLE_", length = 100)
	protected String title;

	/**
	 * 主机
	 */
	@Column(name = "HOST_", length = 100)
	protected String host;

	/**
	 * 端口
	 */
	@Column(name = "PORT_")
	protected int port;

	/**
	 * 用户名
	 */
	@Column(name = "USER_", length = 50)
	protected String user;

	/**
	 * 密码
	 */
	@Column(name = "PASSWORD_", length = 2000)
	protected String password;

	/**
	 * 密锁
	 */
	@Column(name = "KEY_", length = 1024)
	protected String key;

	/**
	 * IntToken
	 */
	@Column(name = "INTTOKEN_")
	protected int intToken;

	/**
	 * Token
	 */
	@Column(name = "TOKEN_", length = 200)
	protected String token;

	/**
	 * 数据库类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

	/**
	 * 运行类型，模板库TPL还是实例库INST
	 */
	@Column(name = "RUNTYPE_", length = 50)
	protected String runType;

	/**
	 * 使用类型
	 */
	@Column(name = "USEYPE_", length = 50)
	protected String useType;

	/**
	 * 级别
	 */
	@Column(name = "LEVEL_")
	protected int level = 0;

	/**
	 * 优先级
	 */
	@Column(name = "PRIORITY_")
	protected int priority = 0;

	/**
	 * 读写操作，支持存储库读写分离<br/>
	 * 0-只读 <br/>
	 * 1-只写 <br/>
	 * 2-读写<br/>
	 */
	@Column(name = "OPERATION_")
	protected int operation = 2;

	/**
	 * 库名
	 */
	@Column(name = "DBNAME_", length = 50)
	protected String dbname;

	/**
	 * 分区名
	 */
	@Column(name = "BUCKET_", length = 50)
	protected String bucket;

	/**
	 * Catalog
	 */
	@Column(name = "CATALOG_", length = 50)
	protected String catalog;

	/**
	 * InfoServer
	 */
	@Column(name = "INFOSERVER_", length = 50)
	protected String infoServer;

	/**
	 * LoginAs
	 */
	@Column(name = "LOGINAS_", length = 50)
	protected String loginAs;

	/**
	 * LoginUrl
	 */
	@Column(name = "LOGINURL_", length = 250)
	protected String loginUrl;

	/**
	 * Ticket
	 */
	@Column(name = "TICKET_", length = 100)
	protected String ticket;

	/**
	 * ProgramId
	 */
	@Column(name = "PROGRAMID_", length = 250)
	protected String programId;

	/**
	 * ProgramName
	 */
	@Column(name = "PROGRAMNAME_", length = 250)
	protected String programName;

	/**
	 * UserNameKey
	 */
	@Column(name = "USERNAMEKEY_", length = 100)
	protected String userNameKey;

	/**
	 * ServerId
	 */
	@Column(name = "SERVERID_")
	protected long serverId;

	/**
	 * SysId
	 */
	@Column(name = "SYSID_", length = 50)
	protected String sysId;

	/**
	 * QueueName
	 */
	@Column(name = "QUEUENAME_", length = 200)
	protected String queueName;

	/**
	 * 激活标记
	 */
	@Column(name = "ACTIVE_", length = 10)
	protected String active;

	/**
	 * 验证标记
	 */
	@Column(name = "VERIFY_", length = 10)
	protected String verify;

	/**
	 * 删除标记
	 */
	@Column(name = "REMOVEFLAG_", length = 10)
	protected String removeFlag;

	/**
	 * 初始化标记
	 */
	@Column(name = "INITFLAG_", length = 10)
	protected String initFlag;

	/**
	 * 提供者类名
	 */
	@Column(name = "PROVIDERCLASS_", length = 100)
	protected String providerClass;

	/**
	 * 远程地址
	 */
	@Column(name = "REMOTEURL_", length = 500)
	protected String remoteUrl;

	/**
	 * 顺序号
	 */
	@Column(name = "SORTNO_")
	protected int sort;

	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME_")
	protected Date createTime;

	@Column(name = "UPDATEBY_", length = 50)
	protected String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME_")
	protected Date updateTime;

	@javax.persistence.Transient
	protected String selected;

	@javax.persistence.Transient
	protected String connectionString;

	@javax.persistence.Transient
	protected Collection<String> actorIds = new HashSet<String>();

	@javax.persistence.Transient
	protected List<DatabaseAccess> accesses = new ArrayList<DatabaseAccess>();

	public Database() {

	}

	public void addAccessor(String actorId) {
		if (actorIds == null) {
			actorIds = new HashSet<String>();
		}
		actorIds.add(actorId);
	}

	@Override
	public Database clone() {
		Database model = new Database();
		model.setActive(this.getActive());
		model.setCode(this.getCode());
		model.setConnectionString(this.getConnectionString());
		model.setCreateBy(this.getCreateBy());
		model.setCreateTime(this.getCreateTime());
		model.setDbname(this.getDbname());
		model.setBucket(this.getBucket());
		model.setHost(this.getHost());
		model.setId(this.getId());
		model.setInitFlag(this.getInitFlag());
		model.setIntToken(this.getIntToken());
		model.setToken(this.getToken());
		model.setLevel(this.getLevel());
		model.setKey(this.getKey());
		model.setMapping(this.getMapping());
		model.setName(this.getName());
		model.setParentId(this.getParentId());
		model.setOperation(this.getOperation());
		model.setPassword(this.getPassword());
		model.setPort(this.getPort());
		model.setPriority(this.getPriority());
		model.setProviderClass(this.getProviderClass());
		model.setTitle(this.getTitle());
		model.setType(this.getType());
		model.setUpdateBy(this.getUpdateBy());
		model.setUpdateTime(this.getUpdateTime());
		model.setUser(this.getUser());
		model.setVerify(this.getVerify());
		model.setCatalog(this.getCatalog());
		model.setInfoServer(this.getInfoServer());
		model.setLoginAs(this.getLoginAs());
		model.setLoginUrl(this.getLoginUrl());
		model.setTicket(this.getTicket());
		model.setProgramId(this.getProgramId());
		model.setProgramName(this.getProgramName());
		model.setUserNameKey(this.getUserNameKey());
		model.setServerId(this.getServerId());
		model.setSysId(this.getSysId());
		model.setQueueName(this.getQueueName());
		return model;
	}

	public int compareTo(Database other) {
		if (other == null) {
			return -1;
		}

		Database field = other;

		int l = this.priority - field.getPriority();

		int ret = 0;

		if (l > 0) {
			ret = -1;
		} else if (l < 0) {
			ret = 1;
		}
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Database other = (Database) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public List<DatabaseAccess> getAccesses() {
		return accesses;
	}

	public String getActive() {
		return this.active;
	}

	public Collection<String> getActorIds() {
		return actorIds;
	}

	public String getBucket() {
		return bucket;
	}

	public String getCatalog() {
		return catalog;
	}

	public String getCode() {
		return code;
	}

	public String getConnectionString() {
		return this.connectionString;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getDbname() {
		return dbname;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public String getHost() {
		return this.host;
	}

	public long getId() {
		return this.id;
	}

	public String getInfoServer() {
		return infoServer;
	}

	public String getInitFlag() {
		return initFlag;
	}

	public int getIntToken() {
		if (intToken < 1000 || intToken > 9999) {
			Random r = new Random();
			intToken = Math.abs(1000 + r.nextInt(8999));
		}
		return intToken;
	}

	public String getKey() {
		return key;
	}

	public int getLevel() {
		return level;
	}

	public String getLoginAs() {
		return loginAs;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public String getMapping() {
		return mapping;
	}

	public String getName() {
		return name;
	}

	public int getOperation() {
		return operation;
	}

	public long getParentId() {
		return this.parentId;
	}

	public String getPassword() {
		return this.password;
	}

	public int getPort() {
		return this.port;
	}

	public int getPriority() {
		return priority;
	}

	public String getProgramId() {
		return programId;
	}

	public String getProgramName() {
		return programName;
	}

	public String getProviderClass() {
		return providerClass;
	}

	public String getQueueName() {
		if (queueName == null) {
			/*
			 * if (this.getHost() != null && this.getDbname() != null) { queueName =
			 * this.getHost() + "_" + this.getDbname(); queueName =
			 * StringTools.replace(queueName, ".", "_"); queueName =
			 * queueName.toLowerCase(); }
			 */
			queueName = this.getSysId();
		}
		return queueName;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public String getRemoveFlag() {
		return removeFlag;
	}

	public String getRunType() {
		return runType;
	}

	public String getSection() {
		return section;
	}

	public String getSelected() {
		return selected;
	}

	public long getServerId() {
		return serverId;
	}

	public int getSort() {
		return sort;
	}

	public String getSysId() {
		return sysId;
	}

	public String getTicket() {
		return ticket;
	}

	public String getTitle() {
		return title;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return this.type;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getUser() {
		return this.user;
	}

	public String getUserNameKey() {
		return userNameKey;
	}

	public String getUseType() {
		return useType;
	}

	public String getVerify() {
		return verify;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public Database jsonToObject(JSONObject jsonObject) {
		return DatabaseJsonFactory.jsonToObject(jsonObject);
	}

	public void setAccesses(List<DatabaseAccess> accesses) {
		this.accesses = accesses;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setActorIds(Collection<String> actorIds) {
		this.actorIds = actorIds;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setInfoServer(String infoServer) {
		this.infoServer = infoServer;
	}

	public void setInitFlag(String initFlag) {
		this.initFlag = initFlag;
	}

	public void setIntToken(int intToken) {
		this.intToken = intToken;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLoginAs(String loginAs) {
		this.loginAs = loginAs;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public void setProviderClass(String providerClass) {
		this.providerClass = providerClass;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public void setRemoveFlag(String removeFlag) {
		this.removeFlag = removeFlag;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setUserNameKey(String userNameKey) {
		this.userNameKey = userNameKey;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public JSONObject toJsonObject() {
		return DatabaseJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return DatabaseJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
