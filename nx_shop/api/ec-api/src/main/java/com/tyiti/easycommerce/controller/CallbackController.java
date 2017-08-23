 /**
  * 文件名[fileName]：CallbackController.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年7月7日 下午5:00:21
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.tyiti.easycommerce.service.CallbackService;

/**
  *<p>类描述：重构回调方法。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年7月7日 下午5:00:21。</p>
  */

public class CallbackController {
	@Autowired
	CallbackService callbackService;
	
	public Map<String,Object>   Callback(HttpServletRequest reqeust,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		String strMessage = "";
		String payMenth="";
		map.put("message", strMessage);
		callbackService.callback(map,payMenth,reqeust,response);
		return map;
	}
}
