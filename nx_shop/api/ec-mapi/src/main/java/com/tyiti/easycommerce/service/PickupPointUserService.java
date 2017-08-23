package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.PickupPointUser;

public interface PickupPointUserService {

	PickupPointUser selectById(Integer id);

	void add(PickupPointUser pickupPointUser);

	void edit(PickupPointUser pickupPointUser);

	void del(Integer id);

	List<PickupPointUser> selectListByPointId(Integer id);

	List<PickupPointUser> selectPointUsers();

	void updateStatus(Integer id);

}
