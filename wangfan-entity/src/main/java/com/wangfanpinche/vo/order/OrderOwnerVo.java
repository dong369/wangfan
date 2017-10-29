package com.wangfanpinche.vo.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.wangfanpinche.dto.OwnerApprove.OwnStatusEnum;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.dto.order.OrderOwner.OrderOwnerStatus;

public class OrderOwnerVo {
	
	private String id;//车单id
	
	private BigDecimal singleFare;// 车费 人/次

	private BigDecimal resultFare;// 车费 总费用
	
	private BigDecimal fromLng;// 起点经度
	
	private BigDecimal fromLat;// 起点纬度
	
	private BigDecimal toLng;// 终点经度
	
	private BigDecimal toLat;// 终点纬度
	
	private String fromCity;//起点城市
	
	private String fromSematicDescription;//起点地点描述
	
	private String toCity;//终点城市
	
	private String toSematicDescription;//终点地点描述
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime departDateTime;// 出行时间

	private Integer seat;// 总座位数

	private Integer currentSeat;// 当前座位数
	
	private Boolean honesty;// 是否诚信必发
	
	private Long honestyOrderNumber;// 诚信必发订单号
	
	private String via;// 途径

	private String description;// 备注
	
	private OrderOwnerStatus status;//状态
	
	private String userId;
	
	private String userIcon;//用户头像
	
	private String mobilePhone;//手机号
	
	private String username;//用户姓名
	
	private StatusEnum userstatus;//用户实名认证
	
	private OwnStatusEnum ownerstatus;//车主认证
	
	private String homeAddress;//家乡地址
	
	private Long total;//拼车次数
	
	private String carBrandAndSystem;// 车型和车系
	
	private String carColor;// 车颜色
	
	private String carNumber;
	
	private Double descriptions;// 描述相符
	
	private Double trustworthy;// 信用
	
	private Double service;// 服务态度
	
	private List<String> tags;//车主给乘客贴的标签
	
	private String toUserId;//评价给谁的Id
	
	private String ip;
	
	private String orderPassengerId;//乘客车单Id
	
	private List<OrderPassengerVo> passengers = new ArrayList<>();//预定乘客列表
	
	private List<OrderPassengerVo> runPassengers = new ArrayList<>();//进行中的乘客列表
	
	private Integer regTotal;//有几个乘客申请家人您的车单
	
	
	//个人中心----车主行程记录        快捷进入---未出行的车单         快捷进入---进行中的车单
	public OrderOwnerVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, OrderOwnerStatus status) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.status = status;
	}	
		
	//首页地图-附近的车主          首页地图-开始和结束位置寻找车单,推荐
	public OrderOwnerVo(String id, BigDecimal singleFare, BigDecimal fromLng, BigDecimal fromLat, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, Boolean honesty, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, String carBrandAndSystem) {
		super();
		this.id = id;
		this.singleFare = singleFare;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.honesty = honesty;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.carBrandAndSystem = carBrandAndSystem;
	}
	
	//车主抢单列表-同意的车主车单信息
	public OrderOwnerVo(String id, BigDecimal singleFare, BigDecimal fromLng, BigDecimal fromLat, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, Boolean honesty, OrderOwnerStatus status,String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, String carBrandAndSystem) {
		super();
		this.id = id;
		this.singleFare = singleFare;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.honesty = honesty;
		this.status = status;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.carBrandAndSystem = carBrandAndSystem;
	}

	//车主车单详情
	public OrderOwnerVo(String id, BigDecimal fromLng, BigDecimal fromLat, BigDecimal toLng, BigDecimal toLat, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat,BigDecimal singleFare, String via, String description) {
		super();
		this.id = id;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
		this.toLng = toLng;
		this.toLat = toLat;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.singleFare = singleFare;
		this.via = via;
		this.description = description;
	}
	
	//车主信息
	public OrderOwnerVo(String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, OwnStatusEnum ownerstatus, String homeAddress, String carBrandAndSystem, String carColor, String carNumber) {
		super();
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.ownerstatus = ownerstatus;
		this.homeAddress = homeAddress;		
		this.carBrandAndSystem = carBrandAndSystem;
		this.carColor = carColor;
		this.carNumber = carNumber;
	}

	//车主的平均分
	public OrderOwnerVo(Double descriptions, Double trustworthy, Double service) {
		super();
		this.descriptions = descriptions;
		this.trustworthy = trustworthy;
		this.service = service;
	}

	//乘客列表
	public OrderOwnerVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, OrderOwnerStatus status, String userId) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.status = status;
		this.userId = userId;
	}
	
	public OrderOwnerVo() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public BigDecimal getSingleFare() {
		return singleFare;
	}

	public void setSingleFare(BigDecimal singleFare) {
		this.singleFare = singleFare;
	}

	public BigDecimal getResultFare() {
		return resultFare;
	}

	public void setResultFare(BigDecimal resultFare) {
		this.resultFare = resultFare;
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

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public Integer getCurrentSeat() {
		return currentSeat;
	}

	public void setCurrentSeat(Integer currentSeat) {
		this.currentSeat = currentSeat;
	}

	public Boolean getHonesty() {
		return honesty;
	}

	public void setHonesty(Boolean honesty) {
		this.honesty = honesty;
	}

	public Long getHonestyOrderNumber() {
		return honestyOrderNumber;
	}

	public void setHonestyOrderNumber(Long honestyOrderNumber) {
		this.honestyOrderNumber = honestyOrderNumber;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OrderOwnerStatus getStatus() {
		return status;
	}

	public void setStatus(OrderOwnerStatus status) {
		this.status = status;
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

	public StatusEnum getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(StatusEnum userstatus) {
		this.userstatus = userstatus;
	}

	public OwnStatusEnum getOwnerstatus() {
		return ownerstatus;
	}

	public void setOwnerstatus(OwnStatusEnum ownerstatus) {
		this.ownerstatus = ownerstatus;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getCarBrandAndSystem() {
		return carBrandAndSystem;
	}

	public void setCarBrandAndSystem(String carBrandAndSystem) {
		this.carBrandAndSystem = carBrandAndSystem;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public Double getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Double descriptions) {
		this.descriptions = descriptions;
	}

	public Double getTrustworthy() {
		return trustworthy;
	}

	public void setTrustworthy(Double trustworthy) {
		this.trustworthy = trustworthy;
	}

	public Double getService() {
		return service;
	}

	public void setService(Double service) {
		this.service = service;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOrderPassengerId() {
		return orderPassengerId;
	}

	public void setOrderPassengerId(String orderPassengerId) {
		this.orderPassengerId = orderPassengerId;
	}

	public List<OrderPassengerVo> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<OrderPassengerVo> passengers) {
		this.passengers = passengers;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Integer getRegTotal() {
		return regTotal;
	}

	public void setRegTotal(Integer regTotal) {
		this.regTotal = regTotal;
	}

	public List<OrderPassengerVo> getRunPassengers() {
		return runPassengers;
	}

	public void setRunPassengers(List<OrderPassengerVo> runPassengers) {
		this.runPassengers = runPassengers;
	}

}
