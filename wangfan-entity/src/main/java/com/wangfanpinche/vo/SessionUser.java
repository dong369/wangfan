package com.wangfanpinche.vo;

import java.util.ArrayList;
import java.util.List;

public class SessionUser {
	
	private String id;// 用户id
	
	private String mobilePhone;// 手机号
	
	private String userIcon;// 头像
	
	private String phoneToken;//设备号和mac	
	
	private List<String> resourceList = new ArrayList<>(0);	// 用户资源
	
	private List<ResourceVo> menuList = new ArrayList<>(0);// 用户菜单
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public List<String> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}

	public List<ResourceVo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<ResourceVo> menuList) {
		this.menuList = menuList;
	}

	public String getPhoneToken() {
		return phoneToken;
	}

	public void setPhoneToken(String phoneToken) {
		this.phoneToken = phoneToken;
	}
	
}
