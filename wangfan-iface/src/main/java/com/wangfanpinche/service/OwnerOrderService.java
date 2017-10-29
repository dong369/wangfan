package com.wangfanpinche.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wangfanpinche.vo.OwnerOrderVo;
import com.wangfanpinche.vo.PassengerListVo;
import com.wangfanpinche.vo.PassengerOrderVo;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;

public interface OwnerOrderService {

	/**
	 * 首页乘客查看车主车单列表(分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	public List<OwnerOrderVo> ownerOrderlist(OwnerOrderVo vo, PageHelper ph);
	
	/**
	 * 用户的车主行程记录
	 * @param vo
	 * @return
	 */
	public List<OwnerOrderVo> ownerStrokeRecord(OwnerOrderVo vo);
	
	/**
	 * 是否有正在进行中车单
	 * @param vo
	 * @return
	 */
	public Boolean hasRunningOrder(OwnerOrderVo vo);

	/**
	 * 通过路线的距离和油价，过路费，油耗，进行计算价格
	 * @param vo
	 * @return
	 */
	public BigDecimal carSalary(OwnerOrderVo vo);

	/**
	 * 车主发布车单，返回ID
	 * @param order
	 * @return
	 */
	public Serializable publish(OwnerOrderVo vo);

	/**
	 * 支付宝车主支付诚信必发
	 * @return 
	 */
	public String alipayHonestyInfo(OwnerOrderVo vo);
	
	/**
	 * 支付宝车主支付诚信必发
	 */
	public void alipayHonesty(Map<String, String> map);
	
	/**
	 * 微信车主支付诚信必发
	 * @param vo
	 * @return
	 */
	public String wechatPayHonestyInfo(OwnerOrderVo vo);
	
	/**
	 * 微信车主支付诚信必发
	 */
	public void wechatPayHonesty(WechatPayNotify notify);
	
	/**
	 * 乘客预定某个车位
	 * @param passenger
	 */
	//public Serializable passengerReserve(OwnerOrderVo vo);	
	
	/**
	 * 支付宝乘客支付车主车单
	 * @param vo
	 * @return
	 */
	public String alipayPassengerOrderInfo(OwnerOrderVo vo);
	
	/**
	 * 支付宝乘客支付车主车单
	 * @return
	 */
	public void alipayPassenger(Map<String, String> map);
	
	/**
	 * 微信乘客支付车主车单
	 * @param vo
	 * @return
	 */
	public String wechatPayPassengerOrderInfo(OwnerOrderVo vo);
	
	/**
	 * 微信乘客支付车主车单
	 * @param notify
	 */
	public void wechatPayPassenger(WechatPayNotify notify);

	/**
	 * 同意乘客搭载
	 * @param vo
	 */
	public void approvePassenger(OwnerOrderVo vo);
	
	/**
	 * 拒绝乘客搭载
	 * @param vo
	 */
	public void disapprovePassenger(OwnerOrderVo vo);
	
	/**
	 * 乘客取消
	 * @param vo
	 */
	public void passengerClosed(OwnerOrderVo vo);
	
	/**
	 * 司机取消
	 * @param vo
	 */
	public void ownerClosed(OwnerOrderVo vo);
	
	/**
	 * 乘客列表
	 * @param vo
	 * @return
	 */
	public PassengerListVo passengerList(OwnerOrderVo vo);
	
	/**
	 * 接客（开始准备接人）
	 * @param vo
	 */
	public void receive(OwnerOrderVo vo);
	
	/**
	 * 乘客上车-接到某个乘客
	 * @param vo
	 */
	public void userReceive(OwnerOrderVo vo);	
	
	/**
	 * 发车(送乘客)
	 * @param vo
	 */
	public void depart(OwnerOrderVo vo);
	
	/**
	 * 乘客完成(某个乘客下车)
	 * @param vo
	 */
	public void userFinish(OwnerOrderVo vo);
	
	/**
	 * 用户评价
	 * @param vo
	 */
	public void userRated(OwnerOrderVo vo);
	
	/**
	 * 司机评价
	 * @param vo
	 */
	public void ownerRated(OwnerOrderVo vo);
	
	/**
	 * 车单完成
	 * @param vo
	 */
	public void finish(OwnerOrderVo vo);
	
	/**
	 * 车主查看正在进行中的车单
	 * @param vo
	 * @return
	 */
	public PassengerListVo running(OwnerOrderVo vo);
	
	/**
	 * 车主查看乘客信息
	 * @param vo
	 * @return
	 */
	public OwnerOrderVo ownerfindPassInfo(OwnerOrderVo vo);
	
	/**
	 * 乘客查看车主信息
	 * @param vo
	 * @return
	 */
	public OwnerOrderVo passfindOwnerInfo(OwnerOrderVo vo);
	
	/**
	 * 根据用户id返回publish的车单信息(id,开始城市，开始城市描述，结束城市，结束城市描述，出发时间，剩余座位数)
	 * @param vo
	 * @return
	 */
	public List<OwnerOrderVo> getOwnerOrderByUserId(OwnerOrderVo vo);
	
	/**
	 * 乘客查询车主车单详情
	 * @param vo
	 * @return
	 */
	public OwnerOrderVo ownerOrderInfo(OwnerOrderVo vo);
	
	/**
	 * 给乘客推荐的车主车单列表
	 * @param vo
	 * @return
	 */
	public List<OwnerOrderVo> recommendOwner(PassengerOrderVo vo);
	

}
