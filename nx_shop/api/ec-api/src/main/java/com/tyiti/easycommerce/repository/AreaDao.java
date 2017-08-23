package com.tyiti.easycommerce.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Area;


public interface AreaDao
{
	List<Area> getAreasByParentIdAndLevel(@Param("parentId")Integer parentId,@Param("level")Integer level);
}