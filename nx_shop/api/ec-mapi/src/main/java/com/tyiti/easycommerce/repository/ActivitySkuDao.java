package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Sku;

public interface ActivitySkuDao {

	List<Sku> getAllSku(Map<String, Object> param);
	
	int saveActivitySku(List<ActivitySku> list);
	
	int updateReservedInventoryById(Map<String, Object> param);
	
	List<Sku> searchByConditions(Map<String, Object> param);
	
	void updateSkuInventory(List<ActivitySku> list);
	
	int deleteSkuByActivityId(Integer id);
	
	List<Map<String, Object>> getActivitySku(Map<String, Object> param);

	 /**
	  * <p>功能描述：。</p>	
	 * @param id 
	  * @return
	  * <p>创建日期:2016年10月17日 下午3:05:46。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	ActivitySku selectByPrimaryKey(Integer id);

	 /**
	  * <p>功能描述：根据活动ID查询所有参加活动的商品。</p>	
	  * @param activityId
	  * @return
	  * <p>创建日期:2016年10月17日 下午3:39:32。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<ActivitySku> getActivitySkuById(Integer activityId);
	 /**
	  * <p>功能描述：更新排序信息。</p>	
	  * @param activitySku
	  * <p>创建日期:2016年10月17日 下午3:49:34。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
    int updateByPrimaryKeySelective(ActivitySku record);
}
