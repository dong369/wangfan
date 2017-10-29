package com.wangfanpinche.vo;


/**
 * 下车乘客
 *
 */
public class PassengerListDepartVo {
	
	private String id;//乘客id

	private Boolean gooutStatus = false;// 是否下车
	
	private String gooutCity;//下车城市
	
	private String gooutSematicDescription;//下车地点描述
	
	private String userId;//用户id
	
	private String userIcon;//用户头像
	
	private String mobilePhone;//手机号
	
	private String username;//用户姓名


	public PassengerListDepartVo(String id, Boolean gooutStatus, String gooutCity, String gooutSematicDescription, String userId, String userIcon, String mobilePhone, String username) {
		super();
		this.id = id;
		this.gooutStatus = gooutStatus;
		this.gooutCity = gooutCity;
		this.gooutSematicDescription = gooutSematicDescription;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
	}


	public PassengerListDepartVo() {
		super();
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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


	public Boolean getGooutStatus() {
		return gooutStatus;
	}


	public void setGooutStatus(Boolean gooutStatus) {
		this.gooutStatus = gooutStatus;
	}


	public String getGooutCity() {
		return gooutCity;
	}


	public void setGooutCity(String gooutCity) {
		this.gooutCity = gooutCity;
	}


	public String getGooutSematicDescription() {
		return gooutSematicDescription;
	}


	public void setGooutSematicDescription(String gooutSematicDescription) {
		this.gooutSematicDescription = gooutSematicDescription;
	}
	
}
