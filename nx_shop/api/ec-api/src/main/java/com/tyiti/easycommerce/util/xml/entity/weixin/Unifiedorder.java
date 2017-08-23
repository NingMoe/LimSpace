package com.tyiti.easycommerce.util.xml.entity.weixin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class Unifiedorder {
	@XmlElement(name="appid")
	String appid;
	@XmlElement(name="body")
	String body;
	@XmlElement(name="device_info")
	String deviceInfo = "WEB";
	@XmlElement(name="fee_type")
	String feeType = "CNY";
	@XmlElement(name="mch_id")
	String mchId;
	@XmlElement(name="nonce_str")
	String nonceStr;
	@XmlElement(name="notify_url")
	String notifyUrl;
	@XmlElement(name="openid")
	String openid;
	@XmlElement(name="out_trade_no")
	String outTradeNo;
	@XmlElement(name="spbill_create_ip")
	String spbillCreateIp; // 终端IP
	@XmlElement(name="total_fee")
	int totalFee;
	@XmlElement(name="trade_type")
	String tradeType = "JSAPI"; // 总金额
	@XmlElement(name="sign")
	String sign;
	@XmlElement(name="attach")
	String attach;//附加数据

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String toUrlParamsSortedByKey() {
		StringBuffer sb = new StringBuffer();
		// ["appid", "body", "deviceInfo", "feeType", "mchId", "nonceStr", 
		// "notifyUrl", "openid", "outTradeNo", "spbillCreateIp", "totalFee", "tradeType"]
		sb.append("appid=" + appid);
		sb.append("&attach=" + attach);
		sb.append("&body=" + body);
		sb.append("&device_info=" + deviceInfo);
		sb.append("&fee_type=" + feeType);
		sb.append("&mch_id=" + mchId);
		sb.append("&nonce_str=" + nonceStr);
		sb.append("&notify_url=" + notifyUrl);
		sb.append("&openid=" + openid);
		sb.append("&out_trade_no=" + outTradeNo);
		sb.append("&spbill_create_ip=" + spbillCreateIp);
		sb.append("&total_fee=" + totalFee);
		sb.append("&trade_type=" + tradeType);
        System.out.println(sb);
		return sb.toString();
	}
}
