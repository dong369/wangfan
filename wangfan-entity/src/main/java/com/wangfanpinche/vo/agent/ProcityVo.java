package com.wangfanpinche.vo.agent;

import com.wangfanpinche.dto.agent.Procity.Type;

/**
 * 省市县联动
 *
 */
public class ProcityVo {
	
	private String id;

	private String name;// 名称
	
	private Type type;// 类型

	private Integer seq;// 排序
	
	private String code;//代码
	
	private String procityId;//上一级id
	
	private String level;//等级
	

	public ProcityVo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public ProcityVo() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getProcityId() {
		return procityId;
	}

	public void setProcityId(String procityId) {
		this.procityId = procityId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
}
