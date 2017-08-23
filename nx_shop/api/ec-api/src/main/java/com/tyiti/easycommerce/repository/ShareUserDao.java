package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.ShareUser;

public interface ShareUserDao {

	Integer addShareUser(ShareUser shareUser);

	ShareUser selectByShareUser(ShareUser shareUser);

	List<ShareUser> getListByShareUser(ShareUser shareUser);

	Integer getCountByShareUser(ShareUser shareUser);

}