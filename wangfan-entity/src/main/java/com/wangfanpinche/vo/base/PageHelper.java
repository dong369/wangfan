package com.wangfanpinche.vo.base;


/**
 * 分页辅助类
 * 
 * @author kevin
 * @date 2016年6月28日
 * @version 1.0
 */
public class PageHelper {

	/** 开始位置 */
	private int offset = 0;
	/** 每页记录数 */
	private int limit = 10;
	/** 根据哪个字段排序 */
	private String sort;
	/** 正序还是倒序 (ASC/DESC) */
	private String order;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
