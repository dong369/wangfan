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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alipay.api.internal.util.AlipaySignature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.PassengerOrderService;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.web.IpUtils;
import com.wangfanpinche.vo.OwnerOrderVo;
import com.wangfanpinche.vo.PassengerOrderVo;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.PassengerOrderVo.Publish;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;

/**
 * 乘客车单Service
 * @author Thanatos
 * @date 2016-11-29
 */
@Controller
@RequestMapping("/passengerorder")
public class PassengerOrderController {
	
	@Autowired
	private PassengerOrderService passengerOrderService;

	
	/**
	 * 乘客发布车单
	 * @param vo
	 * @return
	 */	
	@RequestMapping("/publish")
	public @ResponseBody Json publish(@Validated(Publish.class) PassengerOrderVo vo, BindingResult result, HttpSession session){
		Json j = new Json();
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			
			vo.setUserId(SessionUtils.getUserId(session));
			String s = passengerOrderService.publish(vo);
			j.setSuccess(true);
			j.setObj(s);
		} catch (Exception e) {
			e.printStackTrace();

			j.setSuccess(false);
			j.setMessage(e.getMessage());
		}
		return j;
	}
	
	/**
	 * 支付宝乘客支付乘客车单
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/alipayPassengerOrderInfo")
	public @ResponseBody String alipayPassengerOrderInfo(OwnerOrderVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		String orderInfo = passengerOrderService.alipayPassengerOrderInfo(vo);
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
	 * 支付宝乘客支付乘客车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/alipayPassenger")
	public @ResponseBody String alipayPassenger(Map<String, String> map, HttpServletRequest request) throws Exception{
		// 接收到支付宝的数据，验签
		Map<String, String> mapp = getParameterMap(request);
		boolean rsaCheckV2_1 = AlipaySignature.rsaCheckV1(mapp, AlipayAppUtils.getAliPublicKey() , "utf-8");
		if(rsaCheckV2_1){
			passengerOrderService.alipayPassenger(mapp);																		
			return "success";
		} 
		return "false";
	}	
	
	/**
	 * 微信乘客支付乘客车单
	 * @param vo
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/wechatPayPassengerOrderInfo")
	public @ResponseBody String wechatPayPassengerOrderInfo(OwnerOrderVo vo, HttpSession session, HttpServletRequest request){
		vo.setUserId(SessionUtils.getUserId(session));
		vo.setIp(IpUtils.getIpAddr(request));
		String orderInfo = passengerOrderService.wechatPayPassengerOrderInfo(vo);
		return orderInfo;
	}
	
	/**
	 * 微信乘客支付乘客车单
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
		passengerOrderService.wechatPayPassenger(notify);
		return "success";
	}
	
	/**
	 * 车主查看乘客车单详情
	 * @param vo
	 * @return
	 */
	@RequestMapping("/passOrderInfo")
	public @ResponseBody PassengerOrderVo passOrderInfo(PassengerOrderVo vo){
		return passengerOrderService.passOrderInfo(vo);
	}
	
	/**
	 * 司机抢单(乘客被动)
	 * @param vo
	 * @return
	 */
	@RequestMapping("/receive")
	public @ResponseBody Json receive(PassengerOrderVo vo, HttpSession session){
		try {
			//车主的用户Id
			vo.setUserId(SessionUtils.getUserId(session));
			String id = passengerOrderService.receive(vo);
			return new Json(true, null, id);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);			
		}
	}
	
	/**
	 * 同意乘坐某辆车(乘客被动)
	 * @param vo
	 * @return
	 */
	@RequestMapping("/approve")
	public @ResponseBody Json aprove(PassengerOrderVo vo){
		try {
			String id = passengerOrderService.aprove(vo);
			return new Json(true, null, id);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);	
		}
	}
	
	/**
	 * 乘客预定车主车单(乘客主动)
	 * @param vo
	 * @return
	 */
	@RequestMapping("/passfindOwner")
	public @ResponseBody Json passfindOwner(PassengerOrderVo vo){
		try {
			String publishId = passengerOrderService.passfindOwner(vo);
			return new Json(true, null, publishId);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);	
		}
	}
	
	/**
	 * 查看当前的车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/show")
	public @ResponseBody PassengerOrderVo show(PassengerOrderVo vo){
		return passengerOrderService.show(vo);
	}
	
	/**
	 * 车主查询乘客车单列表(分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	@RequestMapping("/passOrderlist")
	public @ResponseBody List<PassengerOrderVo> passOrderlist(PassengerOrderVo vo, PageHelper ph){
		return passengerOrderService.passOrderlist(vo, ph);
	}
	
	/**
	 * 用户的乘客行程记录
	 * @param vo
	 * @return
	 */
	@RequestMapping("/passStrokeRecord") 
	public @ResponseBody List<PassengerOrderVo> passStrokeRecord(PassengerOrderVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		return passengerOrderService.passStrokeRecord(vo);
	}
	
	/**
	 * 给车主推荐的乘客车单列表
	 * @param vo
	 * @return
	 */
	@RequestMapping("/recommendPass") 
	public @ResponseBody List<PassengerOrderVo> recommendPass(OwnerOrderVo vo){		
		return passengerOrderService.recommendPass(vo);
	}
	
	/**
	 * 根据用户id返回publish的乘客车单
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/getPassOrderByUserId") 
	public @ResponseBody List<PassengerOrderVo> getPassOrderByUserId(PassengerOrderVo vo, HttpSession session){
		vo.setUserId(SessionUtils.getUserId(session));
		return passengerOrderService.getPassOrderByUserId(vo);
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
