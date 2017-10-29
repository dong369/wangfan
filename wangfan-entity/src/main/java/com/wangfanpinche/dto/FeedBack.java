package com.wangfanpinche.dto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 问题反馈表
 *
 */
@Entity
@Table(name = "t_feedback")
@DynamicInsert
@DynamicUpdate
public class FeedBack extends BaseEntity{
	
	private String userId;//用户id
	
	private String content;//内容
	
	private String linkType;//联系方式---手机号或邮箱
	
	@Enumerated(EnumType.ORDINAL)
	private FeedStatusEnum readStatus;//是否已读	
	
	private LocalDateTime readDateTime;//阅读时间
	
	public enum FeedStatusEnum {
		未读,
		已读;		
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public LocalDateTime getReadDateTime() {
		return readDateTime;
	}

	public void setReadDateTime(LocalDateTime readDateTime) {
		this.readDateTime = readDateTime;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public FeedStatusEnum getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(FeedStatusEnum readStatus) {
		this.readStatus = readStatus;
	}

		
}
