package com.tyiti.easycommerce.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import net.sf.json.JSONObject;

import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.Md5;
import com.tyiti.easycommerce.util.oss.Policy;

/**
 * 获取京东token
 * @author shenzhiqiang
 * @ClassName: JdAccessToken 
 * @Description: 
 * @date 2016年6月13日 下午3:56:38
 */
public abstract class JdAccessToken {

	private final static String jdUrl = "https://kploauth.jd.com/oauth/token?";
	private static String accessToken = "";
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
	 * @throws IOException 
	 */
	private static void refreshAccessToken() {
		Properties props = new Properties();
		InputStream inputStream;        
		ClassLoader cl = Policy. class .getClassLoader();   
		inputStream = cl.getResourceAsStream( "config.properties" );  
		try {
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 系统当前时间
		Date date = new Date();
		String jdAccessTokenUrl = jdUrl + "grant_type=password&app_key="+
		props.getProperty("jd_app_key")+"&app_secret="+props.getProperty("jd_app_secret")+
		"&state=0&username="+props.getProperty("jd_username")+"&password="+new Md5().getMD5ofStr(props.getProperty("jd_password"));
		System.out.println(jdAccessTokenUrl);
		StringBuffer buffer = HttpClientUtil.httpsRequest(jdAccessTokenUrl,
				"GET", "");

		JSONObject msgStr = JSONObject.fromObject(buffer.toString());
		if (msgStr.getString("access_token") != null
				&& msgStr.getString("access_token") != "") {
			// 微信的凭证有效时间，单位：秒
			Integer jdExpiresIn = Integer.parseInt(msgStr
					.getString("expires_in"));
			Calendar c = new GregorianCalendar();
			c.setTime(date);// 设置参数时间
			c.add(Calendar.SECOND, jdExpiresIn - 1);// 把日期往后增加SECOND，微信返回的时间减一秒计算
													// 秒.整数往后推,负数往前移动
			date = c.getTime(); // 这个时间就是日期处理后的结果
			// 获取到的凭证
			accessToken = msgStr.getString("access_token");
			// 过期时间
			expiresTime = date;
		}else{
			accessToken="error";	
		}

	}

}