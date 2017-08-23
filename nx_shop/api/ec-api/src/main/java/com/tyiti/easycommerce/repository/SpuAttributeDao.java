package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.SpuAttribute;

public interface SpuAttributeDao {
    int addList(@Param("list") List<SpuAttribute> list);

    List<SpuAttribute> getListBySpuId(@Param("spuId") Integer spuId);
}
