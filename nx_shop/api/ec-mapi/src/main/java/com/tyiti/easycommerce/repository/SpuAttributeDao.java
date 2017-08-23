package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.SpuAttribute;

public interface SpuAttributeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SpuAttribute record);

    int insertSelective(SpuAttribute record);

    SpuAttribute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpuAttribute record);

    int updateByPrimaryKey(SpuAttribute record);

	List<SpuAttribute> getListBySpuId(Integer spuId);

	SpuAttribute getByAttributeId(@Param("spuId") int spuId,@Param("attributeId") Integer attributeId);
}