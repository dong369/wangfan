package com.wangfanpinche.vo;

import java.time.LocalDateTime;

import com.wangfanpinche.dto.FeedBack.FeedStatusEnum;


/**
 * 问题反馈
 *
 */
public class FeedBackVo {
	
	private String id;

	private String userId;//用户id
	
	private String username;//用户名
	
	private String content;//内容
	
	private String linkType;//联系方式---手机号或邮箱
	
	private FeedStatusEnum readStatus;//是否已读
	
	private LocalDateTime readDateTime;//阅读时间
	
	private LocalDateTime createDateTime;//创建时间

	
	public FeedBackVo(String id, String username, String content, String linkType) {
		super();
		this.id = id;
		this.username = username;
		this.content = content;
		this.linkType = linkType;
	}


	public FeedBackVo(String id, String userId, String username, String content, String linkType, FeedStatusEnum readStatus, LocalDateTime readDateTime, LocalDateTime createDateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.content = content;
		this.linkType = linkType;
		this.readStatus = readStatus;
		this.readDateTime = readDateTime;
		this.createDateTime = createDateTime;
	}


	public FeedBackVo() {
		super();
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public LocalDateTime getReadDateTime() {
		return readDateTime;
	}

	public void setReadDateTime(LocalDateTime readDateTime) {
		this.readDateTime = readDateTime;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public FeedStatusEnum getReadStatus() {
		return readStatus;
	}


	public void setReadStatus(FeedStatusEnum readStatus) {
		this.readStatus = readStatus;
	}

	
}
