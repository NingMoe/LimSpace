package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.util.exception.ServiceException;

/**
 * 角色相关的业务处理程序
 * @author rainyhao
 * @since 2016-4-12 下午3:04:56
 */
public interface RoleService {

	/**
	 * 按特定的查询条件加载角色信息
	 * @authro rainyhao
	 * @since 2016-4-12 下午3:05:33
	 * @param role
	 */
	public SearchResult<Role> select(Role entity);
	
	/**
	 * 添加角色
	 * @authro rainyhao
	 * @since 2016-4-12 下午3:09:35
	 * @param role 新角色信息
	 */
	public void add(Role role) throws ServiceException;
	
	/**
	 * 修改角色
	 * @authro rainyhao
	 * @since 2016-4-12 下午3:10:34
	 * @param role 新角色信息
	 */
	public void update(Role role) throws ServiceException;
	
	/**
	 * 加载用户的当前角色
	 * @authro rainyhao
	 * @since 2016-4-13 上午9:28:49
	 * @param userId 用户id
	 * @return
	 */
	public List<Role> selectUserCurrent(Integer userId);
	
	/**
	 * 给用户授权角色
	 * @authro rainyhao
	 * @since 2016-4-13 下午12:40:06
	 * @param userId 用户id
	 * @param roleIds 逗号隔开的角色id
	 * @throws ServiceException
	 */
	void grantForUser(Integer userId, String roleIds) throws ServiceException;
	
	
	/**
	 *查询状态正常的所有的角色
	 *@author hcy
	 * **/
	public List<Role> selectRoles();
	
	/**
	 * 
	 * @Title: findByPrimaryKey 
	 * @Description: 根据主键查询当前角色信息
	 * @param id 主键 
	 * @return  
	 * @return Role  
	 * @throws
	 * @author hcy
	 * @date 2016年5月16日 上午9:13:59
	 */
	public Role findByPrimaryKey(Integer id);
	
	
	
	/**
	 * 
	 * @Title: deleteRole 
	 * @Description: 根据主键删除非内置角色
	 * @param id  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年5月18日 上午9:22:15
	 */
	public void deleteRole(Integer id);
	
	/**
	 * 
	 * @Title: findUserRoles 
	 * @Description: 把 所有的符合条件的角色选出来 而不是把角色列表列出来
	 * @param userId
	 * @return  
	 * @return List<Role>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月23日 上午9:16:05
	 */
	public List<Role> findUserRoles(Integer userId);
	
	/**
	 * 
	 * @Title: updateStatus 
	 * @Description: 修改角色状态
	 * @param role
	 * @throws ServiceException  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年5月31日 下午7:28:56
	 */
	public void updateStatus(Role role) throws ServiceException;
	
}
