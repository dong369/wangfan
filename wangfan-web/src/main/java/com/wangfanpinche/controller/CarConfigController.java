package com.wangfanpinche.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.CarConfigService;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.car.CarConfigVo;

@RequestMapping("/carconfig")
@Controller
public class CarConfigController {
	
	@Autowired
	private CarConfigService carConfigService;
	
	
	@RequestMapping("/carconfig")
	public String manager(HttpServletRequest request){	
		CarConfigVo c = carConfigService.get();
		request.setAttribute("c", c);
		return "carconfig/carconfig";
	}
	
	@RequestMapping("/add")
	public @ResponseBody Json carConfigAdd(CarConfigVo vo){
		try {
			carConfigService.add(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		} 
	}
		
}
