package com.wangfanpinche.service;

import com.wangfanpinche.vo.OwnerApproveVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface OwnerApproveService {
	
	/**
	 * 是否通过车主认证
	 * @param vo
	 * @return
	 */
	public Boolean hasOwnerApprove(OwnerApproveVo vo);
	
	/**
	 * 车主认证(填写车主认证信息)(更新车主信息和保存车辆信息)
	 * @param vo
	 */
	public void OwnerToApprove(OwnerApproveVo vo);
	
	/**
	 * 后台车主认证列表(带分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(OwnerApproveVo vo, PageHelper ph);
	
	/**
	 * 车主认证审核通过(修改状态)
	 * @param vo
	 */
	public void updateOwnerApprove(OwnerApproveVo vo);
	
}
