package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

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


	void updateReturnCount(@Param("id")Integer id,@Param("returnCount") Integer returnCount );

	 /**
	  * <p>功能描述：。</p>	
	  * @param skuId
	  * @param custId
	  * @param activityId
	  * <p>创建日期:2016年8月5日 下午4:11:34。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 * @return 
	  */
	List<Map<String, Object>> findOrderSkuActivity(@Param("skuId")Integer skuId, @Param("custId")Integer custId,  @Param("activityId")Integer activityId);


}
