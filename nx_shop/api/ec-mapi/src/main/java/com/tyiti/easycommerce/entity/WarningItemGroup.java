package com.tyiti.easycommerce.entity;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author wyy
 * @description 报警项发送给哪些报警组
 *
 */
public class WarningItemGroup{	
	private Integer id; 
    private Integer warningItemId;
    private Integer warningGroupId;
	private Integer invalid; 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime; 
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWarningItemId() {
		return warningItemId;
	}
	public void setWarningItemId(Integer warningItemId) {
		this.warningItemId = warningItemId;
	}
	public Integer getWarningGroupId() {
		return warningGroupId;
	}
	public void setWarningGroupId(Integer warningGroupId) {
		this.warningGroupId = warningGroupId;
	}
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
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
	
}