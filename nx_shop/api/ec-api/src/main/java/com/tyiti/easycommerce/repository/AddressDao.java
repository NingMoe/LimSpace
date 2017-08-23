package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.Address;

;

public interface AddressDao {
	int add(Address address);

	int delete(@Param("id") Integer id);

	int update(Address address);

	List<Address> getAll(@Param("custId") Integer custId);

	Address getById(@Param("id") Integer id);

	Address getByUserIdOrId(@Param("id") Integer id,
			@Param("custId") Integer custId);

	int clearDefaultAddress(@Param("custId") Integer custId);

	@Select("select * from t_address where id = #{id} and cust_id = #{custId}")
	@ResultMap("BaseResultMap")
	Address getByIdAndUserId(@Param("id") Integer id,
			@Param("custId") Integer custId);
}