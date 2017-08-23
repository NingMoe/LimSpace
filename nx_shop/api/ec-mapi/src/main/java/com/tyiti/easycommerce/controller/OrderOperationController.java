package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.service.OrderOperationService;

@Controller
public class OrderOperationController {

	@Autowired
	private OrderOperationService orderOperationService;

	/**
	 * 申请取消订单（已制单、已发货）
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/cancellations", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> applyToCancel(@RequestBody OrderCancellation orderCancellation,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.applyToCancel(orderCancellation);
		return data;
	}

	/**
	 * 取消订单（未付款）
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/refunds/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cancel(@PathVariable("id") Integer id,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.cancel(session, id);
		return data;
	}

	/**
	 * 删除订单
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable("id") Integer id,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.delete(session, id);
		return data;
	}

	/**
	 * 订单签收
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> sign(@PathVariable("id") Integer id,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.sign(session, id);
		return data;
	}

	/**
	 * 申请退货
	 * 
	 * @param orderCancellation
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/return", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> applyToReturn(
			@RequestBody OrderCancellation orderCancellation,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.applyToReturn(session,orderCancellation);
		return data;
	}

	/**
	 * 退货列表
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/return", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getReturnList(HttpSession session,
			Integer offset, Integer limit) {
		Map<String, Object> data = new HashMap<String, Object>();

		List<OrderCancellation> listOrderCancellation = orderOperationService.getReturnList(session, offset, limit);

		// 总条数
		int pageCount = orderOperationService.getReturnListCount(session);

		data.put("code", 200);
		data.put("message", "OK");
		data.put("pageCount", pageCount);
		data.put("data", listOrderCancellation);

		return data;
	}

	/**
	 * 退货详细
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/return/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getReturnDetail(@PathVariable("id") Integer id,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		OrderCancellation orderCancellation = orderOperationService.getReturnDetail(session, id);
		data.put("data", orderCancellation);
		return data;
	}
}
