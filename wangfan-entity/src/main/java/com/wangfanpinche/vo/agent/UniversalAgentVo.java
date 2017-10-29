package com.wangfanpinche.vo.agent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Enumerated;

import org.springframework.format.annotation.DateTimeFormat;

import com.wangfanpinche.dto.agent.UniversalAgent.UniversalAgentType;

public class UniversalAgentVo {
	
	private String fromUserPhone;
	
	private String toUserPhone;
	
	private String code;// 验证码
	
	@Enumerated
	private UniversalAgentType regType;//哪种方式推荐来的
	
	private Long totalPeople;//推荐人数
	
	private BigInteger totalOrder;//推荐人的完成车主车单数量
	
	private BigDecimal totalOrderMoney;//推荐人的完成车主车单金额收益
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:ss")
	private LocalDateTime departDateTime;// 出行时间
	
	private BigDecimal reaultFare;//单个车单总车费

	public String getFromUserPhone() {
		return fromUserPhone;
	}

	public void setFromUserPhone(String fromUserPhone) {
		this.fromUserPhone = fromUserPhone;
	}

	public String getToUserPhone() {
		return toUserPhone;
	}

	public void setToUserPhone(String toUserPhone) {
		this.toUserPhone = toUserPhone;
	}

	public UniversalAgentType getRegType() {
		return regType;
	}

	public void setRegType(UniversalAgentType regType) {
		this.regType = regType;
	}

	public Long getTotalPeople() {
		return totalPeople;
	}

	public void setTotalPeople(Long totalPeople) {
		this.totalPeople = totalPeople;
	}

	public BigDecimal getTotalOrderMoney() {
		return totalOrderMoney;
	}

	public void setTotalOrderMoney(BigDecimal totalOrderMoney) {
		this.totalOrderMoney = totalOrderMoney;
	}

	public BigDecimal getReaultFare() {
		return reaultFare;
	}

	public void setReaultFare(BigDecimal reaultFare) {
		this.reaultFare = reaultFare;
	}

	public BigInteger getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(BigInteger totalOrder) {
		this.totalOrder = totalOrder;
	}

	public LocalDateTime getDepartDateTime() {
		return departDateTime;
	}

	public void setDepartDateTime(LocalDateTime departDateTime) {
		this.departDateTime = departDateTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	

}
