package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.RushBuy;
import com.tyiti.easycommerce.service.RushBuyService;

/**
 * 
 * Title:RushBuyController.java
 * <p>
 * Description:通用秒杀
 * 
 * @author: xulihui
 * @date: 2016年4月8日 下午3:03:31
 */
@Scope("prototype")
@Controller
public class RushBuyController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RushBuyController.class);

	@Autowired
	private RushBuyService rushBuyService;

	/**
	 * 添加秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午3:53:36
	 * @param rushBuy
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rushBuy/add", method = RequestMethod.PUT)
	public Map<String, Object> insertRushBuyInfo(@RequestBody RushBuy rushBuy, @RequestParam Map<String, Object> map) {
		int num = rushBuyService.insertRushBuy(rushBuy);
		if (num != 1) {
			map.put("code", "400");
			map.put("message", "添加失败");
			logger.error("秒杀添加失败!");
		} else {
			map.put("code", "200");
			map.put("message", "OK");
		}
		return map;
	}

	/**
	 * 删除秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午3:54:05
	 * @param rushBuy
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rushBuy/del/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> delRushBuy(@PathVariable("id") Integer id, @RequestParam Map<String, Object> map) {
		// Map<String,Object> map = new HashMap<String,Object>();
		int num = rushBuyService.delRushBuy(id);
		if (num != 1) {
			map.put("code", "400");
			map.put("message", "删除失败");
			logger.error("秒杀删除失败!");
		} else {
			map.put("code", "200");
			map.put("message", "OK");
		}
		return map;
	}

	/**
	 * 修改秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午3:54:32
	 * @param rushBuy
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rushBuy/edit", method = RequestMethod.PUT)
	public Map<String, Object> updateRushBuy(@RequestBody RushBuy rushBuy, @RequestParam Map<String, Object> map) {

		int num = rushBuyService.updateRushBuy(rushBuy);
		if (num != 1) {
			map.put("code", "400");
			map.put("message", "修改失败");
			logger.error("秒杀修改失败!");
		} else {
			map.put("code", "200");
			map.put("message", "OK");
		}
		return map;
	}

	/**
	 * 查询秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午3:54:51
	 * @param rushBuy
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rushBuy/sel/{id}", method = RequestMethod.GET)
	public Map<String, Object> selectRushBuy(@PathVariable("id") Integer id, @RequestParam Map<String, Object> map) {
		try {
			List<RushBuy> rushBuyList = rushBuyService.RushBuyList(id);
			map.put("code", 200);
			map.put("message", "OK");
			map.put("data", rushBuyList);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("message", e.getMessage());
			logger.info("秒杀查询失败!");
		}
		return map;
	}

	/**
	 * 查询秒杀列表
	 * 
	 * @author :xulihui
	 * @since :2016年4月12日 下午3:54:55
	 * @param param
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rushBuy/list", method = RequestMethod.GET)
	public Map<String, Object> getRushBuyList(@RequestParam Map<String, Object> param, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		SearchResult<Map<String, Object>> rushBuyList = null;

		try {
			rushBuyList = rushBuyService.getRushBuyList(param);
		} catch (Exception e) {
			map.put("code", 400);
			map.put("exception", e.getMessage());
			map.put("message", "查询秒杀列表失败");
			logger.error("查询秒杀列表失败!");
			return map;
		}
		map.put("code", 200);
		map.put("messsge", "OK");
		map.put("data", rushBuyList);

		return map;
	}

}
