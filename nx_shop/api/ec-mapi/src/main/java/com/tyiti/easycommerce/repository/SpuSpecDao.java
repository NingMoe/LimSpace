package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.SpuSpec;

public interface SpuSpecDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SpuSpec record);

    int insertSelective(SpuSpec record);

    SpuSpec selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpuSpec record);

    int updateByPrimaryKey(SpuSpec record);

	List<SpuSpec> getListBySpuId(Integer spuId);

	SpuSpec getBySpecId(@Param("spuId") Integer spuId,@Param("specId") Integer specId);
}