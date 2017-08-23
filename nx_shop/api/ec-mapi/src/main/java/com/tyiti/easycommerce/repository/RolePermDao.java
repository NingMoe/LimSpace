package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.RolePerm;
import com.tyiti.easycommerce.util.mybatis.BaseMapper;

/**
 * 表t_role_perm的数据访问接口
 * @author rainyhao
 * @since 2016-4-11 下午2:49:18
 */
public interface RolePermDao extends BaseMapper<RolePerm> {
	
	/**
	 * 删除角色的权限
	 * @authro rainyhao
	 * @since 2016-4-13 下午3:17:07
	 * @param roleId 角色信息
	 */
	void deleteByRoleId(@Param("roleId") Integer roleId);
	
	/**
	 * 指写入一个集合
	 * @authro rainyhao
	 * @since 2016-4-13 下午3:18:49
	 * @param entities
	 */
	void insertList(@Param("entities") List<RolePerm> entities);
}