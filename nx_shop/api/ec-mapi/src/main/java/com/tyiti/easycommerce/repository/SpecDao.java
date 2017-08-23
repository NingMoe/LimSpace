package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.Spec;

public interface SpecDao {
    int deleteByPrimaryKey(Integer id);

	int insert(Spec record);

	int insertSelective(Spec record);

	Spec selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Spec record);

	int updateByPrimaryKey(Spec record);

	List<Spec> getSpecList();

	int selectCountByName(String name);

	 
}