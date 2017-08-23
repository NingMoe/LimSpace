package com.tyiti.easycommerce.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.repository.OrderPaymentDao;
import com.tyiti.easycommerce.service.OrderPaymentService;

@Service("orderPaymentService")
public class OrderPaymentServiceImpl implements OrderPaymentService{

	@Autowired
	private  OrderPaymentDao orderPaymentDao ;

	@Override
	public List<Map<String, Object>> getDiscountDetailByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		
		return orderPaymentDao.getDiscountDetail(orderId);
	}

	@Override
	public List<OrderPayment> getByOrderId(Integer id) {
		// TODO Auto-generated method stub
		return orderPaymentDao.getByOrderId(id);
	}
}
