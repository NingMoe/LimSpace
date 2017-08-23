package com.tyiti.easycommerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Advert;
import com.tyiti.easycommerce.entity.Banner;
import com.tyiti.easycommerce.repository.AdvertDao;
import com.tyiti.easycommerce.repository.BannerDao;
import com.tyiti.easycommerce.service.AdvertService;
import com.tyiti.easycommerce.util.CommonException;

@Service("advertService")
public class AdvertServiceImpl implements AdvertService {

	@Autowired
	private AdvertDao advertDao;

	@Autowired
	private BannerDao bannerDao;

	@Override
	public List<Banner> getBannerListByCode(String code) {
		// TODO Auto-generated method stub
		Advert advert = advertDao.getAdvertByCode(code);
		if (advert == null) {
			throw new CommonException("无此广告位");
		}
		return bannerDao.getBannerByAdId(advert.getId());
	}

	/**
	 * @author wyy 2016/06/29
	 * @Description 根据分组或者编码获取 Banner
	 * @param group 分组
	 * @param code  编码
	 */
	@Override
	public Map<String, Object> getBannerListByGroupOrCode(String group,
			String code) {
		List<Advert> advertList = advertDao.getAdvertByGroupOrCode(group, code);
		if (advertList.size() < 1) {
			throw new CommonException("无此广告位");
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		// 循环遍历分组
		for (Advert advert : advertList) {
			data.put(advert.getCode(),
					bannerDao.getBannerByAdId(advert.getId()));
		}
		
		return data;
	}

}
