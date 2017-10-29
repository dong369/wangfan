package com.wangfanpinche.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.dto.agent.Agent;
import com.wangfanpinche.service.AgentService;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.agent.AgentVo;
import com.wangfanpinche.vo.agent.AgentVo.Bind;
import com.wangfanpinche.vo.agent.AgentVo.Unbind;
import com.wangfanpinche.vo.base.Json;

/**
 * 代理商
 *
 */
@Controller
@RequestMapping("/agent")
public class AgentController {
	
	@Autowired
	private AgentService agentService;
	
	@RequestMapping("/agent")
	public String agent(){
		return "agent/agent";
	}
	
	@RequestMapping("/count")
	public String count(AgentVo vo, HttpSession session, HttpServletRequest request){
		vo.setUserId(SessionUtils.getUserId(session));
		List<Agent> agent = agentService.procity(vo);
		request.setAttribute("agent", agent);
		return "agent/count";
	}
	
	/**
	 * 拿到左侧的树对象
	 * @return
	 */
	@RequestMapping("/tree")
	public @ResponseBody List<AgentVo> tree(){
		return null;
	}
	
	/**
	 * 通过手机号拿到用户信息（用户的ID，姓名，手机号，所代理的所有区域）
	 * @param vo
	 * @return
	 */
	@RequestMapping("/info")
	public @ResponseBody UserVo info(UserVo vo){
		return agentService.getUserByPhone(vo);
	}
	
	/**
	 * 给某一个用户,绑定一个新的区域  userid, 区域ID 
	 * @return
	 */
	@RequestMapping("/bind")
	public @ResponseBody Json bind(@Validated(Bind.class) AgentVo vo, BindingResult result){
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			agentService.bind(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			return new Json(false, e.getMessage(), null);
		}
	}
	
	
	/**
	 * 给某一个用户,解绑  userid, 区域ID 
	 * @return
	 */
	@RequestMapping("/unbind")
	public @ResponseBody Json unbind(@Validated(Unbind.class) AgentVo vo, BindingResult result){
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			agentService.unbind(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 根据区域id查询代理的用户
	 * @param vo
	 * @return
	 */
	@RequestMapping("/bingUser")
	public @ResponseBody AgentVo bingUser(AgentVo vo){
		return agentService.getUserByProcityId(vo);
	}
	
	/**
	 * 统计某个代理商代理某个市的车单数量
	 */
	@RequestMapping("/statisticsOrder")
	public @ResponseBody List<Map<String, Object>> statisticsOrder(String id, String zuijin, String startDate, String endDate){
		LocalDate start = null;
		LocalDate end = null;
		if(StringUtils.hasText(zuijin)){
			end = LocalDate.now();
			start = end.minusDays(Integer.parseInt(zuijin) - 1);
		} else {
			start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}

		List<Map<String, Object>> statisticsOrder = agentService.statisticsOrder(start, end, id);
		return statisticsOrder;
	}
		
	/**
	 * 统计某个代理商代理某个市的车单金额
	 */
	@RequestMapping("/statisticsOrderMoney")
	public @ResponseBody List<Map<String, Object>> statisticsOrderMoney(String id, String zuijin, String startDate, String endDate){
		LocalDate start = null;
		LocalDate end = null;
		if(StringUtils.hasText(zuijin)){
			end = LocalDate.now();
			start = end.minusDays(Integer.parseInt(zuijin) - 1);
		} else {
			start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		List<Map<String, Object>> statisticsOrderMoney = agentService.statisticsOrderMoney(start, end, id);
		return statisticsOrderMoney;
	}
	
	/**
	 * 统计某个代理商代理某个市的用户注册量
	 */
	@RequestMapping("/statisticsUserRegist")
	public @ResponseBody List<Map<String, Object>> statisticsUserRegist(String id, String zuijin, String startDate, String endDate){
		LocalDate start = null;
		LocalDate end = null;
		if(StringUtils.hasText(zuijin)){
			end = LocalDate.now();
			start = end.minusDays(Integer.parseInt(zuijin) - 1);
		} else {
			start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		List<Map<String, Object>> statisticsUserRegist = agentService.statisticsUserRegist(start, end, id);
		return statisticsUserRegist;
	}
	
}
