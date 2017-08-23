package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.ReportShare;
import com.tyiti.easycommerce.entity.Share;
import com.tyiti.easycommerce.entity.ShareExecl;
import com.tyiti.easycommerce.entity.ShareUserExecl;

public interface ShareDao {

	Integer addShare(Share share);

	Integer getShareCouponCount(Share share);

	Integer getRegCouponCount(Share share);
	
	Share selectByShare(Share share);

	List<Share> getListByShare(Share share);

	List<ReportShare> getShareAndUserList(Share share);
	
	List<ShareExecl> shareToExecl(Share share);
	
	List<ShareUserExecl> shareUserToExecl(Share share);
	
	Integer getShareAndUserCount(Share share);
}