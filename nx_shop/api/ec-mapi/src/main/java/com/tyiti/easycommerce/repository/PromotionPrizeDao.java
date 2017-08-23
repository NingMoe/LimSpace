package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.PromotionPrize;

public interface PromotionPrizeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionPrize record);

    int insertSelective(PromotionPrize record);

    PromotionPrize selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionPrize record);

    int updateByPrimaryKey(PromotionPrize record);

	List<PromotionPrize> selectPromotionPrizeList(Integer promotionId);
}