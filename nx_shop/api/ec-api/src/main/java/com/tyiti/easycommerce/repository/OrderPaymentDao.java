package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.OrderPayment;

public interface OrderPaymentDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderPayment record);

    int insertSelective(OrderPayment record);

    OrderPayment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderPayment record);

    int updateByPrimaryKey(OrderPayment record);
    
    /*修改记录中订单支付状态*/
    Integer updateOrderPaymentStatus(@Param("orderId") Integer orderId ,@Param("status") Integer status );
    /*根据订单查询支付信息*/
	List<OrderPayment> getByOrderId(Integer orderId);
}