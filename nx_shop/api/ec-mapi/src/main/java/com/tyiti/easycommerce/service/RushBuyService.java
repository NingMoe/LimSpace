package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.RushBuy;

/**
 * 通用秒杀
 * Title:RushBuyService.java
 * Description:
 * @author: xulihui
 * @date: 2016年4月11日 下午1:57:19
 */
public interface RushBuyService {
   
    /**
     * 添加秒杀
     * @author :xulihui
     * @since :2016年4月11日 下午4:05:27
     * @param rushBuy
     * @return
     */
	int insertRushBuy(RushBuy rushBuy);

	/**
     * 删除秒杀
     * @author :xulihui
     * @since :2016年4月11日 下午3:44:59
     * @param id
     * @return
     */
	int delRushBuy(int id);

    /**
     * 更新秒杀
     * @author :xulihui
     * @since :2016年4月11日 下午4:04:48
     * @param rushBuy
     * @return
     */
	int updateRushBuy(RushBuy rushBuy);

   
	/**
	 * 查询秒杀
	 * @author :xulihui
	 * @since :2016年4月11日 下午5:05:08
	 * @param id
	 * @return
	 */
	List<RushBuy> RushBuyList(Integer id);
    /**
     * 查询秒杀列表
     * @author :xulihui
     * @since :2016年4月12日 下午3:40:11
     * @param param
     * @return
     */
	SearchResult<Map<String, Object>> getRushBuyList(Map<String, Object> param);

}
