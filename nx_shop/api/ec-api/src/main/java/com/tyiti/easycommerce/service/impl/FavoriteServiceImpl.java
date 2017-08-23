package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Favorite;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.FavoriteDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService {
	
	@Autowired
	private FavoriteDao favoriteDao;
	
	@Autowired
	private SkuDao skuDao;

	@Override
	public List<Sku> getSkusByUserId(HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer userId = user.getId();
		List<Integer> SkuIds = favoriteDao.getSkuIdsByUserId(userId);
		List<Sku> skus = new ArrayList<Sku>();
        for(int i = 0; i < SkuIds.size(); i++){ 
        	Sku sku = skuDao.getById(SkuIds.get(i));
        	if(sku!=null){
        		sku.setIsFavorite(true);
        		skus.add(sku);       		
        	}
        }  
		return skus;
	}
	
	@Override
	public Boolean addOrCancel (Integer skuId,HttpSession session){
		User user = (User) session.getAttribute(Constants.USER);
		Integer userId = user.getId();
		
		Integer count = favoriteDao.add(skuId,userId);
		if(count<1){
			return favoriteDao.updateStatus(skuId, userId)>0 ;
		}
		return true;
	}

	@Override
	public Boolean addFavoriteFromCart(Favorite favorite, HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		Integer userId = user.getId();
		
		favoriteDao.add(favorite.getSkuId(),userId);
		return true;
	}
}
