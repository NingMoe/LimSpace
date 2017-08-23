package com.tyiti.easycommerce.util.ordersystem;

import java.math.BigDecimal;

public class Sku {
	private String no;
	private BigDecimal price;
	private Integer count;
	private Integer type; // 1:主产品，2:赠品
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}