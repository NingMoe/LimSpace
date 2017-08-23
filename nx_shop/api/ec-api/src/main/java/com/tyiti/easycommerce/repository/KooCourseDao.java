package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.KooCourse;

public interface KooCourseDao {
    int deleteByPrimaryKey(Integer id);

    int insert(KooCourse record);

    int insertSelective(KooCourse record);

    KooCourse selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KooCourse record);

    int updateByPrimaryKey(KooCourse record);
    
    List<KooCourse> selectByProductId(String id);
}