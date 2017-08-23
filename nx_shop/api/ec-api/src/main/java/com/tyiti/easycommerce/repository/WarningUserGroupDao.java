package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.WarningUserGroup;

public interface WarningUserGroupDao{
	
	Integer insert(WarningUserGroup warningUserGroup);
	
	WarningUserGroup getWarningUserGroup(WarningUserGroup warningUserGroup);
	
	Integer updateWarningUserGroupByPrimaryKey(WarningUserGroup warningUserGroup);
	
}