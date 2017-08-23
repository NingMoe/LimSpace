package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.OrderReturn;

public interface ReturnService {

	SearchResult<Map<String, Object>> selectListByParmas(Map<String, Object> params);

	OrderReturn selectReturnDetail(Integer id);

	Map<String, Object> updateStatus(Integer id, Integer status);

	Integer selectReturnCountByOrderSkuId(Integer orderId);

}
