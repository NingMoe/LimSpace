package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.Banner;

public interface AdvertService {

	List<Banner> getBannerListByCode(String code);
	
	Map<String, Object> getBannerListByGroupOrCode(String group,String code);

}
