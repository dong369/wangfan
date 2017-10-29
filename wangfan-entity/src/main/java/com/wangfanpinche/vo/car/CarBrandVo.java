package com.wangfanpinche.vo.car;

import java.time.LocalDateTime;


/**
 * 车辆品牌
 * 
 *
 */
public class CarBrandVo{
	
	private String id;// ID	
	
	private LocalDateTime createDateTime;// 创建时间

	private String name;// 名称
	
	private String initial;// 首字母	
	
	private String picUrl;// 品牌图片路径
	
	public CarBrandVo() {
	}
		
	public CarBrandVo(String id) {
		super();
		this.id = id;
	}

	public CarBrandVo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public CarBrandVo(String id, String name, String initial, LocalDateTime createDateTime) {
		super();
		this.id = id;
		this.name = name;
		this.initial = initial;
		this.createDateTime = createDateTime;
	}

	
	public CarBrandVo(String id, String name, String initial) {
		super();
		this.id = id;
		this.name = name;
		this.initial = initial;
	}
	
	public CarBrandVo(String id, LocalDateTime createDateTime, String name, String initial, String picUrl) {
		super();
		this.id = id;
		this.createDateTime = createDateTime;
		this.name = name;
		this.initial = initial;
		this.picUrl = picUrl;
	}

	public CarBrandVo(String id, String name, String initial, String picUrl) {
		super();
		this.id = id;
		this.name = name;
		this.initial = initial;
		this.picUrl = picUrl;
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
}
