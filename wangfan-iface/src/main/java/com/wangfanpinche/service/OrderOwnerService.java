package com.wangfanpinche.service;

import java.util.List;
import java.util.Map;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.order.OrderOwnerVo;

public interface OrderOwnerService {

	/**
	 * 个人中心-车主车单列表
	 * @param vo
	 * @return
	 */
	List<OrderOwnerVo> listAll(OrderOwnerVo vo);

	/**
	 * 首页-根据位置列出车单
	 * @param vo
	 * @param ph 
	 * @return
	 */
	List<OrderOwnerVo> listByFromLocation(OrderOwnerVo vo, PageHelper ph);
	
	/**
	 * 推荐车主车单
	 * @param vo
	 * @return
	 */
	List<OrderOwnerVo> listByLocation(OrderOwnerVo vo);

	/**
	 * 所有发布的
	 * @param vo
	 * @return
	 */
	List<OrderOwnerVo> listByPublish(OrderOwnerVo vo);

	/**
	 * 正在进行中的车单
	 * @param vo
	 * @return
	 */
	OrderOwnerVo listByRunning(OrderOwnerVo vo);

	/**
	 * 车单详情
	 * @param vo
	 * @return
	 */
	OrderOwnerVo detail(OrderOwnerVo vo);

	/**
	 * 发布车单
	 * @param vo
	 * @return
	 */
	String publish(OrderOwnerVo vo);

	/**
	 * 车主信息
	 * @param vo
	 * @return
	 */
	OrderOwnerVo getUserinfo(OrderOwnerVo vo);
	
	/**
	 * 诚信必发--微信下订单前的准备信息
	 * @param vo
	 * @return
	 */
	String honestyWechatPayPreOrder(OrderOwnerVo vo);
	
	/**
	 * 诚信必发--微信 异步响应
	 * @param notify
	 */
	void honestyWechatPayNotify(WechatPayNotify notify);
	
	/**
	 * 车主邀请乘客搭载
	 * @param vo
	 */
	void invitationPassenger(OrderOwnerVo vo);

	/**
	 * 诚信必发-支付宝准备信息
	 * @param vo
	 * @return
	 */
	String honestyAliPayPreOrder(OrderOwnerVo vo);

	/**
	 * 诚信必发-支付宝成功的回调
	 * @param mapp
	 */
	void honestyAliPayNotify(Map<String, String> mapp);
	
	/**
	 * 车主同意乘客搭载
	 * @param vo
	 */
	void approvePassenger(OrderOwnerVo vo);
	
	/**
	 * 车主拒绝乘客搭载
	 * @param vo
	 */
	void disapprovePassenger(OrderOwnerVo vo);
	
	/**
	 * 乘客列表
	 * @param vo
	 * @return
	 */
	OrderOwnerVo passengerList(OrderOwnerVo vo);

	/**
	 * 出发接乘客
	 * @param vo
	 */
	void receive(OrderOwnerVo vo);
	
	/**
	 * 出发送乘客
	 * @param vo
	 */
	void depart(OrderOwnerVo vo);
	
	/**
	 * 车主车单完成
	 * @param vo
	 */
	void finish(OrderOwnerVo vo);
	
	/**
	 * 车主评价乘客
	 * @param vo
	 */
	void evaluate(OrderOwnerVo vo);
	
	/**
	 * 车主关闭车单
	 * @param vo
	 */
	void close(OrderOwnerVo vo);
	
	/**
	 * 车主提醒乘客支付 
	 */
	void warn(OrderOwnerVo vo);
}
