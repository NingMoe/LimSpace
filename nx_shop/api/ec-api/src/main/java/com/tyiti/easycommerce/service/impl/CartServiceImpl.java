package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Cart;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.ActivitySkuDao;
import com.tyiti.easycommerce.repository.CartDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.service.CartService;
import com.tyiti.easycommerce.util.JsonUtils;
import com.tyiti.easycommerce.util.Result;

/**
 * 
 * @ClassName: CartServiceImpl
 * @Description: 实现购物车增删改查
 * @author hcy
 * @date 2016年8月4日 下午1:54:39
 */
@Service
public class CartServiceImpl implements CartService {

	private Log logg = LogFactory.getLog(this.getClass());
	@Autowired
	private SkuDao skuDao;

	@Autowired
	private CartDao cartDao;

	@Autowired
	private CouponRecordDao couponRecordDao;

	@Autowired
	private ActivitySkuDao activitySkuDao ; 
	
	@Autowired
	private OrderSkuDao orderSkuDao ; 
	/**
	 * 添加购物车商品
	 */
	@Override
	public void addCartItem(Cart cart, HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(Constants.USER);
		Integer itemId = cart.getSkuId();
		Integer num = cart.getCount();
		Integer installment = cart.getInstallmentMonths();
		if (num == null) {
			num = 1;
		}
		if (user != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			map.put("listSkuId", new int[] { itemId });
			Cart myCart = cartDao.selectMyCart(map);

			if (null == myCart || !itemId.equals(myCart.getSkuId())) {
				Cart addCart = new Cart();
				addCart.setSkuId(itemId);
				addCart.setCount(num);
				addCart.setUserId(user.getId());
				addCart.setInstallmentMonths(installment);
				addCart.setStatus(0);
				cartDao.insertSelective(addCart);
			} else {
				if(cart.getChange() !=null){
					myCart.setCount( num);	
				}else{
					myCart.setCount(myCart.getCount() + num);	
				}
				if(installment !=null){
					myCart.setInstallmentMonths(installment);
				} 
				cartDao.updateByPrimaryKeySelective(myCart);
			}

		} else {
			// 取商品信息
			Cart cartItem = null;
			// 取购物车商品列表
			List<Cart> itemList = getCartItemList(request);
			// 判断购物车商品列表中是否存在此商品
			for (Cart cItem : itemList) {
				// 如果存在此商品
				if (cItem.getId().equals(itemId)) {
					// 增加商品数量
					if(cart.getChange() !=null){
						cItem.setCount( num);	
					}else{
						cItem.setCount(cItem.getCount() + num);	
					}
					cartItem = cItem;
					break;
				}
			}
			if (cartItem == null) {
				Sku sku = skuDao.getById(itemId);
				if (sku != null) {
					cartItem = new Cart();
					cartItem.setId(sku.getId());
					cartItem.setSku(sku);
					cartItem.setCount(num);
					cartItem.setInstallmentMonths(installment);
					cartItem.setSkuId(sku.getId());
					cartItem.setStatus(0);
					// 添加到购物车列表
					itemList.add(cartItem);
				}

			}
			// ①把购物车列表写入cookie
			// CookieUtils.setCookie(request, response, "CART",
			// JsonUtils.objectToJson(itemList), true);
			request.getSession().setAttribute(Constants.CART_INFO,
					JsonUtils.objectToJson(itemList));

			// ② 用session实现
		}
	}

	/**
	 * 从cookie中取商品列表
	 * <p>
	 * Title: getCartItemList
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @return
	 */
	private List<Cart> getCartItemList(HttpServletRequest request) {
		// ①从cookie中取商品列表
		// String cartJson = CookieUtils.getCookieValue(request, "CART", true);
		// ②从session中取得商品列表
		if (request.getSession().getAttribute(Constants.CART_INFO) != null) {
			String cartJson = String.valueOf(request.getSession().getAttribute(
					Constants.CART_INFO));
			if (cartJson == null || "".equals(cartJson)) {
				return new ArrayList<Cart>();
			}
			// 把json转换成商品列表
			try {
				List<Cart> list = JsonUtils.jsonToList(cartJson, Cart.class);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ArrayList<Cart>();
	}

	public List<Cart> getCartItemList(HttpServletRequest request,
			HttpServletResponse response) {

		User user = (User) request.getSession().getAttribute(Constants.USER);
		// 已登录
		if (user != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			List<Cart> list = cartDao.selectBySkuAndCust(map);
			if (list != null) {
				list = cartSku(list,user);// 转换一下
				return list;
			}
		} else {
			// ① 从cookie中取得
			// String cartJson = CookieUtils.getCookieValue(request, "CART",
			// true);
			// ② 从session中取得
			String cartJson = String.valueOf(request.getSession().getAttribute(
					Constants.CART_INFO));
			if (cartJson == null) {
				return new ArrayList<>();
			}
			// 把json转换成商品列表
			try {
				List<Cart> list = JsonUtils.jsonToList(cartJson, Cart.class);
				if (list != null) {
					list = cartSku(list,null);// 转换一下
					return list;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<>();
	}

	private List<Cart> cartSku(List<Cart> list, User user) {
		List<Cart> cartSku = new ArrayList<Cart>();
		for (Cart cart : list) {
			Integer skuId = cart.getSkuId();
			Sku sku = skuDao.getById(skuId);
			if(sku != null){
				ActivitySku activitySku = 	activitySkuDao.getActiveBySku(skuId);
				if(activitySku !=null)
				if( null!=user){
					List<Map<String, Object>> orderSkuList = orderSkuDao.findOrderSkuActivity(skuId,user.getId(),activitySku.getActivityId());
					if(orderSkuList!=null&&orderSkuList.size()>0&&orderSkuList.get(0)!=null){
						activitySku.setBuyNum( Integer.parseInt(String.valueOf( orderSkuList.get(0).get("skuCount"))));
					}else{
						activitySku.setBuyNum( 0);
					}
				}
				if(activitySku !=null){
					sku.setActiveSku(activitySku);
				}
				cart.setSku(sku);
			}
			cartSku.add(cart);
		}
		return cartSku;
	}

	@Override
	public Result deleteCartItem(Integer itemId, HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(Constants.USER);
		if (user != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			map.put("listSkuId", new int[] { itemId });
			Cart myCart = cartDao.selectMyCart(map);
			if (myCart != null) {
				myCart.setInvalid(1);
				cartDao.updateByPrimaryKeySelective(myCart);
			}
		} else {
			// 从cookie中取购物车商品列表
			List<Cart> itemList = getCartItemList(request);
			// 从列表中找到此商品
			for (Cart cartItem : itemList) {
				if (cartItem.getId().equals(itemId)) {
					itemList.remove(cartItem);
					break;
				}

			}
			// 把购物车列表重新写入cookie
			// CookieUtils.setCookie(request, response,
			// "CART",JsonUtils.objectToJson(itemList), true);
			// 把购物车列表重新放进session中
			request.getSession().setAttribute(Constants.CART_INFO,
					JsonUtils.objectToJson(itemList));
		}
		return Result.ok();
	}

	@Override
	public List<Cart> getCartList(Integer UserId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", UserId);
		// map.put("listSkuId",listSkuId);
		List<Cart> cartList = cartDao.selectBySkuAndCust(map);
		return cartList;
	}

	@Override
	public int insertSelective(List<Cart> listCart) {
		for (Cart c : listCart) {
			cartDao.insertSelective(c);
		}
		return 0;
	}

	@Override
	public void update(List<Cart> listCart) {
		// TODO Auto-generated method stub
		for (Cart cart : listCart) {
			cartDao.updateByPrimaryKeySelective(cart);
		}

	}

	@Override
	public void delete(List<Cart> lisCart) {
		// TODO Auto-generated method stub
		for (Cart c : lisCart) {
			c.setInvalid(1);
			cartDao.updateByPrimaryKeySelective(c);
		}

	}

	@Override
	public int showCouponRecord(HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		// 3.获取用户未到期、未使用的优惠券
		List<CouponRecord> couponRecordList = couponRecordDao.getByCustId(user
				.getId());
		return couponRecordList.size();
	}

	@Override
	public void updateByselect(Cart cart) {
		cartDao.updateByPrimaryKeySelective(cart);

	}

	@Override
	public void setChooseStatus(HttpServletRequest request,
			HttpServletResponse response, Integer skuId) {
		// ① 从cookie中取得
		if(request.getSession().getAttribute(Constants.CART_INFO) == null){
			return ;
		}
		String cartJson = String.valueOf(request.getSession().getAttribute(
				Constants.CART_INFO));
		List<Cart> cartList = new ArrayList<Cart>();
		if (cartJson != null) {
			List<Cart> list = JsonUtils.jsonToList(cartJson, Cart.class);
			for (Cart c : list) {
				if (c.getSkuId().equals(skuId)) {
					if (c.getStatus() == 0) {
						c.setStatus(1);
					} else {
						c.setStatus(0);
					}

				}
				cartList.add(c);
			}

		}
		// CookieUtils.setCookie(request, response, "CART",
		// JsonUtils.objectToJson(cartList), true);
		request.getSession().setAttribute(Constants.CART_INFO,
				JsonUtils.objectToJson(cartList));
	}

	@Override
	public void updateChooseStatus(Integer skuId, Integer userId) {
		// TODO Auto-generated method stub
		Cart cart = cartDao.selectCartBySku(skuId, userId);
		if (cart != null) {
			if (cart.getStatus() == 0) {
				cart.setStatus(1);
			} else {
				cart.setStatus(0);
			}
			cartDao.updateByPrimaryKeySelective(cart);
		}
	}

	@Override
	public void updateChooseAllStatus(Integer userId, Integer status) {
		cartDao.updateAllStatus(userId, status);
	}

	@Override
	public void setChooseAllStatus(HttpServletRequest request,
			HttpServletResponse response, Integer status) {
		if (request.getSession().getAttribute(Constants.CART_INFO) != null) {
			String cartJson = String.valueOf(request.getSession().getAttribute(
					Constants.CART_INFO));
			if (cartJson == null || "".equals(cartJson)) {
				return;
			}
			// 把json转换成商品列表
			List<Cart> cartList = new ArrayList<Cart>();
			try {
				List<Cart> list = JsonUtils.jsonToList(cartJson, Cart.class);
				for (Cart cart : list) {
					cart.setStatus(status);
					cartList.add(cart);
				}
				request.getSession().setAttribute(Constants.CART_INFO,
						JsonUtils.objectToJson(cartList));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void delLoseCart(HttpServletRequest request) {
		// TODO Auto-generated method stub
		User user = (User) request.getSession().getAttribute(Constants.USER);
		// 已登录
		if (user != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", user.getId());
			List<Cart> list = cartDao.selectBySkuAndCust(map);
			if (list != null) {
				list = cartSku(list,user);// 转换一下
				for (Cart cart : list) {
					//1.已删除的商品sku = null 2.活动中的商品 3.下架的商品  4.库存不足的商品   将被作为失效  删除
					if(cart.getSku()==null||cart.getSku().getActiveSku()!=null || cart.getSku().getStatus() ==0||cart.getSku().getInventory()<1){
						cart.setInvalid(1);
						cartDao.updateByPrimaryKeySelective(cart);
					}
				}
			}
		}
	}

	
}
