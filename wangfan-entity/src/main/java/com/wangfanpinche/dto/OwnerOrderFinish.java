package com.wangfanpinche.dto;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车单-完成
 *
 */
@Entity
@Table(name = "t_ownerorderfinish")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderFinish extends BaseEntity{
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private OwnerOrder ownerOrder;
	
	@OneToMany
	@JoinTable(name="t_ownerorderfinish_ownerorderfinishownerevaluate")
	private Set<OwnerOrderFinishOwnerEvaluate> ownerEvaluate;//乘客给车主的评价
	
	@OneToMany
	@JoinTable(name="t_ownerorderfinish_ownerorderfinishpassengerevaluate")
	private Set<OwnerOrderFinishPassengerEvaluate> passengerEvaluate;//车主给乘客的评价
	
	@Enumerated(EnumType.ORDINAL)
	private FinishStatus status;//状态(是否完成)
	
	@Enumerated(EnumType.ORDINAL)
	private InvoiceStatus invoiceStatus;//发票状态(已拒绝  未申请 审核中 已通过)
	
	private Integer passengerSeat;//乘客所占有的座位数
	
	@Column(precision = 23, scale = 12)
	private BigDecimal allfare;//车单总金额
	
	
	public static enum FinishStatus{
		FINISH, NO_FINISH;
	}

	public static enum InvoiceStatus{
		已拒绝, 未申请, 审核中, 已通过;
	}

	public OwnerOrder getOwnerOrder() {
		return ownerOrder;
	}

	public void setOwnerOrder(OwnerOrder ownerOrder) {
		this.ownerOrder = ownerOrder;
	}

	public Set<OwnerOrderFinishOwnerEvaluate> getOwnerEvaluate() {
		return ownerEvaluate;
	}

	public void setOwnerEvaluate(Set<OwnerOrderFinishOwnerEvaluate> ownerEvaluate) {
		this.ownerEvaluate = ownerEvaluate;
	}

	public Set<OwnerOrderFinishPassengerEvaluate> getPassengerEvaluate() {
		return passengerEvaluate;
	}

	public void setPassengerEvaluate(Set<OwnerOrderFinishPassengerEvaluate> passengerEvaluate) {
		this.passengerEvaluate = passengerEvaluate;
	}

	public FinishStatus getStatus() {
		return status;
	}

	public void setStatus(FinishStatus status) {
		this.status = status;
	}

	public InvoiceStatus getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Integer getPassengerSeat() {
		return passengerSeat;
	}

	public void setPassengerSeat(Integer passengerSeat) {
		this.passengerSeat = passengerSeat;
	}

	public BigDecimal getAllfare() {
		return allfare;
	}

	public void setAllfare(BigDecimal allfare) {
		this.allfare = allfare;
	}
	
	
}
