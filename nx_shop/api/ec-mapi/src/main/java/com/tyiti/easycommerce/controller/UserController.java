package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.service.UserService;

@Controller
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {
	@Autowired
	private UserService userService;

	private Log log = LogFactory.getLog(UserController.class);

	/**
	 * @Title: 获取用户列表
	 * @return Map<String,Object> 返回类型
	 * @author wyy 2016/07/15
	 * @throws
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getUserList(
			@RequestParam Map<String, Object> param,
			HttpServletRequest request, HttpSession session) {
		log.info("获取用户列表参数：" + param);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("code", 200);
			map.put("messsge", "OK");			
			map.put("data", userService.getUserList(param));
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "用户查询出错,请联系管理员");
			return map;
		}

		return map;
	}

}