package com.tyiti.easycommerce.service;

import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.RushBuySku;
/**
 * 
 * Title:RushBuySkuService.java
 * Description:通用秒杀商品管理
 * @author: xulihui
 * @date: 2016年4月13日 下午4:25:00
 */
public interface RushBuySkuService {
    /**
     * 商品列表
     * @author :xulihui
     * @since :2016年4月13日 下午4:03:07
     * @param param
     * @return
     */
	SearchResult<Map<String, Object>> getRushBuySkuList(Map<String, Object> param);
    /**
     * 添加秒杀商品
     * @author :xulihui
     * @since :2016年4月14日 下午4:53:53
     * @param rushBuySku
     * @return
     */
	int insertRushBuySku(RushBuySku rushBuySku);

}
