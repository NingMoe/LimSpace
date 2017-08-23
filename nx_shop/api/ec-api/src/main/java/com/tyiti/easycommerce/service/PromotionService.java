/**  
 * @Title: PromotionService.java
 * @Package com.tyiti.easycommerce.service
 * @Description: TODO
 * @author lmh
 * @date 2016-3-28
 * @version v1.0
 */
package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Promotion;
import com.tyiti.easycommerce.entity.PromotionPrize;
import com.tyiti.easycommerce.entity.PromotionRaffle;


/**
 * @ClassName: PromotionService 
 * @Description: TODO
 * @author lmh
 * @date 2016-3-28 上午10:56:33
 */
public interface PromotionService {

	/**
	 * @Title: saveRaffle
	 * @Description: TODO
	 * @param id
	 * @param Integer userId
	 * @return
	 */
	Map<String, Object> saveRaffle(Integer id, Integer userId);

	/**
	 * @Title: getPromotionPrizeList
	 * @Description: TODO
	 * @param id
	 * @return
	 */
	List<PromotionPrize> getPromotionPrizeList(Integer id);

	/**
	 * @Title: getMyPrizeList
	 * @Description: TODO
	 * @param id
	 * @param Integer userId
	 * @return
	 */
	List<PromotionRaffle> getMyPrizeList(Integer id, Integer userId);

	/**
	 * @Title: getPrizeList
	 * @Description: TODO
	 * @param id
	 * @return
	 */
	List<PromotionRaffle> getPrizeList(Integer id);

	/**
	 * @Title: updateAcceptPrize
	 * @Description: TODO
	 * @param  PromotionRaffle promotionraffle
	 * @return
	 */
	Map<String, Object>  updateAcceptPrize( PromotionRaffle promotionraffle );

	/**
	 * @Title: addPromotion
	 * @Description: TODO
	 * @param promotion
	 * @return
	 */
	Map<String, Object> addPromotion(Promotion promotion);

	/**
	 * @Title: deletePromotion
	 * @Description: TODO
	 * @param id
	 * @return
	 */
	Map<String, Object> deletePromotion(Integer id);

	/**
	 * @Title: addPromotionPrize
	 * @Description: TODO
	 * @param promotionprize
	 * @return
	 */
	Map<String, Object> addPromotionPrize(PromotionPrize promotionprize);

	/**
	 * @Title: deletePromotionPrize
	 * @Description: TODO
	 * @param promotionprize
	 * @return
	 */
	Map<String, Object> deletePromotionPrize(PromotionPrize promotionprize);
	/**
	 * @Title: getRaffleCount
	 * @Description: 根据活动id和用户id获取已经抽奖的次数
	 * @param id
	 * @param newcomer 
	 * @param type 
	 * @param Integer userId
	 * @return
	 */
	PromotionRaffle getRaffleCount(Integer id, Integer userId, Integer type);
	
	/**
	 * @Title: getPromotion
	 * @Description: 查询活动
	 * @param id 活动id
	 * @return
	 */
	Promotion getPromotion(Integer id);

	Integer getIdByCode(String code);
	
	Map<String, Object> lottery(Integer userId);
	
}
