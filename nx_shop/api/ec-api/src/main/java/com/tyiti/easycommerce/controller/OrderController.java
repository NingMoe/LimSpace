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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.CallbackService;
import com.tyiti.easycommerce.service.OrderPaymentService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.PickupOrderService;
import com.tyiti.easycommerce.service.PickupPointService;

@Scope("prototype")
@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderPaymentService orderPaymentService;

	@Autowired
	private PickupOrderService pickupOrderService;

	@Autowired
	private PickupPointService pickupPointService;
	
	@Autowired
	CallbackService callbackService;
	private Log logg = LogFactory.getLog(this.getClass());

	/**
	 * 下单 提交订单
	 * 
	 * @param order
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> add(@RequestBody Order order,
	HttpServletResponse response, HttpSession session) {
 
		Map<String, Object> data = new HashMap<String, Object>();

		User user = (User) session.getAttribute(Constants.USER);
		Integer sessionCustId = user.getId();

		if (orderService.getOrderCountByCustIdAndStatus(sessionCustId, 1) > 2) {
			data.put("code", 400);
			data.put("message", "未付款订单过多！");
			return data;
		}

		order.setCustId(sessionCustId);
		order.setStatus(1);
		try {
			data = orderService.addOrder(user, order);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 400);
			data.put("message", e.getMessage());
			logg.error(e);
		}
		return data;
	}

	/**
	 * 我的订单列表
	 * 
	 * @param status
	 * @param offset
	 * @param limit
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderList(Integer status, Integer offset,
			Integer limit, HttpServletResponse response, HttpSession session) {

		Map<String, Object> map = new HashMap<String, Object>();

		// 客戶Id
		User user = (User) session.getAttribute(Constants.USER);
		Integer sessionCustId = user.getId();

		// 本页的数据
		List<Order> list = orderService.getOrderList(status, sessionCustId,
				offset, limit);

		// 总条数
		int pageCount = orderService.getOrderListCount(status, sessionCustId);

		map.put("code", 200);
		map.put("message", "OK");
		map.put("pageCount", pageCount);
		map.put("data", list);

		return map;
	}

	@RequestMapping(value = "/myorders", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> myOrderList(@RequestParam Map<String,Object> params , HttpServletResponse response, HttpSession session) {

		Map<String, Object> map = new HashMap<String, Object>();

		// 客戶Id
		User user = (User) session.getAttribute(Constants.USER);
		Integer sessionCustId = user.getId();
		params.put("custId",sessionCustId );
		// 本页的数据
		List<Order> list = orderService.getOrderSkuList( params);

		// 总条数
		Long pageCount = orderService.getOrderSkuListCount(params);

		map.put("code", 200);
		map.put("message", "OK");
		map.put("pageCount", pageCount);
		map.put("data", list);

		return map;
	}
	/**
	 * 订单详情
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderDetail(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		// 客戶Id
		Integer sessionCustId = user.getId();
		Order orderSku = orderService.getOrderDetail(id, sessionCustId);
		// 获取支付信息
		List<OrderPayment> orderPaymentList = orderPaymentService
				.selectOrderPaymentByOrderId(orderSku.getId());
		orderSku.setOrderPaymentList(orderPaymentList);

		// 获取自提点信息
		PickupOrder pickupOrder = pickupOrderService.selectByOrderId(id);
		if (pickupOrder != null) {
			if (pickupOrder.getStatus() != 0) {
				orderSku.setPickupCode(pickupOrder.getCode());
			}
			orderSku.setPickupStatus(pickupOrder.getStatus());
			PickupPoint pickupPoint = pickupPointService
					.selectPickupPointById(pickupOrder.getPickupPointId());
			orderSku.setPickupPoint(pickupPoint);
			orderSku.setPickup(1);
		} else {
			orderSku.setPickup(0);
		}

		map.put("code", 200);
		map.put("message", "OK");
		map.put("data", orderSku);

		return map;
	}

	/**
	 * 支付分期 从session中获取支付密码并验证 全款不走该接口 全分期 或者首付+分期会走该接口 调用信分宝支付接口
	 */
	@RequestMapping(value = "/orders/installments/{order_id}&{payPassword}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> installments(@PathVariable("order_id") Integer order_id,@PathVariable("payPassword") String payPassword,
			HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("payPassword", payPassword);
//		 map =orderService.paymentBefore(order_id, payPassword, session);
		map = callbackService.callback(map, "tyfq", request, response);
		return map;
	}

	/**
	 * 修改订单中的姓名和手机号码
	 * 
	 * @param param
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kooOrders/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> updateOrder(@PathVariable("id") Integer id,
			@RequestParam Map<String, Object> param,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (param.get("mobile") == null || "".equals(param.get("mobile"))) {
			map.put("code", 400);
			map.put("message", "手机号不能为空");
			return map;
		}
		if (param.get("name") == null || "".equals(param.get("name"))) {
			param.put("name", null);
		}
		param.put("id", id);
		if (orderService.updateNameAndMobile(param) != 1) {
			map.put("code", 400);
			map.put("message", "修改失败");
			return map;
		}
		map.put("code", 200);
		map.put("message", "OK");
		return map;
	}
	
	/**
	 * 下单 提交订单
	 * 
	 * @param order
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/skus/orders", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> addSkusOrders(@RequestBody Order order,
	HttpServletResponse response, HttpSession session) {
 
		Map<String, Object> data = new HashMap<String, Object>();

		User user = (User) session.getAttribute(Constants.USER);
		Integer sessionCustId = user.getId();

		if (orderService.getOrderCountByCustIdAndStatus(sessionCustId, 1) > 2) {
			data.put("code", 400);
			data.put("message", "未付款订单过多！");
			return data;
		}

		order.setCustId(sessionCustId);
		order.setStatus(1);
		try {
			data = orderService.addSkusOrder(user, order);
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 400);
			data.put("message", e.getMessage());
			data.put("exception", e.getMessage());
			logg.info(e.getStackTrace());
		}
		return data;
	}
}
