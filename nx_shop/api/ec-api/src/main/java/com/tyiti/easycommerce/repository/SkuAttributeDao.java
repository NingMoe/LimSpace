package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.SkuAttribute;

public interface SkuAttributeDao {
	int addList(@Param("list") List<SkuAttribute> list);

	List<SkuAttribute> getListBySkuId(@Param("skuId") Integer skuId);

	List<SkuAttribute> getListBySpuIdAndNotSkuAttrId(@Param("skuId") Integer skuId, @Param("spuId") Integer spuId);
}
