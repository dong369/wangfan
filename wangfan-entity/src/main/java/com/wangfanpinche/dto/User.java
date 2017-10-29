package com.wangfanpinche.dto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wangfanpinche.dto.base.BaseEntity;
import com.wangfanpinche.dto.order.Location;


/**
 * 用户
 *
 */
@Entity
@Table(name = "t_user")
@DynamicInsert
@DynamicUpdate
public class User extends BaseEntity {
	
	@Column(length=20)
	private String username;// 用户真实姓名
	
	@Column(unique = true, nullable=true,length=18)
	private String idNumber;// 身份证号
	
	@Column(length=30)
	private String email;// 邮箱
	
	@Column(unique = true, nullable=true,length=11)
	private String mobilePhone;// 手机号
	
	@Column(length=200)
	private String userIcon;// 头像
	
	//枚举
	@Enumerated(EnumType.ORDINAL)
	private StatusEnum status;// 状态( 认证失败   待验证  待认证   认证通过)
	
	@Column(precision = 23,scale = 12)
	private BigDecimal balance;//余额
	
	@Column(length=30)
	private String pwd;//密码
	
	@Column(length=50)
	private String phoneToken;//设备号和mac			
	
	@ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) })
	private Set<Role> roles = new HashSet<Role>(0);// 用户有哪些角色
	
	@Column(length=20)
	private String industry;//行业
	
	@Column(length=100)
	private String company;//所在公司
	
	@Column(length=50)
	private String profession;//职业
	
	@Column(length=30)
	private String characterSignature;//个性签名
	
	@Column(length=80)
	private String homeAddress;//家乡地址
	
	@Column(length=80)
	private String residentAddress;//常驻地址
	
	@Column(length=20)
	private String sosName;//紧急联系人名称
	
	@Column(length=11)
	private String sosMobilePhone;// 紧急联系人手机号
	
	private Long orderNumber;//订单号
	
	@OneToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST})
	private Location fromLocation;//用户注册位置
		
	public enum StatusEnum {
		认证失败,
		待验证,
		待认证,
		认证通过		
	}
	
	public User(){}
	
	
	public User(String id){
		super.setId(id);
	}
	
	public User(String username, String mobilePhone, String userIcon, String homeAddress) {
		super();
		this.username = username;
		this.mobilePhone = mobilePhone;
		this.userIcon = userIcon;
		this.homeAddress = homeAddress;
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
	
	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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


	public Long getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}


	public Location getFromLocation() {
		return fromLocation;
	}


	public void setFromLocation(Location fromLocation) {
		this.fromLocation = fromLocation;
	}


}
