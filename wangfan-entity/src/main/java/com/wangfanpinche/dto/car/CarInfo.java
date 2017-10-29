package com.wangfanpinche.dto.car;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车辆信息
 *
 */
@Entity
@Table(name = "t_carinfo")
@DynamicInsert
@DynamicUpdate
public class CarInfo extends BaseEntity{
	
	@ManyToOne(fetch=FetchType.LAZY)
	private CarDisplacement displacement;
	
	@Column(length=500)
	private String outsideColor;//外观颜色
	
	@Column(length=500)
	private String outsideColorCode;//外观颜色编码
	
	private Integer seat;	//座位数
	
	@Column(precision = 10, scale = 1)
	private BigDecimal fuel;//工信部油量
	
	@Column(precision = 10, scale = 1)
	private BigDecimal actfuel;//实测油量
	
	@Column(length=500)
	private String carmodel;//车型名称
	
	
	public CarDisplacement getDisplacement() {
		return displacement;
	}

	public void setDisplacement(CarDisplacement displacement) {
		this.displacement = displacement;
	}

	public String getOutsideColor() {
		return outsideColor;
	}

	public void setOutsideColor(String outsideColor) {
		this.outsideColor = outsideColor;
	}

	public String getOutsideColorCode() {
		return outsideColorCode;
	}

	public void setOutsideColorCode(String outsideColorCode) {
		this.outsideColorCode = outsideColorCode;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public BigDecimal getFuel() {
		return fuel;
	}

	public void setFuel(BigDecimal fuel) {
		this.fuel = fuel;
	}

	public BigDecimal getActfuel() {
		return actfuel;
	}

	public void setActfuel(BigDecimal actfuel) {
		this.actfuel = actfuel;
	}

	public String getCarmodel() {
		return carmodel;
	}

	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}
	
	
}
