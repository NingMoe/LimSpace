package com.tyiti.easycommerce.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.Contac;
import com.tyiti.easycommerce.entity.MemberInfo;
import com.tyiti.easycommerce.entity.JobInfo;

public interface CreditService {

	/**
	 * 获取用户授信获取用户的授信状态
	 * 
	 * @return
	 */
	Map<String, Object> Status(HttpSession session);

	/**
	 * 获取用户授信获取用户的信息
	 * 
	 * @return
	 */
	Map<String, Object> MemberInfo(HttpSession session, String id);

	/**
	 * 提交用户授信获取用户的信息
	 * 
	 * @return
	 */
	Map<String, Object> SubmitMemberInfo(HttpSession session,
			MemberInfo memberInfo);

	/**
	 * 获取用户的工作信息
	 * 
	 * @return
	 */
	Map<String, Object> JobInfo(HttpSession session, String id);

	/**
	 * 提交用户的工作信息
	 * 
	 * @return
	 */
	Map<String, Object> SubmitJobInfo(HttpSession session, JobInfo jobInfo);

	/**
	 * 获取用户的联系人信息
	 * 
	 * @return
	 */
	Map<String, Object> ContacInfo(HttpSession session, String id);

	/**
	 * 提交用户的联系人信息
	 * 
	 * @return
	 */
	Map<String, Object> SubmitContacInfo(HttpSession session,
			Contac[] contacList);

	/**
	 * 提交用户的联系人信息
	 * 
	 * @return
	 */
	Map<String, Object> SubmitImg(HttpSession session, String type,
			String imgUrl);

}