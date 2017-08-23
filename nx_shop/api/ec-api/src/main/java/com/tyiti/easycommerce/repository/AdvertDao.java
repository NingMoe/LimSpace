package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Advert;

public interface AdvertDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKey(Advert record);

	Advert getAdvertByCode(@Param("code") String code);
	
	Advert getAdvertByGroup(@Param("group") String group);
	
	List<Advert> getAdvertByGroupOrCode(@Param("group") String group,@Param("code") String code);
	
}