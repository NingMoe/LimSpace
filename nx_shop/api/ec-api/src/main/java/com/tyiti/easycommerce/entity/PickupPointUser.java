package com.tyiti.easycommerce.entity;

import java.util.Date;
/**
 * 
* @ClassName: PickupPointUser 
* @Description:  
* @author hcy 
* @date 2016年6月29日 下午5:14:04
 */
public class PickupPointUser {
    private Integer id;

    private String loginName;

    private String password;

    private Integer type;

    private String pickupPointId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer invalid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPickupPointId() {
        return pickupPointId;
    }

    public void setPickupPointId(String pickupPointId) {
        this.pickupPointId = pickupPointId == null ? null : pickupPointId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

	@Override
	public String toString() {
		return "PickupPointUser [id=" + id + ", loginName=" + loginName
				+ ", password=" + password + ", type=" + type
				+ ", pickupPointId=" + pickupPointId + ", status=" + status
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", invalid=" + invalid + "]";
	}
}