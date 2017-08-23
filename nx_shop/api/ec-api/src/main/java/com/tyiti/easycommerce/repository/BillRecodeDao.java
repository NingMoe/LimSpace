package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.BillRecode;

public interface BillRecodeDao {

	Integer addBillRecode(BillRecode billRecode);

	Integer insertBillRecode(BillRecode billRecode);

	Integer updateBillRecode(BillRecode billRecode);

	Integer updatePayStateByPayNo(String payNo);

	Integer getCount(BillRecode billRecode);
}
