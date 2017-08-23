package com.tyiti.easycommerce.vo;

import java.math.BigDecimal;

public class UnifiedorderVO {
	
	private String body;
	
	private String orderId;
	
	private String ip;
	
	private String openId;
	
	//支付类型标识 0:首付  1：账单还款
	private String type;
	//账单还款总金额
	private BigDecimal grossAmount;
	/***
	 * 付款金额
	 */
	private int orderAmount;

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	/**
	 * ip
	 *
	 * @return  the ip
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getIp() {
		return ip;
	}
	
	/**
	 * @param ip the ip to set
	 */
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * openId
	 *
	 * @return  the openId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getOpenId() {
		return openId;
	}
	
	/**
	 * @param openId the openId to set
	 */
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getGrossAmount() {
		return grossAmount;
	}
	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}
	
}
