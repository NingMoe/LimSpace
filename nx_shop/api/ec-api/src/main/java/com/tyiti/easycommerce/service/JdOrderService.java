package com.tyiti.easycommerce.service;

import java.util.Map;


public interface JdOrderService {
	

	String getAccessToken();
	
	Map<String, Object> addOrder(String method, Map<String, Object> map);
	
}