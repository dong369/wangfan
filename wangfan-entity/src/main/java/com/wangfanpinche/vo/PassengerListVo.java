package com.wangfanpinche.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.wangfanpinche.dto.OwnerOrder.Status;

public class PassengerListVo {
	
	private String id;//车单id
	
	private String fromCity;//起点城市
	
	private String fromSematicDescription;//起点地点描述
	
	private String toCity;//终点城市
	
	private String toSematicDescription;//终点地点描述

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime departDateTime;//出行时间
	
	private Integer currentSeat;//当前座位数
	
	private Status status;//车单状态
	
	private List<PassengerListPublishVo> publish;//发布车单中的乘客
	
	private List<PassengerListReceiveVo> receive;//接的乘客
	
	private List<PassengerListDepartVo> depart;//下车乘客

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<PassengerListPublishVo> getPublish() {
		return publish;
	}

	public void setPublish(List<PassengerListPublishVo> publish) {
		this.publish = publish;
	}

	public List<PassengerListReceiveVo> getReceive() {
		return receive;
	}

	public void setReceive(List<PassengerListReceiveVo> receive) {
		this.receive = receive;
	}

	public List<PassengerListDepartVo> getDepart() {
		return depart;
	}

	public void setDepart(List<PassengerListDepartVo> depart) {
		this.depart = depart;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getFromSematicDescription() {
		return fromSematicDescription;
	}

	public void setFromSematicDescription(String fromSematicDescription) {
		this.fromSematicDescription = fromSematicDescription;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getToSematicDescription() {
		return toSematicDescription;
	}

	public void setToSematicDescription(String toSematicDescription) {
		this.toSematicDescription = toSematicDescription;
	}

	public LocalDateTime getDepartDateTime() {
		return departDateTime;
	}

	public void setDepartDateTime(LocalDateTime departDateTime) {
		this.departDateTime = departDateTime;
	}

	public Integer getCurrentSeat() {
		return currentSeat;
	}

	public void setCurrentSeat(Integer currentSeat) {
		this.currentSeat = currentSeat;
	}
	
	
	

}
