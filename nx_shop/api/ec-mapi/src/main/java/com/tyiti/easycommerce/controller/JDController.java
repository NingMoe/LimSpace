package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.service.JDService;

/**
 * 京东商品接口
 * @author shenzhiqiang
 * @ClassName: JDController 
 * @Description: 
 * @date 2016年6月13日 下午4:27:58
 */
@Controller
public class JDController {

	Logger logger = Logger.getLogger(JDController.class);
	
	@Autowired
	JDService JDService;
	
	@RequestMapping(value = "/queryPoolnum", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryPoolNum(){
		Map<String, Object> map = new HashMap<String, Object>();
		JDService.queryPoolNum();
		return map;
	}
}
