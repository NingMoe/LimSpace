package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Category;
import com.tyiti.easycommerce.service.CategoryService;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(Category category,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();

		category = categoryService.addCategory(category);
		data.put("data", category);
		return data;
	}

	@RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> get(@PathVariable("id") int id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		Category category = categoryService.getById(id);
		data.put("data", category);
		return data;
	}

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAll(HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Category> categories = categoryService.getAll();
		data.put("data", categories);
		return data;
	}
}
