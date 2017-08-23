package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Promotion;

public interface PromotionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Promotion record);

    int insertSelective(Promotion record);

    Promotion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Promotion record);

    int updateByPrimaryKey(Promotion record);

	List<Promotion> selectPromotionList(Map<String, Object> param);

	long selectPromotionCount(Map<String, Object> param);
}