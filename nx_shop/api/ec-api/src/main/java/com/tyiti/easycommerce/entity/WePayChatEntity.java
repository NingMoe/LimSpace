 /**
  * 文件名[fileName]：weChatEntity.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年4月28日 下午2:51:13
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.entity;

 /**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年4月28日 下午2:51:13。</p>
  */

public class WePayChatEntity {
	/***
	 * 应用ID
	 */
   private String appId;
   /**
    * 商户号
    */
   private String partnerid;
   /**
    * 预支付交易会话ID
    */
   private String prepayId;
   /**
    * 扩展字段
    */
   private String strpackage;
   /**
    * 随机字符串
    */
   private String nocestr;
   /**
    * 时间戳
    */
   private String timestamp;
   /***
    * sign
    */
   private String sign;

	/**
	 * appId
	 *
	 * @return  the appId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getAppId() {
		return appId;
	}
	
	/**
	 * @param appId the appId to set
	 */
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * partnerid
	 *
	 * @return  the partnerid
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getPartnerid() {
		return partnerid;
	}
	
	/**
	 * @param partnerid the partnerid to set
	 */
	
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	
	/**
	 * prepayId
	 *
	 * @return  the prepayId
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getPrepayId() {
		return prepayId;
	}
	
	/**
	 * @param prepayId the prepayId to set
	 */
	
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
	/**
	 * strpackage
	 *
	 * @return  the strpackage
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getStrpackage() {
		return strpackage;
	}
	
	/**
	 * @param strpackage the strpackage to set
	 */
	
	public void setStrpackage(String strpackage) {
		this.strpackage = strpackage;
	}
	
	/**
	 * nocestr
	 *
	 * @return  the nocestr
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getNocestr() {
		return nocestr;
	}
	
	/**
	 * @param nocestr the nocestr to set
	 */
	
	public void setNocestr(String nocestr) {
		this.nocestr = nocestr;
	}
	
	/**
	 * timestamp
	 *
	 * @return  the timestamp
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @param timestamp the timestamp to set
	 */
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * sign
	 *
	 * @return  the sign
	 * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	 */
	
	public String getSign() {
		return sign;
	}
	
	/**
	 * @param sign the sign to set
	 */
	
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String toUrlParamsSortedByKey() {
		StringBuffer sb = new StringBuffer();
	    //appId,nocestr,partnerid,prepayId,strpackage,timestamp
		sb.append("appId=" + appId);
		sb.append("&nonceStr=" + nocestr);
		sb.append("&package=" + strpackage);
//		sb.append("&partnerId=" + partnerid);
//		sb.append("&prepayId=" + prepayId);
		sb.append("&signType=MD5");
		sb.append("&timeStamp=" + timestamp);
		
		System.out.println(sb.toString());
		return sb.toString();
	}  
}
