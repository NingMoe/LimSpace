package com.tyiti.easycommerce.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Banner;


public interface BannerDao
{

	List<Banner> getBannerByAdId(@Param("adId") Integer adId);
}