package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Banner;

public interface BannerService {

	int insertBanner(Banner banner);

	int updateBanner(Banner banner);

	int delBanner(Integer id);

	int sortBanner(Banner banner);


	List<Map<String,Object>> bannerList(Integer adId);

	Banner selectBanner(Integer id);


}
