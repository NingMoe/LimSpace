package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.Advert;
import com.tyiti.easycommerce.repository.AdvertDao;
import com.tyiti.easycommerce.repository.BannerDao;
import com.tyiti.easycommerce.service.AdvertService;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("advertService")
public class AdvertServiceImpl  implements AdvertService{

	@Autowired
	private AdvertDao advertDao ;

	@Autowired
	private BannerDao bannerDao ; 
	@Override
	public void addAdvert(Advert advert) {
		// TODO Auto-generated method stub
		if(advert.getName()==null || advert.getName().equals("")){
			throw new CommonException("名称为空");
		}
		advert.setCreateTime( new Date());
		advert.setInvalid(0);
		advertDao.insertSelective(advert);
	}

	@Override
	public List<Advert> selectAdverts() {
		// TODO Auto-generated method stub
		return advertDao.selectAdvertList();
	}

	@Override
	public void editAdvert(Advert advert) {
		// TODO Auto-generated method stub
		Advert advertDB = advertDao.selectByPrimaryKey(advert.getId());
		if(advertDB == null){
			throw new CommonException("修改主键不存在");
		}
		advertDao.updateByPrimaryKeySelective(advert);
	}

	@Override
	public void delAdvert(Integer id) {
		// TODO Auto-generated method stub
		Advert advertDB = advertDao.selectByPrimaryKey(id);
		if(advertDB == null){
			throw new CommonException("修改主键不存在");
		} else{
			advertDB.setInvalid(1);
		}
		List<Map<String, Object>> bannerList = bannerDao.selectBannerList(id);
		if(bannerList.size()>0){
			throw new CommonException("请先删除banner");
		}
		advertDao.updateByPrimaryKeySelective(advertDB);
	}

	@Override
	public Advert selectAdvert(Integer id) {
		Advert advert =	advertDao.selectByPrimaryKey(id);		
		if(advert == null){
			throw new CommonException("主键 id 不存在");
		}
		return  advert ; 
	} 
}
