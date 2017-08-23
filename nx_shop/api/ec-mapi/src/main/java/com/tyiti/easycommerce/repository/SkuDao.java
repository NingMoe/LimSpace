package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Sku;

public interface SkuDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Sku sku);

    int insertSelective(Sku sku);

    Sku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sku sku);

    int updateByPrimaryKeyWithBLOBs(Sku sku);

    int updateByPrimaryKey(Sku sku);

	List<Sku> getSkuBySpuId(Integer spuId);

	void updateDeleteBySpuId(int spuId);

	void deleteNotInByIds(@Param("ids") int[] ids,@Param("spuId") int spuId);

	void deleteAllBySpu(@Param("spuId") int spuId);

	List<Map<String, Object>> selectSkuList();
	
	void setWarningInventoryByIds(@Param("ids") Integer[] ids,@Param("inventory") int inventory);
	
	List<Sku> getWarningSku();
	
}