package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.service.FavoriteService;

@Controller
public class FavoriteController {
	@Autowired
	private FavoriteService favoriteService;

	@RequestMapping(value = "/favorite", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAll(@RequestParam Map<String, Object> param,
			HttpServletResponse response) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		SearchResult<Map<String, Object>> list = favoriteService.getAll(param) ;
		data.put("data", list);
		return data;
	}
	
}
