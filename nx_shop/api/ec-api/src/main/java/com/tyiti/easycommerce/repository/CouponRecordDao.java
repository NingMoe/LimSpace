package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.CouponRecord;

public interface CouponRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponRecord record);

    int insertSelective(CouponRecord record);

    CouponRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponRecord record);

    int updateByPrimaryKey(CouponRecord record);

	List<CouponRecord> getByCustId(@Param("custId") Integer custId);

	List<Map<String, Object>> getMyCouponRecordList(Map<String, Object> param);

	long getMyCouponRecordCount(Map<String, Object> param);

	/**
	* @Title: checkIsReceived
	* @Description:  判断用户是否领取过该优惠券
	* @return Integer    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	Integer checkIsReceived(@Param("custId") Integer custId,@Param("couponId") Integer couponId);

	CouponRecord getCouponRecordByCode(String code);

	Double getCouponDiscount(Integer id);

	Integer insertGetId(CouponRecord record);
}