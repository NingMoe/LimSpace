package com.tyiti.easycommerce.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.RushBuy;
import com.tyiti.easycommerce.entity.RushBuySku;
import com.tyiti.easycommerce.service.RushBuyService;
import com.tyiti.easycommerce.service.RushBuySkuService;
/**
 * Title:RushBuySkuController.java
 * Description:通用秒杀商品管理
 * @author: xulihui
 * @date: 2016年4月13日 下午5:05:27
 */
@Controller
public class RushBuySkuController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RushBuySkuController.class);
    
    @Autowired
    private  RushBuySkuService rushBuySkuService; 
    @Autowired
    private RushBuyService rushBuyService;
    /**
     * 商品列表
     * @author :xulihui
     * @since :2016年4月14日 下午4:42:31
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/rushBuySku/list",method=RequestMethod.GET)
    public Map<String,Object> getRushBuySkuList(@RequestParam Map<String, Object> param){
    	Map<String, Object> map = new HashMap<String, Object>();
    	SearchResult<Map<String, Object>> rushBuyList = null;
    	
    	try {
			rushBuyList = rushBuySkuService.getRushBuySkuList(param);
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
    
    /**
     * 添加通用秒杀商品
     * @author :xulihui
     * @since :2016年4月14日 下午4:51:18
     * @param rushBuySku
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rushBuySku/add", method = RequestMethod.PUT)
    public Map<String, Object> addRushBuySku(@RequestBody RushBuySku rushBuySku,@RequestParam Map<String, Object> map,HttpServletRequest request, HttpServletResponse response){
    	rushBuySku.setLeftSku(0);
    	rushBuySku.setSoldSku(0);
    	List<RushBuy> rushBuyList = rushBuyService.RushBuyList(rushBuySku.getRushBuyId());
    	Iterator<RushBuy> rushBuyInter = rushBuyList.iterator();

    	long startTime=0;
    	long nowTime = new Date().getTime();;
    	while (rushBuyInter.hasNext()) {
    		RushBuy rushBuy = rushBuyInter.next();
    	    startTime = rushBuy.getStartTime().getTime();
		}
    	if(nowTime>=startTime){
    		map.put("code", "503");
			map.put("message", "该商品正在秒杀或秒杀已结束,不能修改哟!");
    	}else{
    		if(rushBuySkuService.insertRushBuySku(rushBuySku) !=1){
    			map.put("code", "400");
    			map.put("message", "添加失败");
    			logger.error("添加失败");
    		 }else {
    			map.put("code", "200");
    			map.put("message", "OK");
    		 }
    	}
		return map;
    }
}
