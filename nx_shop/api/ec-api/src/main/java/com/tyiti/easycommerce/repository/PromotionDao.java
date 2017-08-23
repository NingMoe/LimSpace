package com.tyiti.easycommerce.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tyiti.easycommerce.entity.Promotion;

public interface PromotionDao {
	@Select("SELECT id,name,rate,description,expire_time expireTime,times FROM t_promotion WHERE id = #{id} and invalid = 0 ")
	Promotion getById(@Param("id") Integer id);

	int deleteByPrimaryKey(Integer id);

    int insert(Promotion record);

    int insertSelective(Promotion record);

    Promotion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Promotion record);

    int updateByPrimaryKey(Promotion record);

	Integer getIdByCode(String code);
	
	Integer getOrderCount(Map<String, Object> map);
	
	Integer getPromotionPrizeCount(Integer promotionId);

}
