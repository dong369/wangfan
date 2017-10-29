package com.wangfanpinche.service;

import com.wangfanpinche.vo.TipVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface TipService {
	
	/**
	 * 添加提示
	 * @param vo
	 * @return
	 */
	public void save(TipVo vo);
	
	/**
	 * 通过id获取一条提示
	 * @param id
	 * @return
	 */
	public TipVo get(TipVo vo);
	
	/**
	 * 更新提示
	 * @param vo
	 */
	public void edit(TipVo vo);
	
	/**
	 * 删除提示
	 * @param vo
	 */
	public void delete(TipVo vo);
	
	/**
	 * 后台提示列表(带分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(TipVo vo,PageHelper ph);
		
}
