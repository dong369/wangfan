package com.wangfanpinche.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.wangfanpinche.dto.agent.Agent;
import com.wangfanpinche.vo.UserVo;
import com.wangfanpinche.vo.agent.AgentVo;

public interface AgentService {
	
	/**
	 * 通过手机号查询用户信息
	 * @param vo
	 * @return
	 */
	public UserVo getUserByPhone(UserVo vo);
	
	/**
	 * 绑定:给某一个用户,绑定一个新的区域 
	 * @param vo
	 */
	public void bind(AgentVo vo);
	
	/**
	 * 解绑
	 * @param vo
	 */
	public void unbind(AgentVo vo);
	
	/**
	 * 根据区域id查询绑定的用户
	 * @param vo
	 * @return
	 */
	public AgentVo getUserByProcityId(AgentVo vo);
	
	/**
	 * 根据用户id查询出代理的区域
	 * @param vo
	 * @return
	 */
	public List<Agent> procity(AgentVo vo);
	
	/**
	 * 统计某个代理商代理某个市的车单数量
	 */
	public List<Map<String, Object>> statisticsOrder(LocalDate start, LocalDate end, String id);
	
	/**
	 * 统计某个代理商代理某个市的车单金额
	 */
	public List<Map<String, Object>> statisticsOrderMoney(LocalDate start, LocalDate end, String id);
	
	/**
	 * 统计某个代理商代理某个市的用户注册量
	 */
	public List<Map<String, Object>> statisticsUserRegist(LocalDate start, LocalDate end, String id);
	
}
