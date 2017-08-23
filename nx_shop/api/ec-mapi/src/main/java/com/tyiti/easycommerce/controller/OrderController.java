package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.service.OrderPaymentService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.util.LogUtil;

/**
 * 
 * @ClassName: OrderController
 * @Description: TODO(后台订单查询)
 * @author yanzy
 * @date 2016-3-23 下午5:31:10
 *
 */
@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderPaymentService orderPaymentService;

	/**
	 * 
	 * @Title: orderList
	 * @Description: TODO(获取订单列表、分页、条件查询)
	 * @param order
	 * @param requset
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws 查询出错
	 */
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderList(
			@RequestParam Map<String, Object> param,
			HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();

		SearchResult<Map<String, Object>> orderList = null;
		try {
			orderList = this.orderService.getOrderList(param);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "订单查询出错,请联系管理员");
			return map;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", orderList);

		return map;
	}

	/**
	 * @Title: orderDetail
	 * @Description: TODO(订单详情)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderDetail(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> orderSku = null;
		try {
			orderSku = orderService.getOrderDetail(id);
			List<Map<String, Object>> logs = LogUtil.logs(id,
					LogEnum.OperateModel.ORDER.getKey());
			map.put("logs", logs);
			List<Map<String, Object>> discountList = orderPaymentService
					.getDiscountDetailByOrderId(id);
			map.put("discount", discountList);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "订单详情出错,请联系管理员");
			return map;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", orderSku);

		return map;
	}

	/**
	 * @Title: orderMakeSure
	 * @Description: TODO(订单确定)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/orders/enter", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public synchronized Map<String, Object> orderMakeSure(@RequestBody int[] ids,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		try {
			orderService.orderMakeSure(param);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "订单确定出错,请联系管理员");
			return map;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * @Title: sendSku
	 * @Description: TODO(发货)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/orders/send", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public synchronized Map<String, Object> sendSku(@RequestBody int[] ids,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);
		try {
			orderService.sendSku(param);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "订单发货出错,请联系管理员");
			return map;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * @Title: sendSku
	 * @Description: TODO(取消订单)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/orders/refuse", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public synchronized Map<String, Object> orderCancelled(@RequestBody int id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			orderService.orderCancel(id);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "订单取消出错,请联系管理员");
			return map;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/***
	 * <p>
	 * 功能描述：根据信息查询条件。
	 * </p>
	 * 
	 * @return
	 * @since JDK1.7。
	 *        <p>
	 *        创建日期:2016年7月8日 上午9:43:27。
	 *        </p>
	 *        <p>
	 *        更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。
	 *        </p>
	 */
	@RequestMapping(value = "/findKooOrders",method = RequestMethod.POST)
	@ResponseBody
	public SearchResult<Map<String, Object>> findKooOrders(@RequestBody Map<String, Object> param){
		SearchResult<Map<String, Object>> map = new SearchResult<Map<String, Object>>();
		map = orderService.findKooOrders(param);
		return map;
	}
	
	/***
	 * 导入物流单
	 * @param param {no,lNo,lName}
	 * @return Boolean
	 * @author Huangyi
	 */
	@RequestMapping(value = "/logisticsImport",method = RequestMethod.POST,
			headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> logisticsImport(@RequestBody Order order){
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean b = orderService.logisticsImport(order);
	    map.put("data", b);
		return map;
	}

	/**
	 * @Title: orderSkus
	 * @Description: 一个订单多个商品显示
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/orders/skus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderSkus(@RequestParam Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
		SearchResult<Map<String, Object>> orderList = orderService
				.getOrdersSkus(param);
		map.put("data", orderList);
			if (null != param.get("id")) {
				Integer id = Integer.parseInt(String.valueOf(param.get("id")));
				List<Map<String, Object>> logs = LogUtil.logs(id,
						LogEnum.OperateModel.ORDER.getKey());
				map.put("logs", logs);
				List<OrderPayment> paymentList = orderPaymentService.getByOrderId(id);
				List<Map<String, Object>> discountList = orderPaymentService
						.getDiscountDetailByOrderId(id);
				map.put("discount", discountList);
				map.put("orderPayment", paymentList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/toordersys", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> toOrderSys(@RequestBody int[] ids  ) {
		 Map<String, Object> param = new HashMap<String, Object>();
		 param.put("ids", ids);
		Map<String, Object> data = orderService.toOrderSys(param); 
		
		return data;
		
	}
}
