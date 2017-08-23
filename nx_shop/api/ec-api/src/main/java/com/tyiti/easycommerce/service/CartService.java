package com.tyiti.easycommerce.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tyiti.easycommerce.entity.Cart;
import com.tyiti.easycommerce.util.Result;

public interface CartService {
	List<Cart> getCartItemList(HttpServletRequest request, HttpServletResponse response);
	Result deleteCartItem(Integer itemId, HttpServletRequest request, HttpServletResponse response);
	List<Cart> getCartList(Integer userId);
	int insertSelective(List<Cart> listCart);
	void update(List<Cart> list);
	void delete(List<Cart> lisCart);
	int showCouponRecord(HttpSession session);
	void updateByselect(Cart cart);
	void addCartItem(Cart cart, HttpServletRequest request,
			HttpServletResponse response);
	void setChooseStatus(HttpServletRequest request,
			HttpServletResponse response, Integer skuId);
	void updateChooseStatus(Integer skuId,Integer userId);
	void updateChooseAllStatus(Integer id, Integer status);
	void setChooseAllStatus(HttpServletRequest request,
			HttpServletResponse response, Integer status);
	/**
	* @Title: delLoseCart
	* @Description: 删除失效商品
	* @return void    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	void delLoseCart(HttpServletRequest request);

}
