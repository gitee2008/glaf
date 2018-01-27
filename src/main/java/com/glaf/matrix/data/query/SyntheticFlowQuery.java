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

package com.glaf.matrix.data.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class SyntheticFlowQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected String currentStep;
	protected String currentType;
	protected String previousStep;
	protected String previousType;
	protected String nextStep;
	protected String nextType;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;

	public SyntheticFlowQuery() {

	}

	public SyntheticFlowQuery createTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SyntheticFlowQuery createTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SyntheticFlowQuery currentStep(String currentStep) {
		if (currentStep == null) {
			throw new RuntimeException("currentStep is null");
		}
		this.currentStep = currentStep;
		return this;
	}

	public SyntheticFlowQuery currentType(String currentType) {
		if (currentType == null) {
			throw new RuntimeException("currentType is null");
		}
		this.currentType = currentType;
		return this;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public String getCurrentType() {
		return currentType;
	}

	public String getNextStep() {
		return nextStep;
	}

	public String getNextType() {
		return nextType;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("currentStep".equals(sortColumn)) {
				orderBy = "E.CURRENTSTEP_" + a_x;
			}

			if ("currentType".equals(sortColumn)) {
				orderBy = "E.CURRENTTYPE_" + a_x;
			}

			if ("previousStep".equals(sortColumn)) {
				orderBy = "E.PREVIOUSSTEP_" + a_x;
			}

			if ("previousType".equals(sortColumn)) {
				orderBy = "E.PREVIOUSTYPE_" + a_x;
			}

			if ("nextStep".equals(sortColumn)) {
				orderBy = "E.NEXTSTEP_" + a_x;
			}

			if ("nextType".equals(sortColumn)) {
				orderBy = "E.NEXTTYPE_" + a_x;
			}

			if ("sort".equals(sortColumn)) {
				orderBy = "E.SORT_" + a_x;
			}

			if ("createBy".equals(sortColumn)) {
				orderBy = "E.CREATEBY_" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME_" + a_x;
			}

		}
		return orderBy;
	}

	public String getPreviousStep() {
		return previousStep;
	}

	public String getPreviousType() {
		return previousType;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID_");
		addColumn("currentStep", "CURRENTSTEP_");
		addColumn("currentType", "CURRENTTYPE_");
		addColumn("previousStep", "PREVIOUSSTEP_");
		addColumn("previousType", "PREVIOUSTYPE_");
		addColumn("nextStep", "NEXTSTEP_");
		addColumn("nextType", "NEXTTYPE_");
		addColumn("sort", "SORT_");
		addColumn("createBy", "CREATEBY_");
		addColumn("createTime", "CREATETIME_");
	}

	public SyntheticFlowQuery nextStep(String nextStep) {
		if (nextStep == null) {
			throw new RuntimeException("nextStep is null");
		}
		this.nextStep = nextStep;
		return this;
	}

	public SyntheticFlowQuery nextType(String nextType) {
		if (nextType == null) {
			throw new RuntimeException("nextType is null");
		}
		this.nextType = nextType;
		return this;
	}

	public SyntheticFlowQuery previousStep(String previousStep) {
		if (previousStep == null) {
			throw new RuntimeException("previousStep is null");
		}
		this.previousStep = previousStep;
		return this;
	}

	public SyntheticFlowQuery previousType(String previousType) {
		if (previousType == null) {
			throw new RuntimeException("previousType is null");
		}
		this.previousType = previousType;
		return this;
	}

	public void setCreateTimeGreaterThanOrEqual(Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	public void setCurrentType(String currentType) {
		this.currentType = currentType;
	}

	public void setNextStep(String nextStep) {
		this.nextStep = nextStep;
	}

	public void setNextType(String nextType) {
		this.nextType = nextType;
	}

	public void setPreviousStep(String previousStep) {
		this.previousStep = previousStep;
	}

	public void setPreviousType(String previousType) {
		this.previousType = previousType;
	}

}