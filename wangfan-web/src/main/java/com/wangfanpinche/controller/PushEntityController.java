package com.wangfanpinche.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.PushEntityService;
import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

/**
 * 系统推送 
 */
@Controller
@RequestMapping("/pushentity")
public class PushEntityController {
	
	@Autowired
	private PushEntityService pushEntityService;
	
	@RequestMapping("/pushentity")
	public String pushentity(){
		return "pushentity/pushentity";
	}
	
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(PushEntityVo vo,PageHelper ph){
		return pushEntityService.table(vo, ph);
	}

	@RequestMapping("/addPage")
	public String addPage(){
		return "pushentity/pushentityAdd";
	}
	
	@RequestMapping("/add")
	public @ResponseBody Json add(PushEntityVo vo, HttpSession session){
		Json j = new Json();
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			pushEntityService.save(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/editPage")
	public String editPage(PushEntityVo vo){
		return "pushentity/pushentityEdit";
	}
	
	@RequestMapping("/edit")
	public @ResponseBody Json edit(PushEntityVo vo){
		Json j = new Json();
		try {
			pushEntityService.edit(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/delete")
	public @ResponseBody Json delete(PushEntityVo vo){
		Json j = new Json();
		try {
			pushEntityService.delete(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/get")
	public @ResponseBody PushEntityVo get(PushEntityVo vo){
		return pushEntityService.get(vo);
	}
}
