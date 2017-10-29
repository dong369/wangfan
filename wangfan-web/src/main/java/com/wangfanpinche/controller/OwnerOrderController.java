package com.wangfanpinche.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alipay.api.internal.util.AlipaySignature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.OwnerOrderService;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.web.IpUtils;
import com.wangfanpinche.vo.OwnerOrderVo;
import com.wangfanpinche.vo.OwnerOrderVo.OwnerRated;
import com.wangfanpinche.vo.OwnerOrderVo.Publish;
import com.wangfanpinche.vo.OwnerOrderVo.UserRated;
import com.wangfanpinche.vo.PassengerListVo;
import com.wangfanpinche.vo.PassengerOrderVo;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;


@Controller
@RequestMapping("/ownerorder")
public class OwnerOrderController {
	
	@Autowired
	public OwnerOrderService ownerOrderService;
	
	
	/**
	 * 是否有正在进行中车单 
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/hasRunningOrder")
	public @ResponseBody Json hasRunningOrder(OwnerOrderVo vo, HttpSession session){
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			boolean b = ownerOrderService.hasRunningOrder(vo);			
			return new Json(true, null, b);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 车主发布车单
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/publish")
	public @ResponseBody Json publish(@Validated(Publish.class) OwnerOrderVo vo, BindingResult result, HttpSession session){
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			vo.setUserId(SessionUtils.getUserId(session));
			Serializable id = ownerOrderService.publish(vo);
			return new Json(true, null, id);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 支付宝车主支付诚信必发
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/alipayHonestyOrderInfo")
	public @ResponseBody String alipayHonestyOrderInfo(OwnerOrderVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		String orderInfo = ownerOrderService.alipayHonestyInfo(vo);
		return orderInfo;
	}
	
	/**
	 * 支付宝车主支付诚信必发
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/alipayHonesty")
	public @ResponseBody String alipayHonesty(Map<String, String> map, HttpServletRequest request) throws Exception{
		// 接收到支付宝的数据，验签
		Map<String, String> mapp = getParameterMap(request);
		boolean rsaCheckV2_1 = AlipaySignature.rsaCheckV1(mapp, AlipayAppUtils.getAliPublicKey() , "utf-8");
		if(rsaCheckV2_1){
			ownerOrderService.alipayHonesty(mapp);																		
			return "success";
		} 
		return "false";
	}

	/**
	 * 微信车主支付诚信必发
	 * @param vo
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechatPayHonestyOrderInfo")
	public @ResponseBody String wechatPayHonestyOrderInfo(OwnerOrderVo vo, HttpSession session, HttpServletRequest request){
		vo.setUserId(SessionUtils.getUserId(session));
		vo.setIp(IpUtils.getIpAddr(request));
		String orderInfo = ownerOrderService.wechatPayHonestyInfo(vo);
		return orderInfo;
	}
	
	/**
	 * 微信车主支付诚信必发
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/wechatPayHonesty")
	public @ResponseBody String wechatPayHonesty( HttpServletRequest request) throws Exception{
			 
	    String readInstream = readInstream(request.getInputStream());
		//String readInstream = FileUtils.readFileToString(new File("d:/pay1.txt"), "UTF-8");
		XmlMapper mapper = new XmlMapper();
		WechatPayNotify notify = mapper.readValue(readInstream, WechatPayNotify.class);
		if(!WechatPayAppUtils.valid(Long.parseLong(notify.getOut_trade_no()))){
			throw new RuntimeException("验证失败:无效的支付，如果有问题，请联系管理员");
		}
		ownerOrderService.wechatPayHonesty(notify);
		return "success";
	}
	
	/**
	 * 乘客预定车单
	 * @param vo
	 * @param session
	 * @return
	 */
	/*
	@RequestMapping("/passengerReserve")
	public @ResponseBody Json passengerReserve(OwnerOrderVo vo, HttpSession session){
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			Serializable id = ownerOrderService.passengerReserve(vo);
			return new Json(true, null, id);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	*/
	
	/**
	 * 支付宝乘客支付车主车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/alipayPassengerOrderInfo")
	public @ResponseBody String alipayPassengerOrderInfo(OwnerOrderVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		String orderInfo = ownerOrderService.alipayPassengerOrderInfo(vo);
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
	 * 支付宝乘客支付车主车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/alipayPassenger")
	public @ResponseBody String alipayPassenger(Map<String, String> map, HttpServletRequest request) throws Exception{
		// 接收到支付宝的数据，验签
		Map<String, String> mapp = getParameterMap(request);
		boolean rsaCheckV2_1 = AlipaySignature.rsaCheckV1(mapp, AlipayAppUtils.getAliPublicKey() , "utf-8");
		if(rsaCheckV2_1){
			ownerOrderService.alipayPassenger(mapp);																		
			return "success";
		} 
		return "false";
	}
	
	
	/**
	 * 微信乘客支付车主车单
	 * @param vo
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechatPayPassengerOrderInfo")
	public @ResponseBody String wechatPayPassengerOrderInfo(OwnerOrderVo vo, HttpSession session, HttpServletRequest request){
		vo.setUserId(SessionUtils.getUserId(session));
		vo.setIp(IpUtils.getIpAddr(request));
		String orderInfo = ownerOrderService.wechatPayPassengerOrderInfo(vo);
		return orderInfo;
	}
	
	/**
	 * 微信乘客支付车主车单
	 * @param map
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/wechatPayPassenger")
	public @ResponseBody String wechatPayPassenger( HttpServletRequest request) throws Exception{
			 
	    String readInstream = readInstream(request.getInputStream());
		XmlMapper mapper = new XmlMapper();
		WechatPayNotify notify = mapper.readValue(readInstream, WechatPayNotify.class);
		if(!WechatPayAppUtils.valid(Long.parseLong(notify.getOut_trade_no()))){
			throw new RuntimeException("验证失败:无效的支付，如果有问题，请联系管理员");
		}
		ownerOrderService.wechatPayPassenger(notify);
		return "success";
	}
	
	/**
	 * 车主同意搭载乘客
	 * @param vo
	 * @return
	 */
	@RequestMapping("/approvePassenger")
	public @ResponseBody Json approvePassenger(OwnerOrderVo vo){
		try {			
			ownerOrderService.approvePassenger(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 车主拒绝搭载乘客
	 * @param vo
	 * @return
	 */
	@RequestMapping("/disapprovePassenger")
	public @ResponseBody Json disapprovePassenger(OwnerOrderVo vo){
		try {			
			ownerOrderService.disapprovePassenger(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 乘客列表
	 * @param vo
	 * @return
	 */
	@RequestMapping("/passengerList")
	public @ResponseBody PassengerListVo passengerList(OwnerOrderVo vo){
		return ownerOrderService.passengerList(vo);
	}
	
	/**
	 * 出发接乘客
	 * @param vo
	 * @return
	 */
	@RequestMapping("/receive")
	public @ResponseBody Json receive(OwnerOrderVo vo){
		try {			
			ownerOrderService.receive(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 乘客上车-接到某个乘客
	 * @param vo
	 * @return
	 */
	@RequestMapping("/userReceive")
	public @ResponseBody Json userReceive(OwnerOrderVo vo){
		try {			
			ownerOrderService.userReceive(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}		
	
	/**
	 * 出发送乘客
	 * @param vo
	 * @return
	 */
	@RequestMapping("/depart")
	public @ResponseBody Json depart(OwnerOrderVo vo){
		try {			
			ownerOrderService.depart(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 乘客完成(某个乘客下车)	
	 * @param vo
	 * @return
	 */
	@RequestMapping("/userFinish")
	public @ResponseBody Json userFinish(OwnerOrderVo vo, HttpSession session){
		try {		
			vo.setUserId(SessionUtils.getUserId(session));
			ownerOrderService.userFinish(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 乘客评价车主
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/userRated")
	public @ResponseBody Json userRated(@Validated(UserRated.class) OwnerOrderVo vo, BindingResult result, HttpSession session){
		try {		
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			
			vo.setUserId(SessionUtils.getUserId(session));
			ownerOrderService.userRated(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}	
	
	/**
	 * 车主完成
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/finish")
	public @ResponseBody Json finish(OwnerOrderVo vo, HttpSession session){
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			ownerOrderService.finish(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 车主评价乘客
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/ownerRated")
	public @ResponseBody Json ownerRated(@Validated(OwnerRated.class) OwnerOrderVo vo, BindingResult result, HttpSession session){
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			
			vo.setUserId(SessionUtils.getUserId(session));
			ownerOrderService.ownerRated(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 乘客查询车主车单列表(分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/ownerOrderlist")
	public @ResponseBody List<OwnerOrderVo> ownerOrderlist(OwnerOrderVo vo, PageHelper ph){
		return ownerOrderService.ownerOrderlist(vo, ph);
	}
	
	/**
	 * 用户的车主行程记录
	 * @param vo
	 * @return
	 */
	@RequestMapping("/ownerStrokeRecord")
	public @ResponseBody List<OwnerOrderVo> ownerStrokeRecord(OwnerOrderVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		return ownerOrderService.ownerStrokeRecord(vo);
	}
	
	/**
	 * 车主查看乘客信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("/ownerfindPassInfo")
	public @ResponseBody OwnerOrderVo ownerfindPassInfo(OwnerOrderVo vo){
		return ownerOrderService.ownerfindPassInfo(vo);
	}
	
	/**
	 * 乘客查看车主信息
	 * @param vo
	 * @return
	 */
	@RequestMapping("/passfindOwnerInfo")
	public @ResponseBody OwnerOrderVo passfindOwnerInfo(OwnerOrderVo vo){
		return ownerOrderService.passfindOwnerInfo(vo);
	}
	
	/**
	 * 车主查看正在进行中车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/running")
	public @ResponseBody PassengerListVo running(OwnerOrderVo vo){
		return ownerOrderService.running(vo);
	}
	
	/**
	 *
	 * 根据用户id返回publish的车单信息(id,开始城市，开始城市描述，结束城市，结束城市描述，出发时间，剩余座位数)
	 * @param vo
	 * @return
	 */
	@RequestMapping("/getOwnerOrderByUserId")
	public @ResponseBody List<OwnerOrderVo> getOwnerOrderByUserId(OwnerOrderVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		return ownerOrderService.getOwnerOrderByUserId(vo);
	}
	
	/**
	 * 乘客查询车主车单详情
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/ownerOrderInfo")
	public @ResponseBody OwnerOrderVo ownerOrderInfo(OwnerOrderVo vo){
		return ownerOrderService.ownerOrderInfo(vo);
	}
	
	/**
	 * 给乘客推荐的车主车单列表
	 * @param vo
	 * @return
	 */
	@RequestMapping("/recommendOwner")
	public @ResponseBody List<OwnerOrderVo> recommendOwner(PassengerOrderVo vo){
		return ownerOrderService.recommendOwner(vo);
	}
	
    public static String readInstream(InputStream in) throws IOException {

        String encode = "utf-8";

        List<Byte> byteList = new LinkedList<>();

        try (ReadableByteChannel channel = Channels.newChannel(in)) {
            Byte[] bytes = new Byte[0];
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
