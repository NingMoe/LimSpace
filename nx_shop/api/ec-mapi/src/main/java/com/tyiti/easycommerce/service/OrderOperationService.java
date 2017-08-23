package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.OrderCancellation;

public interface OrderOperationService{
	/***
	  * <p>功能描述：。</p>	
	  * @param orderCancellation
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月8日 下午6:21:07。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 */
	Map<String, Object> applyToCancel(OrderCancellation orderCancellation);
	
	Map<String, Object> cancel(HttpSession session,Integer id);
	Map<String, Object> delete(HttpSession session, Integer id);
	Map<String, Object> sign(HttpSession session,Integer id);
	
	Map<String, Object> applyToReturn(HttpSession session,OrderCancellation orderCancellation);
	
	List<OrderCancellation> getReturnList(HttpSession session,Integer offset,
			Integer limit);
	
	int getReturnListCount(HttpSession session);
	
	OrderCancellation getReturnDetail(HttpSession session,Integer id);

}