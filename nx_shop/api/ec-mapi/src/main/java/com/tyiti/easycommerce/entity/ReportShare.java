package com.tyiti.easycommerce.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportShare {
	private Integer sUserId;


	public Integer getsUserId() {
		return sUserId;
	}

	public void setsUserId(Integer sUserId) {
		this.sUserId = sUserId;
	}

	private String sMobile;
	private Integer shareNum;
	private Integer couponNum;
	// 分享时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date sCreateTime;
	// 分享人列表
	private List<ReportShareUser> shareUserList;

	public String getsMobile() {
		return sMobile;
	}

	public void setsMobile(String sMobile) {
		this.sMobile = sMobile;
	}

	

	public Integer getShareNum() {
		return shareNum;
	}

	public void setShareNum(Integer shareNum) {
		this.shareNum = shareNum;
	}

	public Integer getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	public Date getsCreateTime() {
		return sCreateTime;
	}

	public void setsCreateTime(Date sCreateTime) {
		this.sCreateTime = sCreateTime;
	}

	public List<ReportShareUser> getShareUserList() {
		return shareUserList;
	}

	public void setShareUserList(List<ReportShareUser> shareUserList) {
		this.shareUserList = shareUserList;
	}

}