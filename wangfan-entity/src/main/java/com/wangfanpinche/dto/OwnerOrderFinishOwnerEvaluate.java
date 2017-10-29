package com.wangfanpinche.dto;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 乘客给车主的评价
 *
 */
@Entity
@Table(name = "t_ownerorderfinishownerevaluate")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderFinishOwnerEvaluate extends BaseEntity {
	
	private String content;// 评价内容

	private Double description;// 描述相符

	private Double trustworthy;// 信用

	private Double service;// 服务态度
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User fromUser;//谁评价的
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User toUser;//评价给谁

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getDescription() {
		return description;
	}

	public void setDescription(Double description) {
		this.description = description;
	}

	public Double getTrustworthy() {
		return trustworthy;
	}

	public void setTrustworthy(Double trustworthy) {
		this.trustworthy = trustworthy;
	}

	public Double getService() {
		return service;
	}

	public void setService(Double service) {
		this.service = service;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	

}
