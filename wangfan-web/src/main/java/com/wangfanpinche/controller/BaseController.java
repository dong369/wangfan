package com.wangfanpinche.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.dto.agent.UniversalAgent.UniversalAgentType;
import com.wangfanpinche.service.UniversalAgentService;
import com.wangfanpinche.vo.agent.UniversalAgentVo;
import com.wangfanpinche.vo.base.Json;


@Controller
@RequestMapping("/baseController")
public class BaseController {
	
	@Autowired
	private UniversalAgentService universalAgentService;
	
	@RequestMapping("/reg/{phone}")
	public String reg(@PathVariable("phone") String phone, HttpServletRequest request){
		request.setAttribute("phone", phone);
		return "base/quanmindaili";
	}
	
	/**
	 * 推荐别人注册
	 * @param fromPhone
	 * @param toPhone
	 * @return
	 */
	@RequestMapping(value = "/recommendWeb/{mobilePhone}")
	public @ResponseBody Json recommend(@PathVariable("mobilePhone") String fromPhone, String toPhone, String code) {
		fromPhone = Long.parseLong(fromPhone, 16) + "";
		try {
			UniversalAgentVo vo = new UniversalAgentVo();
			vo.setFromUserPhone(fromPhone);
			vo.setToUserPhone(toPhone);
			vo.setCode(code);
			vo.setRegType(UniversalAgentType.WEB);
			universalAgentService.recommend(vo);
			return new Json(true, "推荐成功!", null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, "推荐失败:" + e.getMessage(), null);
		}
	}
	
}
