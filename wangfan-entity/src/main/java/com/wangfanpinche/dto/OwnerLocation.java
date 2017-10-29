package com.wangfanpinche.dto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车主定位
 *
 */
@Entity
@Table(name = "t_ownerlocation")
@DynamicInsert
@DynamicUpdate
public class OwnerLocation extends BaseEntity{
	
	@Column(precision = 22, scale = 8)
	private BigDecimal lng;// 经度
	
	@Column(precision = 22, scale = 8)
	private BigDecimal lat;// 纬度
	
	@Column(length=20)
	private String province;// 省
	
	@Column(length=20)
	private String city;// 市
	
	@Column(length=20)
	private String district;// 区
	
	@Column(length=20)
	private String street;// 街道
	
	@Column(length=20)
	private String streetNumber;// 街道号码
	
	private String sematicDescription;//描述
	
	private String cityCode;

	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public String getSematicDescription() {
		return sematicDescription;
	}

	public void setSematicDescription(String sematicDescription) {
		this.sematicDescription = sematicDescription;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

}
