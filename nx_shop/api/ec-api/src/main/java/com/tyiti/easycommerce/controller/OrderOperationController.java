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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderReturn;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.OrderOperationService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.PickupOrderService;
import com.tyiti.easycommerce.service.PickupPointService;

@Controller
public class OrderOperationController {

	@Autowired
	private OrderOperationService orderOperationService;
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private PickupOrderService pickupOrderService ; 
	
	@Autowired
	private PickupPointService  pickupPointService;
	/**
	 * 申请取消订单（已制单、已发货）
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/cancellations", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> applyToCancel(@RequestBody OrderCancellation orderCancellation,HttpSession session) {
		Map<String, Object> data = orderOperationService.applyToCancel(session,orderCancellation);
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
		Map<String, Object> data =  new HashMap<String, Object>();
			try {
				data =	orderOperationService.cancel(session, id);
			} catch (Exception e) {
				// TODO: handle exception
				data.put("code", 400);
				if(e.getMessage() == null || e.getMessage().length()>30){
					data.put("message", "操作失败请联系管理员");
				}
				data.put("message", e.getMessage());
			}
				
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
	public synchronized Map<String, Object> applyToReturn(
			@RequestBody OrderCancellation orderCancellation,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.applyToReturn(session,
				orderCancellation);
		return data;
	}

	/**
	 * @description 申请退货上传图片
	 * @author wyy 2016/07/08
	 * @param
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/img", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> imgToReturn(
			@RequestParam(value = "mediaId", required = true) String mediaId,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.returnImg(session,
				mediaId);
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

		List<OrderCancellation> listOrderCancellation = orderOperationService
				.getReturnList(session, offset, limit);

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
		OrderCancellation orderCancellation = orderOperationService
				.getReturnDetail(session, id);
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		Order orderSku = orderService.getOrderDetail(orderCancellation.getOrderId(), custId);
		//获取支付信息 退货详情 暂时不显示
//		List<OrderPayment> orderPaymentList = orderPaymentService.selectOrderPaymentByOrderId(orderSku.getId());
//		orderSku.setOrderPaymentList(orderPaymentList);
//		//获取自提点信息
		PickupOrder pickupOrder = pickupOrderService.selectByOrderId(id);
		if(pickupOrder!=null){
			if(pickupOrder.getStatus() !=0){
				orderSku.setPickupCode(pickupOrder.getCode());
			}
			PickupPoint pickupPoint = pickupPointService.selectPickupPointById(pickupOrder.getPickupPointId());
			orderSku.setPickupPoint(pickupPoint);
		}
		orderCancellation.setOrder(orderSku);
		data.put("data", orderCancellation);
		return data;
	}
	
	/**
	 * 
	 * @Title: cancel 撤销 申请取消操作   
	 * @Description: status 0 状态改为 2
	 * @param id 
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年7月13日 上午10:07:29
	 */
	@RequestMapping(value ="cancelcancellation/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> cancelOrderCallication(@PathVariable("id") Integer id,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			OrderCancellation orderCancellation=orderOperationService.getReturnDetail(session, id);
			if(orderCancellation==null){
				throw new Exception("参数不合法！");
			}else{
				if(orderCancellation.getStatus()!=0 && orderCancellation.getStatus()!=3){
					throw new Exception("此订单不能取消！");
				}else{
					if(orderCancellation.getStatus()==3){
						Order order=orderService.getById(orderCancellation.getOrderId());
						PickupOrder pickupOrder=pickupOrderService.selectByOrderId(order.getId());
						if(pickupOrder!=null){
							pickupOrder.setStatus(3);
							pickupOrderService.updateStus(pickupOrder);
						}
						
					}
						
				}
				orderCancellation.setStatus(2);
				orderOperationService.updateByprimary(orderCancellation);
			}
			data.put("code", 200);
			data.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			data.put("code", 400);
			data.put("message", e.getMessage());
		}
		return data;
	}
	
	/**
	 * 申请退货
	 * 
	 * @param orderCancellation
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/orders/returns", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public synchronized Map<String, Object> orderReturn(
			@RequestBody OrderReturn orderReturn,
			HttpSession session) {
		Map<String, Object> data = orderOperationService.orderReturn(session,
				orderReturn);
		return data;
	}
	
	@RequestMapping(value = "/returns", method = RequestMethod.GET)
	@ResponseBody
	public synchronized Map<String, Object> returns(
			@RequestParam Map<String,Object> params,
			HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		params.put("custId", custId);
		Map<String, Object> map  = new HashMap<String, Object>();
		try {
			SearchResult<Map<String,Object>> result =   orderOperationService.selectReturnsListByParmas(params);
			map.put("code", 200);
			map.put("data", result);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}
	
	/**
	* @Title: cancelReturns
	* @Description: 取消退货申请
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/cancel/returns/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public synchronized Map<String, Object> cancelReturns(
			@PathVariable Integer id ,
			HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer custId = user.getId();
		Map<String, Object> map  = new HashMap<String, Object>();
		try {
			map =  orderOperationService.cancelReturns(custId,id) ; 
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message","系统错误请联系管理员");
			map.put("exception", e.getMessage());
		}
		
		return map;
	}
}
