package com.wangfanpinche.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;


/**
 * 车主车单
 *
 */
@Entity
@Table(name = "t_ownerorder")
@DynamicInsert
@DynamicUpdate
public class OwnerOrder extends BaseEntity {
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;//谁发布的车单
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Car car;//所用车量
	
	@Column(length=20)
	private String brandSystem;//车品牌和车系
		
	@ElementCollection
	@CollectionTable(name="t_ownerorder_tag")
	private List<String> carTags = new ArrayList<>(0);//标签
	
	@OneToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST})
	private OwnerLocation fromLocation;//起始位置
	
	@OneToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST})
	private OwnerLocation toLocation;//结束位置

	private LocalDateTime departDateTime;//出行时间
	
	private Integer seat;//总座位数
	
	private Integer currentSeat;//当前座位数
	
	@Column(precision = 23, scale = 12)
	private BigDecimal fare;//车费 人/次
	
	private Boolean honesty = false;//是否诚信必发
	
	@Enumerated(EnumType.ORDINAL)
	private Status status;//状态
	
	private String via;//途径
	
	private String description;//备注
	
	private Long orderNumber;//订单号
	
	public static enum Status{
		PUBLISH,//发布
		RECEIVE,//接乘客
		DEPART,//发车
		FINISH,//完成
		BACK,//平台录入信息
		CLOSED//关闭
	}
	
	
	public OwnerLocation getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(OwnerLocation fromLocation) {
		this.fromLocation = fromLocation;
	}

	public OwnerLocation getToLocation() {
		return toLocation;
	}

	public void setToLocation(OwnerLocation toLocation) {
		this.toLocation = toLocation;
	}

	public LocalDateTime getDepartDateTime() {
		return departDateTime;
	}

	public void setDepartDateTime(LocalDateTime departDateTime) {
		this.departDateTime = departDateTime;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public Integer getCurrentSeat() {
		return currentSeat;
	}

	public void setCurrentSeat(Integer currentSeat) {
		this.currentSeat = currentSeat;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public Boolean getHonesty() {
		return honesty;
	}

	public void setHonesty(Boolean honesty) {
		this.honesty = honesty;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public List<String> getCarTags() {
		return carTags;
	}

	public void setCarTags(List<String> carTags) {
		this.carTags = carTags;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getBrandSystem() {
		return brandSystem;
	}

	public void setBrandSystem(String brandSystem) {
		this.brandSystem = brandSystem;
	}

}
