package com.wangfanpinche.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 微信支付
 *
 */
@JacksonXmlRootElement(localName = "xml")
public class WeChat {

	private String appid;//应用ID
	private String partnerid;//商户号
	private String prepayid;//预支付交易会话ID
	@JSONField(name="package")
	private String packages;//扩展字段
	private String noncestr;//随机字符串
	private String timestamp;//时间戳
	private String sign;//签名
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
