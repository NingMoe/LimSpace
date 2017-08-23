package com.tyiti.easycommerce.entity;

import com.tyiti.easycommerce.entity.constants.Status;
import com.tyiti.easycommerce.util.mybatis.BaseVO;

/**
 * 角色信息
 * t_role
 * @author rainyhao
 * @since 2016-4-11 下午2:33:46
 */
public class Role extends BaseVO {
    // 角色名(中文名)
    private String name;
    // shiro权限代码
    private String code;
    // 是否为系统内置角色
    private Boolean isDefault;
    // 状态
    private Status status;
    
    // 以下为不参与持久化的sql查询参数
    
    // 查询参数: 用户id, 用于按用户加载其所分配的角色
    private Integer userId;
    
    // 结果参数:是否已授权给用户
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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}