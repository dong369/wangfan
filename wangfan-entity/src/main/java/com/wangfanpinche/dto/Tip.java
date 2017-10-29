package com.wangfanpinche.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 提示
 *
 */
@Entity
@Table(name = "t_tip")
@DynamicInsert
@DynamicUpdate
public class Tip extends BaseEntity{
	
	@Column(length=50)
	private String tipName;//提示内容
	
	@Column(length=100)
	private String tipDescription;//提示描述

	
	public String getTipName() {
		return tipName;
	}

	public void setTipName(String tipName) {
		this.tipName = tipName;
	}

	public String getTipDescription() {
		return tipDescription;
	}

	public void setTipDescription(String tipDescription) {
		this.tipDescription = tipDescription;
	}		
}
