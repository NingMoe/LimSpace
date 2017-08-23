package com.tyiti.easycommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.SystemUser;
import com.tyiti.easycommerce.entity.constants.Status;
import com.tyiti.easycommerce.repository.SystemUserDao;
import com.tyiti.easycommerce.repository.UserRoleDao;
import com.tyiti.easycommerce.service.RoleService;
import com.tyiti.easycommerce.service.SystemUserService;
import com.tyiti.easycommerce.util.Md5;
import com.tyiti.easycommerce.util.XDate;
import com.tyiti.easycommerce.util.XString;
import com.tyiti.easycommerce.util.exception.ServiceException;

/**
 * 系统管理端用户相关业务处理程序
 * @author rainyhao
 * @since 2016-4-12 下午5:22:45
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

	@Autowired
	private SystemUserDao systemUserDao;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public SearchResult<SystemUser> select(SystemUser entity) {
		List<SystemUser> lstSysuser = systemUserDao.selectForPagedList(entity);
		SearchResult<SystemUser> res = new SearchResult<SystemUser>();
		res.setRows(lstSysuser);
		res.setTotal(entity.getPager().getTotal());
		return res;
	}

	@Transactional
	@Override
	public void add(SystemUser sysuser) throws ServiceException {
		SystemUser entity = new SystemUser();
		//entity.setUserName(sysuser.getUserName());
		entity.setTel(sysuser.getTel());
		// 检查登录名是否重复
		SystemUser exists = systemUserDao.selectForObject(entity);
		if (null != exists) {
			throw new ServiceException("登录名重复");
		}
		// 写入
		sysuser.setPwd(new Md5().getMD5ofStr(sysuser.getPwd())); // 密码要md5一次
		sysuser.setStatus(Status.ENABLED);
		sysuser.setCreateTime(XDate.now());
		
		systemUserDao.insert(sysuser);
		
		/**
		 * 用户可以选择角色  可以不选择用户
		 */
		if(!"".equals(sysuser.getRoleIds())){
			roleService.grantForUser(sysuser.getId(), sysuser.getRoleIds());
		}
		
		
	}

	@Override
	public void update(SystemUser sysuser) throws ServiceException {
		// 如果输入了要修改用户名, 先检查用户名是否已存在
		if (!XString.isNullOrEmpty(sysuser.getTel())) {
			int count = systemUserDao.selectupCount(sysuser);
			if (count>0) {
				throw new ServiceException("登录名已存在");
			}
			userRoleDao.deleteByUserId(sysuser.getId());
		}
		
		
		if(!"".equals(sysuser.getRoleIds())&&null!=sysuser.getRoleIds()){
			roleService.grantForUser(sysuser.getId(), sysuser.getRoleIds());
		}
		//String pwd = sysuser.getPwd();
//		if (!XString.isNullOrEmpty(pwd)) {
//			sysuser.setPwd(new Md5().getMD5ofStr(pwd));
//		}
		// 修改
		SystemUser user=systemUserDao.selectByPrimaryKey(sysuser.getId());
		user.setPwd(user.getPwd());
		user.setTel(sysuser.getTel());
		user.setName(sysuser.getName());
		user.setCreateTime(user.getCreateTime());
		systemUserDao.update(user);
	}

	@Override
	public SystemUser findByPrimaryKey(Integer userId) {
		SystemUser systemUser=new SystemUser();
		systemUser=systemUserDao.selectByPrimaryKey(userId);
		return systemUser;
	}

	@Override
	public void updateStatus(SystemUser systemUser) throws ServiceException {
		// TODO Auto-generated method stub
		systemUserDao.update(systemUser);
		
	}

	@Override
	public void updatePass(SystemUser systemUser) throws ServiceException {
		// TODO Auto-generated method stub
		String pwd = systemUser.getPwd();
		if (!XString.isNullOrEmpty(pwd)) {
			systemUser.setPwd(new Md5().getMD5ofStr(pwd));
		}
		systemUserDao.update(systemUser);
		
	}

	@Override
	public void updateResetPass(SystemUser systemUser) throws ServiceException {
		// TODO Auto-generated method stub
		String pwd = systemUser.getPwd();
		if (!XString.isNullOrEmpty(pwd)) {
			systemUser.setPwd(new Md5().getMD5ofStr(pwd));
		}
		systemUserDao.update(systemUser);
	}

}
