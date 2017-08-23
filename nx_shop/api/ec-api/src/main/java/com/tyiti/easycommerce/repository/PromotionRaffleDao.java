package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.tyiti.easycommerce.entity.PromotionPrize;
import com.tyiti.easycommerce.entity.PromotionRaffle;

public interface PromotionRaffleDao {
	
	@Insert("INSERT INTO t_promotion_raffle (user_id, prize_id, promotion_id, code,is_receive,expire_time) VALUES (#{userId}, #{prizeId}, #{promotionId}, #{code},#{isReceive},#{expireTime}) ")
	@Options(useGeneratedKeys=true)
	int add(PromotionRaffle promotionRaffle);

	/**
	 * @Title: getPromotionPrizeList
	 * @Description: TODO
	 * @param id
	 */
	//@Select("select * from t_promotion_prize t where CASE WHEN t.expire_time IS NOT NULL THEN t.expire_time>=SYSDATE() WHEN t.expire_time IS NULL THEN expire_seconds is NOT NULL END  and  t.promotion_id = #{id} ")
	@Select("select * from t_promotion_prize t where t.promotion_id = #{id}")
	List<PromotionPrize> getPromotionPrizeList(Integer id);

	/**
	 * @Title: getMyPrizeList
	 * @Description: TODO
	 * @param id
	 * @param userId
	 * @return
	 */
	
	@Select("SELECT t.id, t.user_id userId, t.prize_id prizeId, t.promotion_id promotionId,t.`code`,t.real_name realName,t.mobile,t.is_receive isReceive, t.address,e.image_url imageUrl,e.`name`,t.create_time createTime,t.expire_time expireTime,SYSDATE()>=t.expire_time isExpire  FROM t_promotion_raffle t JOIN t_promotion_prize e ON t.prize_id = e.id WHERE t.promotion_id = #{id} AND t.user_id = #{userId} and e.invalid = 0 order by t.create_time desc")
	List<PromotionRaffle> getMyPrizeList(@Param("id") Integer id,@Param("userId") Integer userId);

	/**
	 * @Title: getPrizeList
	 * @Description: TODO
	 * @param id
	 * @return
	 */
	@Select("SELECT t.id,t.user_id userId,t.prize_id prizeId,t.promotion_id promotionId,t.`code`,o.mobile,e.`name`,e.image_url imageUrl FROM t_promotion_raffle t JOIN t_user o ON t.user_id = o.id JOIN t_promotion_prize e ON t.prize_id = e.id WHERE t.promotion_id = #{id} and e.invalid = 0 ")
	List<PromotionRaffle> getPrizeList(Integer id);

	/**
	 * @Title: updateAcceptPrize
	 * @Description: TODO
	 * @param id
	 */
	@Update("update t_promotion_raffle SET is_receive = 1 where id = #{promotionraffle.id} and user_id = #{promotionraffle.userId}")
	int updateAcceptPrize(@Param("promotionraffle") PromotionRaffle promotionraffle);

	/**
	 * @Title: getLotteryCount
	 * @Description: 查询活动抽奖次数
	 * @param id
	 * @param userId
	 * @param newcomer 
	 * @param type 
	 * @return
	 */
	PromotionRaffle getLotteryCount(@Param("id") Integer id,@Param("userId") Integer userId,@Param("type")  Integer type);

	PromotionRaffle getPrizeCount(@Param("id") Integer id,@Param("userId") Integer userId,@Param("type")  Integer integer);
	
	int deleteByPrimaryKey(Integer id);

    int insert(PromotionRaffle record);

    int insertSelective(PromotionRaffle record);

    PromotionRaffle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromotionRaffle record);

    int updateByPrimaryKey(PromotionRaffle record);
    
}
