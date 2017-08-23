package com.tyiti.easycommerce.controller;

import java.util.Date;
import java.util.HashMap;
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

import com.ibm.icu.text.SimpleDateFormat;
import com.tyiti.easycommerce.entity.Headline;
import com.tyiti.easycommerce.entity.Share;
import com.tyiti.easycommerce.entity.SkuShelvesSchedule;
import com.tyiti.easycommerce.entity.Warning;
import com.tyiti.easycommerce.service.TagService;
import com.tyiti.easycommerce.service.WarningService;

/**
 * @author wyy
 * @date 2016-9-5 PM 14:03:32
 * @description 用户预警
 */
@Scope("prototype")
@Controller
public class WarningController {
	@Autowired
	private WarningService warningService;
	Logger logger = Logger.getLogger(WarningController.class);
	@Autowired
	private TagService tagService;

	/**
	 * @description 添加预警值 全部的时候放在t_config 表 key=sku_warning_all value=预警值
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/warning", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> addWarning(@RequestBody Warning warning,
			HttpServletResponse response) {
		return warningService.addWarning(warning, response);
	}

	/**
	 * 获取预警用户
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/warning/user", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getWarningUser(
			@RequestParam Map<String, Object> param,
			HttpServletResponse response) {
		return warningService.getWarningUser(param, response);
	}

	/**
	 * 获取部分预警SKU
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/warning/sku", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getWarningSku(HttpServletResponse response) {
		return warningService.getWarningSku(response);
	}

	/**
	 * 删除预警用户
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/warning/user", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> delWarningUser(@RequestBody Warning warning,
			HttpServletResponse response) {
		return warningService.delWarningUser(warning, response);
	}

	/**
	 * 删除部分预警sku
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/warning/sku", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> delWarningSku(@RequestBody Warning warning,
			HttpServletResponse response) {
		return warningService.delWarningSku(warning, response);
	}

	/**
	 * 定时上下架
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/timingShelves", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> addTimingShelves(
			@RequestBody SkuShelvesSchedule sss, HttpServletResponse response) {
		return warningService.addTimingShelves(sss, response);
	}

	/**
	 * 获取定时上下架列表
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/timingShelves", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTimingShelves(
			@RequestParam Map<String, Object> map,
			HttpServletResponse response) {		
		return warningService.getTimingShelves(response,mapToSSS(map));
	}

	/**
	 * 结束制定 定时上下架
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/timingShelves/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> endTimingShelves(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		return warningService.endTimingShelves(id, response);
	}
	
	/**
	 * 结束制定 定时上下架
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/timingShelves/del/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delTimingShelves(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		return warningService.delTimingShelves(id, response);
	}

	/**
	 * 定时设置上下架 每一分钟设置一次
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/timingSku", method = RequestMethod.GET)
	@ResponseBody
	public void addtimingSku(@RequestParam Map<String, Object> param,
			HttpServletResponse response) {
		warningService.addtimingSku();
	}

	/**
	 * 添加头条
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/headline", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> addHeadline(@RequestBody Headline headline,
			HttpServletResponse response) {
		return warningService.addHeadline(headline, response);
	}

	/**
	 * 删除或结束头条
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/headline/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> delHeadline(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		return warningService.delHeadline(id, response);
	}

	/**
	 * 获取头条
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/headline", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHeadlineList(HttpServletResponse response) {
		return warningService.getHeadlineList(response);
	}

	/**
	 * 获取头条
	 * 
	 * @param param
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/headline/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getHeadline(@PathVariable("id") Integer id,
			HttpServletResponse response) {
		return warningService.getHeadline(id, response);
	}

	@RequestMapping(value = "/headline", method = RequestMethod.PUT, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> updateHeadline(@RequestBody Headline headline,
			HttpServletResponse response) {
		return warningService.updateHeadline(headline, response);
	}

	/**
	 * 按父标签及深度获取标签树及商品列表(预警)
	 * 
	 * @author :
	 * @since :2016年4月18日 下午2:23:48
	 * @param tagId
	 * @param level
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tags/skus", method = RequestMethod.GET)
	public Map<String, Object> listTagSkusByTagIdAndLevelAndLimit(
			@RequestParam("code") String code,
			@RequestParam("level") Integer level,
			@RequestParam("limit") Integer limit) {
		Map<String, Object> data = new HashMap<String, Object>();
		Integer tagId = 0;
		try {
			tagId = tagService.getTagIdByCode(code);
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", "code不存在");
			data.put("exception", e.getMessage());
			return data;
		}
		Map<String, Object> res = tagService.getSkuListByParentIdForWarning(
				tagId, level, limit);

		if ((Integer) res.get("code") != 200) {
			return res;
		}
		data = res;
		return data;
	}

	/**
	 * 按父标签及深度获取标签树及商品列表（定时上下架）
	 * 
	 * @author :
	 * @since :2016年4月18日 下午2:23:48
	 * @param tagId
	 * @param level
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tags/TimeSku", method = RequestMethod.GET)
	public Map<String, Object> listByTagIdAndLevelAndLimit(
			@RequestParam("code") String code,
			@RequestParam("level") Integer level,
			@RequestParam("limit") Integer limit) {
		Map<String, Object> data = new HashMap<String, Object>();
		Integer tagId = 0;
		try {
			tagId = tagService.getTagIdByCode(code);
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", "code不存在");
			data.put("exception", e.getMessage());
			return data;
		}
		Map<String, Object> res = tagService.getSkuListByParentIdForTimeSku(
				tagId, level, limit);

		if ((Integer) res.get("code") != 200) {
			return res;
		}
		data = res;
		return data;
	}

	/**
	 * 按父标签及深度获取标签树及商品列表（定时上下架）
	 * 
	 * @author :
	 * @since :2016年4月18日 下午2:23:48
	 * @param tagId
	 * @param level
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/TimeSku/skus/{TagId}", method = RequestMethod.GET)
	public Map<String, Object> TagListByParentId(
			@PathVariable("TagId") Integer TagId, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tagId", TagId);
			param.put("listType", "TimeSku");
			data.put("data", warningService.selectTagLists(param));
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", "code不存在");
			data.put("exception", e.getMessage());
			return data;
		}
		return data;
	}
	
	 /** 
	 * 按父标签及深度获取标签树及商品列表（预警）
	 * @author :
	 * @since :2016年4月18日 下午2:23:48
	 * @param tagId
	 * @param level
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/Warning/skus/{TagId}", method = RequestMethod.GET)
	public Map<String, Object> TagListByParentIdForWarning(
			@PathVariable("TagId") Integer TagId, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tagId", TagId);
			param.put("listType", "warning");
			data.put("data", warningService.selectTagLists(param));
		} catch (Exception e) {
			data.put("code", 400);
			data.put("message", "code不存在");
			data.put("exception", e.getMessage());
			return data;
		}
		return data;
	}

	public SkuShelvesSchedule mapToSSS(Map<String, Object> map) {
		SkuShelvesSchedule sss = new SkuShelvesSchedule();
		try {		
			if (map.get("limit") != null && !"".equals(map.get("limit"))) {
				sss.setLimit(Integer.parseInt(map.get("limit").toString()));
			}
			if (map.get("offset") != null && !"".equals(map.get("offset"))) {
				sss.setOffset(Integer.parseInt(map.get("offset").toString()));
			}
		} catch (Exception e) {
			logger.error("获取上下架，类型那个转换异常捕获：",e);			
		}
		return sss;
	}
	
}