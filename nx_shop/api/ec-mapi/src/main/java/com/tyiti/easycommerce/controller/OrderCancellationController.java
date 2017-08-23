package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.service.OrderCancellationService;
import com.tyiti.easycommerce.service.OrderPaymentService;
import com.tyiti.easycommerce.util.LogUtil;
@Scope("prototype")
@Controller
public class OrderCancellationController {
	
	@Autowired
	private OrderCancellationService orderCancellationService ; 
	
	@Autowired
	private OrderPaymentService orderPaymentService;
	
	@RequestMapping(value = "/orders/returns",  method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> orderCancel(@RequestParam Map<String, Object> param){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		SearchResult<Map<String, Object>> orderList = null ; 
		try {
			orderList= this.orderCancellationService.getOrderCancelList(param);
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
	@RequestMapping(value = "/orders/returns/details/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> orderDetail(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> orderCancelDetail = null  ; 
		try {
			orderCancelDetail = 	orderCancellationService.getOrderCancelDetail(id);
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
	* @Title: orderMakeSure
	* @Description: TODO(退货确定)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/orders/returns/enter", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> orderMakeSure(@RequestBody int[] ids,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		//处理批量参数问题
		Map<String, Object> param  = new HashMap<String, Object>();
		param.put("ids", ids);
		try {
			 orderCancellationService.orderCancelMakeSure(param);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","退货单确定出错,请联系管理员");
			return map ; 
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}
	
	/**
	* @Title: orderCancelRefuse
	* @Description: TODO(退货取消 拒绝)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/orders/returns/refuse", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> orderCancelRefuse(@RequestBody int[] ids,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		//处理参数 转换成数组 用于批量处理
		Map<String, Object> param  = new HashMap<String, Object>();
		param.put("ids", ids);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int num = 0 ;
		try {
			resultMap = orderCancellationService.orderCancelRefuse(param);
			num	= Integer.parseInt(String.valueOf(resultMap.get("num"))) ; 
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","拒绝退货申请出错,请联系管理员");
			return map ; 
		}
		if(num == 0){
			map.put("code", 400);
			map.put("messsge",resultMap.get("message"));
		}else{
			map.put("code", 200);
			map.put("messsge", "OK");
		}
		return map;
	}
	/****
	  * <p>功能描述：。</p>	
	  * @param ids
	  * @param response
	  * @return
	  * <p>创建日期:2016年8月26日 上午9:34:40。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "/orders/returns/delivery", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> orderCancelDelivery(@RequestBody int[] ids,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param  = new HashMap<String, Object>();
		param.put("ids", ids);
		  
		try {
			orderCancellationService.orderCancelDelivery(param);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","确定收货出错,请联系管理员");
			return map ; 
		}
			map.put("code", 200);
			map.put("messsge", "OK");
		return map;
	}
	 
}
