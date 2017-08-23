package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.Attribute;

public interface AttributeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Attribute record);

    int insertSelective(Attribute record);

    Attribute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Attribute record);

    int updateByPrimaryKey(Attribute record);

	List<Attribute> getAttributeList();

	int selectCountByName(String name);
}
