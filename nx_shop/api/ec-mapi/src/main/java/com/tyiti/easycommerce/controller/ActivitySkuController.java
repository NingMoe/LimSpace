package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.ActivitySkuService;
import com.tyiti.easycommerce.util.UnicodeUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
@Controller
public class ActivitySkuController {

	Logger logger = Logger.getLogger(ActivitySkuController.class);
	
	@Autowired
	private ActivitySkuService activitySkuService;
	
	@Autowired
	private ActivityService activityService;
	
	
	
	/**
	 * 获取所有sku(过滤掉参加秒杀活动的sku)
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value="activitySku/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAllSku(@PathVariable("id") Integer id,
			HttpServletResponse response ) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Sku> skuList = activitySkuService.getAllSku(id);
			map.put("code", 200);
			map.put("skuList", skuList);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","sku查询失败。");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据活动id查询所有参与此次活动的sku信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="activitySku/list/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getActivitySkuById(@PathVariable("id") Integer id){
		Map<String, Object> map= new HashMap<String, Object>();
		try {
			List<ActivitySku> activitySkuList = activitySkuService.getActivitySkuById(id);
			map.put("code", 200);
			map.put("activitySkuList", activitySkuList);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","sku查询失败。");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据条件筛选查询sku
	 * @param param
	 * param:1)分类 2)sku状态 3)skuId
	 * @return
	 */
	@RequestMapping(value="activitySku", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchByConditions(@RequestParam Map<String, Object> param){
		Map<String, Object> map= new HashMap<String, Object>();
		try {
			List<Sku> skuList = activitySkuService.searchByConditions(param);
			map.put("code", 200);
			map.put("skuList", skuList);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","sku查询失败。");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 保存参加活动的sku信息
	 * @param activitySku
	 * @param request
	 * @return
	 */
	@RequestMapping(value="activitySku", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveActivitySku(@RequestParam String activitySku, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		List<ActivitySku> skuList = new ArrayList<ActivitySku>();
		skuList = this.getActivitySkuListParam(activitySku);
		try {
			if (skuList != null && skuList.size()>0) {
				Integer count = activitySkuService.saveActivitySku(skuList);
				if (count!=null && count>0) {
					logger.info("》》》》活动创建成功后，修改t_sku表中库存数。《《《《");
					activitySkuService.updateSkuInventory(skuList);
					logger.info("成功保存参加活动的sku的相关信息。");
					/**
					 * 活动创建成功后，判断活动开始时间是否已经开始，如果开始则修改活动状态为已开始
					 */
					Integer activityId = skuList.get(0).getActivityId();
					activityService.updateActivityStatus(activityId);
					map.put("code", 200);
					map.put("message", "保存成功");
				}else {
					map.put("code", 505);
					map.put("message","创建活动失败，同一时间段，同一个sku只能参加一个活动。");
					logger.info("创建活动失败，同一时间段，同一个sku只能参加一个活动。");
				}
			}else {
				map.put("code", 500);
				map.put("message","创建活动时，没有选择相关的sku。");
				logger.info("创建活动时，没有选择相关的sku。");
			}
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","保存参加活动的sku信息失败。");
			logger.info("保存参加活动的sku的相关信息失败。");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 补仓
	 * @param id  参加活动的sku在t_activity_sku表中的id
	 * @param inventory  新增库存数
	 * @return
	 */
	@RequestMapping(value="activitySku/{id}/{inventory}", method=RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> updateReservedInventory(@PathVariable("id") Integer id, @PathVariable("inventory") Integer inventory){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id", id);
			param.put("inventory", inventory);
			activitySkuService.updateReservedInventoryById(param);
			map.put("code", 200);
			map.put("message", "补仓成功");
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message","补仓失败。");
			logger.info("补仓失败。");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取请求数据
	 * 把前端传递的参数解析成对象，并存储在list集合中
	 * @param activitySkuStr
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	private List<ActivitySku> getActivitySkuListParam(String activitySkuStr){
		List<ActivitySku> skuList = new ArrayList<ActivitySku>();
		String tempParam = UnicodeUtil.convert(activitySkuStr);
		JSONArray activitySkuArray = JSONArray.fromObject(tempParam);
		Iterator iterator = activitySkuArray.iterator();
		while (iterator.hasNext()) {
			JSONObject activitySkuObject = JSONObject.fromObject(iterator.next());
			ActivitySku activitySku = (ActivitySku) JSONObject.toBean(activitySkuObject, ActivitySku.class);
			skuList.add(activitySku);
		}
		logger.info("参加本次活动的sku一共有 "+skuList.size()+" 个。");
		return skuList;
	}
	/****
	  * <p>功能描述：修改数据库，把选择的商品置顶。</p>	
	  * @return
	  * <p>创建日期:2016年10月17日 下午2:46:17。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "updateActivitySkuTop/{id}", method = RequestMethod.GET)
    @ResponseBody
	public Map<String, Object> updateActivitySkuTop(@PathVariable("id") Integer id){
		Map<String, Object> map= new HashMap<String, Object>();
		map = activitySkuService.updateActivitySkuTop(id);
		map.put("code", 200);
		map.put("message", "数据置顶成功！");
		return map;
	}
}
