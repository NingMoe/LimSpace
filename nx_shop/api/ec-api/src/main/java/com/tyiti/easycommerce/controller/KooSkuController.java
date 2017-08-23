package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.service.KooSkuService;
/**
 * koolearn 新东方
 * @author wangqi
 * @date 2016年5月12日 上午9:38:59
 * @description TODO
 */
@Controller
public class KooSkuController {
	@Autowired
	private KooSkuService kooSkuService;

	/**
	 * 商品详情
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/kooSku/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> res = kooSkuService.findDetailById(id);
		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}

		data = res;

		return data;
	}
	
}
