package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Attribute;

public interface AttributeDao {
    int add(Attribute attribute);
    
    Attribute getById(@Param("id")Integer id);

    int update(Attribute attribute);

    List<Attribute> getByCriteria(Attribute attribute);

	Attribute getByName(String name);
}
