package com.tyiti.easycommerce.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tyiti.easycommerce.entity.Cart;

public interface CartDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    
    List<Cart> selectBySkuAndCust(Map<String, Object> map);
    
    Cart selectMyCart(Map<String, Object> map);

	Cart selectCartBySku(@Param("skuId") Integer skuId,@Param("userId") Integer userId);

	void updateAllStatus(@Param("userId") Integer userId,@Param("status") Integer status);

}