package com.tyiti.easycommerce.entity;

import java.util.Date;


public class Bill {
	/**账单ID*/
	private String billId;
	
	
	/**账单号*/
	private String billNo;
	/**账单状态*/
	private String billStatus;
	/**分期金额*/
	private String stageAmount;
	/**本金*/
	private String baseAmount;
	/**分期服务费*/
	private String chargeAmount;
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getStageAmount() {
		return stageAmount;
	}
	public void setStageAmount(String stageAmount) {
		this.stageAmount = stageAmount;
	}
	public String getBaseAmount() {
		return baseAmount;
	}
	public void setBaseAmount(String baseAmount) {
		this.baseAmount = baseAmount;
	}
	public String getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	/**当前期*/
	private String currentStage;
	/**还款日*/
	private Date latestRepay;
	/**总还款金额（月供+服务费）*/
	private String userLimit;	
	/**违约金*/
	private String overdueFine;

	/**订单流水号*/
	private String tradeNo;
	/**支付状态*/
	private String payState;
	/**退款时间*/
	private Date refundTime;
	/**创建时间*/
	private Date createTime;
	/**银行网上支付平台交易流水号*/
	private String payNo;
	
	
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
	public Date getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	public Date getLatestRepay() {
		return latestRepay;
	}
	public void setLatestRepay(Date latestRepay) {
		this.latestRepay = latestRepay;
	}
	public String getUserLimit() {
		return userLimit;
	}
	public void setUserLimit(String userLimit) {
		this.userLimit = userLimit;
	}
	public String getOverdueFine() {
		return overdueFine;
	}
	public void setOverdueFine(String overdueFine) {
		this.overdueFine = overdueFine;
	}
	
}