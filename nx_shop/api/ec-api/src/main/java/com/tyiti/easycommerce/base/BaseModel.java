package com.tyiti.easycommerce.base;

import com.tyiti.easycommerce.util.TimeUtils;


public class BaseModel {

	 	//从第几条开始
		protected int offset = 0;
	    // 每页显示的条数
		protected int limit=10;
	    // 排序字段
		protected String sort;
	    // 正序:"asc" 倒序："desc"
		protected String order;
		//条件查询开始时间
		protected String startTime;  
		//条件查询结束时间
		protected String endTime ; 
		
		
		public String getStartTime()  {
			return TimeUtils.checkDate(startTime);
		}


		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		
		public String getEndTime() {
			return TimeUtils.checkDate(endTime);
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		
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
