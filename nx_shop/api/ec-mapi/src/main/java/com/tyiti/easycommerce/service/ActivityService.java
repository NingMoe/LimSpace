package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Activity;

public interface ActivityService {

	int insertActivity(Activity activity);
	
	List<Activity> getAll(Integer avtivityType);
	
	Activity getActivityById(Integer id);
	
	int updateActivity(Activity activity);
	
	int deleteActivityById(Integer id);
	
	int endActivityById(Integer id);
	
	List<Activity> searchByConditions(Map<String, Object> param);
	
	int updateActivityStatus(Integer id);
	
}
