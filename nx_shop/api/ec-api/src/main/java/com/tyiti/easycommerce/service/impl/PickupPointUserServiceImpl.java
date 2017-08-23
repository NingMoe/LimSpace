package com.tyiti.easycommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.PickupPointUser;
import com.tyiti.easycommerce.repository.PickupPointUserDao;
import com.tyiti.easycommerce.service.PickupPointUserService;
@Service
public class PickupPointUserServiceImpl implements PickupPointUserService {
	
	@Autowired
	private PickupPointUserDao pickupPointUserDao;
	
	
	@Override
	public PickupPointUser selectForObject(PickupPointUser entity) {
		// TODO Auto-generated method stub
		return pickupPointUserDao.selectForObject(entity);
	}

}
