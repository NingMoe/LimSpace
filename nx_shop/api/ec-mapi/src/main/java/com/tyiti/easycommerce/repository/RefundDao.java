package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Refund;

public interface RefundDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Refund record);

    int insertSelective(Refund record);

    Refund selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Refund record);

    int updateByPrimaryKey(Refund record);

	List<Map<String, Object>> selectRefundList(Map<String, Object> param);

	long selectRefundCount(Map<String, Object> param);

	Integer refundMakeSure(int id);

	Map<String, Object> getRefundDetail(@Param(value = "id") Integer id);
	
	int selectRefundCountByOrderId(@Param(value = "orderId") Integer orderId,@Param(value = "cancellationId") Integer cancellationId);

	Map<String, Object> selectRefundWithAllStatus(int id);
	
	int refundMakeSureByOrderIdOrType(@Param(value = "orderId") Integer orderId,@Param(value = "type") Integer type);

	List<Map<String, Object>> selectRefundsSkusList(Map<String, Object> param);

	long selectRefundsSkusCount(Map<String, Object> param);

}