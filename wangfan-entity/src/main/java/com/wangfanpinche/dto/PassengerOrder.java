package com.wangfanpinche.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

import com.wangfanpinche.dto.OwnerOrder.Status;
import com.wangfanpinche.dto.base.BaseEntity;

@Entity
@Table( name = "t_passengerorder")
@DynamicInsert
@DynamicUpdate
public class PassengerOrder extends BaseEntity {
	
	@OneToOne(fetch=FetchType.LAZY)
	private OwnerLocation fromLocation;//起始位置
	
	@OneToOne(fetch=FetchType.LAZY)
	private OwnerLocation toLocation;//目标位置
	
	private Integer seat; //座位数
	
	private LocalDateTime publishDateTime;//发布时间
	
	private String description;//描述
	
	@Column(precision = 23, scale = 12)
	private BigDecimal amount;//金额
	
	@Type(type = "true_false")
	@Column(nullable = false)
	private Boolean payStatus = false;
	
	private LocalDateTime payDateTime;
	
	@OneToMany
	@JoinTable(name = "t_passengerorder_ownerorder")
	private List<OwnerOrder> owners;//投标的车主们
	
	@OneToOne
	private OwnerOrder approveOwner;//同意的车主
	
	@Enumerated
	private Status status;//状态
	
	@ManyToOne
	private User user;
	
	private Long orderNumber;//订单号
	

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

	public LocalDateTime getPublishDateTime() {
		return publishDateTime;
	}

	public void setPublishDateTime(LocalDateTime publishDateTime) {
		this.publishDateTime = publishDateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<OwnerOrder> getOwners() {
		return owners;
	}

	public void setOwners(List<OwnerOrder> owners) {
		this.owners = owners;
	}

	public OwnerOrder getApproveOwner() {
		return approveOwner;
	}

	public void setApproveOwner(OwnerOrder approveOwner) {
		this.approveOwner = approveOwner;
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

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	
	
	

}
