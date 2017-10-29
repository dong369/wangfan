package com.wangfanpinche.vo;

import java.time.LocalDateTime;

public class TagVo {

	private String id;// ID

	private LocalDateTime createDateTime;// 创建时间

	private LocalDateTime modifyDateTime;// 修改时间

	private Boolean deleted;// 是否删除

	private String tagName;// 标签内容

	private Integer seq;// 顺序
	
	public TagVo(){}

	public TagVo(String id, LocalDateTime createDateTime, LocalDateTime modifyDateTime, String tagName, Integer seq) {
		super();
		this.id = id;
		this.createDateTime = createDateTime;
		this.modifyDateTime = modifyDateTime;
		this.tagName = tagName;
		this.seq = seq;
	}

	public TagVo(String id, LocalDateTime createDateTime, LocalDateTime modifyDateTime, Boolean deleted, String tagName, Integer seq) {
		super();
		this.id = id;
		this.createDateTime = createDateTime;
		this.modifyDateTime = modifyDateTime;
		this.deleted = deleted;
		this.tagName = tagName;
		this.seq = seq;
	}
	

	public TagVo(String id, String tagName) {
		super();
		this.id = id;
		this.tagName = tagName;
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

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

}
