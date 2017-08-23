package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Banner;

public interface BannerDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Banner record);

    int insertSelective(Banner record);

    Banner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Banner record);

    int updateByPrimaryKey(Banner record);

	int updateSortOtherUp(Banner banner);
	
	int updateSortOtherDown(Banner banner);
	
	int updateSortOwn(Banner banner);

	List<Map<String, Object>> selectBannerList(Integer adId);

	int maxRank(Banner banner);

	int delBanner(Integer id);


}