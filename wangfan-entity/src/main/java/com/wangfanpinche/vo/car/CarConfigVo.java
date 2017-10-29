package com.wangfanpinche.vo.car;

import java.math.BigDecimal;

public class CarConfigVo {
	
	private String id;//id
	
	private BigDecimal postage;//油费
	
	private BigDecimal toll;//过路费
	
	public CarConfigVo(String id, BigDecimal postage, BigDecimal toll) {
		super();
		this.id = id;
		this.postage = postage;
		this.toll = toll;
	}

	public CarConfigVo() {
		super();
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public BigDecimal getToll() {
		return toll;
	}

	public void setToll(BigDecimal toll) {
		this.toll = toll;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
