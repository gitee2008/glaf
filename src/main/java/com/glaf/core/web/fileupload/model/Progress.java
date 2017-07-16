package com.glaf.core.web.fileupload.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

import com.alibaba.fastjson.JSONObject;

public class Progress implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 已读字节 **/
	private long bytesRead = 0L;
	/** 已读MB **/
	private String mbRead = "0";
	/** 总长度 **/
	private long contentLength = 0L;
	/****/
	private int items;
	/** 已读百分比 **/
	private String percent;
	/** 读取速度 **/
	private String speed;
	/** 开始读取的时间 **/
	private long startReatTime = System.currentTimeMillis();

	public long getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public String getPercent() {
		percent = getPercent(bytesRead, contentLength);
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getSpeed() {
		speed = divideNumber(divideNumber(bytesRead * 1000, System.currentTimeMillis() - startReatTime), 1000);
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public long getStartReatTime() {
		return startReatTime;
	}

	public void setStartReatTime(long startReatTime) {
		this.startReatTime = startReatTime;
	}

	public String getMbRead() {
		mbRead = divideNumber(bytesRead, 1000000);
		return mbRead;
	}

	public void setMbRead(String mbRead) {
		this.mbRead = mbRead;
	}

	@Override
	public String toString() {
		JSONObject object = new JSONObject();
		object.put("bytesRead", bytesRead);
		object.put("mbRead", bytesRead);
		object.put("contentLength", contentLength);
		object.put("items", items);
		object.put("percent", this.getPercent());
		object.put("speed", this.getSpeed());
		return object.toString();
	}

	/**
	 * 计算比例
	 * 
	 * @param divisor
	 * @param dividend
	 * @return String
	 * @author fantasy
	 * @date 2013-10-9
	 */
	private String divideNumber(Object divisor, Object dividend) {
		if (divisor == null || dividend == null) {
			return "";
		}
		BigDecimal a = toBig(divisor);
		BigDecimal b = toBig(dividend);
		if (a.equals(toBig(0)) || b.equals(toBig(0))) {
			return "0";
		}
		BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_DOWN);
		return c.toString();
	}

	/**
	 * 计算百分比
	 * 
	 * @param divisor
	 * @param dividend
	 * @return String
	 * @author fantasy
	 * @date 2013-8-27
	 */
	private String getPercent(Object divisor, Object dividend) {
		if (divisor == null || dividend == null) {
			return "";
		}
		NumberFormat percent = NumberFormat.getPercentInstance();
		// 建立百分比格式化引用
		percent.setMaximumFractionDigits(2);
		BigDecimal a = toBig(divisor);
		BigDecimal b = toBig(dividend);
		if (a.equals(toBig(0)) || b.equals(toBig(0)) || a.equals(toBig(0.0)) || b.equals(toBig(0.0))) {
			return "0.00%";
		}
		BigDecimal c = a.divide(b, 4, BigDecimal.ROUND_DOWN);
		return percent.format(c);
	}

	private BigDecimal toBig(Object o) {
		if (o == null || o.toString().equals("") || o.toString().equals("NaN")) {
			return new BigDecimal(0);
		}
		return new BigDecimal(o.toString());
	}
}