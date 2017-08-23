package com.tyiti.easycommerce.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Permission;
import com.tyiti.easycommerce.util.mybatis.BaseMapper;

/**
 * 表t_permission的数据访问接口
 * @author rainyhao
 * @since 2016-4-11 下午2:47:11
 */
public interface PermissionDao extends BaseMapper<Permission>{
	
	/**
	 * 查询所有权限并标识哪此是已授权给指定角色的
	 * @authro rainyhao
	 * @since 2016-4-13 下午2:09:20
	 * @param roleId 角色id
	 * @return
	 */
	List<Permission> selectRoleGranted(@Param("roleId") Integer roleId);
	
	/**
	 * 
	 * @Title: deleByRoleId 
	 * @Description: 
	 * @param roleId  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年5月18日 上午10:06:46
	 */
	void deleByRoleId(Integer id);
	
	
	
	/**
	 * 
	 * @Title: selectPerCount 
	 * @Description: 
	 * @return  
	 * @return int  
	 * @throws
	 * @author hcy
	 * @date 2016年6月7日 下午3:44:01
	 */
	int selectPerCountCode(Permission permission);
	
	
	/**
	 * 
	 * @Title: selectPerCountName 
	 * @Description: 
	 * @param permission
	 * @return  
	 * @return int  
	 * @throws
	 * @author hcy
	 * @date 2016年6月7日 下午3:58:10
	 */
	int selectPerCountName(Permission permission);
	
	
	
	/**
	 * 
	 * @Title: selectForListForCanuse 
	 * @Description: 查询未被禁用的角色的权限
	 * @param permission
	 * @return  
	 * @return List<Permission>  
	 * @throws
	 * @author hcy
	 * @date 2016年6月13日 上午10:54:21
	 */
	List<Permission> selectForListForCanuse(Permission permission);
}