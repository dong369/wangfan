package com.wangfanpinche.dto.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.base.BaseEntity;

@Entity
@Table(name = "t_evaluate")
@DynamicInsert
@DynamicUpdate
public class Evaluate extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private User fromUser;// 谁评价的

	@ManyToOne(fetch = FetchType.LAZY)
	private User toUser;// 评价给谁

	@Enumerated
	private EvaluateType evaluateType;//类型

	@ElementCollection
	@CollectionTable(name = "t_evaluate_tag")
	private List<String> tags = new ArrayList<>(0);// 车主给乘客贴的标签

	private String content;// 评价内容

	private Double description;// 描述相符

	private Double trustworthy;// 信用

	private Double service;// 服务态度

	public static enum EvaluateType {
		Owner, // 车主
		Passenger// 乘客
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

	public EvaluateType getEvaluateType() {
		return evaluateType;
	}

	public void setEvaluateType(EvaluateType evaluateType) {
		this.evaluateType = evaluateType;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

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
	
	
	
	
}
