package com.tyiti.easycommerce.common;

/**
 * 常量
 */
public class Constants {
	public static final String USER = "_USER"; //session用户
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
	
	/** 分享活动的用户名 **/
	public static final String WX_ACT_USER_NAME = "_WX_ACT_USER_NAME";

	/** 分享活动的类型 **/
	public static final String WX_ACT_TYPE = "_WX_ACT_TYPE";
	
	
	 /** 导出文件模板存放路径 */
    public final static String EXPORT_TEMP_PATH = "/report/";
}
