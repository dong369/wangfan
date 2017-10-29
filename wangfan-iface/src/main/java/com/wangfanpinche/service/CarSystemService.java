package com.wangfanpinche.service;

import java.io.Serializable;
import java.util.List;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarSystemVo;


public interface CarSystemService {
	
	/**
	 * 根据品牌id查询车系
	 * @param vo
	 * @return
	 */
	public List<CarSystemVo> list(CarSystemVo vo);		
	
	/**
	 * 根据品牌id查询车系
	 * @param brandId
	 * @return
	 */
	public List<CarSystemVo> listByBrandId(String brandId);
	
	/**
	 * 查询车系
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(CarSystemVo vo, PageHelper ph);
	
	/**
	 * 添加车系信息
	 * @param vo
	 * @return
	 */
	public Serializable save(CarSystemVo vo);
	/**
	 * 根据车系id获取车系信息
	 * @param vo
	 * @return
	 */
	public CarSystemVo get(CarSystemVo vo);
	
	/**
	 * 删除车系
	 * @param vo
	 */
	public void delete(CarSystemVo vo);
	
	/**
	 * 编辑车系
	 * @param vo
	 */
	public void update(CarSystemVo vo);

}
