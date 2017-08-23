package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.PickupPoint;

public interface PickupPointDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PickupPoint record);

    int insertSelective(PickupPoint record);

    PickupPoint selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PickupPoint record);

    int updateByPrimaryKey(PickupPoint record);

	List<PickupPoint> selectPickupPointList();

}