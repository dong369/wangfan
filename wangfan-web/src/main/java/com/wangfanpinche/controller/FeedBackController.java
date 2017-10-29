package com.wangfanpinche.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.FeedBackService;
import com.wangfanpinche.vo.FeedBackVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

/**
 * 反馈
 *
 */
@RequestMapping("/feedback")
@Controller
public class FeedBackController {
	
	@Autowired
	private FeedBackService feedBackService;
	
	
	/**
	 * 跳转到查询数据页面
	 * @return
	 */
	@RequestMapping("/feedback")
	public String manager(){
		return "feedback/feedback";
	}
	
	/**
	 * 查询出反馈数据
	 * @param feedBack
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(FeedBackVo vo, PageHelper ph){
		BootstrapTable table = feedBackService.table(vo, ph);
		return table;
	}	
	
	/**
	 * 根据ID获取单条数据
	 * @param id
	 * @return
	 */
	@RequestMapping("/get")
	public @ResponseBody FeedBackVo get(FeedBackVo vo){
		return feedBackService.updateAndGet(vo);
	}
	
	/**
	 * 跳转到修改标签数据页面
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(FeedBackVo vo){
		return "feedback/feedbackEdit";
	}
		
	/**
	 * 添加问题反馈信息
	 * @param feedBack
	 * @return
	 */
	@RequestMapping("/add")
	public @ResponseBody Json add(FeedBackVo vo, HttpSession session){
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			feedBackService.save(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}	
	
}
