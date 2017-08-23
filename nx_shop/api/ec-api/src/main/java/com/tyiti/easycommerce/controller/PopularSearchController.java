package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tyiti.easycommerce.entity.PopularSearch;
import com.tyiti.easycommerce.service.PopularSearchService;

@Controller
public class PopularSearchController {
	@Autowired
	private PopularSearchService popularSearchService;

	@RequestMapping(value = "/popularsearch", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAll() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<PopularSearch> popularSearchList = popularSearchService.getAll();
		data.put("data", popularSearchList);
		return data;
	}

}
