package com.wangfanpinche.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车主接乘客
 *
 */
@Entity
@Table(name = "t_ownerorderreceive")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderReceive extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private OwnerOrder ownerOrder;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="receive")
	private List<OwnerOrderReceivePassenger> receivePassengers;//车主接到的乘客

	
	public OwnerOrder getOwnerOrder() {
		return ownerOrder;
	}

	public void setOwnerOrder(OwnerOrder ownerOrder) {
		this.ownerOrder = ownerOrder;
	}

	public List<OwnerOrderReceivePassenger> getReceivePassengers() {
		return receivePassengers;
	}

	public void setReceivePassengers(List<OwnerOrderReceivePassenger> receivePassengers) {
		this.receivePassengers = receivePassengers;
	}

}
