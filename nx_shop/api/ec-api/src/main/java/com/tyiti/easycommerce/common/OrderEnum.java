package com.tyiti.easycommerce.common;

public class OrderEnum {
	//	订单状态，0: 草稿 1: 未付款 2: 已付款 3: 已确认 4: 已制单 5: 已发货 6: 已签收 9: 已取消
	public enum status {
		CG(0,"草稿"), WFK(1,"未付款"), YFK(2,"已付款"),YQR(3,"已确认"),
		YZD(4,"已制单"),YFH(5,"已发货"),YQS(5,"已签收"),YQX(5,"已取消");
		
		private int  key ;
		private String value;
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		private status(int key, String value) {
			this.key = key;
			this.value = value;
		}
		 
	}
	
	//订单支付类型，1: 全款 2: 全分期 3: 首付+分期
	public enum payMethod {
		QK(1,"全款"), QFQ(2,"全分期"),SFFQ(3,"首付+分期");
		
		private int  key ;
		private String value;
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		private payMethod(int key, String value) {
			this.key = key;
			this.value = value;
		}
		 
	}
	
}
