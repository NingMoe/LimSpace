package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Sku;

public interface KooSkuDao {
	int add(Sku sku);

	Sku getById(@Param("id") Integer id);

	List<Sku> getListBySpuIdAndNotId(@Param("id") Integer id, @Param("spuId") Integer spuId);

	List<Sku> getByCriteria(Sku sku);
	
	/**
	 * 根据skuId获取CategoryId
	 * @param id
	 * @return
	 */
	Integer getCategoryIdById(@Param("id") Integer id);
	/**
	 * 根据skuId获取SpecId 规格id，品牌
	 * @param id
	 * @return
	 */
	Integer getSpecIdById(@Param("id") Integer id);
}
