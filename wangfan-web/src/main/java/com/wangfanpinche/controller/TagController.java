package com.wangfanpinche.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfanpinche.service.TagService;
import com.wangfanpinche.vo.TagVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

@Controller
@RequestMapping("/tag")
public class TagController {
	
	@Autowired
	private TagService tagService;
	
	@RequestMapping("/tags")
	public String tags(){
		return "tag/tag";
	}
	
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(TagVo vo,PageHelper ph){
		return tagService.table(vo, ph);
	}

	@RequestMapping("/addPage")
	public String addPage(){
		return "tag/tagAdd";
	}
	
	@RequestMapping("/add")
	public @ResponseBody Json add(TagVo vo){
		Json j = new Json();
		try {
			tagService.save(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/editPage")
	public String editPage(TagVo vo){
		return "tag/tagEdit";
	}
	
	@RequestMapping("/edit")
	public @ResponseBody Json edit(TagVo vo){
		Json j = new Json();
		try {
			tagService.edit(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}

	@RequestMapping("/delete")
	public @ResponseBody Json delete(TagVo vo){
		Json j = new Json();
		try {
			tagService.delete(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/get")
	public @ResponseBody TagVo get(TagVo vo){
		return tagService.get(vo);
	}
	
	
	/**
	 * app发布车单时标签列表
	 * @return
	 */
	@RequestMapping("/list")
	public @ResponseBody List<TagVo> list(){
		return tagService.list();
	}

}
