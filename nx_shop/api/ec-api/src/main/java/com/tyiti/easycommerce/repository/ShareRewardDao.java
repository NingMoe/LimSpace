package com.tyiti.easycommerce.repository;

import com.tyiti.easycommerce.entity.ShareReward;

public interface ShareRewardDao {

	/**
	 * 添加奖励记录
	 * 
	 * @param shareReward
	 * @return
	 */
	Integer add(ShareReward shareReward);
}