package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.CategoryTemplate;
import com.tyiti.easycommerce.service.CategoryTemplateService;

@Scope("prototype")
@Controller
public class CategoryTemplateController {

	@Autowired
	private CategoryTemplateService categoryTemplateService ;
	/**
	* @Title: add
	* @Description: TODO(模板添加)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/category/templates",method=RequestMethod.POST,headers  = { "Content-type=application/json" } )
	@ResponseBody
	public Map<String,Object> add(@RequestBody List<CategoryTemplate> categoryTemplateList){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			categoryTemplateService.insertTemplate(categoryTemplateList);
			map.put("code", "200");
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
 			System.out.println(e.getMessage());
			map.put("message", e.getMessage());
		}
		return map ;
	}
	/**
	* @Title: templates
	* @Description: TODO(模板查询)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/category/templates/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> templates(@PathVariable int id){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			CategoryTemplate categoryTemplate = categoryTemplateService.selectTemplate(id);
			map.put("data", categoryTemplate);
			map.put("code", "200");
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", e.getMessage());
		}
		return map ;
	}
	/**
	* @Title: edit
	* @Description: TODO(模板修改)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
//	@RequestMapping(value="/category/templates",method=RequestMethod.PUT)
//	public Map<String,Object> edit(@RequestBody CategoryTemplate categoryTemplate){
//		Map<String,Object> map = new HashMap<String,Object>();
//		try {
//			categoryTemplateService.updateTemplate(categoryTemplate);
//			map.put("code", "200");
//			map.put("message", "ok");
//		} catch (Exception e) {
//			// TODO: handle exception
//			map.put("code", "400");
//			map.put("message", e.getMessage());
//		}
//		return map ;
//	}
	/**
	* @Title: del
	* @Description: TODO(模板删除)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/category/templates",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String,Object> del(@Param("categoryId") int categoryId ){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			categoryTemplateService.deleteTemplate(categoryId);
			map.put("code", "200");
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", e.getMessage());
		}
		return map ;
	}
	/**
	* @Title: templateByParentId
	* @Description: TODO(根据分类id 获取模板列表)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/category/templates/list",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> templateByParentId(@Param("categoryId") int categoryId ){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			List<CategoryTemplate> categoryTemplateList =  categoryTemplateService.getTemplateList(categoryId);
			map.put("data", categoryTemplateList);
			map.put("code", "200");
			map.put("message", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", e.getMessage());
		}
		return map ;
	}
}
