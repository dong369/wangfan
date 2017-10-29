package com.wangfanpinche.service;

import java.util.List;
import com.wangfanpinche.vo.JournalVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface JournalService {
	
	/**
	 * 通过id获取账单详情
	 * @param vo
	 * @return
	 */
	public JournalVo getDetail(JournalVo vo);
	
	/**
	 * 账单列表
	 * @param vo
	 * @return
	 */
	public List<JournalVo> list(JournalVo vo);

	/**
	 * 数据表格
	 * @param vo
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(JournalVo vo, PageHelper ph);
		
}
