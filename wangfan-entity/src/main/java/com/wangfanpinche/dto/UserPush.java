package com.wangfanpinche.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.alibaba.fastjson.annotation.JSONField;
import com.wangfanpinche.dto.base.BaseEntity;

@Entity
@Table(name = "t_userpush")
@DynamicInsert
@DynamicUpdate
public class UserPush extends BaseEntity {
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JSONField(serialize=false)
	private PushEntity pushEntity;//推送实体
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JSONField(serialize=false)
	private User toUser;//谁接收
	
	@Type(type = "true_false")
	@Column(name="read1")
	private Boolean read = false;//是否阅读
	
	private LocalDateTime readDateTime;//阅读时间

	public PushEntity getPushEntity() {
		return pushEntity;
	}

	public void setPushEntity(PushEntity pushEntity) {
		this.pushEntity = pushEntity;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
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
	

}
