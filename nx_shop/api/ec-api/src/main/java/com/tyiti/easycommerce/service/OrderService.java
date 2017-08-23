package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.User;

public interface OrderService {
	Map<String, Object> addOrder(User user, Order order);

	List<Order> getOrderList(Integer status, Integer custId,Integer offset,
			Integer limit);
	
	int getOrderListCount(Integer status, Integer custId);

	Order getOrderDetail(Integer id, Integer custId);


	Order getById(Integer id);

	int getOrderCountByCustIdAndStatus(Integer custId, Integer status);

	String getOrderNo();

	Map<String, Object> paymentBefore(Integer order_id,String payPassword,
			HttpSession session);

	int updateOrder(Order order);

	Order getByNo(String no);
	
	int updateNameAndMobile(Map<String, Object> param);

	 /**
	  * <p>功能描述：判断用户支付的订单时否有效。</p>	
	  * @param map
	  * @param order
	  * @param user
	  * @return
	  * <p>创建日期:2016年8月12日 上午10:19:56。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> getOrderIsSuccessOrder(Order order, User user);

	List<Order> getOrderSkuList(Map<String, Object> params);

	Long getOrderSkuListCount(Map<String, Object> params);

	Map<String, Object> addSkusOrder(User user, Order order);
}