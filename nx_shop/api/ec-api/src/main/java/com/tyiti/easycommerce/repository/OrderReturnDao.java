package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.OrderReturn;

public interface OrderReturnDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderReturn record);

    int insertSelective(OrderReturn record);

    OrderReturn selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderReturn record);

    int updateByPrimaryKey(OrderReturn record);

	List<Map<String, Object>> selectList(Map<String, Object> params);

	long selectCount(Map<String, Object> params);
}