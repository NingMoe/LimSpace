package com.tyiti.easycommerce.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegForm {
	@NotNull(message = "{mobile.NotNull}")
	@Pattern(regexp = "1[3578]\\d{9}", message = "{mobile.Pattern}")
	private String mobile;

	@NotNull(message = "{realName.NotNull}")
	@Size(min = 2, max = 16, message = "{realName.Size}")
	private String realName;
	
	@NotNull(message = "{idCard.NotNull}")
	@Pattern(regexp = "[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}", message = "{idCard.Pattern}")
	private String idCard;
	
	@NotNull(message = "{password.NotNull}")
	@Size(min = 6, max = 32, message = "{password.Size}")
	private String password;
	
	@NotNull(message = "{verifyCode.NotNull}")
	@Pattern(regexp = "\\d{6}", message = "{verifyCode.Pattern}")
	private String verifyCode;
	
	@Pattern(regexp = "1[3578]\\d{9}", message = "{recByCode.Pattern}")
	private String recByCode;

	public String getRecByCode() {
		return recByCode;
	}

	public void setRecCode(String recByCode) {
		this.recByCode = recByCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
}
