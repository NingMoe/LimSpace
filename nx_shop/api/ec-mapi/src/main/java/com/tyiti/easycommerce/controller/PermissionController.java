package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tyiti.easycommerce.entity.Permission;
import com.tyiti.easycommerce.repository.PermissionDao;
import com.tyiti.easycommerce.service.PermissionService;
import com.tyiti.easycommerce.util.TreeNode;
import com.tyiti.easycommerce.util.XString;
import com.tyiti.easycommerce.util.exception.ServiceException;
import com.tyiti.easycommerce.util.spring.ApiResult;
import com.tyiti.easycommerce.util.spring.BaseController;

/**
 * 权限相关api
 * @author rainyhao
 * @since 2016-4-12 下午9:59:07
 */
@RestController
public class PermissionController extends BaseController {
	
	@Autowired
	private PermissionService permissionService;
	
	
	@Autowired
	private  PermissionDao  permissionDao;
	
	/**
	 * 添加权限项
	 * @author rainyhao
	 * @since 2016-4-12 下午10:15:10
	 * @param name
	 * @param code
	 * @param describ
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/perm/add", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system:authority:add")
	public Map<String, Object> add(String name, String code, String describ, Integer parentId,String url,
			HttpServletRequest request) {
		if (XString.isNullOrEmpty(name)) {
			return ApiResult.paramNull("name");
		}
		if (XString.isNullOrEmpty(code)) {
			return ApiResult.paramNull("code");
		}
		if (null == parentId) {
			return ApiResult.paramNull("parentId");
		}
		if (XString.isNullOrEmpty(code)) {
			return ApiResult.paramNull("url");
		}
		Permission perm = new Permission(name, code, describ, parentId,url);
		try {
			permissionService.add(perm);
			return ApiResult.success(perm);
		} catch (ServiceException e) {
			log.error("添加权限发生业务错误:" + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("添加权限发生系统错误:" + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 删除权限
	 * @authro rainyhao
	 * @since 2016-4-13 上午9:08:46
	 * @param id 必选, 权限id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/perm/delete")
	@RequiresPermissions("system:authority:delete")
	public Map<String, Object> delete(Integer id, HttpServletRequest request) {
		if (null == id) {
			return ApiResult.paramNull("id");
		}
		try {
			permissionService.delete(id);
			return ApiResult.success();
		} catch (ServiceException e) {
			log.error("删除权限发生业务错误: " + e.getMessage() + ", " + super.getQueryString(request));
			return ApiResult.paramInvalid(e);
		} catch (Exception e) {
			log.error("删除权限发生系统错误: " + super.getQueryString(request), e);
			return ApiResult.sysError();
		}
	}
	
	/**
	 * 
	 * @Title: preList 
	 * @Description: 菜单树
	 * @param parentId
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月17日 下午5:23:33
	 */
	
	
	@RequestMapping(value = "/pre/tree", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> preList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Permission permWhere = new Permission();
			List<Permission> lstPerm = permissionDao.selectForList(permWhere);
			List<TreeNode> treeList = TreeMenu(lstPerm ,0);
			map.put("code", "200");
			map.put("data", treeList);
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
	 * 
	 * @Title: update 
	 * @Description: 修改权限信息 跟角色没有关联
	 * @param request
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月20日 下午4:23:17
	 */
	@RequestMapping(value = "/perm/update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system:authority:update")
	public Map<String, Object> update(@RequestBody Permission permission, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			permissionService.updatePer(permission);
			map.put("code", "200");
			map.put("message", "ok");
		} catch (Exception e) {
			map.put("code", "400");
			map.put("exception", e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 
	 * @Title: getPerm 
	 * @Description: 根据主键查询权限
	 * @param permission
	 * @param request
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年5月20日 下午5:43:16
	 */
	@RequestMapping(value = "/perm/get", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPerm(Integer id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null==id) {
			return ApiResult.paramNull("id");
		}
		Permission p=permissionService.findByPrimary(id);
		try {
			map.put("code", "200");
			map.put("data", p);
		} catch (Exception e) {
			map.put("code", "400");
			map.put("exception", e.getMessage());
		}
		return map;
	}
	
	
	
	
	
	
	
	
	
	
}
