package com.wangfanpinche.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.OwnerApproveService;
import com.wangfanpinche.vo.OwnerApproveVo;
import com.wangfanpinche.vo.OwnerApproveVo.Ownertoapprove;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;


/**
 * 车主认证
 *
 */
@Controller
@RequestMapping("/ownerapprove")
public class OwnerApproveController {

	@Autowired
	private OwnerApproveService ownerApproveService;

	/**
	 * 是否通过车主认证
	 * @param vo
	 * @param result
	 * @return
	 */
	@RequestMapping("/hasOwnerApprove")
	public @ResponseBody Json hasOwnerApprove(OwnerApproveVo vo,HttpSession session){		
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			boolean b = ownerApproveService.hasOwnerApprove(vo);	
			return new Json(true, null, b);
		} catch (Exception e) {
			return new Json(false, e.getMessage(), null);
		}	
	}
	
	/**
	 * 车主进行认证
	 * @param vo
	 * @param result
	 * @return
	 */
	@RequestMapping("/ownerToApprove")
	public @ResponseBody Json ownerToApprove(@Validated(Ownertoapprove.class) OwnerApproveVo vo, BindingResult result,HttpSession session){		
		try {			
			if (result.hasErrors()) {
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			vo.setUserId(SessionUtils.getUser(session).getId());
			ownerApproveService.OwnerToApprove(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}	
	}
	
	/**
	 * 跳转到查询数据页面
	 * @return
	 */
	@RequestMapping("/ownerapprove")
	public String ownerapprove(){
		return "ownerapprove/ownerapprove";
	}
	
	/**
	 * 查询出车主认证数据
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(OwnerApproveVo vo, PageHelper ph){
		BootstrapTable table = ownerApproveService.table(vo, ph);
		return table;
	}
			
	
	/**
	 * 认证通过
	 * @param ownerApprove
	 * @return
	 */
	@RequestMapping("/ownerapprovesucc")
	public @ResponseBody Json OwnerUpdateApproveSucc(OwnerApproveVo vo,HttpSession session){
		try {			
			ownerApproveService.updateOwnerApprove(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}	

}
