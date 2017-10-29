package com.wangfanpinche.service;

import java.util.List;

import com.wangfanpinche.vo.AdvertiseVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface AdvertiseService {
	
	/**
	 * 添加广告
	 * @param vo
	 * @return
	 */
	public void save(AdvertiseVo vo);
	
	/**
	 * 根据id获取一个广告
	 * @param id
	 * @return
	 */
	public AdvertiseVo get(AdvertiseVo vo);
	
	/**
	 * 更新广告
	 * @param vo
	 */
	public void edit(AdvertiseVo vo);
	
	/**
	 * 删除广告
	 * @param vo
	 */
	public void delete(AdvertiseVo vo);
	
	/**
	 * 后台广告列表(带分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(AdvertiseVo vo,PageHelper ph);
	
	/**
	 * App广告(不需要权限验证)
	 * @param vo
	 * @return
	 */
	public List<AdvertiseVo> list(AdvertiseVo vo);
}
