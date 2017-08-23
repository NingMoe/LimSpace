package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tyiti.easycommerce.entity.PromotionPrize;

public interface PromotionPrizeDao {
	
//	@Select("SELECT	t.id,	t.promotion_id,	t.`name`,	t.description,	t.inventory,	t.awarded_amount,	t.virtual_type,	t.virtual_value,	t.virtual_ext1,	t.virtual_ext2,	t.expire_time,	t.expire_seconds,	t.create_time,	t.modify_time,	t.image_url,	IFNULL(t.rate, 0) rate FROM	t_promotion_prize t WHERE  (t.inventory >0 or t.inventory is NULL) and (t.expire_time is NOT null or t.expire_seconds is NOT NULL ) and t.promotion_id = #{promotionId} ORDER BY t.rate ASC")
	@Select("SELECT	t.id,	t.promotion_id as promotionId,	t.`name`,	t.description,	t.inventory,	t.awarded_amount as awardedAmount,	t.virtual_type as virtualType,	t.virtual_value as virtualValue,	t.virtual_ext1 as virtualExt1,	t.virtual_ext2 as virtualExt2,	t.expire_time as expireTime,	t.expire_seconds as expireSeconds,	t.create_time as createTime,	t.modify_time as modifyTime,	t.image_url imageUrl,	IFNULL(t.rate, 0) rate FROM	t_promotion_prize t WHERE  (t.inventory >0 or t.inventory is NULL)  and t.promotion_id = #{promotionId} and t.invalid = 0  ORDER BY t.rate ASC")
	List<PromotionPrize> getPrizeList(@Param("promotionId") Integer promotionId);

	@Update("UPDATE t_promotion_prize SET inventory = IF(inventory = NULL, NULL, inventory - 1), awarded_amount = IFNULL(awarded_amount,0) + 1 WHERE id = #{id}")
	void decInventory(@Param("id") Integer id);
	
	int deleteByPrimaryKey(Integer id);

    int insert(PromotionPrize record);

    int insertSelective(PromotionPrize record);

    PromotionPrize selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionPrize record);

    int updateByPrimaryKey(PromotionPrize record);
}
