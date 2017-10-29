package com.wangfanpinche.service;

import java.io.Serializable;
import java.util.List;
import com.wangfanpinche.vo.ResourceVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface ResourceService {

	/**
	 * 所有资源列表
	 * 
	 * @return
	 */
	List<ResourceVo> findAll();

	/**
	 * 后台所有资源列表(带分页)
	 * 
	 * @param vo
	 * @param ph
	 * @return
	 */
	BootstrapTable table(com.wangfanpinche.vo.ResourceVo vo, PageHelper ph);

	/**
	 * 添加资源
	 * 
	 * @param vo
	 * @return
	 */
	public Serializable save(ResourceVo vo);
}
