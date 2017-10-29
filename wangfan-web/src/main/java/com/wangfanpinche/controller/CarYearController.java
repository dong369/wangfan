package com.wangfanpinche.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.CarBrandService;
import com.wangfanpinche.service.CarSystemService;
import com.wangfanpinche.service.CarYearService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarBrandVo;
import com.wangfanpinche.vo.car.CarSystemVo;
import com.wangfanpinche.vo.car.CarYearVo;


@Controller
@RequestMapping("/caryear")
public class CarYearController {
	
	@Autowired
	private CarYearService carYearService;
	@Autowired
	private CarBrandService carBrandService;
	@Autowired
	private CarSystemService carSystemService;
	
	
	/**
	 * 通过车系ID列出所有的年代款
	 * @return
	 */
	@RequestMapping("/listBySystemId")
	public @ResponseBody List<CarYearVo> listBySystemId(CarYearVo vo) {
		List<CarYearVo> carYears = carYearService.list(vo);
		return carYears;
	}
	
	/**
	 * 跳转到查询数据页面
	 * @return
	 */
	@RequestMapping("/caryear")
	public String manager(){
		return "caryear/caryear";
	}
	
	/**
	 * 查询出年代款数据
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(CarYearVo vo, PageHelper ph){
		BootstrapTable table = carYearService.table(vo, ph);
		return table;
	}
	
	/**
	 * 跳转到添加年代款页面
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request){
		List<CarBrandVo> list = carBrandService.list();
		request.setAttribute("cbList", list);
		return "caryear/caryearAdd";
	}
	
	/**
	 * 添加年代款
	 * @param vo
	 * @return
	 */
	@RequestMapping("/add")
	public @ResponseBody Json add(CarYearVo vo){
		try {
			carYearService.save(vo);
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
	public @ResponseBody CarYearVo get(CarYearVo vo){
		CarYearVo carYear = carYearService.get(vo);
		return carYear;
	}
	
	/**
	 * 跳转到修改年代款数据页面
	 * @param vo
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(CarYearVo vo,HttpServletRequest request){
		request.setAttribute("id", vo.getId());
		
		CarYearVo carYear = carYearService.get(vo);

		List<CarBrandVo> list = carBrandService.list();
		request.setAttribute("cbList", list);
		
		List<CarSystemVo> systems = carSystemService.listByBrandId(carYear.getBrandId());
		request.setAttribute("csList", systems);
		
		return "caryear/caryearEdit";
	}
	
	/**
	 * 修改年代款数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/edit")
	public @ResponseBody Json edit(CarYearVo vo){
		try {
			carYearService.update(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 删除年代款数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody Json delete(CarYearVo vo){
		try {
			carYearService.delete(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
}
