package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.RushBuy;

/**
 * 表t_rush_buy(秒杀活动)的数据访问接口
 * @author rainyhao
 * @since 2016-3-30 下午2:36:20
 */
public interface RushBuyDao {
	
    /**
     * 按主键查
     * @authro rainyhao
     * @since 2016-3-30 下午2:48:42
     * @param id 主键值
     * @return
     */
    RushBuy getById(Integer id);
    
    /**
     * 写入一行
     * @authro rainyhao
     * @since 2016-3-30 下午2:52:28
     * @param entity 秒杀活动信息
     */
    void insert(RushBuy entity);
    
    /**
     * 更新活动数据
     * @authro rainyhao
     * @since 2016-3-30 下午2:59:49
     * @param entity
     */
    void update(RushBuy entity);
    
    /**
     * 按条件查询一个list
     * @authro rainyhao
     * @since 2016-3-30 下午6:37:57
     * @param entity 查询条件
     * @return
     */
    List<RushBuy> selectForList(RushBuy entity);
}