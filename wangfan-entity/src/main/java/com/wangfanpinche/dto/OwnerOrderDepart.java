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
 * 车主-发车
 *
 */
@Entity
@Table(name = "t_ownerorderdepart")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderDepart extends BaseEntity {
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private OwnerOrder ownerOrder;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="depart")
	private List<OwnerOrderDepartPassenger> departPassengers;//发车已经送过的乘客

	
	public OwnerOrder getOwnerOrder() {
		return ownerOrder;
	}

	public void setOwnerOrder(OwnerOrder ownerOrder) {
		this.ownerOrder = ownerOrder;
	}

	public List<OwnerOrderDepartPassenger> getDepartPassengers() {
		return departPassengers;
	}

	public void setDepartPassengers(List<OwnerOrderDepartPassenger> departPassengers) {
		this.departPassengers = departPassengers;
	}
	
	
}
