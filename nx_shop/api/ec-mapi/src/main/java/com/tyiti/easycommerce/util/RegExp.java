package com.tyiti.easycommerce.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则检查工具
 * @author rainyhao
 * @since 2015-3-10 上午10:34:25
 */
public final class RegExp {
	
	/**
	 * 正整数正则表达式
	 */
	public static final String SRE_DIGIT = "^\\d+$";
	
	/**
	 * 浮点数正则表达式
	 */
	public static final String SRE_FLOAT = "^(-?\\d+)(\\.\\d+)?$";
	
	/**
	 * 电子邮件正则表达式
	 */
	public static final String SRE_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	public static final String SRE_MOBILE = "((1[3|4|5|7|8][0-9]{1})+\\d{8})";
	
	// 正整数
	public static final Pattern DIGIT = Pattern.compile(SRE_DIGIT);
	
	// 浮点数
	public static final Pattern FLOAT = Pattern.compile(SRE_FLOAT);
	
	// email格式
	public static final Pattern EMAIL = Pattern.compile(SRE_EMAIL);
	
	// 手机号
	public static final Pattern MOBILE = Pattern.compile(SRE_MOBILE);
	
	/**
	 * 检查字符串是否为正整数
	 * @author rainyhao 
	 * @since 2015-3-10 上午10:39:19
	 * @param number 数字形式的字符串
	 * @return
	 */
	public static final boolean isDigit(String number) {
		Matcher m = DIGIT.matcher(number);
		return m.matches();
	}
	
	/**
	 * 检查是否为浮点数(不分正负)
	 * @author rainyhao 
	 * @since 2015-3-10 上午10:43:05
	 * @param number 字符形式的字符串
	 * @return
	 */
	public static final boolean isFloat(String number) {
		Matcher m = FLOAT.matcher(number);
		return m.matches();
	}
	
	/**
	 * 检查所输入的字符串是不为email格式
	 * @author rainyhao 
	 * @since 2015-3-23 下午1:18:11
	 * @param email 要检查的字符串
	 * @return
	 */
	public static final boolean isEmail(String email) {
		Matcher m = EMAIL.matcher(email);
		return m.matches();
	}
	
	/**
	 * 检查所输入的字符串是否为mobile格式
	 * @param mobile 要检查的字符串
	 * @return
	 * @author hanyao
	 * @since 2015年7月2日 上午10:16:03
	 */
	public static final boolean isMobile(String mobile) {
		Matcher m = MOBILE.matcher(String.valueOf(mobile));
		return m.matches();
	}
	
	/**
	 * 校验传过来的多个值是否都是数字
	 * 各值用逗号','分隔
	 * @param digits
	 * @return
	 */
	public static boolean isAllDigit(String digits){
		String[] all_sta = digits.split(",");
		for(String num : all_sta){
			if(!isDigit(num)){
				return false;
			}
		}
		return true;
	}
}