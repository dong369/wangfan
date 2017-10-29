package com.wangfanpinche.service;

import com.wangfanpinche.vo.PushEntityVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface PushEntityService {

	BootstrapTable table(PushEntityVo vo, PageHelper ph);

	void save(PushEntityVo vo);

	void edit(PushEntityVo vo);

	void delete(PushEntityVo vo);

	PushEntityVo get(PushEntityVo vo);

}
