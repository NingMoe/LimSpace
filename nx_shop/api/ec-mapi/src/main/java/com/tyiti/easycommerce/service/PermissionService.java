package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Permission;
import com.tyiti.easycommerce.util.exception.ServiceException;

/**
 * 权限相关的业务处理程序
 * @author rainyhao
 * @since 2016-4-12 下午10:23:32
 */
public interface PermissionService {
	
	/**
	 * 添加权限
	 * @author rainyhao
	 * @since 2016-4-12 下午10:34:12
	 * @param perm 权限信息
	 */
	void add(Permission perm) throws ServiceException;
	
	/**
	 * 删除
	 * @author rainyhao
	 * @since 2016-4-12 下午10:35:34
	 * @param id
	 */
	void delete(Integer id) throws ServiceException;
	
	/**
	 * 加载角色的当前角色
	 * @authro rainyhao
	 * @since 2016-4-13 下午2:06:39
	 * @param roleId 角色id
	 * @return
	 */
	List<Permission> selectRoleCurrent(Integer roleId);
	
	/**
	 * 为角色授权操作权限
	 * @authro rainyhao
	 * @since 2016-4-13 下午2:52:50
	 * @param roleId 角色id
	 * @param permIds 逗号隔开的权限id
	 */
	void grantForRole(Integer roleId, String permIds) throws ServiceException;
	
	
	/***
	 * 
	 * @Title: deleteByRoleId 
	 * @Description: 删除角色对应的权限
	 * @param roleId
	 * @throws ServiceException  
	 * @return void  
	 * @throws 
	 * @author hcy
	 * @date 2016年5月18日 上午10:03:00
	 */
	void deleteByRoleId(Integer roleId) throws ServiceException;
	
	/**
	 * 
	 * @Title: updatePer 
	 * @Description:更新权限 
	 * @param permission  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年5月20日 下午5:17:15
	 */
	void updatePer(Permission permission);
	
	/**
	 * 
	 * @Title: findByPrimary 
	 * @Description: 根据主键查询权限信息
	 * @param id
	 * @return  
	 * @return Permission  
	 * @throws
	 * @author hcy
	 * @date 2016年5月20日 下午5:45:31
	 */
	Permission findByPrimary(Integer id);
	
	
	/**
	 * 
	 * @Title: selectPerCount 
	 * @Description: 更改权限时 去重
	 * @param permission
	 * @return  
	 * @return int  
	 * @throws
	 * @author hcy
	 * @date 2016年6月7日 下午3:47:53
	 */
	int selectPerCount(Permission permission);
}
