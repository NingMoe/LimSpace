package com.tyiti.easycommerce.service;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.SystemUser;
import com.tyiti.easycommerce.util.exception.ServiceException;

/**
 * 系统管理端用户相关业务处理程序
 * @author rainyhao
 * @since 2016-4-12 下午5:11:03
 */
public interface SystemUserService {
	
	/**
	 * 查询系统用户列表
	 * @authro rainyhao
	 * @since 2016-4-12 下午5:12:52
	 * @param entity 查询条件
	 * @return
	 */
	SearchResult<SystemUser> select(SystemUser entity);
	
	/**
	 * 添加管理端用户
	 * @authro rainyhao
	 * @since 2016-4-12 下午5:21:11
	 * @param sysuser 新用户信息
	 */
	void add(SystemUser sysuser) throws ServiceException;
	
	/**
	 * 更新管理端用户
	 * @authro rainyhao
	 * @since 2016-4-12 下午5:21:51
	 * @param sysuser 要更新成的用户信息
	 */
	void update(SystemUser sysuser) throws ServiceException;
	
	/**
	 * 根据主键查询用户
	 * @author hcy
	 * 
	 * ***/
	
	SystemUser findByPrimaryKey(Integer userId);
	
	
	/**
	 * 
	 * @Title: updateStatus 
	 * @Description: 
	 * @param systemUser
	 * @throws ServiceException  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年6月1日 上午9:28:26
	 */
	void updateStatus(SystemUser systemUser) throws ServiceException;
	
	
	
	
	/**
	 * 
	 * @Title: updatePass 
	 * @Description: 修改密码
	 * @param systemUser
	 * @throws ServiceException  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年6月1日 上午10:31:02
	 */
	void updatePass(SystemUser systemUser) throws ServiceException;
	
	
	/**
	 * 
	 * @Title: updateResetPass 
	 * @Description: 重置密码 123456
	 * @param systemUser
	 * @throws ServiceException  
	 * @return void  
	 * @throws
	 * @author hcy
	 * @date 2016年6月1日 下午3:36:47
	 */
	void updateResetPass(SystemUser systemUser) throws ServiceException;
}
