package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.OrderReturn;

public interface OrderReturnDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderReturn record);

    int insertSelective(OrderReturn record);

    OrderReturn selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderReturn record);

    int updateByPrimaryKey(OrderReturn record);

	List<Map<String, Object>> selectList(Map<String, Object> params);

	long selectCount(Map<String, Object> params);

	Map<String, Object> selectReturnDetail(Integer id);
	/***获取退货成功数量***/
	Integer selectReturnCount(@Param("orderId") Integer orderId);
	/***获取购买数量***/
	Integer selectBuyCount(@Param("orderId") Integer orderId);

	/**获取已退数量**/
	Integer selectReturnCountByOrderSkuId(@Param("orderSkuId") Integer orderSkuId);
}