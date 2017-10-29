package com.wangfanpinche.dto.agent;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 全民代理
 */
@Entity
@Table(name = "t_universalagent")
public class UniversalAgent extends BaseEntity {
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User fromUser;//推荐人
	
	@OneToOne(fetch=FetchType.LAZY)
	private User toUser;//被推荐人
	
	@Enumerated
	private UniversalAgentType regType;//如何推荐
	
	public static enum UniversalAgentType{
		WEB
	}

	public UniversalAgentType getRegType() {
		return regType;
	}

	public void setRegType(UniversalAgentType regType) {
		this.regType = regType;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

}
