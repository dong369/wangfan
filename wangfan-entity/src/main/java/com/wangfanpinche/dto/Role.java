package com.wangfanpinche.dto;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;


/**
 * 角色
 *
 */
@Entity
@Table(name = "t_role")
@DynamicInsert
@DynamicUpdate
public class Role extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PID")
	private Role role;// 上级角色
	
	@Column(name = "NAME", nullable = false, length = 100)
	private String name;// 名称
	
	@Column(name = "REMARK", length = 200)
	private String remark;// 备注
	
	@Column(name = "SEQ",length=10)
	private Integer seq;// 排序

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	private Set<Role> roles = new HashSet<Role>(0); // 子角色
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_role_resource", joinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false) })
	private Set<Resource> resources = new HashSet<Resource>(0);// 这个角色有哪些资源
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="roles")
//	@JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) })
	private Set<User> users = new HashSet<User>(0);// 哪些用户有这个角色
	
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
