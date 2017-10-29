package com.wangfanpinche.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.CarBrandService;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.car.CarBrandVo;

@Controller
@RequestMapping("/carbrand")
public class CarBrandController {

	@Autowired
	private CarBrandService carBrandService;

	/**
	 * 列出所有的品牌
	 * @return
	 */
	@RequestMapping("/list")
	public @ResponseBody List<CarBrandVo> list() {
		return carBrandService.list();
	}
	
	/**
	 * 通过首字母列出品牌
	 * @param vo
	 * @return
	 */
	@RequestMapping("/listByInitial")
	public @ResponseBody List<CarBrandVo> listByInitial(CarBrandVo vo) {
		return carBrandService.listByInitial(vo);
	}
	
	/**
	 * 跳转到查询数据页面
	 * @return
	 */
	@RequestMapping("/carbrand")
	public String manager(){
		return "carbrand/carbrand";
	}
	
	/**
	 * 查询出品牌数据
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(CarBrandVo vo, PageHelper ph){
		BootstrapTable table = carBrandService.table(vo, ph);
		return table;
	}
	
	/**
	 * 跳转到添加品牌页面
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(){
		return "carbrand/carbrandAdd";
	}
	
	/**
	 * 添加品牌
	 * @param vo
	 * @return
	 */
	@RequestMapping("/add")
	public @ResponseBody Json add(CarBrandVo vo){
		try {
			carBrandService.save(vo);
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
	public @ResponseBody CarBrandVo get(CarBrandVo vo){
		return carBrandService.get(vo);
	}
	
	/**
	 * 跳转到修改品牌数据页面
	 * @param vo 
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(CarBrandVo vo){
		return "carbrand/carbrandEdit";
	}
	
	/**
	 * 修改品牌数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/edit")
	public @ResponseBody Json edit(CarBrandVo vo){
		try {
			carBrandService.update(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 删除品牌数据
	 * @param vo
	 * @return
	 */
	@RequestMapping("/delete")
	public @ResponseBody Json delete(CarBrandVo vo){
		try {
			carBrandService.delete(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

}
