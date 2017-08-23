package com.tyiti.easycommerce.repository;


import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.PayRecode;

public interface PayRecodeDao {

	int addPayRecode(PayRecode payRecode);
	
	int updatePayRecode(PayRecode payRecode);
	
	int queryOrder(@Param("orderNo") String orderNo, @Param("tradeNo") String tradeNo);
	
	int updateOrder(String no);
	
	Map<String, Object> payRecodeByOrderIdOrPayNo(@Param("orderId") String orderId,@Param("payNo") String payNo);
	
	/**
	 * @author wyy 2016/07/08
	 * @description 通过订单Id及微信支付流水号修改流水状态
	 * @param payState
	 *        修改后的状态
	 * @param orderId
	 *        叮当Id
	 * @param payNo
	 *        微信支付流水号
	 * @return
	 */
	@Select(" update t_pay_record p set p.pay_state=#{payState} where p.order_id = #{orderId} and p.pay_no=#{payNo} ")
	int updatePayState(@Param("payState") String payState,
			@Param("orderId") Integer orderId,@Param("payNo") String payNo);
	
}
