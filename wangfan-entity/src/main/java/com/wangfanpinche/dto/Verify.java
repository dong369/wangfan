package com.wangfanpinche.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 验证码表
 */
@Entity
@Table(name = "t_verify")
@DynamicInsert
@DynamicUpdate
public class Verify extends BaseEntity {

	@Column(nullable = false, length = 11)
	private String mobilePhone;// 手机号

	@Column(length = 4)
	private String code;// 验证码

	private String messageContent;// 短信内容

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

}
