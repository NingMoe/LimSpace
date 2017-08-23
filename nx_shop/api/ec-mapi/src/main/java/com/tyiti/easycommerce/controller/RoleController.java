package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Permission;
import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.entity.constants.Status;
import com.tyiti.easycommerce.service.PermissionService;
import com.tyiti.easycommerce.service.RoleService;
import com.tyiti.easycommerce.util.CheckState;
import com.tyiti.easycommerce.util.RegExp;
import com.tyiti.easycommerce.util.TreeNode;
import com.tyiti.easycommerce.util.XString;
import com.tyiti.easycommerce.util.exception.ServiceException;
import com.tyiti.easycommerce.util.mybatis.Pager;
import com.tyiti.easycommerce.util.spring.ApiResult;
import com.tyiti.easycommerce.util.spring.BaseController;

/**
 * 角色管理相关的api
 * @author rainyhao
 * @since 2016-4-12 下午2:40:55
 */
@RestController
public class RoleController extends BaseController {
	
	// 角色相关的业务处理
	@Autowired
	private RoleService roleService;
	// 权限相关的业务处理
	@Autowired
	private PermissionService permissionService;
	
	/**
	 * 查询角色列表
	 * @authro rainyhao
	 * @since 2016-4-12 下午3:59:36
	 * @param name 可选, 角色名
	 * @param code 可选, shiro代码
	 * @param status 可选, 状态, 0正常,1禁用
	 * @param page 可选, 第几页
	 * @param rows 可选, 每页多少条
	 * @return
	 */
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public Map<String, Object> list(String name, String code, Integer status, 
			@RequestParam(defaultValue="1") Integer page, @RequestParam(defaultValue="20") Integer rows,
			HttpServletRequest request) {
		Role entity = new Role();
		entity.setName(name);
		entity.setCode(code);
		if (null != status && null != Status.forValue(status)) {
			entity.setStatus(Status.forValue(status));
		}
		Pager pager = new Pager(page, rows);
		pager.setDoCount(true);
		entity.setPager(pager);
		try {
			SearchResult<Role> res = roleService.select(entity);
			return ApiResult.success(res);
		} catch (Exception e) {
			log.error("查询角色信息出现系统错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 添加自定义角色
	 * @authro rainyhao
	 * @since 2016-4-12 下午4:06:33
	 * @param name 必选, 角色名
	 * @param code 必选, shiro代码
	 * @return
	 */
	
	/****
	 * @author modify by hcy   
	 * @des:在原来的基础上多增加一个参数 标识 停用 或者启用的状态
	 * @date 2016年5月16日 上午9:38:41
	 *  status 0 启用  1 禁用
	 */
	@RequestMapping(value = "/role/add", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system:role:add")
	public Map<String, Object> add(String name,String permIds, HttpServletRequest request) {
		if (XString.isNullOrEmpty(name)) {
			return ApiResult.paramNull("name");
		}
//		if (XString.isNullOrEmpty(code)) {
//			return ApiResult.paramNull("code");
//		}
		/*if (!RegExp.isAllDigit(permIds)) {
			return ApiResult.paramInvalid("permIds各值必须是数字形式");
		}*/
		// 封装请求参数
		Role role = new Role();
		role.setName(name);
		//role.setCode(code);
		role.setPermIds(permIds);
		role.setStatus(Status.forValue(0));
		try {
			roleService.add(role);
			return ApiResult.success(role);
		} catch(ServiceException e) {
			log.error(e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("添加角色时出现系统错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 修改角色信息
	 * 支持部分字段修改, 但name,code,status不能同时为空
	 * @authro rainyhao
	 * @since 2016-4-12 下午4:25:43
	 * @param id
	 * @param name
	 * @param code
	 * @param status
	 * @return
	 */
	/**
	 * modify 修改角色信息的时候把权限也给加上
	 * @author hcy
	 * @date 2016年5月24日 上午10:50:47
	 */
	@RequestMapping(value = "/role/update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system:role:update")
	public Map<String, Object> update(Integer id, String name,  String status,String permIds, HttpServletRequest request) {
		if (null == id) {
			return ApiResult.paramNull("id");
		}
		if (XString.isNullOrEmpty(name)  && null == status) {
			return ApiResult.paramInvalid("name,status不能同时为空");
		}
//		if (null != status) {
//			return ApiResult.paramInvalid("status值不正确");
//		}
		/*if (!RegExp.isAllDigit(permIds)) {
			return ApiResult.paramInvalid("permIds各值必须是数字形式");
		}*/
		// 封装请求参数
		Role role = new Role();
		role.setId(id);
		role.setName(name);
		//role.setCode(code);
		role.setPermIds(permIds);
		if("ENABLED".equals(status)){
			role.setStatus(Status.ENABLED);	
		}else{
			role.setStatus(Status.DISABLED);	
		}
		
		try {
			roleService.update(role);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error(e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("修改角色时出现系统错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 加载角色的当前权限
	 * @authro rainyhao
	 * @since 2016-4-13 下午1:43:47
	 * @param roleId 角色id
	 * @return
	 */
	@RequestMapping(value = "/role/perms", method = RequestMethod.GET)
	public Map<String, Object> perms(Integer roleId) {
		if (null == roleId) {
			return ApiResult.paramNull("roleId");
		}
		try {
			List<Permission> lstPerm = permissionService.selectRoleCurrent(roleId);
			return ApiResult.success(lstPerm);
		} catch (Exception e) {
			log.error("查询角色的权限授权信息发生内部错误: " + "roleId=" + roleId, e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 为角色授权操作权限
	 * @authro rainyhao
	 * @since 2016-4-13 下午2:51:20
	 * @param roleId 角色id
	 * @param permIds 逗号隔开的权限id
	 * @return
	 */
	@RequestMapping(value="/role/grantPerm",method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system:role:grantPerm")
	public Map<String, Object> grantPerm(Integer roleId, String permIds, HttpServletRequest request) {
		if (null == roleId) {
			return ApiResult.paramNull("roleId");
		}
		if (XString.isNullOrEmpty(permIds)) {
			return ApiResult.paramNull("permIds");
		}
		if (!RegExp.isAllDigit(permIds)) {
			return ApiResult.paramInvalid("permIds各值必须是数字形式");
		}
		try {
			permissionService.grantForRole(roleId, permIds);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error("为角色授权时发生业务校验错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("为角色授权时发生内部错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 加载所有的角色
	 * @author hcy
	 * 
	 * ***/
	@RequestMapping(value = "/rolesAll", method = RequestMethod.GET)
	public Map<String, Object> listRoles(HttpServletRequest request){
		List<Role> roles=new ArrayList<Role>();
		try {
			roles=roleService.selectRoles();
			return ApiResult.success(roles);
			
		} catch (ServiceException e) {
			log.error("查询全部角色时发生业务校验错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		}catch (Exception e) {
			log.error("查询全部角色时发生内部错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
		
		
	}
	
	/***
	 * 
	 * @Title: changeStatus 
	 * @Description: 
	 * @param id Role 主键
	 * @param status 角色权限
	 * @param request
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月16日 上午11:39:52
	 */
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeStatus(Integer id,String status,HttpServletRequest request){
		try {
			if(null==id){
				return ApiResult.paramNull("id");
			}
			Role rol=roleService.findByPrimaryKey(id);
			if(status.equals("ENABLED")){
				rol.setStatus(Status.forValue(0));
			}else{
				rol.setStatus(Status.forValue(1));
			}
			roleService.updateStatus(rol);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error("置换角色状态发生业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		}catch (Exception e) {
			log.error("置换角色状态发生内部错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}

	}
	
	/***
	 * 
	 * @Title: RoleFindByPrimary 
	 * @Description: 根据住建查询角色信息 
	 * @param id
	 * @param request
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月16日 下午4:51:54
	 */
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> RoleFindByPrimary(Integer id,HttpServletRequest request){
		try {
			if(null==id){
				return ApiResult.paramNull("id");
			}
			Role rol=roleService.findByPrimaryKey(id);
			return ApiResult.success(rol);
		} catch (ServiceException e) {
			log.error("查询角色状态发生业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		}catch (Exception e) {
			log.error("查询角色状态发生内部错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
		
	}
	
	
	
	/***
	 * 
	 * @Title: deleteRole 
	 * @Description: 删除角色 自己手动添加的角色可以删除  系统有的不能删除  删除角色之后也把对应的权限也给删除了
	 * @param id 角色主键
	 * @param request
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月18日 上午9:09:09
	 */
	@RequestMapping(value = "/deleRole/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@RequiresPermissions("system:role:delete")
	public Map<String, Object> deleteRole(@PathVariable Integer id,HttpServletRequest request){
		try {
			if(null==id){
				return ApiResult.paramNull("id");
			}
			Role rol=roleService.findByPrimaryKey(id);
			if (null == rol) {
				return ApiResult.paramInvalid("角色不存在");
			}
			if(rol.getIsDefault()){
				return ApiResult.paramInvalid("此角色是系统内置角色，不能删除");
			}
			//删除角色 也把对应的权限删除
			roleService.deleteRole(id);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error("删除角色业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		}catch (Exception e) {
			log.error("删除角色发生内部错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
		
	}
	
	/**
	 * 
	 * @Title: getRolePermis 
	 * @Description: 查询当前角色并把这个角色的所有权限用树的格式传给前台
	 * @param id 角色主键
	 * @param parentId 父节点
	 * @param request
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月24日 下午2:56:26
	 */
	@RequestMapping(value = "/getRolePermis", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getRolePermisa(Integer id,HttpServletRequest request){
		//查询当前的角色
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Role role=roleService.findByPrimaryKey(id);
			List<Permission> perList = permissionService.selectRoleCurrent(id);
			List<TreeNode> treeList = TreeMenu(perList ,0);
			map.put("code", "200");
			map.put("data", treeList);
			map.put("roles", role);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("exception", e.getMessage());	
		}
		return map;
	}
	
	/**
	 * 
	 * @Title: TreeMenu 
	 * @Description: 
	 * @param perList
	 * @param parentId
	 * @return  
	 * @return List<TreeNode>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月24日 下午3:12:27
	 */
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
			 CheckState state  = new CheckState() ; 
			 state.setChecked(permission.getChecked());
			treeModel.setState(state);
			treeModel.setChecked(permission.getChecked());
			if (parentId == permission.getParentId()) {
				List<TreeNode> treeNodes = TreeMenu(perList,
						permission.getId());
				treeModel.setNodes(treeNodes);
				listTree.add(treeModel);
			}
		}
		return listTree;
	}
}
