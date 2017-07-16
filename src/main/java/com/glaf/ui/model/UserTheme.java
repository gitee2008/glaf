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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.glaf.core.util.DateUtils;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "UI_USER_THEME")
public class UserTheme implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected Integer id;

	@Column(name = "ACTORID_", length = 50)
	protected String actorId;

	@Column(name = "THEMESTYLE_", length = 20)
	protected String themeStyle;
	
	@Column(name = "HOMETHEMESTYLE_", length = 20)
	protected String homeThemeStyle;

	@Column(name = "LAYOUTMODEL_", length = 20)
	protected String layoutModel;

	@Column(name = "BACKGROUND_", length = 100)
	protected String background;

	@Column(name = "BACKGROUNDTYPE_", length = 20)
	protected String backgroundType;

	@Column(name = "COURSE_")
	protected Integer course;

	@Column(name = "CREATEBY_", length = 50)
	protected String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE_")
	protected Date createDate;

	public UserTheme() {

	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActorId() {
		return this.actorId;
	}

	public String getThemeStyle() {
		return this.themeStyle;
	}

	public String getLayoutModel() {
		return this.layoutModel;
	}

	public String getBackground() {
		return this.background;
	}

	public String getBackgroundType() {
		return this.backgroundType;
	}

	public Integer getCourse() {
		return this.course;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCreateDateString() {
		if (this.createDate != null) {
			return DateUtils.getDateTime(this.createDate);
		}
		return "";
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setThemeStyle(String themeStyle) {
		this.themeStyle = themeStyle;
	}

	public String getHomeThemeStyle() {
		return homeThemeStyle;
	}

	public void setHomeThemeStyle(String homeThemeStyle) {
		this.homeThemeStyle = homeThemeStyle;
	}

	public void setLayoutModel(String layoutModel) {
		this.layoutModel = layoutModel;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setBackgroundType(String backgroundType) {
		this.backgroundType = backgroundType;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserTheme other = (UserTheme) obj;
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

	// public UserTheme jsonToObject(JSONObject jsonObject) {
	// return UserThemeJsonFactory.jsonToObject(jsonObject);
	// }
	//
	//
	// public JSONObject toJsonObject() {
	// return UserThemeJsonFactory.toJsonObject(this);
	// }
	//
	// public ObjectNode toObjectNode(){
	// return UserThemeJsonFactory.toObjectNode(this);
	// }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
