package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.PickupPointUser;

public interface PickupPointUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PickupPointUser record);

    int insertSelective(PickupPointUser record);

    PickupPointUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PickupPointUser record);

    int updateByPrimaryKey(PickupPointUser record);
    
    PickupPointUser selectForObject(PickupPointUser entity);
    
}