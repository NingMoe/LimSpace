package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.CouponRecord;

public interface CouponRecordService {
	/**
	 * 添加一条优惠券记录
	 * @param couponRecord
	 * @return
	 */
	int addOneCouponRecord(CouponRecord couponRecord);
	/**
	 * 修改优惠券记录使用状态
	 * 
	 * @param id
	 * @return
	 */
	boolean updateUseStatus(int id);
	/**
	 * 通过手机号给用户发放一张优惠券
	 * @param cid
	 * @param mobile
	 * @return
	 */
	int sendOneCoup(Integer cid, String mobile);
	/**
	 * 根据id查询单条记录
	 * @param id
	 * @return
	 */
	Map<String, Object> queryById(Integer id);
	/**
	 * 用户优惠券分页查询
	 * @param param
	 * @return
	 */
	SearchResult<Map<String, Object>> queryInfoListByPage(
			Map<String, Object> param);
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	boolean removeById(Integer id);
	/**
	 * 根据id批量移除记录
	 * @param ids
	 * @return
	 */
	boolean removeById(String ids);
	/**
	 * 修改优惠券记录信息
	 * @param couponRecord
	 * @return
	 */
	int updateCouponRecord(CouponRecord couponRecord);
	/**
	 * 根据id查询优惠券详细记录
	 * @param id
	 * @return
	 */
	Map<String, Object> queryDetailById(Integer id);
}
