package com.wangfanpinche.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.alipay.api.internal.util.AlipaySignature;
import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.service.OrderOwnerService;
import com.wangfanpinche.utils.pay.WechatPayAppUtils;
import com.wangfanpinche.utils.web.IpUtils;
import com.wangfanpinche.utils.pay.AlipayAppUtils;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.Json;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.order.OrderOwnerVo;

@Controller
@RequestMapping("/OrderOwner")
public class OrderOwnerController {

	@Autowired
	private OrderOwnerService orderOwnerService;

	/**
	 * 个人中心----车主行程记录
	 * 
	 * @return
	 */
	@RequestMapping("/listAll")
	public @ResponseBody List<OrderOwnerVo> listAll(OrderOwnerVo vo, HttpSession session) {
		vo.setUserId(SessionUtils.getUserId(session));
		List<OrderOwnerVo> list = orderOwnerService.listAll(vo);
		return list;
	}

	/**
	 * 首页地图-附近的车主
	 * 
	 * @return
	 */
	@RequestMapping("/listByFromLocation")
	public @ResponseBody List<OrderOwnerVo> listByFromLocation(OrderOwnerVo vo, PageHelper ph) {
		List<OrderOwnerVo> list = orderOwnerService.listByFromLocation(vo, ph);
		return list;
	}

	/**
	 * 首页地图-开始和结束位置寻找车单
	 * 
	 * @return
	 */
	@RequestMapping("/listByLocation")
	public @ResponseBody List<OrderOwnerVo> listByLocation(OrderOwnerVo vo) {
		List<OrderOwnerVo> list = orderOwnerService.listByLocation(vo);
		return list;
	}

	/**
	 * 快捷进入---未出行的车单
	 * 
	 * @return
	 */
	@RequestMapping("/listByPublish")
	public @ResponseBody List<OrderOwnerVo> listByPublish(OrderOwnerVo vo, HttpSession session) {
		vo.setUserId(SessionUtils.getUserId(session));
		List<OrderOwnerVo> list = orderOwnerService.listByPublish(vo);
		return list;
	}

	/**
	 * 快捷进入---进行中的车单
	 * 
	 * @return
	 */
	@RequestMapping("/listByRunning")
	public @ResponseBody OrderOwnerVo listByRunning(OrderOwnerVo vo, HttpSession session) {
		vo.setUserId(SessionUtils.getUserId(session));
		OrderOwnerVo result = orderOwnerService.listByRunning(vo);
		return result;
	}

	/**
	 * 车单详情
	 * 
	 * @return
	 */
	@RequestMapping("/detail")
	public @ResponseBody OrderOwnerVo detail(OrderOwnerVo vo) {
		OrderOwnerVo result = orderOwnerService.detail(vo);
		return result;
	}

	/**
	 * 车主信息
	 */
	@RequestMapping("/userinfo")
	public @ResponseBody OrderOwnerVo userinfo(OrderOwnerVo vo) {
		OrderOwnerVo result = orderOwnerService.getUserinfo(vo);
		return result;
	}

	/**
	 * 发布车单
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/publish")
	public @ResponseBody Json publish(OrderOwnerVo vo, HttpSession session) {
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			String id = orderOwnerService.publish(vo);
			return new Json(true, "发布成功!", id);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, "发布失败!", null);
		}
	}

	/**
	 * 诚信必发--支付宝下订单前的准备信息
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/honestyAliPayPreOrder")
	public @ResponseBody String honestyAliPayPreOrder(OrderOwnerVo vo) {
		String orderInfo = orderOwnerService.honestyAliPayPreOrder(vo);
		return orderInfo;
	}

	/**
	 * 诚信必发--支付宝 异步响应
	 * 
	 * @return
	 */
	@RequestMapping("/honestyAliPayNotify")
	public @ResponseBody String honestyAliPayNotify(Map<String, String> map, HttpServletRequest request) throws Exception {
		// 接收到支付宝的数据，验签
		Map<String, String> mapp = getParameterMap(request);
		boolean rsaCheckV2_1 = AlipaySignature.rsaCheckV1(mapp, AlipayAppUtils.getAliPublicKey(), "utf-8");
		if (rsaCheckV2_1) {
			orderOwnerService.honestyAliPayNotify(mapp);
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
	 * 诚信必发--微信下订单前的准备信息
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/honestyWechatPayPreOrder")
	public @ResponseBody String honestyWechatPayPreOrder(OrderOwnerVo vo, HttpServletRequest request) {
		vo.setIp(IpUtils.getIpAddr(request));
		String orderInfo = orderOwnerService.honestyWechatPayPreOrder(vo);
		return orderInfo;
	}

	/**
	 * 诚信必发--微信 异步响应
	 * 
	 * @return
	 */
	@RequestMapping("/honestyWechatPayNotify")
	public @ResponseBody String honestyWechatPayNotify(HttpServletRequest request) throws Exception {
		String readInstream = readInstream(request.getInputStream());
		// String readInstream = FileUtils.readFileToString(new
		// File("d:/pay1.txt"), "UTF-8");
		XmlMapper mapper = new XmlMapper();
		WechatPayNotify notify = mapper.readValue(readInstream, WechatPayNotify.class);
		if (!WechatPayAppUtils.valid(Long.parseLong(notify.getOut_trade_no()))) {
			throw new RuntimeException("验证失败:无效的支付，如果有问题，请联系管理员");
		}
		orderOwnerService.honestyWechatPayNotify(notify);
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}

	/**
	 * 车主同意搭载乘客
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/approvePassenger")
	public @ResponseBody Json approvePassenger(OrderOwnerVo vo) {
		try {
			orderOwnerService.approvePassenger(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 车主邀请乘客搭载
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/invitationPassenger")
	public @ResponseBody Json invitationPassenger(OrderOwnerVo vo, HttpSession session) {
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			orderOwnerService.invitationPassenger(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 车主拒绝搭载乘客
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/disapprovePassenger")
	public @ResponseBody Json disapprovePassenger(OrderOwnerVo vo) {
		try {
			orderOwnerService.disapprovePassenger(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 乘客列表
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/passengerList")
	public @ResponseBody OrderOwnerVo passengerList(OrderOwnerVo vo) {
		return orderOwnerService.passengerList(vo);
	}

	/**
	 * 开始接乘客
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/receive")
	public @ResponseBody Json receive(OrderOwnerVo vo) {
		try {
			orderOwnerService.receive(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 出发送乘客
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/depart")
	public @ResponseBody Json depart(OrderOwnerVo vo) {
		try {
			orderOwnerService.depart(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 车单完成
	 * 
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/finish")
	public @ResponseBody Json finish(OrderOwnerVo vo, HttpSession session) {
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			orderOwnerService.finish(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 车主评价乘客
	 * 
	 * @return
	 */
	@RequestMapping("/evaluate")
	public @ResponseBody Json evaluate(OrderOwnerVo vo, HttpSession session) {
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			orderOwnerService.evaluate(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}
	
	/**
	 * 车主关闭车单
	 * @param vo
	 * @return
	 */
	@RequestMapping("/close")
	public @ResponseBody Json close(OrderOwnerVo vo) {
		try {
			orderOwnerService.close(vo);
			return new Json(true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return new Json(false, e.getMessage(), null);
		}
	}

	/**
	 * 车主提醒乘客支付
	 * @param vo
	 * @param session
	 * @return
	 */
	@RequestMapping("/warn")
	public @ResponseBody Json warn(OrderOwnerVo vo, HttpSession session) {
		try {
			vo.setUserId(SessionUtils.getUserId(session));
			orderOwnerService.warn(vo);
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
