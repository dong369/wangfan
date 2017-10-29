package com.wangfanpinche.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.service.ProcityService;
import com.wangfanpinche.vo.agent.ProcityVo;

/**
 * 省市县
 *
 */
@Controller
@RequestMapping("/procity")
public class ProcityController {
	
	@Autowired
	private ProcityService procityService;
	
	/**
	 * 查询出所有的市
	 * @return
	 */
	@RequestMapping("/find")
	public @ResponseBody List<ProcityVo> find(){
		return procityService.find();
	}

}
