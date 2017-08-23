package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.User;


/**
 * @author:wangqi
 * @date:2016-3-30 下午6:25:14
 * @description:优惠券
 */
public interface CouponService {
	/**
	* @Title: getCouponRecord
	* @Description:  根据sku 数量 计算总金额  获取可用优惠券
	* @return List<CouponRecord>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	List<CouponRecord> getCouponRecord(Integer id, Integer skuId, Integer num);
	/**
	* @Title: getMyCouponRecord
	* @Description: TODO(获取我的优惠券信息)
	* @return List<CouponRecord>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	SearchResult<Map<String, Object>> getMyCouponRecord(User user,
			Map<String, Object> param);
	void recevieCouponRecord(Integer custId, Integer couponId);
	
	CouponRecord selectCouponRecordById(Integer couponRecordId);
	/**
	 * @param integer 
	* @Title: updateCouponInfo
	* @Description: 修改优惠券剩余记录 修改优惠券使用记录
	* @return void    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	void updateCouponInfo(Integer custId, Integer couponRecordId);
	
	void recevieCouponRecordByCode(Integer id, String code);
	
	Double getCouponByRecordId(Integer couponRecordId);
	
	/**
	 * 
	 * @Title: getAllCartCoupon 
	 * @Description: 获得购物车中的所有sku符合条件的代金卷
	 * @param skus
	 * @return  
	 * @return List<CouponRecord>  
	 * @throws
	 * @author hcy
	 * @date 2016年8月8日 下午5:21:25
	 */
	List<CouponRecord> getAllCartCoupon(List<Sku> skus,Integer cusId);
	/** 发送优惠券 返回记录Id
	 * 
	 * @param custId
	 * @param couponId
	 * @return
	 */
	Integer sendCouponRecord(Integer custId, Integer couponId);
}
