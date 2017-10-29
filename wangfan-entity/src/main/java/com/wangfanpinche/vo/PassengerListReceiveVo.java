package com.wangfanpinche.vo;


/**
 * 接的乘客
 *
 */
public class PassengerListReceiveVo {
	
	private String id;//乘客id

	private Boolean goinStatus = false;// 是否上车
	
	private String goinCity;//上车城市
	
	private String goinSematicDescription;//上车地点描述
	
	private String userId;//用户id
	
	private String userIcon;//用户头像
	
	private String mobilePhone;//手机号
	
	private String username;//用户姓名


	public PassengerListReceiveVo(String id, Boolean goinStatus, String goinCity, String goinSematicDescription, String userId, String userIcon, String mobilePhone, String username) {
		super();
		this.id = id;
		this.goinStatus = goinStatus;
		this.goinCity = goinCity;
		this.goinSematicDescription = goinSematicDescription;		
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
	}


	public PassengerListReceiveVo() {
		super();
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getGoinStatus() {
		return goinStatus;
	}

	public void setGoinStatus(Boolean goinStatus) {
		this.goinStatus = goinStatus;
	}

	public String getGoinCity() {
		return goinCity;
	}

	public void setGoinCity(String goinCity) {
		this.goinCity = goinCity;
	}

	public String getGoinSematicDescription() {
		return goinSematicDescription;
	}

	public void setGoinSematicDescription(String goinSematicDescription) {
		this.goinSematicDescription = goinSematicDescription;
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
	
}
