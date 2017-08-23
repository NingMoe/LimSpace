package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Permission;
import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.entity.SystemUser;
import com.tyiti.easycommerce.entity.constants.Status;
import com.tyiti.easycommerce.repository.PermissionDao;
import com.tyiti.easycommerce.repository.RoleDao;
import com.tyiti.easycommerce.service.RoleService;
import com.tyiti.easycommerce.service.SystemUserService;
import com.tyiti.easycommerce.util.Md5;
import com.tyiti.easycommerce.util.RegExp;
import com.tyiti.easycommerce.util.TreeNode;
import com.tyiti.easycommerce.util.XString;
import com.tyiti.easycommerce.util.exception.ServiceException;
import com.tyiti.easycommerce.util.mybatis.Pager;
import com.tyiti.easycommerce.util.spring.ApiResult;
import com.tyiti.easycommerce.util.spring.BaseController;

/**
 * 系统用户相关的api
 * @author rainyhao
 * @since 2016-4-11 下午6:14:41
 */
@RestController
public class SystemUserController extends BaseController {
	
	@Autowired
	private SystemUserService systemUserService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private RoleDao roleDao;

	/**
	 * 未登录时的json提示
	 * @authro rainyhao
	 * @since 2016-4-11 下午6:15:18
	 * @return
	 */
	@RequestMapping("/sysuser/nologin")
	@ResponseBody
	public Map<String, Object> nologin() {
		return ApiResult.create(401, "未登录");
	}
	
	/**
	 * 登录
	 * @authro rainyhao
	 * @since 2016-4-12 上午8:52:28
	 * @param username 登录名
	 * @param pwd 密码
	 * @return
	 */
	@RequestMapping(value = "/sysuser/login", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> login(@RequestParam("tel") String tel, @RequestParam("pwd") String pwd,HttpSession session) {
		UsernamePasswordToken token = new UsernamePasswordToken(tel, new Md5().getMD5ofStr(pwd));
		Subject subject = SecurityUtils.getSubject();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			subject.login(token);
			SystemUser systemUser=(SystemUser) session.getAttribute("user");
			Role roleWhere = new Role();
			roleWhere.setUserId(systemUser.getId());
			List<Role> lstRole = roleDao.selectForList(roleWhere);
			// 加载用户权限信息
			Permission permWhere = new Permission();
			permWhere.setUserId(systemUser.getId());
			List<Permission> lstPerm = permissionDao.selectForList(permWhere);
			List<TreeNode> treeList = TreeMenu(lstPerm ,0);
			map.put("code", "200");
			map.put("user", systemUser);
			map.put("roles",lstRole);
			map.put("treeList",treeList);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("exception", e.getMessage());
		}
		return map;
	}
	
	
	private List<TreeNode> TreeMenu(List<Permission> perList, int parentId) {
		List<TreeNode> listTree = new ArrayList<TreeNode>();
		for (Permission permission : perList) {
			TreeNode treeModel = new TreeNode();
			Integer id = permission.getId();
			treeModel.setId(id);
			treeModel.setParentId(permission.getParentId());
			treeModel.setText(permission.getName());
			treeModel.setLink(permission.getUrl());
			treeModel.setGlyphicon(permission.getGlyphicon());
			if (parentId == permission.getParentId()) {
				List<TreeNode> treeNodes = TreeMenu(perList,
						permission.getId());
				treeModel.setNodes(treeNodes);
				listTree.add(treeModel);
			}
		}
		return listTree;
	}
	
	
	/**
	 * 退出
	 * @authro rainyhao
	 * @since 2016-4-12 下午2:19:31
	 * @return
	 */
	@RequestMapping(value="/sysuser/logout")
	@ResponseBody
	public Map<String, Object> logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		return ApiResult.success();
	}
	
	/**
	 * 查询系统管理端用户
	 * @authro rainyhao
	 * @since 2016-4-12 下午5:06:39
	 * @param username 可选, 登录名
	 * @param name 可选, 姓名
	 * @param tel 可选, 联系电话
	 * @param status 可选, 状态 0正常,1禁用
	 * @return
	 */
	@RequestMapping(value = "/sysusers", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("system:suser:gets")
	public Map<String, Object> list( String name, String tel, Integer status,
			@RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="20") Integer rows,
			HttpServletRequest request) {
		SystemUser entity = new SystemUser();
//		entity.setUserName(username);
		entity.setName(name);
		entity.setTel(tel);
		if (null != status && null != Status.forValue(status)) {
			entity.setStatus(Status.forValue(status));
		}
		Pager pager = new Pager(page, rows);
		pager.setDoCount(true);
		entity.setPager(pager);
		try {
			SearchResult<SystemUser> res = systemUserService.select(entity);
			return ApiResult.success(res);
		} catch (Exception e) {
			log.error("查询系统管理端用户出现错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 添加系统管理端用户
	 * @authro rainyhao
	 * @since 2016-4-12 下午5:15:17
	 * @param username 必选, 用户名
	 * @param name 必选, 姓名 
	 * @param tel 可选, 联系电话
	 * @return
	 */
	@RequestMapping(value = "/sysuser/add", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	@RequiresPermissions("system:suser:add")
	public Map<String, Object> add(@RequestBody SystemUser adduser,
			HttpServletRequest request) {
		if (XString.isNullOrEmpty(adduser.getName())) {
			return ApiResult.paramNull("name");
		}
//		if (XString.isNullOrEmpty(name)) {
//			return ApiResult.paramNull("name");
//		}
		// 封装请求参数
		
		if (XString.isNullOrEmpty(adduser.getTel())) {
			return ApiResult.paramNull("tel");
		}
		
		SystemUser sysuser = new SystemUser();
		//sysuser.setUserName(adduser.getUserName());
		sysuser.setPwd("123456"); // 默认新创建用户的密码123456, 等与上边确认
		sysuser.setName(adduser.getName());
		sysuser.setTel(adduser.getTel());
		sysuser.setUrl(adduser.getUrl());
		//add by hcy
		sysuser.setRoleIds(adduser.getRoleIds());
		try {
			systemUserService.add(sysuser);
			/**
			 * modify by hcy 增加用户的时候顺便把选中的角色给加上
			 * 
			 */
			return ApiResult.success(sysuser);
		} catch (ServiceException e) {
			log.error("添加系统管理端用户发业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("添加系统管理端用户发生错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 修改系统管理端用户
	 * 支持部分字段更新
	 * 但username, name, tel, status,不能同时为空
	 * @authro rainyhao
	 * @since 2016-4-12 下午5:17:26
	 * @param id 必选, id
	 * @param username 可选, 登录名
	 * @param name 可选, 姓名
	 * @param tel 可选, 电话
	 * @param status 可选, 状态 0正常,1:禁用
	 * @return
	 */
	@RequestMapping(value = "/sysuer/update", method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	@RequiresPermissions("system:suser:update")
	public Map<String, Object> update(@RequestBody SystemUser adduser,
			HttpServletRequest request) {
		if (null == adduser.getId()) {
			return ApiResult.paramNull("id");
		}
		if (XString.isNullOrEmpty(adduser.getUserName()) && XString.isNullOrEmpty(adduser.getName()) && XString.isNullOrEmpty(adduser.getTel()) && null == adduser.getStatus()) {
			return ApiResult.paramInvalid("username, name, tel, status,不能同时为空");
		}
//		if (null != adduser.getStatus()) {
//			return ApiResult.paramInvalid("status值不正确");
//		}
		
		try {
			//修改之前先判断一下用户授权角色表里面是否
			
			//SystemUser user=systemUserService.findByPrimaryKey(adduser.getId());
			//adduser.setPwd("123456");
			systemUserService.update(adduser);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error("修改系统用户发生业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("修改系统用户发系统错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 重置用户密码
	 * @authro rainyhao
	 * @since 2016-4-12 下午6:17:03
	 * @param id 用户id
	 * @return
	 */
	@RequestMapping(value = "/sysuser/pwd/reset", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system:suser:pwd:reset")
	public Map<String, Object> resetPwd(@RequestBody Integer id, HttpServletRequest request) {
		if (null == id) {
			return ApiResult.paramNull("id");
		}
		SystemUser sysuser = new SystemUser();
		sysuser.setId(id);
		sysuser.setPwd("123456"); // 重置为初始密码123456
		try {
			systemUserService.updateResetPass(sysuser);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error("修改系统用户发生业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("修改系统用户发系统错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 加载管理端用户的当前角色
	 * @authro rainyhao
	 * @since 2016-4-13 上午9:26:46
	 * @param userId 管理端用户
	 * @return
	 */
	@RequestMapping(value = "/sysuser/roles", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("system:suser:get")
	public Map<String, Object> roles(Integer userId) {
		if (null == userId) {
			return ApiResult.paramNull("userId");
		}
		try {
			List<Role> lstRole = roleService.selectUserCurrent(userId);
			return ApiResult.success(lstRole);
		} catch (Exception e) {
			log.error("加载户当前角色信息发生内部错误", e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 给用户授权角色
	 * @authro rainyhao
	 * @since 2016-4-13 上午11:26:25
	 * @param userId 必选, 管理端用户id
	 * @param roleIds 必选, 逗号隔开的权限id
	 * @return
	 */
	@RequestMapping(value = "/sysuser/role/grant", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("system:role:manager")
	public Map<String, Object> grantRole(Integer userId, String roleIds, HttpServletRequest request) {
		if (null == userId) {
			return ApiResult.paramNull("userId");
		}
		if (XString.isNullOrEmpty(roleIds)) {
			return ApiResult.paramNull("roleIds");
		}
		if (!RegExp.isAllDigit(roleIds)) {
			return ApiResult.paramInvalid("roleIds所有值必须都是数字");
		}
		try {
			roleService.grantForUser(userId, roleIds);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error("为用户授权角色发生业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("为用户授权角色发生内部错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	
	/**
	 * 根据主键查询管理端用户
	 * @author hcy
	 * @param userId
	 * 
	 * ****/
	
	@RequestMapping(value = "/sysuser/role", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> selectSystemUser(Integer id, HttpServletRequest request) {
		if (null == id) {
			return ApiResult.paramNull("id");
		}
		try {
			SystemUser user=systemUserService.findByPrimaryKey(id);
			
			/**
			 * 修改用户的时候把当前的角色也给加载上 
			 * modify by hcy 
			 */
			List<Role> lstRole = roleService.selectUserCurrent(id);
			
			user.setRoles(lstRole);
			
			return ApiResult.success(user);
		} catch (Exception e) {
			return ApiResult.sysError();
		}
	}
	/***
	 * 
	 * @Title: changeStatus 
	 * @Description: 启用/禁用 用户
	 * @param id  主键
	 * @param status 用户状态
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月16日 上午10:47:52
	 */
	@RequestMapping(value="/sysuser/changeStatus",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system:suser:changeStatus")
	public Map<String, Object> changeStatus(Integer id,String status,HttpServletRequest request) {
		if (null == id) {
			return ApiResult.paramNull("id");
		}
		if (null == status) {
			return ApiResult.paramNull("status");
		}
		
	try {
			SystemUser user=systemUserService.findByPrimaryKey(id);
			if("ENABLED".equals(status)){
				user.setStatus(Status.forValue(0));	
			}else{
				user.setStatus(Status.forValue(1));
			}
			
			systemUserService.updateStatus(user);
		
		return ApiResult.success();
	} catch (ServiceException e) {
		log.error("修改系统用户发生业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
		return ApiResult.paramInvalid(e);
	} catch (Exception e) {
		log.error("修改系统用户发系统错误: " + super.getQueryString(request), e);
		return ApiResult.sysError();
	}
	
		}
	
	
	/***
	 * 
	 * @Title: flush 
	 * @Description: 刷新页面给返回页面详细信息
	 * @param session
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月20日 上午11:16:02
	 */
	@RequestMapping(value="/sysuser/flush",method = RequestMethod.GET)
	@ResponseBody
	//@RequiresPermissions("system:suser:flush")
	public Map<String, Object> flush(HttpSession session) {
		SystemUser user =(SystemUser) session.getAttribute("user");
		if(user==null){
			return ApiResult.create(401, "未登录");
		}
		List<Role> lstRole = roleService.findUserRoles(user.getId());
		user.setRoles(lstRole);
		return ApiResult.success(user);
		
	}
	
	/**
	 * 
	 * @Title: getUserPermis 
	 * @Description: 登录传权限
	 * @param session
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月26日 下午1:45:01
	 */
	@RequestMapping(value="/getUserPermis",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getUserPermis(HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user =(SystemUser) session.getAttribute("user");
			if(user==null){
				return ApiResult.create(401, "未登录");
			}
			Role roleWhere = new Role();
			roleWhere.setUserId(user.getId());
			List<Role> lstRole = roleDao.selectForListForCanuse(roleWhere);
			// 加载用户权限信息
			Permission permWhere = new Permission();
			permWhere.setUserId(user.getId());
			List<Permission> lstPerm = permissionDao.selectForListForCanuse(permWhere);
			List<String> codes=new ArrayList<String>();
			for(Permission p:lstPerm){
				codes.add(p.getCode());
			}
			List<TreeNode> treeList = TreeMenu(lstPerm ,0);
			map.put("code", "200");
			map.put("menu", treeList);
			map.put("roles",lstRole);
			map.put("codes", codes);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("exception", e.getMessage());
		}
		
		return map;
		
	}
	
	
	/**
	 * 
	 * @Title: modifyPass 
	 * @Description: 
	 * @param pwd 原密码
	 * @param newPass 新密码
	 * @param session
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月27日 下午5:35:21
	 */
	@RequestMapping(value="/sysuser/modifyPass",method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> modifyPass(String pwd,String newPass, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (null == pwd) {
				return ApiResult.paramNull("pwd");
			}
			if (null == newPass) {
				return ApiResult.paramNull("newPass");
			}
			SystemUser user=(SystemUser) session.getAttribute("user");
			
			if(user==null){
				return ApiResult.create(401, "未登录");
			}
			if(!(new Md5().getMD5ofStr(pwd)).equals(user.getPwd())){//new Md5().getMD5ofStr(pwd)
				return ApiResult.getWps("输入密码与原密码不一致！");
			}
			
			user.setPwd(newPass);
			systemUserService.updatePass(user);
			map.put("code", "200");
		} catch (Exception e) {
			map.put("code", "400");
			map.put("exception", e.getMessage());
		}
		return map;
		
	}
	
}


