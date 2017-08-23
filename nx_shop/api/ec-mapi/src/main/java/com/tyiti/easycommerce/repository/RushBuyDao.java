package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.RushBuy;

/**
 * 通用秒杀 Title:RushBuyDao.java Description:
 * 
 * @author: xulihui
 * @date: 2016年4月11日 下午2:06:26
 */
public interface RushBuyDao {

	/**
	 * 刪除秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午4:08:06
	 * @param id
	 * @return
	 */
	int delRushBuy(int id);

	/**
	 * 插入秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午4:21:36
	 * @param rushBuy
	 * @return
	 */
	int insertRushBuy(RushBuy rushBuy);

	/**
	 * 修改秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午4:40:57
	 * @param rushBuy
	 * @return
	 */
	int updateRushBuy(RushBuy rushBuy);

	/**
	 * 查询秒杀
	 * 
	 * @author :xulihui
	 * @since :2016年4月11日 下午5:06:51
	 * @param id
	 * @return
	 */
	List<RushBuy> selectRushBuyList(Integer id);

	/**
	 * 获取RushBuy的列表
	 * 
	 * @author :xulihui
	 * @since :2016年4月12日 下午4:04:38
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> selectRushBuyLists(Map<String, Object> map);

	/**
	 * 获取rushBuy的总数
	 * 
	 * @author :xulihui
	 * @since :2016年4月12日 下午4:04:44
	 * @param param
	 * @return
	 */
	long selectRushBuyCount();

}
