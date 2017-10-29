package com.wangfanpinche.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import com.wangfanpinche.dto.OwnerApprove.OwnStatusEnum;
import com.wangfanpinche.dto.OwnerOrder.Status;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.utils.validation.anno.Future;

public class OwnerOrderVo {
	
	@NotEmpty(message = "车单id不能为空", groups = PassengerReserve.class)
	private String id;
	
	@NotNull(message="起点经度不能为空", groups={Publish.class, PassengerReserve.class})
	private BigDecimal fromLng;// 起点经度
	
	@NotNull(message="起点纬度不能为空", groups={Publish.class, PassengerReserve.class})
	private BigDecimal fromLat;// 起点纬度
	
	@NotNull(message="终点经度不能为空", groups={Publish.class, PassengerReserve.class})
	private BigDecimal toLng;// 终点经度
	
	@NotNull(message="终点纬度不能为空", groups={Publish.class, PassengerReserve.class})
	private BigDecimal toLat;// 终点纬度	
	
	private String fromCity;//起点城市
	
	private String fromSematicDescription;//起点地点描述
	
	private String toCity;//终点城市
	
	private String toSematicDescription;//终点地点描述

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@NotNull(message="出行时间不能为空", groups=Publish.class)
	@Future(message="出行时间不能小于当前时间", groups=Publish.class)
	private LocalDateTime departDateTime;//出行时间
	
	private List<String> carTags;//发布车单的标签
	
	@NotNull(message="座位数不能为空", groups={Publish.class, PassengerReserve.class})
	@Range(min=1,max=4,message="座位数在1-4之间", groups=Publish.class)
	private Integer seat;//总座位数
	
	private Integer currentSeat;//当前座位数
	
	@NotNull(message="车费不能为空", groups=Publish.class)
	@DecimalMin(value="1",message="车费不能小于1", groups=Publish.class)
	private BigDecimal fare;//车费 人/次
	
	private Boolean honesty;//是否诚信必发
	
	private Status status;//状态
	
	private String via;//途径
	
	private String description;//备注

	private String userId;	
	
	private String userIcon;//用户头像
	
	private String mobilePhone;//手机号
	
	private String username;//用户姓名
	
	private StatusEnum userstatus;//用户实名认证
	
	private OwnStatusEnum ownerstatus;//车主认证
	
	private String carId;//车辆id
	
	private String carColor;//车辆颜色
	
	private String carNumber;//车牌号
	
	private String brandSystem;//车品牌和车系
	
	private String carmodel;//车型
	
	private String homeAddress;//家乡地址
	
	private Long total;//拼车次数
	
	private BigDecimal goinLng;//上车经度
	
	private BigDecimal goinLat;//上车纬度
	
	private BigDecimal gooutLng;//下车经度
	
	private BigDecimal gooutLat;//下车纬度
	
	private String content;// 评价内容
	
	@NotNull(message="描述相符不能为空",groups=UserRated.class)
	@DecimalMin(value="0.1",message="描述相符评分最小值不能小于0.1",groups=UserRated.class)
	@DecimalMax(value="5.0",message="描述相符评分最大值不能大于5.0",groups=UserRated.class)
	private Double descriptions;// 描述相符
	
	@NotNull(message="诚信守时不能为空",groups=UserRated.class)
	@DecimalMin(value="0.1",message="诚信守时评分最小值不能小于0.1",groups=UserRated.class)
	@DecimalMax(value="5.0",message="诚信守时评分最大值不能大于5.0",groups=UserRated.class)
	private Double trustworthy;// 信用
	
	@NotNull(message="服务态度不能为空",groups=UserRated.class)
	@DecimalMin(value="0.1",message="服务态度评分最小值不能小于0.1",groups=UserRated.class)
	@DecimalMax(value="5.0",message="服务态度评分最大值不能大于5.0",groups=UserRated.class)
	private Double service;// 服务态度
	
	@NotEmpty(message="评价给谁不能为空",groups={UserRated.class, OwnerRated.class})
	private String toUserId;//评价谁的id
	
	private List<String> tags;//车主给乘客贴的标签
	
	private String publishId;//乘客预定的乘客Id
	
	private String receiveId;//乘客上车的乘客Id
	
	private String departId;//乘客下车的乘客Id
	
	private Long orderNumber;//订单号
	
	private Integer passengerSeat;//乘客所占有的座位数
	
	private BigDecimal allfare;//车单总金额
	
	private String ip;
	
	private String passengerOrderId;//乘客车单id
	
	public interface Publish {}
	public interface PassengerReserve {}
	public interface UserRated {}
	public interface OwnerRated {}
	
	
	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, String via, String description) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.via = via;
		this.description = description;
	}
	
	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, String via, String description, BigDecimal fromLng, BigDecimal fromLat, BigDecimal toLng, BigDecimal toLat) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.via = via;
		this.description = description;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
		this.toLng = toLng;
		this.toLat = toLat;
	}


	public OwnerOrderVo(String id, Status status) {
		super();
		this.id = id;
		this.status = status;
	}


	public OwnerOrderVo(Integer passengerSeat, BigDecimal allfare) {
		super();
		this.passengerSeat = passengerSeat;
		this.allfare = allfare;
	}

	public OwnerOrderVo(String id, Integer currentSeat, Status status) {
		super();
		this.id = id;
		this.currentSeat = currentSeat;
		this.status = status;
	}


	public OwnerOrderVo(String id, Integer currentSeat, BigDecimal fare, Status status) {
		super();
		this.id = id;
		this.currentSeat = currentSeat;
		this.fare = fare;
		this.status = status;
	}
	
	
	public OwnerOrderVo(String id, Integer currentSeat, BigDecimal fare, Status status, String userId) {
		super();
		this.id = id;
		this.currentSeat = currentSeat;
		this.fare = fare;
		this.status = status;
		this.userId = userId;
	}


	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, BigDecimal fare, Boolean honesty, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.fare = fare;
		this.honesty = honesty;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
	}
	
	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, BigDecimal fare, Boolean honesty, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, String brandSystem) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.fare = fare;
		this.honesty = honesty;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.brandSystem = brandSystem;
	}
	
	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, BigDecimal fare, Boolean honesty, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, String brandSystem, BigDecimal fromLng, BigDecimal fromLat) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.fare = fare;
		this.honesty = honesty;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.brandSystem = brandSystem;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
	}

	public OwnerOrderVo(Double descriptions, Double trustworthy, Double service) {
		super();
		this.descriptions = descriptions;
		this.trustworthy = trustworthy;
		this.service = service;
	}

	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, Status status) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.status = status;
	}

	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
	}

	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer currentSeat, BigDecimal fare, Boolean honesty, String description, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, String carId, String carColor, String carNumber, String brandSystem) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.currentSeat = currentSeat;
		this.fare = fare;
		this.honesty = honesty;
		this.description = description;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.carId = carId;
		this.carColor = carColor;
		this.carNumber = carNumber;
		this.brandSystem = brandSystem;
	}


	public OwnerOrderVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Status status) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.status = status;
	}


	public OwnerOrderVo() {
		super();
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

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public Boolean getHonesty() {
		return honesty;
	}

	public void setHonesty(Boolean honesty) {
		this.honesty = honesty;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getGoinLng() {
		return goinLng;
	}


	public void setGoinLng(BigDecimal goinLng) {
		this.goinLng = goinLng;
	}


	public BigDecimal getGoinLat() {
		return goinLat;
	}


	public void setGoinLat(BigDecimal goinLat) {
		this.goinLat = goinLat;
	}


	public BigDecimal getGooutLng() {
		return gooutLng;
	}


	public void setGooutLng(BigDecimal gooutLng) {
		this.gooutLng = gooutLng;
	}


	public BigDecimal getGooutLat() {
		return gooutLat;
	}


	public void setGooutLat(BigDecimal gooutLat) {
		this.gooutLat = gooutLat;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
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


	public String getToUserId() {
		return toUserId;
	}


	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
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


	public List<String> getTags() {
		return tags;
	}


	public void setTags(List<String> tags) {
		this.tags = tags;
	}


	public OwnStatusEnum getOwnerstatus() {
		return ownerstatus;
	}


	public void setOwnerstatus(OwnStatusEnum ownerstatus) {
		this.ownerstatus = ownerstatus;
	}


	public String getCarId() {
		return carId;
	}


	public void setCarId(String carId) {
		this.carId = carId;
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


	public String getCarmodel() {
		return carmodel;
	}

	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}


	public List<String> getCarTags() {
		return carTags;
	}


	public void setCarTags(List<String> carTags) {
		this.carTags = carTags;
	}


	public String getPublishId() {
		return publishId;
	}


	public void setPublishId(String publishId) {
		this.publishId = publishId;
	}


	public String getReceiveId() {
		return receiveId;
	}


	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}


	public String getDepartId() {
		return departId;
	}


	public void setDepartId(String departId) {
		this.departId = departId;
	}


	public Long getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}


	public Integer getPassengerSeat() {
		return passengerSeat;
	}


	public void setPassengerSeat(Integer passengerSeat) {
		this.passengerSeat = passengerSeat;
	}


	public BigDecimal getAllfare() {
		return allfare;
	}


	public void setAllfare(BigDecimal allfare) {
		this.allfare = allfare;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getBrandSystem() {
		return brandSystem;
	}


	public void setBrandSystem(String brandSystem) {
		this.brandSystem = brandSystem;
	}

	public String getPassengerOrderId() {
		return passengerOrderId;
	}

	public void setPassengerOrderId(String passengerOrderId) {
		this.passengerOrderId = passengerOrderId;
	}


}
