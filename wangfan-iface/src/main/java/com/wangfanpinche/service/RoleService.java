package com.wangfanpinche.service;

import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;


public interface RoleService {
		
	
	/**
	 * 后台所有角色列表(带分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	BootstrapTable table(com.wangfanpinche.vo.RoleVo vo,PageHelper ph);
}
