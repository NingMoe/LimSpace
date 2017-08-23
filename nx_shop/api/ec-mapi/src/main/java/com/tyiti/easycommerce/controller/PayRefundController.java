package com.tyiti.easycommerce.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.service.OrderCancellationService;


/**
 * 
 * @ClassName: PayRefundController
 * @Description: (后台订单查询支付微信流水号及退款操作)
 * @author wyy
 * @date 2016-7-7
 *
 */
@Scope("prototype")
@Controller
@RequestMapping(value = "/wxpay", produces = "application/json")
public class PayRefundController {

	@Autowired
	private OrderCancellationService orderCancellationService;

	/**
	 * @author wyy 2016/07/07
	 * @Description: 获取订单的首付及分期的支付流水号
	 * @param id
	 *            退货t_refund 订单Id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/refund/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPayStatus(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		return orderCancellationService.getPayStatus(id, response, session);
	}

}