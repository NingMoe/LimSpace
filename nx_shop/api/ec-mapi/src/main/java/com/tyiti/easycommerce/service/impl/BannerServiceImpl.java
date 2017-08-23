package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.entity.Banner;
import com.tyiti.easycommerce.repository.BannerDao;
import com.tyiti.easycommerce.service.BannerService;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("bannerService")
public class BannerServiceImpl implements BannerService{

	@Autowired
	private BannerDao bannerDao ; 
	@Override
	public int insertBanner(Banner banner) {
		// TODO Auto-generated method stub
		banner.setCreateTime(new Date());
		int rank = bannerDao.maxRank(banner);
		banner.setRank(rank+1);
		banner.setInvalid(0);
		return bannerDao.insertSelective(banner);
	}
	@Override
	public int updateBanner(Banner banner) {
		banner.setUpdateTime(new Date());
		// TODO Auto-generated method stub
		return bannerDao.updateByPrimaryKeySelective(banner);
	}
	@Override
	@Transactional
	public int delBanner(Integer id) {
		// TODO Auto-generated method stub
		//删除 后将排序处理
		Banner	bannerOwn  = bannerDao.selectByPrimaryKey(id);
		bannerOwn.setToRank(bannerDao.maxRank(bannerOwn));
		bannerDao.updateSortOwn(bannerOwn);
		return bannerDao.delBanner(id);
	}
	@Transactional
	public int sortBanner(Banner banner) {
		Banner	bannerOwn  = bannerDao.selectByPrimaryKey(banner.getId());
		bannerOwn.setToRank(banner.getToRank());
		if(banner.getToRank()==bannerOwn.getRank()){
			return 1 ;
		}
		if(banner.getToRank()<bannerOwn.getRank()){
			//向上排序
			try {
				bannerOwn.setToRank(banner.getToRank());
				int other = bannerDao.updateSortOtherUp(bannerOwn);
				if(other !=0){
					int	own = bannerDao.updateSortOwn(bannerOwn);
					if(own !=1){
						throw new CommonException("排序不成功");
					}
				}else {
					throw new CommonException("排序不成功");
				}
			} catch (Exception e) {
				throw new CommonException("排序不成功");
			}
		}else {
			try {
				//向下排序
				bannerOwn.setToRank(banner.getToRank());
				int other = bannerDao.updateSortOtherDown(bannerOwn);
				if(other !=0){
					int	own = bannerDao.updateSortOwn(bannerOwn);
					if(own !=1){
						throw new CommonException("排序不成功");
					}
				}else {
					throw new CommonException("排序不成功");
				}
			} catch (Exception e) {
				throw new CommonException("排序不成功");
			}
		}
		
		return  1;
	}
	@Override
	public List<Map<String,Object>> bannerList(Integer adId) {
		// TODO Auto-generated method stub
		
		return bannerDao.selectBannerList(adId);
	}
	@Override
	public Banner selectBanner(Integer id) {
		// TODO Auto-generated method stub
		return bannerDao.selectByPrimaryKey(id);
	}

}
