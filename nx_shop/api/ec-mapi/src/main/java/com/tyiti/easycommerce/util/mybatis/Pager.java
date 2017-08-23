package com.tyiti.easycommerce.util.mybatis;


/**
 * 分页信息
 * @author rainyhao
 * @since 2015-5-26 下午3:40:19
 */
public class Pager {
	// 页号
	private int page;
	// 页尺寸
	private int rows;
	// 总记录数
	private long total;
	// 分页数据
	private Object data;
	// 是否统计总数(是否执行select count语句)
	private boolean doCount;
	// 排序列
	private String sort;
	// 排序顺序
	private String order;
	
	public Pager() {
		page = 1;
		rows = 10;
		doCount = false;
		order = "desc";
	}
	
	public Pager(int page, int rows) {
		this.page = page;
		this.rows = rows;
		doCount = false;
		order = "desc";
	}
	
	public Pager(int page, int rows, String sort, String order) {
		this.page = page;
		this.rows = rows;
		this.sort = sort;
		this.order = order;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public boolean getDoCount() {
		return doCount;
	}
	public void setDoCount(boolean doCount) {
		this.doCount = doCount;
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
	
	/**
	 * 总页数
	 * @author rainyhao
	 * @since 2015-7-2 下午12:26:03
	 * @return
	 */
	public long getPages() {
		if (total == 0) {
			return 0;
		}
		long left = total % rows;
		long pages = total / rows;
		return pages + (left > 0 ? 1 : 0);
	}
	
	public String baseParams() {
		String s = "page=" + page + ", rows=" + rows +
				", doCount=" + doCount + 
				", sort=" + sort + ", order=" + order;
		return s;
	}
}
