package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Contac;
import com.tyiti.easycommerce.entity.JobInfo;
import com.tyiti.easycommerce.entity.MemberInfo;
import com.tyiti.easycommerce.service.CreditService;

@Controller
@RequestMapping(value = "/Credit", produces = "application/json")
public class CreditController {

	@Autowired
	private CreditService creditService;

	/**
	 * 获取用户授信
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/Status", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> Status(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.Status(session);
		return map;

	}

	/**
	 * 获取用户授信信息
	 * 
	 * @param session
	 * 
	 * @return
	 */
	@RequestMapping(value = "/MemerInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> Member(HttpSession session,
			@RequestParam(value = "id", required = true) String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.MemberInfo(session, id);
		return map;
	}

	/**
	 * 提交用户授信信息
	 * 
	 * @param session
	 * 
	 * @return
	 */
	@RequestMapping(value = "/SaveMemerInfo", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> SaveMember(@RequestBody MemberInfo memberInfo,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.SubmitMemberInfo(session, memberInfo);
		return map;
	}

	/**
	 * 获取用户授信工作信息
	 * 
	 * @param session
	 * 
	 * @return
	 */
	@RequestMapping(value = "/JobInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> Job(HttpSession session,
			@RequestParam(value = "id", required = true) String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.JobInfo(session, id);
		return map;
	}

	/**
	 * 提交用户授信工作信息
	 * 
	 * @param session
	 * 
	 * @return
	 */
	@RequestMapping(value = "/SaveJobInfo", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> SaveJob(@RequestBody JobInfo jobInfo,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.SubmitJobInfo(session, jobInfo);
		return map;
	}

	/**
	 * 获取用户授信联系人信息
	 * 
	 * @param session
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ContacInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> Contac(HttpSession session,
			@RequestParam(value = "id", required = true) String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.ContacInfo(session, id);
		return map;
	}

	/**
	 * 提交用户授信联系人信息
	 * 
	 * @param session
	 * 
	 * @return
	 */
	@RequestMapping(value = "/SaveContacInfo", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> SaveContac(@RequestBody Contac[] contacList,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.SubmitContacInfo(session, contacList);
		return map;
	}

	/**
	 * 提交用户授信图片信息
	 * 
	 * @param session
	 * @param type
	 *            A1:身份证（正面） A2:职业证 A3:学生证（封面） A4:储蓄卡 A5:信用卡 A6:驾照（封面） A7:结婚证
	 *            B1:身份证（反面） C1:身份证（手持） B3:学生证信息页 C3:学生证注册页
	 * @param imgUrl
	 *            微信服务器图片地址
	 * @return
	 */
	@RequestMapping(value = "/SaveImgInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> SaveImg(HttpSession session,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "imgUrl", required = true) String imgUrl) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = creditService.SubmitImg(session, type, imgUrl);
		return map;
	}

}