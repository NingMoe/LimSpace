package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.RushBuyAppoint;

/**
 * 表t_resh_buy_appoint(秒杀活动预约)的数据访问接口
 * @author rainyhao
 * @since 2016-3-30 下午4:10:20
 */
public interface RushBuyAppointDao {
	/**
     * 按主键查
     * @authro rainyhao
     * @since 2016-3-30 下午2:48:42
     * @param id 主键值
     * @return
     */
    RushBuyAppoint getById(Integer id);
    
    /**
     * 按特定的条件查一行
     * @authro rainyhao
     * @since 2016-3-31 下午1:40:13
     * @param entity 查询条件
     * @return
     */
    RushBuyAppoint selectForObject(RushBuyAppoint entity);
}