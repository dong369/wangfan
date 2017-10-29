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
 * 车主-发车乘客
 *
 */
@Entity
@Table(name = "t_ownerorderdepartpassenger")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderDepartPassenger extends BaseEntity{

	@Type(type = "true_false")
	private Boolean gooutStatus = false;// 是否下车

	private LocalDateTime gooutDateTime;// 下车时间
	
	@OneToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST})
	private OwnerLocation gooutLocation;//下车地点

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;// 用户
	
	@ManyToOne(fetch = FetchType.LAZY)
	private OwnerOrderDepart depart;
	
	public Boolean getGooutStatus() {
		return gooutStatus;
	}

	public void setGooutStatus(Boolean gooutStatus) {
		this.gooutStatus = gooutStatus;
	}

	public LocalDateTime getGooutDateTime() {
		return gooutDateTime;
	}

	public void setGooutDateTime(LocalDateTime gooutDateTime) {
		this.gooutDateTime = gooutDateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OwnerLocation getGooutLocation() {
		return gooutLocation;
	}

	public void setGooutLocation(OwnerLocation gooutLocation) {
		this.gooutLocation = gooutLocation;
	}

	public OwnerOrderDepart getDepart() {
		return depart;
	}

	public void setDepart(OwnerOrderDepart depart) {
		this.depart = depart;
	}

	
	
}
