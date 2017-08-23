package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.OrderSkuActivity;

public interface ActivitySkuDao{
    int deleteByPrimaryKey(Integer id);

    int insert(ActivitySku record);

    int insertSelective(ActivitySku record);

    ActivitySku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivitySku record);

    int updateByPrimaryKey(ActivitySku record);

	 /**
	  * <p>功能描述：。</p>	
	  * @param activityId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月19日 下午6:01:37。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findSpikeActivitySkuList(@Param("activityId")Integer activityId,@Param("skuId")Integer skuId);

	 /**
	  * <p>功能描述：通过sku查询当前信息是否参加活动。</p>	
	  * @param activitySku
	  * @return
	  * <p>创建日期:2016年8月3日 上午10:21:12。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findActivitySku(ActivitySku activitySku);

	/**
	  * <p>功能描述：根据OrderId,SkuId查询所有基本信息。</p>	
	  * @param orderId
	  * @param skuId
	  * @return
	  * <p>创建日期:2016年8月10日 上午10:42:12。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<OrderSkuActivity> findOrderSkuActivityList(@Param("orderId")Integer orderId, @Param("skuId")Integer skuId);

	 /**
	  * <p>功能描述：通过商品ID查询该商品参加的所有活动。</p>	
	  * @param skuId
	  * @return
	  * <p>创建日期:2016年9月13日 上午9:51:09。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Map<String, Object>> findByActivitSkuList(@Param("skuId")Integer skuId);

	ActivitySku getActiveBySku(Integer skuId);

	 /**
	  * <p>功能描述：根据活动ID 返回活动商品。</p>	
	  * @param acSku
	  * @return
	  * <p>创建日期:2016年9月14日 下午5:11:06。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	ActivitySku findActivitSku(ActivitySku acSku);
	 /**
	  * <p>功能描述：。</p>	
	  * @param id
	  * @return
	  * <p>创建日期:2016年10月25日 下午4:27:13。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> findActivitSkuStatus(Integer skuId);
}