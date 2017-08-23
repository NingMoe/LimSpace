 /**
  * 文件名[fileName]：ActivityServiceImpl.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年7月19日 下午5:59:05
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderSkuActivity;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.repository.ActivityDao;
import com.tyiti.easycommerce.repository.ActivitySkuDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderSkuActivityDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.SkuService;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.7。
  *<p>创建日期：2016年7月19日 下午5:59:05。</p>
  */
@Service
public class ActivityServiceImpl implements ActivityService {
	@Autowired
	ActivitySkuDao activitySkuDao; 
	@Autowired
	ActivityDao activityDao; 
	@Autowired
	OrderSkuActivityDao orderSkuActivityDao;
	@Autowired
	OrderSkuDao orderSkuDao;
	@Autowired
	OrderDao orderDao;
	@Autowired
	SkuService skuService;
	
	 /**
	  * <p>功能描述:根据ID查询秒杀活动的商品。</p>	
	  * @param activityId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年7月19日 下午5:59:53。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<Map<String, Object>> findSpikeActivitySkuList(Integer activityId, Integer skuId) {
		return activitySkuDao.findSpikeActivitySkuList(activityId,skuId);
	}
	 /**
	  * <p>功能描述:。</p>	
	  * @param activityType
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年7月20日 上午11:46:50。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<Map<String, Object>> findActivityList(Integer activityType) {
		return activityDao.findActivityList(activityType);
	}
	 /**
	  * <p>功能描述:通过SkuId 查询该产品是否是活动产品。</p>	
	  * @param id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年7月20日 下午3:10:41。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public 	List<Map<String, Object>> findByActivitSkuList(Integer skuId) {
		return activitySkuDao.findByActivitSkuList(skuId);
	}
	 /**
	  * <p>功能描述:根据ID查询活动。</p>	
	  * @param activityId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年7月20日 下午3:11:37。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Activity findByActivityId(Integer activityId) {
		return activityDao.selectByPrimaryKey(activityId);
	}
	 /**
	  * <p>功能描述:根据活动ID查询该商品是否参加活动信息。</p>	
	  * @param skuId
	  * @return
	  * <p>创建日期2016年8月3日 上午10:18:53。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 /**
	  * <p>功能描述:。</p>	
	  * @param skuId
	  * @return
	  * <p>创建日期2016年8月3日 上午10:20:47。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<Map<String, Object>> findActivitySku(ActivitySku activitySku) {
		return activitySkuDao.findActivitySku(activitySku);
	}
	 /**
	  * <p>功能描述:。</p>	
	  * @param skuActivityList
	  * <p>创建日期2016年8月4日 下午4:01:32。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public void saveOrderSkuActivity(List<OrderSkuActivity> skuActivityList) {
		if(skuActivityList.size()>0){
			orderSkuActivityDao.insertOrderSkuActivityList(skuActivityList);
		}
		
	}
	 /**
	  * <p>功能描述:。</p>	
	  * @param orderId
	  * @param skuId
	  * @return
	  * <p>创建日期2016年8月4日 下午7:55:00。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public OrderSkuActivity findOrderSkuActivity(Integer orderId, Integer skuId) {
		return orderSkuActivityDao.findOrderSkuActivity(orderId,skuId);
	}
	 /**
	  * <p>功能描述:更新信息。</p>	
	  * @param activitySku
	  * <p>创建日期2016年8月5日 上午10:33:29。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public void updateActivitySku(ActivitySku activitySku) {
		activitySkuDao.updateByPrimaryKeySelective(activitySku);
	}
	 /**
	  * <p>功能描述:。</p>	
	  * @param skuId
	  * @param custId
	  * @param activityId
	  * @return
	  * <p>创建日期2016年8月5日 下午4:08:58。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<Map<String, Object>> findOrderSkuActivity(Integer skuId, Integer custId, Integer activityId) {
		
		return orderSkuDao.findOrderSkuActivity(skuId,custId,activityId);
	}
	 /**
	  * <p>功能描述:根据OrderId,SkuId查询商品是否。</p>	
	  * @param orderId
	  * @param skuId
	  * @return
	  * <p>创建日期2016年8月10日 上午10:40:31。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<OrderSkuActivity> findOrderSkuActivityList(Integer orderId, Integer skuId) {
		
		return orderSkuActivityDao.findOrderSkuActivityList(orderId,skuId);
	}
	/**
	  * <p>功能描述:根据ID修改活动状态。</p>	
	  * @param activity
	  * <p>创建日期2016年9月8日 下午3:06:05。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Map<String, Object> updateActivity(Activity activity) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("code", 200);
		map.put("message", "OK");
 
		int num = activityDao.updateByPrimaryKeySelective(activity);
		if(num<0){
			map.put("code", 500);
			map.put("message", "更改活动信息状态失败！");
		} 
		return map;
	}
	 /**
	  * <p>功能描述:功能描述：活动已经结束，修改所有活动下了订单但是没有支付的订单修改为一失效。。</p>	
	  * @param id  活动信息ID
	  * @return
	  * <p>创建日期2016年9月8日 下午6:03:59。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Map<String, Object> updateActivitySkuList(Integer id) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("code", 200);
		map.put("message", "OK");
		try{
			List<Order> orderList = orderDao.findOrderActivityList(id);
			for(Order order:orderList){
				order.setStatus(7);//订单失效
				orderDao.updateOrder(order);
			}	
		}catch(Exception e){
			e.printStackTrace();
			map.put("code", 500);
			map.put("message", "更新活动订单失效失败");
		}
		
		return map;
	}
	 /**
	  * <p>功能描述:。</p>	
	  * @param id
	  * @return
	  * <p>创建日期2016年9月14日 下午5:00:06。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public  Map<String,Object> isSkuActivity(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		ActivitySku  activitySku= new ActivitySku();
		activitySku.setSkuId(id);
		List<Map<String, Object>> activitySkuList = findActivitySku(activitySku);
		if(activitySkuList==null||activitySkuList.size()==0){
			map.put("code", "400");
			map.put("message", "活动尚未开始,或者是已经结束");
			return map;
		} 
		Date nowDate = new Date();
		Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(nowDate); 
	    calendar.set(Calendar.HOUR,0); 
	    calendar.set(Calendar.MINUTE,0); 
	    calendar.set(Calendar.SECOND,0); 
	    calendar.set(Calendar.MILLISECOND,0); 
		for(Map<String, Object> activitySkuMap:activitySkuList){
			Integer type =null;
			if(activitySkuMap.get("activityType")!=null){
				type = Integer.parseInt(activitySkuMap.get("activityType").toString());
			}
			Integer activityStats=null;
			if(activitySkuMap.get("activityStatus")!=null){
				 activityStats= Integer.parseInt(activitySkuMap.get("activityStatus").toString());
			}
			Date startTime = (Date) activitySkuMap.get("startTime");
			if(startTime.after(nowDate)){//活动未开始
				map.put("code", "400");
				map.put("message", "活动尚未开始！");
				continue;
			}
			if(type!=null&&type==1){//秒杀
				Date end_date_time = (Date) activitySkuMap.get("endTime");
				if(end_date_time.before(nowDate)||(activityStats==2)){//活动结束
					Activity activity = findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(2);
					if(end_date_time.before(calendar.getTime())&&activityStats==2){
						activity.setDisplay(1); 
				    }
					map = updateActivity(activity);
					//map = updateSku(id,activity.getId());
					map = updateActivitySkuList(activity.getId());
					map.put("code", "400");
					map.put("message", "活动已经结束！");
					continue;
				}
				if(startTime.before(nowDate)&&end_date_time.after(nowDate)&&activityStats==0){//活动已经开始
					Activity activity =findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(1);
					map = updateActivity(activity);
					activitySkuMap.put("activityStatus", 1);
				}
			}else if(type!=null&&type==2){//限购活动
				if(startTime.before(nowDate)&&activityStats==0){//活动已经开始
					Activity activity = findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(1);
					map = updateActivity(activity);
					activitySkuMap.put("activityStatus", 1);
				}
				if((startTime.before(nowDate)&&(Integer.parseInt(activitySkuMap.get("reservedInventory").toString())<=0))||activityStats==2){//限购活动已经结束
					Activity activity = findByActivityId(Integer.parseInt(activitySkuMap.get("activityId").toString()));
					activity.setActivityStatus(2);
					map = updateActivity(activity);
					//map = updateSku(id,activity.getId());
					map = updateActivitySkuList(activity.getId());
					map.put("code", "400");
					map.put("message", "活动已经结束！");
					continue;
				}
			}
			activitySkuMap.put("nowTime", System.currentTimeMillis());
			
		}
		return map;
	}
	/**
	  * <p>功能描述:根据活动ID，活动信息返还库存。</p>	
	  * @param activity
	  * @return
	  * <p>创建日期2016年9月14日 下午5:09:36。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	public  Map<String, Object> updateSku(Integer skuId, Integer activityId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(skuId!=null&&activityId!=null){
			ActivitySku acSku = new ActivitySku();
			acSku.setActivityId(activityId);
			acSku.setSkuId(skuId);
			ActivitySku activitySku = activitySkuDao.findActivitSku(acSku);
			if(activitySku!=null){
				Sku sku = skuService.getById(activitySku.getSkuId());
				if(sku!=null){
					System.out.println(sku.getInventory()+activitySku.getReservedInventory()+"+++++++库存数+++++++");
					if(activitySku.getReservedInventory()>0){
						sku.setInventory(sku.getInventory()+activitySku.getReservedInventory());
					}
					skuService.updateSku(sku);
					activitySku.setReservedInventory(0);
					activitySkuDao.updateByPrimaryKeySelective(activitySku);
				}else{
					map.put("code", "500");
					map.put("message", "没有活动商品！");
					return map;
				}
				
			}else{
				map.put("code", "500");
				map.put("message", "没有找到活动商品！");
				return map;
			}
		}else{
			map.put("code", "500");
			map.put("message", "没有找到活动商品！");
			return map;
		}
		map.put("code", "200");
		map.put("message", "ok");
		return map;
	}
	/**
	 * 计算商品总价
	 * @param activityType 活动类型
	 * @param skuPrice 单价（活动价）
	 * @param skuCount 数量
	 * @return
	 */
	@Override
	public BigDecimal compareSkuTotalPrice(Integer activityType,BigDecimal skuPrice,Integer skuCount){
		if(activityType!=null && activityType==3){
			if(skuCount%2==0){
				return new BigDecimal(skuCount).multiply(skuPrice).multiply(new BigDecimal(0.75));
			}else{
				return new BigDecimal(skuCount).multiply(skuPrice).multiply(new BigDecimal(0.75)).add(skuPrice.multiply(new BigDecimal(0.25)));
			}
		}else{
			return skuPrice.multiply(new BigDecimal(skuCount));
		}
	}
}
