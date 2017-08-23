package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.OrderSku;

public interface OrderSkuDao {
    int addOrderSku(OrderSku orderSku);
    
    OrderSku getById(@Param("id")Integer id);
    
    List<OrderSku> getByOrderId(@Param("orderId")Integer orderId);
    
    List<OrderSku> getByCriteria(OrderSku orderSku);
    
    /**
     * 统计指定条件的订单中,所有商品总数
     * @authro rainyhao
     * @since 2016-3-31 下午5:41:34
     * @param entity 所限制的订单范围
     * @return
     */
    Integer sumOrderSkuTotal(OrderSku entity);
    
    /**
     * 按指定的条件查数量
     * @authro rainyhao
     * @since 2016-4-1 下午5:43:47
     * @param entity 查询条件
     * @return
     */
    Integer count(OrderSku entity);

	void updateReturnCount(@Param("id")Integer id,@Param("returnCount") Integer returnCount);
}
