package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.WarningItem;
import com.tyiti.easycommerce.entity.WarningUser;

public interface WarningItemDao{
	
	Integer insert(WarningItem warningItem);
	
	WarningItem getWarningItemByName(String name);
	
	List<WarningUser> getWarningUserByName(@Param("itemName") String itemName,@Param("groupName") String groupName);

}