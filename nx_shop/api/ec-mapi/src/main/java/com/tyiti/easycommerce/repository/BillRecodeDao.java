package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.BillRecode;

public interface BillRecodeDao {

	int addBillRecode(BillRecode billRecode);

	int updateBillRecode(BillRecode billRecode);

	int updatePayStateByPayNo(@Param("payNo") String payNo);

	Map<String, Object> billRecodeByBillIdOrPayNo(
			@Param("billId") String billId, @Param("payNo") String payNo);

	BillRecode getBillRecodeByBillIdOrPayNo(@Param("billId") String billId,
			@Param("payNo") String payNo);

	/**
	 * @author wyy 2016/07/08
	 * @description 通过账单Id及微信支付流水号 修改流水状态
	 * @param payState
	 * @param billId
	 * @param payNo
	 * @return
	 */
	@Select(" update t_bill_record p set p.pay_state=#{payState} where p.bill_id = #{billId} and p.pay_no=#{payNo} ")
	int updatePayState(@Param("payState") String payState,
			@Param("billId") Integer billId, @Param("payNo") Integer payNo);

	@Select(" select id,bill_id,trade_no,pay_no,pay_state,pay_time,create_time,refund_time from t_bill_record where pay_no=#{payNo}")
	List<BillRecode> getBillRecodeByPayNo(@Param("payNo") String payNo);

}
