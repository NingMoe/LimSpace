package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.service.CancellationService;
import com.tyiti.easycommerce.service.OrderPaymentService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.util.LogUtil;

@Scope("prototype")
@Controller
public class CancellationController {
	@Autowired
	private CancellationService cancellationService ; 
	
	@Autowired
	private OrderPaymentService orderPaymentService;
	
	@Autowired
	private OrderService orderService ;
	
	@RequestMapping(value = "/cancels",  method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> orderCancel(@RequestParam Map<String, Object> param){
		
		Map<String, Object> map = new HashMap<String, Object>();
		param.put("cancelList", true);
		SearchResult<Map<String, Object>> orderList = null ; 
		try {
			orderList= this.orderService.getOrdersSkus(param);
			if (null != param.get("id")) {
				Integer id = Integer.parseInt(String.valueOf(param.get("id")));
				List<Map<String, Object>> logs = LogUtil.logs(id,
						LogEnum.OperateModel.CANCEL.getKey());
				map.put("logs", logs);
				List<OrderPayment> paymentList = orderPaymentService.getByOrderId(id);
				List<Map<String, Object>> discountList = orderPaymentService
						.getDiscountDetailByOrderId(id);
				map.put("discount", discountList);
				map.put("orderPayment", paymentList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","退货查询出错,请联系管理员");
		    return map ;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
	    map.put("data", orderList);
		
		return map;
		
	}
	
	/**
	* @Title: orderDetail
	* @Description: TODO( 获取退货订单详情)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/cancels/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderDetail(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> orderCancelDetail = null  ; 
		try {
			orderCancelDetail = 	cancellationService.getOrderCancelDetail(id);
			List<Map<String, Object>> logs  = LogUtil.logs(orderCancelDetail.get("id") ,LogEnum.OperateModel.CANCELLATION.getKey());
			map.put("logs",logs);
			List<Map<String,Object>> discountList =    orderPaymentService.getDiscountDetailByOrderId(Integer.parseInt(String.valueOf(orderCancelDetail.get("orderId"))));
			map.put("discount",discountList);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","退货详情出错,请联系管理员");
			return map ; 
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", orderCancelDetail);

		return map;
	}
	/**
	* @Title: cancelAgree
	* @Description: TODO(申请取消同意)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/cancels/agree/{id}", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public synchronized Map<String, Object> cancelsAgree(@PathVariable int id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Integer status = 1 ; //同意 
			  map =  cancellationService.orderCancel(id,status);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","退货单确定出错,请联系管理员");
			return map ; 
		}
		return map;
	}
	
	/**
	* @Title: orderCancelRefuse
	* @Description: TODO(退货取消 拒绝)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/cancels/refuse/{id}", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public synchronized Map<String, Object> orderCancelRefuse(@PathVariable int id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Integer status = 2; //拒绝 
			map = cancellationService.orderCancel(id ,status );
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","拒绝退货申请出错,请联系管理员");
			return map ; 
		}
		return map;
	}
	
}
