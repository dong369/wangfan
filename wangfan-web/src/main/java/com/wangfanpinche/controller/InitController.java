package com.wangfanpinche.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wangfanpinche.service.InitService;


@Controller
public class InitController {
	
	@Autowired
	private InitService initService;
	
	@RequestMapping("/init")
	public String init(){
		try {
			initService.initData();
			return "redirect:/user/loginByPc";
		} catch (Exception e) {
			e.printStackTrace();
			return "init/initError";
		}
	}
	

}
