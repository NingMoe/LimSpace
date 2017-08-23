package com.tyiti.easycommerce.entity;

public class Credit {

	// 会员类型 A1:工作（上班族） A2:学生
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// 是否显示未通过 A1:是（所有基础授信项显示未通过） A2:否（按原逻辑处理）
	private String isDisplayNopass;

	public String getIsDisplayNopass() {
		return isDisplayNopass;
	}

	public void setisDisplayNopass(String isDisplayNopass) {
		this.isDisplayNopass = isDisplayNopass;
	}

	// 用户推荐数量
	private String userRecomNum;

	public String getUserRecomNum() {
		return userRecomNum;
	}

	public void setUserRecomNum(String userRecomNum) {
		this.userRecomNum = userRecomNum;
	}

	// 个人信息Id
	private String memberInfoId;

	public String getMemberInfoId() {
		return memberInfoId;
	}

	public void setMemberInfoId(String memberInfoId) {
		this.memberInfoId = memberInfoId;
	}

	// 个人信息状态
	private String memberInfoState;

	public String getMemberInfoState() {
		return memberInfoState;
	}

	public void setMemberInfoState(String memberInfoState) {
		this.memberInfoState = memberInfoState;
	}

	// 工作信息Id
	private String jobInfoId;

	public String getJobInfoId() {
		return jobInfoId;
	}

	public void setJobInfoId(String jobInfoId) {
		this.jobInfoId = jobInfoId;
	}

	// 工作信息状态
	private String jobInfoState;

	public String getJobInfoState() {
		return jobInfoState;
	}

	public void setJobInfoState(String jobInfoState) {
		this.jobInfoState = jobInfoState;
	}

	// 联系人信息Id
	private String contactsInfoId;

	public String getContactsInfoId() {
		return contactsInfoId;
	}

	public void setContactsInfoId(String contactsInfoId) {
		this.contactsInfoId = contactsInfoId;
	}

	// 联系人信息状态
	private String contactsInfoState;

	public String getContactsInfoState() {
		return contactsInfoState;
	}

	public void setContactsInfoState(String contactsInfoState) {
		this.contactsInfoState = contactsInfoState;
	}
}