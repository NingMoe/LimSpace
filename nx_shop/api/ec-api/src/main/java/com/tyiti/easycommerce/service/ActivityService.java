 /**
  * 文件名[fileName]：ActivityService.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年7月19日 下午5:58:36
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.OrderSkuActivity;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.7。
  *<p>创建日期：2016年7月19日 下午5:58:36。</p>
  */

public interface ActivityService {

	 /**
	  * <p>功能描述：根据活动ID商品ID 查询信息。</p>	
	  * @param activityId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月19日 下午5:59:36。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findSpikeActivitySkuList(Integer activityId, Integer skuId);

	 /**
	  * <p>功能描述：。</p>	
	  * @param activityType
	 * @param skuId 
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月20日 上午11:49:11。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findActivityList(Integer activityType);

	 /**
	  * <p>功能描述：通过skuId查询该商品参加了那些活动。</p>	
	  * @param id
	  * @return
	  * <p>创建日期:2016年7月20日 下午2:47:05。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findByActivitSkuList(Integer id);

	 /**
	  * <p>功能描述：。</p>	
	  * @param activityId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月20日 下午3:11:28。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Activity findByActivityId(Integer activityId);

	 /**
	  * <p>功能描述：。</p>	
	  * @param activitySku
	  * @return
	  * <p>创建日期:2016年8月3日 上午10:20:17。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findActivitySku(ActivitySku activitySku);

	 /**
	  * <p>功能描述：批量保存订单活动信息</p>	
	  * @param skuActivityList
	  * <p>创建日期:2016年8月4日 下午4:01:12。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	void saveOrderSkuActivity(List<OrderSkuActivity> skuActivityList);
	 /**
	  * <p>功能描述：。</p>	
	  * @param orderId
	  * @param skuId
	  * @return
	  * <p>创建日期:2016年8月4日 下午7:54:43。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	OrderSkuActivity findOrderSkuActivity(Integer orderId, Integer skuId);

	 /**
	  * <p>功能描述：更新信息。</p>	
	  * @param activitySku
	  * <p>创建日期:2016年8月5日 上午10:32:10。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	void updateActivitySku(ActivitySku activitySku);

	 /**
	  * <p>功能描述：。</p>	
	  * @param skuId
	  * @param custId
	  * @param activityId
	  * @return
	  * <p>创建日期:2016年8月5日 下午4:03:51。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findOrderSkuActivity(Integer skuId, Integer custId, Integer activityId);

	 /**
	  * <p>功能描述：根据orderId,skuId根据信息。</p>	
	  * @param id
	  * @param skuId
	  * @return
	  * <p>创建日期:2016年8月10日 上午10:38:26。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<OrderSkuActivity> findOrderSkuActivityList(Integer orderId, Integer skuId);
	/**
	  * <p>功能描述：根据ID修改活动状态。</p>	
	  * @param activity
	  * <p>创建日期:2016年9月8日 下午3:02:25。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 * @return 
	  */
	Map<String, Object> updateActivity(Activity activity);

	 /**
	  * <p>功能描述：活动已经结束，修改所有活动下了订单但是没有支付的订单修改为一失效。</p>	
	  * @param id
	  * @return
	  * <p>创建日期:2016年9月8日 下午5:50:36。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> updateActivitySkuList(Integer id);


	 /**
	  * <p>功能描述：修改活动信息并返还库存。</p>	
	  * @param id
	  * @return
	  * <p>创建日期:2016年9月14日 下午4:58:47。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 Map<String,Object> isSkuActivity(Integer id);

	 /**
	  * <p>功能描述：。</p>	
	  * @param integer
	  * @param id
	  * @return
	  * <p>创建日期:2016年9月14日 下午5:58:00。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> updateSku(Integer integer, Integer id);

	/**
	 * 计算商品总价
	 * @param activityType 活动类型
	 * @param skuPrice 单价（活动价）
	 * @param skuCount 数量
	 * @return
	 */
	public BigDecimal compareSkuTotalPrice(Integer activityType,BigDecimal skuPrice,Integer skuCount);
}
