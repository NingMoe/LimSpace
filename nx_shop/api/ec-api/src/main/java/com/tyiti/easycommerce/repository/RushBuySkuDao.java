package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.RushBuySku;

public interface RushBuySkuDao {

    /**
     * 按主键查秒杀活动商品详情
     * @authro rainyhao
     * @since 2016-4-1 下午3:49:02
     * @param id 主键值
     * @return
     */
    RushBuySku getById(Integer id);
    
    /**
     * 按特唯一查询条件查一行秒杀商品记录
     * @authro rainyhao
     * @since 2016-4-1 下午4:19:06
     * @param entity 秒杀商品的查询条件 
     * @return
     */
    RushBuySku selectForObject(RushBuySku entity);
    
    /**
     * 按特定查询条件查一个集合
     * @authro rainyhao
     * @since 2016-4-5 上午10:31:11
     * @param entity 查询条件值
     * @return
     */
    List<RushBuySku> selectForList(RushBuySku entity);
    
    /**
     * 秒杀活动下单后减库存
     * @authro rainyhao
     * @since 2016-4-1 下午4:37:37
     * @param rushBuyId 所参与的秒杀活动
     * @param skuId 此次活动秒的商品
     * @param toDecrease 秒了多少个(减多少库存)
     * @return
     */
    int updateLeftSkuAsDecrease(@Param("rushBuyId") Integer rushBuyId, @Param("skuId") Integer skuId, @Param("toDecrease") Integer toDecrease);
    
    /**
     * 增加库存, 用于处理秒杀订单过期未支付的库存返还
     * @authro rainyhao
     * @since 2016-4-1 下午4:45:36
     * @param rushBuyId 所参与的秒杀活动
     * @param skuId 未支付的商品
     * @param toIncrease 下单时秒了多少个(要返还的个数)
     * @return
     */
    int updateLeftSkuAsIncrease(@Param("rushBuyId") Integer rushBuyId, @Param("skuId") Integer skuId, @Param("toIncrease") Integer toIncrease);
}