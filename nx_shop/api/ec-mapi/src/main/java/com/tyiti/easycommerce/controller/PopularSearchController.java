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
import com.tyiti.easycommerce.entity.PopularSearch;
import com.tyiti.easycommerce.service.PopularSearchService;

@Controller
public class PopularSearchController {
	@Autowired
	private PopularSearchService popularSearchService;

	@RequestMapping(value = "/popularsearch", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAll() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> a= popularSearchService.getAll();
		data.put("data", a);
		return data;
	}
	
	@RequestMapping(value = "/popularsearch/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer id) {
		Map<String, Object> data = new HashMap<String, Object>();
		PopularSearch popularSearch = popularSearchService.getById(id);
		data.put("data", popularSearch);
		return data;
	}
	
	@RequestMapping(value = "/popularsearch", method = RequestMethod.POST , headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> add(@RequestBody PopularSearch popularSearch) {
		Map<String, Object> data = new HashMap<String, Object>();
		if(popularSearchService.add(popularSearch)>0){
			data.put("code", "200");
			data.put("message", "添加成功");
		}
		else{
			data.put("code", "400");
			data.put("message", "添加失败,已存在相同搜索词");
		}
		return data;
	}
	
	@RequestMapping(value = "/popularsearch/{id}", method = RequestMethod.DELETE )
	@ResponseBody
	public Map<String, Object> delete(@PathVariable("id") Integer id) {
		Map<String, Object> data = new HashMap<String, Object>();
		if(popularSearchService.delete(id)==1){
			data.put("code", "200");
			data.put("message", "删除成功");
		}
		else{
			data.put("code", "400");
			data.put("message", "删除失败，请先停用再删除");
		}
		return data;
	}
	
	@RequestMapping(value = "/popularsearch/status/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> updateStatus(@PathVariable("id") Integer id) {
		Map<String, Object> data = new HashMap<String, Object>();
		if(popularSearchService.updateStatus(id)==1){
			data.put("code", "200");
			data.put("message", "状态更新成功");
		}
		else{
			data.put("code", "400");
			data.put("message", "状态更新失败");
		}
		return data;
	}
	@RequestMapping(value = "/popularsearch/rank", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> updateRank(@RequestBody PopularSearch popularSearch) {
		Map<String, Object> data = new HashMap<String, Object>();
		if(popularSearchService.updateRank(popularSearch)==1){
			data.put("code", "200");
			data.put("message", "排序更新成功");
		}
		else{
			data.put("code", "400");
			data.put("message", "排序更新失败");
		}
		return data;
	}

}
