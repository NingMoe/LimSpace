package com.tyiti.easycommerce.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tyiti.easycommerce.entity.constants.Status;
import com.tyiti.easycommerce.util.mybatis.BaseVO;

/**
 * 系统管理端用户
 * 表t_system_user
 * @author rainyhao
 * @since 2016-4-11 下午2:22:07
 */
@JsonIgnoreProperties("pwd")
public class SystemUser extends BaseVO implements Serializable {
    // 登录名
    private String userName;
    // 登录密码
    private String pwd;
    // 姓名
    private String name;
    // 联系电话
    private String tel;
    // 状态
    private Status status;
    
    
    // add by hcy 
    private List<Role> roles;

    public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}