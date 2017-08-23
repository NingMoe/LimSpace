package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.service.PickupPointService;

@Controller
@RequestMapping(value="/pickupaddress",produces="application/json")
public class PickupPointController {
	
	@Autowired
	private PickupPointService pickupPointService ; 
	
	@RequestMapping
	@ResponseBody
	public Map<String,Object> address(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<PickupPoint> pickupPointList = 	pickupPointService.getPickupPointList();
			map.put("code", 200);
			map.put("data", pickupPointList);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}
				
		return map ; 
	}

	@RequestMapping("/{id}")
	@ResponseBody
	public Map<String,Object> addressDetail(@PathVariable Integer id ){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			 PickupPoint  pickupPoint  = 	pickupPointService.selectPickupPointById(id);
			map.put("code", 200);
			map.put("data", pickupPoint );
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("exception", e.getMessage());
		}
				
		return map ; 
	}	
}
