package com.tyiti.easycommerce.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.tyiti.easycommerce.entity.User;

public interface UserDao {

	User findByMobileAndPassword(@Param("mobile")String mobile, @Param("password")String password);

	User findByMobileAndIdCard(@Param("mobile")String mobile, @Param("idCard")String idCard);

	User findByOpenId(@Param("openId") String openId);
	
	User findByThirdParty(@Param("thirdPartyType") Integer thirdPartyType, @Param("thirdPartyId") String thirdPartyId);

	User findByMobile(@Param("mobile") String mobile);

	User getById(@Param("id")Integer id);

	User getByIdAndPassword(@Param("id")Integer id, @Param("password")String password);

	int register(User user);

	@Update("update t_user set password = #{newPassword} where invalid = 0 and id = #{id}")
	int updatePassword(User user);
	
	@Update("update t_user set password = #{newPassword} where invalid = 0 and mobile = #{mobile}")
	int updatePasswordByMobile(User user);

	@Update("update t_user set pay_password = #{payPassword} where id = #{id}")
	int updatePayPassword(@Param("id")Integer id, @Param("payPassword")String password);

	@Update("UPDATE t_user SET open_id = #{openId} WHERE id = #{id}")
	int updateOpenId(User user);
	
	@Update("UPDATE t_user SET third_party_id = #{thirdPartyId} WHERE id = #{id}")
	int updateThirdPartyId(User user);

	@Insert("INSERT INTO t_user (third_party_type, third_party_id, mobile) VALUES (2, #{thirdPartyId}, #{mobile})")
	@Options(useGeneratedKeys=true)
	int addConnectedUser(User user);
	
	@Update("update t_user set real_name = #{user.realName} , id_card = #{user.idCard}  where invalid = 0 and id = #{user.id}")
	int updateUserRealNameAndIdcard(@Param("user") User user);
	
	/**
	 * @author wyy 2016/07/15
	 * @description 根据实体修改数据
	 * @param user
	 * @return
	 */
	Integer updateByPrimaryKeySelective(User user);
}
