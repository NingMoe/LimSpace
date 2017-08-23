package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Spu;

public interface SpuDao {
    int add(Spu spu);
    
    Spu getById(@Param("id")Integer id);

	List<Spu> getByCriteria(Spu spu);
}
