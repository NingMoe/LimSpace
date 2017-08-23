package com.tyiti.easycommerce.util.xml.entity.weixin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayOrderSyncRes {
	@XmlElement(name="return_code")
	String returnCode;
	@XmlElement(name="return_msg")
	String returnMsg;
	@XmlElement(name="appid")
	String appid;
	@XmlElement(name="mch_id")
	String mchId;
	@XmlElement(name="device_info")
	String deviceInfo;
	@XmlElement(name="nonce_str")
	String nonceStr;
	@XmlElement(name="sign")
	String sign;
	@XmlElement(name="result_code")
	String resultCode;
	@XmlElement(name="err_code")
	String errCode;
	@XmlElement(name="err_code_des")
	String errCodeDes;
	@XmlElement(name="openid")
	String openId;
	@XmlElement(name="is_subscribe")
	String isSubscribe;
	@XmlElement(name="trade_type")
	String tradeType;
	@XmlElement(name="trade_state")
	String tradeState;
	@XmlElement(name="bank_type")
	String bankType;
	@XmlElement(name="total_fee")
	int totalFee;
	@XmlElement(name="fee_type")
	String feeType;
	@XmlElement(name="cash_fee")
	int cashFee;
	@XmlElement(name="cash_fee_type")
	String cashFeeType;
	@XmlElement(name="coupon_fee")
	int couponFee;
	@XmlElement(name="coupon_count")
	int couponCount;
	@XmlElement(name="transaction_id")
	String transactionId;
	@XmlElement(name="out_trade_no")
	String outTradeNo;
	@XmlElement(name="attach")
	String attach;
	@XmlElement(name="time_end")
	String timeEnd;
	@XmlElement(name="trade_state_desc")
	String tradeStateDesc;
	
	
	public String getTradeState() {
		return tradeState;
	}
	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}
	public String getTradeStateDesc() {
		return tradeStateDesc;
	}
	public void setTradeStateDesc(String tradeStateDesc) {
		this.tradeStateDesc = tradeStateDesc;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrCodeDes() {
		return errCodeDes;
	}
	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getIsSubscribe() {
		return isSubscribe;
	}
	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public int getCashFee() {
		return cashFee;
	}
	public void setCashFee(int cashFee) {
		this.cashFee = cashFee;
	}
	public String getCashFeeType() {
		return cashFeeType;
	}
	public void setCashFeeType(String cashFeeType) {
		this.cashFeeType = cashFeeType;
	}
	public int getCouponFee() {
		return couponFee;
	}
	public void setCouponFee(int couponFee) {
		this.couponFee = couponFee;
	}
	public int getCouponCount() {
		return couponCount;
	}
	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	@Override
	public String toString() {
		return "PayNotifyResponse [returnCode=" + returnCode + ", returnMsg="
				+ returnMsg + ", appid=" + appid + ", mchId=" + mchId
				+ ", deviceInfo=" + deviceInfo + ", nonceStr=" + nonceStr
				+ ", sign=" + sign + ", resultCode=" + resultCode
				+ ", errCode=" + errCode + ", errCodeDes=" + errCodeDes
				+ ", openId=" + openId + ", isSubscribe=" + isSubscribe
				+ ", tradeType=" + tradeType+ ", tradeState=" + tradeState + ", bankType=" + bankType
				+ ", totalFee=" + totalFee + ", feeType=" + feeType
				+ ", cashFee=" + cashFee + ", cashFeeType=" + cashFeeType
				+ ", couponFee=" + couponFee + ", couponCount=" + couponCount
				+ ", transactionId=" + transactionId + ", outTradeNo="
				+ outTradeNo + ", attach=" + attach + ", timeEnd=" + timeEnd
				+ ", tradeStateDesc=" + tradeStateDesc + "]";
	}

}
