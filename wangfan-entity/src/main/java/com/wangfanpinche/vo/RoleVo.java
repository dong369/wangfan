package com.wangfanpinche.vo;

import java.util.ArrayList;
import java.util.List;


/**
 * 角色
 *
 */
public class RoleVo {
	
	private String id;
	private String roleId;// 上级角色id
	private String roleName;// 上级角色名称
	private String name;// 名称
	private String remark;// 备注
	private Integer seq;// 排序
	
	private List<ResourceVo> resources = new ArrayList<>();
	
	public RoleVo() {
		super();
	}
	
	public RoleVo(String id) {
		super();
		this.id = id;
	}

	public RoleVo(String id, String roleId, String roleName, String name, String remark, Integer seq) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.roleName = roleName;
		this.name = name;
		this.remark = remark;
		this.seq = seq;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public List<ResourceVo> getResources() {
		return resources;
	}

	public void setResources(List<ResourceVo> resources) {
		this.resources = resources;
	}
}
