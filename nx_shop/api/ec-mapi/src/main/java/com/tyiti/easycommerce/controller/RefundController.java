package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.tyiti.easycommerce.service.OrderPaymentService;
import com.tyiti.easycommerce.service.RefundService;
import com.tyiti.easycommerce.util.LogUtil;
/**
 * 
* @ClassName: RefundController
* @Description: TODO(退款)
* @author yanzy
* @date 2016-3-23 下午5:31:10
*
 */
@Scope("prototype")
@Controller
public class RefundController {
	@Autowired
	private RefundService refundService;
	 
	@Autowired
	private OrderPaymentService orderPaymentService;
	 /**
	  * 
	 * @Title: refundList
	 * @Description: TODO(获取退款列表、分页、条件查询)
	 * @param  order
	 * @param  requset
	 * @return Map<String,Object>    返回类型
	 * @author Yan Zuoyu
	 * @throws 查询出错
	  */
	@RequestMapping(value = "/refunds",  method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> refundList( @RequestParam Map<String, Object> param  ,
			HttpServletResponse response ) {
	 
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		SearchResult<Map<String, Object>> refundList = null ; 
		try {
			refundList= this.refundService.getRefundList(param);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","订单查询出错,请联系管理员");
		    return map ;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
	    map.put("data", refundList);

		return map;
	}
 
	/**
	* @Title: refundDetail
	* @Description: TODO(退款详情)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/refunds/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> refundDetail(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> refundDetail = null  ; 
		try {
			refundDetail = 	refundService.getRefundDetail(id);
			List<Map<String, Object>> logs = new ArrayList<Map<String, Object>>();
			List<Map<String,Object>> discountList = orderPaymentService.getDiscountDetailByOrderId(Integer.parseInt(String.valueOf(refundDetail.get("orderId"))));
			map.put("discount",discountList);
			logs = LogUtil.logs(refundDetail.get("id") ,LogEnum.OperateModel.REFUND.getKey());
			map.put("logs",logs);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","订单详情出错,请联系管理员");
			return map ; 
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", refundDetail);

		return map;
	}
	
	@RequestMapping(value = "/refunds/enter", method = RequestMethod.PUT, headers = {"Content-type=application/json"})
	@ResponseBody
	public Map<String, Object> refundMakeSure(@RequestBody int id,HttpServletResponse response,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			refundService.refundMakeSure(id);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","退款确定出错,请联系管理员");
			return map ; 
		}
			map.put("code", 200);
			map.put("messsge", "OK");
		return map;
	} 
	/**
	* @Title: refunds
	* @Description: 退款列表
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/refunds/skus",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> refunds(@RequestParam Map<String,Object> param) {
		Map<String,Object> map = new HashMap<String, Object>();
		SearchResult<Map<String, Object>> refundsList = null ; 
		try {
			refundsList = refundService.selectRefundsSkusList(param); 
			map.put("code", 200);
			map.put("data", refundsList);
			if(null != param.get("id")){
				List<Map<String, Object>> logs = new ArrayList<Map<String, Object>>();
				List<Map<String,Object>> discountList = orderPaymentService.getDiscountDetailByOrderId(Integer.parseInt(String.valueOf(refundsList.getRows().get(0).get("orderId"))));
				map.put("discount",discountList);
				logs = LogUtil.logs(param.get("id") ,LogEnum.OperateModel.REFUND.getKey());
				map.put("logs",logs);
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询退款订单出错请联系管理员");
			map.put("exception", e.getMessage());
		}
		return map ; 
	}
	/**
	* @Title: refundMake
	* @Description: 执行退款操作
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/refunds/make/{id}", method = RequestMethod.PUT, headers = {"Content-type=application/json"})
	@ResponseBody
	public Map<String, Object> refundMake(@PathVariable int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = refundService.refundMake(id);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","退款确定出错,请联系管理员");
			return map ; 
		}
		return map;
	} 
}
