package com.tyiti.easycommerce.entity;

import java.util.Date;

public class User {
    private Integer id;

    private String mobile;

    private String realName;

    private String idCard;

    private String password;

    private String payPassword;

    private int invalid;

    private int status;

    private String recByCode;

    private String recCode;

    private Date createTime;

    private Date updateTime;

    private int thirdPartyType;

    private String thirdPartyId;

    private String openId;

    private Integer companyInfoId;

    /**wyy 2016/07/15 */
  	/*头像*/
  	private String  headImg;
  	/*昵称*/
  	private String  nickName;
  	/*生日*/
  	private Date  birthday ;
  	/*性别*/
  	private Integer gender;	
    
	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public int getInvalid() {
		return invalid;
	}

	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRecByCode() {
		return recByCode;
	}

	public void setRecByCode(String recByCode) {
		this.recByCode = recByCode;
	}

	public String getRecCode() {
		return recCode;
	}

	public void setRecCode(String recCode) {
		this.recCode = recCode;
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

	public int getThirdPartyType() {
		return thirdPartyType;
	}

	public void setThirdPartyType(int thirdPartyType) {
		this.thirdPartyType = thirdPartyType;
	}

	public String getThirdPartyId() {
		return thirdPartyId;
	}

	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getCompanyInfoId() {
		return companyInfoId;
	}

	public void setCompanyInfoId(Integer companyInfoId) {
		this.companyInfoId = companyInfoId;
	}

     
}