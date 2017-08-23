package com.tyiti.easycommerce.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import net.sf.json.JSONObject;

import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.Md5;

public abstract class JdAccessToken {

	// 京东地址
	private final static String jdUrl = "https://kploauth.jd.com/oauth/token?";
	
	// 写死为字符串"password"  刷新"refresh_token"
	private static String grantType = "password";
	// 可填任何值
	private static String state = "0";
	
	private static String accessToken = "";

	private static Date expiresTime = new Date();

	public static String getAccessToken(Map<String, Object> map) {

		if (!"".equals(accessToken)) {
			// 系统当前时间
			Date date = new Date();
			// 判断时间是否过期
			boolean flag = expiresTime.before(date);
			if (flag) {
				// 失效,重新获取并设置
				refreshAccessToken("refresh_token", map);
			}
		} else {

			refreshAccessToken(grantType, map);
		}
		return accessToken;

	}
	
	/**
	 * 如果京东响应 access token 过期或失效，更新为无效
	 */
	public static void invalidateAccessToken(Map<String, Object> map) {
		refreshAccessToken("refresh_token", map);
	}

	/**
	 * 获取京东AccessToken并保存k
	 */
	private static void refreshAccessToken(String grantType,
			Map<String, Object> map) {
		Md5 md5 = new Md5();
		// 系统当前时间
		Date date = new Date();
		String jdAccessTokenUrl = jdUrl + "grant_type=" + grantType
				+ "&app_key=" + map.get("jdAPPKey") + "&app_secret="
				+ map.get("jdAPPSecret") + "&state=" + state + "&username="
				+ map.get("jdName") + "&password="
				+ md5.getMD5ofStr(map.get("jdPassWord").toString());
		StringBuffer buffer = HttpClientUtil.httpsRequest(jdAccessTokenUrl,
				"GET", "");

		JSONObject msgStr = JSONObject.fromObject(buffer.toString());

		// jdAccessToken = buffer.toString();

		if (msgStr.getString("code") != null && msgStr.getString("code") != "") {

			Integer code = Integer.parseInt(msgStr.getString("code"));

			// access_token过期
			if (code == 1004) {
				// 刷新access_token
				refreshAccessToken("refresh_token", map);
			} else {
				if (code == 0) {
					// 微信的凭证有效时间，单位：秒
					Integer jdExpiresIn = Integer.parseInt(msgStr
							.getString("expires_in"));
					Calendar c = new GregorianCalendar();
					c.setTime(date);// 设置参数时间
					c.add(Calendar.MILLISECOND, jdExpiresIn - 1);// 把日期往后增加SECOND，微信返回的时间减一毫秒计算
					// 秒.整数往后推,负数往前移动
					date = c.getTime(); // 这个时间就是日期处理后的结果
					// 获取到的凭证
					accessToken = msgStr.getString("access_token");
					// 过期时间
					expiresTime = date;
				} else {
					accessToken = "error,code=" + code;
				}
			}
		} else {
			accessToken = "error";
		}
	}
}