package com.wangfanpinche.service;

import java.io.Serializable;
import java.util.List;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarDisplacementVo;


public interface CarDisplacementService {
	
	/**
	 * 根据年代款id查询排量
	 * @param vo
	 * @return
	 */
	public List<CarDisplacementVo> list(CarDisplacementVo vo);
	
	/**
	 * 根据年代款id查询排量
	 * @param yearId
	 * @return
	 */
	public List<CarDisplacementVo> listByYearId(String yearId);

	/**
	 * 查询排量
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(CarDisplacementVo vo, PageHelper ph);
	
	/**
	 * 添加排量信息
	 * @param vo
	 * @return
	 */
	public Serializable save(CarDisplacementVo vo);
	/**
	 * 根据排量id获取排量信息
	 * @param vo
	 * @return
	 */
	public CarDisplacementVo get(CarDisplacementVo vo);
	
	/**
	 * 删除排量
	 * @param vo
	 */
	public void delete(CarDisplacementVo vo);
	
	/**
	 * 编辑排量
	 * @param vo
	 */
	public void update(CarDisplacementVo vo);
}
