package com.wangfanpinche.dto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车辆表
 */
@Entity
@Table(name = "t_car")
@DynamicInsert
@DynamicUpdate
public class Car extends BaseEntity{
	
	private String userId;//用户ID
	
	private String carInfoId;//车辆信息id
	
	private String carNumber;//车牌号
	
	private String carColor;//颜色
	
	private String carUserName;//车辆所有人
	
	@Enumerated(EnumType.ORDINAL)
	private CarStatusEnum status;// 状态(认证失败  认证中   认证通过)
	
	public enum CarStatusEnum {
		认证失败,		
		认证中,
		认证通过; 					
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
	public String getCarUserName() {
		return carUserName;
	}
	public void setCarUserName(String carUserName) {
		this.carUserName = carUserName;
	}	
		
}
