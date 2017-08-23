package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.Order;

public interface OrderDao {

	int addOrder(Order order);

	List<Order> orderList(@Param("status") Integer status,
			@Param("custId") Integer custId, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	@Select("select count(1) orderCount from t_order o LEFT outer join t_order_sku s on o.id=s.order_id LEFT outer join t_order_cancellation c on o.id=c.order_id and c.status != 2 where invalid = 0 and o.status=#{status} and o.cust_id = #{custId}")
	int getOrderListCount(@Param("status") Integer status,
			@Param("custId") Integer custId);

	List<Order> orderListReturn(@Param("status") Integer status,
			@Param("custId") Integer custId, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	Order orderDetail(@Param("id") Integer id, @Param("custId") Integer custId);

	Order getById(@Param("id") Integer id);

	Order getByCustIdOrderById(@Param("custId") Integer custId);

	@Select("select count(1) orderCount from t_order where status=#{status} and cust_id = #{custId}")
	int getByCustIdAndStatus(@Param("custId") Integer custId,
			@Param("status") Integer status);

	List<Order> getByCriteria(Order order);

	int updateOrder(Order order);

	Order getByNo(@Param("no") String no);

	int updateNameAndMobile(Map<String, Object> param);
	
	double getDiscount(Integer orderId);

	Long selectOrderSkusCount(Map<String, Object> params);

	List<Order> selectOrderSkusList(Map<String, Object> params);

	Map<String, Object> getOrderSkuCount(Integer id);

	void updateSkuCount(@Param("skuId") int skuId,
			@Param("skuCount") int skuCount);

	/**
	  * <p>功能描述：活动已经结束，修改所有活动下了订单但是没有支付的订单修改为一失效。。</p>	
	  * @param id
	  * @return
	  * <p>创建日期:2016年9月8日 下午6:44:47。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Order> findOrderActivityList(@Param("activityId")Integer id);
}
