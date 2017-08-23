package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.service.MessageService;



@Controller

public class MessageCtrl {
	
	@Autowired
	private MessageService messageService;
	
	
	@RequestMapping(value = "/messageTpye" , method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> messageType(@RequestParam Map<String, Object> param,HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		map = messageService.getmessageType(param,session);
		return map;
	}
	

}
