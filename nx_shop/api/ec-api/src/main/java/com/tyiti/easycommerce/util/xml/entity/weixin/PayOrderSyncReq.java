package com.tyiti.easycommerce.util.xml.entity.weixin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayOrderSyncReq {
	@XmlElement(name="appid")
	String appid;
	@XmlElement(name="mch_id")
	String mchId;
	@XmlElement(name="nonce_str")
	String nonceStr;
	@XmlElement(name="transaction_id")
	String transactionId;
	@XmlElement(name="out_trade_no")
	String outTradeNo;
	@XmlElement(name="sign")
	String sign;
	
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

	
	public String toUrlParamsSortedByKey() {
		StringBuffer sb = new StringBuffer();
		sb.append("appid=" + appid);
		sb.append("&mch_id=" + mchId);
		sb.append("&nonce_str=" + nonceStr);
		sb.append("&out_trade_no=" + outTradeNo);
		return sb.toString();
	}
}
