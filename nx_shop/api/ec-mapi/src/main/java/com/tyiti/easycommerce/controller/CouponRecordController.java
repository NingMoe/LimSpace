package com.tyiti.easycommerce.controller;

import java.util.HashMap;
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
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.service.CouponRecordService;

@Scope("prototype")
@Controller
@RequestMapping("/couponRecords")
public class CouponRecordController {

	@Autowired
	private CouponRecordService couponRecordService;

	/**
	 * 添加一条优惠券记录 优惠券限制同一用户领取同一张优惠券？？   检查优惠券数量
	 * 发送短信
	 * @param couponRecord
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> addCouponRecord(@RequestBody CouponRecord couponRecord) {
		Map<String, Object> map = new HashMap<String, Object>();
		int num = couponRecordService.addOneCouponRecord(couponRecord);
		if (num != 1) {
			map.put("code", "400");
			map.put("message", "添加失败");
		} else {
			map.put("code", "200");
			map.put("message", "OK");
		}
		return map;
	}
	/**
	 * 通过手机号给用户发放一张优惠券  优惠券限制同一用户领取同一张优惠券   检查优惠券数量
	 * 发送短信
	 * @param couponRecord
	 * @return
	 */
	@RequestMapping(value = "/send/{couponId}&{mobile}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> sendOneCoup(@PathVariable("couponId") Integer couponId,@PathVariable("mobile") String mobile) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			couponRecordService.sendOneCoup(couponId,mobile);
			map.put("code", "200");
			map.put("message", "发送成功");
		}catch (Exception e) {
			map.put("code", "400");
			map.put("message", "发送失败:"+e.getMessage());
		}
		return map;
	}
	
	/**
	 * 根据id查询单条优惠券记录
	 * @param id
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/couponRecord/{id}",  method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer id, HttpServletResponse response ) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data = couponRecordService.queryById(id);
		map.put("data", data);
		return map;
	}
	/**
	 * 根据id查询单条优惠券详细记录
	 * @param id
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/couponRecordDetail/{id}",  method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDetailById(@PathVariable("id") Integer id, HttpServletResponse response ) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data = couponRecordService.queryDetailById(id);
		map.put("data", data);
		return map;
	}
	/**
	 * 用户优惠券分页
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "",  method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> infoList(@RequestParam Map<String, Object> param, HttpServletResponse response ) {
		Map<String, Object> map = new HashMap<String, Object>();
		SearchResult<Map<String, Object>> resultList = null; 
		resultList= couponRecordService.queryInfoListByPage(param);
	    map.put("data", resultList);
		return map;
	}
	/**
	 * 根据id删除记录
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/couponRecord/del/{id}",  method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delById(@PathVariable("id") Integer id, HttpServletResponse response ) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = couponRecordService.removeById(id);
		if (!flag) {
			map.put("code", "400");
			map.put("message", "删除失败");
		} else {
			map.put("code", "200");
			map.put("message", "OK");
		}
		return map;
	}
	/**
	 * 根据id批量删除删除记录
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/couponRecord/dels/{ids}",  method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delById(@PathVariable("ids") String ids, HttpServletResponse response ) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			couponRecordService.removeById(ids);
			map.put("code", "200");
			map.put("message", "OK");
		} catch (Exception e) {
			map.put("code", "400");
			map.put("message", "删除失败:"+e.getMessage());
		}
		return map;
	}
	/**
	 * 修改优惠券记录信息
	 * @param couponRecord
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/couponRecord/edit",  method = RequestMethod.PUT,headers = {"Content-type=application/json"})
	@ResponseBody
	public Map<String, Object> delById(@RequestBody CouponRecord couponRecord, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		int num = couponRecordService.updateCouponRecord(couponRecord);
		if (num != 1) {
			map.put("code", "400");
			map.put("message", "修改失败");
		} else {
			map.put("code", "200");
			map.put("message", "OK");
		}
		return map;
	}
}
