package com.tyiti.easycommerce.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Address;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.AddressDao;
import com.tyiti.easycommerce.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	private AddressDao addressDao;

	@Override
	public Address addAddress(HttpSession session, Address address) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer userId = user.getId();
		address.setCustId(userId);
		int count = addressDao.add(address);
		if (count < 1) {
			return null;
		}
		return addressDao.getById(address.getId());
	}

	@Override
	public boolean detaleAddress(Integer id) {
		int count = addressDao.delete(id);
		if (count < 1) {
			return false;
		}
		return true;
	}

	@Override
	public Address updateAddress(Address address) {
		int count = addressDao.update(address);
		if (count < 1) {
			return null;
		}
		return addressDao.getById(address.getId());
	}

	@Override
	public List<Address> getAll(HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer userId = user.getId();
		return addressDao.getAll(userId);
	}

	@Override
	public Address getById(Integer id) {
		return addressDao.getById(id);
	}

	@Override
	public boolean clearDefaultAddress(HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer userId = user.getId();
		int count = addressDao.clearDefaultAddress(userId);
		if (count < 1) {
			return false;
		}
		return true;
	}

	@Override
	public Address getByIdAndUserId(Integer id, Integer custId) {
		return addressDao.getByIdAndUserId(id, custId);
	}
}
