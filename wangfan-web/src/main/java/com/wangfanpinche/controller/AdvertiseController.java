package com.wangfanpinche.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.AdvertiseService;
import com.wangfanpinche.vo.AdvertiseVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

/**
 * 广告
 *
 */
@Controller
@RequestMapping("/advertise")
public class AdvertiseController {
	
	@Autowired
	private AdvertiseService advertiseService;
	
	@RequestMapping("/advertise")
	public String advertises(){
		return "advertise/advertise";
	}
	
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(AdvertiseVo vo,PageHelper ph){
		return advertiseService.table(vo, ph);
	}

	@RequestMapping("/addPage")
	public String addPage(){
		return "advertise/advertiseAdd";
	}
	
	@RequestMapping("/add")
	public @ResponseBody Json add(AdvertiseVo vo){
		Json j = new Json();
		try {
			advertiseService.save(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/editPage")
	public String editPage(AdvertiseVo vo){
		return "advertise/advertiseEdit";
	}
	
	@RequestMapping("/edit")
	public @ResponseBody Json edit(AdvertiseVo vo){
		Json j = new Json();
		try {
			advertiseService.edit(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/delete")
	public @ResponseBody Json delete(AdvertiseVo vo){
		Json j = new Json();
		try {
			advertiseService.delete(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/get")
	public @ResponseBody AdvertiseVo get(AdvertiseVo vo){
		return advertiseService.get(vo);
	}
	
	/**
	 * App广告(不需要权限验证)
	 * @param vo
	 * @return
	 */
	@RequestMapping("/list")
	public @ResponseBody List<AdvertiseVo> list(AdvertiseVo vo){
		return advertiseService.list(vo);
	}

}
