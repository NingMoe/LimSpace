package com.tyiti.easycommerce.entity;

/**
 * 给角色授权的权限
 * t_role_perm
 * @author rainyhao
 * @since 2016-4-11 下午2:39:39
 */
public class RolePerm {
    private Integer id;

    private Integer roleId;

    private Integer permId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermId() {
        return permId;
    }

    public void setPermId(Integer permId) {
        this.permId = permId;
    }
}