package com.wangfanpinche.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.wangfanpinche.dto.Car;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.base.BaseEntity;

@Entity
@Table(name = "t_orderowner")
@DynamicInsert
@DynamicUpdate
public class OrderOwner extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;// 车单发布者

	@ManyToOne(fetch = FetchType.LAZY)
	private Car car;// 所用车量

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	private Location fromLocation;// 起始位置

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	private Location toLocation;// 结束位置

	private LocalDateTime departDateTime;// 出行时间

	private Integer seat;// 总座位数

	private Integer currentSeat;// 当前座位数

	@Type(type = "true_false")
	private Boolean honesty;// 是否诚信必发
	
	private Long honestyOrderNumber;// 诚信必发订单号

	@Column(precision = 23, scale = 12)
	private BigDecimal singleFare;// 车费 人/次

	@Column(precision = 23, scale = 12)
	private BigDecimal resultFare;// 车费 总费用

	private String via;// 途径

	private String description;// 备注
	
	@Enumerated(EnumType.ORDINAL)
	private OrderOwnerStatus status;//状态
	
	@OneToMany
	@JoinTable(name="t_orderowner_passengers")
	private List<OrderPassenger> passengers  = new ArrayList<>(0);//可以有多个人申请加入此车单

	public static enum OrderOwnerStatus {
		PUBLISH, // 发布
		RECEIVE, // 接乘客
		DEPART, // 发车
		FINISH, // 完成
		BACK, // 平台录入信息
		CLOSED// 关闭
	}

	// ---------------缓存字段-************
	// 车品牌和车系
	private String carBrandAndSystem;
	// 车颜色
	private String carColor;
	//车牌号
	@Column(length=10)
	private String carNumber;

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

	public Location getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(Location fromLocation) {
		this.fromLocation = fromLocation;
	}

	public Location getToLocation() {
		return toLocation;
	}

	public void setToLocation(Location toLocation) {
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

	public Boolean getHonesty() {
		return honesty;
	}

	public void setHonesty(Boolean honesty) {
		this.honesty = honesty;
	}

	public Long getHonestyOrderNumber() {
		return honestyOrderNumber;
	}

	public void setHonestyOrderNumber(Long honestyOrderNumber) {
		this.honestyOrderNumber = honestyOrderNumber;
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

	public String getCarBrandAndSystem() {
		return carBrandAndSystem;
	}

	public void setCarBrandAndSystem(String carBrandAndSystem) {
		this.carBrandAndSystem = carBrandAndSystem;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public BigDecimal getSingleFare() {
		return singleFare;
	}

	public void setSingleFare(BigDecimal singleFare) {
		this.singleFare = singleFare;
	}

	public BigDecimal getResultFare() {
		return resultFare;
	}

	public void setResultFare(BigDecimal resultFare) {
		this.resultFare = resultFare;
	}

	public List<OrderPassenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<OrderPassenger> passengers) {
		this.passengers = passengers;
	}

	public OrderOwnerStatus getStatus() {
		return status;
	}

	public void setStatus(OrderOwnerStatus status) {
		this.status = status;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

}
