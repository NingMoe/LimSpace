package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Permission;
import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.entity.RolePerm;
import com.tyiti.easycommerce.repository.PermissionDao;
import com.tyiti.easycommerce.repository.RoleDao;
import com.tyiti.easycommerce.repository.RolePermDao;
import com.tyiti.easycommerce.service.PermissionService;
import com.tyiti.easycommerce.util.exception.ServiceException;

/**
 * 权限相关的业务处理程序
 * @author rainyhao
 * @since 2016-4-12 下午10:33:47
 */
@Service
public class PermissionServiceImpl implements PermissionService {
	
	// 权限信息的数据访问接口
	@Autowired
	private PermissionDao permissionDao;
	// 角色权限信息数据访问
	@Autowired
	private RolePermDao rolePermDao;
	// 角色信息的数据访问
	@Autowired
	private RoleDao roleDao;

	@Override
	public void add(Permission perm) throws ServiceException {
		// 检查权限代码是否重复
		Permission entity = new Permission();
		entity.setCode(perm.getCode());
		Permission exists = permissionDao.selectForObject(entity);
		if (null != exists) {
			throw new ServiceException("权限代码已存在");
		}
		entity.setCode(null);
		entity.setName(perm.getName());
		Permission exists1 = permissionDao.selectForObject(entity);
		if (null != exists1) {
			throw new ServiceException("权限名称已经重复");
		}
		// 写入
		permissionDao.insert(perm);
	}

	@Override
	public void delete(Integer id) throws ServiceException {
		// 检查要删除的点下边是否还有点
		Permission entity = new Permission();
		entity.setParentId(id);
		int count = permissionDao.selectCount(entity);
		if (count > 0) {
			throw new ServiceException("当前权限之下还有子权限, 不能删除");
		}
		// 删除
		permissionDao.delete(id);
	}

	@Override
	public List<Permission> selectRoleCurrent(Integer roleId) {
		return permissionDao.selectRoleGranted(roleId);
	}

	@Override
	public void grantForRole(Integer roleId, String permIds) throws ServiceException {
		// 检查角色是否存在
		Role exists = roleDao.selectByPrimaryKey(roleId);
		if (null == exists) {
			throw new ServiceException("角色不存在");
		}
		// 删除当前授权数据
		rolePermDao.deleteByRoleId(roleId);
		// 写入新数据
		List<RolePerm> toGrant = new ArrayList<RolePerm>();
		String[] arrPerms = permIds.split(",");
		for (String p : arrPerms) {
			RolePerm rp = new RolePerm();
			rp.setRoleId(roleId);
			rp.setPermId(Integer.parseInt(p));
			toGrant.add(rp);
		}
		// 写入
		rolePermDao.insertList(toGrant);
	}

	@Override
	public void deleteByRoleId(Integer roleId) throws ServiceException {
		// TODO Auto-generated method stub
		permissionDao.deleByRoleId(roleId);
		
	}

	@Override
	public void updatePer(Permission permission) {
		
		int i=permissionDao.selectPerCountCode(permission);
		if(i>0){
			throw new ServiceException("code重复！");
		}
		int i1=permissionDao.selectPerCountName(permission);
		if(i1>0){
			throw new ServiceException("权限名称重复！");
		}
		permissionDao.update(permission);
	}

	@Override
	public Permission findByPrimary(Integer id) {
		// TODO Auto-generated method stub
		Permission per=permissionDao.selectByPrimaryKey(id);
		return per;
	}

	@Override
	public int selectPerCount(
			com.tyiti.easycommerce.entity.Permission permission) {
		// TODO Auto-generated method stub
		int i=permissionDao.selectPerCountCode(permission);
		return i;
	}

}
