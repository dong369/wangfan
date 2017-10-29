package com.wangfanpinche.vo.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.dto.order.OrderPassenger.OrderPassengerStatus;
import com.wangfanpinche.dto.order.OrderPassenger.OrderPassengerType;
import com.wangfanpinche.utils.validation.anno.Future;

public class OrderPassengerVo {

	private String id;//乘客车单id
	
	private String orderOwnerId;//车主车单id
	
	private String userId;//
	
	private String userIcon;//用户头像
	
	private String mobilePhone;//手机号
	
	private String username;//用户姓名
	
	private String profession;//职业
	
	private StatusEnum userstatus;//用户实名认证
	
	private String homeAddress;//家乡
	
	@NotNull(message="起点经度不能为空", groups=Publish.class)
	private BigDecimal fromLng;// 起点经度
	
	@NotNull(message="起点纬度不能为空", groups=Publish.class)
	private BigDecimal fromLat;// 起点纬度
	
	@NotNull(message="终点经度不能为空", groups=Publish.class)
	private BigDecimal toLng;// 终点经度
	
	@NotNull(message="终点纬度不能为空", groups=Publish.class)
	private BigDecimal toLat;// 终点纬度

	private String fromCity;// 起点城市

	private String fromSematicDescription;// 起点地点描述

	private String toCity;// 终点城市

	private String toSematicDescription;// 终点地点描述

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@NotNull(message="出行时间不能为空", groups=Publish.class)
	@Future(message="出行时间不能小于当前时间", groups=Publish.class)
	private LocalDateTime departDateTime;// 出行时间
	
	@NotNull(message="座位数不能为空", groups=Publish.class)
	@Range(min=1,max=2,message="座位数在1-2之间", groups=Publish.class)
	private Integer seat;// 总座位数
	
	@NotNull(message="车费不能为空", groups=Publish.class)
	@DecimalMin(value="1",message="车费不能小于1", groups=Publish.class)
	private BigDecimal singleFare;// 车费 人/次
	
	private BigDecimal resultFare;// 车费 总费用
	
	private BigDecimal realFare;//实际支付钱数

	private OrderPassengerStatus status;
	
	private String descript;// 备注
	
	private OrderOwnerVo approveOwner;//同意的车主
	
	private List<OrderOwnerVo> owners = new ArrayList<>();//抢单车主
	
	private Boolean closedStatus;// 关闭状态

	private LocalDateTime closedDateTime;// 关闭时间

	private String closedDescirpt;// 关闭原因
	
	private OrderPassengerType passengerType; // 坐车类型
	
	private Boolean payStatus;// 支付状态 待付款、已付款

	private LocalDateTime payDateTime;// 支付时间

	private Long payOrderNumber;// 支付订单号
	
	private String ip;//ip
	
	private Boolean approveStatus;// 车主是否同意

	private LocalDateTime approveDateTime;// 同意时间
	
	private Boolean disapproveStatus;// 是否拒绝

	private LocalDateTime disapproveDateTime;//拒绝时间
	
	private Boolean goinStatus;// 是否上车

	private LocalDateTime goinDateTime;// 上车时间
	
	private BigDecimal goinLng;//上车经度
	
	private BigDecimal goinLat;//上车纬度
	
	private String goinCity;// 上车城市

	private String goinSematicDescription;// 上车地点描述
	
	private Boolean gooutStatus;// 是否下车

	private LocalDateTime gooutDateTime;// 下车时间
	
	private BigDecimal gooutLng;//下车经度
	
	private BigDecimal gooutLat;//下车纬度
	
	private String gooutCity;// 下车城市

	private String gooutSematicDescription;// 下车地点描述
	
	@NotNull(message="描述相符不能为空",groups=PassToOwnerevaluate.class)
	@DecimalMin(value="0.1",message="描述相符评分最小值不能小于0.1",groups=PassToOwnerevaluate.class)
	@DecimalMax(value="5.0",message="描述相符评分最大值不能大于5.0",groups=PassToOwnerevaluate.class)
	private Double descriptions;// 描述相符
	
	@NotNull(message="诚信守时不能为空",groups=PassToOwnerevaluate.class)
	@DecimalMin(value="0.1",message="诚信守时评分最小值不能小于0.1",groups=PassToOwnerevaluate.class)
	@DecimalMax(value="5.0",message="诚信守时评分最大值不能大于5.0",groups=PassToOwnerevaluate.class)
	private Double trustworthy;// 信用
	
	@NotNull(message="服务态度不能为空",groups=PassToOwnerevaluate.class)
	@DecimalMin(value="0.1",message="服务态度评分最小值不能小于0.1",groups=PassToOwnerevaluate.class)
	@DecimalMax(value="5.0",message="服务态度评分最大值不能大于5.0",groups=PassToOwnerevaluate.class)
	private Double service;// 服务态度
	
	private String content;// 评价内容
	
	private String toUserId;//评价给谁的Id
	
	private Long total;//拼车次数
	
	private List<String> tags  = new ArrayList<>();//车主给乘客贴的标签
	
	private Boolean toEvaluateModify = false;//给司机的评价是否评价过
	
	private Boolean fromEvaluateModify = false;//给乘客的评价是否评价过
	
	private Integer ownerTotal;//抢单车主的数量
	
	public OrderPassengerVo(){}
	
	
	public interface Publish {}
	public interface PassToOwnerevaluate {}

	//车主出发接乘客推送sql
	public OrderPassengerVo(String id, String userId) {
		super();
		this.id = id;
		this.userId = userId;
	}

	//个人中心乘客行程记录      //快捷进入未出行的车单       // 快捷进入正在进行中的车单      
	public OrderPassengerVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, OrderPassengerStatus status) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.status = status;
	}
	
	//首页--附近乘客车单         //  首页地图-开始和结束位置寻找车单,推荐
	public OrderPassengerVo(String id, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, BigDecimal fromLng, BigDecimal fromLat, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer seat, BigDecimal singleFare, OrderPassengerStatus status) {
		super();
		this.id = id;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.seat = seat;
		this.singleFare = singleFare;
		this.status = status;
	}
	
	//乘客车单详情
	public OrderPassengerVo(String id, String userId, String userIcon, String mobilePhone, String username, String profession, StatusEnum userstatus, BigDecimal fromLng, BigDecimal fromLat, BigDecimal toLng, BigDecimal toLat, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer seat, BigDecimal singleFare, String descript) {
		super();
		this.id = id;
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
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.seat = seat;
		this.singleFare = singleFare;
		this.descript = descript;		
	}
	
	//乘客信息
	public OrderPassengerVo(String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, String homeAddress) {
		super();
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.homeAddress = homeAddress;		
	}
	
	//乘客查看车主抢单列表
	public OrderPassengerVo(String id, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer seat, BigDecimal singleFare, String descript, OrderPassengerStatus status) {
		super();
		this.id = id;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.seat = seat;
		this.singleFare = singleFare;
		this.descript = descript;
		this.status = status;
	}
	
	//车主查看乘客列表 -进行中的乘客          // 乘客查看自己车单
	public OrderPassengerVo(String id, String userId, String userIcon, String mobilePhone, String username, StatusEnum userstatus, BigDecimal fromLng, BigDecimal fromLat, String fromCity, String fromSematicDescription, String toCity, String toSematicDescription, LocalDateTime departDateTime, Integer seat, BigDecimal singleFare, OrderPassengerStatus status, String descript, OrderPassengerType passengerType, Boolean closedStatus, LocalDateTime closedDateTime, String closedDescirpt, Boolean payStatus, LocalDateTime payDateTime, Boolean approveStatus, LocalDateTime approveDateTime, Boolean disapproveStatus, LocalDateTime disapproveDateTime, Boolean goinStatus, LocalDateTime goinDateTime, BigDecimal goinLng, BigDecimal goinLat, String goinCity, String goinSematicDescription, Boolean gooutStatus, LocalDateTime gooutDateTime, BigDecimal gooutLng, BigDecimal gooutLat, String gooutCity, String gooutSematicDescription, Boolean toEvaluateModify, Boolean fromEvaluateModify) {
		super();
		this.id = id;
		this.userId = userId;
		this.userIcon = userIcon;
		this.mobilePhone = mobilePhone;
		this.username = username;
		this.userstatus = userstatus;
		this.fromLng = fromLng;
		this.fromLat = fromLat;
		this.fromCity = fromCity;
		this.fromSematicDescription = fromSematicDescription;
		this.toCity = toCity;
		this.toSematicDescription = toSematicDescription;
		this.departDateTime = departDateTime;
		this.seat = seat;
		this.singleFare = singleFare;
		this.status = status;
		this.descript = descript;
		this.passengerType = passengerType;
		this.closedStatus = closedStatus;
		this.closedDateTime = closedDateTime;
		this.closedDescirpt = closedDescirpt;
		this.payStatus = payStatus;
		this.payDateTime = payDateTime;
		this.approveStatus = approveStatus;
		this.approveDateTime = approveDateTime;
		this.disapproveStatus = disapproveStatus;
		this.disapproveDateTime = disapproveDateTime;
		this.goinStatus = goinStatus;
		this.goinDateTime = goinDateTime;
		this.goinLng = goinLng;
		this.goinLat = goinLat;
		this.goinCity = goinCity;
		this.goinSematicDescription = goinSematicDescription;
		this.gooutStatus = gooutStatus;
		this.gooutDateTime = gooutDateTime;
		this.gooutLng = gooutLng;
		this.gooutLat = gooutLat;
		this.gooutCity = gooutCity;
		this.gooutSematicDescription = gooutSematicDescription;
		this.toEvaluateModify = toEvaluateModify;
		this.fromEvaluateModify = fromEvaluateModify;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderOwnerId() {
		return orderOwnerId;
	}

	public void setOrderOwnerId(String orderOwnerId) {
		this.orderOwnerId = orderOwnerId;
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

	public OrderPassengerStatus getStatus() {
		return status;
	}

	public void setStatus(OrderPassengerStatus status) {
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


	public Integer getSeat() {
		return seat;
	}


	public void setSeat(Integer seat) {
		this.seat = seat;
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


	public BigDecimal getRealFare() {
		return realFare;
	}


	public void setRealFare(BigDecimal realFare) {
		this.realFare = realFare;
	}


	public String getDescript() {
		return descript;
	}


	public void setDescript(String descript) {
		this.descript = descript;
	}


	public List<OrderOwnerVo> getOwners() {
		return owners;
	}


	public void setOwners(List<OrderOwnerVo> owners) {
		this.owners = owners;
	}


	public Boolean getClosedStatus() {
		return closedStatus;
	}


	public void setClosedStatus(Boolean closedStatus) {
		this.closedStatus = closedStatus;
	}


	public LocalDateTime getClosedDateTime() {
		return closedDateTime;
	}


	public void setClosedDateTime(LocalDateTime closedDateTime) {
		this.closedDateTime = closedDateTime;
	}


	public String getClosedDescirpt() {
		return closedDescirpt;
	}


	public void setClosedDescirpt(String closedDescirpt) {
		this.closedDescirpt = closedDescirpt;
	}


	public OrderPassengerType getPassengerType() {
		return passengerType;
	}


	public void setPassengerType(OrderPassengerType passengerType) {
		this.passengerType = passengerType;
	}


	public Boolean getPayStatus() {
		return payStatus;
	}


	public void setPayStatus(Boolean payStatus) {
		this.payStatus = payStatus;
	}


	public LocalDateTime getPayDateTime() {
		return payDateTime;
	}


	public void setPayDateTime(LocalDateTime payDateTime) {
		this.payDateTime = payDateTime;
	}


	public Long getPayOrderNumber() {
		return payOrderNumber;
	}


	public void setPayOrderNumber(Long payOrderNumber) {
		this.payOrderNumber = payOrderNumber;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public Boolean getApproveStatus() {
		return approveStatus;
	}


	public void setApproveStatus(Boolean approveStatus) {
		this.approveStatus = approveStatus;
	}


	public LocalDateTime getApproveDateTime() {
		return approveDateTime;
	}


	public void setApproveDateTime(LocalDateTime approveDateTime) {
		this.approveDateTime = approveDateTime;
	}


	public Boolean getDisapproveStatus() {
		return disapproveStatus;
	}


	public void setDisapproveStatus(Boolean disapproveStatus) {
		this.disapproveStatus = disapproveStatus;
	}


	public LocalDateTime getDisapproveDateTime() {
		return disapproveDateTime;
	}


	public void setDisapproveDateTime(LocalDateTime disapproveDateTime) {
		this.disapproveDateTime = disapproveDateTime;
	}


	public Boolean getGoinStatus() {
		return goinStatus;
	}


	public void setGoinStatus(Boolean goinStatus) {
		this.goinStatus = goinStatus;
	}


	public LocalDateTime getGoinDateTime() {
		return goinDateTime;
	}


	public void setGoinDateTime(LocalDateTime goinDateTime) {
		this.goinDateTime = goinDateTime;
	}


	public Boolean getGooutStatus() {
		return gooutStatus;
	}


	public void setGooutStatus(Boolean gooutStatus) {
		this.gooutStatus = gooutStatus;
	}


	public LocalDateTime getGooutDateTime() {
		return gooutDateTime;
	}


	public void setGooutDateTime(LocalDateTime gooutDateTime) {
		this.gooutDateTime = gooutDateTime;
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

	public String getGoinCity() {
		return goinCity;
	}

	public void setGoinCity(String goinCity) {
		this.goinCity = goinCity;
	}

	public String getGoinSematicDescription() {
		return goinSematicDescription;
	}

	public void setGoinSematicDescription(String goinSematicDescription) {
		this.goinSematicDescription = goinSematicDescription;
	}

	public String getGooutCity() {
		return gooutCity;
	}

	public void setGooutCity(String gooutCity) {
		this.gooutCity = gooutCity;
	}

	public String getGooutSematicDescription() {
		return gooutSematicDescription;
	}

	public void setGooutSematicDescription(String gooutSematicDescription) {
		this.gooutSematicDescription = gooutSematicDescription;
	}


	public StatusEnum getUserstatus() {
		return userstatus;
	}


	public void setUserstatus(StatusEnum userstatus) {
		this.userstatus = userstatus;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
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

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Integer getOwnerTotal() {
		return ownerTotal;
	}

	public void setOwnerTotal(Integer ownerTotal) {
		this.ownerTotal = ownerTotal;
	}

	public OrderOwnerVo getApproveOwner() {
		return approveOwner;
	}

	public void setApproveOwner(OrderOwnerVo approveOwner) {
		this.approveOwner = approveOwner;
	}

	public Boolean getToEvaluateModify() {
		return toEvaluateModify;
	}

	public void setToEvaluateModify(Boolean toEvaluateModify) {
		this.toEvaluateModify = toEvaluateModify;
	}

	public Boolean getFromEvaluateModify() {
		return fromEvaluateModify;
	}

	public void setFromEvaluateModify(Boolean fromEvaluateModify) {
		this.fromEvaluateModify = fromEvaluateModify;
	}

}
