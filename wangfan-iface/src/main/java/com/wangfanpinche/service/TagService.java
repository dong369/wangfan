package com.wangfanpinche.service;

import java.util.List;

import com.wangfanpinche.vo.TagVo;
import com.wangfanpinche.vo.base.BootstrapTable;
import com.wangfanpinche.vo.base.PageHelper;

public interface TagService {
	
	/**
	 * 后台数据列表(带分页)
	 * @param tag
	 * @param ph
	 * @return
	 */
	public BootstrapTable table(TagVo tagVo, PageHelper ph);
	
	/**
	 * 通过id获取一条标签
	 * @param vo
	 * @return
	 */
	public TagVo get(TagVo vo);

	/**
	 * 删除标签
	 * @param vo
	 */
	public void delete(TagVo vo);

	/**
	 * 更新标签
	 * @param vo
	 */
	public void edit(TagVo vo);
	
	/**
	 * 添加标签
	 * @param vo
	 */
	public void save(TagVo vo);
	
	/**
	 * app发布车单时标签列表
	 * @return
	 */
	public List<TagVo> list();
	
}
