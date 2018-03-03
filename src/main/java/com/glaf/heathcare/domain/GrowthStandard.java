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

package com.glaf.heathcare.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.core.util.DateUtils;
import com.glaf.heathcare.util.*;

/**
 * 
 * 实体对象
 *
 */

@Entity
@Table(name = "HEALTH_GROWTH_STANDARD")
public class GrowthStandard implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", nullable = false)
	protected long id;

	/**
	 * 年龄
	 */
	@Column(name = "AGE_")
	protected int age;

	/**
	 * 月
	 */
	@Column(name = "MONTH_")
	protected int month;

	/**
	 * 月龄
	 */
	@Column(name = "AGEOFTHEMOON_")
	protected int ageOfTheMoon;

	/**
	 * 复合键
	 */
	@Column(name = "COMPLEXKEY_", length = 200)
	protected String complexKey;

	/**
	 * 性别
	 */
	@Column(name = "SEX_", length = 1)
	protected String sex;

	/**
	 * 身高
	 */
	@Column(name = "HEIGHT_")
	protected double height;

	/**
	 * 体重
	 */
	@Column(name = "WEIGHT_")
	protected double weight;

	/**
	 * 百分位数3
	 */
	@Column(name = "PERCENT3_")
	protected double percent3;

	/**
	 * 百分位数15
	 */
	@Column(name = "PERCENT15_")
	protected double percent15;

	/**
	 * 百分位数50
	 */
	@Column(name = "PERCENT50_")
	protected double percent50;

	/**
	 * 百分位数85
	 */
	@Column(name = "PERCENT85_")
	protected double percent85;

	/**
	 * 百分位数97
	 */
	@Column(name = "PERCENT97_")
	protected double percent97;

	/**
	 * +1SD
	 */
	@Column(name = "ONEDSDEVIATION_")
	protected double oneDSDeviation;

	/**
	 * +2SD
	 */
	@Column(name = "TWODSDEVIATION_")
	protected double twoDSDeviation;

	/**
	 * +3SD
	 */
	@Column(name = "THREEDSDEVIATION_")
	protected double threeDSDeviation;

	/**
	 * +4SD
	 */
	@Column(name = "FOURDSDEVIATION_")
	protected double fourDSDeviation;

	/**
	 * 中位数
	 */
	@Column(name = "MEDIAN_")
	protected double median;

	/**
	 * -1SD
	 */
	@Column(name = "NEGATIVEONEDSDEVIATION_")
	protected double negativeOneDSDeviation;

	/**
	 * -2SD
	 */
	@Column(name = "NEGATIVETWODSDEVIATION_")
	protected double negativeTwoDSDeviation;

	/**
	 * -3SD
	 */
	@Column(name = "NEGATIVETHREEDSDEVIATION_")
	protected double negativeThreeDSDeviation;

	/**
	 * -4SD
	 */
	@Column(name = "NEGATIVEFOURDSDEVIATION_")
	protected double negativeFourDSDeviation;

	/**
	 * 标准类型
	 */
	@Column(name = "STANDARDTYPE_", length = 50)
	protected String standardType;

	/**
	 * 类型
	 */
	@Column(name = "TYPE_", length = 50)
	protected String type;

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

	public GrowthStandard() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrowthStandard other = (GrowthStandard) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getAge() {
		return this.age;
	}

	public int getAgeOfTheMoon() {
		return ageOfTheMoon;
	}

	public String getComplexKey() {
		return complexKey;
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

	public double getFourDSDeviation() {
		if (fourDSDeviation > 0) {
			fourDSDeviation = Math.round(fourDSDeviation * 10D) / 10D;
		}
		return fourDSDeviation;
	}

	public double getHeight() {
		if (height > 0) {
			height = Math.round(height * 10D) / 10D;
		}
		return height;
	}

	public long getId() {
		return this.id;
	}

	public double getMedian() {
		if (median > 0) {
			median = Math.round(median * 10D) / 10D;
		}
		return this.median;
	}

	public int getMonth() {
		return this.month;
	}

	public double getNegativeFourDSDeviation() {
		if (negativeFourDSDeviation > 0) {
			negativeFourDSDeviation = Math.round(negativeFourDSDeviation * 10D) / 10D;
		}
		return negativeFourDSDeviation;
	}

	public double getNegativeOneDSDeviation() {
		if (negativeOneDSDeviation > 0) {
			negativeOneDSDeviation = Math.round(negativeOneDSDeviation * 10D) / 10D;
		}
		return this.negativeOneDSDeviation;
	}

	public double getNegativeThreeDSDeviation() {
		if (negativeThreeDSDeviation > 0) {
			negativeThreeDSDeviation = Math.round(negativeThreeDSDeviation * 10D) / 10D;
		}
		return this.negativeThreeDSDeviation;
	}

	public double getNegativeTwoDSDeviation() {
		if (negativeTwoDSDeviation > 0) {
			negativeTwoDSDeviation = Math.round(negativeTwoDSDeviation * 10D) / 10D;
		}
		return this.negativeTwoDSDeviation;
	}

	public double getOneDSDeviation() {
		if (oneDSDeviation > 0) {
			oneDSDeviation = Math.round(oneDSDeviation * 10D) / 10D;
		}
		return this.oneDSDeviation;
	}

	public double getPercent15() {
		if (percent15 > 0) {
			percent15 = Math.round(percent15 * 10D) / 10D;
		}
		return percent15;
	}

	public double getPercent3() {
		if (percent3 > 0) {
			percent3 = Math.round(percent3 * 10D) / 10D;
		}
		return percent3;
	}

	public double getPercent50() {
		if (percent50 > 0) {
			percent50 = Math.round(percent50 * 10D) / 10D;
		}
		return percent50;
	}

	public double getPercent85() {
		if (percent85 > 0) {
			percent85 = Math.round(percent85 * 10D) / 10D;
		}
		return percent85;
	}

	public double getPercent97() {
		if (percent97 > 0) {
			percent97 = Math.round(percent97 * 10D) / 10D;
		}
		return percent97;
	}

	public String getSex() {
		return this.sex;
	}

	public String getStandardType() {
		return standardType;
	}

	public double getThreeDSDeviation() {
		if (threeDSDeviation > 0) {
			threeDSDeviation = Math.round(threeDSDeviation * 10D) / 10D;
		}
		return this.threeDSDeviation;
	}

	public double getTwoDSDeviation() {
		if (twoDSDeviation > 0) {
			twoDSDeviation = Math.round(twoDSDeviation * 10D) / 10D;
		}
		return this.twoDSDeviation;
	}

	public String getType() {
		return this.type;
	}

	public double getWeight() {
		if (weight > 0) {
			weight = Math.round(weight * 10D) / 10D;
		}
		return weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public GrowthStandard jsonToObject(JSONObject jsonObject) {
		return GrowthStandardJsonFactory.jsonToObject(jsonObject);
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setAgeOfTheMoon(int ageOfTheMoon) {
		this.ageOfTheMoon = ageOfTheMoon;
	}

	public void setComplexKey(String complexKey) {
		this.complexKey = complexKey;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFourDSDeviation(double fourDSDeviation) {
		this.fourDSDeviation = fourDSDeviation;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setNegativeFourDSDeviation(double negativeFourDSDeviation) {
		this.negativeFourDSDeviation = negativeFourDSDeviation;
	}

	public void setNegativeOneDSDeviation(double negativeOneDSDeviation) {
		this.negativeOneDSDeviation = negativeOneDSDeviation;
	}

	public void setNegativeThreeDSDeviation(double negativeThreeDSDeviation) {
		this.negativeThreeDSDeviation = negativeThreeDSDeviation;
	}

	public void setNegativeTwoDSDeviation(double negativeTwoDSDeviation) {
		this.negativeTwoDSDeviation = negativeTwoDSDeviation;
	}

	public void setOneDSDeviation(double oneDSDeviation) {
		this.oneDSDeviation = oneDSDeviation;
	}

	public void setPercent15(double percent15) {
		this.percent15 = percent15;
	}

	public void setPercent3(double percent3) {
		this.percent3 = percent3;
	}

	public void setPercent50(double percent50) {
		this.percent50 = percent50;
	}

	public void setPercent85(double percent85) {
		this.percent85 = percent85;
	}

	public void setPercent97(double percent97) {
		this.percent97 = percent97;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

	public void setThreeDSDeviation(double threeDSDeviation) {
		this.threeDSDeviation = threeDSDeviation;
	}

	public void setTwoDSDeviation(double twoDSDeviation) {
		this.twoDSDeviation = twoDSDeviation;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public JSONObject toJsonObject() {
		return GrowthStandardJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return GrowthStandardJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
