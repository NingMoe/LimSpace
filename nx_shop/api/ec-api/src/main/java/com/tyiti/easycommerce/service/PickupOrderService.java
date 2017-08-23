package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.PickupOrder;

public interface PickupOrderService {
	Integer selectCodeNum(String code);
	
	PickupOrder selectPickupOrderByCode(String code);

	PickupOrder selectByOrderId(Integer orderId);
	
	List<PickupOrder> selectByPickupPointId(Integer pickupPointId);
	
	
	List<PickupOrder> findbyStatus(PickupOrder pickupOrder);
	
	SearchResult<Map<String, Object>> pickupOrderLists(Map<String, Object> param);
	
	PickupOrder getOrderDetail(Integer id,Integer sessionPickUpId);

	PickupOrder selectPickupOrderById(Integer id);
	/**
	* @Title: getLastPickupOrderByCustId
	* @Description: 获取用户最后一条自提订单
	* @return PickupOrder    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	PickupOrder getLastPickupOrderByCustId(Integer id);
	/**
	 * 
	 * @Title: updateStus 
	 * @Description: 更新订单状态
	 * @param status  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年7月6日 上午10:02:00
	 */
	void updateStus(PickupOrder pickupOrder);
	
	/**
	 * 
	 * @Title: changeStatus 
	 * @Description: 修改status状态 pickorder order orderCanllication
	 * @param status  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年7月11日 下午3:54:54
	 */
	void changeStatus(Integer id,Integer status,String code)  throws Exception;
	

}
