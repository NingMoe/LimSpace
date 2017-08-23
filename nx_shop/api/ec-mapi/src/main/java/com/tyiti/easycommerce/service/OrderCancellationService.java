package com.tyiti.easycommerce.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.base.SearchResult;

public interface OrderCancellationService {
	/**
	* @Title: getOrderCancelList
	* @Description: TODO(获取退货订单列表)
	* @return SearchResult<Map<String,Object>>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	SearchResult<Map<String, Object>> getOrderCancelList(
			Map<String, Object> param);
	/**
	* @Title: getOrderCancelDetail
	* @Description: TODO(这获取退货订单详情)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	Map<String, Object> getOrderCancelDetail(int id);
	
	/**
	* @Title: orderCancelMakeSure
	* @Description: TODO(退货确定)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	
	Map<String, Object> orderCancelMakeSure(Map<String, Object> param);
	/**
	* @Title: orderCancelRefuse
	* @Description: TODO(退货拒绝)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	Map<String, Object> orderCancelRefuse(Map<String, Object> param);
	
	/**
	* @Title: orderCancelRefuse
	* @Description: TODO(退货后确定收货)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	Map<String, Object> orderCancelDelivery(Map<String, Object> param);

	Map<String, Object> getPayStatus(Integer id,
			HttpServletResponse response, HttpSession session);
	
}
