package com.wangfanpinche.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车单-标签
 */
@Entity
@Table(name = "t_tag")
@DynamicInsert
@DynamicUpdate
public class Tag extends BaseEntity{
	
	@Column(length=20)
	private String tagName;//标签内容
	
	@Column(length=10)
	private Integer seq;//顺序
	
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	
}
