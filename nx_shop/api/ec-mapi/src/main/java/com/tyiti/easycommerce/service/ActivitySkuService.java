package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Sku;

public interface ActivitySkuService {

	List<Sku> getAllSku(Integer id);
	
	int saveActivitySku(List<ActivitySku> activitySkuList);
	
	List<ActivitySku> getActivitySkuById(Integer id);
	
	int updateReservedInventoryById(Map<String, Object> map);
	
	List<Sku> searchByConditions(Map<String, Object> param);
	
	void updateSkuInventory(List<ActivitySku> list);

	 /**
	  * <p>功能描述：修改数据库，把选择的商品置顶。</p>	
	  * @param id
	  * @return
	  * <p>创建日期:2016年10月17日 下午3:01:59。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> updateActivitySkuTop(Integer id);
}
