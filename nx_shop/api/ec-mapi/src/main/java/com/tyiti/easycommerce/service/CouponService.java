package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Coupon;

public interface CouponService {
	/**
	 * 新增优惠券
	 * @param coupon
	 * @return
	 */
	void addCoupon(Coupon coupon);

	/**
	 * 修改优惠券
	 * @param coupon
	 * @return
	 */
	void updateCoupon(Coupon coupon);
	
	/**
	 * 删除优惠券
	 * @param id
	 * @return
	 */
	void deleteCoupon(Integer id);


	/**
	 * 查询单个优惠券
	 * @param id
	 * @return
	 */
	Coupon getCouponById(int id);
	
	/**
	 * 分页查询优惠券列表
	 * @param param
	 * @return
	 */
	SearchResult<Map<String, Object>> queryInfoListByPage(Map<String, Object> param);

	void stopCoupon(Integer id);
}
