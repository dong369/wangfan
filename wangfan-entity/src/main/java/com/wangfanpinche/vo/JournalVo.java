package com.wangfanpinche.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.wangfanpinche.dto.Journal.BizType;
import com.wangfanpinche.dto.Journal.PayType;
import com.wangfanpinche.dto.Journal.Status;


/**
 * 账单
 *
 */
public class JournalVo{

	private String id;
	
	private LocalDateTime modifyDateTime;
	
	private BizType type;//交易类型: 诚信必发，乘客支付车主车单，乘客支付乘客车单，充值，提现
	
	private com.wangfanpinche.dto.Journal.InOutType InOutType;//收入，支出
	
	private PayType payType;//付款方式(支付宝 微信)
	
	private BigDecimal money;//金额
	
	private Status status;//交易状态
	
	private String userId;//创建人id
	
	private String username;//创建人名称
	
	private String reviewerId;//审核人id
	
	private String reviewername;//审核人名称
	
	private LocalDateTime reviewDateTime;//审核时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate searchDate;
	
	private Long orderNumber;//订单号
	

	public JournalVo(String id, BizType type, com.wangfanpinche.dto.Journal.InOutType inOutType, PayType payType, BigDecimal money, LocalDateTime modifyDateTime) {
		super();
		this.id = id;
		this.type = type;
		InOutType = inOutType;
		this.payType = payType;
		this.money = money;
		this.modifyDateTime = modifyDateTime;
	}
	
	public JournalVo(String id, BizType type, com.wangfanpinche.dto.Journal.InOutType inOutType, PayType payType, BigDecimal money) {
		super();
		this.id = id;
		this.type = type;
		InOutType = inOutType;
		this.payType = payType;
		this.money = money;
	}

	public JournalVo(String id, BigDecimal money, Status status, String userId, String username, String reviewerId, String reviewername, LocalDateTime reviewDateTime) {
		super();
		this.id = id;
		this.money = money;
		this.status = status;
		this.userId = userId;
		this.username = username;
		this.reviewerId = reviewerId;
		this.reviewername = reviewername;
		this.reviewDateTime = reviewDateTime;
	}

	public JournalVo() {
		super();
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BizType getType() {
		return type;
	}

	public void setType(BizType type) {
		this.type = type;
	}

	public com.wangfanpinche.dto.Journal.InOutType getInOutType() {
		return InOutType;
	}

	public void setInOutType(com.wangfanpinche.dto.Journal.InOutType inOutType) {
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewername() {
		return reviewername;
	}

	public void setReviewername(String reviewername) {
		this.reviewername = reviewername;
	}

	public LocalDateTime getReviewDateTime() {
		return reviewDateTime;
	}

	public void setReviewDateTime(LocalDateTime reviewDateTime) {
		this.reviewDateTime = reviewDateTime;
	}

	public LocalDateTime getModifyDateTime() {
		return modifyDateTime;
	}

	public void setModifyDateTime(LocalDateTime modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}

	public LocalDate getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(LocalDate searchDate) {
		this.searchDate = searchDate;
	}

	
	
}
