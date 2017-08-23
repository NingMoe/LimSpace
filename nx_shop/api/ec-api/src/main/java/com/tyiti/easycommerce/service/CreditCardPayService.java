package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.entity.CreditCardPay;

public interface CreditCardPayService {

	CreditCardPay creditCardPay(Integer id);
	
	Map<String, Object> notifyCall(String signMsg);
		
	String getCebsuccUrl();
	
	String getCebfailUrl();
	
	String getLotteryUrl();
	
	Integer getLotterySkuId();
}
