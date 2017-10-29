package com.wangfanpinche.vo;

import java.math.BigDecimal;

/**
 * 发布的乘客
 *
 */
public class PassengerListPublishVo {
	
	private String id;//乘客id

	private Boolean payStatus = false;// 支付状态 待付款、已付款
	
	private Boolean approveStatus = false;// 车主是否同意
	
	private Boolean disapproveStatus = false;//是否拒绝
	
	private Boolean closedStatus = false;//关闭时间
	
	private String fromCity;//起点城市
	
	private String fromSematicDescription;//起点地点描述
	
	private String toCity;//终点城市
	
	private String toSematicDescription;//终点地点描述
	
	private BigDecimal money;//金额
	
	private Integer seat;// 所占座位
	
	private String descript;//备注
	
	private String userId;//用户id
	
	private String userIcon;//用户头像
	
	private String mobilePhone;//手机号
	
	private String username;//用户姓名

	public PassengerListPublishVo(String id, Boolean payStatus, Boolean approveStatus, Boolean disapproveStatus, Boolean closedStatus, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, BigDecimal money, Integer seat, String descript, String userId, String userIcon, String mobilePhone, String username) {
		super();
		this.id = id;
		this.payStatus = payStatus;
		this.approveStatus = approveStatus;
		this.disapproveStatus = disapproveStatus;
		this.closedStatus = closedStatus;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.money = money;
		this.seat = seat;
		this.descript = descript;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
	}

	public PassengerListPublishVo() {
		super();
	}

	public Boolean getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Boolean payStatus) {
		this.payStatus = payStatus;
	}

	public Boolean getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Boolean approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Boolean getDisapproveStatus() {
		return disapproveStatus;
	}

	public void setDisapproveStatus(Boolean disapproveStatus) {
		this.disapproveStatus = disapproveStatus;
	}

	public Boolean getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(Boolean closedStatus) {
		this.closedStatus = closedStatus;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getFromSematicDescription() {
		return fromSematicDescription;
	}

	public void setFromSematicDescription(String fromSematicDescription) {
		this.fromSematicDescription = fromSematicDescription;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getToSematicDescription() {
		return toSematicDescription;
	}

	public void setToSematicDescription(String toSematicDescription) {
		this.toSematicDescription = toSematicDescription;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
