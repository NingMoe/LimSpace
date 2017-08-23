package com.tyiti.easycommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.repository.PollingDao;
import com.tyiti.easycommerce.service.PollingService;

@Service("pollingService")
public class PollingServiceImpl implements PollingService {

	@Autowired
	private PollingDao pollingDao;
	@Override
	public int updateReceiptTime() {
		return pollingDao.updateReceiptTime();
	}
	@Override
	public int updateOrderStatus(String token) {
		if (token!=null && "orderstatus@123".equals(token)) {
			return pollingDao.updateOrderStatus();
		}else {
			return -1;
		}
	}

}
