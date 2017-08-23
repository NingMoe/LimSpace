package com.tyiti.easycommerce.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.tyiti.easycommerce.entity.Permission;
import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.entity.SystemUser;
import com.tyiti.easycommerce.entity.constants.Status;
import com.tyiti.easycommerce.repository.PermissionDao;
import com.tyiti.easycommerce.repository.RoleDao;
import com.tyiti.easycommerce.repository.SystemUserDao;

/**
 * shiro的登录认证及用户权限加载的实现
 * 必须使用spring创建此类的实例
 * @author rainyhao
 * @since 2016-4-11 下午3:50:35
 */
public class AccessAuthorizeRealm extends AuthorizingRealm {
	
	@Autowired
	private SystemUserDao systemUserDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PermissionDao permissionDao; 
	
	/**
	 * 授权信息加载
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.fromRealm(getName()).iterator().next();
		// 查用户是否存在
		SystemUser entity = new SystemUser();
//		entity.setUserName(username);
		entity.setTel(username);
		SystemUser exists = systemUserDao.selectForObject(entity);
		if (null == exists) {
			throw new UnknownAccountException("用户不存在");
		}
		// 加载用户角色信息
		Role roleWhere = new Role();
		roleWhere.setUserId(exists.getId());
		//roleWhere.setFields("code");
		List<Role> lstRole = roleDao.selectForList(roleWhere);
		// 加载用户权限信息
		Permission permWhere = new Permission();
		permWhere.setUserId(exists.getId());
		//permWhere.setFields("code");
		List<Permission> lstPerm = permissionDao.selectForList(permWhere);
		// 用户的角色信息
		Set<String> roles = new HashSet<String>();
		for (Role r : lstRole) {
			roles.add(r.getCode());
		}
		// 用户的权限信息
		Set<String> priv = new HashSet<String>();
		for (Permission p : lstPerm) {
			priv.add(p.getCode());
		}
		// 准备向shrio返回用户的权限信息
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRoles(roles);
		info.addStringPermissions(priv);
		return info;
	}

	/**
	 * 登录认证
	 * @param token 即使用SecurityUtils.getSubject().login()方法做登录认证时传进来的类型
	 * 在UserApi.login方法上传入了UsernamePasswordToken类型, 所以此token可以用这个类型拆箱
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
		String username = loginToken.getUsername();
		String password = new String(loginToken.getPassword());
		// 查用户是否存在
		SystemUser entity = new SystemUser();
//		entity.setUserName(username);
		entity.setTel(username);
		SystemUser exists = systemUserDao.selectForObject(entity);
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute("user",exists);
		if (null == exists) {
			throw new UnknownAccountException("用户不存在");
		}
		if (Status.ENABLED != exists.getStatus()) {
			throw new LockedAccountException("账户已禁用");
		}
		if (!password.equals(exists.getPwd())) {
			throw new CredentialsException("密码错误");
		}
		
		
		return new SimpleAuthenticationInfo(username, password, getName());
	}

	

}
