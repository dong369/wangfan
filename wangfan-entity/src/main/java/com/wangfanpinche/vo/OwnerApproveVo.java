package com.wangfanpinche.vo;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import com.wangfanpinche.dto.OwnerApprove.OwnStatusEnum;
import com.wangfanpinche.utils.validation.anno.IdCard;

public class OwnerApproveVo{	
	
	private String id;//车主认证id
	
	private String userId;//用户ID
	
	@NotEmpty(message="车主姓名不能为空", groups=Ownertoapprove.class)
	@Length(min=2, max=10, message="姓名长度在2-10之间", groups=Ownertoapprove.class)
	private String ownerName;//车主姓名
	
	@NotEmpty(message="驾驶证号不能为空", groups=Ownertoapprove.class)
	@IdCard(message="不是合法的驾驶证号", groups=Ownertoapprove.class)
	private String ownerIdNumber;//驾驶证号
	
	@NotEmpty(message="行驶证不能为空", groups=Ownertoapprove.class)
	private String idenPhoto;//行驶证
	
	@NotEmpty(message="驾驶证不能为空", groups=Ownertoapprove.class)
	private String drivPhoto;//驾驶证 
	
	@NotEmpty(message="车辆信息不能为空", groups=Ownertoapprove.class)
	private String carInfoId;
	
	private String carmodel;//车型
	
	@NotEmpty(message="车牌号不能为空", groups=Ownertoapprove.class)
	private String carNumber;//车牌号
	
	@NotEmpty(message="车辆颜色不能为空", groups=Ownertoapprove.class)
	private String carColor;//颜色
	
	@NotEmpty(message="车辆所有人不能为空", groups=Ownertoapprove.class)
	private String carUserName;//车辆所有人
	
	private OwnStatusEnum status;// 状态(认证失败  待认证  认证中  认证通过)
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="驾驶证首次领证日期不能为空", groups=Ownertoapprove.class)
	private LocalDate firstDateTime;//驾驶证首次领证日期
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="驾驶证有效开始日期不能为空", groups=Ownertoapprove.class)
	private LocalDate effectiveStartDateTime;//驾驶证有效开始日期
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="驾驶证有效结束日期不能为空", groups=Ownertoapprove.class)
	private LocalDate effectiveEndDateTime;//驾驶证有效结束日期
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="行驶证注册日期不能为空", groups=Ownertoapprove.class)
	private LocalDate recordDateTime;//行驶证注册日期
	
	//车主认证(认证中)
	public interface Ownertoapprove {}
	
	
	
	public OwnerApproveVo(String userId) {
		super();
		this.userId = userId;
	}


	public OwnerApproveVo(String id, String userId, String ownerName, String ownerIdNumber, String idenPhoto, String drivPhoto, String carmodel, String carNumber, String carColor, OwnStatusEnum status) {
		super();
		this.id = id;
		this.userId = userId;
		this.ownerName = ownerName;
		this.ownerIdNumber = ownerIdNumber;
		this.idenPhoto = idenPhoto;
		this.drivPhoto = drivPhoto;
		this.carmodel = carmodel;
		this.carNumber = carNumber;
		this.carColor = carColor;
		this.status = status;
	}


	public OwnerApproveVo() {
		super();
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
	
	public String getCarInfoId() {
		return carInfoId;
	}
	public void setCarInfoId(String carInfoId) {
		this.carInfoId = carInfoId;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
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
	public String getCarmodel() {
		return carmodel;
	}
	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}


	public OwnStatusEnum getStatus() {
		return status;
	}


	public void setStatus(OwnStatusEnum status) {
		this.status = status;
	}



	public String getCarUserName() {
		return carUserName;
	}


	public void setCarUserName(String carUserName) {
		this.carUserName = carUserName;
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
