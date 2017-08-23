package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.PickupPoint;

public interface PickupPointDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PickupPoint record);

    int insertSelective(PickupPoint record);

    PickupPoint selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PickupPoint record);

    int updateByPrimaryKey(PickupPoint record);

    /**
     * 
    * @Title: selectByPickupPointList
    * @Description:  如果是多个营业厅id 的话 判断 是否有不存在的 ，如果有不存在添加失败
    * @return Integer    返回类型
    * @author Yan Zuoyu
    * @throws
     */
	Integer selectByPickupPointList(String[] pickupPointIds);

	List<PickupPoint> selectList();

	PickupPoint checkName(String name);
}