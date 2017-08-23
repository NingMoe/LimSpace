package com.tyiti.easycommerce.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.UserService;

@Controller
public class PrivateInfoController {
	@Autowired
	private UserService userService;
	
	/**
	 * 个人中心-个人信息
	 * @author Black
	 * @date 2016-07-19
	 * @return
	 */
	@RequestMapping(value = "/privateInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPrivateInfo(HttpSession session) {

		User user = (User) session.getAttribute(Constants.USER);

		return userService.getPrivateInfo(user);
	}

}
