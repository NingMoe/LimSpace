package com.tyiti.easycommerce.util.mybatis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tyiti.easycommerce.entity.Role;
import com.tyiti.easycommerce.entity.SystemUser;

/**
 * 所有MyBatis映射实体要继承的父类
 * 按正常人对表中列类型定义时继承这个类
 * 如果是不正常设计的话就不要继承了
 * @author rainyhao
 * @since 2015-5-20 上午9:32:13
 */
@JsonIgnoreProperties({"fields", "pager"})
public abstract class BaseVO {
	
	// id
	private Integer id;
	// 创建时间
	private Date createTime;
	
	
	// Transient 查询字段, 查询辅助性属, 不入库性
	private String fields;
	// 分页及排序信息
	private Pager pager;
	
	
	//角色传过来的串
	private String roleIds;
	
	//每个权限对应的什么操作
	public String url;
	
	private String glyphicon;
	
	
	private List<Role> roles=new ArrayList<Role>(); 
	
	
	private String permIds;
	
	
	//新密码
	private String newPass;
	
	
	public String getNewPass() {
		return newPass;
	}
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	public String getPermIds() {
		return permIds;
	}
	public void setPermIds(String permIds) {
		this.permIds = permIds;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public SystemUser getSystemUser() {
		return systemUser;
	}
	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}
	private SystemUser systemUser;
	
	public String getGlyphicon() {
		return glyphicon;
	}
	public void setGlyphicon(String glyphicon) {
		this.glyphicon = glyphicon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}
}
