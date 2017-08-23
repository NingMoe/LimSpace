package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.entity.HistorySearch;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.HistorySearchService;
import com.tyiti.easycommerce.service.PickupOrderService;
import com.tyiti.easycommerce.service.PickupPointService;
import com.tyiti.easycommerce.service.SkuService;

@Controller
public class SkuController {
	@Autowired
	private SkuService skuService;
	
	@Autowired
	private HistorySearchService historySearchService;
	
	@Autowired
	private PickupOrderService pickupOrderService ; 
	
	@Autowired
	private PickupPointService  pickupPointService ; 
	@Autowired
	ActivityService activityService;
	/**
	 * 商品详情
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/sku/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getById(@PathVariable("id") Integer id,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> res = skuService.findDetailById(session,id);
		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}
		data = res;
		if((User)session.getAttribute(Constants.USER)!=null){
			PickupOrder pickupOrder = pickupOrderService.getLastPickupOrderByCustId(((User)session.getAttribute(Constants.USER)).getId());
			if(pickupOrder !=null){
				PickupPoint pickupPoint = pickupPointService.selectPickupPointById(pickupOrder.getPickupPointId());
				if(pickupPoint.getStatus()==1){
					data.put("defaultPoint", pickupPoint);
				}else{
					data.put("defaultPoint", "");
				}
				
			}else{
				data.put("defaultPoint", "");
			}
		}else{
			data.put("defaultPoint", "");
		}
		activityService.isSkuActivity(id);
		return data;
	}

	@RequestMapping(value = "/stageRate", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> stageRate(HttpServletResponse response,
			HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();

		if (SysConfig.rateConfigMap.get("1") != null) {
			data.put("1", SysConfig.rateConfigMap.get("1"));
		}
		if (SysConfig.rateConfigMap.get("3") != null) {
			data.put("3", SysConfig.rateConfigMap.get("3"));
		}
		if (SysConfig.rateConfigMap.get("6") != null) {
			data.put("6", SysConfig.rateConfigMap.get("6"));
		}
		if (SysConfig.rateConfigMap.get("9") != null) {
			data.put("9", SysConfig.rateConfigMap.get("9"));
		}
		if (SysConfig.rateConfigMap.get("12") != null) {
			data.put("12", SysConfig.rateConfigMap.get("12"));
		}
		if (SysConfig.rateConfigMap.get("24") != null) {
			data.put("24", SysConfig.rateConfigMap.get("24"));
		}
		return data;
	}
	/**
	 * 根据搜索的内容返回产品内容
	 * 
	 * @param id
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/skus/querytext={querytext}&sortfield={sortfield}&sorttype={sorttype}&isfirstquery={isfirstquery}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSkusByQueryText(
			@PathVariable("querytext") String querytext,
			@PathVariable("sortfield") String sortfield,
			@PathVariable("sorttype") String sorttype,
			@PathVariable("isfirstquery") String isfirstquery,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> data = new HashMap<String, Object>();

		//获取当前用户信息
		User user = (User) session.getAttribute(Constants.USER);
		
		//构建要插入的historySearch对象
		HistorySearch historySearch = new HistorySearch();
		historySearch.setQueryText(querytext);
		
		if(isfirstquery.equals("true") ){
			//插入或更新历史搜索表
			if(user==null){
				historySearchService.add_null(historySearch);
				//为配合后台报表，取消更新查询次数字段，改为直接新增
//				if( historySearch.getId() ==null){
//					historySearchService.update_null(historySearch);
//				}
			}
			else{
				historySearch.setUserId(user.getId());
				historySearchService.add_user(historySearch);
				//为配合后台报表，取消更新查询次数字段，改为直接新增
//				if( historySearch.getId() ==null){
//					historySearchService.update_user(historySearch);
//				}
			}
		}


		//返回查询结果
		Map<String, Object> res = skuService.getSkusByQueryText(querytext, sortfield, sorttype);
		Integer code = (Integer) res.get("code");

		if (code != 200) {
			return res;
		}
		data = res;
		return data;
	}
}
