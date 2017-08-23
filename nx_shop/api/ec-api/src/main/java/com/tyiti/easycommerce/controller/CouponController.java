package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.CouponService;

/**
 * @author:wangqi
 * @date:2016-3-30 下午6:23:48
 * @description:优惠券控制层
 */
@Controller
@RequestMapping("/coupons")
public class CouponController {
	@Autowired
	private CouponService couponService;

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 根据用户id获取该用户优惠券 1. 按照 优惠券 类型分类 2 .按照优惠券使用场合分类 3.按照是否 使用 分类
	 * 
	 * @param response
	 * @param session
	 * @return
	 */


	@RequestMapping(value = "/mine", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getByCustId(
			@RequestParam Map<String, Object> param,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
	
		SearchResult<Map<String, Object>> result = couponService
				.getMyCouponRecord(user, param);
		data.put("data", result);
		return data;
	}
	/**
	* @Title: couponReceive
	* @Description: 领取优惠券
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/{couponId}/receive", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> couponReceive(
			@PathVariable("couponId") Integer id, HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		try {
			// 获取用户优惠券信息
			couponService.recevieCouponRecord(user.getId(), id);
			map.put("code", 200);
			map.put("message", "领取成功");
		} catch (Exception e) {
			map.put("code", 400);
			map.put("message", "领取失败");
			map.put("exception", e.getMessage());
		}

		return map;
	}

	
	@RequestMapping(value = "/code/receive", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> couponCodeReceive(
			 String code , HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		
		try {
			// 获取用户优惠券信息
			couponService.recevieCouponRecordByCode(user.getId(), code);
			map.put("code", 200);
			map.put("message", "领取成功");
		} catch (Exception e) {
			map.put("code", 400);
			map.put("message", "领取失败");
			map.put("exception", e.getMessage());
		}

		return map;
	}
	/**
	 * 根据订单判断用户可使用优惠券列表
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/skus/{id}&{num}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer skuId,
			@PathVariable("num") Integer num, HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		List<CouponRecord> couponRecord = null;
		try {
			// 获取用户优惠券信息
			couponRecord = couponService.getCouponRecord(user.getId(), skuId,num);
			map.put("code", 200);
			map.put("data", couponRecord);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			log.error(e);
		}

		return map;
	}
	
	
	/**
	 * @Title: getCoupon 
	 * @Description:在购物车中去结算
	 * @param skus Sku数组
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年8月8日 下午5:07:02
	 */
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getCoupon(@RequestBody Order order,HttpSession session){
		Map<String, Object> map=new HashMap<String, Object>();
		List<CouponRecord>  couponRecords=new ArrayList<CouponRecord>();
		User user = (User) session.getAttribute(Constants.USER);
		try {
			List<Sku> skus=order.getSku();
			couponRecords=couponService.getAllCartCoupon(skus,user.getId());
			map.put("code", 200);
			map.put("data",couponRecords);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e);
		}
		
		return map;
	}

}
