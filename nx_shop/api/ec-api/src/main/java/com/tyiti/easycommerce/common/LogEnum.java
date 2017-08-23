package com.tyiti.easycommerce.common;

public class LogEnum {
	public enum OperateModel {
		ORDER(1,"订单"), CANCELLATION(2,"退货"), REFUND(3,"退款"),LEVMSG(4,"用户留言"),COUPON(5,"优惠券");
		
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
		private OperateModel(int key, String value) {
			this.key = key;
			this.value = value;
		}
		 
	}
	
	public enum Action {
		TIJIAODINGDAN(1,"提交订单"), 
		WEINXIN(2,"微信支付"),
		SHENCHENGFENQI(3,"生成分期"),
		QUEREN(4,"确认"),
		ZHIDAN(5,"制单"),
		FAHUO(6,"发货"),
		QIANSHOU(7,"用户签收"),
		QUXIAO(8,"取消"),
		SHENQINGQUXIAO(9,"申请取消"),
		SHENQINGTUIHUO(10,"申请退货"),
		QUEDINGTUIHUO(11,"确定退货"),
		JUJUETUIHUO(12,"拒绝退货"),
		SHOUHUO(13,"收货"),
		REFUND(14,"退款"),
		TONGYIQUXIAO(15,"同意申请取消订单"),
		ORDERFINISH(16,"订单完成"),
		ADD(17,"添加"),
		REMOVE(18,"删除"),
		UPDATE(19,"修改");
		private int key ;
		private String value;
		private Action(int key, String value) {
			this.key = key;
			this.value = value;
		}
		
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
		
	}
	public enum Source {
		SHOP(1,"商城"),PLAT(2,"平台");
		private int key ;
		private String value;
		private Source(int key, String value) {
			this.key = key;
			this.value = value;
		}
		
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
		
	}
	
}
