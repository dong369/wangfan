package com.wangfanpinche.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.internal.util.AlipaySignature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.UserService;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.web.IpUtils;
import com.wangfanpinche.vo.CarVo;
import com.wangfanpinche.vo.JournalVo;
import com.wangfanpinche.vo.ResourceVo;
import com.wangfanpinche.vo.SessionUser;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.UserVo.LoginByPhone;
import com.wangfanpinche.vo.UserVo.SendVerify;
import com.wangfanpinche.vo.UserVo.UserApprove;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;


	@RequestMapping(value = "/loginByPc", method = RequestMethod.GET)
	public String login() {
		return "user/userLogin";
	}

	@RequestMapping(value = "/loginByPc", method = RequestMethod.POST)
	public @ResponseBody Json login(UserVo vo, HttpSession session) {
		try {
			UserVo user = userService.getByPhoneAndPwd(vo);

			if (user == null) {
				throw new RuntimeException("手机号或密码错误!");
			}

			SessionUser sessionUser = new SessionUser();
			BeanUtils.copyProperties(user, sessionUser);
			sessionUser.setResourceList(userService.getResourceList(user.getId()));
			sessionUser.setMenuList(getMenuList(sessionUser));
			session.setAttribute(SessionUtils.sessionUser, sessionUser);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 后台用户菜单列表
	 * @param session
	 * @return
	 */
	@RequestMapping("/userMenu")
	public @ResponseBody List<ResourceVo> menuList(HttpSession session) {
		return ((SessionUser) session.getAttribute(SessionUtils.sessionUser)).getMenuList();
	}

	@RequestMapping("/index")
	public String index() {
		return "user/index";
	}

	@RequestMapping("/logoutByPc")
	public String logoutByPc(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/user/loginByPc";
	}
	
	@RequestMapping("/logoutByPhone")
	public @ResponseBody Json logoutByPhone(HttpSession session) {
		try {
			if (session != null) {
				session.invalidate();
			}
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, null, null);
		}
	}

	private List<ResourceVo> getMenuList(SessionUser sessionUser) {
		List<ResourceVo> userMenuBar = userService.getMenuList(sessionUser.getId());
		List<ResourceVo> list = new ArrayList<>();
		Collections.sort(list, (a, b) -> a.getSeq() - b.getSeq());
		for (ResourceVo resource : userMenuBar) {
			if (!StringUtils.hasText(resource.getResourceId())) {
				ResourceVo r = new ResourceVo();
				BeanUtils.copyProperties(resource, r);
				list.add(r);
			}
		}

		for (ResourceVo resource : userMenuBar) {
			if (StringUtils.hasText(resource.getResourceId())) {
				for (ResourceVo r : list) {
					if (r.getId().equals(resource.getResourceId())) {
						r.getResources().add(resource);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 跳转到查询数据页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager() {
		return "user/user";
	}

	/**
	 * 查询出用户数据
	 * 
	 * @param user
	 * @param ph
	 * @return
	 */
	@RequestMapping("/table")
	public @ResponseBody BootstrapTable table(UserVo vo, PageHelper ph) {
		BootstrapTable table = userService.table(vo, ph);
		return table;
	}
	
	/**
	 * 1.1 手机端登录-输入手机号，发送验证码, method = RequestMethod.POST
	 * @param user
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/sendVerify")
	public @ResponseBody Json sendVerify(@Validated(SendVerify.class) UserVo vo, BindingResult result) {		
		try {			
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			
			Boolean b = userService.hasPhone(vo);
			if(!b){
				userService.add(vo);
			}
			userService.sendVerify(vo);
			return new Json(true, null, b);
		} catch(ConstraintViolationException e){
			return new Json(false, "手机号重复!", null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	

	/**
	 * 1.2 手机端，输入验证码，登录, method = RequestMethod.POST
	 */
	@RequestMapping(value = "/loginByPhone")
	public @ResponseBody Json loginByPhone(@Validated(LoginByPhone.class) UserVo vo, BindingResult result, HttpSession session) {
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			
			UserVo user = userService.validPhoneAndVerify(vo);
			if (user == null) {
				throw new RuntimeException("手机号或验证码错误!");
			}
			
			SessionUser sessionUser = new SessionUser();
			BeanUtils.copyProperties(user, sessionUser);
			sessionUser.setResourceList(userService.getResourceList(user.getId()));
			sessionUser.setMenuList(getMenuList(sessionUser));
			session.setAttribute(SessionUtils.sessionUser, sessionUser);
			return new Json(true, null, user);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 2.2.2 查看个人资料
	 */
	@RequestMapping("/userInfo")
	public @ResponseBody UserVo userInfo(HttpSession session){
		String userId = SessionUtils.getUser(session).getId();
		UserVo vo = userService.getUserInfo(userId);
		return vo;
	}
	
	/**
	 * 2.2.3 查看个人详细资料
	 */
	@RequestMapping("/userDetail")
	public @ResponseBody UserVo userDetail(HttpSession session){
		String userId = SessionUtils.getUser(session).getId();
		UserVo vo = userService.getUserDetail(userId);
		return vo;
	}
	
	/**
	 * 2.2.4 编辑个人资料(只是更改资料)
	 * @param vo
	 * @param result
	 * @return
	 */
	@RequestMapping("/editUser")
	public @ResponseBody Json editUser(UserVo vo,HttpSession session){
		Json j = new Json();
		try {	
			
			vo.setId(SessionUtils.getUserId(session));
			userService.edit(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			j.setObj(false); 
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	/**
	 * 是否通过实名认证
	 * @param vo
	 * @param result
	 * @return
	 */
	@RequestMapping("/hasUserApprove")
	public @ResponseBody Json hasUserApprove(UserVo vo, BindingResult result,HttpSession session){		
		try {
			vo.setId(SessionUtils.getUser(session).getId());
			boolean b = userService.hasUserApprove(vo);			
			return new Json(true, null, b);
		} catch (Exception e) {
			return new Json(false, e.getMessage(), null);
		}	
	}
	
	/**
	 * 2.2.8  实名认证
	 * @param vo
	 * @param result
	 * @return
	 */
	@RequestMapping("/userApprove")
	public @ResponseBody Json userApprove(@Validated(UserApprove.class) UserVo vo, BindingResult result,HttpSession session){
		Json j = new Json();
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			vo.setId(SessionUtils.getUser(session).getId());							
			userService.userApprove(vo);
			j.setSuccess(true);											
		} catch (Exception e) {
			j.setObj(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}	
	
	/**
	 * 根据用户id获取车辆信息(车id，车型，车牌号，颜色)
	 * @param vo
	 * @return
	 */
	@RequestMapping("/getCarByUserId")
	public @ResponseBody CarVo getCarByUserId(UserVo vo,HttpSession session){
		
		CarVo cvo = userService.getCarByUserId(SessionUtils.getUser(session).getId());
		return cvo;
	}
	
	/**
	 * 添加紧急联系人
	 * @param vo
	 * @return
	 */
	@RequestMapping("/addSos")
	public @ResponseBody Json addSos(UserVo vo, HttpSession session){
		try {	
			vo.setId(SessionUtils.getUserId(session));
			userService.addSos(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 查询紧急联系人
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/getSos")
	public @ResponseBody UserVo getSos(UserVo vo, HttpSession session){
		vo.setId(SessionUtils.getUserId(session));
		return userService.getSos(vo);
	}
	
	/**
	 * 禁用
	 * @param id
	 * @return
	 */
	@RequestMapping("/enable")
	public @ResponseBody Json enableUser(String id){
		try {	
			userService.enableUser(id);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 启用
	 * @param id
	 * @return
	 */
	@RequestMapping("/able")
	public @ResponseBody Json ableUser(String id){
		try {	
			userService.ableUser(id);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 支付宝充值
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/alipayCashInInfo")
	public @ResponseBody String alipayCashInInfo(UserVo vo, HttpSession session){
		vo.setId(SessionUtils.getUserId(session));
		String orderInfo = userService.alipayCashInInfo(vo);
		return orderInfo;
	}

	private Map<String, String> getParameterMap(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, String> mapp = new LinkedHashMap<>();
		ArrayList<String> a = new ArrayList<>(parameterMap.keySet());
		Collections.sort(a);
 		for (String string : a) {
			mapp.put(string, parameterMap.get(string)[0]);
		}
		return mapp;
	}
	
	/**
	 * 支付宝充值
	 * @param vo
	 * @return
	 */
	@RequestMapping("/alipayCashIn")
	public @ResponseBody String alipayCashIn(Map<String, String> map, HttpServletRequest request) throws Exception{
		// 接收到支付宝的数据，验签
		Map<String, String> mapp = getParameterMap(request);
		boolean rsaCheckV2_1 = AlipaySignature.rsaCheckV1(mapp, AlipayAppUtils.getAliPublicKey() , "utf-8");
		if(rsaCheckV2_1){
			userService.alipayCashIn(mapp);																		
			return "success";
		} 
		return "false";
	}
	
	/**
	 * 微信充值
	 * @param vo
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechatPayCashInInfo")
	public @ResponseBody String wechatPayPassengerOrderInfo(UserVo vo, HttpSession session, HttpServletRequest request){
		vo.setId(SessionUtils.getUserId(session));
		vo.setIp(IpUtils.getIpAddr(request));
		String orderInfo = userService.wechatPayCashInInfo(vo);
		return orderInfo;
	}
	
	/**
	 * 微信充值
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/wechatPayCashIn")
	public @ResponseBody String wechatPayPassenger( HttpServletRequest request) throws Exception{
			 
	    String readInstream = readInstream(request.getInputStream());
		XmlMapper mapper = new XmlMapper();
		WechatPayNotify notify = mapper.readValue(readInstream, WechatPayNotify.class);
		if(!WechatPayAppUtils.valid(Long.parseLong(notify.getOut_trade_no()))){
			throw new RuntimeException("验证失败:无效的支付，如果有问题，请联系管理员");
		}
		userService.wechatPayCashIn(notify);
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}
	
	/**
	 * 提现提交后台审核
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/cashOutInfo")
	public @ResponseBody Json ableUser(UserVo vo, HttpSession session){
		try {	
			vo.setId(SessionUtils.getUserId(session));
			userService.cashOutInfo(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 跳转到查询提现数据页面
	 * @return
	 */
	@RequestMapping("/cashout")
	public String cashout() {
		return "user/cashout";
	}

	/**
	 * 查询出提现数据
	 * @param ph
	 * @return
	 */
	@RequestMapping("/cashouttable")
	public @ResponseBody BootstrapTable cashouttable(PageHelper ph) {
		BootstrapTable table = userService.tableOut(ph);
		return table;
	}
	
	/**
	 * 提现审核通过
	 * @param vo
	 * @return
	 */
	@RequestMapping("/cashOutSucc")
	public @ResponseBody Json cashOutSucc(JournalVo vo, HttpSession session){
		try {
			vo.setReviewerId(SessionUtils.getUserId(session));
			userService.cashOutSucc(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	@RequestMapping("/forgetPage")
	public String forgetPage(){
		return "user/userForget";
	}
	
	@RequestMapping("/userForget")
	public @ResponseBody Json userForget(UserVo vo){
		Json j = new Json();
		try {
			userService.userForget(vo);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMessage("密码修改失败：" + e.getMessage());
		}
		return j;
	}
	
	public static String readInstream(InputStream in) throws IOException {

        String encode = "utf-8";

        List<Byte> byteList = new LinkedList<>();

        try (ReadableByteChannel channel = Channels.newChannel(in)) {
            //Byte[] bytes = new Byte[0];
            ByteBuffer byteBuffer = ByteBuffer.allocate(9600);
            while (channel.read(byteBuffer) != -1) {
                byteBuffer.flip();//为读取做好准备

                while (byteBuffer.hasRemaining()) {
                    //builder.append((char)byteBuffer.get());
                    byteList.add(byteBuffer.get());
                }
                byteBuffer.clear();//为下一次写入做好准备
            }
        }
        Byte[] bytes = byteList.toArray(new Byte[byteList.size()]);
        byte[] bytes1 = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            bytes1[i] = bytes[i].byteValue();
        }
        return new String(bytes1, encode);
    }

}
