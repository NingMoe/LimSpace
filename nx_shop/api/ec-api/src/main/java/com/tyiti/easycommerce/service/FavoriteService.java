package com.tyiti.easycommerce.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.Favorite;
import com.tyiti.easycommerce.entity.Sku;

public interface FavoriteService {
	
	List<Sku> getSkusByUserId(HttpSession session);
	
	Boolean addOrCancel(Integer skuId,HttpSession session);

	Boolean addFavoriteFromCart(Favorite favorite, HttpSession session);
}