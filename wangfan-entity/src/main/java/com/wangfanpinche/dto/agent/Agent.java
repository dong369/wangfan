package com.wangfanpinche.dto.agent;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 代理商表
 */
@Entity
@Table(name = "t_agent")
@DynamicInsert
@DynamicUpdate
public class Agent extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY)
	private User user;// 用户

	private LocalDateTime startDateTime;// 成为代理商日期

	@OneToOne
	private Procity procities;// 代理区域

	private String remark;// 备注

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Procity getProcities() {
		return procities;
	}

	public void setProcities(Procity procities) {
		this.procities = procities;
	}


}
