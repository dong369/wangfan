package com.wangfanpinche.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wangfanpinche.dto.base.BaseEntity;
import com.wangfanpinche.entity.utils.LongAsStringWrapper;

/**
 * 账单
 *
 */
@Entity
@Table(name = "t_journal")
@DynamicInsert
@DynamicUpdate
public class Journal extends BaseEntity{

	@Enumerated
	private BizType type;//交易类型: 诚信必发，乘客支付车主车单，乘客支付乘客车单，充值，提现
	
	@Enumerated
	private InOutType InOutType;//收入，支出
	
	@Enumerated
	private PayType payType;//付款方式(支付宝 微信)
	
	@Column(precision = 23,scale = 12)
	private BigDecimal money;//金额
	
	@Enumerated
	private Status status;//交易状态
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;//创建人
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User reviewer;//审核人
	
	private LocalDateTime reviewDateTime;//审核时间
	
	@JSONField(serializeUsing=LongAsStringWrapper.class)
	private Long orderNumber;//订单号
	
	@ManyToOne
	private Favour favour;//收款信息
	
	@ManyToOne
	private Payment payment;//付款信息
	
	@ManyToOne
	private PayInfo payInfo;//支付信息
	
	public static void main(String[] args) {
		Journal j = new Journal();
		j.setOrderNumber(111111111111111L);
		j.setCreateDateTime(LocalDateTime.now());
		System.out.println(JSON.toJSONString(j));
	}
	
	public static enum BizType{
		诚信必发,
		乘客支付车主车单,
		乘客支付乘客车单,
		充值,
		提现
	}
	
	public static enum Status{
		未支付,
		审核中,
		完成
	}
	public static enum InOutType{
		收入,
		支出
	}
	public static enum PayType{
		支付宝,
		微信
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


	public Favour getFavour() {
		return favour;
	}

	public void setFavour(Favour favour) {
		this.favour = favour;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public PayInfo getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(PayInfo payInfo) {
		this.payInfo = payInfo;
	}


	public InOutType getInOutType() {
		return InOutType;
	}

	public void setInOutType(InOutType inOutType) {
		InOutType = inOutType;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setType(BizType type) {
		this.type = type;
	}

	public BizType getType() {
		return type;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public LocalDateTime getReviewDateTime() {
		return reviewDateTime;
	}

	public void setReviewDateTime(LocalDateTime reviewDateTime) {
		this.reviewDateTime = reviewDateTime;
	}
	
	
}
