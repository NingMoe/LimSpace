package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.WarningUserGroup;

public interface WarningUserGroupDao{
	
	Integer insert(WarningUserGroup warningUserGroup);
	
	List<WarningUserGroup> getWarningUserGroup(WarningUserGroup warningUserGroup);
	
	Integer updateWarningUserGroupByPrimaryKey(WarningUserGroup warningUserGroup);
	
}