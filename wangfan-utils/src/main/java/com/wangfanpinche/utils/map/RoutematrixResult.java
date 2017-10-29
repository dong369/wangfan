package com.wangfanpinche.utils.map;

import java.math.BigDecimal;

/**
 * 路线规划距离和行驶时间表
 *
 */
public class RoutematrixResult {
	
	private int status;//状态码  0：成功 1：服务器内部错误 2：参数错误

	private String message;//响应信息--对status的中文描述

	private String distanceText;//线路距离的文本描述 --文本描述的单位有米、公里两种

	private BigDecimal distanceValue;//线路距离的数值--数值的单位为米。若没有计算结果，值为0

	private String durationText;//路线耗时的文本描述--文本描述的单位有分钟、小时两种
	
	private BigDecimal durationValue;//路线耗时的数值--数值的单位为秒。若没有计算结果，值为0

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDistanceText() {
		return distanceText;
	}

	public void setDistanceText(String distanceText) {
		this.distanceText = distanceText;
	}

	public BigDecimal getDistanceValue() {
		return distanceValue;
	}

	public void setDistanceValue(BigDecimal distanceValue) {
		this.distanceValue = distanceValue;
	}

	public String getDurationText() {
		return durationText;
	}

	public void setDurationText(String durationText) {
		this.durationText = durationText;
	}

	public BigDecimal getDurationValue() {
		return durationValue;
	}

	public void setDurationValue(BigDecimal durationValue) {
		this.durationValue = durationValue;
	}

}
