package com.wangfanpinche.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车主-乘客
 * 
 */
@Entity
@Table(name = "t_ownerorderpublishpassenger")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderPublishPassenger extends BaseEntity {

	private Boolean payStatus = false;// 支付状态 待付款、已付款
	
	private LocalDateTime payDateTime;//支付时间
	
	private Boolean approveStatus = false;// 车主是否同意
	
	private LocalDateTime approveDateTime;//同意时间
	
	private Boolean disapproveStatus = false;//是否拒绝
	
	private LocalDateTime disapproveDateTime;

	private Boolean closedStatus = false;//关闭时间
	
	private LocalDateTime closedDateTime;

	@OneToOne(fetch=FetchType.LAZY, optional = false)
	private OwnerLocation fromLocation;//上车地点
	
	@OneToOne(fetch=FetchType.LAZY, optional = false)
	private OwnerLocation toLocation;//下车地点
	
	private Integer seat;// 所占座位
	
	private String descript;//备注
	
	@Column(precision = 23, scale = 12)
	private BigDecimal money;// 金额 单位：分
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;//用户
	
	@Enumerated
	private Type type;//乘客类型(车主主动找乘客  乘客找车主)
		
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private OwnerOrder ownerOrder;
	
	private Long orderNumber;//订单号
	
	@OneToOne(fetch=FetchType.LAZY)
	private PassengerOrder passengerOrder;//乘客车单
	
	public static enum Type{
		OWNER,//车主主动找乘客
		PASSENGER//乘客找车主
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

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public OwnerOrder getOwnerOrder() {
		return ownerOrder;
	}

	public void setOwnerOrder(OwnerOrder ownerOrder) {
		this.ownerOrder = ownerOrder;
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

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public PassengerOrder getPassengerOrder() {
		return passengerOrder;
	}

	public void setPassengerOrder(PassengerOrder passengerOrder) {
		this.passengerOrder = passengerOrder;
	}

}
