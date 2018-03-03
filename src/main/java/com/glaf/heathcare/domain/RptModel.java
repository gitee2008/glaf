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

import java.io.Serializable;

public class RptModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id1;

	protected String id2;

	protected String id3;

	protected String id4;

	protected String id5;

	protected String id6;

	protected String id7;

	protected String id8;

	protected String id9;

	protected String name;

	protected String name1;

	protected String name2;

	protected String name3;

	protected String name4;

	protected String name5;

	protected String name6;

	protected String name7;

	protected String name8;

	protected String name9;

	protected double quantity1;

	protected double quantity2;

	protected double quantity3;

	protected double quantity4;

	protected double quantity5;

	protected double quantity6;

	protected double quantity7;

	protected double quantity8;

	protected double quantity9;

	protected String quantity1String;

	protected String quantity2String;

	protected String quantity3String;

	protected String quantity4String;

	protected String quantity5String;

	protected String quantity6String;

	protected String quantity7String;

	protected String quantity8String;

	protected String quantity9String;

	protected double avgQuantity;

	protected String avgQuantityString;

	protected double totalQuantity;

	protected String totalQuantityString;

	public RptModel() {

	}

	public boolean existsQuantity() {
		if (quantity1 + quantity2 + quantity3 + quantity4 + quantity5 + quantity6 + quantity7 + quantity8
				+ quantity9 > 0) {
			return true;
		}
		return false;
	}

	public double getAvgQuantity() {
		if (avgQuantity > 0) {
			avgQuantity = Math.round(avgQuantity * 100D) / 100D;
		}
		return avgQuantity;
	}

	public String getAvgQuantityString() {
		if (avgQuantity > 0) {
			avgQuantity = Math.round(avgQuantity * 100D) / 100D;
			return String.valueOf(avgQuantity);
		}
		return "";
	}

	public String getId1() {
		return id1;
	}

	public String getId2() {
		return id2;
	}

	public String getId3() {
		return id3;
	}

	public String getId4() {
		return id4;
	}

	public String getId5() {
		return id5;
	}

	public String getId6() {
		return id6;
	}

	public String getId7() {
		return id7;
	}

	public String getId8() {
		return id8;
	}

	public String getId9() {
		return id9;
	}

	public String getName() {
		if (name != null) {
			return name.trim();
		}
		return "";
	}

	public String getName1() {
		if (name1 != null) {
			return name1.trim();
		}
		return "";
	}

	public String getName2() {
		if (name2 != null) {
			return name2.trim();
		}
		return "";
	}

	public String getName3() {
		if (name3 != null) {
			return name3.trim();
		}
		return "";
	}

	public String getName4() {
		if (name4 != null) {
			return name4.trim();
		}
		return "";
	}

	public String getName5() {
		if (name5 != null) {
			return name5.trim();
		}
		return "";
	}

	public String getName6() {
		if (name6 != null) {
			return name6.trim();
		}
		return "";
	}

	public String getName7() {
		if (name7 != null) {
			return name7.trim();
		}
		return "";
	}

	public String getName8() {
		if (name8 != null) {
			return name8.trim();
		}
		return "";
	}

	public String getName9() {
		if (name9 != null) {
			return name9.trim();
		}
		return "";
	}

	public double getQuantity1() {
		if (quantity1 > 0) {
			quantity1 = Math.round(quantity1 * 100D) / 100D;
		}
		return quantity1;
	}

	public String getQuantity1String() {
		if (quantity1 > 0) {
			quantity1 = Math.round(quantity1 * 100D) / 100D;
			return String.valueOf(quantity1);
		}
		return "";
	}

	public double getQuantity2() {
		if (quantity2 > 0) {
			quantity2 = Math.round(quantity2 * 100D) / 100D;
		}
		return quantity2;
	}

	public String getQuantity2String() {
		if (quantity2 > 0) {
			quantity2 = Math.round(quantity2 * 100D) / 100D;
			return String.valueOf(quantity2);
		}
		return "";
	}

	public double getQuantity3() {
		if (quantity3 > 0) {
			quantity3 = Math.round(quantity3 * 100D) / 100D;
		}
		return quantity3;
	}

	public String getQuantity3String() {
		if (quantity3 > 0) {
			quantity3 = Math.round(quantity3 * 100D) / 100D;
			return String.valueOf(quantity3);
		}
		return "";
	}

	public double getQuantity4() {
		if (quantity4 > 0) {
			quantity4 = Math.round(quantity4 * 100D) / 100D;
		}
		return quantity4;
	}

	public String getQuantity4String() {
		if (quantity4 > 0) {
			quantity4 = Math.round(quantity4 * 100D) / 100D;
			return String.valueOf(quantity4);
		}
		return "";
	}

	public double getQuantity5() {
		if (quantity5 > 0) {
			quantity5 = Math.round(quantity5 * 100D) / 100D;
		}
		return quantity5;
	}

	public String getQuantity5String() {
		if (quantity5 > 0) {
			quantity5 = Math.round(quantity5 * 100D) / 100D;
			return String.valueOf(quantity5);
		}
		return "";
	}

	public double getQuantity6() {
		return quantity6;
	}

	public String getQuantity6String() {
		if (quantity6 > 0) {
			quantity6 = Math.round(quantity6 * 100D) / 100D;
			return String.valueOf(quantity6);
		}
		return "";
	}

	public double getQuantity7() {
		return quantity7;
	}

	public String getQuantity7String() {
		if (quantity7 > 0) {
			quantity7 = Math.round(quantity7 * 100D) / 100D;
			return String.valueOf(quantity7);
		}
		return "";
	}

	public double getQuantity8() {
		return quantity8;
	}

	public String getQuantity8String() {
		if (quantity8 > 0) {
			quantity8 = Math.round(quantity8 * 100D) / 100D;
			return String.valueOf(quantity8);
		}
		return "";
	}

	public double getQuantity9() {
		return quantity9;
	}

	public String getQuantity9String() {
		if (quantity9 > 0) {
			quantity9 = Math.round(quantity9 * 100D) / 100D;
			return String.valueOf(quantity9);
		}
		return "";
	}

	public double getTotalQuantity() {
		if (totalQuantity > 0) {
			totalQuantity = Math.round(totalQuantity * 100D) / 100D;
		}
		return totalQuantity;
	}

	public String getTotalQuantityString() {
		if (totalQuantity > 0) {
			totalQuantity = Math.round(totalQuantity * 100D) / 100D;
			return String.valueOf(totalQuantity);
		}
		return "";
	}

	public void setAvgQuantity(double avgQuantity) {
		this.avgQuantity = avgQuantity;
	}

	public void setAvgQuantityString(String avgQuantityString) {
		this.avgQuantityString = avgQuantityString;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public void setId3(String id3) {
		this.id3 = id3;
	}

	public void setId4(String id4) {
		this.id4 = id4;
	}

	public void setId5(String id5) {
		this.id5 = id5;
	}

	public void setId6(String id6) {
		this.id6 = id6;
	}

	public void setId7(String id7) {
		this.id7 = id7;
	}

	public void setId8(String id8) {
		this.id8 = id8;
	}

	public void setId9(String id9) {
		this.id9 = id9;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	public void setName5(String name5) {
		this.name5 = name5;
	}

	public void setName6(String name6) {
		this.name6 = name6;
	}

	public void setName7(String name7) {
		this.name7 = name7;
	}

	public void setName8(String name8) {
		this.name8 = name8;
	}

	public void setName9(String name9) {
		this.name9 = name9;
	}

	public void setQuantity1(double quantity1) {
		this.quantity1 = quantity1;
	}

	public void setQuantity1String(String quantity1String) {
		this.quantity1String = quantity1String;
	}

	public void setQuantity2(double quantity2) {
		this.quantity2 = quantity2;
	}

	public void setQuantity2String(String quantity2String) {
		this.quantity2String = quantity2String;
	}

	public void setQuantity3(double quantity3) {
		this.quantity3 = quantity3;
	}

	public void setQuantity3String(String quantity3String) {
		this.quantity3String = quantity3String;
	}

	public void setQuantity4(double quantity4) {
		this.quantity4 = quantity4;
	}

	public void setQuantity4String(String quantity4String) {
		this.quantity4String = quantity4String;
	}

	public void setQuantity5(double quantity5) {
		this.quantity5 = quantity5;
	}

	public void setQuantity5String(String quantity5String) {
		this.quantity5String = quantity5String;
	}

	public void setQuantity6(double quantity6) {
		this.quantity6 = quantity6;
	}

	public void setQuantity6String(String quantity6String) {
		this.quantity6String = quantity6String;
	}

	public void setQuantity7(double quantity7) {
		this.quantity7 = quantity7;
	}

	public void setQuantity7String(String quantity7String) {
		this.quantity7String = quantity7String;
	}

	public void setQuantity8(double quantity8) {
		this.quantity8 = quantity8;
	}

	public void setQuantity8String(String quantity8String) {
		this.quantity8String = quantity8String;
	}

	public void setQuantity9(double quantity9) {
		this.quantity9 = quantity9;
	}

	public void setQuantity9String(String quantity9String) {
		this.quantity9String = quantity9String;
	}

	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

}
