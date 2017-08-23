package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.util.mybatis.BaseMapper;

/**
 * 表t_role的数据访问接口
 * @author rainyhao
 * @since 2016-4-11 下午2:48:12
 */
public interface RoleDao extends BaseMapper<Role>{
	
	/**
	 * 查询所有角色并标识用户已授权的角色.
	 * @authro rainyhao
	 * @since 2016-4-13 上午10:56:53
	 * @param userId 用户id
	 * @return
	 */
	List<Role> selectUserGranted(@Param("userId") Integer userId);
	
	/**
	 * 查询所有符合条件的角色
	 * @author hcy
	 * ***/
	
	
	List<Role> selectAllRoles();
	
	/**
	 * 
	 * @Title: selectRoles 
	 * @Description: 
	 * @param id
	 * @return  
	 * @return List<Role>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月23日 上午9:42:19
	 */
	List<Role> selectRoles(Integer id);
	
	
	
	/***
	 * 
	 * @Title: selectForListForCanuse 
	 * @Description: 禁用角色的时候不能加载禁用角色和对应的权限
	 * @param userId
	 * @return  
	 * @return List<Role>  
	 * @throws
	 * @author hcy
	 * @date 2016年6月13日 上午9:59:30
	 */
	List<Role> selectForListForCanuse(Role role);
	
}