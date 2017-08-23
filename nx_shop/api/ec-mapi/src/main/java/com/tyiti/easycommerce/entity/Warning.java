package com.tyiti.easycommerce.entity;

import java.util.List;

public class Warning{

	private Integer id; 
	private String name; 
	private Integer invalid; 
	private String warningType;
	private List<WarningUser> userList;
	private Integer inventory;
	private List<Integer> skuIdList;
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
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}
	public String getWarningType() {
		return warningType;
	}
	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}
	public List<WarningUser> getUserList() {
		return userList;
	}
	public void setUserList(List<WarningUser> userList) {
		this.userList = userList;
	}
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public List<Integer> getSkuIdList() {
		return skuIdList;
	}
	public void setSkuIdList(List<Integer> skuIdList) {
		this.skuIdList = skuIdList;
	}
	
}