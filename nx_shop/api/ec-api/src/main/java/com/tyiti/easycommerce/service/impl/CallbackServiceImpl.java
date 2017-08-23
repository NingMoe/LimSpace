 /**
  * 文件名[fileName]：CallbackServiceImpl.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年7月7日 下午5:24:01
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.service.CallbackService;
import com.tyiti.easycommerce.service.CreditCardPayService;
import com.tyiti.easycommerce.service.KooPayOrderService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.WeixinService;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年7月7日 下午5:24:01。</p>
  */
@Service
public class CallbackServiceImpl  implements CallbackService{
	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	CreditCardPayService creditCardPayService;
	@Autowired
	private WeixinService weixinService;
	@Autowired
	private OrderSkuDao orderSkuDao;
	@Autowired
	KooPayOrderService kooPayOrderService;
	@Autowired
	private OrderService orderService;
	
	 /**
	  * <p>功能描述:所有支付调用接口。</p>	
	  * @param strMessage
	  * @param payMenth
	  * @param reqeust
	  * @param response
	  * @since JDK1.7。
	  * <p>创建日期2016年7月7日 下午5:43:14。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Map<String,Object> callback(Map<String, Object> map, String payMenth, HttpServletRequest request, HttpServletResponse response) {
		 logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>进入支付回调方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if(payMenth.equals("ceb")){
			String strMessage = (String) map.get("message");
			map = creditCardPayService.notifyCall(strMessage);
		}else if(payMenth.equals("weCatch")){
			map =  weixinService.paynotify(request,response);
		}else if(payMenth.equals("tyfq")){
			Integer order_id = (Integer) map.get("order_id");
			String payPassword = map.get("payPassword").toString();
			HttpSession session = request.getSession();
			map = orderService.paymentBefore(order_id, payPassword, session);
		}else{
			map.put("code", "400");
			map.put("message", "支付方式失败");
		}
		
		if((map.get("code").toString()).equals("200")){
			System.out.println("=========================调用信息================================");
			String strOrderId = String.valueOf(map.get("orderId"));
			System.out.println(strOrderId+"=========================调用信息================================");
			if(strOrderId!=null&&strOrderId!=""){
				Integer order_id = Integer.parseInt(strOrderId);
				this.pushKooMessage(order_id);
			}
		}
		return map;
	}

	 /**
	  * <p>功能描述：确定插入信息。</p>	
	 * @param order_id 
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月7日 下午6:46:07。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private Map<String, Object> pushKooMessage(Integer order_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrderSku> orderSkuList = orderSkuDao.getByOrderId(order_id);
		Order order = orderService.getById(order_id);
		Integer custId = order.getCustId();
		if(orderSkuList!=null&&orderSkuList.size()>0){
			OrderSku  orderSku = orderSkuList.get(0);
			if(orderSku!=null&&orderSku.getSkuErpCode().equals("koo")){
				map = kooPayOrderService.pushOrderMessage(order.getNo(),custId);//add kong chuilong 2016-06-12
			}
		}
		return map;
	}

}
