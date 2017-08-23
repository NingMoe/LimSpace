package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.LeaveMessage;
import com.tyiti.easycommerce.service.LeaveMessageService;

/**
 * @author wangqi
 * @date 2016-4-22 上午9:32:32
 * @description 用户留言
 */
@Controller
public class LeaveMessageController {
	@Autowired
	private LeaveMessageService leaveMessageService;
	
	@RequestMapping(value = "/leaveMessage", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> addOneLeMessage(@RequestBody LeaveMessage leaveMessage,
			HttpServletResponse response,HttpServletRequest request, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		boolean flag = leaveMessageService.addOneLeMessage(leaveMessage,session,request);
		if(!flag){
			data.put("code", 400);
			data.put("message", "留言失败！");
		}else{
			data.put("code", 200);
			data.put("message", "留言成功！");
		}
		return data;
	}
}
