package com.wangfanpinche.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车主给乘客的评价
 *
 */
@Entity
@Table(name = "t_ownerorderfinishpassengerevaluate")
@DynamicInsert
@DynamicUpdate
public class OwnerOrderFinishPassengerEvaluate extends BaseEntity{
	
	
	@ElementCollection
	@CollectionTable(name="t_ownerorderfinishpassengerevaluate_tag")
	private List<String> tags = new ArrayList<>(0);//车主给乘客贴的标签
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User fromUser;//谁评价的
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User toUser;//评价给谁

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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
