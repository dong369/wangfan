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
 * 车的年代款
 *
 */
@Entity
@Table(name = "t_caryear")
@DynamicInsert
@DynamicUpdate
public class CarYear extends BaseEntity{
	
	@ManyToOne(fetch=FetchType.LAZY)
	private CarSystem carSystem;
	
	@Column(length=20)
	private String name;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="carYear")
	private Set<CarDisplacement> displacements;

	public CarSystem getCarSystem() {
		return carSystem;
	}

	public void setCarSystem(CarSystem carSystem) {
		this.carSystem = carSystem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<CarDisplacement> getDisplacements() {
		return displacements;
	}

	public void setDisplacements(Set<CarDisplacement> displacements) {
		this.displacements = displacements;
	}
	

	
	

}
