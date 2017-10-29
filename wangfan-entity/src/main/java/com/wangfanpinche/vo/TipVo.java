package com.wangfanpinche.vo;

import java.time.LocalDateTime;

public class TipVo {

	private String id;// ID

	private LocalDateTime createDateTime;// 创建时间

	private LocalDateTime modifyDateTime;// 修改时间

	private Boolean deleted;// 是否删除

	private String tipName;//提示内容

	private String tipDescription;//提示描述
	
	public TipVo(){}
	
	
	
	public TipVo(String id, LocalDateTime createDateTime, LocalDateTime modifyDateTime, String tipName, String tipDescription) {
		super();
		this.id = id;
		this.createDateTime = createDateTime;
		this.modifyDateTime = modifyDateTime;
		this.tipName = tipName;
		this.tipDescription = tipDescription;
	}



	public TipVo(String id, LocalDateTime createDateTime, LocalDateTime modifyDateTime, Boolean deleted, String tipName, String tipDescription) {
		super();
		this.id = id;
		this.createDateTime = createDateTime;
		this.modifyDateTime = modifyDateTime;
		this.deleted = deleted;
		this.tipName = tipName;
		this.tipDescription = tipDescription;
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}
	public LocalDateTime getModifyDateTime() {
		return modifyDateTime;
	}
	public void setModifyDateTime(LocalDateTime modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getTipName() {
		return tipName;
	}
	public void setTipName(String tipName) {
		this.tipName = tipName;
	}
	public String getTipDescription() {
		return tipDescription;
	}
	public void setTipDescription(String tipDescription) {
		this.tipDescription = tipDescription;
	}

}
