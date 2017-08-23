package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.tyiti.easycommerce.entity.Promotion;
import com.tyiti.easycommerce.entity.PromotionPrize;
import com.tyiti.easycommerce.service.PromotionService;

@Scope("prototype")
@Controller
@RequestMapping("/promotions")
public class PromotionController {

	@Autowired
	private PromotionService promotionService ; 
	
	/**
	* @Title: addPromotion
	* @Description: TODO(添加抽奖活动)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addPromotion(@RequestBody Promotion promotion){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			promotionService.addPromotion(promotion);
			map.put("code", 200);
			map.put("message", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "添加失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	
	/**
	* @Title: editPromotion
	* @Description: TODO(修改抽奖活动)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="",method = RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> editPromotion(@RequestBody Promotion promotion){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			promotionService.editPromotion(promotion);
			map.put("code", 200);
			map.put("message", "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "修改失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	
	/**
	* @Title: editPromotion
	* @Description: TODO(修改抽奖活动启用不启用)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> editStatus(@PathVariable("id") Integer id ){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			promotionService.editStatus(id);
			map.put("code", 200);
			map.put("message", "状态修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "状态修改失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getPromotion(@PathVariable("id") Integer id ){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			Promotion promotion = 	promotionService.getPromotionById(id);
			
			map.put("code", 200);
			map.put("data", promotion);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	/**
	* @Title: delPromotion
	* @Description: TODO(删除活动)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String,Object> delPromotion(@PathVariable("id") Integer id ){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			promotionService.delPromotion(id);
			map.put("code", 200);
			map.put("message", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "删除失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	
	
	@RequestMapping(value="",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> promotionList(@RequestParam  Map<String,Object> param){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			SearchResult<Promotion> result =  promotionService.selectPromotionList(param);
			map.put("code", 200);
			map.put("data", result);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询列表失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	/**
	* @Title: addPromotionPrize
	* @Description: TODO(奖品添加)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/prize",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addPromotionPrize(@RequestBody PromotionPrize promotionPrize){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			promotionService.addPromotionPrize(promotionPrize);
			map.put("code", 200);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "添加失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	
	
	@RequestMapping(value="/prize/{id}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> addPromotionPrize(@PathVariable Integer id){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			PromotionPrize promotionPrize = 	promotionService.getPrizeById(id);
			map.put("code", 200);
			map.put("data", promotionPrize);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "添加失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	/**
	* @Title: editPromotionPrize
	* @Description: TODO(奖品修改)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/prize",method = RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> editPromotionPrize(@RequestBody PromotionPrize promotionPrize){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			promotionService.editPromotionPrize(promotionPrize);
			map.put("code", 200);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "修改失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	
	@RequestMapping(value="prize/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String,Object> delPrize(@PathVariable("id") Integer id ){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			promotionService.delPrize(id);
			map.put("code", 200);
			map.put("message", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "删除失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	/**
	* @Title: promotionPrizeList
	* @Description: TODO(查询奖品列表)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="{promotionId}/prize",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> promotionPrizeList(@PathVariable("promotionId")  Integer promotionId){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			List<PromotionPrize> result = promotionService.selectPromotionPrizeList(promotionId);
			map.put("code", 200);
			map.put("data", result);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询列表失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
	
	@RequestMapping(value="/raffle",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> promotionRaffleList(@RequestParam  Map<String,Object> param){
		Map<String,Object>   map = new HashMap<String, Object>();
		try {
			SearchResult<Map<String,Object>> result =  promotionService.selectPromotionRaffleList(param);
			map.put("code", 200);
			map.put("data", result);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询列表失败");
			map.put("exception", e.getMessage());
		}
		return  map ;
	}
}
