package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.WarningUser;

public interface WarningUserDao{
	
	Integer insert(WarningUser warningUser);
	
	WarningUser getWarningUser(WarningUser warningUser);
	
	Integer updateWarningUserByPrimaryKey(WarningUser warningUser);
	
	List<WarningUser> getWarningUserByGroupName(String name);
	
}