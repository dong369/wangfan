package com.wangfanpinche.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.TipService;
import com.wangfanpinche.vo.TipVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

/**
 * 小提示
 *
 */
@Controller
@RequestMapping("/tip")
public class TipController {
	
	@Autowired
	private TipService tipService;
	
	@RequestMapping("/tip")
	public String tips(){
		return "tip/tip";
	}
	
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(TipVo vo,PageHelper ph){
		return tipService.table(vo, ph);
	}

	@RequestMapping("/addPage")
	public String addPage(){
		return "tip/tipAdd";
	}
	
	@RequestMapping("/add")
	public @ResponseBody Json add(TipVo vo){
		Json j = new Json();
		try {
			tipService.save(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/editPage")
	public String editPage(TipVo vo){
		return "tip/tipEdit";
	}
	
	@RequestMapping("/edit")
	public @ResponseBody Json edit(TipVo vo){
		Json j = new Json();
		try {
			tipService.edit(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/delete")
	public @ResponseBody Json delete(TipVo vo){
		Json j = new Json();
		try {
			tipService.delete(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/get")
	public @ResponseBody TipVo get(TipVo vo){
		return tipService.get(vo);
	}

}
