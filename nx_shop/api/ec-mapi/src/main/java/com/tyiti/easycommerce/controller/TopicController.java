package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tyiti.easycommerce.service.TopicService;

@Controller
@RequestMapping(value = "/topics", produces = "application/json")
public class TopicController {
	@Autowired
	private TopicService topicService;
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile, 
			@RequestParam(value = "path", required = false) String path, 
			@RequestParam(value = "newPath", required = false) String newPath) {
		Map<String, Object> result = topicService.upload(multipartFile, path, newPath);
		
		String responseText = "<!doctype html><html><head><meta charset=\"utf-8\"><script>window.parent.postMessage(['%s', %s, '%s'], '*');</script></head><body></body></html>";
		
		return String.format(responseText, path, result.get("code"), result.get("message"));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 0);
		map.put("message", "OK");
		map.put("data", topicService.list());

		return map;
	}
}
