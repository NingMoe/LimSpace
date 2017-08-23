package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Address;
import com.tyiti.easycommerce.service.AddressService;

@Controller
public class AddressController {
	@Autowired
	private AddressService addressService;

	@RequestMapping(value = "/address", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> addSAddress(@RequestBody Address address, HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		boolean isDefault = address.getIsDefault();
		if (isDefault) {
			addressService.clearDefaultAddress(session);
		}
		address = addressService.addAddress(session, address);
		data.put("data", address);
		return data;
	}

	@RequestMapping(value = "/address/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delete(@PathVariable("id") Integer id, HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		boolean b = addressService.detaleAddress(id);
		data.put("data", b);
		return data;
	}

	@RequestMapping(value = "/address", method = RequestMethod.PUT, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> updateAddress(@RequestBody Address address, HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		boolean isDefault = address.getIsDefault();
		if (isDefault) {
			addressService.clearDefaultAddress(session);
		}
		address = addressService.updateAddress(address);
		data.put("data", address);
		return data;
	}

	@RequestMapping(value = "/address/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer id, HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		Address address = addressService.getById(id);
		data.put("data", address);
		return data;
	}

	@RequestMapping(value = "/address", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAll(Address address, HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Address> addressList = addressService.getAll(session);
		data.put("data", addressList);
		return data;
	}

}
