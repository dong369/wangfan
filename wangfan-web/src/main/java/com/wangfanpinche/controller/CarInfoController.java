package com.wangfanpinche.controller;

import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.CarBrandService;
import com.wangfanpinche.service.CarConfigService;
import com.wangfanpinche.service.CarDisplacementService;
import com.wangfanpinche.service.CarInfoService;
import com.wangfanpinche.service.CarSystemService;
import com.wangfanpinche.service.CarYearService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarBrandVo;
import com.wangfanpinche.vo.car.CarDisplacementVo;
import com.wangfanpinche.vo.car.CarInfoVo;
import com.wangfanpinche.vo.car.CarSystemVo;
import com.wangfanpinche.vo.car.CarYearVo;


@RequestMapping("/carinfo")
@Controller
public class CarInfoController {
	
	@Autowired
	private CarInfoService carInfoService;	
	@Autowired
	private CarDisplacementService carDisplacementService;
	@Autowired
	private CarBrandService carBrandService;
	@Autowired
	private CarSystemService carSystemService;
	@Autowired
	private CarYearService carYearService;
	@Autowired
	private CarConfigService carConfigService;
	
	/**
	 * 跳转到查询数据页面
	 * @return
	 */
	@RequestMapping("/carinfo")
	public String manager(HttpServletRequest request){	
		request.setAttribute("postage", carConfigService.getPostage());
		request.setAttribute("toll", carConfigService.getToll());
		return "carinfo/carinfo";
	}
	
	/**
	 * 查询出车辆信息数据
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(CarInfoVo vo, PageHelper ph){
		BootstrapTable table = carInfoService.table(vo, ph);
		return table;
	}
	
	/**
	 * 跳转到添加车辆信息页面
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request){
		List<CarBrandVo> cbList = carBrandService.list();
		request.setAttribute("cbList", cbList);
		return "carinfo/carinfoAdd";
	}
	
	/**
	 * 添加车辆信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("/add")
	public @ResponseBody Json add(CarInfoVo vo){
		try {
			carInfoService.save(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		} 
	}
	
	
	/**
	 * 更新工信部油量或座位数
	 * @param vo
	 * @return
	 */
	@RequestMapping("/updatefuel")
	public @ResponseBody Json updateFuelOrSeat(CarInfoVo vo){
		try {
			carInfoService.updateFuelOrSeat(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		} 
	}		
	
	/**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	@RequestMapping("/get")
	public @ResponseBody CarInfoVo get(CarInfoVo vo){
		return carInfoService.get(vo);
	}
	
	/**
	 * 根据排量id获取车辆信息
	 * @param displacementId
	 * @return
	 */
	@RequestMapping("/getByDisplacementId")
	public @ResponseBody List<CarInfoVo> getByDisplacementId(String displacementId){
		List<CarInfoVo> vo = carInfoService.getByDisplacementId(displacementId);
		return vo;
	}
	
	/**
	 * 跳转到修改车辆信息页面
	 * @param vo
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(CarInfoVo vo,HttpServletRequest request){
		request.setAttribute("id", vo.getId());
		
		CarInfoVo cdvo = carInfoService.get(vo);

		List<CarBrandVo> list = carBrandService.list();
		request.setAttribute("cbList", list);
		
		List<CarSystemVo> systems = carSystemService.listByBrandId(cdvo.getBrandId());
		request.setAttribute("csList", systems);
		
		List<CarYearVo> years = carYearService.listBySystemId(cdvo.getSystemId());
		request.setAttribute("cyList", years);
		
		List<CarDisplacementVo> displacements = carDisplacementService.listByYearId(cdvo.getYearId());
		request.setAttribute("cdList", displacements);
		
		return "carinfo/carinfoEdit";
	}
	
	/**
	 * 修改车辆信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("/edit")
	public @ResponseBody Json edit(CarInfoVo vo){
		try {
			carInfoService.update(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 删除车辆信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody Json delete(CarInfoVo vo){
		try {
			carInfoService.delete(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 通过车辆信息ID和距离计算价格
	 * @return
	 */
	@RequestMapping("/amount")
	public @ResponseBody BigDecimal amout(String id, BigDecimal fromLng, BigDecimal fromLat, BigDecimal toLng, BigDecimal toLat){
		BigDecimal amout = carInfoService.amout(id, fromLng, fromLat, toLng, toLat);
		return amout;
	}
}
