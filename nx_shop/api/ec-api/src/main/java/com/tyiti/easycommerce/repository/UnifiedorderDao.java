package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.Unifiedorder;

public interface UnifiedorderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Unifiedorder record);

    int insertSelective(Unifiedorder record);

    Unifiedorder selectByPrimaryKey(Integer id);

    /**
     * 微信支付主动查询支付结果
     * authro:Black
     * date:2016-08-24
     * @return
     */
    List<Unifiedorder> selectPayOrderSync();

    int updateByPrimaryKeySelective(Unifiedorder record);

    int updateByPrimaryKey(Unifiedorder record);
}
