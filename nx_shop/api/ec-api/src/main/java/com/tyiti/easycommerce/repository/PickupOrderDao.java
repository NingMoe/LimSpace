package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.PickupOrder;

public interface PickupOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PickupOrder record);

    int insertSelective(PickupOrder record);

    PickupOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PickupOrder record);

    int updateByPrimaryKey(PickupOrder record);

	Integer selectCodeNum(String code);

	PickupOrder selectByOrderId(Integer orderId);
	
	List<PickupOrder> selectByPickupPointId(Integer pickupPointId);
	
	List<PickupOrder> findByPickupOrderStatus(PickupOrder pickupOrder);
	
	
	//List<PickupOrder> getListPickupOrder(Map<String, Object> param);
	// 结果集
	List<Map<String, Object>> getListPickupOrder(Map<String, Object> param);
	// 查询总记录数
	int selectAllPickupOrder(Map<String, Object> param);
	
	
	PickupOrder selectByCode(String code);

	PickupOrder getListPickupOrderByCustId(Integer custId);
	
}