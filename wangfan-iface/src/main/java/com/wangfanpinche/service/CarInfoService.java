package com.wangfanpinche.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarInfoVo;

public interface CarInfoService {		
	
	/**
	 * 更新工信部油量或座位数
	 * @param vo
	 */
	public void updateFuelOrSeat(CarInfoVo vo);
	
	/**
	 * 查询车辆信息
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(CarInfoVo vo, PageHelper ph);
	
	/**
	 * 添加车辆信息
	 * @param vo
	 * @return
	 */
	public Serializable save(CarInfoVo vo);
	
	/**
	 * 根据车辆信息id获取车辆信息
	 * @param id
	 * @return
	 */
	public CarInfoVo get(CarInfoVo vo);		
	
	/**
	 * 删除车辆信息
	 * @param vo
	 */
	public void delete(CarInfoVo vo);
	
	/**
	 * 编辑车辆信息
	 * @param vo
	 */
	public void update(CarInfoVo vo);

	/**
	 * 根据排量id获取车辆信息
	 * @param displacementId
	 * @return
	 */
	public List<CarInfoVo> getByDisplacementId(String displacementId);
	
	/**
	 * 通过车辆信息ID和距离计算价格
	 * @param id
	 * @return
	 */
	public BigDecimal amout(String id, BigDecimal fromLng, BigDecimal fromLat, BigDecimal toLng, BigDecimal toLat);

}
