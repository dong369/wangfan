package com.wangfanpinche.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.base.BaseEntity;

@Entity
@Table(name = "t_orderpassenger")
@DynamicInsert
@DynamicUpdate
public class OrderPassenger extends BaseEntity {

	// 车单信息
	@ManyToOne
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	private Location fromLocation;// 起始位置

	@OneToOne(fetch = FetchType.LAZY)
	private Location toLocation;// 结束位置

	private LocalDateTime departDateTime;// 出行时间

	private Integer seat;// 总座位数

	@Column(precision = 23, scale = 8)
	private BigDecimal singleFare;// 车费 人/次

	@Column(precision = 23, scale = 8)
	private BigDecimal resultFare;// 车费 总费用

	@Column(precision = 23, scale = 8)
	private BigDecimal realFare;//实际支付钱数

	@OneToMany
	@JoinTable(name = "t_orderpassenger_owners")
	private List<OrderOwner> owners  = new ArrayList<>(0);// 申请列表

	@ManyToOne
	private OrderOwner orderOwner;// 同意加入

	private String descript;// 备注

	@Type(type = "true_false")
	private Boolean closedStatus = false;// 关闭状态

	private LocalDateTime closedDateTime;// 关闭时间

	private String closedDescirpt;// 关闭原因

	// *************车单上车前准备*************
	@Enumerated
	private OrderPassengerType passengerType; // 坐车类型
	
	public static enum OrderPassengerType {
		Owner,//司机邀请
		Passenger//乘客主动加入
	}

	@Type(type = "true_false")
	private Boolean payStatus = false;// 支付状态 待付款、已付款

	private LocalDateTime payDateTime;// 支付时间

	private Long payOrderNumber;// 支付订单号

	@Type(type = "true_false")
	private Boolean approveStatus  = false;// 车主是否同意

	private LocalDateTime approveDateTime;// 同意时间

	@Type(type = "true_false")
	private Boolean disapproveStatus  = false;// 是否拒绝

	private LocalDateTime disapproveDateTime;//拒绝时间

	// ********************上车***************************
	@Type(type = "true_false")
	private Boolean goinStatus  = false;// 是否上车

	private LocalDateTime goinDateTime;// 上车时间

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	private Location goinLocation;// 上车地点

	// ********************下车***************************
	@Type(type = "true_false")
	private Boolean gooutStatus = false;// 是否下车

	private LocalDateTime gooutDateTime;// 下车时间

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	private Location gooutLocation;// 下车地点

	// ---------------评价-----------------
	@OneToOne(fetch=FetchType.LAZY)
	private Evaluate toEvaluate;// 给司机的评价

	@OneToOne(fetch=FetchType.LAZY)
	private Evaluate fromEvaluate;// 司机给乘客的评价
	
	@Type(type = "true_false")
	private Boolean toEvaluateModify = false;//给司机的评价是否评价过
	
	@Type(type = "true_false")
	private Boolean fromEvaluateModify = false;//给乘客的评价是否评价过
	
	@Enumerated
	private OrderPassengerStatus status;//状态
	
	public static enum OrderPassengerStatus {
		PUBLISH, // 待接单
		TAKER,//已接单，待发车
		NO_RECEIVE,//已发车，未接被接到
		RECEIVE, //被接到，未被送达
		FINISH, // 已完成
		CLOSED// 关闭
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Boolean getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(Boolean closedStatus) {
		this.closedStatus = closedStatus;
	}

	public LocalDateTime getClosedDateTime() {
		return closedDateTime;
	}

	public void setClosedDateTime(LocalDateTime closedDateTime) {
		this.closedDateTime = closedDateTime;
	}

	public String getClosedDescirpt() {
		return closedDescirpt;
	}

	public void setClosedDescirpt(String closedDescirpt) {
		this.closedDescirpt = closedDescirpt;
	}

	public Boolean getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Boolean payStatus) {
		this.payStatus = payStatus;
	}

	public LocalDateTime getPayDateTime() {
		return payDateTime;
	}

	public void setPayDateTime(LocalDateTime payDateTime) {
		this.payDateTime = payDateTime;
	}

	public Long getPayOrderNumber() {
		return payOrderNumber;
	}

	public void setPayOrderNumber(Long payOrderNumber) {
		this.payOrderNumber = payOrderNumber;
	}

	public Boolean getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Boolean approveStatus) {
		this.approveStatus = approveStatus;
	}

	public LocalDateTime getApproveDateTime() {
		return approveDateTime;
	}

	public void setApproveDateTime(LocalDateTime approveDateTime) {
		this.approveDateTime = approveDateTime;
	}

	public Boolean getDisapproveStatus() {
		return disapproveStatus;
	}

	public void setDisapproveStatus(Boolean disapproveStatus) {
		this.disapproveStatus = disapproveStatus;
	}

	public LocalDateTime getDisapproveDateTime() {
		return disapproveDateTime;
	}

	public void setDisapproveDateTime(LocalDateTime disapproveDateTime) {
		this.disapproveDateTime = disapproveDateTime;
	}

	public Boolean getGoinStatus() {
		return goinStatus;
	}

	public void setGoinStatus(Boolean goinStatus) {
		this.goinStatus = goinStatus;
	}

	public LocalDateTime getGoinDateTime() {
		return goinDateTime;
	}

	public void setGoinDateTime(LocalDateTime goinDateTime) {
		this.goinDateTime = goinDateTime;
	}

	public Boolean getGooutStatus() {
		return gooutStatus;
	}

	public void setGooutStatus(Boolean gooutStatus) {
		this.gooutStatus = gooutStatus;
	}

	public LocalDateTime getGooutDateTime() {
		return gooutDateTime;
	}

	public void setGooutDateTime(LocalDateTime gooutDateTime) {
		this.gooutDateTime = gooutDateTime;
	}

	public Evaluate getToEvaluate() {
		return toEvaluate;
	}

	public void setToEvaluate(Evaluate toEvaluate) {
		this.toEvaluate = toEvaluate;
	}

	public Evaluate getFromEvaluate() {
		return fromEvaluate;
	}

	public void setFromEvaluate(Evaluate fromEvaluate) {
		this.fromEvaluate = fromEvaluate;
	}

	public OrderOwner getOrderOwner() {
		return orderOwner;
	}

	public void setOrderOwner(OrderOwner orderOwner) {
		this.orderOwner = orderOwner;
	}

	public List<OrderOwner> getOwners() {
		return owners;
	}

	public void setOwners(List<OrderOwner> owners) {
		this.owners = owners;
	}

	public OrderPassengerType getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(OrderPassengerType passengerType) {
		this.passengerType = passengerType;
	}

	public OrderPassengerStatus getStatus() {
		return status;
	}

	public void setStatus(OrderPassengerStatus status) {
		this.status = status;
	}

	public BigDecimal getRealFare() {
		return realFare;
	}

	public void setRealFare(BigDecimal realFare) {
		this.realFare = realFare;
	}

	public Location getGoinLocation() {
		return goinLocation;
	}

	public void setGoinLocation(Location goinLocation) {
		this.goinLocation = goinLocation;
	}

	public Location getGooutLocation() {
		return gooutLocation;
	}

	public void setGooutLocation(Location gooutLocation) {
		this.gooutLocation = gooutLocation;
	}

	public Boolean getToEvaluateModify() {
		return toEvaluateModify;
	}

	public void setToEvaluateModify(Boolean toEvaluateModify) {
		this.toEvaluateModify = toEvaluateModify;
	}

	public Boolean getFromEvaluateModify() {
		return fromEvaluateModify;
	}

	public void setFromEvaluateModify(Boolean fromEvaluateModify) {
		this.fromEvaluateModify = fromEvaluateModify;
	}


}
