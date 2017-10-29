package com.wangfanpinche.vo;


import com.wangfanpinche.dto.Car.CarStatusEnum;

/**
 * 车辆表
 */
public class CarVo{
	
	private String id;
	
	private String userId;//用户ID
	
	private String carInfoId;//车辆信息id
	
	private String carmodel;//车型
	
	private String carNumber;//车牌号
	
	private String carColor;//颜色
	
	private String carUserName;//车辆所有人
		
	private CarStatusEnum status;// 状态(认证失败  认证中   认证通过)
	
	private String brandName;
	
	private String systemName;
	
	public CarVo(String brandName, String systemName) {
		super();
		this.brandName = brandName;
		this.systemName = systemName;
	}

	public CarVo() {
		super();
	}

	public CarVo(String id, String carmodel, String carNumber, String carColor, String carInfoId) {
		super();
		this.id = id;
		this.carmodel = carmodel;
		this.carNumber = carNumber;
		this.carColor = carColor;
		this.carInfoId = carInfoId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCarInfoId() {
		return carInfoId;
	}
	public void setCarInfoId(String carInfoId) {
		this.carInfoId = carInfoId;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	public CarStatusEnum getStatus() {
		return status;
	}
	public void setStatus(CarStatusEnum status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarmodel() {
		return carmodel;
	}
	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}

	public String getCarUserName() {
		return carUserName;
	}

	public void setCarUserName(String carUserName) {
		this.carUserName = carUserName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}	
		
}
