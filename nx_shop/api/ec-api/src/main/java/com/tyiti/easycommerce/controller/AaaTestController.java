package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Address;
import com.tyiti.easycommerce.service.AaaTestService;
import com.tyiti.easycommerce.service.AddressService;
import com.tyiti.easycommerce.util.LogUtil;

/**
 * 测试专用
 * @author wangqi
 * @date 2016年5月9日 下午4:24:48
 * @description TODO
 */
@RequestMapping("/test")
@Controller
public class AaaTestController {
	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private AddressService addressService;
	@Autowired
	private AaaTestService aaaTestService;

	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> add(@RequestBody Address address, HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
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

	@RequestMapping(value = "/testGet", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTest(HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		int i = aaaTestService.updateTest(5, 1);
		System.out.println("====================="+i);
		return data;
	}

}
