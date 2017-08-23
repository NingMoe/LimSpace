package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.Tag;
import com.tyiti.easycommerce.service.CouponService;
import com.tyiti.easycommerce.service.SkuService;
import com.tyiti.easycommerce.service.TagService;

/**
 * @author yanzy
 * @date 2016-6-3 
 * @description TODO
 */
@Scope("prototype")
@Controller
@RequestMapping("/coupons")
public class CouponController {

	@Autowired
	private CouponService couponService;

	@Autowired
	private TagService tagService ;
	@Autowired
	private SkuService skuService ; 
	/**
	 * 新增优惠券
	 * 
	 * @param coupon
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> addCouponRecord(
			@RequestBody Coupon coupon) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			couponService.addCoupon(coupon);
			map.put("code", 200);
			map.put("message", "OK");
		} catch (Exception e) {
			map.put("code", 400);
			map.put("message", "添加失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

	/**
	 * 编辑优惠券
	 * 
	 * @param coupon
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> editCoupon(
			@RequestBody Coupon coupon, HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			couponService.updateCoupon(coupon);
			map.put("code", 200);
			map.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "修改失败");
			map.put("exception", e.getMessage());
		}
		
		 
		return map;
	}

	/**
	 * 删除单个优惠券
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> deleteCoupon(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			 couponService.deleteCoupon(id);
			 map.put("code", 200);
				map.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "删除失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}
	/**
	 * 结束优惠券
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/stop/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> stopCoupon(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			 couponService.stopCoupon(id);
			 map.put("code", 200);
				map.put("message", "OK");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "结束失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

	/**
	 * 分页查询优惠券
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> infoList(
			@RequestParam Map<String, Object> param,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		SearchResult<Map<String, Object>> resultList = null;
		try {
			resultList = couponService.queryInfoListByPage(param);
			map.put("code", 200);
			map.put("data", resultList);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> Coupon(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Coupon coupon = couponService.getCouponById(id);
			if(coupon.getScope() == 3 ){
				//获取标签信息
				List<Integer>  tagIds =  findTagIds(coupon.getRefId());
				map.put("tag", tagIds);
			}else if(coupon.getScope()==2){
				//获取sku信息 及标签信息
				Sku sku = skuService.getSkuById(coupon.getRefId());
				map.put("sku", sku);
			}
			
			map.put("code", 200);
			map.put("data", coupon);
		} catch (Exception e) {
//			 TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}
	List<Integer> tagIds =  new ArrayList<Integer>();
	private List<Integer> findTagIds(Integer refId){
		
		Tag tag = tagService.getById(refId);
		if(tag!=null && tag.getParentId() != null){
			tagIds.add(tag.getId());
			if(tag.getParentId() != 0){
				findTagIds(tag.getParentId());
			}
			
		}
		return tagIds;
	}
}
