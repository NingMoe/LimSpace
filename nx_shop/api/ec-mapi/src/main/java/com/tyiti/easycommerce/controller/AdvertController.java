package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Advert;
import com.tyiti.easycommerce.service.AdvertService;

@Controller
@RequestMapping("/adverts")
public class AdvertController {

	@Autowired
	private AdvertService advertService ; 
	
	/**
	* @Title: Advert
	* @Description: 查询列表
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  Advert (){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Advert> advertList = advertService.selectAdverts();
		try {
        	map.put("code", "200");
        	map.put("data",advertList);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "添加失败");
            map.put("exception", e.getMessage());
		}
		return map;
	}
	/**
	* @Title: addAdvert
	* @Description: 添加
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addAdvert (@RequestBody Advert advert ){
		Map<String,Object> map = new HashMap<String, Object>();
		advertService.addAdvert(advert);
		try {
        	map.put("code", "200");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "添加失败");
            map.put("exception", e.getMessage());
		}
		return map;
	}
	/**
	* @Title: editAdvert
	* @Description: 修改
	*  @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="",method=RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> editAdvert (@RequestBody Advert advert ){
		Map<String,Object> map = new HashMap<String, Object>();
		advertService.editAdvert(advert);
		try {
        	map.put("code", "200");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "修改失败");
            map.put("exception", e.getMessage());
		}
		return map;
	}
	
	/**
	* @Title: delAdvert
	* @Description: 删除
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String,Object> delAdvert (@PathVariable("id") Integer id ){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			advertService.delAdvert(id);
        	map.put("code", "200");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "删除失败");
            map.put("exception", e.getMessage());
		}
		return map;
	}
	
	/**
	* @Title: Advert
	* @Description: 查询
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> Advert (@PathVariable("id") Integer id ){
		Map<String,Object> map = new HashMap<String, Object>();
		Advert advert = advertService.selectAdvert(id);
		try {
        	map.put("code", "200");
        	map.put("data", advert);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "查询失败");
            map.put("exception", e.getMessage());
		}
		return map;
	}
}
