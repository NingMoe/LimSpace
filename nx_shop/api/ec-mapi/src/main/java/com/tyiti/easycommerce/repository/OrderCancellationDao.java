package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.OrderCancellation;

public interface OrderCancellationDao {


	List<Map<String, Object>> selectOrderCancelList(Map<String, Object> param);

	long selectOrderCancelCount(Map<String, Object> param);


	int insertSelective(OrderCancellation orderCancellation);


    int updateByPrimaryKeySelective(OrderCancellation orderCancellation);
	/**
	* @Title: getOrderCancelStatus
	* @Description: TODO(获取批量列表)
	* @return List<Map<String,Object>>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	List<Map<String, Object>> getOrderCancelStatus(Map<String, Object> param);
	 

	Map<String, Object> orderCancelDetail(int id);

	Map<String, Object> getOrderCancelStatusByOrderId(@Param(value = "orderId") Integer orderId);

	int getCancelMaxId();
	
	/**
	 * 修改退货时间
	 * @param id
	 * @return
	 */
	int cancel(@Param(value = "id") Integer id);

	OrderCancellation selectByPrimaryKey(Integer id);
}