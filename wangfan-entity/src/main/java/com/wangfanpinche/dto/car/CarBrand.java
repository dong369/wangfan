package com.wangfanpinche.dto.car;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;


/**
 * 车辆品牌
 * 
 *
 */
@Entity
@Table(name = "t_carbrand")
@DynamicInsert
@DynamicUpdate
public class CarBrand extends BaseEntity{

	@Column(length = 20)
	private String name;// 名称
	
	@Column(length = 5)
	private String initial;// 首字母
	
	@Column(length = 200)
	private String picUrl;// 品牌图片路径

	@OneToMany(fetch = FetchType.LAZY,mappedBy="carBrand")
	private Set<CarSystem> carSystems;
	
	public CarBrand() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public Set<CarSystem> getCarSystems() {
		return carSystems;
	}

	public void setCarSystems(Set<CarSystem> carSystems) {
		this.carSystems = carSystems;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
