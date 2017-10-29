package com.wangfanpinche.vo.car;

import java.time.LocalDateTime;

/**
 * 车的排量 
 *
 */
public class CarDisplacementVo {

	private String id;//排量id
	
	private String name;//排量名称
	
	private String yearId;//年代款id
	
	private String yearName;//年代款名称
	
	private String systemId;//车系id
	
	private String systemName;//车系名称
	
	private String brandId;//品牌id
	
	private String brandName;//品牌名称
	
	private LocalDateTime createDateTime;// 创建时间
	
	
	public CarDisplacementVo() {
		super();
	}
	

	public CarDisplacementVo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public CarDisplacementVo(String id, String name, String yearId, String yearName, String systemId, String systemName, String brandId, String brandName, LocalDateTime createDateTime) {
		super();
		this.id = id;
		this.name = name;
		this.yearId = yearId;
		this.yearName = yearName;
		this.systemId = systemId;
		this.systemName = systemName;
		this.brandId = brandId;
		this.brandName = brandName;
		this.createDateTime = createDateTime;
	}


	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
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

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}


	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	

	
}
