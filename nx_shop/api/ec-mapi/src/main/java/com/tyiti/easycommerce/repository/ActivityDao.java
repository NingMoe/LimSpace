package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Activity;

public interface ActivityDao {

	int insertActivity(Activity activity);
	
	Activity getActivityById(Integer id);
	
	List<Activity> getAll(Integer avtivityType);
	
	int getCount();
	
	int updateActivity(Activity activity);
	
	int deleteActivityById(Integer id);
	
	int endActivityById(Integer id);
	
	List<Activity> searchByConditions(Map<String, Object> param);
	
	int updateActivityStatus(Integer id);
	
}
