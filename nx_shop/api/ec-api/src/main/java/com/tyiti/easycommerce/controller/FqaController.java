package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.service.FqaService;

/**
 * @author wangqi
 * @date 2016-4-22 上午10:11:54
 * @description 常见问题
 */
@Controller
public class FqaController {
	@Autowired
	private FqaService fqaService;
	
	@RequestMapping(value = "/fqa", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getList(@RequestParam Map<String, Object> param, HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		SearchResult<Map<String, Object>> resultList = null; 
		resultList= fqaService.queryFqaByPage(param);
	    map.put("data", resultList);
		return map;
	}
}
