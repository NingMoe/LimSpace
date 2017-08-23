package com.tyiti.easycommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.Cart;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.service.CartService;
import com.tyiti.easycommerce.service.CouponService;
import com.tyiti.easycommerce.service.SkuService;
import com.tyiti.easycommerce.util.Result;

@Controller
public class CartController {
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CouponService couponService ;
	
	@Autowired
	private SkuService skuService;
	
	/**
	 * 
	 * @Title: addCartItem 
	 * @Description: 加入购物车
	 * @param itemId 商品id
	 * @param num 数量
	 * @param request
	 * @param response
	 * @return  
	 * @return String  
	 * @throws
	 * @author hcy
	 * @date 2016年8月4日 上午10:50:08
	 */
	@RequestMapping(value= "/carts",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addCartItem(@RequestBody Cart cart ,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			cartService.addCartItem(cart ,request, response);
			map.put("code", "200");
			map.put("data", "ok");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("code", "400");
			map.put("message", "操作失败！");
            map.put("exception", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 
	 * @Title: showCart 
	 * @Description: 展示购物车
	 * @param request
	 * @param response
	 * @param model
	 * @return  
	 * @return String  
	 * @throws
	 * @author hcy
	 * @date 2016年8月4日 上午10:51:56
	 */
	@RequestMapping(value="/carts",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> showCart(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String,Object> data=new HashMap<String, Object>();
		try {
			/**
			 * 分两种情况 登录前查看购物车（直接查看cookie）   登录后查看购物车(查询数据库的数据)
			 * 
			 */
			List<Cart> cartList = cartService.getCartItemList(request, response);
			//session.setAttribute("cartList", cartList);
			data.put("code", "200");
			data.put("data", cartList);
		} catch (Exception e) {
			data.put("code", "400");
			data.put("data", e.getMessage());
		}
		return data;
	}
	/**
	 * 
	 * @Title: deleteCartItem 
	 * @Description: 删除购物项
	 * @param itemId 商品id
	 * @param request
	 * @param response
	 * @return  
	 * @return String  
	 * @throws
	 * @author hcy
	 * @date 2016年8月4日 上午10:53:05
	 */
	@RequestMapping(value="/carts/{itemId}",method=RequestMethod.DELETE)
	@ResponseBody
	public Result deleteCartItem(@PathVariable Integer itemId, HttpServletRequest request, HttpServletResponse response) {
		Result result=cartService.deleteCartItem(itemId, request, response);
		return result;
	}
	
	
	/**
	 * 
	 * @Title: showCouponRecord 
	 * @Description: 去结算
	 * @param param 前台传过来的对象数组 JSON格式
	 * [{"id": 1, "num": 2},{"id": 2, "num": 4},{"id":3, "num": 2},{"id":4, "num": 4}]
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年8月11日 下午3:38:51
	 */
	@RequestMapping(value="/carts/showcouponnum",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> showCouponRecord(@RequestBody String param ,HttpSession session) {
		Map<String,Object> data=new HashMap<String, Object>();
		// 1.获取用户未到期、未使用的优惠券
		List<CouponRecord> couponRecords=new ArrayList<CouponRecord>();
		List<Sku> skuList=new ArrayList<Sku>();
		try {
			ObjectMapper objectMapper=new ObjectMapper();
			JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class, Sku.class);
			List<Sku> skus=objectMapper.readValue(param,javaType);
			User user = (User) session.getAttribute(Constants.USER);
			if(user==null){
				data.put("code", "400");
				data.put("data", "未登录！");
				return data;
			}
			for(Sku s:skus){
				Sku ss=skuService.getById(s.getId());
				for(int i=0;i<s.getNum();i++){
					skuList.add(ss);
				}
			}
			couponRecords=couponService.getAllCartCoupon(skus,user.getId());
			List<CouponRecord> list=new ArrayList<CouponRecord>();
			for(CouponRecord c:couponRecords){
			if(c.getAvailable()==1){
				list.add(c);
			}
		}	
			data.put("code", "200");
			data.put("size", list.size());
			data.put("skus", skuList);
		} catch (Exception e) {
			data.put("code", "400");
			data.put("data", e.getMessage());
		}
		return data;
	}
	
	
	/**
	 * 
	 * @Title: pitchOn 
	 * @Description: 购物车商品选中后 在返回来还是选中状态
	 * @param skuIds
	 * @return  
	 * @return Map<String,Object>  
	 * @throws
	 * @author hcy
	 * @date 2016年8月15日 下午5:15:26
	 */
	@RequestMapping(value="/carts/choose/{skuId}",method=RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> pitchOn(@PathVariable Integer skuId,HttpServletResponse response,HttpServletRequest request,HttpSession session){
		Map<String,Object> data=new HashMap<String, Object>();
		try {
			User user = (User) session.getAttribute(Constants.USER);
			if(user!=null){
				cartService.updateChooseStatus(skuId, user.getId());
			}else{
				cartService.setChooseStatus(request,response,skuId);

			}
			data.put("code", "200");
		} catch (Exception e) {
			data.put("code", "400");
			data.put("exception", e.getMessage());
		}
		return data;
		
	}
	/**
	* @Title: chooseAll
	* @Description:  全选反选
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	@RequestMapping(value="/carts/choose/all/{status}",method=RequestMethod.PUT)
	@ResponseBody
	public Map<String,Object> chooseAll(@PathVariable Integer status,HttpServletResponse response,HttpServletRequest request,HttpSession session){
		Map<String,Object> data=new HashMap<String, Object>();
		if(status !=1 && status !=0){
			data.put("code", "400");
			data.put("message", "参数错误");
			return data ;
		}
		try {
			User user = (User) session.getAttribute(Constants.USER);
			if(user!=null){
				cartService.updateChooseAllStatus(user.getId(),status);
			}else{
				cartService.setChooseAllStatus(request,response,status);

			}
			data.put("code", "200");
		} catch (Exception e) {
			data.put("code", "400");
			data.put("exception", e.getMessage());
		}
		return data;
		
	}
	
	@RequestMapping(value="/lose",method=RequestMethod.DELETE)
	@ResponseBody
	public Map<String,Object> delLostCart(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String,Object> data=new HashMap<String, Object>();
		try {
			cartService.delLoseCart(request);
			//session.setAttribute("cartList", cartList);
			data.put("code", "200");
			}catch(Exception e ){
				data.put("code", "400");
				data.put("message", "清空失败");
			}
		return data ; 
		}
}
