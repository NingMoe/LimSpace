package com.tyiti.easycommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tyiti.easycommerce.util.mybatis.BaseVO;

/**
 * 权限信息
 * t_permission
 * @author rainyhao
 * @since 2016-4-11 下午2:39:57
 */
@JsonIgnoreProperties({"createTime"})
public class Permission extends BaseVO {
	
	public Permission() {
		
	}
	
	public Permission(String name, String code, String describ, Integer parentId) {
		super();
		this.name = name;
		this.code = code;
		this.describ = describ;
		this.parentId = parentId;
	}
	/**
	 * @author hcy
	 * <p>Description: </p>
	 * @param name
	 * @param code
	 * @param describ
	 * @param parentId
	 * @param url
	 */
	public Permission(String name, String code, String describ, Integer parentId,String url) {
		super();
		this.name = name;
		this.code = code;
		this.describ = describ;
		this.parentId = parentId;
		this.url = url;
	}

	// 权限名
    private String name;
    // shiro的权限代码
    private String code;
    // 描述
    private String describ;
    // 上级权限
    private Integer parentId;
    
    // 以下为不参与持久化的sql查询参数
    
    // 查询参数: 用户id, 用于加载某用户的所有权限
    private Integer userId;
    // 查询参数: 角色id, 用于加载某角色的所有权限
    private Integer roleId;
    
    // 结果参数: 是否已授权
    private Boolean checked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ == null ? null : describ.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}