package com.wangfanpinche.dto;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车主接乘客
 *
 */
@Entity
@Table(name = "t_ownerorderreceivepassenger")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderReceivePassenger extends BaseEntity {

	@Type(type = "true_false")
	private Boolean goinStatus = false;// 是否上车

	private LocalDateTime goinDateTime;// 上车时间
	
	@OneToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST})
	private OwnerLocation goinLocation;//上车地点

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;// 用户
	
	@ManyToOne(fetch = FetchType.LAZY)
	private OwnerOrderReceive receive;
	

	public Boolean getGoinStatus() {
		return goinStatus;
	}

	public void setGoinStatus(Boolean goinStatus) {
		this.goinStatus = goinStatus;
	}

	public LocalDateTime getGoinDateTime() {
		return goinDateTime;
	}

	public void setGoinDateTime(LocalDateTime goinDateTime) {
		this.goinDateTime = goinDateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OwnerLocation getGoinLocation() {
		return goinLocation;
	}

	public void setGoinLocation(OwnerLocation goinLocation) {
		this.goinLocation = goinLocation;
	}

	public OwnerOrderReceive getReceive() {
		return receive;
	}

	public void setReceive(OwnerOrderReceive receive) {
		this.receive = receive;
	}


}
