package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.PromotionRaffle;

public interface PromotionRaffleDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionRaffle record);

    int insertSelective(PromotionRaffle record);

    PromotionRaffle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionRaffle record);

    int updateByPrimaryKey(PromotionRaffle record);


	List<Map<String, Object>> selectPromotionRaffleList(Map<String, Object> param);

	long selectPromotionRaffleCount(Map<String, Object> param);
}