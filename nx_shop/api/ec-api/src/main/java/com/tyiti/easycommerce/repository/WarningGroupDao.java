package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.Config;
import com.tyiti.easycommerce.entity.WarningGroup;

public interface WarningGroupDao{
	
	Integer insert(WarningGroup warningGroup);
	
	WarningGroup getWarningGroup(WarningGroup warningGroup);
	
	
	void updateConfigByPrimaryKey(Config config);
	
	Config getConfig(Config config);
	
	Integer insertConfig(Config config);
	
}