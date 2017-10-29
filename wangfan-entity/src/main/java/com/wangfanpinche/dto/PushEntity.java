package com.wangfanpinche.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.alibaba.fastjson.annotation.JSONField;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 推送实体
 *
 */
@Entity
@Table(name = "t_pushentity")
@DynamicInsert
@DynamicUpdate
public class PushEntity extends BaseEntity {
	
	@Enumerated(EnumType.ORDINAL)
	private PushType type;//系统推送，个人推送，业务推送 
	
	private String title;//标题

	@Enumerated
	private BizNumber bizNumber;//业务号
	
	private String body;//JSON实体
	
	private String content;//Entity
	
	private String pageUrl;//页面链接
	
	private String bannerUrl;//图片
	
	private String pushType;//推送类型（某个日期内显示的推送）
	
	private LocalDateTime startDateTime;//开始时间
	
	private LocalDateTime endDateTime;//结束时间
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JSONField(serialize=false)
	private User user;//谁发的
	
	@OneToMany(mappedBy="pushEntity")
	@JSONField(serialize=false)
	private List<UserPush> pushUsers = new ArrayList<>();//发送给谁

	
	public static enum PushType{
		SYSTEM,//系统推送
		PERSON,//个人推送
		BIZ//业务推送
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserPush> getPushUsers() {
		return pushUsers;
	}

	public void setPushUsers(List<UserPush> pushUsers) {
		this.pushUsers = pushUsers;
	}

	public PushType getType() {
		return type;
	}

	public void setType(PushType type) {
		this.type = type;
	}

	public BizNumber getBizNumber() {
		return bizNumber;
	}

	public void setBizNumber(BizNumber bizNumber) {
		this.bizNumber = bizNumber;
	}

}
