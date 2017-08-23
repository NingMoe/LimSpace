package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.service.LeaveMessageService;

/**
 * @author wangqi
 * @date 2016-4-22 上午9:32:32
 * @description 用户留言
 */
@Scope("prototype")
@Controller
public class LeaveMessageController {
	@Autowired
	private LeaveMessageService leaveMessageService;
	
	/**
	 * 管理员修改留言状态
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/leaveMessage/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> addOneLeMessage(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		int flag = leaveMessageService.updateStatus(id);
		if(flag != 1){
			data.put("code", 400);
			data.put("message", "操作失败！");
		}else{
			data.put("code", 200);
			data.put("message", "操作成功！");
		}
		return data;
	}
	/**
	 * 分页查询
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/leaveMessage",  method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> infoList(@RequestParam Map<String, Object> param, HttpServletResponse response ) {
		Map<String, Object> map = new HashMap<String, Object>();
		SearchResult<Map<String, Object>> resultList = null; 
		resultList= leaveMessageService.queryLeMsgByPage(param);
	    map.put("data", resultList);
		return map;
	}
}
