package com.wangfanpinche.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.wangfanpinche.service.OrderPassengerService;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.web.IpUtils;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.order.OrderPassengerVo;
import com.wangfanpinche.vo.order.OrderPassengerVo.PassToOwnerevaluate;
import com.wangfanpinche.vo.order.OrderPassengerVo.Publish;

@Controller
@RequestMapping("/OrderPassenger")
public class OrderPassengerController {

	@Autowired
	OrderPassengerService orderPassengerService;

	/**
	 * 列出某个人所有的车单
	 * 
	 * @return
	 */
	@RequestMapping("/listAll")
	public @ResponseBody List<OrderPassengerVo> listAll(OrderPassengerVo vo, HttpSession session) {
		vo.setUserId(SessionUtils.getUserId(session));
		return orderPassengerService.listAll(vo);
	}

	/**
	 * 列出本人的所有未出行的行程
	 * 
	 * @return
	 */
	@RequestMapping("/listByPublish")
	public @ResponseBody List<OrderPassengerVo> listByPublish(OrderPassengerVo vo, HttpSession session) {
		vo.setUserId(SessionUtils.getUserId(session));
		return orderPassengerService.listByPublish(vo);
	}

	/**
	 * 根据起始位置。列出所有的乘客车单。
	 * 
	 * @return
	 */
	@RequestMapping("/listByFromLocation")
	public @ResponseBody List<OrderPassengerVo> listByFromLocation(OrderPassengerVo vo, PageHelper ph) {
		return orderPassengerService.listByFromLocation(vo, ph);
	}

	/**
	 * 推荐
	 * 
	 * @return
	 */
	@RequestMapping("/listByLocation")
	public @ResponseBody List<OrderPassengerVo> listByLocation(OrderPassengerVo vo) {
		return orderPassengerService.listByLocation(vo);
	}

	/**
	 * 正在进行中的行程。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/listByRunning")
	public @ResponseBody OrderPassengerVo listByRunning(OrderPassengerVo vo, HttpSession session) {
		vo.setUserId(SessionUtils.getUserId(session));
		OrderPassengerVo running = orderPassengerService.listByRunning(vo);
		return running;
	}

	/**
	 * 乘客上车
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/receive")
	public @ResponseBody Json receive(OrderPassengerVo vo) {
		try {
			orderPassengerService.receive(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 乘客下车
	 * 
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/finish")
	public @ResponseBody Json finish(OrderPassengerVo vo) {
		try {
			orderPassengerService.finish(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 乘客评价车主
	 * 
	 * @return
	 */
	@RequestMapping("/evaluate")
	public @ResponseBody Json evaluate(@Validated(PassToOwnerevaluate.class) OrderPassengerVo vo, BindingResult result, HttpSession session) {
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			vo.setUserId(SessionUtils.getUserId(session));
			orderPassengerService.evaluate(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 计算乘客需要支付的车费
	 * @param vo
	 * @return
	 */
	@RequestMapping("/passPayMoney")
	public @ResponseBody Json passPayMoney(OrderPassengerVo vo) {
		try {			
			BigDecimal money = orderPassengerService.passPayMoney(vo);			
			return new Json(true, null, money);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 乘客车单 支付宝支付前的准备信息
	 * 
	 * @return
	 */
	@RequestMapping("/orderPassengerAliPayPreOrder")
	public @ResponseBody String orderPassengerAliPayPreOrder(OrderPassengerVo vo, HttpSession session) {
		vo.setUserId(SessionUtils.getUserId(session));
		String orderInfo = orderPassengerService.orderPassengerAliPayPreOrder(vo);
		return orderInfo;

	}

	/**
	 * 乘客车单 支付宝支付后的异步请求
	 * 
	 * @return
	 */
	@RequestMapping("/orderPassengerAliPayNotify")
	public @ResponseBody String orderPassengerAliPayNotify(Map<String, String> map, HttpServletRequest request) throws Exception {
		// 接收到支付宝的数据，验签
		Map<String, String> mapp = getParameterMap(request);
		boolean rsaCheckV2_1 = AlipaySignature.rsaCheckV1(mapp, AlipayAppUtils.getAliPublicKey(), "utf-8");
		if (rsaCheckV2_1) {
			orderPassengerService.orderPassengerAliPayNotify(mapp);
			return "success";
		}
		return "false";
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
	 * 乘客车单 微信支付前的准备信息
	 * 
	 * @return
	 */
	@RequestMapping("/orderPassengerWechatPayPreOrder")
	public @ResponseBody String orderPassengerWechatPayPreOrder(OrderPassengerVo vo, HttpServletRequest request, HttpSession session) {
		vo.setIp(IpUtils.getIpAddr(request));
		vo.setUserId(SessionUtils.getUserId(session));
		String orderInfo = orderPassengerService.orderPassengerWechatPayPreOrder(vo);
		return orderInfo;
	}

	/**
	 * 乘客车单 微信支付前的准备信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/orderPassengerWechatPayNotify")
	public @ResponseBody String orderPassengerWechatPayNotify(HttpServletRequest request) throws Exception {
		String readInstream = readInstream(request.getInputStream());
		XmlMapper mapper = new XmlMapper();
		WechatPayNotify notify = mapper.readValue(readInstream, WechatPayNotify.class);
		if (!WechatPayAppUtils.valid(Long.parseLong(notify.getOut_trade_no()))) {
			throw new RuntimeException("验证失败:无效的支付，如果有问题，请联系管理员");
		}
		orderPassengerService.orderPassengerWechatPayNotify(notify);
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}

	/**
	 * 乘客预定车单
	 */
	@RequestMapping("/reserve")
	public @ResponseBody Json reserve(OrderPassengerVo vo, HttpSession session) {
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			orderPassengerService.passengerReserve(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 车单详情
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public @ResponseBody OrderPassengerVo detail(OrderPassengerVo vo) {
		return orderPassengerService.detail(vo);
	}

	/**
	 * 乘客信息
	 */
	@RequestMapping("/userinfo")
	public @ResponseBody OrderPassengerVo userinfo(OrderPassengerVo vo) {
		return orderPassengerService.userinfo(vo);
	}

	/**
	 * 乘客发布
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/publish")
	public @ResponseBody Json publish(@Validated(Publish.class) OrderPassengerVo vo, BindingResult result, HttpSession session) {
		try {
			if(result.hasErrors()){
				throw new RuntimeException(result.getFieldErrors().get(0).getDefaultMessage());
			}
			
			vo.setUserId(SessionUtils.getUserId(session));
			String id = orderPassengerService.publish(vo);
			return new Json(true, null, id);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, null, null);
		}
	}

	/**
	 * 乘客同意坐司机的车
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/approveOwner")
	public @ResponseBody Json approveOwner(OrderPassengerVo vo) {
		try {
			orderPassengerService.approveOwner(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 乘客-查看车主抢单列表,同意的车主车单
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/ownerList")
	public @ResponseBody OrderPassengerVo ownerList(OrderPassengerVo vo) {
		return orderPassengerService.ownerList(vo);
	}
	
	/**
	 * 乘客查看自己车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/show")
	public @ResponseBody OrderPassengerVo show(OrderPassengerVo vo) {
		return orderPassengerService.show(vo);
	}
	
	/**
	 * 乘客关闭车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/close")
	public @ResponseBody Json close(OrderPassengerVo vo) {
		try {
			orderPassengerService.close(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 乘客提醒车主出发
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/warn")
	public @ResponseBody Json warn(OrderPassengerVo vo, HttpSession session) {
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			orderPassengerService.warn(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	public static String readInstream(InputStream in) throws IOException {

		String encode = "utf-8";

		List<Byte> byteList = new LinkedList<>();

		try (ReadableByteChannel channel = Channels.newChannel(in)) {
			// Byte[] bytes = new Byte[0];
			ByteBuffer byteBuffer = ByteBuffer.allocate(9600);
			while (channel.read(byteBuffer) != -1) {
				byteBuffer.flip();// 为读取做好准备

				while (byteBuffer.hasRemaining()) {
					// builder.append((char)byteBuffer.get());
					byteList.add(byteBuffer.get());
				}
				byteBuffer.clear();// 为下一次写入做好准备
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
