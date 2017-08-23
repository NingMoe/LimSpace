package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Promotion;
import com.tyiti.easycommerce.entity.PromotionPrize;

public interface PromotionService {

	void addPromotion(Promotion promotion);

	void editPromotion(Promotion promotion);

	void editStatus(Integer id);

	void delPromotion(Integer id);

	SearchResult<Promotion> selectPromotionList(Map<String, Object> param);

	void addPromotionPrize(PromotionPrize promotionPrize);

	void editPromotionPrize(PromotionPrize promotionPrize);


	List<PromotionPrize> selectPromotionPrizeList(Integer promotionId);

	SearchResult<Map<String,Object>> selectPromotionRaffleList(
			Map<String, Object> param);

	Promotion getPromotionById(Integer id);

	PromotionPrize getPrizeById(Integer id);

	void delPrize(Integer id);

}
