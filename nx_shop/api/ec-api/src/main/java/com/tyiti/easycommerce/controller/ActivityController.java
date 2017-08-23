 /**
  * 文件名[fileName]：ActivityController.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年7月19日 下午5:56:43
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.SkuService;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.7。
  *<p>创建日期：2016年7月19日 下午5:56:43。</p>
  */
@Controller
public class ActivityController {
	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	ActivityService activityService;
	@Autowired
	private SkuService skuService;
	/**
	  * <p>功能描述：根据活动ID查询参加活动的商品。</p>	
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月19日 下午4:44:53。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "/findActivitySku", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String,Object> findSpikeActivitySkuList(@RequestBody ActivitySku activitySku,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		Integer activityId = activitySku.getActivityId();
		Integer skuId = activitySku.getSkuId();
		List<Map<String, Object>> activitySkuList = activityService.findSpikeActivitySkuList(activityId,skuId);
		for(Map<String, Object> activitySkuMap:activitySkuList){
			Date nowDate = new Date();
			int type = (int) activitySkuMap.get("activityType");
			Date startTime = (Date) activitySkuMap.get("startTime");
			Integer activityStats= (Integer) activitySkuMap.get("activityStatus");
			if(type==1){//秒杀
				Date end_date_time = (Date) activitySkuMap.get("endTime");
				if(startTime.before(nowDate)&&end_date_time.after(nowDate)&&activityStats==0){//活动已经开始
					Activity activity = activityService.findByActivityId(activityId);
					activity.setActivityStatus(1);
					Sku sku = skuService.getById(Integer.parseInt(activitySkuMap.get("id").toString()));
					if(sku.getStatus()==0){
						sku.setStatus(1);
						skuService.updateSku(sku);
					}
					map = activityService.updateActivity(activity);
					activitySkuMap.put("activityStatus", 1);
				}
			}else{
				if(startTime.before(nowDate)&&activityStats==0){//活动已经开始
					Activity activity = activityService.findByActivityId(activityId);
					activity.setActivityStatus(1);
					Sku sku = skuService.getById(Integer.parseInt(activitySkuMap.get("id").toString()));
					if(sku.getStatus()==0){
						sku.setStatus(1);
						skuService.updateSku(sku);
					}
					map = activityService.updateActivity(activity);
					activitySkuMap.put("activityStatus", 1);
				}
			}
			activitySkuMap.put("nowTime", System.currentTimeMillis());
		}
		map.put("data", activitySkuList);
		return map;
	}
	/***
	  * <p>功能描述：根据类型查询该活动的List。</p>	
	  * @param activityType
	  * @return
	  * @since JDK1.7。hi
	  * <p>创建日期:2016年7月20日 上午11:22:16。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "/findActivity/{activityType}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> findActivityList(@PathVariable("activityType") Integer activityType){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> activityData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> activityList = activityService.findActivityList(activityType);
		for(Map<String, Object> activityMap:activityList){
			Date nowDate = new Date();
			int type = (int) activityMap.get("activity_type");
			Date startTime = (Date) activityMap.get("start_time");
			Integer activityStats= Integer.parseInt(activityMap.get("activity_status").toString()) ;
		    Calendar calendar = new GregorianCalendar(); 
		    calendar.setTime(nowDate); 
		    calendar.set(Calendar.HOUR_OF_DAY,0); 
		    calendar.set(Calendar.MINUTE,0); 
		    calendar.set(Calendar.SECOND,0); 
		    calendar.set(Calendar.MILLISECOND,0); 
			if(type==1){//秒杀
				Date end_date_time = (Date) activityMap.get("end_time");
				if(startTime.before(nowDate)&&end_date_time.after(nowDate)&&activityStats==0){//活动已经开始
					Activity activity = activityService.findByActivityId(Integer.parseInt(activityMap.get("id").toString()));
					activity.setActivityStatus(1);
					map = activityService.updateActivity(activity);
					//活动开始把该活动下所有的商品改为上架状态
					List<Map<String, Object>> activitySkuList = activityService.findSpikeActivitySkuList(Integer.parseInt(activityMap.get("id").toString()),null);
					for(Map<String, Object> activitySkuMap:activitySkuList){
						Sku sku = skuService.getById(Integer.parseInt(activitySkuMap.get("id").toString()));
						if(sku.getStatus()==0){
							sku.setStatus(1);
							skuService.updateSku(sku);
						}
					}
					activityMap.put("activity_status", 1);
				}
			     
				if(end_date_time.before(nowDate)||activityStats==2){//活动已经结束
					Activity activity = activityService.findByActivityId(Integer.parseInt(activityMap.get("id").toString()));
					activity.setActivityStatus(2);
					if(end_date_time.before(calendar.getTime())){
						activity.setDisplay(1); 
						activityMap.put("display", 1);
				    }
					map = activityService.updateActivity(activity);
					map = activityService.updateActivitySkuList(activity.getId());
					//修改库存
					List<Map<String, Object>> activitySkuList = activityService.findSpikeActivitySkuList(activity.getId(),null);
					for(Map<String, Object> skuMap:activitySkuList){
						map = activityService.updateSku(Integer.parseInt(skuMap.get("id").toString()),activity.getId());
					}
					activityMap.put("activity_status", 2);
				}
			}else{//限购活动
				if((startTime.before(nowDate))&&(activityStats==0)){//活动已经开始
					Activity activity = activityService.findByActivityId(Integer.parseInt(activityMap.get("id").toString()));
					activity.setActivityStatus(1);
					map = activityService.updateActivity(activity);
					//活动开始把该活动下所有的商品改为上架状态
					List<Map<String, Object>> activitySkuList = activityService.findSpikeActivitySkuList(Integer.parseInt(activityMap.get("id").toString()),null);
					for(Map<String, Object> activitySkuMap:activitySkuList){
						Sku sku = skuService.getById(Integer.parseInt(activitySkuMap.get("id").toString()));
						if(sku.getStatus()==0){
							sku.setStatus(1);
							skuService.updateSku(sku);
						}
					}
					activityMap.put("activity_status", 1);
				}
				Integer reservedInventory= (Integer) activityMap.get("reserved_inventory");
				if((startTime.before(nowDate))&&(reservedInventory<=0)){//活动已经结束
					Activity activity = activityService.findByActivityId(Integer.parseInt(activityMap.get("id").toString()));
					activity.setActivityStatus(2);
					map = activityService.updateActivity(activity);
					map = activityService.updateActivitySkuList(activity.getId());
					//修改库存
					List<Map<String, Object>> activitySkuList = activityService.findSpikeActivitySkuList(activity.getId(),null);
					for(Map<String, Object> skuMap:activitySkuList){
						map = activityService.updateSku(Integer.parseInt(skuMap.get("id").toString()),activity.getId());
					}
					activityMap.put("activity_status", 2);
				}
			}
			activityMap.put("now_time", System.currentTimeMillis());
			if(activityMap.get("display")==null||!"1".equals(activityMap.get("display").toString())){
				activityData.add(activityMap);
			}
		}
		if(activityData==null||activityData.size()==0||activityData.get(0)==null){
			map.put("code", "400");
			map.put("message", "没有任何活动！");
			return map;
		}else{
			map.put("code", "200");
			map.put("message", "OK");
		}
		map.put("data", activityData);
		return map;
	}
	/****
	  * <p>功能描述：通过商品和当前时间查询该商品是否参加活动。</p>	
	  * @return
	  * <p>创建日期:2016年8月3日 上午10:15:27。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */                       
	@RequestMapping(value = "/findActivitySkuId",method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String,Object> findActivitySkuId(@RequestBody ActivitySku activitySku,HttpSession session){
		logger.info(">>>>>>>>>>>>>>>>>>>>查询商品信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<Map<String, Object>> activitySkuData = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> activitySkuList = activityService.findActivitySku(activitySku);
		Date nowDate = new Date();
		for(Map<String, Object> activitySkuMap:activitySkuList){
			int type = (int) activitySkuMap.get("activityType");
			Date startTime = (Date) activitySkuMap.get("startTime");
			Integer activityStats= (Integer) activitySkuMap.get("activityStatus");
			if(type==1){//秒杀
				Date end_date_time = (Date) activitySkuMap.get("endTime");
				if(end_date_time.before(nowDate)||(activityStats==2)){//活动结束
					Activity activity = activityService.findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(2);
					map = activityService.updateActivity(activity);
					map = activityService.updateSku((Integer)activitySkuMap.get("Id"),activity.getId());
					map = activityService.updateActivitySkuList(activity.getId());
					map.put("code", "400");
					map.put("message", "活动已经结束！");
					continue;
				}
				if(startTime.before(nowDate)&&end_date_time.after(nowDate)&&activityStats==0){//活动已经开始
					Activity activity = activityService.findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(1);
					map = activityService.updateActivity(activity);
					//活动开始把该活动下所有的商品改为上架状态
					List<Map<String, Object>> SkuList = activityService.findSpikeActivitySkuList(activitySku.getActivityId(),activitySku.getSkuId());
					for(Map<String, Object> SkuMap:SkuList){
						Sku sku = skuService.getById(Integer.parseInt(SkuMap.get("id").toString()));
						if(sku.getStatus()==0){
							sku.setStatus(1);
							skuService.updateSku(sku);
						}
					}
				}
				activitySkuMap.put("nowTime", System.currentTimeMillis());
				if(!"2".equals(activitySkuMap.get("activityStatus").toString())){
					User user = (User) session.getAttribute(Constants.USER);
					if(user!=null){
						logger.info(user+">>>>>>>>>>>>>>>>>用户已经登录>>>>>>>>>>>>>>>>>>>>>>>>");
						Integer custId = user.getId();
						List<Map<String, Object>> orderSkuList= activityService.findOrderSkuActivity(activitySku.getSkuId(),custId,Integer.parseInt(activitySkuMap.get("activityId").toString()));
						logger.info(orderSkuList+">>>>>>>>>>>>>>>>>>>>>>>>>>>>buyNum>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						if(orderSkuList!=null&&orderSkuList.size()>0&&orderSkuList.get(0)!=null){
							activitySkuMap.put("buyNum", orderSkuList.get(0).get("skuCount"));
						}else{
							activitySkuMap.put("buyNum", 0);
						}
						
					}else{
						activitySkuMap.put("buyNum", 0);	
					}
					activitySkuData.add(activitySkuMap);
				}
			}else{//限购活动
				if(startTime.before(nowDate)&&activityStats==0){//活动已经开始
					Activity activity = activityService.findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(1);
					map = activityService.updateActivity(activity);
					//活动开始把该活动下所有的商品改为上架状态
					List<Map<String, Object>> SkuList = activityService.findSpikeActivitySkuList(activitySku.getActivityId(),activitySku.getSkuId());
					for(Map<String, Object> SkuMap:SkuList){
						Sku sku = skuService.getById(Integer.parseInt(SkuMap.get("id").toString()));
						if(sku.getStatus()==0){
							sku.setStatus(1);
							skuService.updateSku(sku);
						}
					}
					activitySkuMap.put("activityStatus", 1);
				}
				if((startTime.before(nowDate)&&(Integer.parseInt(activitySkuMap.get("reservedInventory").toString())<=0))||activityStats==2){//限购活动已经结束
					Activity activity = activityService.findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(2);
					map = activityService.updateActivity(activity);
					//修改库存
					map = activityService.updateSku((Integer)activitySkuMap.get("Id"),activity.getId());
					map = activityService.updateActivitySkuList(activity.getId());
					map.put("code", "400");
					map.put("message", "活动已经结束！");
					continue;
				}
				activitySkuMap.put("nowTime", System.currentTimeMillis());
				if("1".equals(activitySkuMap.get("activityStatus").toString())){
					User user = (User) session.getAttribute(Constants.USER);
					logger.info(user+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>user>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					if(user!=null){
						logger.info(user+">>>>>>>>>>>>>>>>>用户已经登录>>>>>>>>>>>>>>>>>>>>>>>>");
						Integer custId = user.getId();
						List<Map<String, Object>> orderSkuList= activityService.findOrderSkuActivity(activitySku.getSkuId(),custId,Integer.parseInt(activitySkuMap.get("activityId").toString()));
						logger.info(orderSkuList+">>>>>>>>>>>>>>>>>>>>>>>>>>>>buyNum>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						if(orderSkuList!=null&&orderSkuList.size()>0&&orderSkuList.get(0)!=null){
							activitySkuMap.put("buyNum", orderSkuList.get(0).get("skuCount"));
						}else{
							activitySkuMap.put("buyNum", 0);
						}
						
					}else{
						activitySkuMap.put("buyNum", 0);	
					}
					activitySkuData.add(activitySkuMap);
				}
			}
			
		}
		if(activitySkuData!=null&&activitySkuData.size()>0){
			map.put("code", "200");
			map.put("data", activitySkuData);
		}else{
			map.put("code", "400");
			map.put("message", "活动已经结束或者已经结束！");
		}
		return map;
	}
	/***
	  * <p>功能描述：查找秒杀活动商品信息。</p>	
	  * @param activitySku
	  * @param session
	  * @return
	  * <p>创建日期:2016年10月14日 上午10:25:09。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	@RequestMapping(value = "/findActivitySecKillSku",method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String,Object> findActivitySecKillSku(@RequestBody ActivitySku activitySku,HttpSession session){
		logger.info(">>>>>>>>>>>>>>>>>>>>活动商品信息查询商品信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<Map<String, Object>> activitySkuData = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> activitySkuList = activityService.findActivitySku(activitySku);
		Date nowDate = new Date();
		for(Map<String, Object> activitySkuMap:activitySkuList){
			int type = (int) activitySkuMap.get("activityType");
			Date startTime = (Date) activitySkuMap.get("startTime");
			Integer activityStats= (Integer) activitySkuMap.get("activityStatus");
			if(startTime.after(nowDate)){//活动未开始
				map.put("code", "400");
				map.put("message", "活动尚未开始！");
				continue;
			}
			if(type==1){//秒杀
				Date end_date_time = (Date) activitySkuMap.get("endTime");
				if(end_date_time.before(nowDate)||(activityStats==2)){//活动结束
					Activity activity = activityService.findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(2);
					map = activityService.updateActivity(activity);
					map = activityService.updateSku((Integer)activitySkuMap.get("Id"),activity.getId());
					map = activityService.updateActivitySkuList(activity.getId());
					map.put("code", "400");
					map.put("message", "活动已经结束！");
					continue;
				}
				if(startTime.before(nowDate)&&end_date_time.after(nowDate)&&activityStats==0){//活动已经开始
					Activity activity = activityService.findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(1);
					map = activityService.updateActivity(activity);
					//活动开始把该活动下所有的商品改为上架状态
					List<Map<String, Object>> SkuList = activityService.findSpikeActivitySkuList(activitySku.getActivityId(),activitySku.getSkuId());
					for(Map<String, Object> SkuMap:SkuList){
						Sku sku = skuService.getById(Integer.parseInt(SkuMap.get("id").toString()));
						if(sku.getStatus()==0){
							sku.setStatus(1);
							skuService.updateSku(sku);
						}
					}
				}
			}
			activitySkuMap.put("nowTime", System.currentTimeMillis());
			if("1".equals(activitySkuMap.get("activityStatus").toString())){
				User user = (User) session.getAttribute(Constants.USER);
				if(user==null){
					map.put("code", "401");
					map.put("mesage","请登录");
					return map;
				}
				Integer custId = user.getId();
				List<Map<String, Object>> orderSkuList= activityService.findOrderSkuActivity(activitySku.getSkuId(),custId,Integer.parseInt(activitySkuMap.get("activityId").toString()));
				if(orderSkuList!=null&&orderSkuList.size()>0&&orderSkuList.get(0)!=null){
					activitySkuMap.put("buyNum", orderSkuList.get(0).get("skuCount"));
				}else{
					activitySkuMap.put("buyNum", 0);
				}
				activitySkuData.add(activitySkuMap);
			}
			
		}
		if(activitySkuData!=null&&activitySkuData.size()>0){
			map.put("code", "200");
			map.put("data", activitySkuData);
		}else{
			map.put("code", "400");
			map.put("message", "活动已经结束或者已经结束！");
		}
		return map;
	}
}
