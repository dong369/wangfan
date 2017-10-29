package com.wangfanpinche.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.wangfanpinche.dto.OwnerApprove.OwnStatusEnum;
import com.wangfanpinche.dto.User.StatusEnum;
import com.wangfanpinche.utils.validation.anno.IdCard;

/**
 * 用户
 *
 */
public class UserVo {
	
	private String id;
	
	private LocalDateTime createDateTime;// 创建时间
	
	private Boolean deleted;// 是否删除
	
	@NotEmpty(message="真实姓名必填", groups={UserApprove.class})
	@Length(min=2, max=10, message="姓名长度在2-10之间", groups={ UserApprove.class})
	private String username;// 用户真实姓名
	
	@NotEmpty(message="身份证号必填", groups={UserApprove.class})
	@IdCard(message="不是合法的身份证号", groups={UserApprove.class})
	private String idNumber;// 身份证号
	
	private String email;// 邮箱
	
	@NotEmpty(message="手机号必填", groups={SendVerify.class, LoginByPhone.class})
	@Pattern(regexp="(13|14|15|17|18)[0-9]{9}", message="请填写正确的手机号", groups={SendVerify.class, LoginByPhone.class})
	private String mobilePhone;// 手机号
	
	@NotEmpty(message="验证码必填", groups=LoginByPhone.class)
	@Length(min=4,max=4,message="验证码必须是4位", groups=LoginByPhone.class)
	private String code;// 验证码
	
	private String userIcon;// 头像
	//枚举	
	private StatusEnum status;// 实名认证状态( 认证失败  待验证   待认证   认证通过)
	
	private OwnStatusEnum ostatus;// 车主认证状态(认证失败 待认证   认证中   认证通过)
	
	private BigDecimal balance;//余额
	
	private String pwd;//密码
	
	@NotEmpty(message="设备号和mac必填", groups=LoginByPhone.class)
	private String phoneToken;//设备号和mac			
	
	private String industry;//行业
	
	private String company;//所在公司
	
	private String profession;//职业
	
	private String characterSignature;//个性签名
	
	private String homeAddress;//家乡地址
	
	private String residentAddress;//常驻地址	
			
	private List<RoleVo> roles = new ArrayList<>();
	
	private String carId;//车辆id
	
	private String carColor;
	
	private String carNumber;
	
	private String carInfoId;//车信息id
	
	private String carmodel;
	
	private String brandSystem;//车品牌和车系
	
	private Long total;//拼车次数
	
	private String sosName;//紧急联系人名称
	
	private String sosMobilePhone;// 紧急联系人手机号
	
	private Long orderNumber;//订单号
	
	private String ip;//ip
	
	private BigDecimal fromLng;// 用户注册时获取的经度
	
	private BigDecimal fromLat;// 用户注册时获取的纬度
	
	private String fromCity;//用户注册城市
	
	private String fromSematicDescription;//用户注册地点描述

	//1.1 手机端登录-输入手机号，发送验证码
	public interface SendVerify {}
	//1.2 手机端，输入验证码，登陆
	public interface LoginByPhone {}	
	//2.2.8实名认证
	public interface UserApprove {}
	
	public UserVo() {
		super();
	}
	
	public UserVo(String id, Boolean deleted, String username, String mobilePhone) {
		super();
		this.id = id;
		this.deleted = deleted;
		this.username = username;
		this.mobilePhone = mobilePhone;
	}

	public UserVo(String id, String mobilePhone, StatusEnum status, String phoneToken) {
		super();
		this.id = id;
		this.mobilePhone = mobilePhone;
		this.status = status;
		this.phoneToken = phoneToken;
	}

	public UserVo(String id, LocalDateTime createDateTime, String username, String mobilePhone) {
		super();
		this.id = id;
		this.createDateTime = createDateTime;
		this.username = username;
		this.mobilePhone = mobilePhone;
	}

	public UserVo(String id) {
		this.id = id;
	}
	
	public UserVo(String id, String sosName, String sosMobilePhone) {
		super();
		this.id = id;
		this.sosName = sosName;
		this.sosMobilePhone = sosMobilePhone;
	}

	public UserVo(String id, StatusEnum status) {
		this.id = id;
		this.status = status;
	}

	public UserVo(String id, String username, String mobilePhone, StatusEnum status, OwnStatusEnum ostatus, BigDecimal balance) {
		super();
		this.id = id;
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.status = status;
		this.ostatus = ostatus;
		this.balance = balance;
	}
	

	public UserVo(String id, Boolean deleted, String username, String mobilePhone, StatusEnum status, OwnStatusEnum ostatus, BigDecimal balance) {
		super();
		this.id = id;
		this.deleted = deleted;
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.status = status;
		this.ostatus = ostatus;
		this.balance = balance;
	}

	public UserVo(String id, String username, String idNumber, String email, String mobilePhone, String userIcon, StatusEnum status, BigDecimal balance, String pwd, String phoneToken, String industry, String company, String profession, String characterSignature, String homeAddress, String residentAddress) {
		this.id = id;
		this.username = username;
		this.idNumber = idNumber;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.status = status;
		this.balance = balance;
		this.pwd = pwd;
		this.phoneToken = phoneToken;
		this.industry = industry;
		this.company = company;
		this.profession = profession;
		this.characterSignature = characterSignature;
		this.homeAddress = homeAddress;
		this.residentAddress = residentAddress;
	}
			
	public UserVo(String id, String username, String mobilePhone, String userIcon, StatusEnum status, OwnStatusEnum ostatus) {
		super();
		this.id = id;
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.status = status;
		this.ostatus = ostatus;
	}		
	
	public UserVo(String id, String username, String mobilePhone, String userIcon, StatusEnum status, OwnStatusEnum ostatus, String characterSignature) {
		super();
		this.id = id;
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.status = status;
		this.ostatus = ostatus;
		this.characterSignature = characterSignature;
	}

	
	public UserVo(String id, String username, String idNumber, String mobilePhone, String userIcon, StatusEnum status, String industry, String company, String profession, String characterSignature, String homeAddress, String residentAddress) {
		super();
		this.id = id;
		this.username = username;
		this.idNumber = idNumber;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.status = status;
		this.industry = industry;
		this.company = company;
		this.profession = profession;
		this.characterSignature = characterSignature;
		this.homeAddress = homeAddress;
		this.residentAddress = residentAddress;
	}
	
	public UserVo(String username, String mobilePhone, String userIcon, String homeAddress, StatusEnum status) {
		super();
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.homeAddress = homeAddress;
		this.status = status;
	}

	public UserVo(String username, String mobilePhone, String userIcon, StatusEnum status, OwnStatusEnum ostatus, String homeAddress, String carId, String carColor, String carNumber, String carmodel) {
		super();
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.status = status;
		this.ostatus = ostatus;
		this.homeAddress = homeAddress;
		this.carId = carId;
		this.carColor = carColor;
		this.carNumber = carNumber;
		this.carmodel = carmodel;
	}
	
	public UserVo(String username, String mobilePhone, String userIcon, StatusEnum status, OwnStatusEnum ostatus, String homeAddress, String carId, String carColor, String carNumber, String carmodel, String carInfoId) {
		super();
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.status = status;
		this.ostatus = ostatus;
		this.homeAddress = homeAddress;
		this.carId = carId;
		this.carColor = carColor;
		this.carNumber = carNumber;
		this.carmodel = carmodel;
		this.carInfoId = carInfoId;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPhoneToken() {
		return phoneToken;
	}
	public void setPhoneToken(String phoneToken) {
		this.phoneToken = phoneToken;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getCharacterSignature() {
		return characterSignature;
	}
	public void setCharacterSignature(String characterSignature) {
		this.characterSignature = characterSignature;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getResidentAddress() {
		return residentAddress;
	}
	public void setResidentAddress(String residentAddress) {
		this.residentAddress = residentAddress;
	}

	public OwnStatusEnum getOstatus() {
		return ostatus;
	}

	public void setOstatus(OwnStatusEnum ostatus) {
		this.ostatus = ostatus;
	}

	public List<RoleVo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVo> roles) {
		this.roles = roles;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public String getSosName() {
		return sosName;
	}

	public void setSosName(String sosName) {
		this.sosName = sosName;
	}

	public String getSosMobilePhone() {
		return sosMobilePhone;
	}

	public void setSosMobilePhone(String sosMobilePhone) {
		this.sosMobilePhone = sosMobilePhone;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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

	public String getBrandSystem() {
		return brandSystem;
	}

	public void setBrandSystem(String brandSystem) {
		this.brandSystem = brandSystem;
	}

	public String getCarInfoId() {
		return carInfoId;
	}

	public void setCarInfoId(String carInfoId) {
		this.carInfoId = carInfoId;
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

	
}
