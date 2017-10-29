package com.wangfanpinche.vo;

import com.wangfanpinche.dto.Advertise.AdLocation;

public class AdvertiseVo {
	
	private String id;//id

	private String title;//标题
	
	private String descript;//描述

	private String picUrl;// 广告路径

	private AdLocation adLocation;// 广告类型

	private String h5url;// h5的链接地址

	private Integer second;//显示几秒
	
	
	public AdvertiseVo(String id, String title, String descript, String picUrl, AdLocation adLocation, String h5url, Integer second) {
		super();
		this.id = id;
		this.title = title;
		this.descript = descript;
		this.picUrl = picUrl;
		this.adLocation = adLocation;
		this.h5url = h5url;
		this.second = second;
	}


	public AdvertiseVo(){}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public AdLocation getAdLocation() {
		return adLocation;
	}

	public void setAdLocation(AdLocation adLocation) {
		this.adLocation = adLocation;
	}

	public String getH5url() {
		return h5url;
	}

	public void setH5url(String h5url) {
		this.h5url = h5url;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	
}
