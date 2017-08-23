package com.tyiti.easycommerce.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.VerifyCode;

public interface VerifyCodeDao {

	@Select("select * from t_verify_code where mobile = #{mobile} and DATE_ADD(create_time, INTERVAL 10 MINUTE) >= NOW() order by id desc limit 1")
	@ResultMap(value = "BaseResultMap")
	VerifyCode findLastByMobile(@Param("mobile")String mobile);

    int add(VerifyCode verifyCode);
}
