package com.wangfanpinche.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.OwnerApproveService;
import com.wangfanpinche.service.UniversalAgentService;
import com.wangfanpinche.service.UserService;
import com.wangfanpinche.vo.OwnerApproveVo;
import com.wangfanpinche.vo.SessionUser;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.agent.UniversalAgentVo;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

/**
 * 全民代理
 *
 */
@Controller
@RequestMapping("/universalagent")
public class UniversalAgentController {
	
	@Autowired
	private UniversalAgentService universalAgentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OwnerApproveService ownerApproveService;

	/**
	 * 获取url 
	 */
	@RequestMapping("/genurl")
	public @ResponseBody Json genUrl(HttpSession session){
		try {
			SessionUser user = SessionUtils.getUser(session);
			//是否通过实名认证
			UserVo vo = new UserVo();
			vo.setId(user.getId());
			Boolean userApprove = userService.hasUserApprove(vo);
			if (userApprove == false) {
				return new Json(false, "生成失败:没有通过实名认证", null);
			}
			//是否通过车主认证
			OwnerApproveVo ovo = new OwnerApproveVo();
			ovo.setUserId(user.getId());
			Boolean ownerApprove = ownerApproveService.hasOwnerApprove(ovo);
			if (ownerApprove == false) {
				return new Json(false, "生成失败:没有通过车主认证", null);
			}
			
			String mobilePhone = user.getMobilePhone();
			String url = "/baseController/reg/" + Long.toHexString(Long.parseLong(mobilePhone));
			return new Json(true, null, url);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, "生成失败:" + e.getMessage(), null);
		}
	}
	
	/**
	 * 统计推荐人推荐的人数，完成车主车单数量，完成车主车单金额收益
	 */
	@RequestMapping("/statistics")
	public @ResponseBody UniversalAgentVo statistics(UniversalAgentVo vo, HttpSession session){
		vo.setFromUserPhone(SessionUtils.getUser(session).getMobilePhone());
		return universalAgentService.statistics(vo);
	}

	/**
	 * 今日收益 
	 */
	@RequestMapping("/today")
	public @ResponseBody UniversalAgentVo today(UniversalAgentVo vo, HttpSession session){
		vo.setFromUserPhone(SessionUtils.getUser(session).getMobilePhone());
		return universalAgentService.today(vo);
	}

	/**
	 * 查看收益记录
	 */
	@RequestMapping("/detail")
	public @ResponseBody List<UniversalAgentVo> detail(UniversalAgentVo vo, PageHelper ph, HttpSession session){
		vo.setFromUserPhone(SessionUtils.getUser(session).getMobilePhone());
		return universalAgentService.detail(vo, ph);
	}
}
