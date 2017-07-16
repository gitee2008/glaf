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

import java.util.Date;

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
@Table(name = "SYS_SERVER")
public class ServerEntity implements java.lang.Comparable<ServerEntity>, Cloneable, Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	@Column(name = "NODEID_")
	protected Long nodeId;

	@Column(name = "NAME_", length = 200)
	protected String name;

	@Column(name = "CODE_", length = 50)
	protected String code;

	/**
	 * 鉴别符
	 */
	@Column(name = "DISCRIMINATOR_", length = 10)
	protected String discriminator;

	@Column(name = "MAPPING_", length = 50)
	protected String mapping;

	@Column(name = "TITLE_", length = 100)
	protected String title;

	@Column(name = "HOST_", length = 100)
	protected String host;

	@Column(name = "PORT_")
	protected int port;

	@Column(name = "USER_", length = 50)
	protected String user;

	@Column(name = "PASSWORD_", length = 100)
	protected String password;

	@Column(name = "KEY_", length = 1024)
	protected String key;

	@Column(name = "TYPE_", length = 50)
	protected String type;

	@Column(name = "LEVEL_")
	protected int level = 0;

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
	 * Catalog
	 */
	@Column(name = "CATALOG_", length = 50)
	protected String catalog;

	@Column(name = "DBNAME_", length = 50)
	protected String dbname;

	/**
	 * Path
	 */
	@Column(name = "PATH_", length = 200)
	protected String path;
	
    /**
     * Program
     */
    @Column(name = "PROGRAM_", length=500) 
    protected String program;

	@Column(name = "ACTIVE_", length = 10)
	protected String active;

	@Column(name = "VERIFY_", length = 10)
	protected String verify;

	@Column(name = "INITFLAG_", length = 10)
	protected String initFlag;

	@Column(name = "DETECTIONFLAG_", length = 10)
	protected String detectionFlag;

	@Column(name = "PROVIDERCLASS_", length = 100)
	protected String providerClass;

	@Column(name = "VERIFYCLASS_", length = 100)
	protected String verifyClass;

	/**
	 * 加密算法
	 */
	@Column(name = "SECRETALGORITHM_", length = 50)
	protected String secretAlgorithm;

	/**
	 * 加密密锁
	 */
	@Lob
	@Column(name = "SECRETKEY_", length = 4000)
	protected String secretKey;

	/**
	 * 向量
	 */
	@Column(name = "SECRETIV_", length = 200)
	protected String secretIv;

	/**
	 * 允许地址列表
	 */
	@Column(name = "ADDRESSPERMS_", length = 1000)
	protected String addressPerms;

	/**
	 * 允许权限列表
	 */
	@Column(name = "PERMS_", length = 1000)
	protected String perms;

	@Column(name = "ATTRIBUTE_", length = 500)
	protected String attribute;

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
	protected String connectionString;

	public ServerEntity() {

	}
	
	

	public String getProgram() {
		return program;
	}



	public void setProgram(String program) {
		this.program = program;
	}



	@Override
	public ServerEntity clone() {
		ServerEntity model = new ServerEntity();
		model.setActive(this.getActive());
		model.setAttribute(this.getAttribute());
		model.setAddressPerms(this.getAddressPerms());
		model.setPerms(this.getPerms());
		model.setCode(this.getCode());
		model.setDiscriminator(this.getDiscriminator());
		model.setConnectionString(this.getConnectionString());
		model.setCreateBy(this.getCreateBy());
		model.setCreateTime(this.getCreateTime());
		model.setCatalog(this.getCatalog());
		model.setDbname(this.getDbname());
		model.setHost(this.getHost());
		model.setId(this.getId());
		model.setInitFlag(this.getInitFlag());
		model.setLevel(this.getLevel());
		model.setKey(this.getKey());
		model.setMapping(this.getMapping());
		model.setName(this.getName());
		model.setNodeId(this.getNodeId());
		model.setOperation(this.getOperation());
		model.setPassword(this.getPassword());
		model.setPort(this.getPort());
		model.setPriority(this.getPriority());
		model.setProviderClass(this.getProviderClass());
		model.setTitle(this.getTitle());
		model.setType(this.getType());
		model.setPath(this.getPath());
		model.setProgram(this.getProgram());
		model.setUpdateBy(this.getUpdateBy());
		model.setUpdateTime(this.getUpdateTime());
		model.setUser(this.getUser());
		model.setVerify(this.getVerify());
		model.setSecretAlgorithm(this.getSecretAlgorithm());
		model.setSecretKey(this.getSecretKey());
		model.setSecretIv(this.getSecretIv());

		return model;
	}

	public int compareTo(ServerEntity other) {
		if (other == null) {
			return -1;
		}

		ServerEntity field = other;

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
		ServerEntity other = (ServerEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public String getActive() {
		return this.active;
	}

	public String getAddressPerms() {
		return addressPerms;
	}

	public String getAttribute() {
		return attribute;
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

	public String getDetectionFlag() {
		return detectionFlag;
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

	public String getInitFlag() {
		return initFlag;
	}

	public String getKey() {
		return key;
	}

	public int getLevel() {
		return level;
	}

	public String getMapping() {
		return mapping;
	}

	public String getName() {
		return name;
	}

	public Long getNodeId() {
		return this.nodeId;
	}

	public int getOperation() {
		return operation;
	}

	public String getPassword() {
		return this.password;
	}

	public String getPath() {
		return path;
	}

	public String getPerms() {
		return perms;
	}

	public int getPort() {
		return this.port;
	}

	public int getPriority() {
		return priority;
	}

	public String getProviderClass() {
		return providerClass;
	}

	public String getSecretAlgorithm() {
		return secretAlgorithm;
	}

	public String getSecretIv() {
		return secretIv;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getTitle() {
		return title;
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

	public String getVerify() {
		return verify;
	}

	public String getVerifyClass() {
		return verifyClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public ServerEntity jsonToObject(JSONObject jsonObject) {
		return ServerEntityJsonFactory.jsonToObject(jsonObject);
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setAddressPerms(String addressPerms) {
		this.addressPerms = addressPerms;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
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

	public void setDetectionFlag(String detectionFlag) {
		this.detectionFlag = detectionFlag;
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

	public void setInitFlag(String initFlag) {
		this.initFlag = initFlag;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setProviderClass(String providerClass) {
		this.providerClass = providerClass;
	}

	public void setSecretAlgorithm(String secretAlgorithm) {
		this.secretAlgorithm = secretAlgorithm;
	}

	public void setSecretIv(String secretIv) {
		this.secretIv = secretIv;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public void setVerifyClass(String verifyClass) {
		this.verifyClass = verifyClass;
	}

	public JSONObject toJsonObject() {
		return ServerEntityJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return ServerEntityJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
