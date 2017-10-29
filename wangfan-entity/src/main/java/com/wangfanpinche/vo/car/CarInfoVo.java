package com.wangfanpinche.vo.car;

import java.math.BigDecimal;

/**
 * 车辆信息
 *
 */
public class CarInfoVo {
	
	private String id;//id

	private String outsideColor;// 外观颜色

	private String outsideColorCode;// 外观颜色编码

	private Integer seat; // 座位数

	private BigDecimal fuel;// 工信部油量

	private BigDecimal actfuel;// 实测油量
	
	private String carmodel;//车型名称

	private String displacementId;//排量id
	
	private String displacementName;//排量名称

	private String yearId;//年代款id
	
	private String yearName;//年代款名称

	private String systemId;//车系id
	
	private String systemName;//车系名称

	private String brandId;//品牌id
	
	private String brandName;//品牌名称
	
	private String initial;//首字母
		
	
	public CarInfoVo() {
	}
	
	
	
	public CarInfoVo(String id, String outsideColor, String outsideColorCode, Integer seat, BigDecimal fuel, BigDecimal actfuel, String carmodel) {
		super();
		this.id = id;
		this.outsideColor = outsideColor;
		this.outsideColorCode = outsideColorCode;
		this.seat = seat;
		this.fuel = fuel;
		this.actfuel = actfuel;
		this.carmodel = carmodel;
	}



	public CarInfoVo(String id, String outsideColor, String outsideColorCode, Integer seat, BigDecimal fuel, BigDecimal actfuel, String carmodel, String displacementId, String displacementName, String yearId, String yearName, String systemId, String systemName, String brandId, String brandName) {
		super();
		this.id = id;
		this.outsideColor = outsideColor;
		this.outsideColorCode = outsideColorCode;
		this.seat = seat;
		this.fuel = fuel;
		this.actfuel = actfuel;
		this.carmodel = carmodel;
		this.displacementId = displacementId;
		this.displacementName = displacementName;
		this.yearId = yearId;
		this.yearName = yearName;
		this.systemId = systemId;
		this.systemName = systemName;
		this.brandId = brandId;
		this.brandName = brandName;
	}



	public CarInfoVo(String id) {
		super();
		this.id = id;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDisplacementId() {
		return displacementId;
	}

	public void setDisplacementId(String displacementId) {
		this.displacementId = displacementId;
	}

	public String getDisplacementName() {
		return displacementName;
	}

	public void setDisplacementName(String displacementName) {
		this.displacementName = displacementName;
	}

	public String getYearId() {
		return yearId;
	}

	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}	

	public String getCarmodel() {
		return carmodel;
	}


	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}



	public String getInitial() {
		return initial;
	}



	public void setInitial(String initial) {
		this.initial = initial;
	}

}
