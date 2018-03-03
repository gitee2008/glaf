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

public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;

	protected long goodsId;

	protected long goodsNodeId;

	protected String goodsName;

	protected double quantity;

	protected String quantityString;

	protected double price;

	protected String priceString;

	public Goods() {

	}

	public long getGoodsId() {
		return goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public long getGoodsNodeId() {
		return goodsNodeId;
	}

	public double getPrice() {
		if (price > 0) {
			price = Math.round(price * 100D) / 100D;
		}
		return price;
	}

	public String getPriceString() {
		priceString = "";
		if (price > 0) {
			priceString = String.valueOf(getPrice());
		}
		return priceString;
	}

	public double getQuantity() {
		if (quantity > 0) {
			quantity = Math.round(quantity * 100D) / 100D;
		}
		return quantity;
	}

	public String getQuantityString() {
		quantityString = "";
		if (quantity > 0) {
			quantityString = String.valueOf(getQuantity());
		}
		return quantityString;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setGoodsNodeId(long goodsNodeId) {
		this.goodsNodeId = goodsNodeId;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setQuantityString(String quantityString) {
		this.quantityString = quantityString;
	}

}
