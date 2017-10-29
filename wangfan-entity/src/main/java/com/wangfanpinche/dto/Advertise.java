package com.wangfanpinche.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.agent.Agent;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 广告
 *
 */
@Entity
@Table(name = "t_advertise")
@DynamicInsert
@DynamicUpdate
public class Advertise extends BaseEntity {
	
	private String title;//标题
	
	private String descript;//描述

	@Column(length = 200)
	private String picUrl;// 广告路径

	@Enumerated
	private AdLocation adLocation;// 广告类型

	private String h5url;// h5的链接地址

	@ManyToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;// 运营商
	
	private Integer second;//显示几秒

	public static enum AdLocation {

		/**
		 * 登陆广告
		 */
		LOGIN,

		/**
		 * 退出广告
		 */
		LOGOUT
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public AdLocation getAdLocation() {
		return adLocation;
	}

	public void setAdLocation(AdLocation adLocation) {
		this.adLocation = adLocation;
	}

	public String getH5url() {
		return h5url;
	}

	public void setH5url(String h5url) {
		this.h5url = h5url;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

}
