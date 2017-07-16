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
package com.glaf.ui.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "UI_PANEL_BUTTON")
public class PanelButton implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected String id;

	@Column(name = "PID_")
	protected Integer pid;

	@Column(name = "OPENTYPE_")
	protected Integer openType;

	@Column(name = "IMGURL_", length = 100)
	protected String imgUrl;

	@Column(name = "HREF_", length = 100)
	protected String href;

	@Column(name = "SCRIPT_", length = 100)
	protected String script;

	public PanelButton() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPid() {
		return this.pid;
	}

	public Integer getOpenType() {
		return this.openType;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public String getHref() {
		return this.href;
	}

	public String getScript() {
		return this.script;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PanelButton other = (PanelButton) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	// public PanelButton jsonToObject(JSONObject jsonObject) {
	// return PanelButtonJsonFactory.jsonToObject(jsonObject);
	// }
	//
	//
	// public JSONObject toJsonObject() {
	// return PanelButtonJsonFactory.toJsonObject(this);
	// }
	//
	// public ObjectNode toObjectNode(){
	// return PanelButtonJsonFactory.toObjectNode(this);
	// }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
