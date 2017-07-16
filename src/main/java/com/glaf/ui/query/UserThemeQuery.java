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
package com.glaf.ui.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class UserThemeQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Integer> ids;
	protected Collection<String> appActorIds;
	protected String actorIdLike;
	protected String themeStyle;
	protected String themeStyleLike;
	protected List<String> themeStyles;
	protected String layoutModel;
	protected String layoutModelLike;
	protected List<String> layoutModels;
	protected String background;
	protected String backgroundLike;
	protected List<String> backgrounds;
	protected String backgroundType;
	protected String backgroundTypeLike;
	protected List<String> backgroundTypes;
	protected Integer course;
	protected Integer courseGreaterThanOrEqual;
	protected Integer courseLessThanOrEqual;
	protected List<Integer> courses;
	protected String createByLike;
	protected List<String> createBys;
	protected Date createDateGreaterThanOrEqual;
	protected Date createDateLessThanOrEqual;

	public UserThemeQuery() {

	}

	public Collection<String> getAppActorIds() {
		return appActorIds;
	}

	public void setAppActorIds(Collection<String> appActorIds) {
		this.appActorIds = appActorIds;
	}

	public String getActorId() {
		return actorId;
	}

	public String getActorIdLike() {
		if (actorIdLike != null && actorIdLike.trim().length() > 0) {
			if (!actorIdLike.startsWith("%")) {
				actorIdLike = "%" + actorIdLike;
			}
			if (!actorIdLike.endsWith("%")) {
				actorIdLike = actorIdLike + "%";
			}
		}
		return actorIdLike;
	}

	public List<String> getActorIds() {
		return actorIds;
	}

	public String getThemeStyle() {
		return themeStyle;
	}

	public String getThemeStyleLike() {
		if (themeStyleLike != null && themeStyleLike.trim().length() > 0) {
			if (!themeStyleLike.startsWith("%")) {
				themeStyleLike = "%" + themeStyleLike;
			}
			if (!themeStyleLike.endsWith("%")) {
				themeStyleLike = themeStyleLike + "%";
			}
		}
		return themeStyleLike;
	}

	public List<String> getThemeStyles() {
		return themeStyles;
	}

	public String getLayoutModel() {
		return layoutModel;
	}

	public String getLayoutModelLike() {
		if (layoutModelLike != null && layoutModelLike.trim().length() > 0) {
			if (!layoutModelLike.startsWith("%")) {
				layoutModelLike = "%" + layoutModelLike;
			}
			if (!layoutModelLike.endsWith("%")) {
				layoutModelLike = layoutModelLike + "%";
			}
		}
		return layoutModelLike;
	}

	public List<String> getLayoutModels() {
		return layoutModels;
	}

	public String getBackground() {
		return background;
	}

	public String getBackgroundLike() {
		if (backgroundLike != null && backgroundLike.trim().length() > 0) {
			if (!backgroundLike.startsWith("%")) {
				backgroundLike = "%" + backgroundLike;
			}
			if (!backgroundLike.endsWith("%")) {
				backgroundLike = backgroundLike + "%";
			}
		}
		return backgroundLike;
	}

	public List<String> getBackgrounds() {
		return backgrounds;
	}

	public String getBackgroundType() {
		return backgroundType;
	}

	public String getBackgroundTypeLike() {
		if (backgroundTypeLike != null
				&& backgroundTypeLike.trim().length() > 0) {
			if (!backgroundTypeLike.startsWith("%")) {
				backgroundTypeLike = "%" + backgroundTypeLike;
			}
			if (!backgroundTypeLike.endsWith("%")) {
				backgroundTypeLike = backgroundTypeLike + "%";
			}
		}
		return backgroundTypeLike;
	}

	public List<String> getBackgroundTypes() {
		return backgroundTypes;
	}

	public Integer getCourse() {
		return course;
	}

	public Integer getCourseGreaterThanOrEqual() {
		return courseGreaterThanOrEqual;
	}

	public Integer getCourseLessThanOrEqual() {
		return courseLessThanOrEqual;
	}

	public List<Integer> getCourses() {
		return courses;
	}

	public String getCreateBy() {
		return createBy;
	}

	public String getCreateByLike() {
		if (createByLike != null && createByLike.trim().length() > 0) {
			if (!createByLike.startsWith("%")) {
				createByLike = "%" + createByLike;
			}
			if (!createByLike.endsWith("%")) {
				createByLike = createByLike + "%";
			}
		}
		return createByLike;
	}

	public List<String> getCreateBys() {
		return createBys;
	}

	public Date getCreateDateGreaterThanOrEqual() {
		return createDateGreaterThanOrEqual;
	}

	public Date getCreateDateLessThanOrEqual() {
		return createDateLessThanOrEqual;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public void setActorIdLike(String actorIdLike) {
		this.actorIdLike = actorIdLike;
	}

	public void setActorIds(List<String> actorIds) {
		this.actorIds = actorIds;
	}

	public void setThemeStyle(String themeStyle) {
		this.themeStyle = themeStyle;
	}

	public void setThemeStyleLike(String themeStyleLike) {
		this.themeStyleLike = themeStyleLike;
	}

	public void setThemeStyles(List<String> themeStyles) {
		this.themeStyles = themeStyles;
	}

	public void setLayoutModel(String layoutModel) {
		this.layoutModel = layoutModel;
	}

	public void setLayoutModelLike(String layoutModelLike) {
		this.layoutModelLike = layoutModelLike;
	}

	public void setLayoutModels(List<String> layoutModels) {
		this.layoutModels = layoutModels;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setBackgroundLike(String backgroundLike) {
		this.backgroundLike = backgroundLike;
	}

	public void setBackgrounds(List<String> backgrounds) {
		this.backgrounds = backgrounds;
	}

	public void setBackgroundType(String backgroundType) {
		this.backgroundType = backgroundType;
	}

	public void setBackgroundTypeLike(String backgroundTypeLike) {
		this.backgroundTypeLike = backgroundTypeLike;
	}

	public void setBackgroundTypes(List<String> backgroundTypes) {
		this.backgroundTypes = backgroundTypes;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	public void setCourseGreaterThanOrEqual(Integer courseGreaterThanOrEqual) {
		this.courseGreaterThanOrEqual = courseGreaterThanOrEqual;
	}

	public void setCourseLessThanOrEqual(Integer courseLessThanOrEqual) {
		this.courseLessThanOrEqual = courseLessThanOrEqual;
	}

	public void setCourses(List<Integer> courses) {
		this.courses = courses;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateByLike(String createByLike) {
		this.createByLike = createByLike;
	}

	public void setCreateBys(List<String> createBys) {
		this.createBys = createBys;
	}

	public void setCreateDateGreaterThanOrEqual(
			Date createDateGreaterThanOrEqual) {
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
	}

	public void setCreateDateLessThanOrEqual(Date createDateLessThanOrEqual) {
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
	}

	public UserThemeQuery actorId(String actorId) {
		if (actorId == null) {
			throw new RuntimeException("actorId is null");
		}
		this.actorId = actorId;
		return this;
	}

	public UserThemeQuery actorIdLike(String actorIdLike) {
		if (actorIdLike == null) {
			throw new RuntimeException("actorId is null");
		}
		this.actorIdLike = actorIdLike;
		return this;
	}

	public UserThemeQuery actorIds(List<String> actorIds) {
		if (actorIds == null) {
			throw new RuntimeException("actorIds is empty ");
		}
		this.actorIds = actorIds;
		return this;
	}

	public UserThemeQuery themeStyle(String themeStyle) {
		if (themeStyle == null) {
			throw new RuntimeException("themeStyle is null");
		}
		this.themeStyle = themeStyle;
		return this;
	}

	public UserThemeQuery themeStyleLike(String themeStyleLike) {
		if (themeStyleLike == null) {
			throw new RuntimeException("themeStyle is null");
		}
		this.themeStyleLike = themeStyleLike;
		return this;
	}

	public UserThemeQuery themeStyles(List<String> themeStyles) {
		if (themeStyles == null) {
			throw new RuntimeException("themeStyles is empty ");
		}
		this.themeStyles = themeStyles;
		return this;
	}

	public UserThemeQuery layoutModel(String layoutModel) {
		if (layoutModel == null) {
			throw new RuntimeException("layoutModel is null");
		}
		this.layoutModel = layoutModel;
		return this;
	}

	public UserThemeQuery layoutModelLike(String layoutModelLike) {
		if (layoutModelLike == null) {
			throw new RuntimeException("layoutModel is null");
		}
		this.layoutModelLike = layoutModelLike;
		return this;
	}

	public UserThemeQuery layoutModels(List<String> layoutModels) {
		if (layoutModels == null) {
			throw new RuntimeException("layoutModels is empty ");
		}
		this.layoutModels = layoutModels;
		return this;
	}

	public UserThemeQuery background(String background) {
		if (background == null) {
			throw new RuntimeException("background is null");
		}
		this.background = background;
		return this;
	}

	public UserThemeQuery backgroundLike(String backgroundLike) {
		if (backgroundLike == null) {
			throw new RuntimeException("background is null");
		}
		this.backgroundLike = backgroundLike;
		return this;
	}

	public UserThemeQuery backgrounds(List<String> backgrounds) {
		if (backgrounds == null) {
			throw new RuntimeException("backgrounds is empty ");
		}
		this.backgrounds = backgrounds;
		return this;
	}

	public UserThemeQuery backgroundType(String backgroundType) {
		if (backgroundType == null) {
			throw new RuntimeException("backgroundType is null");
		}
		this.backgroundType = backgroundType;
		return this;
	}

	public UserThemeQuery backgroundTypeLike(String backgroundTypeLike) {
		if (backgroundTypeLike == null) {
			throw new RuntimeException("backgroundType is null");
		}
		this.backgroundTypeLike = backgroundTypeLike;
		return this;
	}

	public UserThemeQuery backgroundTypes(List<String> backgroundTypes) {
		if (backgroundTypes == null) {
			throw new RuntimeException("backgroundTypes is empty ");
		}
		this.backgroundTypes = backgroundTypes;
		return this;
	}

	public UserThemeQuery course(Integer course) {
		if (course == null) {
			throw new RuntimeException("course is null");
		}
		this.course = course;
		return this;
	}

	public UserThemeQuery courseGreaterThanOrEqual(
			Integer courseGreaterThanOrEqual) {
		if (courseGreaterThanOrEqual == null) {
			throw new RuntimeException("course is null");
		}
		this.courseGreaterThanOrEqual = courseGreaterThanOrEqual;
		return this;
	}

	public UserThemeQuery courseLessThanOrEqual(Integer courseLessThanOrEqual) {
		if (courseLessThanOrEqual == null) {
			throw new RuntimeException("course is null");
		}
		this.courseLessThanOrEqual = courseLessThanOrEqual;
		return this;
	}

	public UserThemeQuery courses(List<Integer> courses) {
		if (courses == null) {
			throw new RuntimeException("courses is empty ");
		}
		this.courses = courses;
		return this;
	}

	public UserThemeQuery createBy(String createBy) {
		if (createBy == null) {
			throw new RuntimeException("createBy is null");
		}
		this.createBy = createBy;
		return this;
	}

	public UserThemeQuery createByLike(String createByLike) {
		if (createByLike == null) {
			throw new RuntimeException("createBy is null");
		}
		this.createByLike = createByLike;
		return this;
	}

	public UserThemeQuery createBys(List<String> createBys) {
		if (createBys == null) {
			throw new RuntimeException("createBys is empty ");
		}
		this.createBys = createBys;
		return this;
	}

	public UserThemeQuery createDateGreaterThanOrEqual(
			Date createDateGreaterThanOrEqual) {
		if (createDateGreaterThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateGreaterThanOrEqual = createDateGreaterThanOrEqual;
		return this;
	}

	public UserThemeQuery createDateLessThanOrEqual(
			Date createDateLessThanOrEqual) {
		if (createDateLessThanOrEqual == null) {
			throw new RuntimeException("createDate is null");
		}
		this.createDateLessThanOrEqual = createDateLessThanOrEqual;
		return this;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("actorId".equals(sortColumn)) {
				orderBy = "E.ACTORID_" + a_x;
			}

			if ("themeStyle".equals(sortColumn)) {
				orderBy = "E.THEMESTYLE_" + a_x;
			}

			if ("layoutModel".equals(sortColumn)) {
				orderBy = "E.LAYOUTMODEL_" + a_x;
			}

			if ("background".equals(sortColumn)) {
				orderBy = "E.BACKGROUND_" + a_x;
			}

			if ("backgroundType".equals(sortColumn)) {
				orderBy = "E.BACKGROUNDTYPE_" + a_x;
			}

			if ("course".equals(sortColumn)) {
				orderBy = "E.COURSE_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createDate".equals(sortColumn)) {
				orderBy = "E.CREATEDATE_" + a_x;
			}

		}
		return orderBy;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("actorId", "ACTORID_");
		addColumn("themeStyle", "THEMESTYLE_");
		addColumn("layoutModel", "LAYOUTMODEL_");
		addColumn("background", "BACKGROUND_");
		addColumn("backgroundType", "BACKGROUNDTYPE_");
		addColumn("course", "COURSE_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createDate", "CREATEDATE_");
	}

}