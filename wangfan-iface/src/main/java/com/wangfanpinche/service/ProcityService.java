package com.wangfanpinche.service;

import java.util.List;

import com.wangfanpinche.dto.agent.Procity;
import com.wangfanpinche.vo.agent.ProcityVo;

public interface ProcityService {
	
	/**
	 * 查询出所有的市
	 * @param id
	 * @return
	 */
	public List<ProcityVo> find();
	
	
	void export();
	
}
