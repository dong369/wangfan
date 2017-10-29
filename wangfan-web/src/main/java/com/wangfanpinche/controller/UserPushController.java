package com.wangfanpinche.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.UserPushService;
import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

@Controller
@RequestMapping("/UserPush")
public class UserPushController {
	
	@Autowired
	UserPushService userPushService;

	/**
	 * 推送设为已读 
	 */
	@RequestMapping("/read")
	public @ResponseBody Json read(PushEntityVo vo, HttpSession session){
		try {
			vo.setToUserId(SessionUtils.getUserId(session));
			userPushService.read(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 同步
	 */
	@RequestMapping("/sync")
	public @ResponseBody Json sync(HttpSession session){
		try {
			//本人的ID
			String userId = SessionUtils.getUserId(session);
			userPushService.sync(userId);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 查看所有未读的系统推送，再推送一下 
	 */
	@RequestMapping("/noread")
	public @ResponseBody Json noread(PushEntityVo vo, HttpSession session){
		try {
			//本人的ID
			vo.setToUserId(SessionUtils.getUserId(session));
			userPushService.noread(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 查看所有系统推送，按照结束时间倒叙(分页)
	 */
	@RequestMapping("/list")
	public @ResponseBody List<PushEntityVo> list(PushEntityVo vo, HttpSession session, PageHelper ph){
		vo.setToUserId(SessionUtils.getUserId(session));
		List<PushEntityVo> list = userPushService.list(vo, ph);
		return list;
	}
	
}
