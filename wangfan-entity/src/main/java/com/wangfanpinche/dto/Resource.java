package com.wangfanpinche.dto;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 资源
 *
 */
@Entity
@Table(name = "t_resource")
@DynamicInsert
@DynamicUpdate
public class Resource extends BaseEntity {

	// 枚举
	@Enumerated(EnumType.ORDINAL)
	private TypeEnum type;// 类型 ：菜单，功能

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PID")
	private Resource resource;// 所属资源

	@Column(name = "NAME", nullable = false, length = 100)
	private String name;// 名称

	@Column(name = "REMARK", length = 200)
	private String remark;// 备注

	@Column(name = "SEQ", length = 10)
	private Integer seq;// 排序

	@Column(name = "URL", length = 200)
	private String url;// 地址

	@Column(name = "ICON", length = 100)
	private String icon;// 图标

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "resources")
	private Set<Role> roles = new HashSet<Role>(0);// 所属角色

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resource")
	private Set<Resource> resources = new HashSet<Resource>(0);// 下级资源

	public static enum TypeEnum {
		菜单, 功能;
	}

	public Resource() {
	}

	public Resource(String id) {
		this.setId(id);
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

}
