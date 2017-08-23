package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.RushBuySku;
/**
 * 
 * Title:RushBuySkuDao.java
 * Description:通用秒杀商品管理
 * @author: xulihui
 * @date: 2016年4月13日 下午5:24:51
 */
public interface RushBuySkuDao {
    /**
     * 查询RushBuySku全表
     * @author :xulihui
     * @since :2016年4月13日 下午5:25:09
     * @param param
     * @return
     */
	List<Map<String, Object>> selectRushBuySkuList(Map<String, Object> param);
    /**
     * 获取表的总数
     * @author :xulihui
     * @since :2016年4月13日 下午5:26:01
     * @return
     */
	long selectRushBuySkuCount();
	/**
	 * 添加商品
	 * @author :xulihui
	 * @since :2016年4月14日 下午5:05:42
	 * @param rushBuySku
	 * @return
	 */
	int insertRushBuySku(RushBuySku rushBuySku);

}
