package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.Advert;

public interface AdvertService {


	void addAdvert(Advert advert);

	List<Advert> selectAdverts();

	void editAdvert(Advert advert);

	void delAdvert(Integer id);

	Advert selectAdvert(Integer id);

}
