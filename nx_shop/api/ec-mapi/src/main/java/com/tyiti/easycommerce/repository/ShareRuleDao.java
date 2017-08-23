package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.ShareRule;

public interface ShareRuleDao {

	int addShareRule(ShareRule shareRule);

	ShareRule selectByShareRule(ShareRule shareRule);

	List<ShareRule> getListByShareRule(ShareRule shareRule);

}