package com.wangfanpinche.dto.agent;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 省市县联动
 *
 */
@Entity
@Table(name = "t_procity")
@DynamicInsert
@DynamicUpdate
public class Procity extends BaseEntity {

	private String name;// 名称
	
	@Enumerated(EnumType.ORDINAL)
	private Type type;// 类型

	private Integer seq;// 排序
	
	private String code;//代码
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Procity procity;//上一级
	
	private String level;//等级
	
	public static enum Type {
		PROVINCE,//省
		CITY,//市
		DISTRICT,//区县
		MUNICIPALITIES,//直辖市
		OTHER//港澳台
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Procity getProcity() {
		return procity;
	}

	public void setProcity(Procity procity) {
		this.procity = procity;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
}
