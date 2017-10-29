package com.wangfanpinche.dto.base;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;

/**
 * 继承类
 * 
 * @author kevin
 * @date 2016-09-26 12:05:26
 */
@MappedSuperclass
public class BaseEntity {

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	private String id;// ID

	@Column(name = "CREATEDATETIME")
	private LocalDateTime createDateTime;// 创建时间

	@Column(name = "MODIFYDATETIME")
	private LocalDateTime modifyDateTime;// 修改时间

	@Type(type = "true_false")
	@Column(name = "DELETED")
	private Boolean deleted;// 是否删除

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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


}
