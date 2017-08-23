package com.tyiti.easycommerce.service;

public interface PollingService {

	int updateReceiptTime();
	
	int updateOrderStatus(String token);
}
