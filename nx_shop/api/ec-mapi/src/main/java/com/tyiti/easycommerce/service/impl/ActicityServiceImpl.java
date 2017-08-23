package com.tyiti.easycommerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.repository.ActivityDao;
import com.tyiti.easycommerce.service.ActivityService;

@Service("activityService")
public class ActicityServiceImpl implements ActivityService {

	Logger logger = Logger.getLogger(JDServiceImpl.class);
	
	@Autowired
	private ActivityDao activityDao;
	
	@Override
	public int insertActivity(Activity activity) {
		activity.setActivityStatus(0);  //活动状态默认为0：未开始
		/*String coupon = activity.getCoupon();
		if (coupon!=null && "0".equals(coupon)) {
			activity.setCoupon(null);
		}*/
		return activityDao.insertActivity(activity);
	}
	
	@Override
	public List<Activity> getAll(Integer avtivityType) {
		return activityDao.getAll(avtivityType);
	}

	@Override
	public Activity getActivityById(Integer id) {
		return activityDao.getActivityById(id);
	}

	@Override
	public int updateActivity(Activity activity) {
		return activityDao.updateActivity(activity);
	}

	@Override
	public int deleteActivityById(Integer id) {
		return activityDao.deleteActivityById(id);
	}

	@Override
	public int endActivityById(Integer id) {
		return activityDao.endActivityById(id);
	}

	@Override
	public List<Activity> searchByConditions(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer activityType = null;
		String activityName = null;
		Integer activityStatus = null;
		String status = null;
		activityType = Integer.valueOf((String) param.get("activityType"));
		activityName = (String) param.get("activityName");
		
		status = (String) param.get("activityStatus");
		if (status != null && status.length()>0) {
			activityStatus = Integer.valueOf(status);
			if (activityStatus==3) {
				activityStatus = null;
			}
		}
		map.put("activityType", activityType);
		map.put("activityName", activityName);
		map.put("activityStatus", activityStatus);
		return activityDao.searchByConditions(map);
	}

	@Override
	public int updateActivityStatus(Integer id) {
		return activityDao.updateActivityStatus(id);
	}


}
