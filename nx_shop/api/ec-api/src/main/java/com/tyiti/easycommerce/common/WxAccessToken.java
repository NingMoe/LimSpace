package com.tyiti.easycommerce.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.tyiti.easycommerce.util.HttpClientUtil;

public abstract class WxAccessToken {

	private static Log log = LogFactory.getLog(WxAccessToken.class);

	private final static String wxUrl = "https://api.weixin.qq.com/cgi-bin/token?";
	private static String accessToken = "";
	// private static Integer expiresIn = 7200;
	private static Date expiresTime = new Date();

	public static String getAccessToken() {
		if (!"".equals(accessToken)) {
			// 系统当前时间
			Date date = new Date();
			// 判断时间是否过期
			boolean flag = expiresTime.before(date);
			if (flag) {
				// 失效,重新获取并设置
				refreshAccessToken();
			}
		} else {
			refreshAccessToken();
		}
		return accessToken;
	}

	/**
	 * 如果微信响应 access token 过期或失效，更新为无效
	 */
	public static void invalidateAccessToken() {
		refreshAccessToken();
	}

	/**
	 * 获取微信AccessToken并保存
	 */
	private static void refreshAccessToken() {
		// 系统当前时间
		Date date = new Date();
		String wxAccessTokenUrl = wxUrl + "grant_type=client_credential&appid="
				+ SysConfig.configMap.get(ConfigKey.WX_APPID) + "&secret="
				+ SysConfig.configMap.get(ConfigKey.WX_APPSECRET);

		StringBuffer buffer = HttpClientUtil.httpsRequest(wxAccessTokenUrl,
				"GET", "");

		log.info("前端获取微信的access_token：" + buffer.toString());

		try {
			JSONObject msgStr = JSONObject.fromObject(buffer.toString());
			if (msgStr.getString("access_token") != null
					&& msgStr.getString("access_token") != "") {
				// 微信的凭证有效时间，单位：秒
				Integer wxExpiresIn = Integer.parseInt(msgStr
						.getString("expires_in"));
				Calendar c = new GregorianCalendar();
				c.setTime(date);// 设置参数时间
				c.add(Calendar.SECOND, wxExpiresIn - 1);// 把日期往后增加SECOND，微信返回的时间减一秒计算
														// 秒.整数往后推,负数往前移动
				date = c.getTime(); // 这个时间就是日期处理后的结果
				// 获取到的凭证
				accessToken = msgStr.getString("access_token");
				// 过期时间
				expiresTime = date;
			} else {
				accessToken = "error";
			}
		} catch (Exception e) {
			accessToken = "error";
		}
	}

}