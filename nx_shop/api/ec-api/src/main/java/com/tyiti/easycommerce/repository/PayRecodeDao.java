package com.tyiti.easycommerce.repository;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.BillRecode;
import com.tyiti.easycommerce.entity.PayRecode;

public interface PayRecodeDao {

	int addPayRecode(PayRecode payRecode);
	
	int insertPayRecode(PayRecode payRecode);
	
	int updatePayRecode(PayRecode payRecode);
	
	int queryOrder(@Param("orderNo") String orderNo, @Param("tradeNo") String tradeNo);
	
	int updateOrder(String no);
	
	int updateKooOrder(String no);
	
	@Select("SELECT COUNT(0) FROM t_pay_record where order_id=#{orderId} and trade_no=#{tradeNo}")
	Integer getCount(@Param("orderId") String orderId,@Param("tradeNo") String tradeNo);
}
