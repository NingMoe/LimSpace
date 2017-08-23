package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.CreditCardPay;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.service.CallbackService;
import com.tyiti.easycommerce.service.CreditCardPayService;
import com.tyiti.easycommerce.service.OrderService;

/**
 * 光大信用卡支付接口
 * 
 * 项目名称：easycommerce-api-mybatis 类名称：CreditCardPayController 类描述：
 * 1）调用光大订单支付接口；2）接收支付结果通知接口 创建人：shenzhiqiang 创建时间：2016-3-23 下午4:29:29
 * 
 * @version
 *
 */
@Controller
public class CreditCardPayController {

	@Autowired
	private CreditCardPayService creditCardPayService;
	@Autowired
	CallbackService callbackService;
	@Autowired
	OrderService orderService;
	@Autowired
	private OrderSkuDao orderSkuDao;

	private static Log logger = LogFactory
			.getLog(CreditCardPayController.class);

	@RequestMapping(value = "/creditCardPay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> creditCardPay(Integer id,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Order order = orderService.getById(id);
		/******下订单之前订单是否是有效订单 开始***/
		User user = (User) session.getAttribute(Constants.USER);
		map = orderService.getOrderIsSuccessOrder(order,user);
		Integer code = (Integer) map.get("code");
		if(code!=200){
			return map;
		}
		/******下订单之前订单是否是有效订单 结束***/
		CreditCardPay creditCardPay = creditCardPayService.creditCardPay(id);
		map.put("creditCardPay", creditCardPay);
		return map;
	}

	@RequestMapping(value = "/cebpay/notify", method = RequestMethod.POST, produces = "text/html")
	@ResponseBody
	public String notifyCall(String signMsg, HttpServletRequest reqeust,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", signMsg);
		map = callbackService.callback(map, "ceb", reqeust, response);
		String refreshUrl = "";
		Integer code =  (Integer) map.get("code");
		Integer orderId = (Integer) map.get("orderId");
		boolean flag = false;
		List<OrderSku> list = orderSkuDao.getByOrderId(orderId);
		logger.info("》》》》参与抽奖活动的skuId为："
				+ creditCardPayService.getLotterySkuId());
		if (list != null && list.size() > 0) {
			for (OrderSku orderSku : list) {
				logger.info("》》》》与用户支付的订单相关的skuId为：" + orderSku.getSkuId());
				if (orderSku.getSkuId().equals(
						creditCardPayService.getLotterySkuId())) {// 判断订单中是否有与抽奖相关的sku
					flag = true;
					break;
				}
			}
		}
		if (code == 200) {
			if (flag) {
				refreshUrl = creditCardPayService.getLotteryUrl();
			} else {
				refreshUrl = creditCardPayService.getCebsuccUrl();
			}
		} else {
			refreshUrl = creditCardPayService.getCebfailUrl();
		}
		logger.info("重定向地址：" + refreshUrl);
		// return "redirect:"+refreshUrl;
		return "<!doctype html><html><head><meta charset=\"utf-8\"><meta http-equiv=\"refresh\" content=\"0; url="
				+ refreshUrl + "\"></head></html>";
	}
}
