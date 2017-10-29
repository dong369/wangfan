package com.wangfanpinche.vo;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.wangfanpinche.dto.BizNumber;
import com.wangfanpinche.dto.PushEntity.PushType;

public class PushEntityVo {
	
	private String id;
	
	private PushType type;//系统推送，个人推送，业务推送 
	
	private String title;//标题
	
	private BizNumber bizNumber;//业务号
	
	private String body;//JSON实体
	
	private String content;//Entity
	
	private String pageUrl;//页面链接
	
	private String bannerUrl;//图片
	
	private String pushType;//推送类型（某个日期内显示的推送）
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JSONField(format="yyyy-MM-dd HH:mm")
	private LocalDateTime startDateTime;//开始时间
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JSONField(format="yyyy-MM-dd HH:mm")
	private LocalDateTime endDateTime;//结束时间
	
	private String userId;//谁发的
	
	private String userpushId;//
	
	private String toUserId;//谁接收
	
	private Boolean read = false;//是否阅读
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")	
	private LocalDateTime readDateTime;//阅读时间

	public PushEntityVo(String id, String userpushId) {
		super();
		this.id = id;
		this.userpushId = userpushId;
	}

	public PushEntityVo(String id, String title, String pageUrl, String bannerUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, BizNumber bizNumber) {
		super();
		this.id = id;
		this.title = title;
		this.pageUrl = pageUrl;
		this.bannerUrl = bannerUrl;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.bizNumber = bizNumber;
	}
	
	public PushEntityVo(String id, String title, String pageUrl, String bannerUrl, LocalDateTime startDateTime, LocalDateTime endDateTime, String userpushId, Boolean read, LocalDateTime readDateTime, BizNumber bizNumber) {
		super();
		this.id = id;
		this.title = title;
		this.pageUrl = pageUrl;
		this.bannerUrl = bannerUrl;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.userpushId = userpushId;
		this.read = read;
		this.readDateTime = readDateTime;
		this.bizNumber = bizNumber;
	}

	public PushEntityVo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public LocalDateTime getReadDateTime() {
		return readDateTime;
	}

	public void setReadDateTime(LocalDateTime readDateTime) {
		this.readDateTime = readDateTime;
	}

	public PushType getType() {
		return type;
	}

	public void setType(PushType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BizNumber getBizNumber() {
		return bizNumber;
	}

	public void setBizNumber(BizNumber bizNumber) {
		this.bizNumber = bizNumber;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserpushId() {
		return userpushId;
	}

	public void setUserpushId(String userpushId) {
		this.userpushId = userpushId;
	}

}
