package com.glaf.heathcare.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsSet implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String key;

	protected List<Goods> rows1 = new ArrayList<Goods>();

	protected List<Goods> rows2 = new ArrayList<Goods>();

	protected List<Goods> rows3 = new ArrayList<Goods>();

	protected List<Goods> rows4 = new ArrayList<Goods>();

	protected List<Goods> rows5 = new ArrayList<Goods>();

	public GoodsSet() {

	}

	public String getKey() {
		return key;
	}

	public List<Goods> getRows1() {
		return rows1;
	}

	public List<Goods> getRows2() {
		return rows2;
	}

	public List<Goods> getRows3() {
		return rows3;
	}

	public List<Goods> getRows4() {
		return rows4;
	}

	public List<Goods> getRows5() {
		return rows5;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setRows1(List<Goods> rows1) {
		this.rows1 = rows1;
	}

	public void setRows2(List<Goods> rows2) {
		this.rows2 = rows2;
	}

	public void setRows3(List<Goods> rows3) {
		this.rows3 = rows3;
	}

	public void setRows4(List<Goods> rows4) {
		this.rows4 = rows4;
	}

	public void setRows5(List<Goods> rows5) {
		this.rows5 = rows5;
	}

}
