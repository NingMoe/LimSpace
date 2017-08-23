package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.PickupPointUser;
import com.tyiti.easycommerce.service.PickupPointUserService;

@Scope("prototype")
@Controller
@RequestMapping("pointusers")
public class PickupPointUserController {

	@Autowired
	private PickupPointUserService pickupPointUserService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> pick(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PickupPointUser pickupPointUser = pickupPointUserService.selectById(id);
			map.put("code", 200);
			map.put("data", pickupPointUser);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}

		return map;
	}

	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> pickList() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<PickupPointUser> pickupPointUser = pickupPointUserService.selectPointUsers();
			map.put("code", 200);
			map.put("data", pickupPointUser);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}

		return map;
	}
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public synchronized Map<String, Object> add(@RequestBody PickupPointUser pickupPointUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pickupPointUserService.add(pickupPointUser);
			map.put("code", 200);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> edit(@RequestBody PickupPointUser pickupPointUser) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			pickupPointUserService.edit(pickupPointUser);
			map.put("code", 200);
		} catch (Exception e) {
			map.put("code", 400);
			// TODO: handle exception
			map.put("exception", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> del(@PathVariable Integer id ) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			pickupPointUserService.del(id);
			map.put("code", 200);
		} catch (Exception e) {
			map.put("code", 400);
			// TODO: handle exception
			map.put("exception", e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> updateStatus(@PathVariable Integer id ) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			pickupPointUserService.updateStatus(id);
			map.put("code", 200);
		} catch (Exception e) {
			map.put("code", 400);
			// TODO: handle exception
			map.put("exception", e.getMessage());
		}
		return map;
	}
}
