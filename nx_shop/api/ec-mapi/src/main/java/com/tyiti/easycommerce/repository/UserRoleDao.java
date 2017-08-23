package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.UserRole;
import com.tyiti.easycommerce.util.mybatis.BaseMapper;

/**
 * 表user_role的数据访问程序
 * @author rainyhao
 * @since 2016-4-11 下午2:45:30
 */
public interface UserRoleDao extends BaseMapper<UserRole> {
    
	/**
	 * 按用户删除
	 * @authro rainyhao
	 * @since 2016-4-13 上午11:43:37
	 * @param userId 用户id
	 */
	void deleteByUserId(@Param("userId") Integer userId);
	
	/**
	 * 批量写一个集合
	 * @authro rainyhao
	 * @since 2016-4-13 上午11:50:01
	 * @param entities
	 */
	void insertList(@Param("entities")List<UserRole> entities);
}