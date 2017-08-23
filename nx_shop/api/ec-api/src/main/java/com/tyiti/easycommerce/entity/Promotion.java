package com.tyiti.easycommerce.entity;

import java.util.Date;

public class Promotion {
	 
	private Integer id;
	private String name;
	private Integer rate;
	private String description;
	private Date expireTime;
	private Integer times;

	private Date updateTime ; 
	
	private Integer invalid ; 
	private Integer status;

	private Integer type ; 
	private Integer newcomer ; 
	
	private Integer minHit ;
	private Integer maxHit ; 
	
	
	private Date beginTime;

	private String code;
	
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private Date createTime ; 
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
	public Integer getMinHit() {
		return minHit;
	}
	public void setMinHit(Integer minHit) {
		this.minHit = minHit;
	}
	public Integer getMaxHit() {
		return maxHit;
	}
	public void setMaxHit(Integer maxHit) {
		this.maxHit = maxHit;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getNewcomer() {
		return newcomer;
	}
	public void setNewcomer(Integer newcomer) {
		this.newcomer = newcomer;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
 
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}

}
