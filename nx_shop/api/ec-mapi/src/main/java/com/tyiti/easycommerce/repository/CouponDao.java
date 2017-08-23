package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Coupon;

public interface CouponDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Coupon coupon);

    int insertSelective(Coupon coupon);

    Coupon selectByPrimaryKey(Integer id);
    

    int updateByPrimaryKeySelective(Coupon coupon);

    int updateByPrimaryKey(Coupon coupon);
    
    /**
     * 分页查询优惠券列表
     * @param param
     * @return
     */
    List<Map<String, Object>> selectInfoListByPage(Map<String, Object> param);

    /**
     * 分页查询结果行数
     * @param param
     * @return
     */
	long selectInfoListCountByPage(Map<String, Object> param);
	

	int selectPrefixNum(@Param("prefix") String prefix);
}