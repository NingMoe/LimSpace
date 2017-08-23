package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface FavoriteDao {
	
	List<Integer> getSkuIdsByUserId(Integer userId);
	
	Integer add(@Param("skuId") Integer skuId,@Param("userId") Integer userId);

	Integer updateStatus(@Param("skuId") Integer skuId,@Param("userId") Integer userId);

}