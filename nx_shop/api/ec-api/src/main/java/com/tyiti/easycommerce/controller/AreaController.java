package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Area;
import com.tyiti.easycommerce.service.AreaService;

@Controller
public class AreaController {
	@Autowired
	private AreaService AreaService;
    
	@RequestMapping(value = "/area/{id}&{level}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getArea(@PathVariable("id") int id,@PathVariable("level") int level,
			HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Area> areaList = AreaService.getAreasByParentIdAndLevel(id, level) ;
		data.put("data", areaList);
		return data;
	}
}
