package com.tyiti.easycommerce.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tyiti.easycommerce.entity.RushBuy;
import com.tyiti.easycommerce.entity.RushBuySku;
import com.tyiti.easycommerce.service.RushBuyService;

/**
 * 秒杀活动相关的请求处理接口
 * @author rainyhao
 * @since 2016-3-30 下午6:30:43
 */
@RestController
public class RushBuyController {
	
	// 秒杀活动信息相关的业务处理程序
	@Autowired
	private RushBuyService rushBuyService;

	/**
	 * 获取秒杀活动列表
	 * 加载当前时间大于活动结束时间1小时的活动
	 * @authro rainyhao
	 * @since 2016-3-30 下午6:33:36
	 * @return
	 */
	@RequestMapping(value = "/rushBuy", method = RequestMethod.GET)
	public Map<String, Object> rushBuyList() {
		Map<String, Object> data = new HashMap<String, Object>();
		// 加载可见的活动信息
		List<RushBuy> lstRushBuy = rushBuyService.getVisibleList();
		data.put("code", 200);
		data.put("message", "OK");
		data.put("data", lstRushBuy);
		data.put("currentTime", new Date());
		return data;
	}
	
	/**
	 * 获取指定秒杀活动中的所有商品信息
	 * @authro rainyhao
	 * @since 2016-4-5 上午10:35:11
	 * @param rushBuyId 指定的秒杀活动
	 * @return
	 */
	@RequestMapping(value = "/rushBuy/sku", method = RequestMethod.GET)
	public Map<String, Object> rushBuySkuList(@RequestParam("rushBuyId") Integer rushBuyId) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 执行加载
		List<RushBuySku> lstSku = rushBuyService.getSkuListByRushBuy(rushBuyId);
		if (0 == lstSku.size()) {
			data.put("code", 404);
			data.put("message", "活动不存在或活动商品已抢光");
		} else {
			data.put("code", 200);
			data.put("message", "OK");
			data.put("data", lstSku);
			data.put("currentTime", new Date());
		}
		return data;
	}

}
