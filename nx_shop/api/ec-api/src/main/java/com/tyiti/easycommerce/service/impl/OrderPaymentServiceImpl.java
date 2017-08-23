package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.repository.OrderPaymentDao;
import com.tyiti.easycommerce.service.OrderPaymentService;

@Service("orderPaymentService")
public class OrderPaymentServiceImpl implements OrderPaymentService{
	@Autowired
	private OrderPaymentDao orderPaymentDao;
	@Override
	public Integer updateStatus(Integer orderId, Integer status) {
		// TODO Auto-generated method stub
		return orderPaymentDao.updateOrderPaymentStatus(orderId, status);
	}
	@Override
	public List<OrderPayment> selectOrderPaymentByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		return orderPaymentDao.getByOrderId(orderId);
	}
	@Override
	public List<OrderPayment> selectOrderPaymentByPickUpOrderId(
			Integer pickUpOrderid) {
		// TODO Auto-generated method stub
		return null;
	}

}
