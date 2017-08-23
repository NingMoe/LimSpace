package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.service.BillService;

/**
 * @author wangqi
 * @date 2016年4月29日 下午1:46:24
 * @description TODO 账单
 */
@Controller
@RequestMapping("/bill")
public class BillController {
	
	@Autowired
	private BillService billService;
	/**
	 * 账单列表
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> billList(@RequestParam Map<String, Object> param,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = billService.getBillList(param,session);
		return map;
	}
	/**
	 * 账单详情
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer id,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = billService.getBillById(id,session);
		return map;
	}
	/**
	 * 账单还款
	 * @param session
	 * grossAmount 还款总金额  billArr 账单还款id串，多个用英文逗号隔开
	 * @return
	 */
	/*@RequestMapping(value="/repayment",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> repayment(@RequestParam Map<String, Object> param,HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = billService.repayment(param,session);
		return map;
	}*/
}
