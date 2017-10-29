package com.wangfanpinche.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 支付信息
 *
 */
@Entity
@Table(name = "t_payinfo")
@DynamicInsert
@DynamicUpdate
public class PayInfo extends BaseEntity {

	@Type(type="org.hibernate.type.MaterializedClobType")
	private String requestBody;
	
	@Type(type="org.hibernate.type.MaterializedClobType")
	private String notifyBody;

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getNotifyBody() {
		return notifyBody;
	}

	public void setNotifyBody(String notifyBody) {
		this.notifyBody = notifyBody;
	}
	
	
}
