package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.service.PollingService;

/**
 * 
 * @author shenzhiqiang
 * @ClassName: PollingController 
 * @Description: 
 * @date 2016年5月18日 下午5:32:22
 */
@Controller
@RequestMapping("/polling")
public class PollingController {

	Logger logger = Logger.getLogger(PollingController.class);
	
	@Autowired
	private PollingService pollingService;
	
	@RequestMapping(value = "/updateReceiptTime", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> updateReceiptTime(){
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("》》》》跑批修改15天未签收的订单签收时间《《《《");
		pollingService.updateReceiptTime();
		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}
	
	/**
	 * 跑批修改订单状态
	 * 活动已经结束，没有支付的订单状态修改为“已失效”
	 * @return
	 */
	@RequestMapping(value = "/updateOrderStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> updateOrderStatus(String token){
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info("》》》》跑批修改活动已经结束但没有付款的订单，修改状态为：已失效《《《《");
		int count = pollingService.updateOrderStatus(token);
		if (count>0) {
			map.put("code", 200);
			map.put("messsge", "OK");
		}else {
			map.put("code", 400);
			map.put("messsge", "token校验失败。");
		}
		return map;
	}
}
