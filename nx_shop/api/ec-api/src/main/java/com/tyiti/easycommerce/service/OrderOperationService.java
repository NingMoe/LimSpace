package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.OrderCancellation;
import com.tyiti.easycommerce.entity.OrderReturn;

public interface OrderOperationService {
	Map<String, Object> applyToCancel(HttpSession session,
			OrderCancellation orderCancellation);

	Map<String, Object> cancel(HttpSession session, Integer id);
 
 
	Map<String, Object> delete(HttpSession session, Integer id);

	Map<String, Object> sign(HttpSession session, Integer id);

	Map<String, Object> applyToReturn(HttpSession session,
			OrderCancellation orderCancellation);

	List<OrderCancellation> getReturnList(HttpSession session, Integer offset,
			Integer limit);

	int getReturnListCount(HttpSession session);

	OrderCancellation getReturnDetail(HttpSession session, Integer id);

	/**
	 * @author wyy 2016/07/08
	 * @description 退货提交图片
	 * @param session
	 * @param imgUrl
	 * @return
	 */
	Map<String, Object> returnImg(HttpSession session, String mediaId);
	
	OrderCancellation selectorderOperationByOrderId(Integer orderId);

	void updateByprimary(OrderCancellation orderCancellation);

	Map<String, Object> orderReturn(HttpSession session, OrderReturn orderReturn);

	SearchResult<Map<String, Object>> selectReturnsListByParmas(Map<String, Object> params);

	Map<String, Object> cancelReturns(Integer custId, Integer id);
}