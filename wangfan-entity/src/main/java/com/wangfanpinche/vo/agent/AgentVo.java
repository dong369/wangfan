package com.wangfanpinche.vo.agent;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 代理商
 */
public class AgentVo{
	
	private String id;//代理商id
	
	@NotEmpty(message="用户不能为空!", groups={Bind.class, Unbind.class})
	private String userId;//用户Id
	
	private String username;// 用户真实姓名
	
	private String mobilePhone;// 手机号
	
	private String remark;// 备注
	
	@NotEmpty(message="代理区域不能为空!", groups={Bind.class, Unbind.class})
	private String procityId;//选中的区域id
	
	private List<String> ids;
	
	public interface Bind {}
	public interface Unbind {}

	public AgentVo(String userId, String username, String mobilePhone) {
		super();
		this.userId = userId;
		this.username = username;
		this.mobilePhone = mobilePhone;
	}

	public AgentVo() {
		super();
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getProcityId() {
		return procityId;
	}

	public void setProcityId(String procityId) {
		this.procityId = procityId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	
}
