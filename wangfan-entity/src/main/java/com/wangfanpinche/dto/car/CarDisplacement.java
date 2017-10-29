package com.wangfanpinche.dto.car;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车的排量
 *
 */
@Entity
@Table(name = "t_cardisplacement")
@DynamicInsert
@DynamicUpdate
public class CarDisplacement extends BaseEntity{

	@ManyToOne(fetch=FetchType.LAZY)
	private CarYear carYear;
	
	@Column(length=20)
	private String name;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="displacement")
	private Set<CarInfo> carInfos;

	public CarYear getCarYear() {
		return carYear;
	}

	public void setCarYear(CarYear carYear) {
		this.carYear = carYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<CarInfo> getCarInfos() {
		return carInfos;
	}

	public void setCarInfos(Set<CarInfo> carInfos) {
		this.carInfos = carInfos;
	}
		
	
	
}
