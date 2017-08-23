package com.tyiti.easycommerce.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.BillEntity;

public interface BillDao {
	List<BillEntity> findBillList( @Param("skuErpCode")String skuErpCode, @Param("startCloseTime")Date startCloseTime, @Param("endCloseTime")Date endCloseTime,
			@Param("startReturnTime")Date startReturnTime,@Param("endReturnTime")Date endReturnTime);

}