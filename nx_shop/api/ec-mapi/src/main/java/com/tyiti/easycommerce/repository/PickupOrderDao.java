package com.tyiti.easycommerce.repository;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.PickupOrder;

public interface PickupOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PickupOrder record);

    int insertSelective(PickupOrder record);

    PickupOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PickupOrder record);

    int updateByPrimaryKey(PickupOrder record);
    /**
    * @Title: updatePickupOrderStatus
    * @Description:  自提订单退货
    * @return void    返回类型
    * @author Yan Zuoyu
    * @throws
     */
	void updatePickupOrderStatus(@Param("orderId") int orderId,@Param("status") int status);

	void deletePickupOrder(@Param("orderId") int orderId);

	PickupOrder selectByOrderId(@Param("orderId") int orderId);
}