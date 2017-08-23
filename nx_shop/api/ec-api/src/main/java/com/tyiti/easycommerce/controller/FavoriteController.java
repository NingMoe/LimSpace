package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.Favorite;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.service.FavoriteService;

@Controller
public class FavoriteController {
	@Autowired
	private FavoriteService favoriteService;

	@RequestMapping(value = "/favorite", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getByUserId(HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Sku> skus = favoriteService.getSkusByUserId(session);
		data.put("data", skus);
		return data;
	}
	
	@RequestMapping(value = "/favorite/{skuId}", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> add(@PathVariable("skuId") Integer skuId,HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		Boolean flag = favoriteService.addOrCancel(skuId, session);
		data.put("data", flag);
		return data;
	}
	/**
	* @Title: add
	* @Description: 从购物车收藏商品
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value = "/favorite", method = RequestMethod.POST, headers = { "Content-type=application/json" })
	@ResponseBody
	public Map<String, Object> add(@RequestBody Favorite  favorite,  HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();
		Boolean flag = favoriteService.addFavoriteFromCart(favorite, session);
		data.put("data", flag);
		return data;
	}
}
