package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.OrderPayment;

public interface OrderPaymentService {

	List<Map<String, Object>> getDiscountDetailByOrderId(Integer id);

	List<OrderPayment> getByOrderId(Integer id);

}
