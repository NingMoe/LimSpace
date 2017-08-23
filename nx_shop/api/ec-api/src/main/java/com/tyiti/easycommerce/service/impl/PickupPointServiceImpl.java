package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.repository.PickupPointDao;
import com.tyiti.easycommerce.service.PickupPointService;

@Service("pickupPointService")
public class PickupPointServiceImpl implements PickupPointService{

	@Autowired
	private PickupPointDao pickupPointDao ;

	@Override
	public List<PickupPoint> getPickupPointList() {
		// TODO Auto-generated method stub
		return pickupPointDao.selectPickupPointList();
	}

	 

	@Override
	public PickupPoint selectPickupPointById(Integer id) {
		// TODO Auto-generated method stub
		return pickupPointDao.selectByPrimaryKey(id);
	}
}
