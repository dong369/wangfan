package com.wangfanpinche.dto;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.wangfanpinche.dto.base.BaseEntity;

/**
 * 车主认证表
 */
@Entity
@Table(name = "t_ownerapprove")
@DynamicInsert
@DynamicUpdate
public class OwnerApprove extends BaseEntity{
	
	@Column(length=36)
	private String userId;//用户ID
	
	@Column(length=20)
	private String ownerName;//车主姓名
	
	@Column(unique = true, nullable=true,length=18)
	private String ownerIdNumber;//驾驶证号
	
	@Column(length=200)
	private String idenPhoto;//行驶证
	
	@Column(length=200)
	private String drivPhoto;//驾驶证
	
	@Enumerated(EnumType.ORDINAL)
	private OwnStatusEnum status;// 状态(认证失败  待认证  认证中  认证通过)
	
	private LocalDate firstDateTime;//驾驶证首次领证日期
	
	private LocalDate effectiveStartDateTime;//驾驶证有效开始日期
	
	private LocalDate effectiveEndDateTime;//驾驶证有效结束日期
	
	private LocalDate recordDateTime;//行驶证注册日期
	
	public enum OwnStatusEnum {
		认证失败,
		待认证,
		认证中,
		认证通过; 					
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerIdNumber() {
		return ownerIdNumber;
	}
	public void setOwnerIdNumber(String ownerIdNumber) {
		this.ownerIdNumber = ownerIdNumber;
	}
	public String getIdenPhoto() {
		return idenPhoto;
	}
	public void setIdenPhoto(String idenPhoto) {
		this.idenPhoto = idenPhoto;
	}
	public String getDrivPhoto() {
		return drivPhoto;
	}
	public void setDrivPhoto(String drivPhoto) {
		this.drivPhoto = drivPhoto;
	}
	public OwnStatusEnum getStatus() {
		return status;
	}
	public void setStatus(OwnStatusEnum status) {
		this.status = status;
	}	
	public LocalDate getFirstDateTime() {
		return firstDateTime;
	}
	public void setFirstDateTime(LocalDate firstDateTime) {
		this.firstDateTime = firstDateTime;
	}
	public LocalDate getEffectiveStartDateTime() {
		return effectiveStartDateTime;
	}
	public void setEffectiveStartDateTime(LocalDate effectiveStartDateTime) {
		this.effectiveStartDateTime = effectiveStartDateTime;
	}
	public LocalDate getEffectiveEndDateTime() {
		return effectiveEndDateTime;
	}
	public void setEffectiveEndDateTime(LocalDate effectiveEndDateTime) {
		this.effectiveEndDateTime = effectiveEndDateTime;
	}
	public LocalDate getRecordDateTime() {
		return recordDateTime;
	}
	public void setRecordDateTime(LocalDate recordDateTime) {
		this.recordDateTime = recordDateTime;
	}
	
		
}
