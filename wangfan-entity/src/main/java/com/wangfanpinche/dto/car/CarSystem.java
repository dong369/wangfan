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
 * 车系
 *
 */
@Entity
@Table(name = "t_carsystem")
@DynamicInsert
@DynamicUpdate
public class CarSystem extends BaseEntity{
	
	@ManyToOne(fetch=FetchType.LAZY)
	private CarBrand carBrand;
	
	@Column(length=20)
	private String name;//车系名
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="carSystem")
	private Set<CarYear> carYears;

	public CarBrand getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(CarBrand carBrand) {
		this.carBrand = carBrand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<CarYear> getCarYears() {
		return carYears;
	}

	public void setCarYears(Set<CarYear> carYears) {
		this.carYears = carYears;
	}
	
	

}
