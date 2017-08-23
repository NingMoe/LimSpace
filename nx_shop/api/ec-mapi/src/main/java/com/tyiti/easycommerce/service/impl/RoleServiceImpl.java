package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.entity.SystemUser;
import com.tyiti.easycommerce.entity.UserRole;
import com.tyiti.easycommerce.repository.PermissionDao;
import com.tyiti.easycommerce.repository.RoleDao;
import com.tyiti.easycommerce.repository.RolePermDao;
import com.tyiti.easycommerce.repository.SystemUserDao;
import com.tyiti.easycommerce.repository.UserRoleDao;
import com.tyiti.easycommerce.service.PermissionService;
import com.tyiti.easycommerce.service.RoleService;
import com.tyiti.easycommerce.util.XDate;
import com.tyiti.easycommerce.util.XString;
import com.tyiti.easycommerce.util.exception.ServiceException;

/**
 * 角色信息的业务处理程序
 * @author rainyhao
 * @since 2016-4-12 下午3:11:10
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	// 角色信息的数据访问接口
	@Autowired
	private RoleDao roleDao;
	// 管理端用户相关的数据接口
	@Autowired
	private SystemUserDao systemUserDao;
	// 用户角色授权信息的数据访问
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private PermissionService permissionService; 
	
	@Autowired
	private RolePermDao rolePermDao;

	@Override
	public SearchResult<Role> select(Role entity) {
		List<Role> lstRole = roleDao.selectForPagedList(entity);
		SearchResult<Role> res = new SearchResult<Role>();
		res.setRows(lstRole);
		res.setTotal(entity.getPager().getTotal());
		return res;
	}

	@Transactional
	@Override
	public void add(Role role) throws ServiceException {
		Role entity = new Role();
		// 检查名称是否重复
		entity.setName(role.getName());
		Role exists = roleDao.selectForObject(entity);
		if (null != exists) {
			throw new ServiceException("角色名称重复");
		}
		entity.setName(null);
		// 检查shiro代码是否重复
//		entity.setCode(role.getCode());
//		exists = roleDao.selectForObject(entity);
//		if (null != exists) {
//			throw new ServiceException("shiro代码重复");
//		}
		// 写入
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("PermIds", role.getPermIds());
		//role.setIsDefault(false); // 只要是手动添加的, 肯定不是系统内置的
		//role.setStatus(Status.forValue(Integer.valueOf())); // 默认角色状态都正常
		//role.setCreateTime(XDate.now()); // 创建时间
		entity.setName(role.getName());
		//entity.setCode(role.getCode());
		entity.setCreateTime(XDate.now());
		entity.setIsDefault(false);
		entity.setStatus(role.getStatus());
		roleDao.insert(entity);
		String permIds=map.get("PermIds");
		if(!"".equals(permIds)){
			permissionService.grantForRole(entity.getId(),permIds );	
		}
		map.clear();
		
	}

	@Transactional
	@Override
	public void update(Role role) throws ServiceException {
		
		
		// 修改权限信息
		if(role.getPermIds()!=null&&!"".equals(role.getPermIds())){
			permissionService.grantForRole(role.getId(), role.getPermIds());
		}else{
			rolePermDao.deleteByRoleId(role.getId());
		}
		
		Role entity = new Role();
		// 检查名称是否重复
		if (!XString.isNullOrEmpty(role.getName())) {
			entity.setName(role.getName());
			Role exists = roleDao.selectForObject(entity);
			if (null != exists && exists.getId().intValue() != role.getId().intValue()) {
				throw new ServiceException("角色名称重复");
			}
		}
		// 检查shiro代码是否重复
		/*if (!XString.isNullOrEmpty(role.getCode())) {
			entity = new Role();
			entity.setCode(role.getCode());
			Role exists = roleDao.selectForObject(entity);
			if (null != exists && exists.getId().intValue() != role.getId().intValue()) {
				throw new ServiceException("shiro代码重复");
			}
		}*/
		
		// 修角色信息
		roleDao.update(role);
	}
	
	@Transactional
	@Override
	public void updateStatus(Role role) throws ServiceException {
		
		roleDao.update(role);
	}

	@Transactional
	@Override
	public List<Role> selectUserCurrent(Integer userId) {
		return roleDao.selectUserGranted(userId);
	}

	@Transactional
	@Override
	public void grantForUser(Integer userId, String roleIds) throws ServiceException {
		// 检查用户是否存在
		SystemUser exists = systemUserDao.selectByPrimaryKey(userId);
		if (null == exists) {
			throw new ServiceException("用户不存在");
		}
		// 删除当前授权数据
		userRoleDao.deleteByUserId(userId);
		// 写入的新授权数据
		List<UserRole> toGrant = new ArrayList<UserRole>();
		String[] arrRoles = roleIds.split(",");
		for (String r : arrRoles) {
			UserRole ur = new UserRole();
			ur.setUserId(userId);
			ur.setRoleId(Integer.parseInt(r));
			toGrant.add(ur);
		}
		// 写入
		userRoleDao.insertList(toGrant);
	}

	@Override
	public List<Role> selectRoles() {
		List<Role> roles=roleDao.selectAllRoles();
		return roles;
	}

	@Override
	public Role findByPrimaryKey(Integer id) {
		Role role=roleDao.selectByPrimaryKey(id);
		return role;
	}

	@Override
	public void deleteRole(Integer id) {
		roleDao.delete(id);
		permissionDao.deleByRoleId(id);
		
	}

	@Override
	public List<Role> findUserRoles(Integer userId) {
		// TODO Auto-generated method stub
		List<Role> listRoles=roleDao.selectRoles(userId);
		return listRoles;
	}
}
