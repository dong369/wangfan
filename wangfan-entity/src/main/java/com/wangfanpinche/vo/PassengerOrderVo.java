package com.wangfanpinche.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.wangfanpinche.dto.OwnerOrder.Status;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.utils.validation.anno.Future;

public class PassengerOrderVo {
	
	private String id;

	@NotNull(message="起点经度不能为空", groups=Publish.class)
	private BigDecimal fromLng;
	
	@NotNull(message="起点纬度不能为空", groups=Publish.class)
	private BigDecimal fromLat;
	
	@NotNull(message="终点经度不能为空", groups=Publish.class)
	private BigDecimal toLng;
	
	@NotNull(message="终点纬度不能为空", groups=Publish.class)
	private BigDecimal toLat;
	
	private String fromCity;//起点城市
	
	private String fromSematicDescription;//起点地点描述
	
	private String toCity;//终点城市
	
	private String toSematicDescription;//终点地点描述

	@NotNull(message="座位数不能为空", groups=Publish.class)
	@Range(min=1,max=2,message="座位数在1-2之间", groups=Publish.class)
	private Integer seat; // 座位数

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")	
	@NotNull(message="出行时间不能为空", groups=Publish.class)
	@Future(message="出行时间不能小于当前时间", groups=Publish.class)
	private LocalDateTime publishDateTime;// 发布时间
	
	private Status status;//乘客车单状态
	
	private com.wangfanpinche.dto.OwnerOrder.Status ownerStatus;//车主车单状态

	private String description;// 描述

	@NotNull(message="车费不能为空", groups=Publish.class)
	@DecimalMin(value="1",message="车费不能小于1", groups=Publish.class)
	private BigDecimal amount;// 金额
	
	private String ownerOrderId;
	
	private String userId;
	
	private String userIcon;//用户头像
	
	private String mobilePhone;//手机号
	
	private String username;//用户姓名
	
	private String profession;//职业
	
	private StatusEnum userstatus;//用户实名认证
	
	private Long total;//拼车次数
	
	private List<String> tags;//车主给乘客贴的标签
	
	private List<OwnerOrderVo> ownerList;//抢单的车主
	
	private OwnerOrderVo approveOwner;//同意的车主
	
	private Long orderNumber;//订单号
	
	private String ip;//ip

	public interface Publish {}
	
	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, Integer seat, LocalDateTime publishDateTime, String description, BigDecimal amount, String userId, String userIcon, String mobilePhone, String username, String profession, StatusEnum userstatus, BigDecimal fromLng, BigDecimal fromLat, BigDecimal toLng, BigDecimal toLat) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.seat = seat;
		this.publishDateTime = publishDateTime;
		this.description = description;
		this.amount = amount;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.profession = profession;
		this.userstatus = userstatus;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
		this.toLng = toLng;
		this.toLat = toLat;
	}
	
	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, Integer seat, LocalDateTime publishDateTime, String description, BigDecimal amount, String userId, String userIcon, String mobilePhone, String username, String profession, StatusEnum userstatus) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.seat = seat;
		this.publishDateTime = publishDateTime;
		this.description = description;
		this.amount = amount;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.profession = profession;
		this.userstatus = userstatus;
	}
	

	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, Integer seat, LocalDateTime publishDateTime, BigDecimal amount, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.seat = seat;
		this.publishDateTime = publishDateTime;
		this.amount = amount;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
	}
	
	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, Integer seat, LocalDateTime publishDateTime, BigDecimal amount, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, BigDecimal fromLng, BigDecimal fromLat) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.seat = seat;
		this.publishDateTime = publishDateTime;
		this.amount = amount;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
	}

	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime publishDateTime, Status status, com.wangfanpinche.dto.OwnerOrder.Status ownerStatus, String ownerOrderId) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.publishDateTime = publishDateTime;
		this.status = status;
		this.ownerStatus = ownerStatus;
		this.ownerOrderId = ownerOrderId;
	}

	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, Integer seat, LocalDateTime publishDateTime, String description, BigDecimal amount, Status status) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.seat = seat;
		this.publishDateTime = publishDateTime;
		this.description = description;
		this.amount = amount;
		this.status = status;
	}
	
	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, Integer seat, LocalDateTime publishDateTime, String description, BigDecimal amount, Status status, String ownerOrderId) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.seat = seat;
		this.publishDateTime = publishDateTime;
		this.description = description;
		this.amount = amount;
		this.status = status;
		this.ownerOrderId = ownerOrderId;
	}

	
	public PassengerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, Integer seat, LocalDateTime publishDateTime) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.seat = seat;
		this.publishDateTime = publishDateTime;
	}

	public PassengerOrderVo() {
		super();
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public LocalDateTime getPublishDateTime() {
		return publishDateTime;
	}

	public void setPublishDateTime(LocalDateTime publishDateTime) {
		this.publishDateTime = publishDateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFromLng() {
		return fromLng;
	}

	public void setFromLng(BigDecimal fromLng) {
		this.fromLng = fromLng;
	}

	public BigDecimal getFromLat() {
		return fromLat;
	}

	public void setFromLat(BigDecimal fromLat) {
		this.fromLat = fromLat;
	}

	public BigDecimal getToLng() {
		return toLng;
	}

	public void setToLng(BigDecimal toLng) {
		this.toLng = toLng;
	}

	public BigDecimal getToLat() {
		return toLat;
	}

	public void setToLat(BigDecimal toLat) {
		this.toLat = toLat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerOrderId() {
		return ownerOrderId;
	}

	public void setOwnerOrderId(String ownerOrderId) {
		this.ownerOrderId = ownerOrderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<OwnerOrderVo> getOwnerList() {
		return ownerList;
	}

	public void setOwnerList(List<OwnerOrderVo> ownerList) {
		this.ownerList = ownerList;
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

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public StatusEnum getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(StatusEnum userstatus) {
		this.userstatus = userstatus;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public com.wangfanpinche.dto.OwnerOrder.Status getOwnerStatus() {
		return ownerStatus;
	}


	public void setOwnerStatus(com.wangfanpinche.dto.OwnerOrder.Status ownerStatus) {
		this.ownerStatus = ownerStatus;
	}


	public Long getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}

	public OwnerOrderVo getApproveOwner() {
		return approveOwner;
	}

	public void setApproveOwner(OwnerOrderVo approveOwner) {
		this.approveOwner = approveOwner;
	}

}
