 package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.PickupPointUser;
import com.tyiti.easycommerce.service.OrderOperationService;
import com.tyiti.easycommerce.service.OrderPaymentService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.PickupOrderService;
import com.tyiti.easycommerce.service.PickupPointService;

@Controller
@RequestMapping("/pickuporders")
public class PickupOrderController {

	@Autowired
	private PickupOrderService pickupOrderService;

	@Autowired
	private PickupPointService pickupPointService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderPaymentService orderPaymentService;

	@Autowired
	private OrderOperationService orderOperationService;

	/**
	 * 
	 * @Title: getList
	 * @Description: 根据订单状态 查询自提订单
	 * @param param
	 * @param session
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @author hcy
	 * @date 2016年7月6日 上午9:34:11
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getList(@RequestParam Map<String, Object> param,
			HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		SearchResult<Map<String, Object>> pickupOrderLists = null;
		try {
			PickupPointUser pickupPointUser = (PickupPointUser) Constants.pointUserMap
					.get(request.getSession().getId());
			if (pickupPointUser == null) {
				data.put("code", "401");
				data.put("message", "请登录!");
				return data;
			}
			int type = pickupPointUser.getType();
			param.put("type", type);
			if (type == 2) {
				if(!param.containsKey("status") || param.get("status").equals("") ){
//					data.put("code", 400);
//					data.put("message", "信息异常");
//					return data;
				}else{
					if (!param.get("status").equals("4")
							&& !param.get("status").equals("5")
							&& !param.get("status").equals("6")
							&& !param.get("status").equals("11")) {
						data.put("code", 400);
						data.put("message", "信息异常");
						return data;
					}
				}
				
				//判断map里面的pickupPointId 是否合法
				String pickupPointId=(String)param.get("pickupPointId");
				if(pickupPointUser.getPickupPointId().indexOf(pickupPointId)>-1){
					param.put("pickupPointId", pickupPointId);
				}else{
					data.put("code", 400);
					data.put("message", "传入自提点不正确！");
					return data;
				}
				
				
			}else{
				param.put("pickupPointId",pickupPointUser.getPickupPointId());
			}
			// type 1: 营业厅人员 2: 配送员/验货员
			// param.put("status", param.get("status").toString().split(","));
			/*param.put("pickupPointId", pickupPointUser.getPickupPointId()
					.split(","));*/
			pickupOrderLists = pickupOrderService.pickupOrderLists(param);
			data.put("code", "200");
			data.put("data", pickupOrderLists);
		} catch (Exception e) {
			data.put("code", 400);
			data.put("exception", e.getMessage());

		}
		return data;
	}

	/***
	 * 
	 * @Title: getPickUpOrder
	 * @Description: 根据主键查询自提订单详情
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @author hcy
	 * @date 2016年7月6日 上午9:34:56
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPickUpOrder(@PathVariable("id") Integer id,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		PickupPointUser pickupPointUser = (PickupPointUser) Constants.pointUserMap
				.get(request.getSession().getId());
		if (pickupPointUser == null) {
			map.put("code", 401);
			map.put("message", "请登录后操作");
			return map;
		}
		PickupOrder pickupOrder = pickupOrderService.selectPickupOrderById(id);
		if (pickupOrder == null) {
			map.put("code", 400);
			map.put("message", "订单不存在");
			return map;
		}
		int is_include = 0;
		String pickupPointIds = pickupPointUser.getPickupPointId();
		String[] pointIds = pickupPointIds.split(",");
		for (String ids : pointIds) {
			if (pickupOrder.getPickupPointId().equals(Integer.parseInt(ids))) {
				is_include = 1;
			}
		}
		if (is_include == 0) {
			map.put("code", 400);
			map.put("message", "无访问权限");
			return map;
		}
		Order order = orderService.getById(pickupOrder.getOrderId());
		Order orderSku = orderService.getOrderDetail(pickupOrder.getOrderId(),
				order.getCustId());
		// 获取支付信息
		List<OrderPayment> pickOrderPaymentList = orderPaymentService
				.selectOrderPaymentByOrderId(orderSku.getId());
		pickupOrder.setOrderPaymentList(pickOrderPaymentList);
		PickupPoint pickupPoint = pickupPointService
				.selectPickupPointById(pickupOrder.getPickupPointId());
		OrderCancellation orderCancellation = orderOperationService
				.selectorderOperationByOrderId(orderSku.getId());
		pickupOrder.setOrderCancellation(orderCancellation);
		pickupOrder.setOrder(orderSku);
		pickupOrder.setPickupPoint(pickupPoint);
		map.put("code", 200);
		map.put("message", "OK");
		map.put("data", pickupOrder);

		return map;
	}

	/**
	 * 
	 * @Title: updateStatus
	 * @Description: 收货 提货
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 * @return Map<String,Object>
	 * @throws
	 * @author hcy
	 * @date 2016年7月6日 上午9:42:05
	 */
	@RequestMapping(value = "/{id}/status/{status}", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseBody
	public synchronized Map<String, Object> updateStatus(@PathVariable("id") Integer id,
			@PathVariable("status") Integer status, String code,
			HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			/**
			 * t_pickup_order 状态，0: 初始(订单生成) 1: 已付款 2: 已收货(货物已到自提点) 3: 已自提(已签收)
			 * 4: 退货中 5: 已拒绝 6: 已退货
			 * 
			 * t_order 订单状态，0: 草稿 1: 未付款 2: 已付款 3: 已确认 4: 已制单 5: 已发货 6: 已签收 9:
			 * 已取消
			 * 
			 * t_cancellation 状态，0: 初始(已提交) 1: 已确认 2: 已拒绝 3: 待收货 4: 已收货 5: 完成
			 * (3、4、5只对退货)
			 */
			//String code=request.getParameter("code");
			
			PickupPointUser pickupPointUser = (PickupPointUser) Constants.pointUserMap
					.get(request.getSession().getId());
			if (pickupPointUser == null) {
				map.put("code", 401);
				map.put("message", "请登录后操作");
				return map;
			}
			PickupOrder pickupOrder = pickupOrderService
					.selectPickupOrderById(id);
			if (pickupOrder == null) {
				map.put("code", 400);
				map.put("message", "订单不存在");
				return map;
			}
			int is_include = 0;
			String pickupPointIds = pickupPointUser.getPickupPointId();
			String[] pointIds = pickupPointIds.split(",");
			for (String ids : pointIds) {
				if (pickupOrder.getPickupPointId().equals( Integer.parseInt(ids))) {
					is_include = 1;
				}
			}
			if (is_include == 0) {
				map.put("code", 400);
				map.put("message", "无访问权限");
				return map;
			}

			pickupOrderService.changeStatus(id, status,code);
			map.put("code", 200);
			map.put("message", "OK");

		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", e.getMessage());
		}
		return map;

	}

}
