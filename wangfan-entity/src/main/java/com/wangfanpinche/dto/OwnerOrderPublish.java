package com.wangfanpinche.dto;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车主发布车单
 *
 */
@Entity
@Table(name = "t_ownerorderpublish")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderPublish extends BaseEntity {
	
	@OneToOne(fetch=FetchType.LAZY, optional=false)
	private OwnerOrder ownerOrder;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="t_ownerorderpublish_ownerpassenger")
	private Set<OwnerOrderPublishPassenger> ownerPassengers;//车单的乘客		
	
	private LocalDateTime closedDateTime;//车主关闭车单时间
	
	

	public OwnerOrder getOwnerOrder() {
		return ownerOrder;
	}

	public void setOwnerOrder(OwnerOrder ownerOrder) {
		this.ownerOrder = ownerOrder;
	}

	public Set<OwnerOrderPublishPassenger> getOwnerPassengers() {
		return ownerPassengers;
	}

	public void setOwnerPassengers(Set<OwnerOrderPublishPassenger> ownerPassengers) {
		this.ownerPassengers = ownerPassengers;
	}

	public LocalDateTime getClosedDateTime() {
		return closedDateTime;
	}

	public void setClosedDateTime(LocalDateTime closedDateTime) {
		this.closedDateTime = closedDateTime;
	}
	
	

}
