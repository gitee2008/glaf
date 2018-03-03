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

public class NameQuantity implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id;

	protected String name;

	protected double quantity;

	protected String quantityString;

	public NameQuantity() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NameQuantity other = (NameQuantity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		if (name != null) {
			return name.trim();
		}
		return "";
	}

	public double getQuantity() {
		return quantity;
	}

	public String getQuantityString() {
		quantityString = "";
		if (quantity > 0) {
			quantity = Math.round(quantity * 100D) / 100D;
			quantityString = String.valueOf(quantity);
		}
		return quantityString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setQuantityString(String quantityString) {
		this.quantityString = quantityString;
	}

	@Override
	public String toString() {
		return "NameQuantity [id=" + id + ", name=" + name + ", quantity=" + quantity + ", quantityString="
				+ quantityString + "]";
	}

}
