package com.tyiti.easycommerce.repository;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Coupon;

/**
 * @author:wangqi
 * @date:2016-3-30 下午6:25:37
 * @description:优惠券
 */
public interface CouponDao {
	 
 



	/**
	 * 根据记录id和用户id获取符合条件的优惠券信息
	 * 
	 * @param couponRecordId
	 * @return
	 */
	Coupon selCouponByRecordId(@Param("couponRecordId") Integer couponRecordId,
			@Param("custId") Integer custId);

	int deleteByPrimaryKey(Integer id);

	int insert(Coupon record);

	int insertSelective(Coupon record);

	Coupon selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Coupon record);

	int updateByPrimaryKey(Coupon record);

}