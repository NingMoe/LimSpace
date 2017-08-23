package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Cart {
    private Integer id;

    private Integer status;

    private Date createTime;

    private Integer skuId;

    private Integer userId;
    
    private Integer count;
    
    private BigDecimal price;
    
    private Integer installmentMonths ; 
    
    private Date updateTime ;  
    
    private Integer invalid ; 
    
    private Integer change ; //判断是否需要增加购物车数量
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

 

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getInstallmentMonths() {
		return installmentMonths;
	}

	public void setInstallmentMonths(Integer installmentMonths) {
		this.installmentMonths = installmentMonths;
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

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Integer getChange() {
		return change;
	}

	public void setChange(Integer change) {
		this.change = change;
	}


	private Sku sku ; 
	 
}