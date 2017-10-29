package com.wangfanpinche.service;

import java.util.List;

import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.base.PageHelper;

public interface UserPushService {

	/**
	 * 推送设为已读 
	 */
	void read(PushEntityVo vo);

	/**
	 * 系统推送同步到记录 
	 */
	void sync(String userId);

	/**
	 * 查看所有未读的系统推送，再推送一下 
	 */
	void noread(PushEntityVo vo);

	/**
	 * 查看所有系统推送，按照结束时间倒叙(分页)
	 */
	List<PushEntityVo> list(PushEntityVo vo, PageHelper ph);

}
