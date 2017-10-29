package com.wangfanpinche.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;
import com.wangfanpinche.vo.order.OrderPassengerVo;

public interface OrderPassengerService {

	/**
	 * 列出某个人所有的乘客车单
	 * @param vo
	 * @return
	 */
	List<OrderPassengerVo> listAll(OrderPassengerVo vo);

	/**
	 * 列出本人的所有未出行的行程
	 * @param vo
	 * @return
	 */
	List<OrderPassengerVo> listByPublish(OrderPassengerVo vo);

	/**
	 * 首页-根据开始位置。列出所有的乘客车单
	 * @param vo
	 * @param ph
	 * @return
	 */
	List<OrderPassengerVo> listByFromLocation(OrderPassengerVo vo, PageHelper ph);
	
	/**
	 * 推荐乘客车单
	 * @param vo
	 * @return
	 */
	List<OrderPassengerVo> listByLocation(OrderPassengerVo vo);
	
	/**
	 * 查询抢单车主
	 * @param vo
	 * @return
	 */
	OrderPassengerVo ownerList(OrderPassengerVo vo);
	
	/**
	 * 正在进行中的行程。
	 * @param vo
	 * @return
	 */
	OrderPassengerVo listByRunning(OrderPassengerVo vo);
	
	/**
	 * 乘客上车
	 * @param vo
	 */
	void receive(OrderPassengerVo vo);
	
	/**
	 * 乘客下车
	 * @param vo
	 */
	void finish(OrderPassengerVo vo);
	
	/**
	 * 乘客评价车主
	 * @param vo
	 */
	void evaluate(OrderPassengerVo vo);
	
	/**
	 * 乘客预定
	 * @param vo
	 */
	void passengerReserve(OrderPassengerVo vo);
	
	/**
	 * 乘客车单详情
	 * @param vo
	 * @return
	 */
	OrderPassengerVo detail(OrderPassengerVo vo);
	
	/**
	 * 乘客信息
	 * @param vo
	 * @return
	 */
	OrderPassengerVo userinfo(OrderPassengerVo vo);
	
	/**
	 * 乘客发布
	 * @param vo
	 * @return
	 */
	String publish(OrderPassengerVo vo);
	
	/**
	 * 乘客同意坐司机的车
	 * @param vo
	 */
	void approveOwner(OrderPassengerVo vo);
	
	/**
	 * 计算乘客需要支付的车费
	 * @param vo
	 * @return
	 */
	BigDecimal passPayMoney(OrderPassengerVo vo);
	
	String orderPassengerAliPayPreOrder(OrderPassengerVo vo);
	
	void orderPassengerAliPayNotify(Map<String, String> map);	
	
	String orderPassengerWechatPayPreOrder(OrderPassengerVo vo);
	
	void orderPassengerWechatPayNotify(WechatPayNotify notify);
	
	/**
	 * 乘客查看自己车单
	 * @param vo
	 * @return
	 */
	OrderPassengerVo show(OrderPassengerVo vo);
	
	/**
	 * 乘客关闭车单
	 * @param vo
	 */
	void close(OrderPassengerVo vo);
	
	/**
	 * 乘客提醒车主出发 
	 */
	void warn(OrderPassengerVo vo);

}
