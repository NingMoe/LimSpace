package com.tyiti.easycommerce.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.Address;

public interface AddressService {
	Address addAddress(HttpSession session, Address address);

	boolean detaleAddress(Integer id);

	Address updateAddress(Address address);

	Address getById(Integer id);

	List<Address> getAll(HttpSession session);

	boolean clearDefaultAddress(HttpSession session);

	Address getByIdAndUserId(Integer id, Integer custId);
}