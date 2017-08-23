package com.tyiti.easycommerce.service;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.WePayChatEntity;
import com.tyiti.easycommerce.entity.WeixinSignature;
import com.tyiti.easycommerce.util.xml.entity.weixin.PayOrderSyncReq;
import com.tyiti.easycommerce.util.xml.entity.weixin.Unifiedorder;
import com.tyiti.easycommerce.util.xml.entity.weixin.UnifiedorderResponse;
import com.tyiti.easycommerce.vo.UnifiedorderVO;


public interface WeixinService {

	 /**
	  * <p>功能描述：。</p>	
	  * @param unifiedorder
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年4月28日 下午2:27:23。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	String getSignature(Unifiedorder unifiedorder);
	
	 /**
	  * <p>功能描述：。</p>	
	  * @param unifiedorder
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年4月28日 下午2:27:23。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	String getSignature(PayOrderSyncReq payOrderSyncReq);
	 /**
	  * <p>功能描述：。</p>	
	  * @param weixinSignature
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年4月28日 下午2:28:50。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	boolean checkSignature(WeixinSignature weixinSignature);

	 /**
	  * <p>功能描述：。</p>	
	  * @param inputStream
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年4月28日 下午2:28:58。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	String processRequest(ServletInputStream inputStream);

	 /**
	  * <p>功能描述：。</p>	
	  * @param code
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年4月28日 下午2:29:05。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	String getOpenId(String code);

	 /**
	  * <p>功能描述：第一次与请求完成以后校验签名。</p>	
	  * @param unifiedorderResponse
	  * @param unifiedorder
	 * @return 
	 * @throws ParseException 
	  * @since JDK1.7。
	  * <p>创建日期:2016年4月28日 下午2:29:37。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	WePayChatEntity getSignature(UnifiedorderResponse unifiedorderResponse, Unifiedorder unifiedorder) throws ParseException;

	 /**
	  * <p>功能描述：获取用户的信息。</p>	
	 * @param session 
	 * @param request 
	 * @param map 
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年4月28日 下午4:30:29。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Unifiedorder getUnifiedorder(UnifiedorderVO vo,HttpSession session);
	
	/**
	 * 获取JS-SDK使用权限签名
	 * @param request
	 * @return
	 */
	Map<String,Object> getSignature(HttpServletRequest request,String url);

	 /**
	  * <p>功能描述：。</p>	
	  * @param request
	  * @param response
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月7日 下午6:04:23。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> paynotify(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 微信支付主动查询支付结果
	 * @param request
	 * @param response
	 * @return
	 */
	void payordersync(HttpServletRequest request, HttpServletResponse response);
}
