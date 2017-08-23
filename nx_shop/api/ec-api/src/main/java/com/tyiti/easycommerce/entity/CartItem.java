package com.tyiti.easycommerce.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CartItem {
	private int id;
	private Integer status;
	private Integer will_buy;
	private Date current;
	private Integer sku_id;
	private Integer cust_id;
	private Integer num;
	private String skuName;
	private BigDecimal price;
	private String image;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public CartItem() {
		super();
	}
	public CartItem(int id, Integer status, Integer will_buy, Date current,
			Integer sku_id, Integer cust_id) {
		super();
		this.id = id;
		this.status = status;
		this.will_buy = will_buy;
		this.current = current;
		this.sku_id = sku_id;
		this.cust_id = cust_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getWill_buy() {
		return will_buy;
	}
	public void setWill_buy(Integer will_buy) {
		this.will_buy = will_buy;
	}
	public Date getCurrent() {
		return current;
	}
	public void setCurrent(Date current) {
		this.current = current;
	}
	public Integer getSku_id() {
		return sku_id;
	}
	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}
	public Integer getCust_id() {
		return cust_id;
	}
	public void setCust_id(Integer cust_id) {
		this.cust_id = cust_id;
	}
	

}
