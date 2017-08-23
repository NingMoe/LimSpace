package com.tyiti.easycommerce.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 */
public final class Constants {
	private Constants() {}

	public static final String USER = "_USER"; //session用户
	public static final String USER_OPENID = "_USER_OPENID"; //session用户
	public static final String THIRD_PARTY_ID = "_THIRD_PARTY_ID";

	public static final String LOGIN_RAND = "_LOGIN_RAND"; //注册随机码 
	/** 手机验证码*/
	public static final String MOBILE_RAND = "_MOBILE_RAND";
	/**注册验证码*/
	public static final String REGISTER_RAND="_REGISTER_RAND";
	/** 邮箱验证码 通过邮箱获取*/
	public static final String EMAIL_EMAIL_RAND = "_EMAIL_EMAIL_RAND";
	/** 邮箱验证码 通过手机获取*/
	public static final String EMAIL_MOBILE_RAND = "_EMAIL_MOBILE_RAND";
	/** 邮箱验证码 认证*/
	public static final String EMAIL_RAND = "_EMAIL_RAND";
	
	/**手机验证码次数*/
	public static final String MOBILE_NUM = "_MOBILE_NUM";
	
	public static final String REG_RAND_WRONG_CNT = "_REG_RAND_WRONG_CNT";
	public static final String COUPON_WRONG_CNT = "_COUPON_WRONG_CNT";

	/** 微信用户的openId **/
	public static final String WX_OPENID = "_WX_OPENID";
	
	/** 信分宝授信信息*/
	public static final String XFB_Credit = "_XFB_Credit";
	
	/** 分享活动的用户名 **/
	public static final String WX_ACT_USER_NAME = "_WX_ACT_USER_NAME";

	/** 分享活动的类型 **/
	public static final String WX_ACT_TYPE = "_WX_ACT_TYPE";
	
	public static final String WX_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/***微信支付主动查询支付结果URL author:Black****/
	public static final String WX_PAY_ORDER_SYNC_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**秒杀***/
	public static final Integer ACTIVITY_TYPE_SECKILL= 1;
	/**限购***/
	public static final Integer ACTIVITY_TYPE_PURCHASE= 2;
	/**优惠卷***/
	public static final Integer ACTIVITY_TYPE_COUPON = 3;
	
    /***测试商户登录****/
	public static final String POINT_USER = "_POINT_USER";
	
	public static final Map<String , Object > pointUserMap  =  new HashMap<String,Object>();
	/****购物车 session 信息************/
	public static final String CART_INFO = "CART_INFO";
}
