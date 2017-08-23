package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Order;

public interface OrderService {
	 

	/**
	 * 
	* @Title: getOrderList
	* @Description: TODO(查询订单列表)
	* @return SearchResult<Map<String,Object>>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	SearchResult<Map<String, Object>> getOrderList(Map<String, Object> param);

	/**
	 * 
	* @Title: getOrderByIds
	* @Description: TODO(只做制单导出功能)
	* @param @param param
	* @param @return    设定文件
	* @return SearchResult<Map<String,Object>>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	SearchResult<Map<String, Object>> getOrderByIds(Map<String, Object> param);
	/**
	* @Title: getOrderDetail
	* @Description: TODO(获取订单详情)
	* @return Order    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	Map<String, Object> getOrderDetail(Integer id);

	void sendSku(Map<String,Object> param);


	void orderCancel(Integer id);

	void orderMakeSure(Map<String, Object> param);

	 /**
	  * <p>功能描述：。</p>	
	 * @param param 
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月8日 上午9:50:02。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	SearchResult<Map<String, Object>> findKooOrders(Map<String, Object> param);
	
	Boolean logisticsImport(Order order);

	SearchResult<Map<String, Object>> getOrdersSkus(Map<String, Object> param);

	SearchResult<Map<String, Object>> promoteOrdersExport(
			Map<String, Object> param);

	Map<String, Object> toOrderSys(Map<String, Object> param);

	
}
