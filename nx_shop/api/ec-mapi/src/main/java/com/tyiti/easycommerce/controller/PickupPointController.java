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

import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.PickupPointUser;
import com.tyiti.easycommerce.service.PickupPointService;
import com.tyiti.easycommerce.service.PickupPointUserService;

@Scope("prototype")
@Controller
@RequestMapping(value="pickups")
public class PickupPointController {

	@Autowired
	private PickupPointService pickupPointService;
	
	@Autowired
	private PickupPointUserService pickupPointUserService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> pickupList() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<PickupPoint> pickupPoint = pickupPointService.selectList();
			map.put("code", 200);
			map.put("pickupPoint", pickupPoint);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}

		return map;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> pickup(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			PickupPoint pickupPoint = pickupPointService.selectById(id);
			List<PickupPointUser>	pickupPointUser = pickupPointUserService.selectListByPointId(id);
			for (PickupPointUser pickupPointUser2 : pickupPointUser) {
				if(pickupPointUser2.getType()==1){
					pickupPoint.setLoginName(pickupPointUser2.getLoginName());
				}
			}
			map.put("code", 200);
			map.put("pickupPoint", pickupPoint);
			
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}

		return map;
	}

	@RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> pickupusers(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<PickupPointUser>	pickupPointUser = pickupPointUserService.selectListByPointId(id);
			map.put("code", 200);
			map.put("pickupPointUser", pickupPointUser);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}

		return map;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public synchronized Map<String, Object> add(@RequestBody PickupPoint pickupPoint  ) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pickupPointService.add(pickupPoint);
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
	public Map<String, Object> edit(@RequestBody PickupPoint pickupPoint) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pickupPointService.edit(pickupPoint);
			map.put("code", 200);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> del(@PathVariable Integer id ) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			pickupPointService.del(id);
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
			pickupPointService.updateStatus(id);
			map.put("code", 200);
		} catch (Exception e) {
			map.put("code", 400);
			// TODO: handle exception
			map.put("exception", e.getMessage());
		}
		return map;
	}
	@RequestMapping(value = "/{id}/rank/{rank}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> updateRank(@PathVariable Integer id , @PathVariable Integer rank ) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			pickupPointService.updateRank(id,rank);
			map.put("code", 200);
		} catch (Exception e) {
			map.put("code", 400);
			// TODO: handle exception
			map.put("exception", e.getMessage());
		}
		return map;
	}
}
