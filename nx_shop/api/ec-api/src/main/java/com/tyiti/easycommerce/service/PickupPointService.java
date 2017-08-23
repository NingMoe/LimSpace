package com.tyiti.easycommerce.service;

import java.util.List;

import com.tyiti.easycommerce.entity.PickupPoint;

public interface PickupPointService {
	/**
	* @Title: getPickupPointList
	* @Description: 获取所有营业厅地址
	* @return List<PickupPoint>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	List<PickupPoint> getPickupPointList();
	/**
	 *
	* @Title: selectPickupPointById
	* @Description: 根据主键获取 id
	* @return PickupPoint    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	PickupPoint selectPickupPointById(Integer id);

}
