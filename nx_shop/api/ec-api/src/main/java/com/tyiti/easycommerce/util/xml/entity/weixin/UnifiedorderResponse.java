package com.tyiti.easycommerce.util.xml.entity.weixin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnifiedorderResponse {
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
	@XmlElement(name="trade_type")
	String tradeType;
	@XmlElement(name="prepay_id")
	String prepayId;
	@XmlElement(name="code_url")
	String codeUrl;

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
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	@Override
	public String toString() {
		return "UnifiedorderResponse [returnCode=" + returnCode
				+ ", returnMsg=" + returnMsg + ", appid=" + appid + ", mchId="
				+ mchId + ", deviceInfo=" + deviceInfo + ", nonceStr="
				+ nonceStr + ", sign=" + sign + ", resultCode=" + resultCode
				+ ", errCode=" + errCode + ", errCodeDes=" + errCodeDes
				+ ", tradeType=" + tradeType + ", prepayId=" + prepayId
				+ ", codeUrl=" + codeUrl + "]";
	}

}
