package com.wangfanpinche.service;

import java.util.List;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarBrandVo;

public interface CarBrandService {
	
	/**
	 * 列出所有的车辆品牌
	 * @return
	 */
	public List<CarBrandVo> list();
	
	/**
	 * 根据首字母
	 * @param vo
	 * @return
	 */
	public List<CarBrandVo> listByInitial(CarBrandVo vo);
	/**
	 * 查询车辆品牌
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(CarBrandVo vo, PageHelper ph);
	
	/**
	 * 添加车辆品牌信息
	 * @param vo
	 * @return
	 */
	public void save(CarBrandVo vo);
	/**
	 * 根据品牌id获取品牌信息
	 * @param vo
	 * @return
	 */
	public CarBrandVo get(CarBrandVo vo);
	
	/**
	 * 删除车辆品牌
	 * @param vo
	 */
	public void delete(CarBrandVo vo);
	
	/**
	 * 编辑车辆品牌
	 * @param vo
	 */
	public void update(CarBrandVo vo);
}
