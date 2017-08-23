package com.tyiti.easycommerce.entity;

import java.util.Date;

public class PayRecode {

	private Integer id;
	/** 订单ID */
	private String orderId;
	/** 订单流水号 */
	private String tradeNo;
	/** 支付状态 */
	private String payState;
	/** 支付完成时间 */
	private Date payTime;
	/** 创建时间 */
	private Date createTime;
	/** 银行网上支付平台交易流水号 */
	private String payNo;
	/** 个人支付卡号 */
	private String accNo;
	/** 持卡人姓名 */
	private String cstName;

	/** 客户支付ip地址 */
	private String ip;

	/** 微信: 是否关注公众账号(is_subscribe) */
	private Integer isSubscribe;

	/** 微信: 微信 openid */
	private String openId;

	public Integer getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(Integer isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getCstName() {
		return cstName;
	}

	public void setCstName(String cstName) {
		this.cstName = cstName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
