package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.PickupPoint;

public interface PickupPointService {

	void add(PickupPoint pickupPoint);

	void edit(PickupPoint pickupPoint);

	PickupPoint selectById(Integer id);

	void del(Integer id);

	List<PickupPoint> selectList();

	void updateStatus(Integer id);

	void updateRank(Integer id, Integer rank);

}
