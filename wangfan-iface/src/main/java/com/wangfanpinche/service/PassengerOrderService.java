package com.wangfanpinche.service;

import java.util.List;
import java.util.Map;

import com.wangfanpinche.vo.OwnerOrderVo;
import com.wangfanpinche.vo.PassengerOrderVo;
import com.wangfanpinche.vo.WechatPayNotify;
import com.wangfanpinche.vo.base.PageHelper;

public interface PassengerOrderService {
	
	/**
	 * 首页车主查询乘客车单列表(分页)
	 * @param vo
	 * @param ph
	 * @return
	 */
	public List<PassengerOrderVo> passOrderlist(PassengerOrderVo vo, PageHelper ph);
	
	/**
	 * 用户的乘客行程记录
	 * @param vo
	 * @return
	 */
	public List<PassengerOrderVo> passStrokeRecord(PassengerOrderVo vo);

	/**
	 * 发布车单
	 * @param vo
	 * @return
	 */
	public String publish(PassengerOrderVo vo);
	
	/**
	 * 支付宝乘客支付乘客车单
	 * @param vo
	 * @return
	 */
	public String alipayPassengerOrderInfo(OwnerOrderVo vo);
	
	/**
	 * 支付宝乘客支付乘客车单
	 * @param map
	 */
	public void alipayPassenger(Map<String, String> map);
	
	/**
	 * 微信乘客支付乘客车单
	 * @param vo
	 * @return
	 */
	public String wechatPayPassengerOrderInfo(OwnerOrderVo vo);
	
	/**
	 * 微信乘客支付乘客车单
	 * @param notify
	 */
	public void wechatPayPassenger(WechatPayNotify notify);
	
	/**
	 * 车主查看乘客车单详情
	 * @param vo
	 * @return
	 */
	public PassengerOrderVo passOrderInfo(PassengerOrderVo vo);
	
	/**
	 * 车主抢单(被动)
	 * @param vo
	 * @return
	 */
	public String receive(PassengerOrderVo vo);
	
	/**
	 * 乘客同意乘坐某个车单(被动)
	 * @param vo
	 * @return
	 */
	public String aprove(PassengerOrderVo vo);
	
	/**
	 * 乘客发布车单之后找车主车单(主动)
	 * @param vo
	 */
	public String passfindOwner(PassengerOrderVo vo);

	/**
	 * 乘客查看当前车单
	 * @param vo
	 * @return
	 */
	public PassengerOrderVo show(PassengerOrderVo vo);
	
	/**
	 * 给车主推荐的乘客车单列表
	 * @param vo
	 * @return
	 */
	public List<PassengerOrderVo> recommendPass(OwnerOrderVo vo);
	
	/**
	 * 根据用户id返回publish的乘客车单
	 * @param vo
	 * @return
	 */
	public List<PassengerOrderVo> getPassOrderByUserId(PassengerOrderVo vo);
}
