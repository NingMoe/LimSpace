package com.tyiti.easycommerce.entity;

public class MemberInfo {

	// 个人信息ID(必须)
	private String id;
	// 姓名
	private String name;
	// 身份证号
	private String cardNumber;
	// 常住省
	private String province;
	// 常住市
	private String city;
	// 常住县
	private String county;
	// 常住地址
	private String address;
	// A1:草稿 A2:已退回 A3:审核中 A4:审核通过
	private String verifyState;
	// 地理位置
	private String geolocation;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVerifyState() {
		return verifyState;
	}

	public void setVerifyState(String verifyState) {
		this.verifyState = verifyState;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

}