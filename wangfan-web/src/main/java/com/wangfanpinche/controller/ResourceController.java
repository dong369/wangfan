package com.wangfanpinche.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resource")
public class ResourceController {
	
	@RequestMapping("/manager")
	public String manager(){
		return "resource/resource";
	}
	
}
