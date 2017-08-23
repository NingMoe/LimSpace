package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.CouponRecord;

public interface CouponRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponRecord record);

    int insertSelective(CouponRecord record);

    CouponRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponRecord record);

    int updateByPrimaryKey(CouponRecord record);
}