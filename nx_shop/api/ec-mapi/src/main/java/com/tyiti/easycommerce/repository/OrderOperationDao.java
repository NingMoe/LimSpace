package com.tyiti.easycommerce.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.OrderCancellation;

public interface OrderOperationDao {

	/**
	 * 申请取消
	 * 
	 * @param id
	 * @param custId
	 * @return
	 */
	int applyToCancel(@Param("orderId") Integer orderId, @Param("no") String no, @Param("reason") String reason);

	/**
	 * 取消订单
	 * 
	 * @param id
	 * @param custId
	 * @return
	 */
	int cancel(@Param("id") Integer id, @Param("custId") Integer custId);
	
	/**
	 * 取消订单时如果有首付或分期，则插入退货
	 * 
	 * @param orderId
	 * @param amount
	 * @return
	 */
	int insertRefund(@Param("orderId") Integer orderId, @Param("amount") BigDecimal amount);
	
	/**
	 * 删除订单
	 * 
	 * @param id
	 * @param custId
	 * @return
	 */
	int delete(@Param("id") Integer id, @Param("custId") Integer custId);

	/**
	 * 签收订单
	 * 
	 * @param id
	 * @param custId
	 * @return
	 */
	int sign(@Param("id") Integer id, @Param("custId") Integer custId);

	/**
	 * 获取OrderCancellation
	 * 
	 * @param orderId
	 * @param type
	 * @return
	 */
	List<OrderCancellation> getByOrderIdAndType(@Param("orderId") Integer orderId, @Param("type") Integer type);

	/**
	 * 申请退货
	 * 
	 * @param id
	 * @param custId
	 * @return
	 */
	int applyToReturn(@Param("orderId") Integer orderId, @Param("custId") Integer custId, @Param("no") String no,@Param("reason") String reason);

	/**
	 * 退货列表
	 * 
	 * @param id
	 * @param custId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<OrderCancellation> getReturnList(@Param("custId") Integer custId,@Param("offset") Integer offset,@Param("limit") Integer limit);

	/**
	 * 退货列表总条数
	 * 
	 * @param id
	 * @param custId
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Select("select count(1) from t_order_cancellation c join t_order o on c.order_id = o.id left outer join t_order_sku s on s.order_id = o.id where o.cust_id =#{custId}  and c.type = 2")
	int getReturnListCount(@Param("custId") Integer custId);

	
	/**
	 * 退货详细
	 * 
	 * @param id
	 * @param custId
	 * @return
	 */
	OrderCancellation getReturnDetail(@Param("id") Integer id, @Param("custId") Integer custId);
}
