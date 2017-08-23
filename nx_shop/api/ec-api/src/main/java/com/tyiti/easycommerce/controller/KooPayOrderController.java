 /**
  * 文件名[fileName]：KooPayOrderController.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年5月17日 上午11:20:21
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.KooPushLog;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.repository.KooPushLogDao;
import com.tyiti.easycommerce.service.KooPayOrderService;
import com.tyiti.easycommerce.service.OrderService;

/**
  *<p>类描述：订单支付完成调用推送新东方接口。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.7。
  *<p>创建日期：2016年5月17日 上午11:20:21。</p>
  */
@Controller
public class KooPayOrderController {
	@Autowired
	KooPayOrderService kooPayOrderService;
	@Autowired
	KooPushLogDao kooPushLogDao;
	@Autowired
	OrderService orderService;
	
	private Log logger = LogFactory.getLog(KooPayOrderController.class);
	/**
	  * <p>功能描述：定时任务。</p>	
	  * @param order
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月30日 上午9:50:28。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "/kooPushOrderData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> pushOrderMessage(String password){
		   Map<String,Object>  map = new HashMap<String,Object>();
		   try {
			   if(password.equals("kooPush123@#")){//kooPush123@#
				   List<KooPushLog>  kooPushLogList = kooPushLogDao.findByKooPushLogList();
				   if(kooPushLogList!=null&&kooPushLogList.size()>0){
					   for(KooPushLog kooPushLog:kooPushLogList){
						   Order orderEntity = orderService.getById(kooPushLog.getOrderId());
						   kooPayOrderService.getHttpClients(orderEntity.getNo(),kooPushLog.getUserId());
					   }
					   
				   }
			   }
			   System.out.println("===========================完成kooPushOrderData=======================================");
		   } catch (Exception e) {
				e.printStackTrace();
				map.put("code", "400");
				map.put("Message", "网络不通畅！信息发送失败,半个小时候才能接收信息");
			}
		   
		   return map;
	  
	}
	
	 /**
	  * logger
	  *
	  * @return  the logger
	  * @since   CodingExample Ver(编码[bian ma]范例[fan li]查看) 1.0
	  */
	 
	public Log getLogger() {
		return logger;
	}
	
	 /**
	  * @param logger the logger to set
	  */
	 
	public void setLogger(Log logger) {
		this.logger = logger;
	}
	
	
}
