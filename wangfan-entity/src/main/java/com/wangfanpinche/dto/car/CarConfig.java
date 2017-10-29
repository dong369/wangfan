package com.wangfanpinche.dto.car;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;


@Entity
@Table(name = "t_carconfig")
@DynamicInsert
@DynamicUpdate
public class CarConfig extends BaseEntity {
	
	private BigDecimal postage;//油费
	
	private BigDecimal toll;//过路费

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

	
	
}
