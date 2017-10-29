package com.wangfanpinche.service;

import java.io.Serializable;
import com.wangfanpinche.vo.FeedBackVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface FeedBackService {
	
	/**
	 * 保存反馈信息
	 * @param vo
	 * @return
	 */
	public Serializable save(FeedBackVo vo);
	
	/**
	 * 后台查询所有反馈信息
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(FeedBackVo vo, PageHelper ph);
	
	/**
	 * 更改状态为已读
	 * @param vo
	 * @return
	 */
	public FeedBackVo updateAndGet(FeedBackVo vo);
	
}
