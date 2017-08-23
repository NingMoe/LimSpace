package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.OrderReturn;
import com.tyiti.easycommerce.service.ReturnService;
import com.tyiti.easycommerce.util.LogUtil;

@Controller
@Scope("prototype")
@RequestMapping("/returns")
public class ReturnController {
	
	@Autowired
	private  ReturnService returnService ; 
	/**
	* @Title: orderReturnList
	* @Description: TODO(查询列表)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="",method =  RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> orderReturnList(@RequestParam Map<String,Object> params){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			SearchResult<Map<String,Object>> result =   returnService.selectListByParmas(params);
			map.put("code", 200);
			map.put("data", result);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		return map ;
	}
	
	@RequestMapping(value="/{id}",method =  RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> orderReturnDetail(@PathVariable Integer id ){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			OrderReturn orderReturn =	returnService.selectReturnDetail(id);
			List<Map<String, Object>> logs  = LogUtil.logs(id ,LogEnum.OperateModel.RETURN.getKey());
			map.put("logs",logs);
			map.put("code", 200);
			map.put("data", orderReturn);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		return map ;
	}
	/**
	* @Title: sure
	* @Description: 同意退货
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/agree/{id}",method =  RequestMethod.PUT)
	@ResponseBody
	public synchronized Map<String,Object> sure(@PathVariable Integer id ){
		Map<String,Object> map  = new HashMap<String, Object>(); 
		try {
			Integer status =3  ; //确认
			map =returnService.updateStatus(id,status);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		//记录操作日志
		if(map.get("code").equals(200)){
			LogUtil.log(id, LogEnum.OperateModel.RETURN.getKey(), LogEnum.Action.QUEDINGTUIHUO.getKey(),
					LogEnum.Action.QUEDINGTUIHUO.getValue(), LogEnum.Source.PLAT.getKey(), 1);
		}else{
			
		}
		return map ;
	}
	/**
	* @Title: refuse
	* @Description:  拒绝退货
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/refuse/{id}",method =  RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> refuse(@PathVariable Integer id ){
		Map<String,Object> map  = new HashMap<String, Object>(); 
		try {
			Integer status =2  ; //拒绝
			map =returnService.updateStatus(id,status);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		//记录操作日志
		if(map.get("code").equals(200)){
			LogUtil.log(id, LogEnum.OperateModel.RETURN.getKey(), LogEnum.Action.JUJUETUIHUO.getKey(),
					LogEnum.Action.JUJUETUIHUO.getValue(), LogEnum.Source.PLAT.getKey(), 1);
		}else{
			
		}
		return map ;
	}
	/**
	* @Title: receive
	* @Description: 收货
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/receive/{id}",method =  RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> receive(@PathVariable Integer id ){
		Map<String,Object> map  = new HashMap<String, Object>(); 
		try {
			Integer status =4  ; //收货
			map =returnService.updateStatus(id,status);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", 400);
			map.put("message", "查询失败");
			map.put("exception", e.getMessage());
		}
		//记录操作日志
		if(map.get("code").equals(200)){
			LogUtil.log(id, LogEnum.OperateModel.RETURN.getKey(), LogEnum.Action.SHOUHUO.getKey(),
					LogEnum.Action.SHOUHUO.getValue(), LogEnum.Source.PLAT.getKey(), 1);
		}else{
		}
		return map ;
	}
	
	/**
	* @Title: returncount
	* @Description: 查询退货单正在退货数量
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/count/{orderSkuId}",method =  RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> returncount(@PathVariable Integer orderSkuId ){
		Map<String,Object> map  = new HashMap<String, Object>(); 
		Integer returnCount = returnService.selectReturnCountByOrderSkuId(orderSkuId); 
		map.put("code", 200);
		map.put("data", returnCount);
		return map ;
	}
}
