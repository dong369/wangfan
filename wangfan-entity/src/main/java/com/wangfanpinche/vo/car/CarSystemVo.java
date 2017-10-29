package com.wangfanpinche.vo.car;


import java.time.LocalDateTime;

/**
 * 车系
 *
 */
public class CarSystemVo{
	
	private String id;//车系id
	
	private String name;//车系名称
	
	private String brandId;//品牌id
	
	private String brandName;//品牌名称
	
	private LocalDateTime createDateTime;// 创建时间
	
	
	public CarSystemVo() {
		super();
	}
	
	public CarSystemVo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public CarSystemVo(String id, String brandId, String brandName, String name, LocalDateTime createDateTime) {
		super();
		this.id = id;
		this.brandId = brandId;
		this.brandName = brandName;
		this.name = name;
		this.createDateTime = createDateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
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

	
}
