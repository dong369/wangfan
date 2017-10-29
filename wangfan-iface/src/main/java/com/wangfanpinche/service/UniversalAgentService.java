package com.wangfanpinche.service;

import java.util.List;
import com.wangfanpinche.vo.agent.UniversalAgentVo;
import com.wangfanpinche.vo.base.PageHelper;

public interface UniversalAgentService {

	/**
	 * 推荐别人注册
	 */
	void recommend(UniversalAgentVo vo);

	/**
	 * 统计推荐人推荐的人数,成车主车单数量,完成车主车单金额收益
	 */
	UniversalAgentVo statistics(UniversalAgentVo vo);
	
	/**
	 * 今日收益 
	 */
	UniversalAgentVo today(UniversalAgentVo vo);

	/**
	 * 查看收益记录
	 */
	List<UniversalAgentVo> detail(UniversalAgentVo vo, PageHelper ph);

}
