package com.wangfanpinche.service;

import java.io.Serializable;
import java.util.List;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarYearVo;

public interface CarYearService {
	
	/**
	 * 根据车系id查询年代款
	 * @param vo
	 * @return
	 */
	public List<CarYearVo> list(CarYearVo vo);	
	
	/**
	 *  根据车系id查询年代款
	 * @param systemId
	 * @return
	 */
	public List<CarYearVo> listBySystemId(String systemId);
	
	/**
	 * 查询年代款
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(CarYearVo vo, PageHelper ph);
	
	/**
	 * 添加年代款信息
	 * @param vo
	 * @return
	 */
	public Serializable save(CarYearVo vo);
	/**
	 * 根据年代款id获取年代款信息
	 * @param vo
	 * @return
	 */
	public CarYearVo get(CarYearVo vo);
	
	/**
	 * 删除年代款
	 * @param vo
	 */
	public void delete(CarYearVo vo);
	
	/**
	 * 编辑年代款
	 * @param vo
	 */
	public void update(CarYearVo vo);

}
