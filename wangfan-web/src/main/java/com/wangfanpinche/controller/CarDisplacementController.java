package com.wangfanpinche.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.CarBrandService;
import com.wangfanpinche.service.CarDisplacementService;
import com.wangfanpinche.service.CarSystemService;
import com.wangfanpinche.service.CarYearService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarBrandVo;
import com.wangfanpinche.vo.car.CarDisplacementVo;
import com.wangfanpinche.vo.car.CarSystemVo;
import com.wangfanpinche.vo.car.CarYearVo;


@Controller
@RequestMapping("/cardisplacement")
public class CarDisplacementController {
	
	@Autowired
	private CarDisplacementService carDisplacementService;
	@Autowired
	private CarBrandService carBrandService;
	@Autowired
	private CarSystemService carSystemService;
	@Autowired
	private CarYearService carYearService;
	
	/**
	 * 通过年代款获取所有的排量
	 * @return
	 */
	@RequestMapping("/listByYearId")
	public @ResponseBody List<CarDisplacementVo> listByYearId(CarDisplacementVo vo){
		return carDisplacementService.list(vo);
	}
	
	/**
	 * 跳转到查询数据页面
	 * @return
	 */
	@RequestMapping("/cardisplacement")
	public String manager(){
		return "cardisplacement/cardisplacement";
	}
	
	/**
	 * 查询出排量数据
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(CarDisplacementVo vo, PageHelper ph){
		BootstrapTable table = carDisplacementService.table(vo, ph);
		return table;
	}
	
	/**
	 * 跳转到添加排量页面
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request){
		
		List<CarBrandVo> cbList = carBrandService.list();
		request.setAttribute("cbList", cbList);
		
		return "cardisplacement/cardisplacementAdd";
	}
	
	/**
	 * 添加排量
	 * @param vo
	 * @return
	 */
	@RequestMapping("/add")
	public @ResponseBody Json add(CarDisplacementVo vo){
		try {
			carDisplacementService.save(vo);
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
	public @ResponseBody CarDisplacementVo get(CarDisplacementVo vo){
		CarDisplacementVo carDisplacement = carDisplacementService.get(vo);
		return carDisplacement;
	}
	
	/**
	 * 跳转到修改排量数据页面
	 * @param vo
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(CarDisplacementVo vo,HttpServletRequest request){
		request.setAttribute("id", vo.getId());
		
		CarDisplacementVo cdvo = carDisplacementService.get(vo);

		List<CarBrandVo> list = carBrandService.list();
		request.setAttribute("cbList", list);
		
		List<CarSystemVo> systems = carSystemService.listByBrandId(cdvo.getBrandId());
		request.setAttribute("csList", systems);
		
		List<CarYearVo> years = carYearService.listBySystemId(cdvo.getSystemId());
		request.setAttribute("cyList", years);
		
		return "cardisplacement/cardisplacementEdit";
	}
	
	/**
	 * 修改排量数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/edit")
	public @ResponseBody Json edit(CarDisplacementVo vo){
		try {
			carDisplacementService.update(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 删除排量数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody Json delete(CarDisplacementVo vo){
		try {
			carDisplacementService.delete(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

}
