package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.OrderPayment;

public interface OrderPaymentService {

	Integer updateStatus(Integer orderId, Integer status);

	List<OrderPayment> selectOrderPaymentByOrderId(Integer orderId);
	
	List<OrderPayment> selectOrderPaymentByPickUpOrderId(Integer pickUpOrderid);

}
