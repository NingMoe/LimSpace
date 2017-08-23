package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.User;

public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	User selectByCustId(Integer custId);
	

	/**
	 * @author wyy 2016/07/15
	 * @description 获取用户列表
	 * @param user
	 * @return
	 */
	List<Map<String, Object>> selectUserList(Map<String, Object> param);
    
	List<User> getUserList(Map<String, Object> param);
	
	/**
	 * @author wyy 2016/07/15
	 * @description 根据条件查询总条数
	 * @param user
	 * @return
	 */
	long selectUserCount(Map<String, Object> param);
	
}