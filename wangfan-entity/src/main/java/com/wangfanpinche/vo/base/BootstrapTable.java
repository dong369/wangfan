package com.wangfanpinche.vo.base;


import java.util.ArrayList;
import java.util.List;

/**
 * 数据表格
 * @author kevin
 * @date 2016年6月28日 上午11:01:42
 * @version 1.0
 */
public class BootstrapTable {

	private long total;
	private List<?> rows = new ArrayList<>();

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
}
