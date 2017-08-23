package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Spec;
import com.tyiti.easycommerce.service.SpecService;

@Scope("prototype")
@Controller
public class SpecController {

	@Autowired
	private SpecService specService;

	/**
	 * 
	 * @Title: add
	 * @Description: TODO(规格添加)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/specs", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> add(@RequestBody Spec spec) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int num = specService.selectCountByName(spec.getName());
			if (num == 0) {
				specService.addSpec(spec);
				map.put("code", "200");
			} else {
				map.put("code", "400");
				map.put("message", "重复添加");
			}
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "添加失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/specs", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> edit(@RequestBody Spec spec) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			specService.update(spec);
			map.put("code", "200");
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "修改失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}

	/**
	 * @Title: specList
	 * @Description: TODO(获取列表)
	 * @return Map<String,Object> 返回类型
	 * @author Yan Zuoyu
	 * @throws
	 */
	@RequestMapping(value = "/specs", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> specList() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Spec> specList = specService.specList();
			map.put("code", "200");
			map.put("data", specList);
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "查询列表失败");
			map.put("exception", e.getMessage());
		}
		return map;
	}
}
