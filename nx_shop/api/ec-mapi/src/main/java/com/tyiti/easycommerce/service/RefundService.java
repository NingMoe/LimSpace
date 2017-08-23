package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;

public interface RefundService {

	/**
	* @Title: getRefundList
	* @Description: TODO(获取列表)
	* @return SearchResult<Map<String,Object>>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	SearchResult<Map<String, Object>> getRefundList(Map<String, Object> param);

	/**
	* @Title: getRefundDetail
	* @Description: TODO(获取详情)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	Map<String, Object> getRefundDetail(Integer id);

	/**
	* @Title: refundMakeSure
	* @Description: TODO(确定退款)
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	void refundMakeSure(int id);

	SearchResult<Map<String, Object>> selectRefundsSkusList(
			Map<String, Object> param);

	Map<String, Object> refundMake(int id);

}
