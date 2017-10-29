package com.wangfanpinche.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.CarBrandService;
import com.wangfanpinche.service.CarSystemService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarBrandVo;
import com.wangfanpinche.vo.car.CarSystemVo;


@Controller
@RequestMapping("/carsystem")
public class CarSystemController {
	
	@Autowired
	private CarSystemService carSystemService;
	@Autowired
	private CarBrandService carBrandService;
	
	/**
	 * 通过品牌ID列出所有的车系
	 * @return
	 */
	@RequestMapping("/listByBrandId")
	public @ResponseBody List<CarSystemVo> listByBrandId(CarSystemVo vo){
		List<CarSystemVo> carSystems = carSystemService.list(vo);
		return carSystems;
	}
	
	/**
	 * 跳转到查询数据页面
	 * @return
	 */
	@RequestMapping("/carsystem")
	public String manager(){
		return "carsystem/carsystem";
	}
	
	/**
	 * 查询出车系数据
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(CarSystemVo vo, PageHelper ph){
		BootstrapTable table = carSystemService.table(vo, ph);
		return table;
	}
	
	/**
	 * 跳转到添加车系页面
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request){
		List<CarBrandVo> list = carBrandService.list();
		request.setAttribute("cbList", list);
		return "carsystem/carsystemAdd";
	}
	
	/**
	 * 添加车系
	 * @param vo
	 * @return
	 */
	@RequestMapping("/add")
	public @ResponseBody Json add(CarSystemVo vo){
		try {
			carSystemService.save(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		} 
	}
	
	/**
	 * 根据ID获取单条数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/get")
	public @ResponseBody CarSystemVo get(CarSystemVo vo){
		CarSystemVo system = carSystemService.get(vo);
		return system;
	}
	
	/**
	 * 跳转到修改车系数据页面
	 * @param vo
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(CarSystemVo vo, HttpServletRequest request){
		List<CarBrandVo> list = carBrandService.list();
		request.setAttribute("cbList", list);
		request.setAttribute("id", vo.getId());
		return "carsystem/carsystemEdit";
	}
	
	/**
	 * 修改车系数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/edit")
	public @ResponseBody Json edit(CarSystemVo vo){
		try {
			carSystemService.update(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 删除车系数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody Json delete(CarSystemVo vo){
		try {
			carSystemService.delete(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
}
