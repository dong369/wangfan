package com.wangfanpinche.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信配置类
 * @author kevin
 * @date 2016-09-30 13:37:39
 */
@Component
public class SmsProperties {
	
	@Value("${sms.url}")
	private String url;

	@Value("${sms.appKey}")
	private String appKey;

	@Value("${sms.appSecret}")
	private String appSecret;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

}
