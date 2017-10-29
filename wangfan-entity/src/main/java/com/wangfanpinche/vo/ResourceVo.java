package com.wangfanpinche.vo;

import java.util.ArrayList;
import java.util.List;

import com.wangfanpinche.dto.Resource.TypeEnum;

/**
 * 资源
 *
 */
public class ResourceVo{
	
	private String id;
	//枚举
	private TypeEnum type;// 类型  ：菜单，功能	
	private String resourceId;// 所属资源Id
	private String resourceName;// 所属资源名称
	private String name;// 资源名称
	private String remark;// 备注
	private Integer seq;// 排序
	private String url;// 地址
	private String icon;// 图标
	
	private List<ResourceVo> resources = new ArrayList<>();
	
	public ResourceVo() {
	}	
	
	public ResourceVo(String id) {
		super();
		this.id = id;
	}

	public ResourceVo(String id, String resourceId, String resourceName,String name, Integer seq, String url, String icon) {
		super();
		this.id = id;
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.name = name;
		this.seq = seq;
		this.url = url;
		this.icon = icon;
	}


	public ResourceVo(String id, TypeEnum type, String resourceId, String resourceName, String name, String remark, Integer seq, String url, String icon) {
		super();
		this.id = id;
		this.type = type;
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.name = name;
		this.remark = remark;
		this.seq = seq;
		this.url = url;
		this.icon = icon;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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


	public List<ResourceVo> getResources() {
		return resources;
	}


	public void setResources(List<ResourceVo> resources) {
		this.resources = resources;
	}	
	
}
