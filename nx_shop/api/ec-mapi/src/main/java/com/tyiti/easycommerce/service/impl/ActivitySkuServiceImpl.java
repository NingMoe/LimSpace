package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.repository.ActivityDao;
import com.tyiti.easycommerce.repository.ActivitySkuDao;
import com.tyiti.easycommerce.repository.CategoryDao;
import com.tyiti.easycommerce.service.ActivitySkuService;

@Service("activitySkuService")
public class ActivitySkuServiceImpl implements ActivitySkuService {

	@Autowired
	private ActivitySkuDao activitySkuDao;
	
	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	Logger logger = Logger.getLogger(ActivitySkuServiceImpl.class);
	
	@Override
	public List<Sku> getAllSku(Integer id) {
		Activity activity = activityDao.getActivityById(id);
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if (endTime == null) {
			endTime = startTime;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return activitySkuDao.getAllSku(param);
	}

	@Override
	public int saveActivitySku(List<ActivitySku> activitySkuList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ActivitySku> arrList = new ArrayList<ActivitySku>();
		ActivitySku activitySku = activitySkuList.get(0);
		Integer activityId = activitySku.getActivityId();
		//根据活动id查询数据库是否已经有与此活动相关的sku信息
		List<ActivitySku> list = activitySkuDao.getActivitySkuById(activityId);
		//list不为空表示是更新操作，需要先修改sku表中的库存，再删除t_activity_sku表中与活动id相关的信息
		if (list.size()>0 && list!=null) { 
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setInventory(-list.get(i).getReservedInventory());
				arrList.add(list.get(i));
			}
			logger.info("》》》》修改活动信息的时候，先修改t_sku表中的库存数量，再删除t_activity_sku表中的数据。《《《《");
			activitySkuDao.updateSkuInventory(arrList);
			activitySkuDao.deleteSkuByActivityId(activityId);
		}
		//保存sku的时候，再次判断新创建的活动中的sku是否与正在参加活动的sku有冲突
		Activity activity = activityDao.getActivityById(activityId);
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if (endTime == null) {
			endTime = startTime;
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		List<Map<String, Object>> skuIds = activitySkuDao.getActivitySku(map);
		if (skuIds!=null && skuIds.size()>0) {
			for (Map<String, Object> skuIdMap:skuIds) {
				for (ActivitySku activitySkus:activitySkuList) {
					if (activitySkus.getSkuId().equals(skuIdMap.get("skuId"))) {
						return -1;
					}
				}
			}
		}
		return activitySkuDao.saveActivitySku(activitySkuList);
	}

	@Override
	public List<ActivitySku> getActivitySkuById(Integer id) {
		return activitySkuDao.getActivitySkuById(id);
	}

	@Override
	public int updateReservedInventoryById(Map<String, Object> param) {
		return activitySkuDao.updateReservedInventoryById(param);
	}

	@Override
	public List<Sku> searchByConditions(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer id = Integer.valueOf((String) param.get("id")); //活动id
		logger.info("查询条件中活动id为："+id);
		Integer skuId = null;
		Integer categoryId = null;
		Integer status = null;
		String[] categoryArr = null;
		//skuId
		String skuIdStr = (String) param.get("skuId");
		logger.info("查询条件中skuid为："+skuIdStr);
		if (skuIdStr!=null && skuIdStr.length()>0) {
			skuId = Integer.valueOf(skuIdStr);
		}
		map.put("skuId", skuId);
		//分类id
		String categoryIdStr = (String) param.get("categoryId");
		logger.info("查询条件中sku分类id为："+categoryIdStr);
		if (categoryIdStr!=null && categoryIdStr.length()>0) {
			categoryId = Integer.valueOf(categoryIdStr);  
		}
		if (categoryId!=null) {
			String str = categoryDao.getIdsByCategoryId(categoryId);
			categoryArr = str.split(",");
		}
		map.put("categoryIds", categoryArr);
		
		//sku状态
		String statusStr = (String) param.get("status");  
		logger.info("查询条件中sku状态为："+statusStr);
		if (statusStr!=null && statusStr.length()>0) {
			status = Integer.valueOf(statusStr);
			if (status==2) {
				status = null;
			}
		}
		map.put("status", status);
		
		Activity activity = activityDao.getActivityById(id);
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if (endTime == null) {
			endTime = startTime;
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		
		return activitySkuDao.searchByConditions(map);
	}

	@Override
	public void updateSkuInventory(List<ActivitySku> list) {
		activitySkuDao.updateSkuInventory(list);
	}

	 /**
	  * <p>功能描述:修改数据库，把选择的商品置顶。</p>	
	  * @param id
	  * @return
	  * <p>创建日期2016年10月17日 下午3:03:10。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Map<String, Object> updateActivitySkuTop(Integer id) {
		Map<String, Object> map= new HashMap<String, Object>();
		ActivitySku activitySku = activitySkuDao.selectByPrimaryKey(id);
		if(activitySku!=null){
			List<ActivitySku> activitySkuList = activitySkuDao.getActivitySkuById(activitySku.getActivityId());
			if(activitySkuList!=null&&activitySkuList.size()>1){//活动商品两个以上才需要排序
				int topRank = activitySkuList.get(0).getTopRank();//获取当前商品最大的排序信息
				int topNumber = activitySkuList.size()-1;//记录当前
				int number=topNumber-activitySku.getTopRank();// 当前修改的下标 
				if(topRank==activitySkuList.size()-1){//如果当前商品个数等于最大个数则需要一个一个的递减否则直接直接加一
					activitySku.setTopRank(topRank);
					activitySkuDao.updateByPrimaryKeySelective(activitySku);
					for(int i=0;i<number;i++){
						ActivitySku activitySkuTop =activitySkuList.get(i); 
						activitySkuTop.setTopRank(activitySkuTop.getTopRank()-1);
						activitySkuDao.updateByPrimaryKeySelective(activitySkuTop);
					}

				}else{
					activitySku.setTopRank(topRank+1);
					activitySkuDao.updateByPrimaryKeySelective(activitySku);
				}
			}else{
				map.put("code", "400");
				map.put("message", "商品不需要排序");
			}
			
		}else{
			map.put("code", "400");
			map.put("message", "没有找到需要置顶商品");
		}
		return map;
	}

}
